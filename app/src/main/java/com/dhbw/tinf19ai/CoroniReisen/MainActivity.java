package com.dhbw.tinf19ai.CoroniReisen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private FrameLayout leftContainer;
    private FrameLayout container;

    private Schutzmaßnahmen Schutzmaßnahmen;
    private Map Map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            RiskCountriesExtraction.riskCountriesExtraction();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*getSupportFragmentManager().beginTransaction().
                replace(R.id.container, new Map(), "Activity").commitAllowingStateLoss();*/

    }
}