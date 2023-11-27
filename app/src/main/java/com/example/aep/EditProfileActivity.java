package com.example.aep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private String fullName;
    private EditText editPhoneNumber, editPlace, editBloodGroup, editPlayerType, editAge;
    private Button btnSave;
    private String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Bundle extras = getIntent().getExtras();
        fullName = extras.getString("fullname");
        userid =  extras.getString("userid");

        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editPlace = findViewById(R.id.editPlace);
        editBloodGroup = findViewById(R.id.editBloodGroup);
        editPlayerType = findViewById(R.id.editPlayerType);
        editAge = findViewById(R.id.editAge);
        btnSave = findViewById(R.id.btnSave);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the user input to Firebase Firestore
                saveProfileData();
                //Intent intent = new Intent(EditProfileActivity.this, PlayerActivity.class);
                //startActivity(intent);
                finish();
            }
        });

    }

    private void logout(){

    }

    private void saveProfileData() {
        // Retrieve user input
        String phoneNumber = editPhoneNumber.getText().toString();
        String place = editPlace.getText().toString();
        String bloodGroup = editBloodGroup.getText().toString();
        String playerType = editPlayerType.getText().toString();
        int age = Integer.parseInt(editAge.getText().toString());

        // Create a map with the profile data
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("phoneNumber", phoneNumber);
        profileData.put("place", place);
        profileData.put("bloodGroup", bloodGroup);
        profileData.put("playerType", playerType);
        profileData.put("age", age);

        // Save the data to Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("profile").document(fullName)
                .set(profileData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this, "Profile data saved successfully", Toast.LENGTH_SHORT).show();

                            // Start ProfileActivity after saving data
                            startPlayerActivity();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Error saving profile data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void startPlayerActivity() {
        // Start the ProfileActivity
        Intent intent = new Intent(EditProfileActivity.this, PlayerActivity.class);
        // Pass the necessary data if needed
        intent.putExtra("userid", userid);
        intent.putExtra("current_username", fullName);
        startActivity(intent);

        // Finish the EditProfileActivity
        finish();
    }
}