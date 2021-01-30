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

import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CountryDetails extends AppCompatActivity {
    private TextView tx_title_country, tx_advice;
    private MapView map_cutout;
    private IMapController mapController;
    private GeoPoint geoPoint;
    private ImageView im_coroni;
    public Marker country_marker;
    boolean internetConnection = MainActivity.internetConnection;
    private final static String TAG = "CountryDetails";

    private String country_eingabe, country;
    public static String[] countryArray;

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
        country_eingabe = intent.getStringExtra("country");
        tx_title_country.setText(country_eingabe);

        //set cards
        CardView pieChart_card = (CardView) findViewById(R.id.piecard);
        CardView advice_card = (CardView) findViewById(R.id.card_einreisebestimmungen);
        CardView source_card = (CardView) findViewById(R.id.card_source_link);
        CardView coroni_card = (CardView) findViewById(R.id.card_coroni);

        searchAndCenterAddress(country_eingabe);
        setLinks(advice_card, source_card, coroni_card, pieChart_card);



        AsyncTask.execute(() -> {
            country = country_eingabe;
            if(!CountryDictionary.countriesDict.containsKey(country_eingabe) && !CountryDictionary.countriesDict.containsValue(country_eingabe)) {
                    try {
                        throw new Exception("Failed");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            else {
                    if (CountryDictionary.countriesDict.containsValue(country_eingabe))
                        country = CountryDictionary.getCountryInEnglish(country_eingabe);
            }

            try {
                countryArray = BingData.getArrayCountry(country);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });




    }



    //set links for clickable cards
    private void setLinks(CardView advice_card, CardView source_card, CardView coroni_card, CardView pieChart_card) {
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
        pieChart_card.setOnClickListener(view-> {
            Intent intent = new Intent(this, com.dhbw.tinf19ai.CoroniReisen.PieChart.class);
            intent.putExtra("country", country_eingabe);
            if (countryArray != null) {
            startActivity(intent);
            }
            else {
                new Handler().postDelayed(() -> {
                    startActivity(intent);
                }, 7000);   //7 seconds
            }
        });
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
    private void setMarkerAndCenter(GeoPoint geoPoint, final String country_eingabe) throws IOException {
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
