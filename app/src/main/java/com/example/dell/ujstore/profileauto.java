package com.example.dell.ujstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class profileauto extends AppCompatActivity {
    private static final String PUTDETAILS_URL = "https://ujapi.herokuapp.com/api/v1/s/stores";
    private TextView editTextName;
    private TextView editTextadd;
    private TextView editTextcat;
    private TextView editTextdes;
    private Button buttonnext;
    private ImageView circle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileauto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        editTextName = (TextView) findViewById(R.id.nameTextauto);
        editTextadd = (TextView) findViewById(R.id.addTextauto);
        editTextcat = (TextView) findViewById(R.id.catTextauto);
        editTextdes = (TextView) findViewById(R.id.desTextauto);
        editTextName.setText(Html.fromHtml("<b>Store Name:</b> "));
        editTextadd.setText(Html.fromHtml("<b>Store Address:</b> "));
        editTextcat.setText(Html.fromHtml("<b>Store Category:</b> "));
        editTextdes.setText(Html.fromHtml("<b>Store Description:</b> "));
        buttonnext = (Button) findViewById(R.id.btnnextauto);
        String storeid = getIntent().getExtras().getString("id");
        circle = (ImageView) findViewById(R.id.pfauto);
        getDetails(storeid);
        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profileauto.this,profile_activity.class);
                startActivity(intent);
            }
        });
    }

    private void getDetails(String storeid) {
        try {
            SharedPreferences pref = this.getSharedPreferences("MyPref", 0);
            final String authtoken = pref.getString("token", null);
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET,
                    PUTDETAILS_URL + "/" + storeid,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            try {
                                String name = object.getString("store_name");
                                editTextName.append(name);
                                String storeid = object.getString("store_category");
                                JSONObject storeobject = new JSONObject(storeid);
                                String store = storeobject.getString("category");
                                editTextcat.append(store);
                                String a2 = object.getString("picture");
                                JSONObject a3 = new JSONObject(a2);
                                String a4 = a3.getString("picture");
                                JSONObject a4obj = new JSONObject(a4);
                                String url = a4obj.getString("url");
                                Picasso.with(profileauto.this).load(url).into(circle);
                                String des = object.getString("description");
                                editTextdes.append(des);
                                String add = object.getString("address");
                                editTextadd.append(add);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", authtoken);
                    return headers;
                }

                @Override
                public Request.Priority getPriority() {
                    return Request.Priority.IMMEDIATE;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonArrayRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
