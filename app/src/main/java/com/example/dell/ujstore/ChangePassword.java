package com.example.dell.ujstore;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.apptik.widget.multiselectspinner.MultiSelectSpinner;

public class ChangePassword extends AppCompatActivity {
    private static final String PUTDETAILS_URL = "https://ujapi.herokuapp.com/api/v1/s/stores";
    private EditText editTextOld;
    private EditText editTextNew;
    private EditText editTextCon;
    private Button buttonnext;
    String items;
    private ArrayAdapter<String> adapter2;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        editTextOld = (EditText) findViewById(R.id.oldpassText);
        editTextNew = (EditText) findViewById(R.id.newpassText);
        editTextCon = (EditText) findViewById(R.id.conpassText);
        buttonnext = (Button) findViewById(R.id.btnchange);

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeDetails();
            }
        });

    }
    private void ChangeDetails() {
        final String old = editTextOld.getText().toString();
        final String newpass = editTextNew.getText().toString();
        final String connew = editTextCon.getText().toString();
        if (old.isEmpty()) {
            editTextOld.setError("Enter old password");
        } else if (newpass.isEmpty()) {
            editTextNew.setError("Enter new password");
        } else if (connew.isEmpty() ) {
            editTextCon.setError("Confirm new password");
        } else if (!connew.equals(newpass)) {
            editTextCon.setError("Password don't match");
        }
        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences pref = getBaseContext().getSharedPreferences("MyPref", 0);
                    String id =pref.getString("id", null);
                    final String authtoken = pref.getString("token", null);
                    JSONObject js = new JSONObject();
                    try {
                        JSONObject jsonobject_one = new JSONObject();

                        jsonobject_one.put("password", newpass);

                        js.put("store", jsonobject_one);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.PUT,
                            PUTDETAILS_URL+"/"+id, js,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Intent intent = new Intent(ChangePassword.this, SwipeTabActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    pDialog.dismiss();
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ChangePassword.this, "Invalid Entries", Toast.LENGTH_LONG).show();
                            pDialog.dismiss();
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

                    RequestQueue requestQueue = Volley.newRequestQueue(ChangePassword.this);
                    requestQueue.add(jsonObject);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog = new SweetAlertDialog(ChangePassword.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Updating");
                            pDialog.setCancelable(false);
                            pDialog.show();
                        }
                    });
                }
            }).start();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

