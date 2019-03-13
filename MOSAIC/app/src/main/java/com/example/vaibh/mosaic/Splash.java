package com.example.vaibh.mosaic;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final UserSession userSession = new UserSession(Splash.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (userSession.getIsLogin()) {
                    Intent intent = new Intent(Splash.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    intent.putExtra("email",userSession.getEmailId());
                    startActivity(intent);
                    finish();

                }
            }
        },SPLASH_TIME_OUT);

    }
}
