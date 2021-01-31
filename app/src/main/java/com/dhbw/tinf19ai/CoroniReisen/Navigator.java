package com.dhbw.tinf19ai.CoroniReisen;

/**
 * This class serves as a container for the MapFragment.
 */

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

public class Navigator extends AppCompatActivity {
    boolean internetConnection = MainActivity.internetConnection;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        internetConnection = isNetworkAvailable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigator);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container, new MapFragment(), "Activity").commitAllowingStateLoss();

    }

    @Override
    protected void onResume() {
        internetConnection = isNetworkAvailable();
        super.onResume();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}