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
    private ArrayList<String> lead_id;
    private Context mContext;
    SharedPreferences pref;
    ViewPager viewPager;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mCardView;
        public TextView mTextView;
        public TextView storeText;
        public TextView viewimage;
        public TextView adress;
        private Button apply;
        private Button reject;
        ExpandableLayout llExpandArea;

        public MyViewHolder(View v) {
            super(v);
            viewPager = (ViewPager) ((SwipeTabActivity)mContext).findViewById(R.id.container);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.tv_text);
            storeText = (TextView) v.findViewById(R.id.type);
            viewimage = (TextView) v.findViewById(R.id.viewimage);
            adress = (TextView) v.findViewById(R.id.address);
            apply = (Button) v.findViewById(R.id.apply);
            reject = (Button) v.findViewById(R.id.reject);
            llExpandArea = (ExpandableLayout) itemView.findViewById(R.id.llExpandArea);
            mCardView.setOnClickListener(this);
            reject.setOnClickListener(this);
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
            if(view == apply){
                acceptDialog();
            }
            if (view == reject){
                rejectDialog();
            }
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TwoAdapter (Context context, ArrayList<String> myDataset, ArrayList<String> StoreType,
                       ArrayList<String> imageUrl, ArrayList<String> addString, ArrayList<String> lead_id){
        this.mDataset = myDataset;
        this.StoreType = StoreType;
        this.imageUrl = imageUrl;
        this.addString = addString;
        this.lead_id = lead_id;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TwoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
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
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.mTextView.setText(mDataset.get(position));
        holder.storeText.setText(StoreType.get(position));
        holder.adress.setText(addString.get(position));
//        final String id = lead_id.get(position);
        String s = imageUrl.get(position);
        if (s == "null") {
            holder.viewimage.setVisibility(View.GONE);
        } else
            holder.viewimage.setVisibility(View.VISIBLE);
        holder.viewimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageView_Activity.class);
                String imageurl = "https://ujapi.herokuapp.com" + imageUrl.get(position);
                System.out.println(imageurl);
                intent.putExtra("imageUrl", imageurl);
                v.getContext().startActivity(intent);
            }
        });
        holder.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.viewimage.getVisibility() == View.VISIBLE) {
                    acceptDialog();
                } else {
                   // submiit(id);
                }
            }
        });
    }

    @Override
    public void onViewRecycled(MyViewHolder holder) {
        holder.llExpandArea.collapse();
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void acceptDialog(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View subView = inflater.inflate(R.layout.dialog_accept, null);
        final EditText seektext = (EditText)subView.findViewById(R.id.seektext);
        RangeBar rangebar = (RangeBar)subView.findViewById(R.id.rangebar);
        float seek = Float.parseFloat(seektext.getText().toString());
        rangebar.setRangePinsByValue(0,seek);
        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                seektext.setText(rightPinValue);

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("AlertDialog");
        builder.setMessage("AlertDialog Message");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "Cancel", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }
    private void rejectDialog(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View subView = inflater.inflate(R.layout.dialog_reject, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("AlertDialog");
        builder.setMessage("AlertDialog Message");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "Yes",Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "No", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
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

}
