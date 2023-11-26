package com.example.aep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    TextView playerNameTextView;
    TextView phoneNumberTextView;
    TextView emailIdTextView;
    TextView placeTextView;
    TextView bloodgrpTextView;
    TextView playerTypeTextView;
    TextView ageTextView;
    Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();

        if (extras != null && extras.containsKey("userid") && extras.containsKey("fullname")) {
            String userid = extras.getString("userid");
            String fullName = extras.getString("fullname");
            Log.i("hakuna","Name : "+fullName);
            Log.i("hakuna","userid : "+userid);
            // Set the fullName to the playerNameTextView
            playerNameTextView = findViewById(R.id.playerNameTextView);
            playerNameTextView.setText(fullName);
            emailIdTextView = findViewById(R.id.emailIdTextView);

            retrieveEmailFromFirestore(userid);

        }
        btnEdit = findViewById(R.id.btnEdit1);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the EditProfileActivity
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

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
}
