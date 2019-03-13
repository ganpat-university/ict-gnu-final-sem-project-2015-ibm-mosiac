package com.example.vaibh.mosaic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class MeetingActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG="MeetingActivity";
    EditText txtDate,editContacted,editMeeting;

    private int mYear, mMonth, mDay;
    private EditText editRestId,editEmp,bsn_opp_id;
    private Button btnAdd,buttonClear;
    ProgressBar progressBar;
    private int restaurantId;
    private int businessOppId;
    UserSession userSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        txtDate = (EditText) findViewById(R.id.editDate);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        editContacted = (EditText) findViewById(R.id.editContacted);
        editMeeting = (EditText) findViewById(R.id.editMeeting);
        editEmp = (EditText) findViewById(R.id.editEmp);
        bsn_opp_id = (EditText) findViewById(R.id.bsn_opp_id);
        btnAdd = findViewById(R.id.buttonAdd);
        buttonClear = findViewById(R.id.buttonClear);
        editRestId = findViewById(R.id.editRestId);

       userSession = new UserSession(MeetingActivity.this);
        editEmp.setText(String.valueOf(userSession.getEmpId()));

        restaurantId = getIntent().getIntExtra("restaurant_id",-1);
        editRestId.setText(String.valueOf(restaurantId));

        businessOppId = getIntent().getIntExtra("business_opportunity_id",-1);
        bsn_opp_id.setText(String.valueOf(businessOppId));


        Log.i(TAG, "RES ID : "+restaurantId+", BUSINESS OPP ID : "+businessOppId);

        txtDate.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        buttonClear.setOnClickListener(this);

    }
       private void Add() {

         /*  String contactname =  editContacted.getText().toString().trim();
           String Meeting = editMeeting.getText().toString().trim();
           String sales = editSales.getText().toString().trim();


           if (contactname.isEmpty()) {

               editContacted.setError("Contact Person name is required");
               editContacted.requestFocus();
               return;
           }
           if (Meeting.isEmpty()) {

               editMeeting.setError("Meeting description is required");
               editContacted.requestFocus();
               return;
           }
           if (sales.isEmpty()) {

               editSales.setError("Sales Person name is required");
               editContacted.requestFocus();
               return;
           }*/


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("restaurant_id", restaurantId);
            jsonObject.put("business_opportunity_id", businessOppId);
            jsonObject.put("employee_id", userSession.getEmpId());
            jsonObject.put("contacted_person", editContacted.getText().toString().trim());
            jsonObject.put("meeting_date", txtDate.getText().toString().trim());
            jsonObject.put("meeting_description", editMeeting.getText().toString().trim());


            setUpCustomerLoginAPICall(jsonObject);
        } catch (Exception exp) {
            Log.i(TAG, "ERR > " + Log.getStackTraceString(exp));
        }

    }



    public void clearForm() {
     //   editRestId.setText("");
       // editRestName.setText("");
        editContacted.setText("");
        editMeeting.setText("");
        txtDate.setText("");

    }


    @Override
    public void onClick(View v) {

        if (v == txtDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                 //   txtDate.setText((monthOfYear + 1) + "-" +dayOfMonth  + "-" + year);
                    txtDate.setText(year + "-" +(monthOfYear + 1) + "-" +dayOfMonth);

                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == btnAdd) {
            Add();

        }
        if (v == buttonClear) {
             clearForm();
        }
    }



    private void setUpCustomerLoginAPICall(JSONObject jsonObject) {
        try {

            Log.i(TAG, "Login PARAM : " + jsonObject.toString());

            ByteArrayEntity entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            AsyncHttpClient client = new AsyncHttpClient();

            client.post(getApplicationContext(), "http://192.168.0.101:8000/add_meeting_record/", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    Log.i(TAG, "onStart" + ", " + this.getRequestURI());

                }



                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        String str = new String(responseBody);
                        Log.i(TAG, "Response : " + str);

                        JSONObject jResponse = new JSONObject(str);
                        if (jResponse != null && jResponse.has("valid") && jResponse.getBoolean("valid")) {

                           Toast.makeText(MeetingActivity.this, "Successful", Toast.LENGTH_SHORT).show();


//                            Intent intent = new Intent(MeetingActivity.this, ProfileActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);

                            finish();
                        }
                    } catch (Exception exp) {
                        Log.i(TAG, "ERR : onSuccess -> LOGIN_API_CALL : " + Log.getStackTraceString(exp));
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e)
                    {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        try {
                            progressBar.setVisibility(View.GONE);
                            String str = new String(errorResponse);
                            Log.i(TAG, "Response : " + str);

                            JSONObject jResponse=new JSONObject(str);
                            if(jResponse!=null && jResponse.has("valid") && jResponse.getBoolean("valid")) {
                                Toast.makeText(MeetingActivity.this, " please try again...", Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception exp) {
                            Log.i(TAG, "ERR : onFailure -> LOGIN_API_CALL : " + Log.getStackTraceString(exp));
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
