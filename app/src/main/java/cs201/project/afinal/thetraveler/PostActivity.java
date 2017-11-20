package cs201.project.afinal.thetraveler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("PostActivity", "made");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        final String uniqueID = UUID.randomUUID().toString();

        queue = Volley.newRequestQueue(this);
        User user = SignupActivity.user;
        final ArrayList<String> placeIds = new ArrayList<String>();

        //Place place = (Place) getIntent().getExtras().get("Title");
        final Bundle extras = getIntent().getExtras();
        final String place = extras.getString("Title");
        placeUSC = "";
        idUSC = "";



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


        PICK_IMAGE_REQUEST = 1;
        Button Upload = (Button)findViewById(R.id.uploadButton);
        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                
                final ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.show();

                //converting image to base64 string
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                String URL = "http://10.0.2.2:8080/csci201-fp-server/rest/file/image/upload/post/" + uniqueID + "/index/1";
                //sending image to server
                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        if(s.equals("true")){
                            Toast.makeText(PostActivity.this, "Uploaded Successful", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(PostActivity.this, "Some error occurred!", Toast.LENGTH_LONG).show();
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(PostActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
                    }
                }) {
                    //adding parameters to send
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("image", imageString);
                        return parameters;
                    }
                };

               // RequestQueue rQueue = Volley.newRequestQueue(PostActivity.this);
                queue.add(request);
            }
        });


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


    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //onActivityResult(requestCode, resultCode, data);
//        Log.e("Picture", "entered hello!r");
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Log.e("Picture", "entered onActivityResult");
//            Uri filePath = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                Bitmap lastBitmap = null;
//                lastBitmap = bitmap;
//                //encoding image to string
//                String image = getStringImage(lastBitmap);
//                Log.d("image",image);
//                //passing the image to volley
//                SendImage(image);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public String getStringImage(Bitmap bmp) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageBytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        return encodedImage;
//
//    }
//
//    private void SendImage( final String image) {
//        String url = "http://10.0.2.2:8080/csci201-fp-server/rest/file/image/upload/post/" + uniqueID +"/index/1";
//        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("uploaded", response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //Toast.makeText(Edit_Profile.this, "No internet connection", Toast.LENGTH_LONG).show();
//
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new Hashtable<String, String>();
//
//                params.put("image", image);
//                return params;
//            }
//        };
//        queue.add(stringRequest);
//
//    }
//
//});

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ImageView image = (ImageView) findViewById(R.id.uploadedImage);
                //Setting image to ImageView
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }







        }
