package com.example.silas.trackapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.example.silas.trackapp.cache.AppCache;

public class MainActivity extends AppCompatActivity {

    AppCache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cache = AppCache.getInstance(MainActivity.this);
        if(!cache.isSignedIn()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }else {
            setContentView(R.layout.activity_main);
            String title = "Tracker Details";
            SpannableString s = new SpannableString(title);
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s);
        }
    }
}
