package com.dhbw.tinf19ai.task5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.FrameLayout;

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

    }
}