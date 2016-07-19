package com.example.dell.ujstore;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.appyvet.rangebar.RangeBar;
import com.github.chuross.library.ExpandableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 13-Jul-16.
 */
public class TwoAdapter extends RecyclerView.Adapter<TwoAdapter.MyViewHolder> {
    private ArrayList<String> mDataset;
    private ArrayList<String> StoreType;
    private ArrayList<String> imageUrl;
    private ArrayList<String> addString;
    private ArrayList<String> date;
    private ArrayList<String> time;
    private ArrayList<String> ago;
    private Context mContext;
    ViewPager viewPager;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mCardView2;
        public TextView mTextView2;
        public TextView storeText2;
        public TextView viewimage2;
        public TextView adress2;
        public TextView time_ago2;
        public TextView booking_time2;
        public TextView booking_date2;
        ExpandableLayout llExpandArea2;

        public MyViewHolder(View v) {
            super(v);
            viewPager = (ViewPager) ((SwipeTabActivity) mContext).findViewById(R.id.container);
            mCardView2 = (CardView) v.findViewById(R.id.card_view2);
            mTextView2 = (TextView) v.findViewById(R.id.tv_text2);
            storeText2 = (TextView) v.findViewById(R.id.type2);
            viewimage2 = (TextView) v.findViewById(R.id.viewimage2);
            adress2 = (TextView) v.findViewById(R.id.address2);
            time_ago2 = (TextView) v.findViewById(R.id.time_ago2);
            booking_date2 = (TextView) v.findViewById(R.id.booking_date2);
            booking_time2 = (TextView) v.findViewById(R.id.booking_time2);
            llExpandArea2 = (ExpandableLayout) v.findViewById(R.id.llExpandArea2);
            mCardView2.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == mCardView2) {
                MyViewHolder holder = (MyViewHolder) view.getTag();
                if (holder.llExpandArea2.isExpanded()) {
                    holder.llExpandArea2.collapse();
                } else {
                    holder.llExpandArea2.expand();
                }
            }
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TwoAdapter(Context context, ArrayList<String> myDataset, ArrayList<String> StoreType,
                      ArrayList<String> imageUrl, ArrayList<String> addString,
                      ArrayList<String> date, ArrayList<String> time, ArrayList<String> ago) {
        this.mDataset = myDataset;
        this.StoreType = StoreType;
        this.imageUrl = imageUrl;
        this.addString = addString;
        this.date = date;
        this.time = time;
        this.ago = ago;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TwoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card2_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        holder.mCardView2.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.mTextView2.setText(mDataset.get(position));
        holder.storeText2.setText(StoreType.get(position));
        holder.adress2.setText(addString.get(position));
        holder.time_ago2.setText(ago.get(position));
        holder.booking_date2.setText(date.get(position));
        holder.booking_time2.setText(time.get(position));
        String s = imageUrl.get(position);
        if (s == "null") {
            holder.viewimage2.setVisibility(View.GONE);
        } else
            holder.viewimage2.setVisibility(View.VISIBLE);
        holder.viewimage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageView_Activity.class);
                String imageurl = "https://ujapi.herokuapp.com" + imageUrl.get(position);
                System.out.println(imageurl);
                intent.putExtra("imageUrl", imageurl);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}