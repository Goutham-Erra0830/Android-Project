package com.example.aep;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class PlanEventActivity extends AppCompatActivity {

    private EditText teamAEditText;
    private EditText teamBEditText;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText locationEditText;
    private Button scheduleButton;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_event);

        teamAEditText = findViewById(R.id.teamAEditText);
        teamBEditText = findViewById(R.id.teamBEditText);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        locationEditText = findViewById(R.id.locationEditText);
        scheduleButton = findViewById(R.id.scheduleButton);

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbarplanevent);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Plan Events");
        // Get ActionBar reference
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Set the navigation (up) button color to white
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back); // Replace with your arrow drawable
            upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);

            // Set the title text color to white
            int textColor = getResources().getColor(android.R.color.white);
            Spannable text = new SpannableString("Plan Events");
            text.setSpan(new ForegroundColorSpan(textColor), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            actionBar.setTitle(text);

            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleEvent();
            }
        });
    }

    private void scheduleEvent() {
        // Get names of TeamA and TeamB
        String teamAName = teamAEditText.getText().toString();
        String teamBName = teamBEditText.getText().toString();

        // Get selected date, time, and location
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        String location = locationEditText.getText().toString();

        // Validate input
        if (teamAName.isEmpty() || teamBName.isEmpty() || location.isEmpty() ) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve teams' data from Firestore
        retrieveTeamsData(year, month, day, hour, minute, location, teamAName, teamBName);
    }

    private void retrieveTeamsData(int year, int month, int day, int hour, int minute, String location, String teamAName, String teamBName) {
        // Retrieve TeamA data from Firestore
        db.collection("teams").document("TeamA").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> teamATask) {
                        if (teamATask.isSuccessful()) {
                            DocumentSnapshot teamADocument = teamATask.getResult();
                            if (teamADocument.exists()) {
                                // Retrieve TeamB data from Firestore
                                retrieveTeamBData(year, month, day, hour, minute, location, teamAName, teamBName, teamADocument);
                            } else {
                                Toast.makeText(PlanEventActivity.this, "Error fetching TeamA data", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PlanEventActivity.this, "Error fetching TeamA data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void retrieveTeamBData(int year, int month, int day, int hour, int minute, String location, String teamAName, String teamBName, DocumentSnapshot teamADocument) {
        db.collection("teams").document("TeamB").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> teamBTask) {
                        if (teamBTask.isSuccessful()) {
                            DocumentSnapshot teamBDocument = teamBTask.getResult();
                            if (teamBDocument.exists()) {
                                // Both TeamA and TeamB data retrieved, schedule the event
                                scheduleEventInFirestore(year, month, day, hour, minute, location, teamAName, teamBName, teamADocument, teamBDocument);
                            } else {
                                Toast.makeText(PlanEventActivity.this, "Error fetching TeamB data", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PlanEventActivity.this, "Error fetching TeamB data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void scheduleEventInFirestore(int year, int month, int day, int hour, int minute, String location, String teamAName, String teamBName, DocumentSnapshot teamADocument, DocumentSnapshot teamBDocument) {
        // Extract player data from TeamA and TeamB documents
        Map<String, Object> teamAData = teamADocument.getData();
        Map<String, Object> teamBData = teamBDocument.getData();

        // Add additional information for scheduling the event
        teamAData.put("teamName", teamAName);
        teamBData.put("teamName", teamBName);
        teamAData.put("eventDate", year + "-" + (month + 1) + "-" + day);
        teamBData.put("eventDate", year + "-" + (month + 1) + "-" + day);
        teamAData.put("eventTime", hour + ":" + minute);
        teamBData.put("eventTime", hour + ":" + minute);
        teamAData.put("eventLocation", location);
        teamBData.put("eventLocation", location);

        // Create a new collection for storing event information
        String eventId = "Event" + System.currentTimeMillis(); // Use a unique identifier for each event
        db.collection("events")
                .document(eventId)
                .set(createEventData(teamAData, teamBData))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Event scheduled for TeamA and TeamB
                            Toast.makeText(PlanEventActivity.this, "Event scheduled for TeamA and TeamB", Toast.LENGTH_SHORT).show();
                            finish(); // Finish the activity after scheduling the event
                        } else {
                            Toast.makeText(PlanEventActivity.this, "Error scheduling event", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private Map<String, Object> createEventData(Map<String, Object> teamAData, Map<String, Object> teamBData) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("TeamA", teamAData);
        eventData.put("TeamB", teamBData);
        return eventData;
    }
}
/*
public class PlanEventActivity extends AppCompatActivity {

    private EditText teamAEditText;
    private EditText teamBEditText;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText locationEditText;
    private Button scheduleButton;

    private FirebaseFirestore db;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_event);

        teamAEditText = findViewById(R.id.teamAEditText);
        teamBEditText = findViewById(R.id.teamBEditText);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        locationEditText = findViewById(R.id.locationEditText);
        scheduleButton = findViewById(R.id.scheduleButton);

        db = FirebaseFirestore.getInstance();

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleEvent();
            }
        });
    }

    private void scheduleEvent() {
        // Get names of TeamA and TeamB
        String teamAName = teamAEditText.getText().toString();
        String teamBName = teamBEditText.getText().toString();
        title = teamAName +" vs "+teamBName;

        // Get selected date, time, and location
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        String location = locationEditText.getText().toString();

        // Validate input
        if (teamAName.isEmpty() || teamBName.isEmpty()) {
            Toast.makeText(this, "Please enter names for both teams", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve teams' data from Firestore
        retrieveTeamsData(title, year, month, day, hour, minute, location, teamAName, teamBName);
    }

    private void retrieveTeamsData(String title, int year, int month, int day, int hour, int minute, String location, String teamAName, String teamBName) {
        // Retrieve TeamA data from Firestore
        db.collection("teams").document("TeamA").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> teamATask) {
                        if (teamATask.isSuccessful()) {
                            DocumentSnapshot teamADocument = teamATask.getResult();
                            if (teamADocument.exists()) {
                                // Retrieve TeamB data from Firestore
                                retrieveTeamBData(title, year, month, day, hour, minute, location, teamAName, teamBName, teamADocument);
                            } else {
                                Toast.makeText(PlanEventActivity.this, "Error fetching TeamA data", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PlanEventActivity.this, "Error fetching TeamA data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void retrieveTeamBData(String title, int year, int month, int day, int hour, int minute, String location, String teamAName, String teamBName, DocumentSnapshot teamADocument) {
        db.collection("teams").document("TeamB").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> teamBTask) {
                        if (teamBTask.isSuccessful()) {
                            DocumentSnapshot teamBDocument = teamBTask.getResult();
                            if (teamBDocument.exists()) {
                                // Both TeamA and TeamB data retrieved, schedule the event
                                scheduleEventInFirestore(title, year, month, day, hour, minute, location, teamAName, teamBName, teamADocument, teamBDocument);
                            } else {
                                Toast.makeText(PlanEventActivity.this, "Error fetching TeamB data", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PlanEventActivity.this, "Error fetching TeamB data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void scheduleEventInFirestore(String title, int year, int month, int day, int hour, int minute, String location, String teamAName, String teamBName, DocumentSnapshot teamADocument, DocumentSnapshot teamBDocument) {
        // Extract player data from TeamA and TeamB documents
        Map<String, Object> teamAData = teamADocument.getData();
        Map<String, Object> teamBData = teamBDocument.getData();

        // Add additional information for scheduling the event
        teamAData.put("teamName", teamAName);
        teamBData.put("teamName", teamBName);
        teamAData.put("eventDate", year + "-" + (month + 1) + "-" + day);
        teamBData.put("eventDate", year + "-" + (month + 1) + "-" + day);
        teamAData.put("eventTime", hour + ":" + minute);
        teamBData.put("eventTime", hour + ":" + minute);
        teamAData.put("eventLocation", location);
        teamBData.put("eventLocation", location);

        // Create a new collection for storing event information
        db.collection("events")
                .document("Event" + System.currentTimeMillis()) // Use a unique identifier for each event
                .set(teamAData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Event scheduled for TeamA, now schedule for TeamB
                            scheduleEventForTeamB(teamBData);
                        } else {
                            Toast.makeText(PlanEventActivity.this, "Error scheduling event for TeamA", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void scheduleEventForTeamB(Map<String, Object> teamBData) {
        db.collection("events")
                .document("Event" + System.currentTimeMillis()) // Use a unique identifier for each event
                .set(teamBData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Event scheduled for TeamB
                            Toast.makeText(PlanEventActivity.this, "Event scheduled for TeamA and TeamB", Toast.LENGTH_SHORT).show();
                            finish(); // Finish the activity after scheduling the event
                        } else {
                            Toast.makeText(PlanEventActivity.this, "Error scheduling event for TeamB", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}*/