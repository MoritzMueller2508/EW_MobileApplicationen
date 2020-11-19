package com.dhbw.tinf19ai.CoroniReisen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.FrameLayout;

import java.io.IOException;

import static com.dhbw.tinf19ai.CoroniReisen.RedRiskCountriesExtraction.redRiskCountriesExtraction;

public class MainActivity extends AppCompatActivity {

    private FrameLayout leftContainer;
    private FrameLayout container;

    private Schutzmaßnahmen Schutzmaßnahmen;
    private Map Map;


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