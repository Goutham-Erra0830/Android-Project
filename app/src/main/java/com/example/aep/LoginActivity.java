package com.example.aep;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();


        // Find views by their IDs
        loginButton = findViewById(R.id.login_button);

        emailText = findViewById(R.id.email_edittext);

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
        emailText = findViewById(R.id.email_edittext);
        // To Store the entered email in SharedPreferences
        String enteredEmail = emailText.getText().toString();

        //to get password
        passwordText = findViewById(R.id.password_edittext);
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
                            // Proceed to the main app screen
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
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