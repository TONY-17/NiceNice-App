package com.blueconnectionz.nicenice.ui.entry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.blueconnectionz.nicenice.MainActivity;
import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.utils.Common;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(),this);

        // View to be animated
        ImageView logo = findViewById(R.id.niceNiceLogo);
        logo.setAnimation(Common.viewBottomToOriginalAnim(getApplicationContext()));

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(new Intent(Splash.this, OnBoarding.class));
            finish();
        },2000);
    }
}