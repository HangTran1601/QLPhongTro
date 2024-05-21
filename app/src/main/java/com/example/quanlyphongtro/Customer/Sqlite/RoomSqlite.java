package com.example.quanlyphongtro.Customer.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.example.quanlyphongtro.Customer.Model.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class RoomSqlite extends SQLiteOpenHelper {
    private static final String DB_NAME = "datab11121.db";
    private static final String TABLE_NAME = "Room";
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_ROOM_NAME = "RoomName";
    private static final String COLUMN_ROOM_DESCRIPTION = "RoomDescription";
    private static final String COLUMN_ROOM_PRICE = "RoomPrice";
    private static final String COLUMN_ROOM_ELECTRONIC = "RoomElectronic";
    private static final String COLUMN_ROOM_WATER = "RoomWater";
    private static final String COLUMN_ROOM_ADDRESS = "RoomAddress";
    private static final String COLUMN_ROOM_AREA = "RoomArea";
    private static final String COLUMN_STATUS = "Status";
    private static final String COLUMN_IMAGE = "Image";
    private static final String COLUMN_IDCUSTOMER = "IdCustomerHost";
    private static final String COLUMN_LISTIDCUSTOMER = "IdCustomerList";
    public RoomSqlite(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ROOM_NAME + " TEXT," +
                COLUMN_ROOM_DESCRIPTION + " TEXT," +
                COLUMN_ROOM_PRICE + " TEXT," +
                COLUMN_ROOM_ELECTRONIC + " TEXT," +
                COLUMN_ROOM_WATER + " TEXT," +
                COLUMN_ROOM_ADDRESS + " TEXT," +
                COLUMN_ROOM_AREA + " TEXT," +
                COLUMN_LISTIDCUSTOMER + " TEXT," + // Thêm dấu phẩy ở đây
                COLUMN_IDCUSTOMER + " INTEGER," +
                COLUMN_STATUS + " TEXT," +
                COLUMN_IMAGE + " TEXT)";

        sqLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
    public int updateRoomStatusByName(String roomName, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, newStatus);

        return db.update(TABLE_NAME, values, COLUMN_ROOM_NAME + " = ?", new String[]{roomName});
    }
    public boolean deleteRoomByName(String roomName) {
        long customerId = getCustomerIdByRoomName(roomName);
        if (customerId > 0) {
            return false;
        }
        else{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, COLUMN_ROOM_NAME + " = ? AND " + COLUMN_IDCUSTOMER + " < 0", new String[]{roomName});
            return true;
        }
    }



    public long addRoom(RoomModel room) {
        if (isRoomNameExists(room.getRoomName())) {
            return -1; // Trả về -1 nếu tên phòng đã tồn tại
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROOM_NAME, room.getRoomName());
        values.put(COLUMN_ROOM_DESCRIPTION, room.getRoomDescription());
        values.put(COLUMN_ROOM_PRICE, room.getRoomPrice());
        values.put(COLUMN_ROOM_ELECTRONIC, room.getRoomElectronic());
        values.put(COLUMN_ROOM_WATER, room.getRoomWater());
        values.put(COLUMN_ROOM_ADDRESS, room.getRoomAddress());
        values.put(COLUMN_ROOM_AREA, room.getRoomArea());
        values.put(COLUMN_STATUS, room.getStatus());
        values.put(COLUMN_IMAGE, room.getImage());
        values.put(COLUMN_IDCUSTOMER,-1);
        long result = db.insert(TABLE_NAME, null, values);
        return result;
    }

    private boolean isRoomNameExists(String roomName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ROOM_NAME},
                COLUMN_ROOM_NAME + "=?", new String[]{roomName}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public List<RoomModel> getAllAvailableRooms() {
        List<RoomModel> roomList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 'Trống'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RoomModel room = new RoomModel();
                room.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                room.setRoomName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_NAME)));
                room.setRoomDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_DESCRIPTION)));
                room.setRoomPrice(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_PRICE)));
                room.setRoomElectronic(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_ELECTRONIC)));
                room.setRoomWater(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_WATER)));
                room.setRoomAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_ADDRESS)));
                room.setRoomArea(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_AREA)));
                room.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                room.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)));
                roomList.add(room);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return roomList;
    }

    public List<RoomModel> getRoomsByStatus(String status) {
        List<RoomModel> roomList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null,
                COLUMN_STATUS + "=?", new String[]{status},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                RoomModel room = new RoomModel();
                room.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                room.setRoomName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_NAME)));
                room.setRoomDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_DESCRIPTION)));
                room.setRoomPrice(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_PRICE)));
                room.setRoomElectronic(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_ELECTRONIC)));
                room.setRoomWater(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_WATER)));
                room.setRoomAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_ADDRESS)));
                room.setRoomArea(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_AREA)));
                room.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                room.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)));
                roomList.add(room);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return roomList;
    }

    public int updateRoom(RoomModel room,long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROOM_NAME, room.getRoomName());
        values.put(COLUMN_ROOM_DESCRIPTION, room.getRoomDescription());
        values.put(COLUMN_ROOM_PRICE, room.getRoomPrice());
        values.put(COLUMN_ROOM_ELECTRONIC, room.getRoomElectronic());
        values.put(COLUMN_ROOM_WATER, room.getRoomWater());
        values.put(COLUMN_ROOM_ADDRESS, room.getRoomAddress());
        values.put(COLUMN_ROOM_AREA, room.getRoomArea());
        values.put(COLUMN_IMAGE, room.getImage());
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }
    public int updateCustomerRoom(String roomName, long newCustomerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String currentCustomerIds = getCurrentCustomerIds(roomName);

        if (currentCustomerIds.isEmpty()) {
            currentCustomerIds = String.valueOf(newCustomerId);
        } else {
            currentCustomerIds += "/" + newCustomerId;
        }

        values.put(COLUMN_LISTIDCUSTOMER, currentCustomerIds);

        return db.update(TABLE_NAME, values, COLUMN_ROOM_NAME + " = ?", new String[]{roomName});
    }

    public int removeCustomerIdFromRoomList(String roomName, long customerIdToRemove) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Lấy danh sách ID khách hàng hiện tại liên kết với phòng
        String currentCustomerIds = getCurrentCustomerIds(roomName);

        // Chia chuỗi thành mảng các số
        String[] customerIdArray = currentCustomerIds.split("/");

        // Tạo một danh sách mới để lưu trữ các ID khách hàng sau khi loại bỏ
        List<String> updatedCustomerIdList = new ArrayList<>();

        // Thêm các ID khách hàng khác nhau vào danh sách mới nếu không phải là ID cần loại bỏ
        for (String customerId : customerIdArray) {
            long id = Long.parseLong(customerId);
            if (id != customerIdToRemove) {
                updatedCustomerIdList.add(customerId);
            }
        }

        // Ghép các ID khách hàng lại thành một chuỗi mới, sử dụng dấu "/"
        String updatedCustomerIds = TextUtils.join("/", updatedCustomerIdList);

        // Cập nhật lại danh sách ID khách hàng cho phòng
        values.put(COLUMN_LISTIDCUSTOMER, updatedCustomerIds);

        return db.update(TABLE_NAME, values, COLUMN_ROOM_NAME + " = ?", new String[]{roomName});
    }

    public int updateRoomCustomerByName(String roomName, long newCustomerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IDCUSTOMER, newCustomerId);
        values.put(COLUMN_LISTIDCUSTOMER,String.valueOf(newCustomerId));
        return db.update(TABLE_NAME, values, COLUMN_ROOM_NAME + " = ?", new String[]{roomName});
    }

    public RoomModel getRoomByName(String roomName) {
        SQLiteDatabase db = this.getReadableDatabase();
        RoomModel room = null;

        Cursor cursor = db.query(TABLE_NAME, null,
                COLUMN_ROOM_NAME + "=?", new String[]{roomName},
                null, null, null);

        if (cursor.moveToFirst()) {
            room = new RoomModel();
            room.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            room.setRoomName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_NAME)));
            room.setRoomDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_DESCRIPTION)));
            room.setRoomPrice(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_PRICE)));
            room.setRoomElectronic(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_ELECTRONIC)));
            room.setRoomWater(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_WATER)));
            room.setRoomAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_ADDRESS)));
            room.setRoomArea(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_AREA)));
            room.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
            room.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)));
        }

        cursor.close();

        return room;
    }
    private String getCurrentCustomerIds(String roomName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String currentCustomerIds = "";

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_LISTIDCUSTOMER},
                COLUMN_ROOM_NAME + "=?", new String[]{roomName},
                null, null, null);

        if (cursor.moveToFirst()) {
            currentCustomerIds = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LISTIDCUSTOMER));
        }

        cursor.close();
        return currentCustomerIds;
    }
    public long getCustomerIdByRoomName(String roomName) {
        SQLiteDatabase db = this.getReadableDatabase();
        long idCustomer = -1; // Giá trị mặc định nếu không tìm thấy

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_IDCUSTOMER},
                COLUMN_ROOM_NAME + "=?", new String[]{roomName},
                null, null, null);

        if (cursor.moveToFirst()) {
            idCustomer = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_IDCUSTOMER));
        }

        cursor.close();
        return idCustomer;
    }
    public String getListCustomerIdByRoomName(String roomName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String idCustomer = ""; // Giá trị mặc định nếu không tìm thấy

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_LISTIDCUSTOMER},
                COLUMN_ROOM_NAME + "=?", new String[]{roomName},
                null, null, null);

        if (cursor.moveToFirst()) {
            idCustomer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LISTIDCUSTOMER));
        }

        cursor.close();
        return idCustomer;
    }
    public String getRoomPriceByName(String roomName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String roomPrice = null;

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ROOM_PRICE},
                COLUMN_ROOM_NAME + "=?", new String[]{roomName},
                null, null, null);

        if (cursor.moveToFirst()) {
            roomPrice = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM_PRICE));
        }

        cursor.close();
        return roomPrice;
    }

    public int getIdRoomByName(String roomName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int roomId = -1; // Giá trị mặc định nếu không tìm thấy

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID},
                COLUMN_ROOM_NAME + "=?", new String[]{roomName},
                null, null, null);

        if (cursor.moveToFirst()) {
            roomId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }

        cursor.close();
        return roomId;
    }
}
