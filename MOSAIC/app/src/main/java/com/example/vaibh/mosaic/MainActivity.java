package com.example.vaibh.mosaic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jgabrielfreitas.core.BlurImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import static java.util.logging.Level.INFO;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG="MainActivity";
   // FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.checkbox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            editTextEmail.setText(loginPreferences.getString("username", ""));
            editTextPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);

    }

    private void userLogin() {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }
        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", email);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

        progressBar.setVisibility(View.VISIBLE);

        /*mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    UserSession userSession = new UserSession(MainActivity.this);
                    userSession.setEmailId(email);
                    userSession.setIsLogin(true);
                    finish();

                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", editTextEmail.getText().toString().trim());
            jsonObject.put("password", editTextPassword.getText().toString().trim());

            //jsonObject.put("time", txtTime.getText().toString().trim());

            setUpCustomerLoginAPICall(jsonObject);
        } catch (Exception exp) {
            Log.i(TAG, "ERR > " + Log.getStackTraceString(exp));
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

//        if (mAuth.getCurrentUser() != null) {
//            finish();
//            startActivity(new Intent(this, ProfileActivity.class));
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewSignup:
                finish();
                startActivity(new Intent(this, SecondActivity.class));
                break;

            case R.id.buttonLogin:
                userLogin();
                break;
        }

    }
    private void setUpCustomerLoginAPICall(JSONObject jsonObject) {
        try {

            Log.i(TAG, "Login PARAM : " + jsonObject.toString());

            ByteArrayEntity entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            AsyncHttpClient client = new AsyncHttpClient();

            client.post(getApplicationContext(), "http://192.168.0.101:8000/employeelogin/", entity, "application/json", new AsyncHttpResponseHandler() {
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

                        JSONObject jResponse=new JSONObject(str);
                        if(jResponse!=null && jResponse.has("valid") && jResponse.getBoolean("valid")){

                            Toast.makeText(MainActivity.this, "Login Sucessful", Toast.LENGTH_SHORT).show();

                            UserSession userSession = new UserSession(MainActivity.this);
                            userSession.setEmpId(jResponse.getInt("employee_id"));
                            userSession.setIsLogin(true);

                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            finish();

                        }


                    } catch (Exception exp) {
                        Log.i(TAG, "ERR : onSuccess -> LOGIN_API_CALL : " + Log.getStackTraceString(exp));
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    try {
                        progressBar.setVisibility(View.GONE);
                        String str = new String(errorResponse);
                        Log.i(TAG, "Response : " + str);

                        JSONObject jResponse=new JSONObject(str);
                        if(jResponse!=null && jResponse.has("valid") && jResponse.getBoolean("valid")) {
                            Toast.makeText(MainActivity.this, "Username and password is wrong please try again...", Toast.LENGTH_SHORT).show();
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