package com.example.dell.ujstore;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by DELL on 17-Jun-16.
 */
public class OneFragment extends Fragment {
    private Random randy = new Random();
    private ArrayList<String> myDataset;
    private OneAdapter adapter;


    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        rv.setHasFixedSize(true);
        myDataset = new ArrayList<String>();

        // Load up the dataset with random titles
        myDataset.add("Name 1");
        myDataset.add("Name 2");
        myDataset.add("Name 3");
        myDataset.add("Name 4");
        myDataset.add("Name 5");
        myDataset.add("Name 6");
        myDataset.add("Name 7");
        myDataset.add("Name 8");
        myDataset.add("Name 9");

         adapter = new OneAdapter(getContext(), myDataset);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

}
