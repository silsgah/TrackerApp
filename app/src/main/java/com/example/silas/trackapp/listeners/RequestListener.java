package com.example.silas.trackapp.listeners;

/**
 * Created by Lisa on 6/15/17.
 **/

public interface RequestListener {
    void onBefore();
    void onResponse(String response);
}
