package com.example.quanlyphongtro.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlyphongtro.Customer.Adapter.RoomManagementAdapter;
import com.example.quanlyphongtro.Customer.Model.CustomerModel;
import com.example.quanlyphongtro.Customer.Model.RoomModel;
import com.example.quanlyphongtro.Customer.Sqlite.CustomerSqlite;
import com.example.quanlyphongtro.Customer.Sqlite.RoomSqlite;
import com.example.quanlyphongtro.Customer.View.MainActivity;
import com.example.quanlyphongtro.R;

import java.util.ArrayList;
import java.util.List;

public class AcceptRoomActivity extends AppCompatActivity {
    ListView listView;
    List<RoomModel> list = new ArrayList<>();
    RoomManagementAdapter adapter;
    RoomSqlite roomSqlite;
    CustomerSqlite customerSqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_room);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.lvdp);
        roomSqlite = new RoomSqlite(AcceptRoomActivity.this);
        customerSqlite = new CustomerSqlite(AcceptRoomActivity.this);
        list = roomSqlite.getRoomsByStatus("Đang chờ duyệt");
        adapter = new RoomManagementAdapter(AcceptRoomActivity.this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> showCustomerInformation(i));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();// Sử dụng finish() để quay lại hoạt động trước đó
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCustomerInformation(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.customer, null);
        builder.setView(dialogView);

        //khởi tạo
        TextView textName = dialogView.findViewById(R.id.editTextName);
        TextView textCCCD = dialogView.findViewById(R.id.editTextCCCD);
        TextView textPhone = dialogView.findViewById(R.id.editTextPhone);
        TextView textAddress = dialogView.findViewById(R.id.editTextAddress);
        TextView textJob = dialogView.findViewById(R.id.editTextJob);
        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);

        long id = roomSqlite.getCustomerIdByRoomName(list.get(pos).getRoomName());
        CustomerModel customerModel = customerSqlite.getCustomerById(id);
        if (customerModel == null) {
            Log.e("AcceptRoomActivity", "CustomerModel is null for ID: " + id);
            Toast.makeText(AcceptRoomActivity.this, "Customer information not found", Toast.LENGTH_SHORT).show();
            return;
        }
        textName.setText(customerModel.getName());
        textCCCD.setText(customerModel.getCccd());
        textPhone.setText(customerModel.getPhone());
        textAddress.setText(customerModel.getAddress());
        textJob.setText(customerModel.getJob());

        buttonSubmit.setOnClickListener(view -> {
            Toast.makeText(AcceptRoomActivity.this, "Chấp nhận thành công", Toast.LENGTH_SHORT).show();
            roomSqlite.updateRoomStatusByName(list.get(pos).getRoomName(), "Đang có người ở");
        });

        builder.setNegativeButton("Hủy Bỏ", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
