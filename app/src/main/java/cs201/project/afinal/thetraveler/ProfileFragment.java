package cs201.project.afinal.thetraveler;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import cs201.project.afinal.thetraveler.model.Post;


public class ProfileFragment extends Fragment {

    private ListView mProfileList;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        View rootView =  inflater.inflate(R.layout.fragment_profile, container, false);

        mProfileList = (ListView) rootView.findViewById(R.id.profile_list);

        //dummy list
        ArrayList<Post> dummyList = new ArrayList<Post>();
        for(int i = 0; i < 10; i++) {
            Post post = new Post();
            dummyList.add(post);
        }

        ProfileAdapter adapter = new ProfileAdapter(getActivity(), dummyList, 2);

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

            //get references to widgets and set them to post's data



            return listViewItem;
        }
    }


}
