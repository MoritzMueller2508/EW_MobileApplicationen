package com.dhbw.tinf19ai.CoroniReisen;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private FrameLayout container;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container, new Map(), "Activity").commitAllowingStateLoss();


        //to get list of countries in background. To use where and when needed
        //TODO: can an Executor, ThreadPoolExecutor or a Runnable achieve a better performance?
        /*AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try {
                    //getRedRiskCountriesExtraction();
                    //getOrangeRiskCountriesExtraction();
                     BingData.getConfirmedCases("Germany");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

}