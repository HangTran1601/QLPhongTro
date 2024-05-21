package com.example.quanlyphongtro.Customer.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyphongtro.Customer.Model.CustomerModel;
import com.example.quanlyphongtro.Customer.Model.RoomModel;
import com.example.quanlyphongtro.Customer.Sqlite.AccountSqlite;
import com.example.quanlyphongtro.Customer.Sqlite.CustomerSqlite;
import com.example.quanlyphongtro.Customer.Sqlite.RoomSqlite;
import com.example.quanlyphongtro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class DetailRActivity extends AppCompatActivity {

    private ImageView roomImageView;
    private TextView roomTitleTextView, roomDescriptionTextView, roomPriceTextView, roomElectronicTextView, roomWaterTextView, roomStatusTextView;
    private RoomSqlite roomSqlite;
    private Button bookButton;
    private CustomerSqlite customerSqlite;
    private String roomName;
    private AccountSqlite accountSqlite;
    private String emailUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custominformationroom);

        initViews();
        setupToolbar();
        accountSqlite = new AccountSqlite(this);
        roomSqlite = new RoomSqlite(this);
        customerSqlite = new CustomerSqlite(this);
        Intent intent = getIntent();
        roomName = intent.getStringExtra("name");
        loadRoomDetails(roomName);

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomerInformation();
            }
        });
    }

    private void initViews() {
        roomImageView = findViewById(R.id.room_image);
        roomTitleTextView = findViewById(R.id.room_title);
        roomDescriptionTextView = findViewById(R.id.room_description);
        roomPriceTextView = findViewById(R.id.room_price);
        roomElectronicTextView = findViewById(R.id.room_electricity);
        roomWaterTextView = findViewById(R.id.room_water);
        roomStatusTextView = findViewById(R.id.room_status);
        bookButton = findViewById(R.id.book_button);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        emailUpdate = currentUser.getEmail();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadRoomDetails(String roomName) {
        RoomModel room = roomSqlite.getRoomByName(roomName);
        if (room != null) {
            Picasso.get().load(room.getImage()).into(roomImageView);
            roomTitleTextView.setText("Thông tin phòng chi tiết");
            roomDescriptionTextView.setText(room.getRoomName());
            roomPriceTextView.setText("Giá phòng: " + room.getRoomPrice());
            roomElectronicTextView.setText("Số điện: " + room.getRoomElectronic());
            roomWaterTextView.setText("Số nước: " + room.getRoomWater());
            roomStatusTextView.setText("Trạng thái: " + room.getStatus());
        }
    }

    private void updateStatusAndCustomer(String roomName,long idCustomer) {
        roomSqlite.updateRoomStatusByName(roomName, "Đang chờ duyệt");
        roomSqlite.updateRoomCustomerByName(roomName,idCustomer);
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

               //kiểm tra xem có trường nào để trống hay không
                if (name.isEmpty() || cccd.isEmpty() || phone.isEmpty() || address.isEmpty() || job.isEmpty()) {
                    Toast.makeText(DetailRActivity.this, "Vui lòng không để trống!", Toast.LENGTH_SHORT).show();
                } else if(customerSqlite.isPhoneNumberExists(phone)){
                    Toast.makeText(DetailRActivity.this, "Số điện thoại đã tồn tại!", Toast.LENGTH_SHORT).show();
                } else {
                    long id = customerSqlite.addCustomer(new CustomerModel(name, cccd, phone, address, job));
                    Toast.makeText(DetailRActivity.this, id + " " , Toast.LENGTH_SHORT).show();
                    updateStatusAndCustomer(roomName, id);
                    double beforeBalance = accountSqlite.getBalance(emailUpdate);
                    double roomPrice = Double.parseDouble(roomSqlite.getRoomPriceByName(roomName).replace("VND",""));
                    if (beforeBalance < roomPrice) {
                        Toast.makeText(DetailRActivity.this, "Không đủ số dư để đặt phòng!", Toast.LENGTH_SHORT).show();
                    } else {
                        accountSqlite.updateBalance(emailUpdate, beforeBalance - roomPrice);
                        Toast.makeText(DetailRActivity.this, "Đặt phòng thành công!!", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, ListRoom.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
