package com.dhbw.tinf19ai.CoroniReisen;

/**
 * This interface shows a PieChart with data for the selected country from:
 * - Confirmed cases
 * - Death cases
 * - Recovered cases
 * With a button you can return to the country details.
 */

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.github.mikephil.charting.components.Legend.LegendDirection.LEFT_TO_RIGHT;
import static com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT;

public class PieChart extends AppCompatActivity {
    //initialize values and objects
    private TextView tx_title_country;
    private int recoveredCases, deathCases, confirmedCases;
    private String country;
    private final String[] countryArray = CountryDetails.countryArray;
    private final static String TAG = "PieChart";


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart);

        tx_title_country = findViewById(R.id.tx_country);
        ImageButton button = findViewById(R.id.btn_PieBack);
        TextView textView = findViewById(R.id.tv_lastUpdateChart);
        textView.setText("last update: " + countryArray[3]);

        //set titel matching the country
        Intent intent = getIntent();
        country = intent.getStringExtra("country");
        tx_title_country.setText(country);

        initPieChart();
        setDataPieChart();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: back to CountryDetails_Activity");
                finish();
            }
        });

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
        //
        chart.getLegend().setTextSize(19f);
        Legend legend = chart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);



        //set custom marker for displaying data an tap
        CustomMarker my = new CustomMarker(getApplicationContext(), R.layout.tv_content);
        chart.setMarkerView(my);
        Log.i(TAG, "initPieChart: PieChart parameter set " +
                "Description(false), Rotation(true), DragDeceleration(true), " +
                "HighlightTap(true), animation(true), holeColor(background), LabelText(false)");

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDataPieChart() {

        com.github.mikephil.charting.charts.PieChart chart = findViewById(R.id.pieChart);

        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
        String label = "";


        String recovered = countryArray[2];
        String confirmed = countryArray[0];
        String deaths = countryArray[1];

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

        Log.i(TAG, "onCreate: PieChart drawn" +
            "country(" + "intent.getStringExtra('country') + ),"  +
            "TextSize(12f), DrawValues(false)");
        chart.invalidate();




        }
    }

