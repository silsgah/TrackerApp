package com.example.silas.trackapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.silas.trackapp.alerts.Alerts;
import com.example.silas.trackapp.cache.AppCache;
import com.example.silas.trackapp.config.Endpoints;
import com.example.silas.trackapp.listeners.RequestListener;
import com.example.silas.trackapp.requests.Requests;

import org.json.JSONObject;


public class SelectTimeActivity extends AppCompatActivity {

    private static final String TAG = SelectTimeActivity.class.getSimpleName();
    TextView lbtTv, arrScTv, lScTv, lHosTv, arrBaseTv;
    Switch lbtSw, arrScSw, lScSw, lHosSw, arrBaseSw;
    Context context;
    ProgressDialog dialog;
    Requests requests;
    AppCache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        String title = "Select Time";
        final SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        requests = new Requests();
        context = SelectTimeActivity.this;
        Alerts.setStatusbarColor(context);

        cache = AppCache.getInstance(context);
        lbtTv = (TextView) findViewById(R.id.tv_lbt);
        arrScTv = (TextView) findViewById(R.id.tv_arrsc);
        lScTv = (TextView) findViewById(R.id.tv_lsc);
        lHosTv = (TextView) findViewById(R.id.tv_lhos);
        arrBaseTv = (TextView) findViewById(R.id.tv_arrbase);

        lbtSw = (Switch) findViewById(R.id.sw_lbt);
        arrScSw = (Switch) findViewById(R.id.sw_arrsc);
        lScSw = (Switch) findViewById(R.id.sw_lsc);
        lHosSw = (Switch) findViewById(R.id.sw_lhos);
        arrBaseSw = (Switch) findViewById(R.id.sw_arrbase);

        lbtSw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentTime(lbtSw, lbtTv);
            }
        });

        lbtSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    arrScSw.setEnabled(true);
                }else{
                    arrScSw.setEnabled(false);
                }
            }
        });

        arrScSw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentTime(arrScSw, arrScTv);
            }
        });


        arrScSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    lScSw.setEnabled(true);
                }else{
                    lScSw.setEnabled(false);
                }
            }
        });

        lScSw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentTime(lScSw, lScTv);
            }
        });

        lScSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    lHosSw.setEnabled(true);
                }else{
                    lHosSw.setEnabled(false);
                }
            }
        });

        lHosSw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentTime(lHosSw, lHosTv);
            }
        });

        lHosSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    arrBaseSw.setEnabled(true);
                }else{
                    arrBaseSw.setEnabled(false);
                }
            }
        });

        arrBaseSw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentTime(arrBaseSw, arrBaseTv);
            }
        });

    }


    private void getCurrentTime(final Switch switchWidget, final TextView txtView){
        requests.makeGetRequest(Endpoints.GET_TIME.toString(), context, new RequestListener() {
            @Override
            public void onBefore() {
                dialog = ProgressDialog.show(SelectTimeActivity.this, "", "Please wait... Getting time");
            }

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                dialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if ((jObj.has("statuscode") ? jObj.getString("statuscode") : "").equalsIgnoreCase("200")){
                    //if(response.contains(":") && !response.contains("!DOCTYPE")){
                        String res = (jObj.has("messagecontent") ? jObj.getString("messagecontent") : "");
                        Log.d(TAG, "success"+response);
                        if(!res.isEmpty()) {
                            successMessage(switchWidget, txtView, res);
                        }else{
                            oopsMessage(switchWidget);
                        }
                    }else{
                        oopsMessage(switchWidget);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    oopsMessage(switchWidget);
                }
            }
        });
    }


    private void successMessage(Switch switchWidget, TextView txtView, String string){
        switchWidget.setChecked(true);
        switchWidget.setEnabled(false);
        txtView.setText(string);
        checkIfAllFieldsAreChecked();
    }

    private void oopsMessage(Switch switchWidget){
        switchWidget.setChecked(false);
        Alerts.showMessageDialog(context, "Oops!!! Something went wrong", "Please check your internet connection and try again.");
    }

    private void checkIfAllFieldsAreChecked(){
        if(arrBaseSw.isChecked() && arrScSw.isChecked() && lbtSw.isChecked() && lHosSw.isChecked() && lScSw.isChecked()){
            Intent intent = new Intent(SelectTimeActivity.this, MainActivity.class);
            intent.putExtra("arrBase", arrBaseTv.getText().toString());
            intent.putExtra("arrSc", arrScTv.getText().toString());
            intent.putExtra("lbt", lbtTv.getText().toString());
            intent.putExtra("lHos", lHosTv.getText().toString());
            intent.putExtra("lSc", lScTv.getText().toString());
            startActivity(intent);
            finish();
        }
    }

}
