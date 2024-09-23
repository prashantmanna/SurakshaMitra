package com.surakshamitra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.surakshamitra.Screens.AllScreen.LoginActivity;
import com.surakshamitra.Screens.AllScreen.MainActivity;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        new Handler().postDelayed(new Runnable() {
            @Override


            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.LoginID,0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn",false);


                    if(hasLoggedIn){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                        finish();
                    }
                }
        },3000);





    }





}