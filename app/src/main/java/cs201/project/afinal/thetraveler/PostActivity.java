package cs201.project.afinal.thetraveler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

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
    private int PICK_IMAGE_REQUEST;
    private String placeUSC;
    private String idUSC;
    private RequestQueue queue;
    private Bitmap bitmap;

    String uniqueID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("PostActivity", "made");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        uniqueID = UUID.randomUUID().toString();

        queue = Volley.newRequestQueue(this);
        User user = SignupActivity.user;
        final ArrayList<String> placeIds = new ArrayList<String>();

        //Place place = (Place) getIntent().getExtras().get("Title");
        final Bundle extras = getIntent().getExtras();
        final String place = extras.getString("Title");
        placeUSC = "";
        idUSC = "";

        String mImageURLString ="http://10.0.2.2:8080/csci201-fp-server/rest/file/image/download/user/" + user.getId();
//        // Initialize a new ImageRequest
        final ImageView mImageView = (ImageView) findViewById(R.id.post_profile_picture);

        ImageRequest imageRequest = new ImageRequest(
                mImageURLString, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response

                        ImageView mImageView = (ImageView) findViewById(R.id.post_profile_picture);
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

        queue.add(imageRequest);


        if(place.equals("USC")){
            Log.e("POSTACTIVITY", "choose location");
                final String placeNames[] = new String[10];
                Log.e("POSTACTIVITY", "reached after placeNames array");
                String getPlaces = "http://10.0.2.2:8080/csci201-fp-server/rest/map/inArea/latBetween/34/and/35/lonBetween/-119/and/-118";
            JsonArrayRequest request = new JsonArrayRequest(getPlaces,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.e("POSTACTIVITY", "enteredonResponse()");
                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            Log.e("POSTACTIVITY", "getting" + i);
                                            JSONObject placeObj = (JSONObject) response.get(i);
                                            placeNames[i] = placeObj.getString("name");
                                            placeIds.add(placeObj.getString("id"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.e("POSTACTIVITY", "entered error for request");
                                        }
                                    }
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            Log.e("PostActivity", "here");
            queue.add(request);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose a location:");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
            for(int i = 0; i < 10; i++)
            {

                arrayAdapter.add(placeNames[i]);
                Log.e("POSTACTVITY", placeNames[i]);
            }

            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPostUserLocationTextView = (TextView)findViewById(R.id.post_location_text_view);
                    mPostUserLocationTextView.setText(placeNames[which]);
                    placeUSC = placeNames[which];
                    idUSC = placeIds.get(which);
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        else
        {
            mPostUserLocationTextView = (TextView)findViewById(R.id.post_location_text_view);
            mPostUserLocationTextView.setText(place);

        }

        //mPostUserLocationTextView = (TextView)findViewById(R.id.post_location_text_view);
        mPostUserNameTextView = (TextView) findViewById(R.id.post_user_name_text_view);
        //mPostUserLocationTextView.setText(place);
        mPostUserNameTextView.setText(user.getName());
        mPostPrivacyRadioGroup = (RadioGroup) findViewById(R.id.post_privacy_radio_group);
        mPostPrivacyRadioGroup.check(0);

        final String userId = user.getId();
        final String name = user.getName();


        mPostButton = (Button) findViewById(R.id.post_button);
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPostTextAreaEditText = (EditText) findViewById(R.id.textArea_information);
                String postContent = mPostTextAreaEditText.getText().toString();
                long currentTime = System.currentTimeMillis();

                Post p;

                if(place.equals("USC"))
                {
                    p = new Post(uniqueID, currentTime, userId, name, idUSC ,placeUSC, postContent, 0, true);


                }
                else{
                    String placeId = extras.getString("placeId");
                    Log.e("POSTACTIVITY", placeId);
                     p = new Post(uniqueID, currentTime, userId, name, placeId, place, postContent, 0, true);

                }

                String url =  "http://10.0.2.2:8080/csci201-fp-server/rest/post/";


                try {
                    final JSONObject jsonBody = new JSONObject();

                    jsonBody.put("id", p.getId());
                    JSONObject timestamp = new JSONObject();
                    timestamp.put("$date", p.getTimestamp());
                    jsonBody.put("timestamp", timestamp);
                    jsonBody.put("placeId", p.getPlaceId());
                    jsonBody.put("userId", p.getUserId());
                    jsonBody.put("postContent", p.getPostContent());
                    jsonBody.put("numImages", p.getNumImages());
                    jsonBody.put("isPublic", p.isPublic());
                    final String requestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);
                            PostActivity.super.onBackPressed();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                        }
                    }) {

                        @Override
                        public byte[] getBody() throws AuthFailureError {

                                //return requestBody == null ? null : requestBody.getBytes("utf-8");
                                return requestBody.getBytes();

                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json";
                        }

                    };

                    queue.add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });

        Button Upload = (Button)findViewById(R.id.uploadButton);
        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if everything is ok we will open image chooser
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }

        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                ImageView imageView = (ImageView) findViewById(R.id.uploadedImage);
                //displaying selected image to imageview
                imageView.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image
                uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ImageView imageView = (ImageView) findViewById(R.id.uploadedImage);
        //displaying selected image to imageview
//        imageView.setImageDrawable(getResources(R.drawable.leavey);
        //imageView.setImageResource(getResources().getIdentifier("res:drawable/leavey.jpg", null, null));
        imageView.setImageResource(R.drawable.leavey);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) imageView.getLayoutParams();
        params.width = 100;
        params.height = 100;
        // existing height is ok as is, no need to edit it
        imageView.setLayoutParams(params);

        Drawable drawable = getResources().getDrawable(R.drawable.leavey);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        uploadBitmap(bitmap);
    }

    private void uploadBitmap(final Bitmap bitmap) {

        //getting the tag from the edittext

        String url = "http://10.0.2.2:8080/csci201-fp-server/rest/file/image/upload/post/" + uniqueID + "/index/1";
        //our custom volley request
       StringRequest volleyMultipartRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                            Log.e("PostActivity", response);
                            //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
            * If you want to add more parameters with the image
            * you can do it here
            * here we have only one parameter with the image
            * which is tags
            * */
            @Override
            public byte[] getBody() throws AuthFailureError {

                //return requestBody == null ? null : requestBody.getBytes("utf-8");
               // return bitmap.toString().getBytes();
                return bitmap.toString().getBytes();

            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }

            /*
            * Here we are passing image by renaming it with a unique name
            * */
//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                long imagename = System.currentTimeMillis();
//                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
//                return params;
//            }
        };

        //adding the request to volley
       queue.add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }



}
