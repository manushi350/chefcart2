package com.example.chefcart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmBookingActivity extends AppCompatActivity {

    TextView txtSelectedChef;
    EditText edtEvent, edtDate, edtContact, edtAddress;
    Button btnConfirmBooking;
    DatabaseHelper dbHelper;
    String userEmail = "test@example.com"; // Replace with logged-in user email

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        txtSelectedChef = findViewById(R.id.txtSelectedChef);
        edtEvent = findViewById(R.id.edtEvent);
        edtDate = findViewById(R.id.edtDate);
        edtContact = findViewById(R.id.edtContact);
        edtAddress = findViewById(R.id.edtAddress);

        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        dbHelper = new DatabaseHelper(this);

        // Get the selected chef from intent
        String selectedChef = getIntent().getStringExtra("selectedChef");
        txtSelectedChef.setText("Selected Chef: " + selectedChef);

        btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event = edtEvent.getText().toString();
                String date = edtDate.getText().toString();
                String contact = edtContact.getText().toString();
                String address = edtAddress.getText().toString();

                if (event.isEmpty() || date.isEmpty() || contact.isEmpty() || address.isEmpty()) {
                    Toast.makeText(ConfirmBookingActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Insert booking into the database with new fields
                    boolean inserted = dbHelper.insertBooking(userEmail, selectedChef, "N/A", event, date, contact, address);

                    if (inserted) {
                        Toast.makeText(ConfirmBookingActivity.this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ConfirmBookingActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ConfirmBookingActivity.this, "Booking Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}