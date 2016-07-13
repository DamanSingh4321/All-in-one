package com.example.dell.ujstore;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

public class profile2_activity extends AppCompatActivity {
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
        setContentView(R.layout.layout_profile2_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editTextsName = (EditText) findViewById(R.id.sNameText);
        editTextsAdd = (EditText) findViewById(R.id.sAddText);
        editTextsMobile = (EditText) findViewById(R.id.sMobile);
        editTextsEmail = (EditText) findViewById(R.id.sEmail);
        editTextsTiming = (EditText) findViewById(R.id.sTiming);
        editTextsOff = (EditText) findViewById(R.id.sOffDay);
        editTextsAbout = (EditText) findViewById(R.id.sAbout);
        editTextsCerti = (EditText) findViewById(R.id.sCertified);

    }
}
