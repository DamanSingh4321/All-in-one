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
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.chuross.library.ExpandableLayout;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 18-Jun-16.
 */
public class OneAdapter extends RecyclerView.Adapter<OneAdapter.MyViewHolder> {
    private ArrayList<String> mDataset;
    private ArrayList<String> StoreType;
    private ArrayList<String> imageUrl;
    private ArrayList<String> addString;
    private ArrayList<String> lead_id;
    private ArrayList<String> date;
    private ArrayList<String> time;
    private ArrayList<String> ago;
    private Context mContext;
    SharedPreferences pref;
    ViewPager viewPager;
    private int VIEW_EMPTY = 1;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mCardView;
        public TextView mTextView;
        public TextView storeText;
        public TextView viewimage;
        public TextView time_ago;
        public TextView booking_time;
        public TextView booking_date;
        public TextView adress;
        private Button apply;
        ExpandableLayout llExpandArea;

        public MyViewHolder(View v, int viewType) {
            super(v);
            if (viewType == 0) {
                viewPager = (ViewPager) ((SwipeTabActivity) mContext).findViewById(R.id.container);
                mCardView = (CardView) v.findViewById(R.id.card_view);
                mTextView = (TextView) v.findViewById(R.id.tv_text);
                storeText = (TextView) v.findViewById(R.id.type);
                viewimage = (TextView) v.findViewById(R.id.viewimage);
                adress = (TextView) v.findViewById(R.id.address);
                time_ago = (TextView) v.findViewById(R.id.time_ago);
                booking_date = (TextView) v.findViewById(R.id.booking_date);
                booking_time = (TextView) v.findViewById(R.id.booking_time);
                apply = (Button) v.findViewById(R.id.apply);
                llExpandArea = (ExpandableLayout) itemView.findViewById(R.id.llExpandArea);
                mCardView.setOnClickListener(this);
            } else if (viewType == 1) {

            }
        }

        @Override
        public void onClick(View view) {
            if(view == mCardView) {
                MyViewHolder holder = (MyViewHolder) view.getTag();
                if (holder.llExpandArea.isCollapsed()) {
                    holder.llExpandArea.expand();
                } else {
                    holder.llExpandArea.collapse();
                }
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OneAdapter (Context context, ArrayList<String> myDataset, ArrayList<String> StoreType,
                       ArrayList<String> imageUrl, ArrayList<String> addString, ArrayList<String> lead_id,
                       ArrayList<String> date, ArrayList<String> time, ArrayList<String> ago){
        this.mDataset = myDataset;
        this.StoreType = StoreType;
        this.imageUrl = imageUrl;
        this.addString = addString;
        this.lead_id = lead_id;
        this.date = date;
        this.time = time;
        this.ago = ago;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OneAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        MyViewHolder holder;
       switch (viewType){
           case 0:
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cards_layout, parent, false);
            holder = new MyViewHolder(v, viewType);
               VIEW_EMPTY = 0;
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyViewHolder holder = (MyViewHolder) v.getTag();
                    if (holder.llExpandArea.isCollapsed()) {
                        holder.llExpandArea.expand();
                    } else {
                        holder.llExpandArea.collapse();
                    }
                }
            });
            holder.mCardView.setTag(holder);
            return holder;
           default:
               v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.leadempty, parent, false);
               holder = new MyViewHolder(v, viewType);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            holder.mTextView.setText(mDataset.get(position));
            holder.storeText.setText(StoreType.get(position));
            holder.adress.setText(addString.get(position));
            holder.time_ago.setText(ago.get(position));
            holder.booking_date.setText(date.get(position));
            holder.booking_time.setText(time.get(position));
            setScaleAnimation(holder.itemView);
            final String id = lead_id.get(position);
            String s = imageUrl.get(position);
            if (s.equals("[]")) {
                holder.viewimage.setVisibility(View.GONE);
            } else
                holder.viewimage.setVisibility(View.VISIBLE);
            holder.viewimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GridActivity.class);
                    String imageurl = imageUrl.get(position);
                    System.out.println(imageurl);
                    intent.putExtra("imageUrl", imageurl);
                    intent.putExtra("id",id);
                    v.getContext().startActivity(intent);
                }
            });
            holder.apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.viewimage.getVisibility() == View.VISIBLE) {
                       submitList(id);
                    } else {
                        submiit(id);
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onViewRecycled(MyViewHolder holder) {
        if(VIEW_EMPTY == 0) {
            holder.llExpandArea.collapse();
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

    private void submiit(String id) {
        String Submit_Url = "https://ujapi.herokuapp.com/api/v1/s/bookings/" + id + "/respond_bookings";
        pref = this.mContext.getSharedPreferences("MyPref", 0); // 0 - for private mode
        final String authtoken = pref.getString("token", null);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("discount", "0");
            jsonBody.put("booking_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST,
                Submit_Url, jsonBody,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                       viewPager.setCurrentItem(1);
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

        RequestQueue requestQueue = Volley.newRequestQueue(this.mContext);
        requestQueue.add(jsonObject);
    }

    public void submitList(final String id) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View subView = inflater.inflate(R.layout.dialog_accept, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Discount & Offer");
        builder.setMessage("Enter discount");
        final String Submit_Url = "https://ujapi.herokuapp.com/api/v1/s/bookings/" + id + "/respond_bookings";
        pref = this.mContext.getSharedPreferences("MyPref", 0); // 0 - for private mode
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
                                viewPager.setCurrentItem(1);
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

                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                requestQueue.add(jsonObject);
            }
        });


        builder.show();
    }
}