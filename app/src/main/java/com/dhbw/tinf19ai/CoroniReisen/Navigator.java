package com.dhbw.tinf19ai.CoroniReisen;

/**
 *This class navigates between the different interfaces (phone-view / tablet-view)
 */

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

    }

}