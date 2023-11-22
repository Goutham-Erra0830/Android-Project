package com.example.aep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Spinner userTypeSpinner;
    private Button registerButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Spinner spinner = findViewById(R.id.spinner);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

// Sample data - you can replace this with your own list of items
        String[] items = {"Coach", "Player"};

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        fullNameEditText = findViewById(R.id.FullnameET);
        emailEditText = findViewById(R.id.emailET);
        passwordEditText = findViewById(R.id.passwordET);
        userTypeSpinner = findViewById(R.id.spinner);
        registerButton = findViewById(R.id.RegisterButton);

    }

    public void clickedOnRegister(View view){
        Log.d("RegisterActivity","Hi bro");
        String fullName = fullNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String userType = userTypeSpinner.getSelectedItem().toString();

        // Check if the fields are not empty
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isEmailValid(email)) {
            Toast.makeText(this, "Please enter email in proper format i.e. xyz@gmail.com ", Toast.LENGTH_LONG).show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d("AuthenticationStatus", "User is authenticated. UID: " + authResult.getUser().getUid());
                FirebaseUser newUser = authResult.getUser();
                Map<String, Object> userData = new HashMap<>();
                userData.put("full_name", fullName);
                userData.put("email", email);
                userData.put("password",password);
                userData.put("user_type", userType);

                db.collection("users")
                        .document(newUser.getUid())
                        .set(userData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("RegistrationStatus", "User data stored in Firestore");
                                Toast.makeText(RegisterActivity.this,"Registration successful", Toast.LENGTH_LONG).show();
                                if(userType == "Player") {
                                    // Initialize player stats with dummy values
                                    db.collection("playerstats").document(fullName).get()
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (!document.exists()) {
                                                        // "playerstats" collection doesn't exist, create it with default values
                                                        Map<String, Object> defaultValues = new HashMap<>();
                                                        defaultValues.put("matchesPlayed", 0);
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

                                                        db.collection("playerstats").document(fullName).set(defaultValues)
                                                                .addOnSuccessListener(aVoid -> {
                                                                    Log.d("RegisterActivity", "Player stats collection created with default values");
                                                                })
                                                                .addOnFailureListener(e -> {
                                                                    Log.e("RegisterActivity", "Error creating default values for player", e);
                                                                });
                                                    }
                                                }
                                            });
                                }

                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("RegistrationStatus","Failed to push data to db");
                                Toast.makeText(RegisterActivity.this,"Registration Failed ", Toast.LENGTH_LONG).show();
                            }
                        });

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // User authentication failed
                Log.d("RegistrationStatus", "User registration  failed: " + e.getMessage());

                // Handle the authentication failure (e.g., show an error message to the user).
                Toast.makeText(RegisterActivity.this, "Registration  failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean isEmailValid(String email) {
        // Define a regular expression pattern for a valid email format
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Use regex to check if the email matches the pattern
        return email.matches(emailPattern);
    }
}