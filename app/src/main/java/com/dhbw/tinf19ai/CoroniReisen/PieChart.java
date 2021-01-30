package com.dhbw.tinf19ai.CoroniReisen;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PieChart extends AppCompatActivity {
    private TextView tx_title_country;
    private int recoveredCases, deathCases, confirmedCases;
    private String country;
    private String[] countryArray = CountryDetails.countryArray;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart);

        tx_title_country = (TextView) findViewById(R.id.tx_country);


        //set titel matching the country
        Intent intent = getIntent();
        country = intent.getStringExtra("country");
        tx_title_country.setText(country);

        initPieChart();
        setDataPieChart();

    }

    public void initPieChart() {

        com.github.mikephil.charting.charts.PieChart chart = findViewById(R.id.pieChart);

        //remove Description Label
        chart.getDescription().setEnabled(false);

        //enabling the user to rotate chart
        chart.setRotationEnabled(true);
        //adding friction to rotation
        chart.setDragDecelerationFrictionCoef(0.9f);
        //setting first entry start from right hand side
        chart.setRotationAngle(0);
        //highlight entry when tapped
        chart.setHighlightPerTapEnabled(true);
        //adding animation - entries pip up from 0 degree
        chart.animateY(1400, Easing.EaseInOutQuad);
        //color of hole in the middel
        chart.setHoleColor(getResources().getColor(R.color.background));
        //set hole color
        chart.setHoleColor(getResources().getColor(R.color.card));
        //disable Entry Labels
        chart.setDrawEntryLabels(false);
        //disable center Text
        chart.setDrawCenterText(false);


        //set custom marker for displaying data an tap
        CustomMarker my = new CustomMarker(getApplicationContext(), R.layout.tv_content);
        chart.setMarkerView(my);

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDataPieChart() {

        com.github.mikephil.charting.charts.PieChart chart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.pieChart);

        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
        String label = "type";


            String recovered = countryArray[0];
            String confirmed = countryArray[1];
            String deaths = countryArray[2];

            if(!recovered.equals(""))
                recoveredCases = Integer.parseInt(recovered);
            else
                recoveredCases = 0;

            if(!confirmed.equals(""))
                confirmedCases = Integer.parseInt(confirmed);
            else
                confirmedCases = 0;

            if (!deaths.equals(""))
                deathCases = Integer.parseInt(deaths);
            else
                deathCases = 0;



            //initialize data
            Map<String, Integer> typeAmountMap = new HashMap<>();
            Log.i("Put recovered cases: ", recovered);
            typeAmountMap.put("recovered_cases", recoveredCases );

            Log.i("Put death cases: ", deaths);
            typeAmountMap.put("deaths", deathCases);

            Log.i("Put confirmed cases: ", confirmed);
            typeAmountMap.put("confirmed", confirmedCases);


            //initializing colors for the entries
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#304567"));
            colors.add(Color.parseColor("#309967"));
            colors.add(Color.parseColor("#476567"));
            colors.add(Color.parseColor("#890567"));
            colors.add(Color.parseColor("#a35567"));
            colors.add(Color.parseColor("#ff5f67"));

            //input data and fit data into pie chart entry

            for (String type:typeAmountMap.keySet()
            ) {
                pieEntries.add(new
                        PieEntry(typeAmountMap.get(type).floatValue(), type));
            }


            //collecting the entries with label name
            PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
            //setting text size of the value
            pieDataSet.setValueTextSize(18f);
            //providing color list for coloring different entries
            pieDataSet.setColors(colors);
            //grouping the data set from entry to chart
            PieData pieData = new PieData(pieDataSet);
            //showing the value of the entries, default true if not set
            pieData.setDrawValues(false);

            chart.setData(pieData);
            chart.invalidate();

        }
    }

