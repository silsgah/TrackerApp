package com.example.silas.trackapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.silas.trackapp.alerts.Alerts;
import com.example.silas.trackapp.cache.AppCache;
import com.example.silas.trackapp.config.Endpoints;
import com.example.silas.trackapp.listeners.RequestListener;
import com.example.silas.trackapp.requests.Requests;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    AppCache cache;
    Button submitButton;
    Spinner stationsSpinner, servPointSpinner, incTypeSpinner, initStateSpinner, handOffStateSpinner;
    EditText locationEd, mediumCallEd, dateEd, ageEd, dobEd, timeCalledEd, lbtEd, arrScEd, leftScEd, handTimeEd, leftHosEd, arrBaseEd, mainComplainEd;
    RadioGroup genderRg;
    Requests requests;
    String gender = "";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cache = AppCache.getInstance(MainActivity.this);
        if (!cache.isSignedIn()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
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

            final ArrayList<String> servicePoint = new ArrayList<>();
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

            requests = new Requests();

            genderRg = (RadioGroup) findViewById(R.id.rg_gender);

            submitButton = (Button) findViewById(R.id.btn_submit);

            locationEd = (EditText) findViewById(R.id.ed_location);
            mediumCallEd = (EditText) findViewById(R.id.ed_med_call);
            dateEd = (EditText) findViewById(R.id.ed_date);
            ageEd = (EditText) findViewById(R.id.ed_age);
            dobEd = (EditText) findViewById(R.id.ed_dob);
            timeCalledEd = (EditText) findViewById(R.id.ed_time_called);
            lbtEd = (EditText) findViewById(R.id.ed_lbt);
            arrScEd = (EditText) findViewById(R.id.ed_arr_sc);
            leftScEd = (EditText) findViewById(R.id.ed_left_sc);
            handTimeEd = (EditText) findViewById(R.id.ed_hand_time);
            leftHosEd = (EditText) findViewById(R.id.ed_left_hos);
            arrBaseEd = (EditText) findViewById(R.id.ed_arrive_base);
            mainComplainEd = (EditText) findViewById(R.id.ed_main_complain);

            stationsSpinner = (Spinner) findViewById(R.id.sp_station);
            servPointSpinner = (Spinner) findViewById(R.id.sp_serv_point);
            incTypeSpinner = (Spinner) findViewById(R.id.sp_inc_type);
            initStateSpinner = (Spinner) findViewById(R.id.sp_init_stat);
            handOffStateSpinner = (Spinner) findViewById(R.id.sp_hand_stat);

            stationsSpinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stations));
            servPointSpinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, servicePoint));
            incTypeSpinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, incidentTypes));
            initStateSpinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, initialState));
            handOffStateSpinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, handOffState));

            String title = "Tracker Details";
            final SpannableString s = new SpannableString(title);
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s);

            final EditText[] editTexts = {locationEd, mediumCallEd, dateEd, ageEd, dobEd, timeCalledEd, lbtEd, arrScEd, leftScEd, handTimeEd, leftHosEd, arrBaseEd, mainComplainEd};

            timeCalledEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        showTimeDialog(v.getContext(), "timeCalled");
                    }
                }
            });

            lbtEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        showTimeDialog(v.getContext(), "lbt");
                    }
                }
            });

            arrScEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        showTimeDialog(v.getContext(), "arrSc");
                    }
                }
            });

            leftScEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        showTimeDialog(v.getContext(), "leftsc");
                    }
                }
            });

            leftHosEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        showTimeDialog(v.getContext(), "lefthos");
                    }
                }
            });

            arrBaseEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        showTimeDialog(v.getContext(), "arrBase");
                    }
                }
            });

            handTimeEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        showTimeDialog(v.getContext(), "handTime");
                    }
                }
            });



            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validate(editTexts)) {
                        int index = genderRg.indexOfChild(findViewById(genderRg.getCheckedRadioButtonId()));
                        gender = index == 0 ? "Female" : "Male";

                        sendData(v.getContext(), stationsSpinner.getSelectedItem().toString(), locationEd.getText().toString().trim(), servPointSpinner.getSelectedItem().toString(), mediumCallEd.getText().toString(), dateEd.getText().toString(), incTypeSpinner.getSelectedItem().toString(), ageEd.getText().toString(), dobEd.getText().toString(),
                                timeCalledEd.getText().toString(), lbtEd.getText().toString().trim(), arrScEd.getText().toString(), leftScEd.getText().toString(), handTimeEd.getText().toString().trim(),
                                leftHosEd.getText().toString().trim(), arrBaseEd.getText().toString().trim(), gender
                                , initStateSpinner.getSelectedItem().toString(), handOffStateSpinner.getSelectedItem().toString(), mainComplainEd.getText().toString().trim()
                                );
                    } else {
                        Snackbar.make(v, "Please enter all fields", Snackbar.LENGTH_LONG).show();
                    }
                }
            });

            dobEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        showDateDialog(v.getContext(), "dob");
                    }
                }
            });

            dateEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        showDateDialog(v.getContext(), "date");
                    }
                }
            });


        }
    }

    private boolean validate(EditText[] fields) {
        boolean isNotEmpty = true;
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            if (currentField.getText().toString().length() <= 0) {
                isNotEmpty = false;
                currentField.setError("Cant be empty");
            }
        }
        return isNotEmpty;
    }


    private void sendData(final Context context, String station, String location, String servicePoint,
                          String mediumCall, String date, String incidentType, String age, String dob, String timeCalled,
                          String leaveBaseTime, String arriveScene, String leftScene, String handOverTime, String leftHospital,
                          String arriveBase, String sex, String initialState, String handoverState, String mainComplain) {
        String json = "{Station: \""+station+"\", Location: \""+location+"" +
                "\", Servicepoint: \""+servicePoint+"\", MediumCall: \""+mediumCall+"\", Date: \""+date+"\",IncidentType: \""+incidentType+"\", " +
                "Age: \""+age+"\", DOB: \""+dob+"\", TimeCalled: \""+timeCalled+"\", LeaveBaseTime: \""+leaveBaseTime+"\", ArriveScene: \""+arriveScene+"\", " +
                "LeftScene: \""+leftScene+"\", HandOverTime: \""+handOverTime+"\", LeftHospital: \""+leftHospital+"\", " +
                "ArriveBase: \""+arriveBase+"\", Sex: \""+sex+"\", InitialState:  \""+initialState+"\", " +
                "HandOverState: \""+handoverState+"\", MainComplain: \""+mainComplain+"\"}";
        requests.makePostRequest(Endpoints.SEND_DATA.toString(), json, context, new RequestListener() {
            @Override
            public void onBefore() {
                dialog = ProgressDialog.show(context, "", "Please wait... Sending data");
            }

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                dialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if ((jObj.has("statuscode") ? jObj.getString("statuscode") : "").equalsIgnoreCase("200")) {
                        Log.d(TAG, "success");
                        EditText[] editTexts = {locationEd, mediumCallEd, dateEd, ageEd, dobEd, timeCalledEd, lbtEd, arrScEd, leftScEd, handTimeEd, leftHosEd, arrBaseEd, mainComplainEd};
                        successMessage(context, editTexts);

                    } else {
                        oopsMessage(context);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    oopsMessage(context);
                }
            }
        });
    }


    private void oopsMessage(Context context) {
        Alerts.showMessageDialog(context, "Oops!!! Something went wrong", "Please check your internet connection and try again.");
    }
    private void successMessage(Context context, EditText[] fields) {
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            currentField.setText("");
        }
        Alerts.showMessageDialog(context, "Yay!!!", "Data sent.");
    }

    private void showTimeDialog(Context context, String tag){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setThemeDark(true);
        tpd.enableSeconds(false);
        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
        tpd.setAccentColor(Color.parseColor("#9C27B0"));
        tpd.setTitle("Select Time");
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
            }
        });
        tpd.show(getSupportFragmentManager(), tag);
    }

    private void showDateDialog(Context context, String tag){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setThemeDark(true);
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setAccentColor(Color.parseColor("#9C27B0"));
        dpd.setTitle("Select Date");
        dpd.show(getSupportFragmentManager(), tag);
    }
    @Override
    public void onTimeSet(TimePickerDialog timePickerDialog, int i, int i1, int i2) {
        if(timePickerDialog.getTag().equals("timeCalled")){
            timeCalledEd.setText(String.valueOf(i)+":"+String.valueOf(i1));
        }else if(timePickerDialog.getTag().equals("lbt")){
            lbtEd.setText(String.valueOf(i)+":"+((String.valueOf(i1).length() == 1) ? "0"+String.valueOf(i1) : String.valueOf(i1)));
        }else if(timePickerDialog.getTag().equals("arrSc")){
            arrScEd.setText(String.valueOf(i)+":"+((String.valueOf(i1).length() == 1) ? "0"+String.valueOf(i1) : String.valueOf(i1)));
        }else if(timePickerDialog.getTag().equals("leftsc")){
            leftScEd.setText(String.valueOf(i)+":"+((String.valueOf(i1).length() == 1) ? "0"+String.valueOf(i1) : String.valueOf(i1)));
        }else if(timePickerDialog.getTag().equals("lefthos")){
            leftHosEd.setText(String.valueOf(i)+":"+((String.valueOf(i1).length() == 1) ? "0"+String.valueOf(i1) : String.valueOf(i1)));
        }else if(timePickerDialog.getTag().equals("arrBase")){
            arrBaseEd.setText(String.valueOf(i)+":"+((String.valueOf(i1).length() == 1) ? "0"+String.valueOf(i1) : String.valueOf(i1)));
        }else if(timePickerDialog.getTag().equals("handTime")){
            handTimeEd.setText(String.valueOf(i)+":"+((String.valueOf(i1).length() == 1) ? "0"+String.valueOf(i1) : String.valueOf(i1)));
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
        if(datePickerDialog.getTag().equals("dob")){
            dobEd.setText(String.valueOf(i)+"-"+((String.valueOf(i1).length() == 1) ? "0"+String.valueOf(i1) : String.valueOf(i1))+"-"+String.valueOf(i2));
        }else if(datePickerDialog.getTag().equals("date")){
            dateEd.setText(String.valueOf(i)+"-"+  ((String.valueOf(i1).length() == 1) ? "0"+String.valueOf(i1): String.valueOf(i1))+"-"+String.valueOf(i2));
        }

    }
}
