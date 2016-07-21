package com.example.dell.ujstore;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by DELL on 17-Jun-16.
 */
public class OneFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private ArrayList<String> myDataset = new ArrayList<String>();
    private ArrayList<String> StoreType = new ArrayList<String>();
    private ArrayList<String> imageUrl = new ArrayList<String>();
    private ArrayList<String> images = new ArrayList<String>();
    private ArrayList<String> addString = new ArrayList<String>();
    private ArrayList<String> lead_id = new ArrayList<String>();
    private ArrayList<String> date = new ArrayList<String>();
    private ArrayList<String> time = new ArrayList<String>();
    private ArrayList<String> ago = new ArrayList<String>();
    private OneAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    CoordinatorLayout coordinatorLayout;
    private String URL = "https://ujapi.herokuapp.com/api/v1/s/bookings/openall";
    SharedPreferences pref;
    int check_update=0;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        coordinatorLayout = (CoordinatorLayout)rootView.findViewById(R.id.coordinator_layout);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        rv.setHasFixedSize(true);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        System.out.println("Mei");
        adapter = new OneAdapter(getContext(), myDataset, StoreType, imageUrl, addString, lead_id, date, time, ago);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_orange_light,
                android.R.color.holo_orange_dark);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                        int x = pref.getInt("check", 0);
                                        data();
                                        adapter.notifyDataSetChanged();
                                        int a = x - check_update;
                                        if (a != 0) {
                                            Snackbar.make(coordinatorLayout, "Updated List:" + a, Snackbar.LENGTH_SHORT).show();
                                            System.out.println("Snackbar");
                                            check_update = x;
                                        } else {
                                            Snackbar.make(coordinatorLayout, "No Updates", Snackbar.LENGTH_SHORT).show();
                                            System.out.println("Snack");
                                        }
                                    }
                                }
        );


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                data();
                adapter.notifyDataSetChanged();
                handler.postDelayed(this, 60 * 1000);
            }
        }, 60 * 1000);
        return rootView;
    }

    public void onRefresh() {
        pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        int x =pref.getInt("check", 0);
        data();
        adapter.notifyDataSetChanged();
        int a=x-check_update;
        if(a!=0){
            Snackbar.make(coordinatorLayout,"Updated List: "+a,Snackbar.LENGTH_SHORT).show();
        }
        else
            Snackbar.make(coordinatorLayout,"No Updates",Snackbar.LENGTH_SHORT).show();
    }


    public void data() {
        try {
            swipeRefreshLayout.setRefreshing(true);
            final String authtoken = pref.getString("token", null);
            final NetworkResponse networkResponse = null;
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                    URL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                if(networkResponse.statusCode==200) {
                                    myDataset.clear();
                                    StoreType.clear();
                                    imageUrl.clear();
                                    addString.clear();
                                    lead_id.clear();
                                    date.clear();
                                    time.clear();
                                    ago.clear();
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject object = response.getJSONObject(i);
                                        String syncresponse = object.getString("user");
                                        JSONObject object2 = new JSONObject(syncresponse);
                                        myDataset.add(object2.getString("name"));
                                        String storeid = object.getString("store_category");
                                        JSONObject storeobject = new JSONObject(storeid);
                                        StoreType.add(storeobject.getString("category"));
                                        String a1 = object.getString("attachments");
                                        JSONArray a1obj = new JSONArray(a1);
                                        for (int j = 0; j < a1obj.length(); j++) {
                                            JSONObject a2obj = a1obj.getJSONObject(j);
                                            String a2 = a2obj.getString("attachment");
                                            JSONObject a3 = new JSONObject(a2);
                                            String a4 = a3.getString("attachment");
                                            JSONObject a4obj = new JSONObject(a4);
                                            images.add(a4obj.getString("url"));
                                        }
                                        imageUrl.add(images.toString());
                                        System.out.println(imageUrl.toString());
                                        addString.add(object.getString("address"));
                                        lead_id.add(object.getString("id"));
                                        date.add("Booking date: " + object.getString("date"));
                                        time.add("Booking time: " + object.getString("time"));
                                        ago.add(object.getString("created_at") + " ago");
                                    }
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putInt("check", response.length());
                                    editor.apply();
                                    adapter.notifyDataSetChanged();
                                    swipeRefreshLayout.setRefreshing(false);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(getContext(), "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", authtoken);
                    return headers;
                }

                @Override
                public Request.Priority getPriority() {
                    return Request.Priority.IMMEDIATE;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonArrayRequest);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
