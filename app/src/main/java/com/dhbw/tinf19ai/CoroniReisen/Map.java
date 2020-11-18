package com.dhbw.tinf19ai.CoroniReisen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;

public class Map extends Fragment {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private static final GeoPoint MANNHEIM_GEO_POINT = new GeoPoint(49.48406198, 8.47564897);

    private MapView mapView;
    private IMapController mapController;
    private EditText et;
    private  String eingabe;
    private GeoPoint selectedLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public CountryDetails cD;


    public Map() {
        // Required empty public constructor
    }

    public static Map newInstance() {
        Map fragment = new Map();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().setUserAgentValue(getActivity().getPackageName());
        requestPermissionsIfNecessary(new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        });
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
        eingabe = et.getText().toString();

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
                mapView.clearAnimation();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    //Setzen vom neuen eingelesenem GeoPoint
    public void searchAndCenterAddress(final String eingabe) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GeocoderNominatim geocoderNominatim = new GeocoderNominatim("default-user-agent");
                    Address address = geocoderNominatim.getFromLocationName(eingabe, 10).get(0);
                    final GeoPoint geoPoint = new GeoPoint(address.getLatitude(), address.getLongitude());
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



    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    //setzen von neuen Markern
    private void setMarkerAndCenter(GeoPoint geoPoint) {
        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(geoPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(startMarker);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("Help");
        Map map= new Map();
        map.setOnInfoWindowClickListener(startMarker);

        //setzen von Marker Icon (grüner/roter/orangener Coroni)
        Drawable dr = getResources().getDrawable(R.drawable.coroni_gruen);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));

        startMarker.setIcon(drawable);

        this.mapController.setCenter(geoPoint);
    }


    @SuppressLint("MissingPermission")
    private void getLatKnownLocation() {
        this.fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // setMarkerAndCenter(new GeoPoint(location.getLatitude(), location.getLongitude()));
            }
        });
    }

    public void setOnInfoWindowClickListener(Marker marker) {
        Intent intent = new Intent(Map.this.getActivity(), CountryDetails.class);
        //Intent intent = new Intent(String.valueOf(CountryDetails.class));
        startActivity(intent);
    }
}
