package com.example.dell.ujstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by daman on 25/7/16.
 */
public class Grid_Single extends BaseAdapter{

    private Context mContext;
    private ArrayList<String> Imageid;

    public Grid_Single(Context c,ArrayList<String> Imageid ) {
        mContext = c;
        this.Imageid = Imageid;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Imageid.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return Imageid.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            Picasso.with(mContext)
                    .load(Imageid.get(position))
                    .placeholder(R.drawable.loading)
                    .into(imageView);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
