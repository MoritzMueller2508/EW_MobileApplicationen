package com.dhbw.tinf19ai.CoroniReisen;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            /*try {
                verifyStoragePermissions(this);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
    }

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

    //permission method to save a CSV file with the Bing Data.
    public static void verifyStoragePermissions(Activity activity) throws IOException {
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
            //TODO: can an Executor, ThreadPoolExecutor or a Runnable achieve a better performance?
            AsyncTask.execute(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    try {
                        BingData.getBingDataOnline();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                try {
                    BingData.getBingDataOnline();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        }

    }
}
