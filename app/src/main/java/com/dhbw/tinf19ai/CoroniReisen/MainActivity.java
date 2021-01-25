package com.dhbw.tinf19ai.CoroniReisen;

/**
 *This class represents the first interface that includes a greeting and rules of conduct during a pandemic.
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
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static boolean internetConnection = false;
    public boolean permissions = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        if (permissions) {
            saveData();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Die Daten die Sie bei der App sehen sind nicht aktuell, " +
                    "da Sie über eine Internetverbindung nicht verfügen. Sobald sie online gehen, " +
                    "werden die Daten aktualisiert.");
            alertDialogBuilder.setTitle("Keine Internetverbindung");
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ///verifyStoragePermissions(get);
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        //Dictionary initialization
        CountryDictionary.setCountriesDict();
    }

    //Forwarding to the MapFragment by clicking on the image
    public void imageClick(View view) {
        Intent intent = new Intent(this, Navigator.class);
        startActivity(intent);
    }

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void verifyStoragePermissions(Activity activity)  {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );

        }

        permissions = writePermission == PackageManager.PERMISSION_GRANTED && readPermission == PackageManager.PERMISSION_GRANTED;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveData(){
        internetConnection = isNetworkAvailable();
        if (internetConnection){
                try {
                    BingData.saveBingData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    RiskCountriesExtraction.saveData();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        } else {
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
