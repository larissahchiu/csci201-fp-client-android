package cs201.project.afinal.thetraveler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cs201.project.afinal.thetraveler.model.User;


public class HomeActivity extends AppCompatActivity {

    public static final String SIGNUP_KEY = "cs201.project.afinal.thetraveler.signup";

    private int guest;

    //members
    private ImageButton mHomeButton;
    private TextView mHomeTextView;

    private ImageButton mExploreButton;
    private TextView mExploreTextView;

    private ImageButton mPostButton;
    private TextView mPostTextView;

    private ImageButton mTimelineButton;
    private TextView mTimelineTextView;

    private ImageButton mProfileButton;
    private TextView mProfileTextView;

    public User homeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("HOME ACTIVITY", "Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeUser = (User) getIntent().getSerializableExtra("user");
//        Log.e("homeactivity", homeUser.getId());

        //user choice guest or not
        Intent i = getIntent();
        guest = i.getIntExtra(SIGNUP_KEY, -1);


        //show home if app just launched
        FragmentManager fm = getSupportFragmentManager();
        Fragment sampleFragment = fm.findFragmentById(R.id.fragment_container);

        if (sampleFragment == null) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            sampleFragment = HomeFragment.newInstance(this, guest);

            fragmentTransaction.add(R.id.fragment_container, sampleFragment);
            fragmentTransaction.commit();

            mHomeTextView = (TextView) findViewById(R.id.home_text_view);
            mHomeButton = (ImageButton) findViewById(R.id.home_button);
            mHomeTextView.setTextColor(Color.rgb(52, 152, 219));
            mHomeButton.setImageResource(R.drawable.icons8_home_blue);
        }


        mHomeTextView = (TextView) findViewById(R.id.home_text_view);
        mHomeButton = (ImageButton) findViewById(R.id.home_button);
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {

                    //checks for API version
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        int cx = mHomeButton.getWidth() / 2;
                        int cy = mHomeButton.getHeight() / 2;
                        float radius = mHomeButton.getWidth();
                        Animator anim = ViewAnimationUtils.createCircularReveal(mHomeButton,
                                cx, cy, radius, 0);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                            }
                        });
                        anim.start();

                    }

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft;
                    HomeFragment fragment = HomeFragment.newInstance(HomeActivity.this, guest);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("homeUserName", homeUserName);
//                    fragment.setArguments(bundle);
                    ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, fragment);
                    ft.commit();


                    deselectButtons();
                    mHomeTextView.setTextColor(Color.rgb(52, 152, 219));
                    mHomeButton.setImageResource(R.drawable.icons8_home_blue);

                }
            }
        });


        mExploreButton = (ImageButton) findViewById(R.id.explore_button);
        mExploreTextView = (TextView) findViewById(R.id.explore_text_view);
        mExploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft;
                ExploreFragment fragment = ExploreFragment.newInstance();
                ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.commit();

                //checks for API version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    int cx = mExploreButton.getWidth() / 2;
                    int cy = mExploreButton.getHeight() / 2;
                    float radius = mExploreButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mExploreButton,
                            cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                        }
                    });
                    anim.start();

                }

                deselectButtons();
                mExploreTextView.setTextColor(Color.rgb(52, 152, 219));
                mExploreButton.setImageResource(R.drawable.icons8_adventures_blue);
                mExploreTextView.setTextColor(Color.rgb(52, 152, 219));

            }
        });


        mPostButton = (ImageButton) findViewById(R.id.post_button);
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, PostActivity.class);
//                intent.putExtra("Title", "USC");
//                startActivity(intent);
                Toast.makeText(getBaseContext(), "Please go to explore page", Toast.LENGTH_SHORT).show();

            }
        });

        mTimelineTextView = (TextView) findViewById(R.id.timeline_text_view);
        mTimelineButton = (ImageButton) findViewById(R.id.timeline_button);
        mTimelineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft;
                TimelineFragment fragment = TimelineFragment.newInstance();
                ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.commit();

                //checks for API version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    int cx = mTimelineButton.getWidth() / 2;
                    int cy = mTimelineButton.getHeight() / 2;
                    float radius = mTimelineButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mTimelineButton,
                            cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                        }
                    });
                    anim.start();

                }

                deselectButtons();
                mTimelineTextView.setTextColor(Color.rgb(52, 152, 219));
                mTimelineButton.setImageResource(R.drawable.icons8_list_blue);
                mTimelineTextView.setTextColor(Color.rgb(52, 152, 219));
            }
        });

        mProfileTextView = (TextView) findViewById(R.id.profile_text_view);
        mProfileButton = (ImageButton) findViewById(R.id.profile_button);
        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft;
                ProfileFragment fragment = ProfileFragment.newInstance(HomeActivity.this);
                ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.commit();

                //checks for API version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    int cx = mProfileButton.getWidth() / 2;
                    int cy = mProfileButton.getHeight() / 2;
                    float radius = mProfileButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mProfileButton,
                            cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                        }
                    });
                    anim.start();

                }

                deselectButtons();
                mProfileTextView.setTextColor(Color.rgb(52, 152, 219));
                mProfileButton.setImageResource(R.drawable.icons8_male_user_blue);
                mProfileTextView.setTextColor(Color.rgb(52, 152, 219));
            }
        });


        if(guest == 2) {
            Toast.makeText(this, "Sign-up to unlock all of The Traveler's features!", Toast.LENGTH_SHORT).show();
            guestUser();
        }
    }

    public void deselectButtons() {
        mHomeButton.setImageResource(R.drawable.icons8_home);
        mHomeTextView.setTextColor(Color.rgb(128, 128, 128));

        mExploreButton.setImageResource(R.drawable.icons8_adventures);
        mExploreTextView.setTextColor(Color.rgb(128, 128, 128));

        mTimelineButton.setImageResource(R.drawable.icons8_list);
        mTimelineTextView.setTextColor(Color.rgb(128, 128, 128));

        mProfileButton.setImageResource(R.drawable.icons8_male_user);
        mProfileTextView.setTextColor(Color.rgb(128, 128, 128));



    }

    public void guestUser() {
        mExploreButton.setEnabled(false);
        mProfileButton.setEnabled(false);
        mPostButton.setEnabled(false);
    }

//


}
