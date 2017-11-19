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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
        View rootView =  inflater.inflate(R.layout.fragment_profile, container, false);
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
