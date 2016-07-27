package com.example.dell.ujstore;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.github.chuross.library.ExpandableLayout;

import java.util.ArrayList;

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
        public TextView address2;
        public TextView time_ago2;
        public TextView booking_time2;
        public TextView booking_date2;
        ExpandableLayout llExpandArea2;

        public MyViewHolder(View v,int viewType) {
            super(v);
            if (viewType == 0) {
            viewPager = (ViewPager) ((SwipeTabActivity) mContext).findViewById(R.id.container);
            mCardView2 = (CardView) v.findViewById(R.id.card_view2);
            mTextView2 = (TextView) v.findViewById(R.id.tv_text2);
            storeText2 = (TextView) v.findViewById(R.id.type2);
            viewimage2 = (TextView) v.findViewById(R.id.viewimage2);
            address2 = (TextView) v.findViewById(R.id.address2);
            time_ago2 = (TextView) v.findViewById(R.id.time_ago2);
            booking_date2 = (TextView) v.findViewById(R.id.booking_date2);
            booking_time2 = (TextView) v.findViewById(R.id.booking_time2);
            llExpandArea2 = (ExpandableLayout) v.findViewById(R.id.llExpandArea2);
            mCardView2.setOnClickListener(this);
            } else if (viewType == 1) {

            }
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
        View v;
        MyViewHolder holder;
        switch (viewType) {
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card2_layout, parent, false);
                holder = new MyViewHolder(v, viewType);
                holder.mCardView2.setTag(holder);
                return holder;
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.respondempty, parent, false);
                holder = new MyViewHolder(v, viewType);
                return holder;
        }
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            holder.mTextView2.setText(mDataset.get(position));
            holder.storeText2.setText(StoreType.get(position));
            holder.address2.setText(addString.get(position));
            holder.time_ago2.setText(ago.get(position));
            holder.booking_date2.setText(date.get(position));
            holder.booking_time2.setText(time.get(position));
            setScaleAnimation(holder.itemView);
            String s = imageUrl.get(position);
            if (s.equals("[]")) {
                holder.viewimage2.setVisibility(View.GONE);
            } else
                holder.viewimage2.setVisibility(View.VISIBLE);
            holder.viewimage2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GridActivity.class);
                    String imageurl = imageUrl.get(position);
                    System.out.println(imageurl);
                    intent.putExtra("imageUrl", imageurl);
                    intent.putExtra("id", "null");
                    v.getContext().startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(mDataset.size() == 0){
            return 1;
        }else {
            return mDataset.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0; //Default is 1
        if (mDataset.size() == 0) viewType = 1; //if zero, it will be a header view
        return viewType;
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(200);
        view.startAnimation(anim);
    }
}