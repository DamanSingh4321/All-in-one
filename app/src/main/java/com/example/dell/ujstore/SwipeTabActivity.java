package com.example.dell.ujstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Intent.ACTION_PICK;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
import static com.squareup.picasso.Callback.EmptyCallback;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

public class SwipeTabActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager mViewPager;
    private static int RESULT_LOAD_IMG = 1;
    SharedPreferences pref;
    private ImageView imageView;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        imageView = (ImageView) hView.findViewById(R.id.imageViewnav);
        TextView tvname = (TextView) hView.findViewById(R.id.headername);
        TextView tvemail = (TextView) hView.findViewById(R.id.headeremail);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String name = pref.getString("name", null);
        pref.getString("token", null);
        String email = pref.getString("email", null);
        tvemail.setText(email);
        tvname.setText(name);
        loadImage();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(ACTION_PICK, EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, RESULT_LOAD_IMG);
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.container);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("NEW LEAD"));
        tabLayout.addTab(tabLayout.newTab().setText("RESPONSE"));
        tabLayout.addTab(tabLayout.newTab().setText("HIRE"));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
             mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                image = data.getData().toString();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("ImagePref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("image", image);
                editor.apply();
                loadImage();
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            Intent intent = new Intent(this,setting_activity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            SharedPreferences pref2 = getApplicationContext().getSharedPreferences("ImagePref", 0);
            SharedPreferences.Editor editor2 = pref2.edit();
            editor2.clear();
            editor2.apply();
            Intent intent = new Intent(SwipeTabActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.store_profile) {
            Intent intent = new Intent(this,profile_activity.class);
            startActivity(intent);

        } else if (id == R.id.how_it_works) {
            Intent intent = new Intent(this,get_started.class);
            startActivity(intent);

        } else if (id == R.id.privacy_policy) {
            Intent intent = new Intent(this,privacy_activity.class);
            startActivity(intent);

        } else if (id == R.id.term_condition) {
            Intent intent = new Intent(this,term_activity.class);
            startActivity(intent);
        } else if (id == R.id.settings) {
            Intent intent = new Intent(this,setting_activity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
        int nooftabs;

        public ViewPagerAdapter(FragmentManager manager, int nooftabs) {
            super(manager);
            this.nooftabs = nooftabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    OneFragment f1 = new OneFragment();
                    return f1;
                case 1:
                    TwoFragment f2 = new TwoFragment();
                    return f2;
                case 2:
                    ThreeFragment f3 = new ThreeFragment();
                    return f3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return nooftabs;
        }

    }

    private void loadImage() {
        pref = getApplicationContext().getSharedPreferences("ImagePref", 0); // 0 - for private mode
        image = pref.getString("image", null);
        if(image != null) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .showImageForEmptyUri(R.drawable.gray_button_background)
                    .showImageOnFail(R.drawable.blue_button_background)
                    .showImageOnLoading(R.drawable.ic_download).build();

            ImageAware imageAware = new ImageViewAware(imageView, false);
            imageLoader.displayImage(image, imageAware, options);
        }
    }
}