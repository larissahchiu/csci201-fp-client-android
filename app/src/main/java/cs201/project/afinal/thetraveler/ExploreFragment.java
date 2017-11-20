package cs201.project.afinal.thetraveler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cs201.project.afinal.thetraveler.model.Place;


public class ExploreFragment extends Fragment {
    private RequestQueue queue;

    public ExploreFragment() {
        //empty
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentLayout = inflater.inflate(R.layout.fragment_explore, container, false);
        Mapbox.getInstance(super.getContext(), "pk.eyJ1IjoibGFyaXNzYWNoaXUiLCJhIjoiY2o5eGRkbGRuMHZmcTJxcG8wcWtwbnBubyJ9.W6Zsc7_FJa8irGHzbNAaXw");

        final MapView mapView = (MapView) fragmentLayout.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        String requestUrl = "http://10.0.2.2:8080/csci201-fp-server/rest/map/inArea/latBetween/34/and/35/lonBetween/-119/and/-118";
        //populate map here
        queue = Volley.newRequestQueue(getActivity());
        //data comes back as an array
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(MapboxMap mapboxMap) {

                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject place = (JSONObject) response.get(i);
                                        double lng = place.getDouble("lon");
                                        double lat = place.getDouble("lat");
                                        String title = place.getString("name");
                                        mapboxMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(lat, lng))
                                                .title(title)


                                        );
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(final Marker marker) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                                        alertDialogBuilder
                                                .setMessage("Click yes to add a post for " + marker.getTitle())
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                                {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int id) {
                                                                Intent intent = new Intent(getActivity(), PostActivity.class);
                                                                for(int i = 0; i < response.length(); i++){
                                                                   try{

                                                                       JSONObject place = (JSONObject) response.get(i);
                                                                       if(place.getString("name").equals(marker.getTitle())){
                                                                           Bundle extras = new Bundle();
                                                                           extras.putString("Title",marker.getTitle());
                                                                           extras.putString("placeId", place.getString("id"));
                                                                           Log.e("Explore Fragment", place.getString("id"));
                                                                           intent.putExtras(extras);
                                                                       }


//                                                                       if(marker.getTitle().equals(place.get("name"))){
//                                                                           intent.putExtra("Title", new Place(place.getString("id"),place.getString("name"),
//                                                                                   place.getDouble("lat"), place.getDouble("lon"), place.getInt("points"), place.getInt("numVisits")));
//                                                                       }
                                                                   }catch (JSONException e) {
                                                                       e.printStackTrace();
                                                                   }
                                                                }
                                                                //intent.putExtra("Title", marker.getTitle());
                                                                startActivity(intent);
                                                            }
                                                })
                                                .setNegativeButton(android.R.string.cancel, null);
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                        return true;
                                    }

                                });

                            }
                        });
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(request);

        // Inflate the layout for this fragment

        return fragmentLayout;
    }

    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


}
