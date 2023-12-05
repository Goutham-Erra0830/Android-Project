package com.example.aep;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BMIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmiactivity);
        final EditText editTextWeight = findViewById(R.id.editTextWeight);
        final EditText editTextHeight = findViewById(R.id.editTextHeight);
        final RadioGroup radioGroupGender = findViewById(R.id.radioGroupGender);
        final Button buttonCalculate = findViewById(R.id.buttonCalculate);
        final TextView textViewResult = findViewById(R.id.textViewResult);

        ImageView imageViewBack = findViewById(R.id.profileBMI);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); // Simulate back navigation
            }
        });

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String weightStr = editTextWeight.getText().toString();
                String heightStr = editTextHeight.getText().toString();
                boolean isMale = ((RadioButton) findViewById(R.id.radioButtonMale)).isChecked();

                if (!weightStr.isEmpty() && !heightStr.isEmpty()) {
                    // Convert input to numbers
                    float weight = Float.parseFloat(weightStr);
                    float height = Float.parseFloat(heightStr);

                    // Calculate BMI
                    float bmi = calculateBMI(weight, height, isMale);

                    // Determine BMI category
                    String category = determineBMICategory(bmi);

                    // Display result with category and color coding
                    displayResult(bmi, category, textViewResult);
                } else {
                    textViewResult.setText("Please enter both weight and height.");
                    textViewResult.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }
        });
    }

    private float calculateBMI(float weight, float height, boolean isMale) {
        // BMI formula: weight (kg) / (height (m) * height (m))
        float bmi = weight / (height * height);


        return bmi;
    }

    private String determineBMICategory(float bmi) {
        // Determine BMI category based on WHO guidelines
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal Weight";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    private void displayResult(float bmi, String category, TextView textViewResult) {
        String resultText = "Your BMI is: " + String.format("%.2f", bmi) + "\nCategory: " + category;
        textViewResult.setText(resultText);

        // Set color based on BMI category
        int color = getColorForCategory(category);
        textViewResult.setTextColor(getResources().getColor(color));
    }

    private int getColorForCategory(String category) {
        switch (category) {
            case "Underweight":
                return android.R.color.holo_blue_light;
            case "Normal Weight":
                return android.R.color.holo_green_light;
            case "Overweight":
                return android.R.color.holo_orange_light;
            case "Obese":
                return android.R.color.holo_red_light;
            default:
                return android.R.color.black;
        }
    }

}