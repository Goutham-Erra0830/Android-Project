package com.example.aep;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamBuildingActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTeamA;
    private RecyclerView recyclerViewTeamB;
    private Button buttonEditTeams;

    private TeamsAdapter teamAAdapter;
    private TeamsAdapter teamBAdapter;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_building);

        recyclerViewTeamA = findViewById(R.id.recyclerViewTeamA);
        recyclerViewTeamB = findViewById(R.id.recyclerViewTeamB);
        buttonEditTeams = findViewById(R.id.buttonEditTeams);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerViews
        teamAAdapter = new TeamsAdapter(new ArrayList<>());
        teamBAdapter = new TeamsAdapter(new ArrayList<>());

        recyclerViewTeamA.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTeamA.setAdapter(teamAAdapter);

        recyclerViewTeamB.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTeamB.setAdapter(teamBAdapter);

        // Set up Edit Teams button
        buttonEditTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Edit Teams button click
                editTeams();
            }
        });

        // Check and initialize TeamA and TeamB data in Firebase
        initializeTeamsData();
    }

    private void initializeTeamsData() {
        // Check if "teams" collection exists
        db.collection("teams").document("TeamA").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (!document.exists()) {
                                // "teams" collection doesn't exist, initialize TeamA and TeamB data
                                initializeTeam("TeamA", "player1", "player2", "player3", "player4", "player5", "player6", "player7", "player8", "player9", "player10", "player11", "player12", "player13");
                                initializeTeam("TeamB", "player15", "player16", "player17", "player18", "player19", "player20", "player21", "player22", "player23", "player24", "player26", "player27", "player28");
                            }
                        } else {
                            // Handle exceptions
                            Toast.makeText(TeamBuildingActivity.this, "Error checking if collection exists", Toast.LENGTH_SHORT).show();
                        }

                        // Retrieve TeamA and TeamB data from Firebase
                        Log.i("yoyo","trying to retrieve data");
                        retrieveTeamsData();
                    }
                });
    }

    private void initializeTeam(String teamName, String... players) {
        Map<String, Object> teamData = new HashMap<>();
        List<String> playerList = new ArrayList<>();
        for (String player : players) {
            playerList.add(player);
        }
        teamData.put("players", playerList);

        // Set data in Firebase
        db.collection("teams").document(teamName).set(teamData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(TeamBuildingActivity.this, teamName + " data initialized", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TeamBuildingActivity.this, "Error initializing " + teamName + " data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void retrieveTeamsData() {
        // Retrieve TeamA data
        db.collection("teams").document("TeamA").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<String> teamAPlayers = (List<String>) document.get("players");
                                teamAAdapter.setPlayerList(getPlayersFromFullNames(teamAPlayers));
                            }
                        } else {
                            // Handle exceptions
                            Toast.makeText(TeamBuildingActivity.this, "Error retrieving TeamA data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Retrieve TeamB data
        db.collection("teams").document("TeamB").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<String> teamBPlayers = (List<String>) document.get("players");
                                teamBAdapter.setPlayerList(getPlayersFromFullNames(teamBPlayers));
                            }
                        } else {
                            // Handle exceptions
                            Toast.makeText(TeamBuildingActivity.this, "Error retrieving TeamB data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private List<Player> getPlayersFromFullNames(List<String> fullNames) {
        List<Player> players = new ArrayList<>();
        for (String fullName : fullNames) {
            // Retrieve Player data from "users" collection
            db.collection("users").document(fullName).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Player player = document.toObject(Player.class);
                                    players.add(player);

                                    if (players.size() == 13) {
                                        // Update the adapter only once after all players are retrieved
                                        teamAAdapter.setPlayerList(players);
                                        teamBAdapter.setPlayerList(players);
                                        teamAAdapter.notifyDataSetChanged();
                                        teamBAdapter.notifyDataSetChanged();

                                    }

                                }
                            } else {
                                // Handle exceptions
                                Toast.makeText(TeamBuildingActivity.this, "Error retrieving player data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        return players;
    }

    private void editTeams() {
        // Handle Edit Teams button click
        // This is where you can navigate to the EditTeamActivity or perform any other actions
        // For now, let's show a toast message
        Toast.makeText(this, "Edit Teams Clicked", Toast.LENGTH_SHORT).show();
    }
}
