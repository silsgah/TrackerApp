package com.example.silas.trackapp.config;

/**
 * Created by Lisa on 6/15/17.
 **/

public enum Endpoints {
    LOGIN("http://vraschool200-005-site2.gtempurl.com/Account/Login"),
    SEND_DATA("http://vraschool200-005-site2.gtempurl.com/api/AddRecord"),
    GET_TIME("http://vraschool200-005-site2.gtempurl.com/api/AddFirstTime");
    private final String stringValue;
    Endpoints(final String s) { stringValue = s; }
    public String toString() { return stringValue; }
}
