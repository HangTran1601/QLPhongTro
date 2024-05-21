package com.example.quanlyphongtro.Customer.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.quanlyphongtro.Customer.Model.CustomerModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerSqlite extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "md253.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CUSTOMERS = "customers";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CCCD = "cccd";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_JOB = "job";


    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_CUSTOMERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_CCCD + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_JOB + " TEXT" +
                    ");";

    public CustomerSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("CustomerSqlite", "Database upgraded to version: ");
    }
    public long addCustomer(CustomerModel customerModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, customerModel.getName());
        values.put(COLUMN_CCCD, customerModel.getCccd());
        values.put(COLUMN_PHONE, customerModel.getPhone());
        values.put(COLUMN_ADDRESS, customerModel.getAddress());
        values.put(COLUMN_JOB, customerModel.getJob());
        long id = db.insert(TABLE_CUSTOMERS, null, values);
        return id;
    }


    // Phương thức để xóa một user bằng ID
    public void deleteCustomer(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CUSTOMERS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

    }
    public CustomerModel getCustomerById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOMERS, null,
                COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);

        CustomerModel customer = null;

        if (cursor.moveToFirst()) {
            customer = new CustomerModel();
            customer.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            customer.setCccd(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CCCD)));
            customer.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
            customer.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)));
            customer.setJob(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB)));
        }

        cursor.close();
        return customer;
    }

    public List<CustomerModel> getListCustomerById(String idList) {
        List<CustomerModel> customers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Chia chuỗi id thành các id riêng lẻ
        String[] ids = idList.split("/");

        // Duyệt qua từng id và truy vấn dữ liệu từ cơ sở dữ liệu
        for (String id : ids) {
            Cursor cursor = db.query(TABLE_CUSTOMERS, null,
                    COLUMN_ID + "=?", new String[]{id},
                    null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    CustomerModel customer = new CustomerModel();
                    customer.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                    customer.setCccd(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CCCD)));
                    customer.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                    customer.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)));
                    customer.setJob(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB)));
                    customers.add(customer);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        return customers;
    }
    public int updateCustomerById(long id, CustomerModel customerModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, customerModel.getName());
        values.put(COLUMN_CCCD, customerModel.getCccd());
        values.put(COLUMN_PHONE, customerModel.getPhone());
        values.put(COLUMN_ADDRESS, customerModel.getAddress());
        values.put(COLUMN_JOB, customerModel.getJob());

        return db.update(TABLE_CUSTOMERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    public long getCustomerIdByNameAndPhone(String name, String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        long customerId = -1; // Giá trị mặc định nếu không tìm thấy

        Cursor cursor = db.query(TABLE_CUSTOMERS, new String[]{COLUMN_ID},
                COLUMN_NAME + "=? AND " + COLUMN_PHONE + "=?",
                new String[]{name, phone}, null, null, null);

        if (cursor.moveToFirst()) {
            customerId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }

        cursor.close();
        return customerId;
    }
    public void deleteCustomerById(long customerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CUSTOMERS, COLUMN_ID + " = ?", new String[]{String.valueOf(customerId)});
    }

    public boolean isPhoneNumberExists(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOMERS, new String[]{COLUMN_ID},
                COLUMN_PHONE + "=?", new String[]{phone},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Kiểm tra xem bảng "customers" có tồn tại không
        if (db.getVersion() < newVersion) {
            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + TABLE_CUSTOMERS + "'", null);
            if (cursor.getCount() == 0) {
                // Bảng không tồn tại, tạo mới
                Log.d("CustomerSqlite", "Customers Table Upgraded (Created)");  // Add a log statement
                onCreate(db);
            } else {
                // Bảng tồn tại, thực hiện mọi logic nâng cấp cần thiết ở đây
                // (ví dụ, thêm các cột mới nếu cần)
            }
            cursor.close(); //đóng
        }

    }

}
