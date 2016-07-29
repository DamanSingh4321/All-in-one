package com.example.dell.ujstore;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.RelativeLayout;
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

public class SignUp_Activity extends AppCompatActivity implements View.OnClickListener{
    private static final String REGISTER_URL = "https://ujapi.herokuapp.com/api/v1/s/stores";
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextRePassword;
    private EditText editTextName;
    private EditText editTextMobile;
    private View view;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Button buttonRegister;
    SweetAlertDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        view =findViewById(R.id.linearlayout);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail2);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword2);
        editTextRePassword = (EditText) findViewById(R.id.editTextRePassword2);
        editTextName = (EditText) findViewById(R.id.editTextName2);
        editTextMobile = (EditText) findViewById(R.id.editTextMobile2);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);
    }

    private void RegisterUser() {
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String mobile = editTextMobile.getText().toString().trim();
        if (!editTextPassword.getText().toString().equals(editTextRePassword.getText().toString())) {
            editTextRePassword.setError("Passwords don't match");
        }
        else if (!email.matches(emailPattern) || email.isEmpty()) {
            editTextEmail.setError("Inavalid email");
        }
        else if(password.isEmpty() || password.length() < 8){
            editTextPassword.setError("Password should be greater than 8 characters");
        }
        else if (name.isEmpty()){
            editTextName.setError("Enter your name");
        }
        else if (mobile.isEmpty()){
            editTextName.setError("Enter your mobile number");
        }
        else {
            final String mobileNew = "+91"+mobile;
            new Thread(new Runnable() {
                @Override
                public void run() {
            JSONObject js = new JSONObject();
            try {
                JSONObject jsonobject_one = new JSONObject();
                jsonobject_one.put("name", name);
                jsonobject_one.put("email", email);
                jsonobject_one.put("password", password);
                jsonobject_one.put("mobile", mobileNew);

                js.put("store", jsonobject_one);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST,
                    REGISTER_URL, js,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            // Toast.makeText(SignUp_Activity.this,response.toString(),Toast.LENGTH_LONG).show();
                            try {
                                String id = response.getString("id");
                                Intent intent = new Intent(SignUp_Activity.this, Verify.class);
                                intent.putExtra("id",id);
                                intent.putExtra("email",email);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } catch (JSONException e) {
                                Snackbar.make(view,"Please enter all the details correctly",Snackbar.LENGTH_LONG).show();
                                pDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Snackbar.make(view, "Please enter all the details correctly", Snackbar.LENGTH_LONG).show();
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

            RequestQueue requestQueue = Volley.newRequestQueue(SignUp_Activity.this);
            requestQueue.add(jsonObject);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog = new SweetAlertDialog(SignUp_Activity.this, SweetAlertDialog.PROGRESS_TYPE);
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

        if(v == buttonRegister){
            RegisterUser();
        }
    }

}

