package com.example.dell.ujstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Verify extends AppCompatActivity {
    private static final String VERIFY_URL = "https://ujapi.herokuapp.com/api/v1/s/stores/verify";
    private static final String RESEND_URL = "https://ujapi.herokuapp.com/api/v1/s/stores/resend";
    private EditText edittextotp;
    private Button btnverify;
    private TextView btnresend;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_layout);
        edittextotp = (EditText) findViewById(R.id.editTextotp);
        btnverify = (Button) findViewById(R.id.buttonverify);
        btnresend = (TextView) findViewById(R.id.editTextresend);
        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verification();
            }
        });
        btnresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend();
            }
        });
    }

    private void verification() {
        final String otp = edittextotp.getText().toString();
        final String idget = getIntent().getExtras().getString("id");
        final String email = getIntent().getExtras().getString("email");
        System.out.println(idget);
        if (edittextotp.toString().isEmpty()) {
            edittextotp.setError("Enter valid OTP");
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONObject js = new JSONObject();
                    try {
                        JSONObject jsonobject_one = new JSONObject();
                        jsonobject_one.put("otp", otp);
                        jsonobject_one.put("id", idget);

                        js.put("store", jsonobject_one);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST,
                            VERIFY_URL, js,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String name = response.getString("name");
                                        String authtoken = response.getString("authentication_token");
                                        String id = response.getString("id");
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("name", name);
                                        editor.putString("token", authtoken);
                                        editor.putString("email", email);
                                        editor.putString("id", id);
                                        editor.apply();
                                        Intent intent = new Intent(Verify.this, SwipeTabActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        //Snackbar.make(view, "Please enter all the details correctly", Snackbar.LENGTH_LONG).show();
                                        pDialog.dismiss();
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Snackbar.make(view, "Please enter all the details correctly", Snackbar.LENGTH_LONG).show();
                            pDialog.dismiss();
                        }
                    }) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            return headers;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(Verify.this);
                    requestQueue.add(jsonObject);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog = new SweetAlertDialog(Verify.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Loading");
                            pDialog.setCancelable(false);
                            pDialog.show();
                        }
                    });
                }
            }).start();
        }
    }

    public void resend(){
        final String idget = getIntent().getExtras().getString("id");
        new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonobject_one = new JSONObject();
                    try {
                        jsonobject_one.put("id", idget);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST,
                            RESEND_URL, jsonobject_one,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                  pDialog.dismiss();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(Verify.this,error.toString(),Toast.LENGTH_LONG).show();
                                    pDialog.dismiss();
                                }
                    }) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            return headers;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(Verify.this);
                    requestQueue.add(jsonObject);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog = new SweetAlertDialog(Verify.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Loading");
                            pDialog.setCancelable(false);
                            pDialog.show();
                        }
                    });
                }
            }).start();
        }
}