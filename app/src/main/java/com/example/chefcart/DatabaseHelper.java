package com.example.chefcart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chef cart.db";
    private static final int DATABASE_VERSION = 2; // Updated version from 1 to 2

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String createUsersTable = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT)";
        db.execSQL(createUsersTable);

        // Create Bookings table
        String createBookingsTable = "CREATE TABLE bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_email TEXT, " +
                "chef_name TEXT, " +
                "cuisine TEXT, " +
                "event TEXT, " +
                "date TEXT, " +
                "contact TEXT, " +
                "address TEXT, " +
                "FOREIGN KEY(user_email) REFERENCES users(email))";
        db.execSQL(createBookingsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add missing columns if database was created before adding contact & address
            db.execSQL("ALTER TABLE bookings ADD COLUMN contact TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE bookings ADD COLUMN address TEXT DEFAULT ''");
        }
    }

    // Insert user for signup
    public boolean insertUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);

        long result = db.insert("users", null, values);
        return result != -1;
    }

    // Check user credentials for login
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND password=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Insert chef booking
    public boolean insertBooking(String userEmail, String chefName, String cuisine, String event, String date, String contact, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_email", userEmail);
        values.put("chef_name", chefName);
        values.put("cuisine", cuisine);
        values.put("event", event);
        values.put("date", date);
        values.put("contact", contact);
        values.put("address", address);

        long result = db.insert("bookings", null, values);
        return result != -1;
    }

    // Retrieve all bookings for a specific user
    // Retrieve all bookings for a specific user
    public Cursor getUserBookings(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("DatabaseHelper", "Fetching bookings for email: " + userEmail);

        return db.rawQuery(
                "SELECT * FROM bookings WHERE LOWER(user_email) = ?",
                new String[]{userEmail.trim().toLowerCase()}
        );
    }


    // Cancel a booking
    public boolean cancelBooking(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("bookings", "id=?", new String[]{String.valueOf(bookingId)});
        return rowsDeleted > 0;
    }
}
