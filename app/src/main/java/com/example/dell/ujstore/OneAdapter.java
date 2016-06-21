package com.example.dell.ujstore;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
        private Button apply;
        private Button reject;
        LinearLayout llExpandArea;

        public MyViewHolder(View v) {
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.tv_text);
            apply = (Button) v.findViewById(R.id.apply);
            reject = (Button) v.findViewById(R.id.reject);
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
    public OneAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(v);

        holder.itemView.setOnClickListener(OneAdapter.this);
        holder.itemView.setTag(holder);
        holder.apply.setOnClickListener(this);
        holder.reject.setOnClickListener(this);
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
            a=0;
        }
        holder.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptDialog();
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectDialog();
            }
        });
    }

    @Override
    public void onClick(View view) {
        MyViewHolder holder = (MyViewHolder) view.getTag();
//        String theString = mDataset.get(holder.getAdapterPosition());

        // Check for an expanded view, collapse if you find one
        if (expandedPosition >= 0) {
            int prev = expandedPosition;
            notifyItemChanged(prev);
        }
        // Set the current position to "expanded"
        expandedPosition = holder.getAdapterPosition();
        notifyItemChanged(expandedPosition);


        //Toast.makeText(mContext, "Clicked: "+theString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void acceptDialog(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View subView = inflater.inflate(R.layout.dialog_accept, null);
        final EditText subEditText = (EditText)subView.findViewById(R.id.dialogEditText);
        final ImageView subImageView = (ImageView)subView.findViewById(R.id.image);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("AlertDialog");
        builder.setMessage("AlertDialog Message");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = subEditText.getText().toString();
                Toast.makeText(mContext, s,Toast.LENGTH_LONG).show();
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
        final EditText subEditText = (EditText)subView.findViewById(R.id.dialogEditText);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("AlertDialog");
        builder.setMessage("AlertDialog Message");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = subEditText.getText().toString();
                Toast.makeText(mContext, s,Toast.LENGTH_LONG).show();
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
}