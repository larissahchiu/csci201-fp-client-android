package cs201.project.afinal.thetraveler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

import cs201.project.afinal.thetraveler.model.Place;
import cs201.project.afinal.thetraveler.model.Post;
import cs201.project.afinal.thetraveler.model.User;


public class PostActivity extends AppCompatActivity {

    //member variables
    private TextView mPostUserNameTextView;
    private ImageView mPostUserProfileImage;
    private RadioGroup mPostPrivacyRadioGroup;
    private TextView mPostUserLocationTextView;
    private EditText mPostTextAreaEditText;
    private ImageView mPostImage;
    private ImageButton mPostAddImage;
    private Button mPostButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        User user = SignupActivity.user;

        Place place = (Place) getIntent().getExtras().get("Title");
        mPostUserLocationTextView = (TextView)findViewById(R.id.post_location_text_view);
        mPostUserNameTextView = (TextView) findViewById(R.id.post_user_name_text_view);
        mPostUserLocationTextView.setText(place.getName());
        mPostUserNameTextView.setText(user.getName());
        mPostPrivacyRadioGroup = (RadioGroup) findViewById(R.id.post_privacy_radio_group);
        mPostPrivacyRadioGroup.check(0);


        final String email = user.getEmail();
        final String userId = user.getId();
        final String name = user.getName();
       // String password = user.getString("password");
        final String placeId = place.getId();
        final String placeName = place.getName();

//        mPostButton = (Button) findViewById(R.id.post_button);
//        mPostButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                mPostTextAreaEditText = (EditText) findViewById(R.id.textArea_information);
//                String postContent = mPostTextAreaEditText.getText().toString();
//
//                Post p = new Post(100, new Date(), userId, name, placeId, placeName, postContent, 0, isPublic)
//                p.
//            }
//        });






    }


}
