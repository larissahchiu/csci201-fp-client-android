package cs201.project.afinal.thetraveler;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cs201.project.afinal.thetraveler.model.Post;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v("HOME FRAGMENT", "SUCCESS");
        View fragmentLayout = inflater.inflate(R.layout.fragment_home, container, false);
        Mapbox.getInstance(super.getContext(), "pk.eyJ1IjoibGFyaXNzYWNoaXUiLCJhIjoiY2o5eGRkbGRuMHZmcTJxcG8wcWtwbnBubyJ9.W6Zsc7_FJa8irGHzbNAaXw");

       final MapView mapView = (MapView) fragmentLayout.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        String requestUrl = "http://10.0.2.2:8080/csci201-fp-server/rest/map/inArea/latBetween/0/and/0/lonBetween/0/and/0";
        //populate map here

        //data comes back as an array
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(final JSONArray response) {

//                            JSONArray posts = (JSONArray)response;
                            Log.e("TIMELINE FRAGMENT", response.length() + "");
                            mapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(MapboxMap mapboxMap) {

                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            JSONObject place = (JSONObject) response.get(i);
                                            int lng = place.getInt("longitude");
                                            int lat = place.getInt("latitude");
                                            String title = place.getString("placeName");
                                            mapboxMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(lat, lng))
                                                    .title(title)

                                            );
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.e("TIMELINE FRAGMENT", "ERROR");
                                        }
                                    }
                                }
                            });
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        // Inflate the layout for this fragment

        return fragmentLayout;
    }

}
