package com.example.quanlyphongtro.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlyphongtro.Customer.Adapter.CustomerAdapter;
import com.example.quanlyphongtro.Customer.Model.CustomerModel;
import com.example.quanlyphongtro.Customer.Model.RoomModel;
import com.example.quanlyphongtro.Customer.Sqlite.CustomerSqlite;
import com.example.quanlyphongtro.Customer.Sqlite.RoomSqlite;
import com.example.quanlyphongtro.Customer.View.DetailRActivity;
import com.example.quanlyphongtro.Customer.View.LoginActivity;
import com.example.quanlyphongtro.Customer.View.MainActivity;
import com.example.quanlyphongtro.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class DataCustomerActivity extends AppCompatActivity {
    RoomSqlite roomSqlite;
    CustomerSqlite customerSqlite;
    ListView listView;
    CustomerAdapter customerAdapter;
    List<CustomerModel> list;
    String idList;
    String nameRoomIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_customer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent =getIntent();
        nameRoomIntent = intent.getStringExtra("name");
        toolbar.setTitle("Danh sách người ở phòng " + nameRoomIntent);
        roomSqlite = new RoomSqlite(DataCustomerActivity.this);
        customerSqlite  = new CustomerSqlite(this);
        String idList = roomSqlite.getListCustomerIdByRoomName(nameRoomIntent);
        list = customerSqlite.getListCustomerById(idList);
        customerAdapter = new CustomerAdapter(this,list);
        listView = findViewById(R.id.listView);
        listView.setAdapter(customerAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPopupMenu(view, position);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(DataCustomerActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if(item.getItemId() == R.id.addcustomer){
            showCustomerInformation();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addcustomer, menu);
        return true;
    }
    private void showCustomerInformation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.customer, null);
        builder.setView(dialogView);

        //khởi tạo
        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextCCCD = dialogView.findViewById(R.id.editTextCCCD);
        EditText editTextPhone = dialogView.findViewById(R.id.editTextPhone);
        EditText editTextAddress = dialogView.findViewById(R.id.editTextAddress);
        EditText editTextJob = dialogView.findViewById(R.id.editTextJob);
        Button button = dialogView.findViewById(R.id.buttonSubmit);
        AlertDialog alertDialog = builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String cccd = editTextCCCD.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String job = editTextJob.getText().toString().trim();

             //kiểm tra xem có trường nào đang để trống không
                if (name.isEmpty() || cccd.isEmpty() || phone.isEmpty() || address.isEmpty() || job.isEmpty()) {
                    Toast.makeText(DataCustomerActivity.this, "Vui lòng không để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    long id = customerSqlite.addCustomer(new CustomerModel(name, cccd, phone, address, job));
                    roomSqlite.updateCustomerRoom(nameRoomIntent, id);
                    CustomerModel customerModel = new CustomerModel(name, cccd, phone, address, job);
                    list.add(customerModel); // Thêm vào danh sách trước khi cập nhật Adapter
                    customerAdapter.notifyDataSetChanged();
                    Toast.makeText(DataCustomerActivity.this, "Thêm người ở thành công!!", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });


        builder.setNegativeButton("Hủy Bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }
    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(DataCustomerActivity.this, view);
        popupMenu.inflate(R.menu.dlt);
        // Xử lý sự kiện khi một item trong PopupMenu được click
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.deletecustomer) {
                    roomSqlite.removeCustomerIdFromRoomList(nameRoomIntent,customerSqlite.getCustomerIdByNameAndPhone(list.get(position).getName(),list.get(position).getPhone()));
                    customerSqlite.deleteCustomer(customerSqlite.getCustomerIdByNameAndPhone(list.get(position).getName(),list.get(position).getPhone()));
                    list.remove(list.get(position));
                    customerAdapter.notifyDataSetChanged();
                    if(list.size() == 0){
                        roomSqlite.updateRoomStatusByName(nameRoomIntent,"Trống");
                    }
                    return true;
                }
                if(item.getItemId() == R.id.updatecustomer){
                    showCustomerEditInformation(position);
                }
                return false;
            }
        });

        // Hiển thị PopupMenu
        popupMenu.show();
    }

    private void showCustomerEditInformation(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.customer, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextCCCD = dialogView.findViewById(R.id.editTextCCCD);
        EditText editTextPhone = dialogView.findViewById(R.id.editTextPhone);
        EditText editTextAddress = dialogView.findViewById(R.id.editTextAddress);
        EditText editTextJob = dialogView.findViewById(R.id.editTextJob);
        Button button = dialogView.findViewById(R.id.buttonSubmit);
        AlertDialog alertDialog = builder.create();
        long idCustomer = customerSqlite.getCustomerIdByNameAndPhone(list.get(pos).getName(),list.get(pos).getPhone());
        CustomerModel model = customerSqlite.getCustomerById(idCustomer);
        editTextName.setText(model.getName());
        editTextPhone.setText(model.getPhone());
        editTextCCCD.setText(model.getCccd());
        editTextAddress.setText(model.getAddress());
        editTextJob.setText(model.getJob());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String cccd = editTextCCCD.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String job = editTextJob.getText().toString().trim();


                if (name.isEmpty() || cccd.isEmpty() || phone.isEmpty() || address.isEmpty() || job.isEmpty()) {
                    Toast.makeText(DataCustomerActivity.this, "Vui lòng không để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    CustomerModel customerModel = new CustomerModel(name, cccd, phone, address, job);
                    customerSqlite.updateCustomerById(idCustomer,customerModel);
                    list.set(pos,customerModel);
                    customerAdapter.notifyDataSetChanged();
                    Toast.makeText(DataCustomerActivity.this, "Thêm người ở thành công!!", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });


        builder.setNegativeButton("Hủy Bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }
}