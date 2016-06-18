package com.example.dell.ujstore;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by DELL on 18-Jun-16.
 */
public class OneAdapter extends RecyclerView.Adapter<OneAdapter.MyViewHolder> implements View.OnClickListener {
    private int expandedPosition = -1;
    private ArrayList<String> mDataset;
    private Context mContext;
    int a=0;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        LinearLayout llExpandArea;

        public MyViewHolder(View v) {
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.tv_text);
            llExpandArea = (LinearLayout) itemView.findViewById(R.id.llExpandArea);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OneAdapter (Context context, ArrayList<String> myDataset){
        this.mDataset = myDataset;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OneAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder holder = new MyViewHolder(v);

        // Sets the click adapter for the entire cell
        // to the one in this class.
        holder.itemView.setOnClickListener(OneAdapter.this);
        holder.itemView.setTag(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(a==0) {
           holder.mTextView.setText(mDataset.get(position));
            if (position == expandedPosition) {
                holder.llExpandArea.setVisibility(View.VISIBLE);
                a=1;
            } else {
                holder.llExpandArea.setVisibility(View.GONE);
            }
        }
        else{
            holder.mTextView.setText(mDataset.get(position));
            holder.llExpandArea.setVisibility(View.GONE);
            Toast.makeText(mContext, "Clicked again", Toast.LENGTH_SHORT).show();
            a=0;
        }
    }

    @Override
    public void onClick(View view) {
        MyViewHolder holder = (MyViewHolder) view.getTag();
        String theString = mDataset.get(holder.getLayoutPosition());

        // Check for an expanded view, collapse if you find one
        if (expandedPosition >= 0) {
            int prev = expandedPosition;
            notifyItemChanged(prev);
        }
        // Set the current position to "expanded"
        expandedPosition = holder.getLayoutPosition();
        notifyItemChanged(expandedPosition);

        //Toast.makeText(mContext, "Clicked: "+theString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}