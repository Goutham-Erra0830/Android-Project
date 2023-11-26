package com.example.aep;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatisticsActivity extends AppCompatActivity {

    private EditText editTextRunsScored, editTextWicketsTaken, editTextBoundaries,
            editTextOversBowled, editTextBallsPlayed, editTextCatchesTaken;

    private SeekBar seekBarRating;
    private Button buttonSave;

    private String full_name;

    private TextView textViewPlayername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_statistics);
        Log.i("PlayerStatisticsActivity", "I am in stats class");
        full_name = getIntent().getStringExtra("playerFullname");

        // Initialize views
        editTextRunsScored = findViewById(R.id.editTextRunsScored);
        editTextWicketsTaken = findViewById(R.id.editTextWicketsTaken);
        editTextBoundaries = findViewById(R.id.editTextBoundaries);
        editTextOversBowled = findViewById(R.id.editTextOversBowled);
        editTextBallsPlayed = findViewById(R.id.editTextBallsPlayed);
        editTextCatchesTaken = findViewById(R.id.editTextCatchesTaken);

        textViewPlayername=findViewById(R.id.textViewPlayerName);
        textViewPlayername.setText(full_name);

        seekBarRating = findViewById(R.id.seekBarRating);

        buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlayerStatistics();
            }
        });


    }

    private void savePlayerStatistics() {
        // Retrieve values from EditText fields and SeekBar
        int runsScored = Integer.parseInt(editTextRunsScored.getText().toString());
        int wicketsTaken = Integer.parseInt(editTextWicketsTaken.getText().toString());
        int boundaries = Integer.parseInt(editTextBoundaries.getText().toString());
        int oversBowled = Integer.parseInt(editTextOversBowled.getText().toString());
        int ballsPlayed = Integer.parseInt(editTextBallsPlayed.getText().toString());
        int catchesTaken = Integer.parseInt(editTextCatchesTaken.getText().toString());
        int rating = seekBarRating.getProgress();

        // Retrieve values for additional statistics




        if (full_name != null) {
            // Create a PlayerStatistics object


            // Save the data to Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Check if the "playerstats" collection exists
            db.collection("playerstats").document(full_name).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (!document.exists()) {
                                // "playerstats" collection doesn't exist, create it with default values
                                Map<String, Object> defaultValues = new HashMap<>();
                                defaultValues.put("runsScored", 0);
                                defaultValues.put("wicketsTaken", 0);
                                defaultValues.put("boundaries", 0);
                                defaultValues.put("oversBowled", 0);
                                defaultValues.put("ballsPlayed", 0);
                                defaultValues.put("catchesTaken", 0);
                                defaultValues.put("rating", 0);
                                defaultValues.put("strikeRate", 0);
                                defaultValues.put("totalCenturies", 0);
                                defaultValues.put("totalHalfCenturies", 0);
                                defaultValues.put("matchesPlayed",0);

                                db.collection("playerstats").document(full_name).set(defaultValues)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("PlayerStatisticsActivity", "Player stats collection created with default values");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("PlayerStatisticsActivity", "Error creating player stats collection", e);
                                        });
                            }
                            updateMatchesPlayed();
                            updateRunsScored(runsScored);
                            updateWicketsTaken(wicketsTaken);
                            updateBoundaries(boundaries);
                            updateOversBowled(oversBowled);
                            updateBallsPlayed(ballsPlayed);
                            updateCatchesTaken(catchesTaken);
                            updateRating(rating);
                            updateStrikeRate(runsScored, ballsPlayed);
                            updateHalfCenturies(runsScored);
                            updateCenturies(runsScored);

                            finish();
                        } else {
                            // Handle exceptions
                            Log.e("PlayerStatisticsActivity", "Error checking if collection exists", task.getException());
                        }
                    });
        } else {
            // Handle the case where full_name is null
            Log.e("PlayerStatisticsActivity", "Error: full_name is null");
        }
    }

    private void updateHalfCenturies(int runsscored){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve the existing value of totalCenturies
        db.collection("playerstats").document(full_name).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Integer existingHalfCenturies = document.getLong("totalHalfCenturies").intValue();

                            if (existingHalfCenturies != null ) {
                                // Increment the value if runsScored is greater than or equal to 100
                                if (runsscored >= 50 && runsscored < 100) {
                                    // Increment the existing value from Firebase
                                    existingHalfCenturies = existingHalfCenturies + 1;
                                }
                                int newTotalHalfCenturies = existingHalfCenturies;
                                // Update the value in Firestore
                                db.collection("playerstats").document(full_name)
                                        .update("totalHalfCenturies", newTotalHalfCenturies)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("PlayerStatisticsActivity", "TotalHalfCenturies updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("PlayerStatisticsActivity", "Error updating TotalHalfCenturies", e);
                                        });
                            } else {
                                // Handle the case where the existing value is null
                                Log.e("PlayerStatisticsActivity", "Existing TotalHalfCenturies value is null");
                            }
                        } else {
                            // Handle the case where the document doesn't exist
                            Log.e("PlayerStatisticsActivity", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("PlayerStatisticsActivity", "Error getting TotalHalfCenturies", task.getException());
                    }
                });

        // Default return value (you can change this as needed)
    }

    private void updateCenturies(int runsscored){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve the existing value of totalCenturies
        db.collection("playerstats").document(full_name).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Integer existingCenturies = document.getLong("totalCenturies").intValue();

                            if (existingCenturies != null ) {
                                // Increment the value if runsScored is greater than or equal to 100
                                if (runsscored >= 100) {
                                    // Increment the existing value from Firebase
                                    existingCenturies = existingCenturies + 1;
                                }
                                int newTotalCenturies = existingCenturies;
                                // Update the value in Firestore
                                db.collection("playerstats").document(full_name)
                                        .update("totalCenturies", newTotalCenturies)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("PlayerStatisticsActivity", "totalCenturies updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("PlayerStatisticsActivity", "Error updating totalCenturies", e);
                                        });
                            } else {
                                // Handle the case where the existing value is null
                                Log.e("PlayerStatisticsActivity", "Existing totalCenturies value is null");
                            }
                        } else {
                            // Handle the case where the document doesn't exist
                            Log.e("PlayerStatisticsActivity", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("PlayerStatisticsActivity", "Error getting totalCenturies", task.getException());
                    }
                });

        // Default return value (you can change this as needed)
    }

    private void updateStrikeRate(int runsscored, int ballsplayed){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve the existing value of totalCenturies
        db.collection("playerstats").document(full_name).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Integer existingRunsScored = document.getLong("runsScored").intValue();
                            Integer existingBallsPlayed = document.getLong("ballsPlayed").intValue();
                            if (existingRunsScored != null && existingBallsPlayed !=null) {
                                // Increment the value if runsScored is greater than or equal to 100
                                double totalRuns = existingRunsScored + runsscored;
                                double totalBalls = existingBallsPlayed + ballsplayed;
                                double newStrikeRate1 = (totalRuns/totalBalls)*100;
                                int newStrikeRate = (int) Math.floor(newStrikeRate1);
                                // Update the value in Firestore
                                db.collection("playerstats").document(full_name)
                                        .update("strikeRate", newStrikeRate)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("PlayerStatisticsActivity", "strikeRate updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("PlayerStatisticsActivity", "Error updating strikeRate", e);
                                        });
                            } else {
                                // Handle the case where the existing value is null
                                Log.e("PlayerStatisticsActivity", "Existing strikeRate value is null");
                            }
                        } else {
                            // Handle the case where the document doesn't exist
                            Log.e("PlayerStatisticsActivity", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("PlayerStatisticsActivity", "Error getting strikeRate", task.getException());
                    }
                });

        // Default return value (you can change this as needed)
    }

    private void updateRating(int rating){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve the existing value of totalCenturies
        db.collection("playerstats").document(full_name).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Integer existingRating = document.getLong("rating").intValue();
                            Integer existingMatchesPlayed = document.getLong("matchesPlayed").intValue();

                            if (existingRating != null) {
                                // Increment the value if runsScored is greater than or equal to 100
                                if(existingMatchesPlayed == null || existingMatchesPlayed == 1){
                                    existingMatchesPlayed = 2;

                                }
                                if(existingMatchesPlayed == 0){
                                    existingMatchesPlayed = 1;
                                }
                                existingRating = existingRating * (existingMatchesPlayed-1);

                                int newRating = (existingRating + rating)/existingMatchesPlayed;
                                // Update the value in Firestore
                                db.collection("playerstats").document(full_name)
                                        .update("rating", newRating)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("PlayerStatisticsActivity", "rating updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("PlayerStatisticsActivity", "Error updating rating", e);
                                        });
                            } else {
                                // Handle the case where the existing value is null
                                Log.e("PlayerStatisticsActivity", "Existing rating value is null");
                            }
                        } else {
                            // Handle the case where the document doesn't exist
                            Log.e("PlayerStatisticsActivity", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("PlayerStatisticsActivity", "Error getting rating", task.getException());
                    }
                });

        // Default return value (you can change this as needed)
    }

    private void updateCatchesTaken(int catchestaken){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve the existing value of totalCenturies
        db.collection("playerstats").document(full_name).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Integer existingCatchesTaken = document.getLong("catchesTaken").intValue();
                            if (existingCatchesTaken != null) {
                                // Increment the value if runsScored is greater than or equal to 100
                                int newCatchesTaken = existingCatchesTaken + catchestaken;
                                // Update the value in Firestore
                                db.collection("playerstats").document(full_name)
                                        .update("catchesTaken", newCatchesTaken)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("PlayerStatisticsActivity", "catchesTaken updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("PlayerStatisticsActivity", "Error updating catchesTaken", e);
                                        });
                            } else {
                                // Handle the case where the existing value is null
                                Log.e("PlayerStatisticsActivity", "Existing catchesTaken value is null");
                            }
                        } else {
                            // Handle the case where the document doesn't exist
                            Log.e("PlayerStatisticsActivity", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("PlayerStatisticsActivity", "Error getting catchesTaken", task.getException());
                    }
                });

        // Default return value (you can change this as needed)
    }

    private void updateBallsPlayed(int ballsplayed){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve the existing value of totalCenturies
        db.collection("playerstats").document(full_name).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Integer existingBallsPlayed = document.getLong("ballsPlayed").intValue();
                            if (existingBallsPlayed != null) {
                                // Increment the value if runsScored is greater than or equal to 100
                                int newBallsPlayed = existingBallsPlayed + ballsplayed;
                                // Update the value in Firestore
                                db.collection("playerstats").document(full_name)
                                        .update("ballsPlayed", newBallsPlayed)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("PlayerStatisticsActivity", "ballsPlayed updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("PlayerStatisticsActivity", "Error updating ballsPlayed", e);
                                        });
                            } else {
                                // Handle the case where the existing value is null
                                Log.e("PlayerStatisticsActivity", "Existing ballsPlayed value is null");
                            }
                        } else {
                            // Handle the case where the document doesn't exist
                            Log.e("PlayerStatisticsActivity", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("PlayerStatisticsActivity", "Error getting ballsPlayed", task.getException());
                    }
                });

        // Default return value (you can change this as needed)
    }

    private void updateOversBowled(int oversbowled){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve the existing value of totalCenturies
        db.collection("playerstats").document(full_name).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Integer existingOversBowled = document.getLong("oversBowled").intValue();
                            if (existingOversBowled != null) {
                                // Increment the value if runsScored is greater than or equal to 100
                                int newOversBowled = existingOversBowled + oversbowled;
                                // Update the value in Firestore
                                db.collection("playerstats").document(full_name)
                                        .update("oversBowled", newOversBowled)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("PlayerStatisticsActivity", "oversBowled updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("PlayerStatisticsActivity", "Error updating oversBowled ", e);
                                        });
                            } else {
                                // Handle the case where the existing value is null
                                Log.e("PlayerStatisticsActivity", "Existing oversBowled  value is null");
                            }
                        } else {
                            // Handle the case where the document doesn't exist
                            Log.e("PlayerStatisticsActivity", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("PlayerStatisticsActivity", "Error getting oversBowled ", task.getException());
                    }
                });

        // Default return value (you can change this as needed)
    }

    private void updateBoundaries(int boundaries){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve the existing value of totalCenturies
        db.collection("playerstats").document(full_name).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Integer existingBoundaries = document.getLong("boundaries").intValue();
                            if (existingBoundaries != null) {
                                // Increment the value if runsScored is greater than or equal to 100
                                int newBoundaries = existingBoundaries + boundaries;
                                // Update the value in Firestore
                                db.collection("playerstats").document(full_name)
                                        .update("boundaries", newBoundaries)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("PlayerStatisticsActivity", "Boundaries updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("PlayerStatisticsActivity", "Error updating Boundaries", e);
                                        });
                            } else {
                                // Handle the case where the existing value is null
                                Log.e("PlayerStatisticsActivity", "Existing Boundaries value is null");
                            }
                        } else {
                            // Handle the case where the document doesn't exist
                            Log.e("PlayerStatisticsActivity", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("PlayerStatisticsActivity", "Error getting Boundaries", task.getException());
                    }
                });

        // Default return value (you can change this as needed)
    }

    private void updateWicketsTaken(int wicketstaken){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve the existing value of totalCenturies
        db.collection("playerstats").document(full_name).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Integer existingWickets = document.getLong("wicketsTaken").intValue();
                            if (existingWickets != null) {
                                // Increment the value if runsScored is greater than or equal to 100
                                int newWickets = existingWickets + wicketstaken;
                                // Update the value in Firestore
                                db.collection("playerstats").document(full_name)
                                        .update("wicketsTaken", newWickets)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("PlayerStatisticsActivity", "wicketsTaken updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("PlayerStatisticsActivity", "Error updating wicketsTaken", e);
                                        });
                            } else {
                                // Handle the case where the existing value is null
                                Log.e("PlayerStatisticsActivity", "Existing wicketsTaken value is null");
                            }
                        } else {
                            // Handle the case where the document doesn't exist
                            Log.e("PlayerStatisticsActivity", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("PlayerStatisticsActivity", "Error getting wicketsTaken", task.getException());
                    }
                });

        // Default return value (you can change this as needed)
    }

    private void updateRunsScored(int runsscored){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve the existing value of totalCenturies
        db.collection("playerstats").document(full_name).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Integer existingRuns = document.getLong("runsScored").intValue();
                            if (existingRuns != null) {
                                // Increment the value if runsScored is greater than or equal to 100
                                int newRuns = existingRuns + runsscored ;
                                // Update the value in Firestore
                                db.collection("playerstats").document(full_name)
                                        .update("runsScored", newRuns)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("PlayerStatisticsActivity", "RunsScored updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("PlayerStatisticsActivity", "Error updating RunsScored", e);
                                        });
                            } else {
                                // Handle the case where the existing value is null
                                Log.e("PlayerStatisticsActivity", "Existing RunsScored value is null");
                            }
                        } else {
                            // Handle the case where the document doesn't exist
                            Log.e("PlayerStatisticsActivity", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("PlayerStatisticsActivity", "Error getting RunsScored", task.getException());
                    }
                });

        // Default return value (you can change this as needed)
    }
    private void updateMatchesPlayed(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve the existing value of totalCenturies
        db.collection("playerstats").document(full_name).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Integer existingMatches = document.getLong("matchesPlayed").intValue();
                            if (existingMatches != null) {
                                // Increment the value if runsScored is greater than or equal to 100
                                int newMatches = existingMatches + 1 ;
                                // Update the value in Firestore
                                db.collection("playerstats").document(full_name)
                                        .update("matchesPlayed", newMatches)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("PlayerStatisticsActivity", "MatchesPlayed updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("PlayerStatisticsActivity", "Error updating MatchesPlayed", e);
                                        });
                            } else {
                                // Handle the case where the existing value is null
                                Log.e("PlayerStatisticsActivity", "Existing MatchesPlayed value is null");
                            }
                        } else {
                            // Handle the case where the document doesn't exist
                            Log.e("PlayerStatisticsActivity", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("PlayerStatisticsActivity", "Error getting MatchesPlayed", task.getException());
                    }
                });

        // Default return value (you can change this as needed)
    }

    // Helper method to calculate strike rate
    private int calculateStrikeRate(int runsScored, int ballsPlayed) {
        if (ballsPlayed == 0) {
            return 0; // Avoid division by zero
        }
        return (int) ((float) runsScored / ballsPlayed * 100);
    }

}
