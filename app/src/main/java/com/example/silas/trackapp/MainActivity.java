package com.example.silas.trackapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.silas.trackapp.cache.AppCache;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    AppCache cache;
    Spinner stationsSpinner, servPointSpinner, incTypeSpinner, initStateSpinner, handOffStateSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cache = AppCache.getInstance(MainActivity.this);
        if(!cache.isSignedIn()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }else {
            setContentView(R.layout.activity_main);

            ArrayList<String> stations = new ArrayList<>();
            stations.add("Asokore");
            stations.add("Ashanti");
            stations.add("Mampon");
            stations.add("Baba Yara");
            stations.add("Bekwai");
            stations.add("Ejisu");
            stations.add("Ejura");
            stations.add("Juaso");
            stations.add("Kath");
            stations.add("Konongo");

            ArrayList<String> servicePoint = new ArrayList<>();
            servicePoint.add("hospital");
            servicePoint.add("industrial");
            servicePoint.add("Recreational");
            servicePoint.add("Residence");
            servicePoint.add("Roadside");

            ArrayList<String> incidentTypes = new ArrayList<>();
            incidentTypes.add("Investigations");
            incidentTypes.add("Medical");
            incidentTypes.add("OBS & Gynae");
            incidentTypes.add("Others");
            incidentTypes.add("Trauma");

            ArrayList<String> initialState = new ArrayList<>();
            initialState.add("Alert");
            initialState.add("Verbal Response");
            initialState.add("Pain");
            initialState.add("Unresponsive");

            ArrayList<String> handOffState = new ArrayList<>();
            handOffState.add("Deteriorated");
            handOffState.add("Expired en route");
            handOffState.add("Improved");
            handOffState.add("Unchanged");

            stationsSpinner = (Spinner)findViewById(R.id.sp_station);
            servPointSpinner = (Spinner)findViewById(R.id.sp_serv_point);
            incTypeSpinner = (Spinner)findViewById(R.id.sp_inc_type);
            initStateSpinner = (Spinner)findViewById(R.id.sp_init_stat);
            handOffStateSpinner = (Spinner)findViewById(R.id.sp_hand_stat);

            stationsSpinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stations));
            servPointSpinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, servicePoint));
            incTypeSpinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, incidentTypes));
            initStateSpinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, initialState));
            handOffStateSpinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, handOffState));

            String title = "Tracker Details";
            SpannableString s = new SpannableString(title);
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s);
        }
    }
}
