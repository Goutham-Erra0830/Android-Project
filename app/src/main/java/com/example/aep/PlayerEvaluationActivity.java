package com.example.aep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class PlayerEvaluationActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPlayers;
    private PlayerAdapter playerAdapter;
    private List<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_evaluation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewPlayers = findViewById(R.id.recyclerViewPlayers);
        playerList = new ArrayList<>();
        playerAdapter = new PlayerAdapter(playerList, new PlayerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Player player) {
                startPlayerStatisticsActivity(player);
            }
        });

        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPlayers.setAdapter(playerAdapter);

        // Fetch players from Firestore
        fetchPlayers();
    }
    private void fetchPlayers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("user_type", "Player")  // Filter by player type if needed
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            playerList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Player player = document.toObject(Player.class);
                                playerList.add(player);
                            }
                            playerAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(PlayerEvaluationActivity.this, "Error fetching players", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startPlayerStatisticsActivity(Player player) {
        Intent intent = new Intent(PlayerEvaluationActivity.this, PlayerStatisticsActivity.class);
        intent.putExtra("playerFullname", player.getFull_name());
        Log.i("PLayerAdapter",player.getFull_name());
        Log.i("PlayerEvaluation","Hakunamatata");
        startActivity(intent);
    }
}