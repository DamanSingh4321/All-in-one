package com.example.dell.ujstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GridActivity extends AppCompatActivity {
    String regex = "\\[|\\]";
    ArrayList<String> imageshow = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GridView grid;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_laayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String url = getIntent().getExtras().getString("imageUrl");
        System.out.print(url);
        final String leadid = getIntent().getExtras().getString("id");
        List<String> imagelist = Arrays.asList(url.split(","));
        System.out.println(imagelist);
        for (int i=0; i<imagelist.size();i++) {
            String list = imagelist.get(i);
            String urlS = list.trim();
            String urlmain = urlS.replaceAll(regex, "");
            imageshow.add(urlmain);
            System.out.println("IMaage:"+imageshow);
        }
        Grid_Single adapter = new Grid_Single(this, imageshow);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(GridActivity.this,ImageView_Activity.class);
                intent.putExtra("IMAGE",imageshow.get(position));
                intent.putExtra("leadid",leadid);
                startActivity(intent);

            }
        });
    }
}
