package com.example.vaibh.mosaic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class ViewMeeting extends AppCompatActivity {

    public static final String TAG="ViewMeeting";
    private int restaurantId;
    private int businessOppId;

    ProgressBar progressBar;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meeting);



        recyclerView = (RecyclerView) findViewById(R.id.rv_business_opportunity);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LinearLayoutManager);
        recyclerView.setHasFixedSize(true);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        restaurantId = getIntent().getIntExtra("restaurant_id", -1);
        businessOppId = getIntent().getIntExtra("business_opportunity_id", -1);
        Log.i(TAG, "RES ID : "+restaurantId+", BUSINESS OPP ID : "+businessOppId);



            try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("restaurant_id", restaurantId);
            jsonObject.put("business_opportunity_id", businessOppId);


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

            client.post(getApplicationContext(), "http://192.168.0.101:8000/view_meetings/", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    Log.i(TAG, "onStart" + ", " + this.getRequestURI());

                }



                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {

                        String res = new String(responseBody, "UTF-8");
                        Log.i(TAG, "onSuccess >>>>" + res);
                        if (TextUtils.isEmpty(res)) {
                            Toast.makeText(ViewMeeting.this, "Response not getting properly", Toast.LENGTH_SHORT).show();
                        } else {
                            JSONArray jsonArray = new JSONArray(res);

                            List<ViewMt> sampleuser = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jRes = jsonArray.getJSONObject(i);

                                ViewMt user = new ViewMt();

                                user.setMeetingEmpId(jRes.getInt("meeting_emp_id"));
                                user.setContactedPerson(jRes.getString("contacted_person"));
                                user.setMeetingDate(jRes.getString("meeting_date"));
                                user.setMeetingDescription(jRes.getString("meeting_description"));
                                //user.setCity(jRes.getString("city"));
                                // user.setLocality(jRes.getString("locality"));
                                // user.setLongitude(jRes.getString("longitude"));
                                // user.setLatitude(jRes.getString("latitude"));


                                sampleuser.add(user);
                            }
                               recyclerView.setAdapter(new ViewMeetingAdapter(getApplicationContext(),sampleuser));
                         //   recyclerView.setAdapter(new ViewMeetingAdapter(Viw, sampleuser);


                            Log.i(TAG, "Response : " + res);
                        }
                    } catch (Exception ep) {
                        ep.printStackTrace();
                    }
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

}
