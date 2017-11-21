package cs201.project.afinal.thetraveler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
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
                                post.setId(jsonPost.getString("id"));
                                post.setUserId(jsonPost.getString("userId"));
                                post.setNumImages(jsonPost.getInt("numImages"));
                                if(post.getId().equals("1DC62916-0D17-42BC-9848-BC5E43C8CE75")){
                                    post.setNumImages(1);
                                }
                                allPosts.add(post);
                                //Log.e("TIMELINE FRAGMENT", post.toString());
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


            String mImageURLString ="http://10.0.2.2:8080/csci201-fp-server/rest/file/image/download/user/" +  post.getUserId();
//        // Initialize a new ImageRequest
            final ImageView mImageViewprofile = (ImageView) listViewItem.findViewById(R.id.timeline_profile_picture);

            ImageRequest imageRequestprofile = new ImageRequest(
                    mImageURLString, // Image URL
                    new Response.Listener<Bitmap>() { // Bitmap listener
                        @Override
                        public void onResponse(Bitmap response) {
                            // Do something with response
                            mImageViewprofile.setImageBitmap(response);

                        }
                    },
                    0, // Image width
                    0, // Image height
                    ImageView.ScaleType.CENTER_CROP, // Image scale type
                    Bitmap.Config.RGB_565, //Image decode configuration
                    new Response.ErrorListener() { // Error listener
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Do something with error response
                            error.printStackTrace();
                            //Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                        }
                    }
            );


            // Add ImageRequest to the RequestQueue
            queue.add(imageRequestprofile);


            final ImageView image = (ImageView) listViewItem.findViewById(R.id.timeline_post_image);
            Log.e("timelinefragment", post.getPostContent() + post.getNumImages());

            if(post.getNumImages() > 0){
                String mImageURLStrin ="http://10.0.2.2:8080/csci201-fp-server/rest/file/image/download/post/" + post.getId() + "/index/1";
                //Log.e("Profile Fragment", mImageURLString);
//        // Initialize a new ImageRequest
                //final ImageView mImageView = (ImageView) rootView.findViewById(R.id.profile_picture);
                final String id = post.getId();
                ImageRequest imageRequest = new ImageRequest(
                        mImageURLStrin, // Image URL
                        new Response.Listener<Bitmap>() { // Bitmap listener
                            @Override
                            public void onResponse(Bitmap response) {
                                // Do something with response

                                Log.e("timeline fragment", id);
                                image.setImageBitmap(response);

                            }
                        },
                        0, // Image width
                        0, // Image height
                        ImageView.ScaleType.CENTER_CROP, // Image scale type
                        Bitmap.Config.RGB_565, //Image decode configuration
                        new Response.ErrorListener() { // Error listener
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Do something with error response
                                error.printStackTrace();
                                //Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                            }
                        }
                );



                // Add ImageRequest to the RequestQueue
                queue.add(imageRequest);

            }
            else{
               image.setImageDrawable(getResources().getDrawable(R.drawable.leavey));
            }



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


