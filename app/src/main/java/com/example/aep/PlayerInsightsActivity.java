package com.example.aep;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Cartesian3d;
import com.anychart.charts.Pie;

import com.anychart.core.cartesian.series.Bar;
import com.anychart.core.cartesian.series.Column3d;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.HoverMode;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.graphics.vector.SolidFill;
import com.anychart.palettes.RangeColors;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.aep.LoginActivity;


public class PlayerInsightsActivity extends AppCompatActivity {

    // private FirebaseDatabase database ;
    private TextView playerInfoTextView;
    private PieChart pieChart;
    private DonutProgress ratingDonutProgress;
    String current_user_fullname;
    private AnyChartView anyChartView;
    private BarChart barChart;
    private AnyChartView anyChartView_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_insights);


        barChart = findViewById(R.id.barChart);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);

        //pieChart = findViewById(R.id.pieChart);

        // Additional chart customization
//        pieChart.getDescription().setEnabled(false);

       /*//rating chart
        ratingDonutProgress = findViewById(R.id.ratingDonutProgress);
        ratingDonutProgress.setMax(100);*/
       //anyChartView_bar = findViewById(R.id.any_chart_view);
        anyChartView = findViewById(R.id.anyChartView);


        //anyChartView_bar = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Intent intent = getIntent();

        if (intent.hasExtra("playerFullname")) {
            // Retrieve the string from the Intent
            current_user_fullname = intent.getStringExtra("playerFullname");
        }

        playerInfoTextView = findViewById(R.id.playerInfoTextView);

        // Read data from Firebase
        readPlayerData();
    }

    private List<String> getXAxisLabels() {
        List<String> labels = new ArrayList<>();
        labels.add("Centuries");
        labels.add("Half-centuries");
        labels.add("Wickets");
        labels.add("Catches");
        return labels;
    }


    private void readPlayerData() {
        // Add a ValueEventListener to read data from Firebase
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("playerstats").document(current_user_fullname).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            playerInfoTextView.setText(current_user_fullname);

                            // Create a Cartesian chart with vertical bars
                            /*Cartesian barChart = AnyChart.bar();

                            // Add data to the bar chart
                            List<DataEntry> data_bar = new ArrayList<>();
                            data_bar.add(new ValueDataEntry("Centuries", document.getLong("totalCenturies")));
                            data_bar.add(new ValueDataEntry("Half Centuries", document.getLong("totalHalfCenturies")));
                            data_bar.add(new ValueDataEntry("Wickets Taken", document.getLong("wicketsTaken")));
                            data_bar.add(new ValueDataEntry("Catches Taken", document.getLong("catchesTaken")));

                            barChart.data(data_bar);

                            // Customize the bar chart (if needed)
                            barChart.title("Player Statistics");
                            // Add more customizations as per your requirements

                            // Display the bar chart in AnyChartView
                             // Replace with your AnyChartView ID
                            anyChartView_bar.setChart(barChart);
                            anyChartView_bar.invalidate();*/

                            // bar graph
                            List<BarEntry> entries = new ArrayList<>();
                            entries.add(new BarEntry(1, document.getLong("totalCenturies"))); // Centuries
                            entries.add(new BarEntry(2, document.getLong("totalHalfCenturies"))); // Half-centuries
                            entries.add(new BarEntry(3, document.getLong("wicketsTaken"))); // Wickets
                            entries.add(new BarEntry(4, document.getLong("catchesTaken"))); // Catches

                            BarDataSet dataSet = new BarDataSet(entries, "Performance Stats");

                            BarData barData = new BarData(dataSet);
                            barChart.setData(barData);

                            // Customize the x-axis labels
                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisLabels()));
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setGranularity(1);

                            barChart.invalidate();

                            Integer ballsPlayed = document.getLong("ballsPlayed").intValue();
                            Integer oversBowled = document.getLong("oversBowled").intValue() * 6;
                            /*// Pie chart

                            // Example data


                            List<PieEntry> entries_pie = new ArrayList<>();
                            entries_pie.add(new PieEntry(ballsPlayed, "Balls Played"));
                            entries_pie.add(new PieEntry(oversBowled, "Overs Bowled"));

                            PieDataSet dataSet_pie = new PieDataSet(entries_pie, "Performance Distribution");

                            // Set colors for the chart
                            dataSet_pie.setColors(ColorTemplate.COLORFUL_COLORS);

                            PieData pieData = new PieData(dataSet_pie);
                            pieChart.setData(pieData);

                            pieChart.invalidate();*/

                            /*//rating chart
                             // Replace with the actual rating value from your database
                           // ratingDonutProgress.setProgress(0);
                            new Handler().postDelayed(() -> {
                                Log.d("piediagram", "inside dognut ");
                                @SuppressLint("ObjectAnimatorBinding") ObjectAnimator anim = ObjectAnimator.ofInt(ratingDonutProgress, "donut_progress", ratingValue);
                                anim.setDuration(100); // Set the duration of the animation in milliseconds
                                anim.start();
                            }, 500);
                            //ratingDonutProgress.setProgress(ratingValue);*/


                            // AnyCharts
                            /*int ratingValue = document.getLong("rating").intValue()*10;
                            com.anychart.charts.Pie pie = AnyChart.pie();
                            List<DataEntry> data = new ArrayList<>();
                            data.add(new ValueDataEntry("Rating", ratingValue));
                            Log.d("Ratingvalue", String.valueOf(ratingValue));
                            pie.data(data);

                            // Enable animation and set the animation duration
                            pie.animation().enabled(true);
                            pie.animation().duration(1000); // Set the duration of the animation in milliseconds

                            // Customize chart settings if needed
                            pie.legend().title().enabled(true).text("Legend Title");

                            // Display the chart
                            anyChartView.setChart(pie);*/

                            // Set background color
                            //anyChartView.setBackgroundColor("#4CAF50");
                            //Anycharts pie diagram
                            List<DataEntry> data = new ArrayList<>();
                            data.add(new ValueDataEntry("Balls Played", ballsPlayed));
                            data.add(new ValueDataEntry("Overs Bowled", oversBowled));

                            // Set the data to the pie chart
                            // Set set = Set.instantiate();
                            //set.data(data);

                            // Create a pie chart and set the data
                            com.anychart.charts.Pie pie = AnyChart.pie();
                            pie.data(data);


                            // Customize the appearance of the pie chart (optional)
                            pie.title("Balls Played vs Overs Bowled");
                            pie.labels().position("outside");
                            pie.legend().title().enabled(true);
                            pie.animation(true);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    1000 // Set your desired height here
                            );

                            anyChartView.setLayoutParams(layoutParams);

                            RangeColors palette = RangeColors.instantiate();
                            palette.items("#ff0000", "#00ff00");
                            palette.count(10);
                            pie.palette(palette);

                            // Display the pie chart

                            anyChartView.setChart(pie);

                            // Adjust the size programmatically (optional)
                            //adjustChartSize();


                        } else {
                            // Handle the case where the document doesn't exist
                            Log.e("PlayerStatisticsActivity", "Document does not exist");
                        }
                    } else {
                        // Handle exceptions
                        Log.e("PlayerStatisticsActivity", "Error getting TotalHalfCenturies", task.getException());
                    }
                });

    }

    private void adjustChartSize() {
        // Set your desired width and height programmatically
        int desiredWidth = 600; // in pixels
        int desiredHeight = 600; // in pixels

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        anyChartView.setLayoutParams(layoutParams);
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }

}