package com.example.vaibh.mosaic;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class DisplayRestaurants extends AppCompatActivity {

    public static final String TAG=DisplayRestaurants.class.getName();

    private RestaurantName restaurantName;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_restaurants);

        recyclerView = (RecyclerView) findViewById(R.id.rv_business_opportunity);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LinearLayoutManager);

        recyclerView.setHasFixedSize(true);

        try {

            if(getIntent()!=null && getIntent().hasExtra("RESTAURANT")){
                restaurantName=(RestaurantName)getIntent().getSerializableExtra("RESTAURANT");
                Log.i(TAG,"<<<<<<<<<< RES : "+restaurantName.getRestname());

            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("restaurant_id", restaurantName.getNumid());


            setUpCustomerLoginAPICall(jsonObject);
        } catch (Exception exp) {
            Log.i(TAG, "ERR > " + Log.getStackTraceString(exp));
        }
    }



    private void setUpCustomerLoginAPICall(JSONObject jsonObject) {
        try {

            Log.i(TAG, "Login PARAM : " + jsonObject.toString());

            ByteArrayEntity entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            AsyncHttpClient client = new AsyncHttpClient();

            client.post(getApplicationContext(), "http://192.168.0.101:8000/view_business_opportunity/", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    Log.i(TAG, "onStart" + ", " + this.getRequestURI());

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String res=new String(responseBody, "UTF-8");
                        Log.i(TAG,"onSuccess >>>>" + res);
                        if(TextUtils.isEmpty(res)){
                            Toast.makeText(DisplayRestaurants.this, "Response not getting properly", Toast.LENGTH_SHORT).show();
                        }else{
                            JSONArray jsonArray=new JSONArray(res);

                            List<BussinessOpportunity> sampleuser = new ArrayList<>();

                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject jRes=jsonArray.getJSONObject(i);

                                BussinessOpportunity user = new BussinessOpportunity();

                                user.setBusinessOpportunityId(jRes.getInt("business_opportunity_id"));
                                user.setRestaurantId(jRes.getInt("restaurant_id"));
                                user.setBusinessRequirement(jRes.getString("business_requirement"));
                                user.setDescription(jRes.getString("description"));

                                sampleuser.add(user);
                                UserSession userSession = new UserSession(DisplayRestaurants.this);
                                userSession.setRestId(jRes.getInt("restaurant_id"));
                            }

                            recyclerView.setAdapter(new BusinessOpertunityAdapter(DisplayRestaurants.this,sampleuser,restaurantName));


                            Log.i(TAG, "Response : "+res);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    try {
                        String res=new String(errorResponse, "UTF-8");
                        Log.i(TAG,"onFailure >>>>" + res);
                        if(TextUtils.isEmpty(res)){
                            Toast.makeText(DisplayRestaurants.this, "Response not getting properly", Toast.LENGTH_SHORT).show();
                        }else{



                            Log.i(TAG, "Response : "+res);
                        }
                    } catch (Exception ep) {
                        ep.printStackTrace();
                    }

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

}

