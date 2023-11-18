package com.example.aep;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.FirebaseApp;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText emailText;

    private EditText passwordText;

    private  String enteredPassword;
    private FirebaseFirestore db;
    private Spinner userType;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();


        // Find views by their IDs
        loginButton = findViewById(R.id.login_btn);

        emailText = findViewById(R.id.email);

        // Initialize SharedPreferences
        SharedPreferences sharedPreference = getSharedPreferences("Email", Context.MODE_PRIVATE);

        String defaultEmail = "email@domain.com";
        // Reading the stored email address from SharedPreferences
        String storedEmail = sharedPreference.getString("email", defaultEmail);

        // Setting the initial text of the email field for the first time default email, then the last entered email
        emailText.setText(storedEmail);
    }

    public void login(View view)
    {


        //To get Email
        emailText = findViewById(R.id.email);
        // To Store the entered email in SharedPreferences
        String enteredEmail = emailText.getText().toString();

        //to get password
        passwordText = findViewById(R.id.password);
        //Get the password and store it in the enteredPassword variable
        enteredPassword = passwordText.getText().toString();

        // Check if the email is properly formatted
        if (!isValidEmail(enteredEmail)|| enteredEmail.isEmpty()) {
            emailText.setError(getResources().getString(R.string.Invalidemail));

            return; // Not proceeding with login since email is invalid
        }
        else if (enteredPassword.isEmpty()) {
            passwordText.setError(getResources().getString(R.string.passwordempty));

            return; // Not proceeding with login since password is empty
        } else {
            SharedPreferences sharedPreferences_erram = getSharedPreferences("Email", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences_erram.edit();
            editor.putString("email", enteredEmail);
            editor.apply();
            FirebaseAuth.getInstance().signInWithEmailAndPassword(enteredEmail, enteredPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // User login successful
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            Log.d("LoginActivity","1");
                            // Proceed to the main app screen
                            /*Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);*/
                            if (user != null) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                String userId = user.getUid();
                                Log.d("LoginActivity","2");
                                Log.d("LoginActivity",userId);

                                db.collection("users")
                                        .document(userId)
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            if (documentSnapshot.exists()) {
                                                String userType = documentSnapshot.getString("user_type");
                                                Log.d("LoginActivity","3");
                                                if ("Coach".equals(userType)) {
                                                    // User is a Coach, start CoachActivity
                                                    Intent intent = new Intent(this, Coach.class);
                                                    startActivity(intent);
                                                } else if ("Player".equals(userType)){
                                                    // User is not a Coach, start MainActivity
                                                    Intent intent = new Intent(this, Student.class);
                                                    startActivity(intent);
                                                } else {
                                                    // User is not a Coach, start MainActivity
                                                    Intent intent = new Intent(this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            } else {
                                                Log.d("LoginActivity","4");
                                                // Handle the case where the document doesn't exist
                                                Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle any Firestore query errors
                                            Toast.makeText(this, "Firestore query failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                // Handle the case where user is null
                                Toast.makeText(this, "User is null.", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            // Handle login failure
                            Toast.makeText(this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    });
            //Main activity Calling
            //Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);
        }

    }

    public void register(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    // Function to check if an email is properly formatted using a regex pattern
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
        return email.matches(emailPattern);
    }
}