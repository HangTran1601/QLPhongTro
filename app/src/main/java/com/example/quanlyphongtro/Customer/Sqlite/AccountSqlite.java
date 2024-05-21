package com.example.quanlyphongtro.Customer.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountSqlite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "account1.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ACCOUNT = "account";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_BALANCE = "balance";

    // SQL statement to create the account table with UNIQUE constraint on email
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_ACCOUNT + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_BALANCE + " REAL NOT NULL" +
                    ");";

    public AccountSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Thực thi câu lệnh SQL để tạo bảng tài khoản
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu nó tồn tại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        // Tạo lại bảng
        onCreate(db);
    }

    // Chèn một tài khoản mới, đảm bảo không có email trùng lặp
    public long insertAccount(String email, double balance) {
        if (isEmailExists(email)) {
            return -1; // Email đã tồn tại
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_BALANCE, balance);

        return db.insert(TABLE_ACCOUNT, null, values);
    }

    // Kiểm tra xem một email đã tồn tại trong cơ sở dữ liệu chưa
    private boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACCOUNT, new String[]{COLUMN_ID},
                COLUMN_EMAIL + " = ?", new String[]{email},
                null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    // Lấy số dư từ email
    public double getBalance(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        double balance = 0;

        Cursor cursor = db.query(TABLE_ACCOUNT, new String[]{COLUMN_BALANCE},
                COLUMN_EMAIL + " = ?", new String[]{email},
                null, null, null);

        if (cursor.moveToFirst()) {
            balance = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BALANCE));
        }

        cursor.close();
        return balance;
    }

    public void updateBalance(String email, double newBalance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BALANCE, newBalance);

        db.update(TABLE_ACCOUNT, values, COLUMN_EMAIL + " = ?", new String[]{email});
    }


}
