package com.example.aep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.appcompat.app.AlertDialog;

public class ProfileActivity extends AppCompatActivity {

    TextView playerNameTextView;
    TextView phoneNumberTextView;
    TextView emailIdTextView;
    TextView placeTextView;
    TextView bloodgrpTextView;
    TextView playerTypeTextView;
    TextView ageTextView;
    Button btnEdit;
    private String fullName;
    private String userid;
    private Button btnlogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        playerNameTextView = findViewById(R.id.playerNameTextView);
        emailIdTextView = findViewById(R.id.emailIdTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        placeTextView = findViewById(R.id.placeTextView);
        bloodgrpTextView = findViewById(R.id.bloodgrpTextView);
        playerTypeTextView = findViewById(R.id.playerTypeTextView);
        ageTextView = findViewById(R.id.ageTextView);
        btnEdit = findViewById(R.id.btnEdit1);
        btnlogout = findViewById(R.id.btnLogout);

        if (extras != null && extras.containsKey("userid") && extras.containsKey("fullname")) {
            userid = extras.getString("userid");
            fullName = extras.getString("fullname");
            Log.i("hakuna","Name : "+fullName);
            Log.i("hakuna","userid : "+userid);
            // Set the fullName to the playerNameTextView

            playerNameTextView.setText(fullName);
            retrieveEmailFromFirestore(userid);
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference profileRef = db.collection("profile").document(fullName);

            profileRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Document exists, update TextViews with Firestore data
                            phoneNumberTextView.setText(document.getString("phoneNumber"));
                            placeTextView.setText(document.getString("place"));
                            bloodgrpTextView.setText(document.getString("bloodGroup"));
                            playerTypeTextView.setText(document.getString("playerType"));
                            ageTextView.setText(String.valueOf(document.getLong("age")));
                        } else {
                            // Document doesn't exist, create it with default values
                            createDefaultProfile(fullName);
                        }
                    } else {
                        Log.d("TAG", "Error getting profile document: ", task.getException());
                    }
                }
            });

        }


        ImageView imageViewBack = findViewById(R.id.profilebackbutton);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); // Simulate back navigation
            }
        });

        ImageView imageViewhelp = findViewById(R.id.profilehelp);

        imageViewhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showHelpDialog(); // Simulate back navigation
            }
        });



        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();

                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the EditProfileActivity
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("fullname", fullName);
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });

    }

    private void logout(){
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        // Pass the necessary data if needed
        startActivity(intent);

        // Finish the EditProfileActivity
        finish();
    }

    private void retrieveEmailFromFirestore(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the document reference for the specified user ID
        db.collection("users")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // DocumentSnapshot data contains the data of the document
                                String email = document.getString("email");
                                Log.i("hakuna","email : "+email);
                                // Use the email as needed (e.g., set it to a TextView)
                                emailIdTextView.setText(email);
                            } else {
                                Log.d("Firestore", "No such document");
                            }
                        } else {
                            Log.d("Firestore", "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void createDefaultProfile(String fullName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new profile document with default values
        db.collection("profile").document(fullName)
                .set(new Profile("5485779999", "Waterloo", "O +", "Bowler", 25))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Default profile created successfully");
                            // Update TextViews with default values
                            phoneNumberTextView.setText("5485779999");
                            placeTextView.setText("Waterloo");
                            bloodgrpTextView.setText("O +");
                            playerTypeTextView.setText("Bowler");
                            ageTextView.setText("25");
                        } else {
                            Log.d("TAG", "Error creating default profile: ", task.getException());
                        }
                    }
                });

        DocumentReference profileRef = db.collection("profile").document(fullName);

        profileRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        // Document exists, update TextViews with Firestore data
                        phoneNumberTextView.setText(document.getString("phoneNumber"));
                        placeTextView.setText(document.getString("place"));
                        bloodgrpTextView.setText(document.getString("bloodGroup"));
                        playerTypeTextView.setText(document.getString("playerType"));
                        ageTextView.setText(String.valueOf(document.getLong("age")));
                    }
                } else {
                    Log.d("TAG", "Error getting profile document: ", task.getException());
                }
            }
        });
    }

    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help Menu");
        builder.setMessage("Author:Group 12\nVersion: version 1.0\nInstructions:1.If you're a new user, sign up for an account using your email \n 2.If you're an existing user, log in using your registered credentials\n" +
                "3.You can login as either Coach or Player and test functionalities");
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Perform any action on OK click if needed
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
