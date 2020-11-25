package com.dhbw.tinf19ai.CoroniReisen;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;

public class Navigator extends AppCompatActivity {
    private FrameLayout container;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigator);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container, new MapFragment(), "Activity").commitAllowingStateLoss();

        //to get list of countries in background. To use where and when needed
        //TODO: can an Executor, ThreadPoolExecutor or a Runnable achieve a better performance?
        /*AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try {
                    //getRedRiskCountriesExtraction();
                    //getOrangeRiskCountriesExtraction();
                     //BingData.getConfirmedCases("Germany");
                     //CoroniAssignment.getAllCountries();
                    //System.out.println(CoroniAssignment.getCoroni("Vietnam"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

}