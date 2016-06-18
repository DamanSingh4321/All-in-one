package com.example.dell.ujstore;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuItem;
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
        import com.android.volley.toolbox.Volley;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.HashMap;
        import java.util.Map;

public class SignUp_Activity extends AppCompatActivity implements View.OnClickListener{
    private static final String REGISTER_URL = "https://ujapi.herokuapp.com/api/v1/users";
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;

    private Button buttonRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);
    }

    private void RegisterUser(){
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        JSONObject js =new JSONObject();
        try {
            JSONObject jsonobject_one = new JSONObject();
            jsonobject_one.put("name", name);
            jsonobject_one.put("email", email);
            jsonobject_one.put("password", password);

            js.put("user",jsonobject_one);

        }catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST,
                REGISTER_URL, js,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                       // Toast.makeText(SignUp_Activity.this,response.toString(),Toast.LENGTH_LONG).show();
                       try {
                           String name = response.getString("name");
                           String authtoken = response.getString("authentication_token");
                           SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                           SharedPreferences.Editor editor = pref.edit();
                           editor.putString("name", name);
                           editor.putString("token", authtoken);
                           editor.putString("email", email);
                           editor.commit();
                           Intent intent = new Intent(SignUp_Activity.this, SwipeTabActivity.class);
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           intent.putExtra("email", email);
                           intent.putExtra("name", name);
                           startActivity(intent);
                       }
                       catch (JSONException e) {
                           Toast.makeText(SignUp_Activity.this, e.toString(), Toast.LENGTH_LONG).show();
                       }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUp_Activity.this,error.toString(),Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObject);
    }

    @Override
    public void onClick(View v) {

        if(v == buttonRegister){
            RegisterUser();
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

