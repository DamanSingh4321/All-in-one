package com.example.dell.ujstore;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgetPassword extends AppCompatActivity {
    EditText emailtxt;
    Button send;
    private static final String RESET_URL = "https://ujapi.herokuapp.com/api/v1/s/password/reset";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        emailtxt = (EditText) findViewById(R.id.editemail);
        send = (Button) findViewById(R.id.buttonsend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Forget();
            }
        });
    }

    private void Forget() {
        final String email = emailtxt.getText().toString();
        if (emailtxt.toString().isEmpty()){
            emailtxt.setError("Enter Email");
        }
        else {
            JSONObject jsonobject_one = new JSONObject();
            try {
                jsonobject_one.put("email", email);
                System.out.println(email);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST,
                    RESET_URL, jsonobject_one,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(ForgetPassword.this, response.toString(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ForgetPassword.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ForgetPassword.this,error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(ForgetPassword.this);
            requestQueue.add(jsonObject);
        }
    }
}