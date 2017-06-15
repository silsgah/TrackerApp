package com.example.silas.trackapp.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;



public class AppCache
{
    private static final String CACHE = "APPCACHE";
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private static AppCache mAppCache;
    private Gson mGson;


    private AppCache(Context context)
    {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(CACHE, Context.MODE_PRIVATE);
        mGson = new Gson();

    }

    public static AppCache getInstance(Context context)
    {
        if ( mAppCache == null ) {
            mAppCache = new AppCache(context);
        }
        return mAppCache;
    }

    public synchronized void putInt(String key, int number)
    {
        mSharedPreferences.edit().putInt(key, number).commit();
    }

    public synchronized void putLong(String key, long number)
    {
        mSharedPreferences.edit().putLong(key, number).commit();
    }

    public synchronized void putBool(String key, boolean value)
    {
        mSharedPreferences.edit().putBoolean(key, value).commit();
    }

    public synchronized void setSignedIn(boolean value)
    {
        mSharedPreferences.edit().putBoolean("SIGNED_IN", value).commit();
    }

    public synchronized void putString(String key, String value)
    {
        mSharedPreferences.edit().putString(key, value).commit();
    }

    public synchronized void putFavouriteCount(String key, long value)
    {
        mSharedPreferences.edit().putLong(key, value);
    }

    public int getInt(String key)
    {
        return mSharedPreferences.getInt(key, 0);
    }

    public boolean hasInt(String key)
    {
        return mSharedPreferences.contains(key);
    }

    public long getLong(String key)
    {
        return mSharedPreferences.getLong(key, 0L);
    }

    public boolean getBool(String key)
    {
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean isSignedIn()
    {
        return mSharedPreferences.getBoolean("SIGNED_IN", false);
    }

    public String getString(String key)
    {
        return mSharedPreferences.getString(key, "");
    }

    public String getStringWith0Default(String key)
    {
        return mSharedPreferences.getString(key, "0");
    }
}
