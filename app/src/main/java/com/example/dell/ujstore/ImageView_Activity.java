package com.example.dell.ujstore;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
    share.putExtra(Intent.EXTRA_SUBJECT, "Great photo from Poland!");
    share.putExtra(Intent.EXTRA_TEXT, "Hi,  I'm sharing with you this great picture!");
    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile( new File( this.getFileStreamPath( fileName).getAbsolutePath())));
    startActivity(Intent.createChooser(share,"Share via"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}