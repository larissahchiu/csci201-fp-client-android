package cs201.project.afinal.thetraveler;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cs201.project.afinal.thetraveler.model.Post;


public class TimelineFragment extends Fragment {

    private ListView mTimelineList;
    private ArrayList<Post> allPosts;
    private TimelineAdapter adapter;
    private RequestQueue queue;


    public TimelineFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    //set args of Fragment in onCreate
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
        View rootView =  inflater.inflate(R.layout.fragment_timeline, container, false);

        mTimelineList = (ListView) rootView.findViewById(R.id.timeline_list_view);

        //dummy list
//        ArrayList<Post> dummyList = new ArrayList<Post>();
//        for(int i = 0; i < 10; i++) {
//            Post post = new Post();
//            dummyList.add(post);
//        }

        allPosts = new ArrayList<Post>();

        //TODO set up Volley queue
        queue = Volley.newRequestQueue(getActivity());
        String requestUrl = "http://10.0.2.2:8080/csci201-fp-server/rest/timeline/everything";
        String URL = "https://restcountries.eu/rest/v2/name/es";
        requestJSONParse(requestUrl);

        //instance of adapter
        adapter = new TimelineAdapter(getActivity(), allPosts, 1);

        //give adapter to list
        mTimelineList.setAdapter(adapter);

        return rootView;

    }

    //TODO This will contact the Volley request for JSON data
    public void requestJSONParse(String reqURL) {
        //data comes back as an array
        JsonArrayRequest request = new JsonArrayRequest(reqURL,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
//                            JSONArray posts = (JSONArray)response;
                            Log.e("TIMELINE FRAGMENT", response.length() + "");
                            for(int i = 0; i < response.length(); i++)
                            {
                                Post post = new Post();
                                JSONObject jsonPost  = (JSONObject)response.get(i);
                                String postContent = jsonPost.getString("postContent");
                                String userName = jsonPost.getString("userName");
                                String placeName = jsonPost.getString("placeName");
                                post.setUserName(userName);
                                post.setPlaceName(placeName);
                                post.setPostContent(postContent);
                                allPosts.add(post);
                                Log.e("TIMELINE FRAGMENT", post.toString());
                            }

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TIMELINE FRAGMENT", "ERROR");
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        if(request == null)
            Log.e("TIMELINE FRAGMENT", "NULL REQUEST");
        queue.add(request);
    }

    private class TimelineAdapter extends ArrayAdapter<Post> {

        private int resourceId;


        //called using array list gotten from server
        public TimelineAdapter(Activity context, ArrayList<Post> insult, int id) {
            super(context, 0, insult);
            resourceId = id;
        }



        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            //get view for item
            View listViewItem = convertView;


            //if it's not being reused, inflate it
            if(convertView == null) {
                listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.timeline_list_item,
                        parent, false);
            }


            //get current in arraylist
            Post post = getItem(position);
            //Log.d("content", post.getPostContent());
//            post.setPostContent(allPosts.get(0).getPostContent());
//            //get references to widgets and set them to post's data
           TextView timelineCaption = (TextView) listViewItem.findViewById(R.id.timeline_post_caption);
           TextView userName = (TextView) listViewItem.findViewById(R.id.timeline_user_name_text_view);
            TextView placeName = (TextView) listViewItem.findViewById(R.id.timeline_location_text_view);
            timelineCaption.setText(post.getPostContent());
            userName.setText(post.getUserName());
            placeName.setText(post.getPlaceName());


            return listViewItem;
        }
    }


}


