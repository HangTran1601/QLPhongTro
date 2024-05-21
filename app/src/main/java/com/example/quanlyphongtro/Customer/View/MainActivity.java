package com.example.quanlyphongtro.Customer.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyphongtro.Admin.AcceptRoomActivity;
import com.example.quanlyphongtro.Admin.CustomerManagement;
import com.example.quanlyphongtro.Customer.Model.CustomerModel;
import com.example.quanlyphongtro.Customer.Model.RoomModel;
import com.example.quanlyphongtro.Customer.Sqlite.AccountSqlite;
import com.example.quanlyphongtro.Customer.Sqlite.RoomSqlite;
import com.example.quanlyphongtro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button btButton;
    private LinearLayout linearLayout;
    private TextView logout;
    private FirebaseAuth mAuth;
    private LinearLayout view1;
    private LinearLayout view2;
    private LinearLayout view3;
    private TextView customer;
    private TextView balance;
    private AccountSqlite accountSqlite;
    private RoomSqlite roomSqlite;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.listRoom);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        roomSqlite = new RoomSqlite(MainActivity.this);
        accountSqlite = new AccountSqlite(MainActivity.this);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListRoom.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        email = currentUser.getEmail();
        if(currentUser != null) {
            if(email != null && email.equals("admin@gmail.com")) {
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                view3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, AcceptRoomActivity.class);
                        startActivity(intent);
                    }
                });
                view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAddInformation();
                    }
                });
                view2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, CustomerManagement.class);
                        startActivity(intent);
                    }
                });
            }
        }
        customer = findViewById(R.id.customer_name);
        balance = findViewById(R.id.customer_balance);
        customer.setText("Tên tài khoản: " + currentUser.getEmail());
        balance.setText(String.valueOf("Số tiền còn lại: " + accountSqlite.getBalance(email)));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.naptien) {
            accountSqlite.updateBalance(email,500000);
            balance.setText("Số tiền còn lại:: 500000VND");
            return true;
        }
        if(id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void showAddInformation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.customaddroom, null);
        builder.setView(dialogView);

        // khởi tạo
        EditText editTextRoomName = dialogView.findViewById(R.id.editTextRoomName);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        EditText editTextElectronic = dialogView.findViewById(R.id.editTextElectronic);
        EditText editTextWater = dialogView.findViewById(R.id.editTextWater);
        EditText editTextAddress = dialogView.findViewById(R.id.editTextAddress);
        EditText editTextArea = dialogView.findViewById(R.id.editTextArea);
        EditText editTextImage = dialogView.findViewById(R.id.editTextImage);
        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);
        AlertDialog alertDialog = builder.create();
        // khi ấn vào button Xác nhận (Submit)
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roomName = editTextRoomName.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String price = editTextPrice.getText().toString().trim();
                String electronic = editTextElectronic.getText().toString().trim();
                String water = editTextWater.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String area = editTextArea.getText().toString().trim();
                String status = "Trống";
                String image = editTextImage.getText().toString().trim();
                roomSqlite.addRoom(new RoomModel(roomName,description,price,electronic,water,address,area,status,image));
                Toast.makeText(MainActivity.this, "Thêm phòng thành công", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
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
