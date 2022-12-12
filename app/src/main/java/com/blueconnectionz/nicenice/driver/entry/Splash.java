package com.blueconnectionz.nicenice.driver.entry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.blueconnectionz.nicenice.MainActivity;
import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.owner.OwnerMainActivity;
import com.blueconnectionz.nicenice.utils.Common;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        runOnUiThread(() -> {
            // Set the status bar color to white
            Common.setStatusBarColor(getWindow(),Splash.this,Color.WHITE);
            // View to be animated
            ImageView logo = findViewById(R.id.niceNiceLogo);
            logo.setAnimation(Common.viewBottomToOriginalAnim(getApplicationContext()));
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                Long userID = sh.getLong("ID", 0);
                String role = sh.getString("ROLE","");

                System.out.println("ID VAL " + userID);
                if(userID == 0){
                    startActivity(new Intent(Splash.this, LandingPage.class));
                }else{
                    if(role.equals("OWNER")){
                        startActivity(new Intent(Splash.this, OwnerMainActivity.class));
                    }else if (role.equals("DRIVER")){
                        startActivity(new Intent(Splash.this, MainActivity.class));
                    }
                }
                finish();

            },2000);
        });
    }
}