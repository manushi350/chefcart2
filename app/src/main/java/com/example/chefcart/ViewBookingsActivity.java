package com.example.chefcart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ViewBookingsActivity extends AppCompatActivity {

    ListView listViewBookings;
    DatabaseHelper dbHelper;
    String userEmail = "test@example.com"; // Replace with logged-in user email
    ArrayList<String> bookingsList;
    ArrayList<Integer> bookingIds;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);

        listViewBookings = findViewById(R.id.listViewBookings);
        dbHelper = new DatabaseHelper(this);

        loadBookings();

        // Handle booking cancellation on item click
        listViewBookings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showCancelConfirmationDialog(bookingIds.get(position));
            }
        });
    }

    private void loadBookings() {
        bookingsList = new ArrayList<>();
        bookingIds = new ArrayList<>();
        Cursor cursor = dbHelper.getUserBookings(userEmail);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No bookings found!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                int bookingId = cursor.getInt(0);
                String booking = "\nChef: " + cursor.getString(2) +
                        "\nEvent: " + cursor.getString(4) +
                        "\nDate: " + cursor.getString(5) +
                        "\nContact Number: " + cursor.getString(6) +
                        "\nAddress: " + cursor.getString(7);

                bookingsList.add(booking);
                bookingIds.add(bookingId);
            }
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookingsList);
        listViewBookings.setAdapter(adapter);
    }

    private void showCancelConfirmationDialog(final int bookingId) {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelBooking(bookingId);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelBooking(int bookingId) {
        boolean success = dbHelper.cancelBooking(bookingId);
        if (success) {
            Toast.makeText(this, "Booking canceled successfully", Toast.LENGTH_SHORT).show();
            loadBookings(); // Refresh list after deletion
        } else {
            Toast.makeText(this, "Failed to cancel booking", Toast.LENGTH_SHORT).show();
        }
    }
}