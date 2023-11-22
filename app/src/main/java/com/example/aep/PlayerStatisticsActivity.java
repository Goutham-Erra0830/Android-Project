package com.example.aep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_statistics);

        //Initializing the database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("your_data_node");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BarEntry> entries = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming your data structure has "xValue" and "yValue" fields
                    String xValue = snapshot.child("xValue").getValue(String.class);
                    float yValue = snapshot.child("yValue").getValue(Float.class);

                    entries.add(new BarEntry(Float.parseFloat(xValue), yValue));
                }

                BarDataSet dataSet = new BarDataSet(entries, "Label");
                BarData barData = new BarData(dataSet);

                // Set up the chart
                BarChart barChart = findViewById(R.id.barChart);
                barChart.setData(barData);
                barChart.invalidate(); // Refresh the chart
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}