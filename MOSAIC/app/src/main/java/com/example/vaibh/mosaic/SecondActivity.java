package com.example.vaibh.mosaic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG="SecondActivity";
    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword, editTextName, editTextLastName, editTextphone, editConfirmPassword;
    DatabaseReference ref;
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextphone = (EditText) findViewById(R.id.editTextphone);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        btnClear = findViewById(R.id.buttonClear);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("myfirebase-8a986");


        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    public User getValues() {
        User user = new User();
        user.setName(editTextName.getText().toString());
        user.setLastName(editTextLastName.getText().toString());
        user.setEmail(editTextEmail.getText().toString());
        user.setPhone(editTextphone.getText().toString());
        user.setPassword(editTextPassword.getText().toString());
        user.setConfirmPassword(editConfirmPassword.getText().toString());
        return user;
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String lastname = editTextLastName.getText().toString().trim();
        String cpass = editConfirmPassword.getText().toString().trim();
        String phone = editTextphone.getText().toString().trim();
        if (name.isEmpty()) {

            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return;
        }
        if (lastname.isEmpty()) {

            editTextLastName.setError("LastName is required");
            editTextLastName.requestFocus();
            return;
        }
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
        if (phone.isEmpty()) {
            editTextphone.setError("Mobile no is required");
            editTextphone.requestFocus();
            return;
        }
        if (!Patterns.PHONE.matcher(phone).matches()) {
            editTextphone.setError("Please enter a valid mobile no");
            editTextphone.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if (cpass.isEmpty()) {
            editConfirmPassword.setError("Confirm password is required");
            editConfirmPassword.requestFocus();
        }
        if (!password.equals(cpass)) {

            Toast.makeText(SecondActivity.this, "Password not matching", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

       /* mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                    storeData(user.getUid());

                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });*/

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("first_name", editTextName.getText().toString().trim());
            jsonObject.put("last_name", editTextLastName.getText().toString().trim());
            jsonObject.put("email", editTextEmail.getText().toString().trim());
            jsonObject.put("mobile", editTextphone.getText().toString().trim());
            jsonObject.put("password", editTextPassword.getText().toString().trim());
          //  jsonObject.put("confirm_password", editConfirmPassword.getText().toString().trim());
            //jsonObject.put("time", txtTime.getText().toString().trim());

            setUpCustomerLoginAPICall(jsonObject);
        } catch (Exception exp) {
            Log.i(TAG, "ERR > " + Log.getStackTraceString(exp));
        }

    }

    public void clearForm() {

        editTextName.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        editConfirmPassword.setText("");
        editTextLastName.setText("");
        editTextphone.setText("");

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonSignUp:
                registerUser();
                break;

            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.buttonClear:
                clearForm();
                break;
        }
    }


    private void setUpCustomerLoginAPICall(JSONObject jsonObject) {
        try {

            Log.i(TAG, "Login PARAM : " + jsonObject.toString());

            ByteArrayEntity entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            AsyncHttpClient client = new AsyncHttpClient();

            client.post(getApplicationContext(), "http://192.168.0.101:8000/employeeregistration/", entity, "application/json", new AsyncHttpResponseHandler() {
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
                        if(jResponse!=null && jResponse.has("valid")){

                            Toast.makeText(SecondActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();


                            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

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
                        if(jResponse!=null && jResponse.has("valid")){

                            Toast.makeText(SecondActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();


                            /*Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
*/
                        }


                    } catch (Exception exp) {
                        Log.i(TAG, "ERR : onSuccess -> LOGIN_API_CALL : " + Log.getStackTraceString(exp));
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

    public void storeData(final String token){
        ref.child("User").child(token).setValue(getValues());

        startActivity(new Intent(SecondActivity.this, MainActivity.class));
        finish();
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




