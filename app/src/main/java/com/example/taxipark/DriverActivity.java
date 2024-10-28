package com.example.taxipark;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DriverActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button addDriverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers);

        recyclerView = findViewById(R.id.recyclerView);
        addDriverButton = findViewById(R.id.addDriverButton);

        // Set up RecyclerView (you will need to create an adapter)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setAdapter(new DriversAdapter(driverList)); // Set your adapter here

        addDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle adding new driver logic here, e.g., open a dialog or another activity
            }
        });
    }
}