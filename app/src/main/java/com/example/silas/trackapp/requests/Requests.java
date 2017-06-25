package com.example.silas.trackapp.requests;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.silas.trackapp.config.Endpoints;
import com.example.silas.trackapp.listeners.RequestListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Lisa on 6/15/17.
 **/

public class Requests {

    static OkHttpClient client;
    static MediaType JSON;
    static String responseString = "";

    public Requests(){
        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");
    }


    public static void makeGetRequest(final String url, final Context context, final RequestListener listener){
        listener.onBefore();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    responseString = get(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResponse(responseString);
                    }
                });

            }
        }).start();
    }

    public void makePostRequest(final String url, final String json, final Context context, final RequestListener listener){
        listener.onBefore();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("TAG", url+json);
                    responseString = post(url, json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResponse(responseString);
                    }
                });

            }
        }).start();
    }

    private static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
