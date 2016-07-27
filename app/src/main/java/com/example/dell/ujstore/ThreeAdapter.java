package com.example.dell.ujstore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.github.chuross.library.ExpandableLayout;

import java.util.ArrayList;

/**
 * Created by DELL on 15-Jul-16.
 */
public class ThreeAdapter extends RecyclerView.Adapter<ThreeAdapter.MyViewHolder> {
    private ArrayList<String> mDataset;
    private ArrayList<String> StoreType;
    private ArrayList<String> imageUrl;
    private ArrayList<String> addString;
    private ArrayList<String> lead_id;
    private ArrayList<String> date;
    private ArrayList<String> time;
    private ArrayList<String> ago;
    private ArrayList<String> mobile;
    private Context mContext;
    SharedPreferences pref;
    ViewPager viewPager;
    private int VIEW_EMPTY = 1;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mCardView3;
        public TextView mTextView3;
        public TextView storeText3;
        public TextView viewimage3;
        public TextView time_ago3;
        public TextView booking_time3;
        public TextView booking_date3;
        public TextView adress3;
        public Button btncall;
        ExpandableLayout llExpandArea3;

        public MyViewHolder(View v,int viewType) {
            super(v);
            if (viewType == 0) {
            viewPager = (ViewPager) ((SwipeTabActivity) mContext).findViewById(R.id.container);
            mCardView3 = (CardView) v.findViewById(R.id.card_view3);
            mTextView3 = (TextView) v.findViewById(R.id.tv_text3);
            storeText3 = (TextView) v.findViewById(R.id.type3);
            viewimage3 = (TextView) v.findViewById(R.id.viewimage3);
            adress3 = (TextView) v.findViewById(R.id.address3);
            time_ago3 = (TextView) v.findViewById(R.id.time_ago3);
            booking_date3 = (TextView) v.findViewById(R.id.booking_date3);
            booking_time3 = (TextView) v.findViewById(R.id.booking_time3);
            btncall = (Button) v.findViewById(R.id.btncall);
            llExpandArea3 = (ExpandableLayout) itemView.findViewById(R.id.llExpandArea3);
            mCardView3.setOnClickListener(this);
            } else if (viewType == 1) {

            }
        }

        @Override
        public void onClick(View view) {
            if (view == mCardView3) {
                MyViewHolder holder = (MyViewHolder) view.getTag();
                if (holder.llExpandArea3.isCollapsed()) {
                    holder.llExpandArea3.expand();
                } else {
                    holder.llExpandArea3.collapse();
                }
            }
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ThreeAdapter(Context context, ArrayList<String> myDataset, ArrayList<String> StoreType,
                        ArrayList<String> imageUrl, ArrayList<String> addString, ArrayList<String> lead_id,
                        ArrayList<String> date, ArrayList<String> time, ArrayList<String> ago, ArrayList<String> mobile) {
        this.mDataset = myDataset;
        this.StoreType = StoreType;
        this.imageUrl = imageUrl;
        this.addString = addString;
        this.lead_id = lead_id;
        this.date = date;
        this.time = time;
        this.ago = ago;
        this.mContext = context;
        this.mobile = mobile;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ThreeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card3_layout, parent, false);
                MyViewHolder holder = new MyViewHolder(v,viewType);
                VIEW_EMPTY = 0;
                parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyViewHolder holder = (MyViewHolder) v.getTag();
                if (holder.llExpandArea3.isCollapsed()) {
                    holder.llExpandArea3.expand();
                } else {
                    holder.llExpandArea3.collapse();
                }
            }
        });
                holder.mCardView3.setTag(holder);
                return holder;
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.hiredempty, parent, false);
                holder = new MyViewHolder(v, viewType);
                return holder;
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try{
        holder.mTextView3.setText(mDataset.get(position));
        holder.storeText3.setText(StoreType.get(position));
        holder.adress3.setText(addString.get(position));
        holder.time_ago3.setText(ago.get(position));
        holder.booking_date3.setText(date.get(position));
        holder.booking_time3.setText(time.get(position));
        setScaleAnimation(holder.itemView);
            holder.btncall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = mobile.get(position);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    mContext.startActivity(intent);
                }
            });
        String s = imageUrl.get(position);
        if (s.equals("[]")) {
            holder.viewimage3.setVisibility(View.GONE);
        } else
            holder.viewimage3.setVisibility(View.VISIBLE);
        holder.viewimage3.setOnClickListener(new View.OnClickListener() {
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
    }

    catch(Exception e) {
        e.printStackTrace();
    }
}

    @Override
    public void onViewRecycled(MyViewHolder holder) {
        if(VIEW_EMPTY == 0) {
            holder.llExpandArea3.collapse();
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