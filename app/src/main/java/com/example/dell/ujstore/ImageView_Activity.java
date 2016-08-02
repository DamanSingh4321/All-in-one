package com.example.dell.ujstore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageView_Activity extends AppCompatActivity {

    private PhotoViewAttacher mAttacher;
    private ImageView mImageView;
    private Button btn;
    String regex = "\\[|\\]";
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String url = getIntent().getExtras().getString("IMAGE");
        final String id = getIntent().getExtras().getString("leadid");
        viewPager = (ViewPager) this.findViewById(R.id.container);
        mImageView = (ImageView) findViewById(R.id.imageView);
        btn = (Button) findViewById(R.id.applyimage);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitList(id);
            }
        });
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
            String url = getIntent().getExtras().getString("IMAGE");
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
                    .setDestinationInExternalPublicDir("/UjImages", "image.jpg");

            mgr.enqueue(request);
        return true;
        }

        if(id == R.id.share) {
            ImageView ivImage = (ImageView) findViewById(R.id.imageView);
            Drawable mDrawable = ivImage.getDrawable();
            Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

            String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                    mBitmap, "Image Description", null);

            Uri uri = Uri.parse(path);// see previous remote images section
            // Construct share intent as described above based on bitmap
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent,"Share via"));
        }

        return super.onOptionsItemSelected(item);
    }

    public void submitList(final String id) {
        LayoutInflater inflater = LayoutInflater.from(ImageView_Activity.this);
        View subView = inflater.inflate(R.layout.dialog_accept, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ImageView_Activity.this);
        builder.setTitle("Discount & Offer");
        builder.setMessage("Enter discount");
        final String Submit_Url = "https://ujapi.herokuapp.com/api/v1/s/bookings/" + id + "/respond_bookings";
        SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        final String authtoken = pref.getString("token", null);
        final EditText discount = (EditText) subView.findViewById(R.id.editTextmindis);
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("RESPOND", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dis_amount = discount.getText().toString();
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("discount", dis_amount);
                    jsonBody.put("booking_id", id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST,
                        Submit_Url, jsonBody,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(ImageView_Activity.this,"Successfully responded",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ImageView_Activity.this, SwipeTabActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Authorization", authtoken);
                        return headers;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(ImageView_Activity.this);
                requestQueue.add(jsonObject);
            }
        });


        builder.show();
    }

}