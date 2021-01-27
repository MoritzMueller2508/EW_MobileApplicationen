package com.dhbw.tinf19ai.CoroniReisen;

/**
 * This class represents the first interface that includes a greeting and rules of conduct during a pandemic.
 * By clicking on the image you will be redirected to the next interface.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;


public class MainActivity extends AppCompatActivity {
    public static boolean internetConnection = false;
    public boolean permissions = false;
    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private final static String TAG = "MainActivity";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "On Create .....");

        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        //Dictionary initialization
        CountryDictionary.setCountriesDict();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "On Start .....");
        verifyStoragePermissions(this);
    }

    //Forwarding to the MapFragment by clicking on the image
    public void imageClick(View view) {
        Intent intent = new Intent(this, Navigator.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        //check for permission and request if needed
        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Asking for permissions");

            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        permissions = writePermission == PackageManager.PERMISSION_GRANTED && readPermission == PackageManager.PERMISSION_GRANTED;

        if (permissions) {
            Log.d(TAG, "calling function saveData()");
            saveData();
        } else {
            //popup message in case permission were denied
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Unsere App kann offline nicht gut funktionieren wenn die Berechtigungen nicht erteilt sind. " +
                    "Bitte bei den Berechtigungen zustimmen.");
            alertDialogBuilder.setTitle("Berechtigungen");
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //ask again for permissions
                    verifyStoragePermissions(activity);
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    //function to request the Bing Data and the RKI data are saved
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveData() {
        //check if internet is available
        internetConnection = isNetworkAvailable();
        Log.d(TAG, "internet connection: " + internetConnection);

        if (internetConnection) {
            AsyncTask.execute(() -> {
                try {
                    BingData.saveBingData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            AsyncTask.execute(() -> {

                try {
                    RiskCountriesExtraction.saveData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            //popup message in the case that internet is not available
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Die Daten die Sie bei der App sehen sind nicht aktuell, " +
                    "da Sie über eine Internetverbindung nicht verfügen. Sobald sie online gehen, " +
                    "werden die Daten aktualisiert.");
            alertDialogBuilder.setTitle("Keine Internetverbindung");
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
