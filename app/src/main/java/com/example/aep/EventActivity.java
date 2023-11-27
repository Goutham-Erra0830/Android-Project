package com.example.aep;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aep.EventAdapter;
import com.example.aep.EventData;
import com.example.aep.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
//import com.jakewharton.threetenabp.AndroidThreeTen;

public class EventActivity extends AppCompatActivity {

    private EventAdapter adapter1, adapter2, adapter3;
    private List<EventData> pastEvents, presentEvents, futureEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        //AndroidThreeTen.init(this);

        // Initialize data for RecyclerViews
        pastEvents = new ArrayList<>();
        presentEvents = new ArrayList<>();
        futureEvents = new ArrayList<>();

        // Initialize RecyclerViews
        RecyclerView recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter1 = new EventAdapter(pastEvents);
        recyclerView1.setAdapter(adapter1);

        RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter2 = new EventAdapter(presentEvents);
        recyclerView2.setAdapter(adapter2);

        RecyclerView recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter3 = new EventAdapter(futureEvents);
        recyclerView3.setAdapter(adapter3);

        fetchDataFromFirebase();
    }

    private void fetchDataFromFirebase() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("events").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId();
                            Log.d("database data", "Document ID: " + documentId);

                            // For each document, retrieve its subcollections
                            retrieveSubcollections(documentId);
                            Log.d("database data", "After function call");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("EventActivity", "Error getting events", task.getException());
                    }
                });
    }

    private void retrieveSubcollections(String documentId) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("events").document(documentId).get()
                .addOnCompleteListener(subcollectionTask -> {
                    if (subcollectionTask.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = subcollectionTask.getResult();

                        // Check if the document exists
                        if (documentSnapshot.exists()) {
                            String teamADate = documentSnapshot.getString("TeamA.eventDate");

                            // Parse the event date from the database
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                            LocalDate eventLocalDate = LocalDate.parse(teamADate, formatter);

                            // Compare the event date with the current date
                            LocalDate currentLocalDate = LocalDate.now();
                            // Log.d("Date", String.valueOf(currentDate));
                            //Log.d("Date", "database "+String.valueOf(eventDate));

                            //Log.d("Date", "comparision "+String.valueOf(eventDate.before(currentDate)));


                            if (eventLocalDate != null) {
                                if (eventLocalDate.isBefore(currentLocalDate)) {
                                    // Event is in the past
                                    pastEvents.add(new EventData((
                                            documentSnapshot.getString("TeamA.teamName")+ " vs "+ documentSnapshot.getString("TeamB.teamName")),
                                            documentSnapshot.getString("TeamA.eventLocation"),
                                            documentSnapshot.getString("TeamA.eventDate"),
                                            documentSnapshot.getString("TeamA.eventTime")
                                    ));
                                    adapter1.notifyDataSetChanged();
                                } else if (eventLocalDate.equals(currentLocalDate)) {
                                    // Event is today
                                    presentEvents.add(new EventData((
                                            documentSnapshot.getString("TeamA.teamName")+ " vs "+ documentSnapshot.getString("TeamB.teamName")),
                                            documentSnapshot.getString("TeamA.eventLocation"),
                                            documentSnapshot.getString("TeamA.eventDate"),
                                            documentSnapshot.getString("TeamA.eventTime")
                                    ));
                                    adapter2.notifyDataSetChanged();
                                } else {
                                    // Event is in the future
                                    futureEvents.add(new EventData((
                                            documentSnapshot.getString("TeamA.teamName")+ " vs "+ documentSnapshot.getString("TeamB.teamName")),
                                            documentSnapshot.getString("TeamA.eventLocation"),
                                            documentSnapshot.getString("TeamA.eventDate"),
                                            documentSnapshot.getString("TeamA.eventTime")
                                    ));
                                    adapter3.notifyDataSetChanged();
                                }
                            }

                        } else {
                            Log.d("database data", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("EventActivity", "Error getting document", subcollectionTask.getException());
                    }
                });
    }
}
