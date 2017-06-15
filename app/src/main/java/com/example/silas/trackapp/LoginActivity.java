package com.example.silas.trackapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.silas.trackapp.alerts.Alerts;
import com.example.silas.trackapp.cache.AppCache;
import com.example.silas.trackapp.config.Endpoints;
import com.example.silas.trackapp.listeners.RequestListener;
import com.example.silas.trackapp.requests.Requests;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    EditText editTextUsername, editTextInputPin;
    Button buttonLogin;
    Context context;
    ProgressDialog dialog;
    Requests requests;
    AppCache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requests = new Requests();
        context = LoginActivity.this;
        Alerts.setStatusbarColor(context);

        cache = AppCache.getInstance(context);
        editTextInputPin = (EditText) findViewById(R.id.input_password);
        editTextUsername = (EditText) findViewById(R.id.input_username);
        buttonLogin = (Button) findViewById(R.id.btn_signup);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextInputPin.getText().toString().trim().isEmpty()){
                    editTextInputPin.setError(getString(R.string.emptyPassword));
                }if(editTextUsername.getText().toString().trim().isEmpty()){
                    editTextUsername.setError(getString(R.string.emptyUsername));
                }else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    //loginUser(editTextUsername.getText().toString(), editTextInputPin.getText().toString());
                }

            }
        });

    }


    private void loginUser(String user, String pin){
        String json = "{\"UserName\":"+user+", \"Password\":"+pin+"}";
        requests.makePostRequest(Endpoints.LOGIN.toString(), json, context, new RequestListener() {
            @Override
            public void onBefore() {
                dialog = ProgressDialog.show(LoginActivity.this, "", "Please wait... Logging in");
            }

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                dialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if ((jObj.has("statuscode") ? jObj.getString("statuscode") : "").equalsIgnoreCase("200")){
                        Log.d(TAG, "success");
                        cache.isSignedIn();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else{
                        invalidCredentials("Your PIN is invalid. Please check and try again.");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    oopsMessage();
                }
            }
        });
    }


    private void invalidCredentials(String string){
        editTextInputPin.setText("");
        editTextUsername.setText("");
        Alerts.showMessageDialog(context, "Invalid Credentials", string);
    }

    private void oopsMessage(){
        editTextInputPin.setText("");
        editTextUsername.setText("");
        Alerts.showMessageDialog(context, "Oops!!! Something went wrong", "Please check your internet connection and try again.");
    }



}
