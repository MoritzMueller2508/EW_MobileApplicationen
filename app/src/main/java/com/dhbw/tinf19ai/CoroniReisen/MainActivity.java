package com.dhbw.tinf19ai.CoroniReisen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container, new Map(), "Activity").commitAllowingStateLoss();

        //to get list of countries in background. To use where and when needed
        /*AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    redRiskCountriesExtraction();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }
}