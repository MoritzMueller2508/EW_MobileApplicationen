package com.dhbw.tinf19ai.CoroniReisen;

/**
 * This class displays the data for a matching country.
 * The class contains 5 cards with
 * a map and the matching coroni,
 * our travel advices,
 * a reference to the entry requirements of the foreign office,
 * the current numbers in the form of a pie chart and
 * a reference to our data source.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.app.PendingIntent.getActivity;

public class CountryDetails extends AppCompatActivity {
    private TextView tx_title_country, tx_advice;
    private MapView map_cutout;
    private IMapController mapController;
    private GeoPoint geoPoint;
    private ImageView im_coroni;
    public Marker country_marker;
    boolean internetConnection = MainActivity.internetConnection;
    private final static String TAG = "CountryDetails";



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_details);



        //set map
        map_cutout = (MapView) findViewById(R.id.map_view);
        mapController = this.map_cutout.getController();
        this.mapController.setZoom(3.0);

        //set textViews
        tx_title_country = (TextView) findViewById(R.id.tx_country);
        tx_advice = (TextView) findViewById(R.id.tx_advice);

        //set image
        im_coroni = (ImageView) findViewById(R.id.image_coroni);

        //set titel matching the country
        Intent intent = getIntent();
        String country_eingabe = intent.getStringExtra("country");
        tx_title_country.setText(country_eingabe);

        //set cards
        CardView pieChart_card = (CardView) findViewById(R.id.piecard);
        CardView advice_card = (CardView) findViewById(R.id.card_einreisebestimmungen);
        CardView source_card = (CardView) findViewById(R.id.card_source_link);
        CardView coroni_card = (CardView) findViewById(R.id.card_coroni);

        searchAndCenterAddress(country_eingabe);
        setLinks(advice_card, source_card, coroni_card);

        //Chart


        initPieChart();


        try {
            setDataPieChart();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }



    //set links for clickable cards
    private void setLinks(CardView advice_card, CardView source_card, CardView coroni_card) {
        advice_card.setOnClickListener(view -> {
            Uri uriUrl = Uri.parse("https://www.auswaertiges-amt.de/de/ReiseUndSicherheit/reise-und-sicherheitshinweise");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        });
        source_card.setOnClickListener(view -> {
            Uri uriUrl = Uri.parse("https://www.bing.com/covid/dev");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        });
        coroni_card.setOnClickListener(view -> {
            Uri uriUrl = Uri.parse("https://www.rki.de/DE/Content/InfAZ/N/Neuartiges_Coronavirus/Risikogebiete_neu.html");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        });
    }

    private void initPieChart() {

        PieChart chart = findViewById(R.id.pieChart);

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
    private void setDataPieChart() throws Exception {

        PieChart chart = (PieChart) findViewById(R.id.pieChart);

        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
        String label = "type";
        Intent intent = getIntent();
        System.out.println("Till Here II");

        String country =intent.getStringExtra("country");

        if(!CountryDictionary.countriesDict.containsKey(country) && !CountryDictionary.countriesDict.containsValue(country)){

            throw new Exception("Failed");
        }

        else {

            if (CountryDictionary.countriesDict.containsValue(country))
                country = CountryDictionary.getCountryInEnglish(country);



            System.out.println("Help");

            String recovered = BingData.getRecoveredCases(country);
            String confirmed = BingData.getConfirmedCases(country);
            String deaths = BingData.getDeathsCases(country);

            int recoveredInt;
            int deathsInt;
            int confirmedInt;

            if(!recovered.equals(""))
                recoveredInt = Integer.parseInt(recovered);
            else
                recoveredInt = 0;

            if(!confirmed.equals(""))
                confirmedInt = Integer.parseInt(confirmed);
            else
                confirmedInt = 0;

            if (!deaths.equals(""))
                deathsInt = Integer.parseInt(deaths);
            else
                deathsInt = 0;



            //initialize data
            Map<String, Integer> typeAmountMap = new HashMap<>();
            Log.i("Put recovered cases: ", recovered);
            typeAmountMap.put("recovered_cases",recoveredInt );

            Log.i("Put death cases: ", deaths);
            typeAmountMap.put("deaths", deathsInt);

            Log.i("Put confirmed cases: ", confirmed);
            typeAmountMap.put("confirmed", confirmedInt);


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
            pieDataSet.setValueTextSize(12f);
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

    //set geoPoint with address and the user input
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void searchAndCenterAddress(final String country_eingabe) {
        Runnable runnable = () -> {
            try {
                Log.d(TAG, "internetConnection: " + internetConnection);
                if (internetConnection) {
                    GeocoderNominatim geocoderNominatim = new GeocoderNominatim("default-user-agent");
                    Address address = geocoderNominatim.getFromLocationName(country_eingabe, 10).get(0);
                    geoPoint = new GeoPoint(address.getLatitude(), address.getLongitude());
                } else {
                    geoPoint = null;
                }
                setMarkerAndCenter(geoPoint, country_eingabe);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(runnable);
        executor.shutdown(); // tell executor no more work is coming
    }


    //set new markers
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMarkerAndCenter(GeoPoint geoPoint, final String country_eingabe) {
        if (internetConnection) {
            country_marker = new Marker(map_cutout);
            country_marker.setPosition(geoPoint);
            country_marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map_cutout.getOverlays().add(country_marker);
        }

        //set advice text
        final String green = getResources().getString(R.string.advice_green);
        final String orange = getResources().getString(R.string.advice_orange);
        final String red = getResources().getString(R.string.advice_red);

        //preset
        tx_advice.setText(green);

        //set marker image, Coroni image and advice text with user input
        Runnable runnable = () -> {
            try {
                String coroni = CoroniAssignment.getCoroni(country_eingabe);
                if (coroni.equals("red")) {
                    im_coroni.setImageResource(R.drawable.coroni_red);
                    tx_advice.setText(red);
                }
                if (coroni.equals("orange")) {
                    im_coroni.setImageResource(R.drawable.coroni_orange);
                    tx_advice.setText(orange);
                }
                if (coroni.equals("green")) {
                    im_coroni.setImageResource(R.drawable.coroni_green);
                    tx_advice.setText(green);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(runnable);
        executor.shutdown(); // tell executor no more work is coming
        if (internetConnection) {
            this.mapController.setCenter(geoPoint);
        }
    }
}
