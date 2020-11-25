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
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.MarkerOptions;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.BuildConfig;
import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapFragment extends Fragment {
    private MapView mapView;
    private IMapController mapController;
    public static EditText et;
    public static String tx_eingabe, eingabe, coroni, btn;
    private ArrayList btn_eingabe;
    private static GeoPoint selectedLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public CountryDetails cD;
    public Marker startMarker;
    public static GeoPoint geoPoint;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        //MapFragment wird initialisiert
        this.mapView = (MapView) view.findViewById(R.id.map_view);
        this.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        this.mapController = this.mapView.getController();
        this.mapController.setZoom(3.0);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        //Eingabe vom User werden eingelesen
        this.et = (EditText) view.findViewById(R.id.et_address_input);
        tx_eingabe = et.getText().toString();


        //Button zur Darstellung der Useranfrage durch manuelle Eingabe
        Button btn_suchen = view.findViewById(R.id.btn_go);
        btn_suchen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String location = et.getText().toString();
                searchAndCenterAddress(location);
            }
        });

        //Buttons für Kategorien von Urlaubszielen
        Button btn_sonne = view.findViewById(R.id.btn_sonne);
        btn_sonne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn="sonne";
                Intent intent = new Intent(getActivity(), DestinationsList.class);
                startActivity(intent);
            }
        });


        Button btn_berge = view.findViewById(R.id.btn_berge);
        btn_berge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn="berge";
                Intent intent = new Intent(getActivity(), DestinationsList.class);
                startActivity(intent);
            }
        });

        Button btn_stadt = view.findViewById(R.id.btn_stadt);
        btn_stadt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn="stadt";
                Intent intent = new Intent(getActivity(), DestinationsList.class);
                startActivity(intent);
            }
        });

        final Button btn_natur = view.findViewById(R.id.btn_natur);
        btn_natur.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn="natur";
                Intent intent = new Intent(getActivity(), DestinationsList.class);
                startActivity(intent);
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
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            setMarkerAndCenter(geoPoint, eingabe);
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMarkerAndCenter(GeoPoint geoPoint, final String eingabe) {
        startMarker = new Marker(mapView);
        startMarker.setPosition(geoPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(startMarker);
        MarkerOptions markerOptions = new MarkerOptions();

        //setzen von Marker Icon (grüner/roter/orangener Coroni)
        final Drawable drawable_green = getResources().getDrawable(R.drawable.coroni_gruen);
        final Drawable drawable_orange = getResources().getDrawable(R.drawable.coroni_orange);
        final Drawable drawable_red = getResources().getDrawable(R.drawable.coroni_red);

        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    String coroni = CoroniAssignment.getCoroni(eingabe);
                    if(coroni.equals("red")) {
                        Bitmap bitmap = ((BitmapDrawable) drawable_red).getBitmap();
                        Drawable bitmapDrawable_red = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));
                        startMarker.setIcon(bitmapDrawable_red);
                    }
                    if(coroni.equals("orange")) {
                        Bitmap bitmap2 = ((BitmapDrawable) drawable_orange).getBitmap();
                        Drawable bitmapDrawable_orange = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap2, 60, 60, true));
                        startMarker.setIcon(bitmapDrawable_orange);
                    }
                    if(coroni.equals("green")) {
                        Bitmap bitmap3 = ((BitmapDrawable) drawable_green).getBitmap();
                        Drawable bitmapDrawable_green = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap3, 60, 60, true));
                        startMarker.setIcon(bitmapDrawable_green);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(runnable);
        executor.shutdown(); // tell executor no more work is coming


        final Activity context = getActivity();
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


}
