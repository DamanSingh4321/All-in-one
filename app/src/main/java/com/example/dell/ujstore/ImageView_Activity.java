package com.example.dell.ujstore;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageView_Activity extends AppCompatActivity {

    private PhotoViewAttacher mAttacher;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String url = getIntent().getExtras().getString("imageUrl");
        System.out.print(url);

        mImageView = (ImageView) findViewById(R.id.imageView);

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
                .fit()
                .into(mImageView, imageLoadedCallback);
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

        return super.onOptionsItemSelected(item);
    }

}