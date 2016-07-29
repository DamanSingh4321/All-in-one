package com.example.dell.ujstore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Imagenobtn extends AppCompatActivity {

    private PhotoViewAttacher mAttacher;
    private ImageView mImageView;
    String regex = "\\[|\\]";
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagenobtn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String url = getIntent().getExtras().getString("IMAGE");
        final String id = getIntent().getExtras().getString("id");
        viewPager = (ViewPager) findViewById(R.id.container);
        mImageView = (ImageView) findViewById(R.id.imageViewno);
        Callback imageLoadedCallback = new Callback() {

            @Override
            public void onSuccess() {
                if(mAttacher!=null){
                    mAttacher.update();
                }else{
                    mAttacher = new PhotoViewAttacher(mImageView);
                }
            }

            @Override
            public void onError() {

            }
        };

        Picasso.with(this)
                .load(url)
                .into(mImageView, imageLoadedCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.download) {
            String url = getIntent().getExtras().getString("imageUrl");
            File direct = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/UjImages");

            if (!direct.exists()) {
                direct.mkdirs();
            }

            DownloadManager mgr = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadUri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(
                    downloadUri);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("Demo")
                    .setDescription("Something useful. No, really.")
                    .setDestinationInExternalPublicDir("/UjImages", "fileName.jpg");

            mgr.enqueue(request);
            return true;
        }

        if(id == R.id.share){
            ImageView iv = (ImageView )findViewById(R.id.imageView);
            iv.getDrawable();
            String fileName = "image.jpg";
            iv.setDrawingCacheEnabled(true);
            Bitmap bitmap = iv.getDrawingCache();
            try
            {
                FileOutputStream ostream = this.openFileOutput( fileName, Context.MODE_WORLD_READABLE);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                ostream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");
            share.putExtra(Intent.EXTRA_SUBJECT, "Lead pic!");
            share.putExtra(Intent.EXTRA_TEXT, "Unclejoy lead image");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile( new File( this.getFileStreamPath( fileName).getAbsolutePath())));
            startActivity(Intent.createChooser(share,"Share via"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
