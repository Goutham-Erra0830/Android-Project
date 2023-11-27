package com.example.aep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PlayerGrowthActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPlayers;
    private PlayerAdapter playerAdapter;
    private List<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_growth);

        recyclerViewPlayers = findViewById(R.id.recyclerViewPlayersgrowth);
        playerList = new ArrayList<>();
        playerAdapter = new PlayerAdapter(playerList, new PlayerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Player player) {
                startPlayerGrowthActivity(player);
            }
        });

        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPlayers.setAdapter(playerAdapter);

        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbarplayergrowth);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Player Growth");
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Set the navigation (up) button color to white
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back); // Replace with your arrow drawable
            upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);

            // Set the title text color to white
            int textColor = getResources().getColor(android.R.color.white);
            Spannable text = new SpannableString("Player Growth");
            text.setSpan(new ForegroundColorSpan(textColor), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            actionBar.setTitle(text);

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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
                            Toast.makeText(PlayerGrowthActivity.this, "Error fetching players", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void startPlayerGrowthActivity(Player player) {
        Intent intent = new Intent(PlayerGrowthActivity.this, PlayerInsightsActivity.class);
        intent.putExtra("playerFullname", player.getFull_name());
        Log.i("PLayerAdapter", player.getFull_name());
        startActivity(intent);
    }

}


