package cs201.project.afinal.thetraveler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button signUpButton = (Button)findViewById(R.id.signUp);
        Button signInButton = (Button) findViewById(R.id.signIn);
        Button guestSignInButton = (Button) findViewById(R.id.guestSignIn);



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText fname = (EditText) findViewById(R.id.fName);
                String firstName = fname.getText().toString();

                EditText lname = (EditText) findViewById(R.id.lName);
                String lastName = lname.getText().toString();

                EditText emailText = (EditText) findViewById(R.id.email);
                String email = emailText.getText().toString();

                EditText passwordText = (EditText) findViewById(R.id.password);
                String password = passwordText.getText().toString();

                Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText fname = (EditText) findViewById(R.id.fName);
                String firstName = fname.getText().toString();

                EditText lname = (EditText) findViewById(R.id.lName);
                String lastName = lname.getText().toString();

                EditText emailText = (EditText) findViewById(R.id.email);
                String email = emailText.getText().toString();

                EditText passwordText = (EditText) findViewById(R.id.password);
                String password = passwordText.getText().toString();

                Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        guestSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }


}
