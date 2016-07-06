package com.example.dell.ujstore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class profile_activity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextsName;
    private EditText editTextsAdd;
    private EditText editTextsMobile;
    private EditText editTextsTiming;
    private EditText editTextsEmail;
    private EditText editTextsOff;
    private EditText editTextsAbout;
    private EditText editTextsCerti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        editTextName = (EditText) findViewById(R.id.nameText);
        editTextEmail = (EditText) findViewById(R.id.emailText);
        editTextPhone = (EditText) findViewById(R.id.phoneText);
        editTextsName = (EditText) findViewById(R.id.sNameText);
        editTextsAdd = (EditText) findViewById(R.id.sAddText);
        editTextsMobile = (EditText) findViewById(R.id.sMobile);
        editTextsEmail = (EditText) findViewById(R.id.sEmail);
        editTextsTiming = (EditText) findViewById(R.id.sTiming);
        editTextsOff = (EditText) findViewById(R.id.sOffDay);
        editTextsAbout = (EditText) findViewById(R.id.sAbout);
        editTextsCerti = (EditText) findViewById(R.id.sCertified);


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
