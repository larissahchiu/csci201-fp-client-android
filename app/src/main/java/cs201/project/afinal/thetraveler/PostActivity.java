package cs201.project.afinal.thetraveler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;


public class PostActivity extends AppCompatActivity {

    //member variables
    private TextView mPostUserNameTextView;
    private ImageView mPostUserProfileImage;
    private RadioGroup mPostPrivacyRadioGroup;
    private TextView mPostUserLocationTextView;
    private EditText mPostTextAreaEditText;
    private ImageView mPostImage;
    private ImageButton mPostAddImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

    }


}
