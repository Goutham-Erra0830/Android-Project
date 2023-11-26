package com.example.aep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.palettes.RangeColors;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    private EventAdapter eventAdapter;
    private List<EventData> eventDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Fetch and Display Events from Firebase
        fetchDataFromFirebase();

        eventDataList = new ArrayList<>();

        // Set up RecyclerView with horizontal LinearLayoutManager
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        eventAdapter = new EventAdapter(eventDataList);
        recyclerView.setAdapter(eventAdapter);

    }

    private void fetchDataFromFirebase() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("events").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId();
                            Log.d("database data", "Document ID: " + documentId);

                            // For each document, retrieve its subcollections
                            retrieveSubcollections(documentId);
                            Log.d("database data", "After function call");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("PlayerStatisticsActivity", "Error getting TotalHalfCenturies", task.getException());
                    }
                });
    }


    private void retrieveSubcollections(String documentId) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("events").document(documentId).get()
                .addOnCompleteListener(subcollectionTask -> {
                    if (subcollectionTask.isSuccessful()) {

                        // Assuming "subcollection_name" is the name of your subcollection
                        DocumentSnapshot documentSnapshot = subcollectionTask.getResult();

                        // Check if the document exists
                        if (documentSnapshot.exists()) {
                            // Accessing values for TeamA
                            String teamAName = documentSnapshot.getString("TeamA.teamName");
                            String teamALocation = documentSnapshot.getString("TeamA.eventLocation");
                            String teamADate = documentSnapshot.getString("TeamA.eventDate");
                            String teamATime = documentSnapshot.getString("TeamA.eventTime");

                            Log.d("database data", "Team A Name: " + teamAName);
                            Log.d("database data", "Team A Location: " + teamALocation);
                            Log.d("database data", "Team A Date: " + teamADate);
                            Log.d("database data", "Team A Time: " + teamATime);

                            // Accessing values for TeamB
                            String teamBName = documentSnapshot.getString("TeamB.teamName");
                            String teamBLocation = documentSnapshot.getString("TeamB.eventLocation");
                            String teamBDate = documentSnapshot.getString("TeamB.eventDate");
                            String teamBTime = documentSnapshot.getString("TeamB.eventTime");

                            Log.d("database data", "Team B Name: " + teamBName);
                            Log.d("database data", "Team B Location: " + teamBLocation);
                            Log.d("database data", "Team B Date: " + teamBDate);
                            Log.d("database data", "Team B Time: " + teamBTime);
                            EventData teamAEventData = new EventData(teamAName+" vs "+teamBName, teamALocation, teamADate, teamATime);
                           // EventData teamBEventData = new EventData(teamBName, teamBLocation, teamBDate, teamBTime);

                            eventDataList.add(teamAEventData);
                           // eventDataList.add(teamBEventData);

                            // Notify the adapter that the data has changed
                            eventAdapter.notifyDataSetChanged();

                        } else {
                            Log.d("database data", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("database data", "Error getting document", subcollectionTask.getException());
                    }
                });


    }
    private List<Event> getEvents() {
        // Replace this with your logic to fetch upcoming events
        // Return a list of events
        return new ArrayList<>();
    }
}