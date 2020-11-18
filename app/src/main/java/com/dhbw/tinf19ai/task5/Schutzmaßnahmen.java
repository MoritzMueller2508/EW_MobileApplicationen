package com.dhbw.tinf19ai.task5;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Schutzmaßnahmen extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
    }

    public void imageClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
