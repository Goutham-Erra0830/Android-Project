package com.example.aep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.android.gms.tasks.Continuation;

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
                .continueWithTask(new Continuation<DocumentSnapshot, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (!document.exists()) {
                                // "TeamA" data doesn't exist, initialize TeamA data
                                return initializeTeam("TeamA", "player1", "player2", "player3", "player4", "player5", "player6", "player7", "player8", "player9", "player10", "player11", "player12", "player13");
                            }
                        } else {
                            // Handle exceptions
                            Toast.makeText(TeamBuildingActivity.this, "Error checking if TeamA data exists", Toast.LENGTH_SHORT).show();
                        }

                        // TeamA data exists, return a completed task
                        return Tasks.forResult(null);
                    }
                })
                .continueWithTask(new Continuation<Void, Task<DocumentSnapshot>>() {
                    @Override
                    public Task<DocumentSnapshot> then(@NonNull Task<Void> task) throws Exception {
                        // Retrieve TeamB data from Firebase
                        return db.collection("teams").document("TeamB").get();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // "TeamB" data exists, retrieve both TeamA and TeamB data
                                retrieveTeamsData();
                            } else {
                                // "TeamB" data doesn't exist, initialize TeamB data
                                initializeTeam("TeamB", "player15", "player16", "player17", "player18", "player19", "player20", "player21", "player22", "player23", "player24", "player26", "player27", "player28")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                // Retrieve updated data and update the UI
                                                retrieveTeamsData();
                                            }
                                        });
                            }
                        } else {
                            // Handle exceptions
                            Toast.makeText(TeamBuildingActivity.this, "Error retrieving TeamB data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private Task<Void> initializeTeam(String teamName, String... playerNames) {
        List<Player> playerList = new ArrayList<>();

        List<Task<Void>> tasks = new ArrayList<>();

        for (String playerName : playerNames) {
            Task<Void> playerTask = db.collection("users").whereEqualTo("full_name", playerName).get()
                    .continueWith(new Continuation<QuerySnapshot, Void>() {
                        @Override
                        public Void then(@NonNull Task<QuerySnapshot> task) throws Exception {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> data = document.getData();
                                    Player player = PlayerUtils.convertHashMapToPlayer(data);
                                    playerList.add(player);
                                }
                            } else {
                                // Handle exceptions
                                Toast.makeText(TeamBuildingActivity.this, "Error retrieving player data", Toast.LENGTH_SHORT).show();
                            }
                            return null;
                        }
                    });

            tasks.add(playerTask);
        }

        // Return a combined task that completes when all player tasks are finished
        return Tasks.whenAll(tasks).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(@NonNull Task<Void> task) throws Exception {
                if (task.isSuccessful()) {
                    // All players retrieved, proceed to update Firebase
                    Map<String, Object> teamData = new HashMap<>();
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
                } else {
                    // Handle exceptions
                    Toast.makeText(TeamBuildingActivity.this, "Error initializing Team data", Toast.LENGTH_SHORT).show();
                }
                return null;
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

                                //List<Player> teamAPlayers = (List<Player>) document.get("players");
                                List<HashMap<String, Object>> teamAPlayers = (List<HashMap<String, Object>>) document.get("players");
                                List<Player> convertedPlayersA = PlayerUtils.convertHashMapListToPlayerList(teamAPlayers);
                                teamAAdapter.setPlayerList(convertedPlayersA);
                            }
                        } else {

                            // Handle exceptions
                            Toast.makeText(TeamBuildingActivity.this, "Error retrieving TeamA data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Retrieve TeamB data
        Log.i("insideretrieve","5");
        db.collection("teams").document("TeamB").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.i("insideretrieve","7");
                                List<HashMap<String, Object>> teamBPlayers = (List<HashMap<String, Object>>) document.get("players");
                                List<Player> convertedPlayersB = PlayerUtils.convertHashMapListToPlayerList(teamBPlayers);
                                teamBAdapter.setPlayerList(convertedPlayersB);
                            }
                        } else {
                            Log.i("insideretrieve","8");
                            // Handle exceptions
                            Toast.makeText(TeamBuildingActivity.this, "Error retrieving TeamB data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void editTeams() {
        // Handle Edit Teams button click
        // This is where you can navigate to the EditTeamActivity or perform any other actions
        // For now, let's show a toast message
        Toast.makeText(this, "Edit Teams Clicked", Toast.LENGTH_SHORT).show();
        deleteTeamData("TeamA");
        deleteTeamData("TeamB");
        Intent intent = new Intent(this, EditTeamActivity.class);
        startActivity(intent);
    }
    private void deleteTeamData(String teamName) {
        db.collection("teams").document(teamName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TeamDeletion", "Team " + teamName + " data deleted successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TeamDeletion", "Error deleting team " + teamName + " data", e);
                    }
                });
    }
}
