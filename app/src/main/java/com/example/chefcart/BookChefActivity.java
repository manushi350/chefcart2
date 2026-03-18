package com.example.chefcart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class BookChefActivity extends AppCompatActivity {

    ListView listViewChefs;
    ArrayList<String> chefList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_chef);

        listViewChefs = findViewById(R.id.listViewChefs);
        chefList = new ArrayList<>();

        // Sample chefs (This can later be fetched from a database)
        chefList.add("Name: Chef Isha Oliver\n" + "Cuisine: Italian Cuisine\n" + "Experience: 10 Years\n" + "Price: ₹3000/Day\n");
        chefList.add("Name: Chef Devarsh Totrino\n" + "Cuisine: Indian Cuisine\n" + "Experience: 8 Years\n" + "Price: ₹2500/Day\n");
        chefList.add("Name: Chef Diya Bottura\n" + "Cuisine: Chinese Cuisine\n" + "Experience: 5 Years\n" + "Price: ₹1800/Day\n");
        chefList.add("Name: Chef Manushi Killara\n" + "Cuisine: Mexican Cuisine\n" + "Experience: 7 Years\n" + "Price: ₹3500/Day\n");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chefList);
        listViewChefs.setAdapter(adapter);

        // Handle chef selection
        listViewChefs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedChef = chefList.get(position);

                // Open Booking Form with Selected Chef
                Intent intent = new Intent(BookChefActivity.this, ConfirmBookingActivity.class);
                intent.putExtra("selectedChef", selectedChef);
                startActivity(intent);
            }
        });
    }
}
