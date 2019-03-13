package com.example.vaibh.mosaic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ProfileActivity extends AppCompatActivity {

    public static final String TAG = ProfileActivity.class.getName();
    Button location1;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    ProgressBar progressBar;
    RecyclerView recyclerView;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    // TextView locationText;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        /*List<RestaurantName> sampleuser = new ArrayList<>();

        for (int i=0; i<name.length; i++){
            RestaurantName user = new RestaurantName();

            user.username = name[i];
            user.userdesc = desc[i];
            user.userimage = image[i];

            sampleuser.add(user);
        }*/

        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LinearLayoutManager);

        recyclerView.setHasFixedSize(true);
        //recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(),sampleuser,jsonArrayRestaurantDetails));

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        location1 = (Button) findViewById(R.id.location1);


        // locationText = (TextView) findViewById(R.id.locationText);
        location1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);

                locationTrack = new LocationTrack(ProfileActivity.this);

                if (locationTrack.canGetLocation()) {


                    double longitude = locationTrack.getLongitude();
                    double latitude = locationTrack.getLatitude();

                    Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                    Log.d("Location ", "longitude : " + Double.toString(longitude) + " latitude " + Double.toString(latitude));
//                    getRestaurantList();


                    try {
                        JSONObject jsonObject = new JSONObject();
                        //  jsonObject.put("id", txtDate.getText().toString().trim());
                        jsonObject.put("longitude", longitude);
                        jsonObject.put("latitude", latitude);

                        //jsonObject.put("time", txtTime.getText().toString().trim());

//                   setUpCustomerLoginAPICall(jsonObject);
                        getRestaurantList(jsonObject);
                    } catch (Exception exp) {
                        Log.i(TAG, "ERR > " + Log.getStackTraceString(exp));
                    }

                } else {

                    locationTrack.showSettingsAlert();

                }

            }
        });
    }
    private void setUpCustomerLoginAPICall(JSONObject jsonObject) {
        try {

            Log.i(TAG, "Login PARAM : " + jsonObject.toString());

            ByteArrayEntity entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            AsyncHttpClient client = new AsyncHttpClient();

            client.post(getApplicationContext(), "http://192.168.0.101:8000/restaurantnearby/", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    Log.i(TAG, "onStart" + ", " + this.getRequestURI());

                }



                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                }

                @Override
                public void onRetry(int retryNo) {
                    Log.i(TAG, "onRetry");
                }
            });
        } catch (final Exception exp) {
            Log.i(TAG, "ERROR -> setUpCustomerLoginAPICall() : " + Log.getStackTraceString(exp));
        }
    }




   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            new UserSession(ProfileActivity.this).removeUser();

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.package.ACTION_LOGOUT");
            sendBroadcast(broadcastIntent);

            Toast.makeText(this, "You are successfully logged out", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
            return true;

        }
        return true;
    }*/

    public void getRestaurantList(JSONObject jsonObject){
        try {
            Log.i(TAG,"GetRestaurant List PARAM :: "+jsonObject.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.setConnectTimeout(10*1000);

            ByteArrayEntity entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(getApplicationContext(),"http://192.168.0.101:8000/restaurantnearby/",entity, "application/json",new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    Log.i(TAG,"API URL >>>>" + this.getRequestURI().toString());

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // called when response HTTP status is "200 OK"

                    try {
                        progressBar.setVisibility(View.GONE);
                        String res=new String(response, "UTF-8");
                        Log.i(TAG,"onSuccess >>>>" + res);
                        if(TextUtils.isEmpty(res)){
                            Toast.makeText(ProfileActivity.this, "Response not getting properly", Toast.LENGTH_SHORT).show();
                        }else{
                            JSONArray jsonArray=new JSONArray(res);

                            List<RestaurantName> sampleuser = new ArrayList<>();

                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject jRes=jsonArray.getJSONObject(i);

                                RestaurantName user = new RestaurantName();

                                user.setRestname(jRes.getString("restaurant_name"));
                                // user.setUserdesc(jRes.getString("cuisines"));
                                user.setUserimage(R.drawable.hotel1);
                                user.setNumid(jRes.getInt("restaurant_id"));
                                user.setOwner(jRes.getString("owner"));
                                user.setNumber(jRes.getString("mobile"));
                                //user.setCity(jRes.getString("city"));
                                user.setAddress(jRes.getString("address"));
                                user.setDistance(jRes.getDouble("distance"));
                                // user.setLocality(jRes.getString("locality"));
                                // user.setLongitude(jRes.getString("longitude"));
                                // user.setLatitude(jRes.getString("latitude"));



                                sampleuser.add(user);
                            }

                            recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(),sampleuser));


                            Log.i(TAG, "Response : "+res);
                        }
                    } catch (Exception ep) {
                        ep.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        String res=new String(errorResponse, "UTF-8");
                        Log.i(TAG,"onFailure >>>>" + res);
                        if(TextUtils.isEmpty(res)){
                            Toast.makeText(ProfileActivity.this, "Response not getting properly", Toast.LENGTH_SHORT).show();
                        }else{
                            JSONArray jsonArray=new JSONArray(res);

                            List<RestaurantName> sampleuser = new ArrayList<>();

                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject jRes=jsonArray.getJSONObject(i);

                                RestaurantName user = new RestaurantName();

                                user.setRestname(jRes.getString("restaurant_name"));
                                // user.setUserdesc(jRes.getString("cuisines"));
                                user.setUserimage(R.drawable.hotel1);
                                user.setNumid(jRes.getInt("restaurant_id"));
                                user.setOwner(jRes.getString("owner"));
                                user.setNumber(jRes.getString("mobile"));
                                //user.setCity(jRes.getString("city"));
                                user.setAddress(jRes.getString("address"));
                                user.setDistance(jRes.getDouble("distance"));
                                // user.setLocality(jRes.getString("locality"));
                                // user.setLongitude(jRes.getString("longitude"));
                                // user.setLatitude(jRes.getString("latitude"));



                                sampleuser.add(user);
                            }

                            recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(),sampleuser));


                            Log.i(TAG, "Response : "+res);
                        }
                    } catch (Exception ep) {
                        ep.printStackTrace();
                    }

                }
                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });
        }catch (Exception e){
            Log.i(TAG, "ERR : "+Log.getStackTraceString(e));
        }
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                    }
                                }
                            });
                            return;
                        }
                    }

                }
                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ProfileActivity.this).setMessage(message).setPositiveButton("OK", okListener).setNegativeButton("Cancel", null).create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationTrack!=null)
            locationTrack.stopListener();
    }
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}





