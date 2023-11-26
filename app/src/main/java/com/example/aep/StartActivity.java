package com.example.aep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    private ImageView instagram;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final Button sign_up = findViewById(R.id.sign_up);
        final Button login = findViewById(R.id.login);

        instagram=findViewById(R.id.instagramIcon);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
            }
        });

        instagram.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                instagram(view);
            }
        }
        );

        ImageView twitterButton = findViewById(R.id.twitterIcon);

        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter();
            }
        });

        ImageView openWebsiteButton = findViewById(R.id.googleicon);

        openWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite();
            }
        });

        ImageView openFacebookButton = findViewById(R.id.facebookIcon);

        openFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebook();
            }
        });
    }

    private void instagram(View view)
    {
        Uri uri = Uri.parse("https://instagram.com/wilfridlaurieruni?igshid=MTk0NTkyODZkYg==");

        // Create an Intent with the ACTION_VIEW action and the Instagram URI
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        // Set the package to Instagram to ensure it opens in the Instagram app if available
        intent.setPackage("com.instagram.android");

        // Check if Instagram app is installed on the device
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Instagram app is not installed, open Instagram in a web browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/wilfridlaurieruni?igshid=MTk0NTkyODZkYg=="));
            startActivity(intent);
        }
    }

    private void openTwitter() {

        Uri uri = Uri.parse("https://twitter.com/WLUAthletics");

        // Create an Intent with the ACTION_VIEW action and the Twitter URI
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        // Set the package to Twitter to ensure it opens in the Twitter app if available
        intent.setPackage("com.twitter.android");

        // Check if Twitter app is installed on the device
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Twitter app is not installed, open Twitter in a web browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/WLUAthletics"));
            startActivity(intent);
        }
    }

    private void openWebsite() {

        String websiteUrl = "https://laurierathletics.com/";
        Uri uri = Uri.parse(websiteUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void openFacebook() {

        Uri uri = Uri.parse("https://www.facebook.com/WilfridLaurierUniversity?mibextid=2JQ9oc");

        // Create an Intent with the ACTION_VIEW action and the Facebook URI
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        // Set the package to Facebook to ensure it opens in the Facebook app if available
        intent.setPackage("com.facebook.katana"); // com.facebook.katana is the package name for the Facebook app

        // Check if Facebook app is installed on the device
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Facebook app is not installed, open Facebook in a web browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/WilfridLaurierUniversity?mibextid=2JQ9oc"));
            startActivity(intent);
        }
    }
}