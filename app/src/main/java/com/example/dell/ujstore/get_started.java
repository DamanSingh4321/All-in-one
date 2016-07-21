package com.example.dell.ujstore;



import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import me.relex.circleindicator.CircleIndicator;

public class get_started extends AppCompatActivity {

    private ViewPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_started_layout);
        mSectionsPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        indicator.setViewPager(mViewPager);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public class ViewPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Fragment f1 = new get_started_one();
                    return f1;
                case 1:
                    Fragment f2 = new get_started_two();
                    return f2;
                case 2:
                    Fragment f3 = new get_started_three();
                    return f3;
                case 3:
                    Fragment f4 = new get_started_four();
                    return f4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

    }
}