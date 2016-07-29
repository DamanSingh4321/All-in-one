package com.example.dell.ujstore;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 17-Jun-16.
 */
public class TwoFragment extends Fragment{
    private ArrayList<String> myDataset = new ArrayList<String>();
    private ArrayList<String> StoreType = new ArrayList<String>();
    private ArrayList<String> imageUrl = new ArrayList<String>();
    private ArrayList<String> addString = new ArrayList<String>();
    private ArrayList<String> images = new ArrayList<String>();
    private ArrayList<String> lead_id = new ArrayList<String>();
    private ArrayList<String> date = new ArrayList<String>();
    private ArrayList<String> time = new ArrayList<String>();
    private ArrayList<String> ago = new ArrayList<String>();
    private TwoAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    CoordinatorLayout coordinatorLayout;
    private String URL = "https://ujapi.herokuapp.com/api/v1/s/respond_bookings";
    SharedPreferences pref;
    private String Etag = "null" ;
    private  RequestQueue requestQueue;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_two, container, false);
        coordinatorLayout = (CoordinatorLayout)rootView.findViewById(R.id.coordinator_layout2);
        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Snackbar.make(coordinatorLayout, "Internet Connected", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(coordinatorLayout, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.my_recycler_view2);
        rv.setHasFixedSize(true);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout2);
        System.out.println("Mei");
        adapter = new TwoAdapter(getContext(), myDataset, StoreType, addString, date, time, ago, imageUrl);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_orange_light,
                android.R.color.holo_orange_dark);
        data(getContext());
        getetag(getContext());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        data(getContext());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkdata(getContext());
                handler.postDelayed(this, 10 * 1000);
            }
        }, 10 * 1000);
        return rootView;
    }

    public void data(Context context) {
        try {
            swipeRefreshLayout.setRefreshing(true);
            pref = context.getSharedPreferences("MyPref", 0);
            final String authtoken = pref.getString("token", null);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                    URL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                myDataset.clear();
                                StoreType.clear();
                                imageUrl.clear();
                                addString.clear();
                                lead_id.clear();
                                date.clear();
                                time.clear();
                                ago.clear();
                                for (int i = 0; i < response.length(); i++) {
                                    images.clear();
                                    JSONObject object = response.getJSONObject(i);
                                    String booking = object.getString("booking");
                                    JSONObject object2 = new JSONObject(booking);
                                    myDataset.add(object.getString("user_name"));
                                    String storeid = object.getString("store_category");
                                    JSONObject storeobject = new JSONObject(storeid);
                                    StoreType.add(storeobject.getString("category"));
                                    addString.add(object2.getString("address"));
                                    lead_id.add(object2.getString("id"));
                                    date.add("Booking date: " + object2.getString("date"));
                                    time.add("Booking time: " + object2.getString("time"));
                                    ago.add(object2.getString("created_at") + " ago");
                                    String a1 = object.getString("attach_list");
                                    JSONArray a1obj = new JSONArray(a1);
                                    for (int j = 0; j < a1obj.length(); j++) {
                                        JSONObject a2obj = a1obj.getJSONObject(j);
                                        String a2 = a2obj.getString("attachment");
                                        JSONObject a3 = new JSONObject(a2);
                                        images.add(a3.getString("url"));
                                        System.out.println("images:" + images.toString());
                                    }
                                    imageUrl.add(images.toString());
                                }
                                adapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NoConnectionError) {
                        Snackbar.make(coordinatorLayout, "No internet Access, Check your internet connection.",Snackbar.LENGTH_LONG).show();
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
            requestQueue.add(jsonArrayRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkdata(Context context) {
        try {
            //  pref = context.getSharedPreferences("MyPref",0);
            final String authtoken = pref.getString("token", null);
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    int mStatusCode = response.statusCode;
                    if (mStatusCode == 200) {
                        Snackbar.make(coordinatorLayout, "Refresh needed", Snackbar.LENGTH_LONG).show();
                    }
                    return super.parseNetworkResponse(response);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", authtoken);
                    headers.put("If-None-Match", Etag);
                    System.out.println(Etag);
                    return headers;
                }

            };
            requestQueue.add(strReq);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getetag(Context context) {
        try {
            pref = context.getSharedPreferences("MyPref",0);
            final String authtoken = pref.getString("token", null);
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    Etag = response.headers.get("Etag");
                    System.out.println(Etag);
                    return super.parseNetworkResponse(response);
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", authtoken);
                    return headers;
                }

            };
            requestQueue.add(strReq);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}