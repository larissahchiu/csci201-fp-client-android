package cs201.project.afinal.thetraveler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import cs201.project.afinal.thetraveler.model.Post;


public class ProfileFragment extends Fragment {

    private ListView mProfileList;
    private HomeActivity homeActivity;
    private ArrayList<Post> allPosts;
    private ProfileAdapter adapter;
    private RequestQueue queue;


    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(HomeActivity homeActivity) {

        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.homeActivity = homeActivity;
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

        queue = Volley.newRequestQueue(getActivity());
        allPosts = new ArrayList<Post>();

        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_profile, container, false);
        TextView nameView = (TextView) rootView.findViewById(R.id.profile_user_name);
        nameView.setText(homeActivity.homeUser.getName());

        TextView levelView = (TextView) rootView.findViewById(R.id.profile_level);
        levelView.setText("Level " + Integer.toString(homeActivity.homeUser.getScore() / 10 + 1) );

        Log.e("Profile Fragment", "name and things");

        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.profile_level_progress);
        progressBar.setProgress((homeActivity.homeUser.getScore() % 10) * 10);

        Log.e("Profile Fragment", "progress bar");

        TextView visitedView = (TextView) rootView.findViewById(R.id.profile_places_visited);
        String requestURL = "http://10.0.2.2:8080/csci201-fp-server/rest/timeline/user/"+ homeActivity.homeUser.getId() +
                "/maxLength/1000";
        Log.e("Profile Fragment", requestURL);

        String requestUrl = "http://10.0.2.2:8080/csci201-fp-server/rest/user/rank/id/" + homeActivity.homeUser.getId();

        Log.e("HELLO", requestUrl);
        StringRequest request2 = new StringRequest(Request.Method.GET, requestUrl
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Home Fragment", response);

                TextView rankTextView = (TextView) rootView.findViewById(R.id.profile_rank);
                rankTextView.setText(response);

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(request2);

         String mImageURLString ="http://10.0.2.2:8080/csci201-fp-server/rest/file/image/download/user/" +  homeActivity.homeUser.getId();
//        // Initialize a new ImageRequest
        final ImageView mImageView = (ImageView) rootView.findViewById(R.id.profile_picture);

        ImageRequest imageRequest = new ImageRequest(
                mImageURLString, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response

                        ImageView mImageView = (ImageView) rootView.findViewById(R.id.profile_picture);

                        mImageView.setImageBitmap(response);

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

        getNumberOfPlaces(rootView, requestURL);

        TextView rankView = (TextView) rootView.findViewById(R.id.profile_rank);

        mProfileList = (ListView) rootView.findViewById(R.id.profile_list);

        adapter = new ProfileAdapter(getActivity(), allPosts, 2);

        mProfileList.setAdapter(adapter);


        return rootView;
    }

    private class ProfileAdapter extends ArrayAdapter<Post> {

        private int resourceId;

        //called using array list gotten from server
        public ProfileAdapter(Activity context, ArrayList<Post> insult, int id) {
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
                listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.profile_list_item,
                        parent, false);
            }

            //get current in arraylist
            Post post = getItem(position);

            final ImageView image = (ImageView) listViewItem.findViewById(R.id.timeline_post_image);
            if(post.getNumImages() > 0){

                String mImageURLString ="http://10.0.2.2:8080/csci201-fp-server/rest/file/image/download/post/" + post.getId() + "/index/1";
                Log.e("Profile Fragment", mImageURLString);
//        // Initialize a new ImageRequest
                //final ImageView mImageView = (ImageView) rootView.findViewById(R.id.profile_picture);

                ImageRequest imageRequest = new ImageRequest(
                        mImageURLString, // Image URL
                        new Response.Listener<Bitmap>() { // Bitmap listener
                            @Override
                            public void onResponse(Bitmap response) {
                                // Do something with response

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

            TextView caption = (TextView) listViewItem.findViewById(R.id.profile_post_caption);
            TextView placeName = (TextView) listViewItem.findViewById(R.id.profile_location_text_view);
            caption.setText(post.getPostContent());
            placeName.setText(post.getPlaceName());



            return listViewItem;
        }
    }


    private int getNumberOfPlaces(final View root, String reqURL){
        JsonArrayRequest request = new JsonArrayRequest(reqURL,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
//                            JSONArray posts = (JSONArray)response;
                            Log.e("PROFILE FRAGMENT", response.length() + "");
                            TextView visitedView = (TextView) root.findViewById(R.id.profile_places_visited);
                            visitedView.setText(Integer.toString(response.length()));
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
                                post.setNumImages(jsonPost.getInt("numImages"));
                                if(post.getId().equals("1DC62916-0D17-42BC-9848-BC5E43C8CE75")){
                                    post.setNumImages(1);
                                }

                                allPosts.add(post);
                                Log.e("PROFILE FRAGMENT", post.toString());
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

        return allPosts.size();
    }


}
