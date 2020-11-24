package com.dhbw.tinf19ai.CoroniReisen;

import android.annotation.SuppressLint;
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

import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;

public class Map extends Fragment {
    private MapView mapView;
    private IMapController mapController;
    public static EditText et;
    public static String tx_eingabe, eingabe;
    private ArrayList btn_eingabe;
    private GeoPoint selectedLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public CountryDetails cD;
    public Marker startMarker;
    public static GeoPoint geoPoint;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity, container, false);
        //Map wird initialisiert
        this.mapView = (MapView) view.findViewById(R.id.map_view);
        this.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        this.mapController = this.mapView.getController();
        this.mapController.setZoom(3.0);

        //Eingabe vom User werden eingelesen
        this.et = (EditText) view.findViewById(R.id.et_address_input);
        tx_eingabe = et.getText().toString();

        //Button zur Darstellung der Useranfrage durch manuelle Eingabe
        final Button btn_suchen = view.findViewById(R.id.btn_go);
        btn_suchen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String location = et.getText().toString();
                searchAndCenterAddress(location);
            }
        });

        //Buttons für Kategorien von Urlaubszielen
        final Button btn_sonne = view.findViewById(R.id.btn_sonne);
        btn_sonne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchAndCenterAddress("Spanien");
                searchAndCenterAddress("Italien");
                searchAndCenterAddress("Madagascar");
                searchAndCenterAddress("Australien");
                searchAndCenterAddress("Türkei");
                searchAndCenterAddress("Malta");
                searchAndCenterAddress("Hawaii");
                searchAndCenterAddress("Ägypten");
                searchAndCenterAddress("Brasilien");
                searchAndCenterAddress("Mexico");
            }
        });

        final Button btn_berge = view.findViewById(R.id.btn_berge);
        btn_berge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchAndCenterAddress("Schweiz");
                searchAndCenterAddress("USA");
                searchAndCenterAddress("Deutschland");
                searchAndCenterAddress("Australien");
                searchAndCenterAddress("England");
                searchAndCenterAddress("Nordmazedonien");
                searchAndCenterAddress("Italien");
                searchAndCenterAddress("Griechenland");
                searchAndCenterAddress("Belgien");
                searchAndCenterAddress("Slowakei");
            }
        });

        final Button btn_stadt = view.findViewById(R.id.btn_stadt);
        btn_stadt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchAndCenterAddress("Spanien");
                searchAndCenterAddress("Polen");
                searchAndCenterAddress("Englandr");
                searchAndCenterAddress("Indien");
                searchAndCenterAddress("Türkei");
                searchAndCenterAddress("Japan");
                searchAndCenterAddress("China");
                searchAndCenterAddress("Deutschland");
                searchAndCenterAddress("Russland");
                searchAndCenterAddress("USA");
            }
        });

        final Button btn_natur = view.findViewById(R.id.btn_natur);
        btn_natur.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchAndCenterAddress("Marokko");
                searchAndCenterAddress("Nordamerika");
                searchAndCenterAddress("Neuseeland");
                searchAndCenterAddress("Japan");
                searchAndCenterAddress("Iran");
                searchAndCenterAddress("Deutschland");
                searchAndCenterAddress("Österreich");
                searchAndCenterAddress("Thailand");
                searchAndCenterAddress("Norwegen");
                searchAndCenterAddress("Irland");
            }
        });

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        return view;
    }

    //Setzen vom neuen eingelesenem GeoPoint
    public void searchAndCenterAddress(final String tx_eingabe) {
        eingabe = tx_eingabe;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GeocoderNominatim geocoderNominatim = new GeocoderNominatim("default-user-agent");
                    Address address = geocoderNominatim.getFromLocationName(eingabe, 10).get(0);
                    geoPoint = new GeoPoint(address.getLatitude(), address.getLongitude());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setMarkerAndCenter(geoPoint);
                            selectedLocation = geoPoint;
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //setzen von neuen Markern
    private void setMarkerAndCenter(GeoPoint geoPoint) {
        startMarker = new Marker(mapView);
        startMarker.setPosition(geoPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(startMarker);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("Help");
        Map map= new Map();
        //map.setOnInfoWindowClickListener(startMarker);

        //setzen von Marker Icon (grüner/roter/orangener Coroni)
        Drawable dr = getResources().getDrawable(R.drawable.coroni_gruen);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));
        final Activity context = getActivity();
        startMarker.setIcon(drawable);
        startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                                                 @Override
                                                 public boolean onMarkerClick(Marker marker, MapView mapView) {
                                                     Log.d("Test", "Help");
                                                     Intent intent = new Intent(context, CountryDetails.class);
                                                     startActivity(intent);

                                                     return false;
                                                 }
                                             }

        );

        this.mapController.setCenter(geoPoint);
    }

    /*private void setMarkerAndCenter1(GeoPoint geoPoint) {
        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(geoPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(startMarker);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("Help");
        Map map= new Map();
        //map.setOnInfoWindowClickListener(startMarker);

        //setzen von Marker Icon (grüner/roter/orangener Coroni)
        Drawable dr = getResources().getDrawable(R.drawable.coroni_gruen);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));

        startMarker.setIcon(drawable);

        this.mapController.setCenter(geoPoint);
    }*/
}
