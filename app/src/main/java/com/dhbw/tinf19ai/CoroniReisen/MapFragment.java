package com.dhbw.tinf19ai.CoroniReisen;

/**
 * This class represents the selection of the country to be considered with a map.
 * The user can search for individual countries and display the corresponding Coronis on the map.
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapFragment extends Fragment {
    private MapView mapView;
    private IMapController mapController;
    public static EditText et;
    public static String eingabe, coroni, btn;
    private static GeoPoint selectedLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public Marker startMarker;
    public static GeoPoint geoPoint;
    boolean internetConnection = MainActivity.internetConnection;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

        //Button for displaying the user request by manual input
        Button btn_suchen = view.findViewById(R.id.btn_go);
        btn_suchen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String location = et.getText().toString();

                //check if input is null
                if (location.trim().length() == 0) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("Bitte geben Sie ein Land ein.");
                    alertDialogBuilder.setTitle("Input leer.");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    if (internetConnection) {
                        try {
                            System.out.println("hello");
                            searchAndCenterAddress(location);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        final Activity context2 = getActivity();
                        Intent intent = new Intent(context2, CountryDetails.class);
                        intent.putExtra("country", location);
                        startActivity(intent);
                    }
                }
            }
        });

        //buttons for categories of vacation destinations
        Button btn_sonne = view.findViewById(R.id.btn_sonne);
        btn_sonne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn = "sonne";
                Intent intent = new Intent(getActivity(), DestinationsList.class);
                startActivity(intent);
            }
        });
        Button btn_berge = view.findViewById(R.id.btn_berge);
        btn_berge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn = "berge";
                Intent intent = new Intent(getActivity(), DestinationsList.class);
                startActivity(intent);
            }
        });
        Button btn_stadt = view.findViewById(R.id.btn_stadt);
        btn_stadt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn = "stadt";
                Intent intent = new Intent(getActivity(), DestinationsList.class);
                startActivity(intent);
            }
        });
        Button btn_natur = view.findViewById(R.id.btn_natur);
        btn_natur.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn = "natur";
                Intent intent = new Intent(getActivity(), DestinationsList.class);
                startActivity(intent);
            }
        });

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        return view;
    }


    //set from new read GeoPoint
    private void searchAndCenterAddress(final String tx_eingabe) throws IOException {
        if (CountryDictionary.countriesDict.containsKey(tx_eingabe)){
            eingabe = CountryDictionary.getCountryInGerman(tx_eingabe);
        } else {
            eingabe = tx_eingabe;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GeocoderNominatim geocoderNominatim = new GeocoderNominatim("default-user-agent");
                    Address address = geocoderNominatim.getFromLocationName(tx_eingabe, 10).get(0);
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

    //set new markers
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMarkerAndCenter(final GeoPoint geoPoint, final String eingabe) {
        startMarker = new Marker(mapView);
        startMarker.setPosition(geoPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(startMarker);

        //set marker icon (green/red/orange coroni)
        final Drawable drawable_green = getResources().getDrawable(R.drawable.coroni_green);
        final Drawable drawable_orange = getResources().getDrawable(R.drawable.coroni_orange);
        final Drawable drawable_red = getResources().getDrawable(R.drawable.coroni_red);

        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    String coroni = CoroniAssignment.getCoroni(eingabe);
                    if (coroni.equals("red")) {
                        Bitmap bitmap = ((BitmapDrawable) drawable_red).getBitmap();
                        Drawable bitmapDrawable_red = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));
                        startMarker.setIcon(bitmapDrawable_red);
                    }
                    if (coroni.equals("orange")) {
                        Bitmap bitmap2 = ((BitmapDrawable) drawable_orange).getBitmap();
                        Drawable bitmapDrawable_orange = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap2, 60, 60, true));
                        startMarker.setIcon(bitmapDrawable_orange);
                    }
                    if (coroni.equals("green")) {
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

        startMarker.setDraggable(true);

        final Activity context2 = getActivity();
        startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                                                 @Override
                                                 public boolean onMarkerClick(Marker marker, MapView mapView) {
                                                     Intent intent = new Intent(context2, CountryDetails.class);
                                                     intent.putExtra("country", eingabe);
                                                     startActivity(intent);
                                                     return false;
                                                 }
                                             }

        );

        this.mapController.setCenter(geoPoint);
    }


    public void setOnMarkerDrag(Marker startMarker) {
        System.out.println("Hallo");
        mapView.getOverlays().remove(startMarker);
    }

}
