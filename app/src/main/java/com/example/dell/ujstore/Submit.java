package com.example.dell.ujstore;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.appyvet.rangebar.RangeBar;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.SnackBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Submit extends AppCompatActivity {
    com.rey.material.widget.CheckBox checkbox;
    RangeBar rangebar;
    EditText seektext;
    EditText amount;
    EditText discount;
    Button reject;
    Button submit;
    SharedPreferences pref;
    int dis_amount;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.container);
        final String id = getIntent().getExtras().getString("id");
        amount =  (EditText) findViewById(R.id.editTextmin);
        discount = (EditText) findViewById(R.id.editTextmindis);
        reject = (Button) findViewById(R.id.rejectsub);
        submit = (Button) findViewById(R.id.applysub);
        checkbox = (CheckBox)findViewById(R.id.checkdiscount);
        seektext = (EditText)findViewById(R.id.seektext1);
        rangebar = (RangeBar)findViewById(R.id.rangebar1);
        float seek = Float.parseFloat(seektext.getText().toString());
        rangebar.setRangePinsByValue(0, seek);
        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                seektext.setText(rightPinValue);

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkbox.isChecked() || seektext.getText().toString().equals("0") ||
                        amount.getText().toString().isEmpty() || discount.getText().toString().isEmpty() ){
                    SnackBar.make(getApplicationContext()).text("Enter discount value in any field").show();
                }
                else if (checkbox.isChecked()){
                    dis_amount = 0;
                    send(dis_amount);
                }
                else if(!seektext.getText().toString().equals("0")){
                    dis_amount = Integer.parseInt(seektext.getText().toString());
                    send(dis_amount);
                }
                else if(!amount.getText().toString().isEmpty() && !discount.getText().toString().isEmpty()){
                    dis_amount = Integer.parseInt(discount.getText().toString());
                    send(dis_amount);
                }
                else
                    SnackBar.make(getApplicationContext()).text("Enter discount value in any field").show();
    }

    public void send(int dis_amount){
        String Submit_Url = "https://ujapi.herokuapp.com/api/v1/s/bookings/" + id + "/respond_bookings";
        pref = getBaseContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final String authtoken = pref.getString("token", null);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("discount",String.valueOf(dis_amount));
            jsonBody.put("booking_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST,
                Submit_Url, jsonBody,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        viewPager.setCurrentItem(1);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", authtoken);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObject);
    }
        });
    }
}
