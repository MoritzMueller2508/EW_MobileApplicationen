package com.dhbw.tinf19ai.CoroniReisen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.MarkerOptions;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Locale;

import static android.app.PendingIntent.getActivity;
import static java.util.Locale.forLanguageTag;

public class CountryDetails extends AppCompatActivity {
    private TextView tx_country, tx_advice;
    private MapView map_cutout;
    private IMapController mapController;
    private String country_search, country_eingabe;
    private GeoPoint selectedLocation, geoPoint;
    private ImageView im_coroni;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_details);
        im_coroni = (ImageView) findViewById(R.id.image_Coroni);
        setCoroniImage();
        tx_country = (TextView) findViewById(R.id.tx_country);
        country_eingabe = Map.eingabe;
        tx_country.setText(country_eingabe);
        tx_country.setTextSize(40);

        tx_advice = (TextView) findViewById(R.id.tx_advice);
        String advice_red = getString(R.string.Advice);
        String advice_link = getString(R.string.Advice_Link);
        tx_advice.setText(advice_red+advice_link);

        map_cutout = (MapView) findViewById(R.id.map_view);
        mapController = this.map_cutout.getController();
        this.mapController.setZoom(10.0);
        searchAndCenterAddress();

        /*val pieChart = PieChart(
                slices = provideSlices(), clickListener = null, sliceStartPoint = 0f, sliceWidth = 80f
        ).build()

        chart.setPieChart(pieChart)
*/
    }

    private void setCoroniImage() {
        im_coroni.setImageResource(R.drawable.coroni_gruen);
    }
    public void searchAndCenterAddress() {
            geoPoint = Map.geoPoint;
            setMarkerAndCenter(geoPoint);
            selectedLocation = geoPoint;
       }



    //setzen von neuen Markern
    private void setMarkerAndCenter(GeoPoint geoPoint) {
        Marker country_marker = new Marker(map_cutout);
        country_marker.setPosition(geoPoint);
        country_marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map_cutout.getOverlays().add(country_marker);
        //setzen von Marker Icon (grüner/roter/orangener Coroni)
        Drawable dr = getResources().getDrawable(R.drawable.coroni_gruen);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));
        country_marker.setIcon(drawable);
        this.mapController.setCenter(geoPoint);
    }

  
}
