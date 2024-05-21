package com.example.quanlyphongtro.Customer.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quanlyphongtro.Customer.Model.RoomModel;
import com.example.quanlyphongtro.Customer.Adapter.RoomAdapter;
import com.example.quanlyphongtro.Customer.Sqlite.RoomSqlite;
import com.example.quanlyphongtro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ListRoom extends AppCompatActivity {
    private RoomSqlite roomSqlite;
    private List<RoomModel> list = new ArrayList<>();
    private RoomAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listView);

        roomSqlite = new RoomSqlite(ListRoom.this);

        // Khởi tạo dữ liệu phòng chỉ khi danh sách trống để tránh sự trùng lặp
            RoomModel roomModel = new RoomModel("Phòng 101", "Phòng đẹp, thoáng mát", "2000VND", "3000VND", "4000VND", "147 Vĩnh Thanh , Rạch Giá , Kiên Giang", "200m^2", "Trống", "https://imperiaskygardens.com/wp-content/uploads/2023/01/unnamed-3.jpg");
            RoomModel roomModel1 = new RoomModel("Phòng 102", "Phòng đẹp, thoáng mát,có ban công", "2000VND", "3000VND", "4000VND", "158 Chu Văn An , Rạch Giá , Kiên Giang", "200m^2", "Trống", "https://pt123.cdn.static123.com/images/thumbs/900x600/fit/2021/11/16/cho-thue-phong-tro-1613975723_1637034014.jpg");
            RoomModel roomModel2 = new RoomModel("Phòng 103", "Phòng rộng rãi", "4000VND", "3000VND", "4000VND", "100 Đống Đa, Rạch Giá , Kiên Giang", "20m^2", "Trống", "https://blogcdn.muaban.net/wp-content/uploads/2022/06/28183438/cach-trang-tri-phong-tro-co-gac-lung-16.jpg");
            RoomModel roomModel3 = new RoomModel("Phòng 104", "Phòng đẹp", "5000VND", "3000VND", "4000VND", "9/1 Lê Lai, Rạch Giá , Kiên Giang", "30m^2", "Trống", "https://trongoixaynha.com/wp-content/uploads/2021/12/xay-phong-tro-31-e1662519720304.jpg");

            roomSqlite.addRoom(roomModel);
            roomSqlite.addRoom(roomModel1);
            roomSqlite.addRoom(roomModel2);
             roomSqlite.addRoom(roomModel3);


        list = roomSqlite.getAllAvailableRooms();
        adapter = new RoomAdapter(ListRoom.this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ListRoom.this, DetailRActivity.class);
                intent.putExtra("name", list.get(position).getRoomName());
                startActivity(intent);
            }
        });
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();
        if(email.equals("admin@gmail.com")){
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showPopupMenu(view, position);
                }
            });
        }
    }
    private void showPopupMenu(View view, final int position) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.crudmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.update) {
                    showUpdateInformation(position);
                    return true;
                } else if (itemId == R.id.delete) {
                    if(roomSqlite.deleteRoomByName(list.get(position).getRoomName())){
                        Toast.makeText(ListRoom.this, "Xóa phòng thành công", Toast.LENGTH_SHORT).show();
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(ListRoom.this, "Phòng đang được cho thuê không thể xóa", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }
    private void showUpdateInformation(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.updateroom, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        // Khởi tạo các phần tử của hộp thoại
        EditText editTextRoomName = dialogView.findViewById(R.id.editTextRoomName);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        EditText editTextElectronic = dialogView.findViewById(R.id.editTextElectronic);
        EditText editTextWater = dialogView.findViewById(R.id.editTextWater);
        EditText editTextAddress = dialogView.findViewById(R.id.editTextAddress);
        EditText editTextArea = dialogView.findViewById(R.id.editTextArea);
        EditText editTextImage = dialogView.findViewById(R.id.editTextImage);
        RoomModel roomModel  = list.get(pos);
        editTextRoomName.setText(roomModel.getRoomName());
        editTextDescription.setText(roomModel.getRoomDescription());
        editTextPrice.setText(roomModel.getRoomPrice());
        editTextWater.setText(roomModel.getRoomWater());
        editTextImage.setText(roomModel.getImage());
        editTextArea.setText(roomModel.getRoomArea());
        editTextElectronic.setText(roomModel.getRoomElectronic());
        editTextAddress.setText(roomModel.getRoomAddress());
        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);

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
                int roomId = roomSqlite.getIdRoomByName(roomName);
                roomSqlite.updateRoom(new RoomModel(roomName,description,price,electronic,water,address,area,status,image),roomId);
                RoomModel updatedRoom = new RoomModel(roomName, description, price, electronic, water, address, area, status, image);
                list.set(pos, updatedRoom);
                adapter.notifyDataSetChanged();
                Toast.makeText(ListRoom.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(ListRoom.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
