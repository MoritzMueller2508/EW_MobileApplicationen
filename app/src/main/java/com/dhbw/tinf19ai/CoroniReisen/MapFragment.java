package com.dhbw.tinf19ai.CoroniReisen;

/**
 * This class represents the selection of the country to be considered with a map.
 * The user can search for individual countries.
 * When clicking on a marker of an entered country, the user is redirected to the last UI for the country details.
 * There are also vacation categories which are represented by buttons.
 * By clicking on them, the user will be redirected to the appropriate list of countries.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapFragment extends Fragment {
    private MapView mapView;
    private IMapController mapController;
    public EditText et;
    public static String eingabe, btn;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public Marker startMarker;
    public static GeoPoint geoPoint;
    boolean internetConnection = MainActivity.internetConnection;
    private final static String TAG = "MapFragment";
    private Hashtable<String, String> countriesDict = CountryDictionary.countriesDict;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


/*    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "On Resume .....");
        if (!internetConnection){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setMessage("Die Daten die Sie bei der App sehen sind nicht aktuell, " +
                    "da Sie über eine Internetverbindung nicht verfügen. Sobald sie online gehen, " +
                    "werden die Daten aktualisiert.");
            alertDialogBuilder.setTitle("Keine Internetverbindung");
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        //MapFragment is initialized
        this.mapView = (MapView) view.findViewById(R.id.map_view);
        this.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        this.mapController = this.mapView.getController();
        this.mapController.setZoom(3.0);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        //Input from the user is imported
        this.et = (EditText) view.findViewById(R.id.et_address_input);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        //Button for displaying the user request by manual input
        Button btn_suchen = view.findViewById(R.id.btn_go);
        btn_suchen.setOnClickListener(v -> {
            String string = et.getText().toString();
            String location = string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();

            //check if input is null
            if (location.trim().length() == 0) {
                alertDialogBuilder.setMessage("Bitte geben Sie ein Land ein.");
                alertDialogBuilder.setTitle("Input leer.");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            //check if input is in dictionary
            else if (!countriesDict.containsKey(location) && !countriesDict.containsValue(location)) {
                alertDialogBuilder.setMessage("Bitte geben Sie ein existierendes Land ein.");
                alertDialogBuilder.setTitle("Land existiert nicht");
                AlertDialog alertDialog = alertDialogBuilder.create();
                et.getText().clear();
                alertDialog.show();
            } else {
                Log.d(TAG, "internet connection: " + internetConnection);
                if (internetConnection) {
                    searchAndCenterAddress(location);
                } else {
                    final Activity context2 = getActivity();
                    Intent intent = new Intent(context2, CountryDetails.class);
                    if (countriesDict.containsKey(location)) {
                        eingabe = CountryDictionary.getCountryInGerman(location);
                    } else {
                        eingabe = location;
                    }
                    intent.putExtra("country", eingabe);
                    startActivity(intent);
                }
            }

        });

        //buttons for categories of vacation destinations
        Button btn_sonne = view.findViewById(R.id.btn_sonne);
        btn_sonne.setOnClickListener(v -> {
            btn = "sonne";
            Intent intent = new Intent(getActivity(), DestinationsList.class);
            startActivity(intent);
        });
        Button btn_berge = view.findViewById(R.id.btn_berge);
        btn_berge.setOnClickListener(v -> {
            btn = "berge";
            Intent intent = new Intent(getActivity(), DestinationsList.class);
            startActivity(intent);
        });
        Button btn_stadt = view.findViewById(R.id.btn_stadt);
        btn_stadt.setOnClickListener(v -> {
            btn = "stadt";
            Intent intent = new Intent(getActivity(), DestinationsList.class);
            startActivity(intent);
        });
        Button btn_natur = view.findViewById(R.id.btn_natur);
        btn_natur.setOnClickListener(v -> {
            btn = "natur";
            Intent intent = new Intent(getActivity(), DestinationsList.class);
            startActivity(intent);
        });

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        return view;
    }


    //set from new read GeoPoint
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void searchAndCenterAddress(final String tx_eingabe) {

        new Thread(() -> {
            try {
                GeocoderNominatim geocoderNominatim = new GeocoderNominatim("default-user-agent");
                Address address = geocoderNominatim.getFromLocationName(tx_eingabe, 10).get(0); //get address from input //if country does not exists, app will crash
                //get geopoint always by country, no matter if searched by city or country
                String country = address.getCountryName(); //get country-name from address
                //geoPoint = new GeoPoint(address.getLatitude(), address.getLongitude());
                System.out.println(countriesDict.get(country));
                if (countriesDict.containsKey(country)) {
                    eingabe = CountryDictionary.getCountryInGerman(country);
                } else {
                    eingabe = tx_eingabe;
                }
                System.out.println(eingabe);

                Address countryAddress = geocoderNominatim.getFromLocationName(eingabe, 10).get(0); //get country-address from country-name
                geoPoint = new GeoPoint(countryAddress.getLatitude(), countryAddress.getLongitude()); //set geopoint from country-name
                getActivity().runOnUiThread(() -> setMarkerAndCenter(geoPoint, eingabe));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //set new markers
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMarkerAndCenter(final GeoPoint geoPoint, final String eingabe) {
        startMarker = new Marker(mapView);
        startMarker.setPosition(geoPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(startMarker);

        startMarker.setDraggable(true);
        startMarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {
                marker.remove(mapView);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                marker.remove(mapView);
            }

            @Override
            public void onMarkerDragStart(Marker marker) {
                marker.remove(mapView);
            }
        });

        final Activity currentActivity = getActivity();
        startMarker.setOnMarkerClickListener((marker, mapView) -> {
                    Intent intent = new Intent(currentActivity, CountryDetails.class);
                    intent.putExtra("country", eingabe);
                    startActivity(intent);
                    return false;
                }

        );

        this.mapController.setCenter(geoPoint);
    }

}
