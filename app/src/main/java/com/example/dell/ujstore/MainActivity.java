package com.example.dell.ujstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String LOGIN_URL = "https://ujapi.herokuapp.com/api/v1/s/login";
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView editTextSignup;
    private TextView editTextForget;
    private Button buttonLogin;
    SharedPreferences pref;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SweetAlertDialog pDialog;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login_layout);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            SharedPreferences pref2 = getApplicationContext().getSharedPreferences("GSPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref2.edit();
            editor.putString("start", "true");
            editor.apply();
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pref = getApplicationContext().getSharedPreferences("MyPref", 0);
            if (pref.contains("name")) {
                Intent intent = new Intent(MainActivity.this,SwipeTabActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else{
                editTextEmail = (EditText) findViewById(R.id.editTextEmail);
                editTextPassword = (EditText) findViewById(R.id.editTextPassword);
                editTextSignup = (TextView) findViewById(R.id.editTextSignup);
                editTextForget = (TextView) findViewById(R.id.forgettext);
                buttonLogin = (Button) findViewById(R.id.buttonLogin);
                buttonLogin.setOnClickListener(this);
                editTextSignup.setOnClickListener(this);
                editTextForget.setOnClickListener(this);
            }
        }
        private void LoginUser() {
            final String password = editTextPassword.getText().toString().trim();
            final String email = editTextEmail.getText().toString().trim();
            if (!email.matches(emailPattern) || email.isEmpty()) {
                editTextEmail.setError("Inavalid email");
            } else if (password.isEmpty() || password.length() < 8) {
                editTextPassword.setError("Password should be greater than 8 characters");
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject js = new JSONObject();
                        try {
                            JSONObject jsonobject_one = new JSONObject();

                            jsonobject_one.put("email", email);
                            jsonobject_one.put("password", password);

                            js.put("session", jsonobject_one);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST,
                                LOGIN_URL, js,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String name = response.getString("name");
                                            String id = response.getString("id");
                                            String authtoken = response.getString("authentication_token");
                                            pref = getBaseContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("name", name);
                                            editor.putString("token", authtoken);
                                            editor.putString("email", email);
                                            editor.putString("id", id);
                                            editor.commit();
                                            Intent intent = new Intent(MainActivity.this, SwipeTabActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);


                                        } catch (JSONException e) {
                                            Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                                            pDialog.dismiss();

                                        }
                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
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

                        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                        requestQueue.add(jsonObject);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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

        @Override
        public void onClick(View v) {
            if(v == buttonLogin) {
                LoginUser();
            }
            if(v == editTextSignup){
                startActivity(new Intent(this, SignUp_Activity.class));
            }
            if(v == editTextForget){
                startActivity(new Intent(this, ForgetPassword.class));
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

