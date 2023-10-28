package com.example.aep;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
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

        /*Map<String, Object> userData = new HashMap<>();
        userData.put("full_name", fullName);
        userData.put("email", email);
        userData.put("user_type", userType);

        db.collection("users")
                .add(userData)
                .addOnSuccessListener(documentReference -> {
                    // Data added to Firestore successfully
                    Log.d("RegisterActivity","succeeded to push data to db");
                })
                .addOnFailureListener(e -> {
                    // Handle Firestore data storage error
                    Log.d("RegisterActivity","Failed to push data to db");
                });

        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Access the data from each document
                        String fullNamee = document.getString("full_name");
                        String emaill = document.getString("email");
                        String userTypee = document.getString("user_type");

                        // Print or display the data
                        Log.d("FirestoreData", "Full Name: " + fullNamee);
                        Log.d("FirestoreData", "Email: " + emaill);
                        Log.d("FirestoreData", "User Type: " + userTypee);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occur during data retrieval
                    Log.w("FirestoreData", "Error getting documents.", e);
                });*/
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Registration successful
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Update user's display name with full name
                        user.updateProfile(new UserProfileChangeRequest.Builder()
                                        .setDisplayName(fullName)
                                        .build())
                                .addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        // Display name updated successfully
                                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

                                        // Store user data in Firestore
                                        Map<String, Object> userData = new HashMap<>();
                                        userData.put("full_name", fullName);
                                        userData.put("email", email);
                                        userData.put("user_type", userType);

                                        db.collection("users")
                                                .add(userData)
                                                .addOnSuccessListener(documentReference -> {
                                                    // Data added to Firestore successfully
                                                    Toast.makeText(this, "User data stored in Firestore", Toast.LENGTH_SHORT).show();
                                                    Log.d("RegisterActivity","succeeded");
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Handle Firestore data storage error
                                                    Toast.makeText(this, "Failed to store user data in Firestore", Toast.LENGTH_SHORT).show();
                                                    Log.d("RegisterActivity","failed");
                                                });

                                        // Store the email for later use
                                        SharedPreferences sharedPreferences = getSharedPreferences("Email", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("email", email);
                                        editor.apply();

                                        // Navigate to the main activity
                                        Intent intent = new Intent(this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // Handle display name update failure
                                        Toast.makeText(this, "Display name update failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // Registration failed
                        Toast.makeText(this, "Registration failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}