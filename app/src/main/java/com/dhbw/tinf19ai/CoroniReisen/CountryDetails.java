package com.dhbw.tinf19ai.CoroniReisen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

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

import static android.app.PendingIntent.getActivity;

public class CountryDetails extends AppCompatActivity {
    private TextView tx_country, tx_advice, tx_adviceLink;
    private MapView map_cutout;
    private IMapController mapController;
    private String country_search, coroni;
    private GeoPoint selectedLocation, geoPoint;
    private ImageView im_coroni;
    private Button btn_advice_link;
    public Marker country_marker;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_details);
        im_coroni = (ImageView) findViewById(R.id.image_Coroni);
        tx_country = (TextView) findViewById(R.id.tx_country);
        tx_adviceLink = (TextView) findViewById(R.id.tx_adviceLink);
        tx_advice = (TextView) findViewById(R.id.tx_advice);
        Intent intent = getIntent();
        String country_eingabe = intent.getStringExtra("country");
        //setCoroniImage(country_eingabe, tx_advice);
        tx_country.setText(country_eingabe);

        //Card für PieChart
        CardView pieChart = (CardView) findViewById(R.id.piecard);

        String travelRules = getString(R.string.travelRules);
        tx_adviceLink.setText(travelRules);
        CardView advice_card = (CardView) findViewById(R.id.card_link);
        advice_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://www.auswaertiges-amt.de/de/ReiseUndSicherheit/reise-und-sicherheitshinweise");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        CardView source_card = (CardView) findViewById(R.id.card_source_link);
        source_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://www.bing.com/covid/dev");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        map_cutout = (MapView) findViewById(R.id.map_view);
        mapController = this.map_cutout.getController();
        this.mapController.setZoom(3.0);
        searchAndCenterAddress(country_eingabe);

        im_coroni.setImageResource(R.drawable.coroni_gruen);
        tx_advice.setText(getResources().getString(R.string.advice_green));


        /*val pieChart = PieChart(
                slices = provideSlices(), clickListener = null, sliceStartPoint = 0f, sliceWidth = 80f
        ).build()

        chart.setPieChart(pieChart)

        btn_advice_link = findViewById(R.id.btn_Link);
        btn_advice_link.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });*/

    }

   // private void setCoroniImage(final String country_eingabe, final TextView tx_advice) {}

    public void searchAndCenterAddress(final String country_eingabe) {

        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    GeocoderNominatim geocoderNominatim = new GeocoderNominatim("default-user-agent");
                    Address address = geocoderNominatim.getFromLocationName(country_eingabe, 10).get(0);
                    geoPoint = new GeoPoint(address.getLatitude(), address.getLongitude());
                            setMarkerAndCenter(geoPoint, country_eingabe);
                            selectedLocation = geoPoint;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(runnable);
        executor.shutdown(); // tell executor no more work is coming
       }


    //setzen von neuen Markern
    private void setMarkerAndCenter(GeoPoint geoPoint, final String country_eingabe) {
        country_marker = new Marker(map_cutout);
        country_marker.setPosition(geoPoint);
        country_marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map_cutout.getOverlays().add(country_marker);

        //setzen von Marker Icon (grüner/roter/orangener Coroni)
        final Drawable drawable_gruen = getResources().getDrawable(R.drawable.coroni_gruen);
        final Drawable drawable_orange = getResources().getDrawable(R.drawable.coroni_orange);
        final Drawable drawable_rot = getResources().getDrawable(R.drawable.coroni_red);

        final String green = getResources().getString(R.string.advice_green);
        final String orange = getResources().getString(R.string.advice_orange);
        final String red = getResources().getString(R.string.advice_red);

        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                try {
                    String coroni = CoroniAssignment.getCoroni(country_eingabe);
                    if(coroni.equals("red")) {
                        im_coroni.setImageResource(R.drawable.coroni_red);
                        Bitmap bitmap = ((BitmapDrawable) drawable_rot).getBitmap();
                        Drawable bitmapDrawable_red = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));
                        country_marker.setIcon(bitmapDrawable_red);
                        tx_advice.setText(red);
                    }
                    if(coroni.equals("orange")) {
                        im_coroni.setImageResource(R.drawable.coroni_orange);
                        Bitmap bitmap2 = ((BitmapDrawable) drawable_orange).getBitmap();
                        Drawable bitmapDrawable_orange = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap2, 50, 50, true));
                        country_marker.setIcon(bitmapDrawable_orange);
                        tx_advice.setText(orange);
                    }
                    if(coroni.equals("green")) {
                        im_coroni.setImageResource(R.drawable.coroni_gruen);
                        Bitmap bitmap3 = ((BitmapDrawable) drawable_gruen).getBitmap();
                        Drawable bitmapDrawable_green = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap3, 50, 50, true));
                        country_marker.setIcon(bitmapDrawable_green);
                        tx_advice.setText(green);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(runnable);
        executor.shutdown(); // tell executor no more work is coming
        this.mapController.setCenter(geoPoint);
    }
}
