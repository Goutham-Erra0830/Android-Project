package com.example.aep;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditTeamActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPlayers;
    private Button buttonDone;

    private EditPlayerAdapter editPlayerAdapter;

    private FirebaseFirestore db;
    private TextView textViewSelectionMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        recyclerViewPlayers = findViewById(R.id.recyclerViewPlayers);
        textViewSelectionMessage = findViewById(R.id.textViewSelectionMessage);
        buttonDone = findViewById(R.id.doneButton);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        //editPlayerAdapter = new EditPlayerAdapter(PlayerUtils.getSamplePlayerList()); // You can replace this with the actual player list from Firebase
        editPlayerAdapter = new EditPlayerAdapter(new ArrayList<>());
        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPlayers.setAdapter(editPlayerAdapter);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTeamData();
                // Handle Done button click
                if (textViewSelectionMessage.getText().toString().equals("Select players for Team A")) {
                    textViewSelectionMessage.setText("Select players for Team B");
                    // Repopulate player data for TeamB (excluding TeamA players)
                    repopulatePlayerDataForTeamB();

                } else {
                    // Handle other actions for Team B if needed
                    finish();
                }
            }
        });
        fetchPlayers();
    }

    private void fetchPlayers() {
        db.collection("users")
                .whereEqualTo("user_type", "Player")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Player> playerList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Player player = PlayerUtils.convertHashMapToPlayer(document.getData());
                                playerList.add(player);
                            }
                            editPlayerAdapter.setPlayerList(playerList);
                        } else {
                            Toast.makeText(EditTeamActivity.this, "Error fetching player data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void updateTeamData() {
        List<Player> selectedPlayers = editPlayerAdapter.getSelectedPlayers();

        String teamName = "TeamA";
        if (textViewSelectionMessage.getText().toString().equals("Select players for Team B")) {
            teamName = "TeamB";
        }

        // Update the "TeamA" document in the "teams" collection
        Map<String, Object> teamData = new HashMap<>();
        teamData.put("players", selectedPlayers);

        DocumentReference teamADocumentRef = db.collection("teams").document(teamName);
        teamADocumentRef.set(teamData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditTeamActivity.this, "TeamA data updated", Toast.LENGTH_SHORT).show();
                            //finish(); // Finish the activity after updating data
                        } else {
                            Toast.makeText(EditTeamActivity.this, "Error updating TeamA data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void repopulatePlayerDataForTeamB() {
        // Fetch all players from the "users" collection whose userType is "Player"
        db.collection("users")
                .whereEqualTo("user_type", "Player")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Player> allPlayers = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Player player = PlayerUtils.convertHashMapToPlayer(document.getData());
                                allPlayers.add(player);
                            }

                            // Fetch TeamA players to exclude them from TeamB
                            db.collection("teams").document("TeamA").get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> teamATask) {
                                            if (teamATask.isSuccessful()) {
                                                DocumentSnapshot teamADocument = teamATask.getResult();
                                                if (teamADocument.exists()) {
                                                    List<Player> teamAPlayers = (List<Player>) teamADocument.get("players");
                                                    //List<Player> teamAPlayers = PlayerUtils.convertHashMapListToPlayerList(teamADocument.get("players"));
                                                    // Exclude TeamA players from the allPlayers list
                                                    List<Player> teamBPlayers = new ArrayList<>(allPlayers);
                                                    teamBPlayers.removeAll(teamAPlayers);

                                                    // Set the updated player list for TeamB
                                                    editPlayerAdapter.setPlayerList(teamBPlayers);
                                                }
                                            } else {
                                                Toast.makeText(EditTeamActivity.this, "Error fetching TeamA data", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(EditTeamActivity.this, "Error fetching player data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}