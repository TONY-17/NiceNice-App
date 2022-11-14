package com.blueconnectionz.nicenice.ui;

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

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Set the status bar color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            View view = window.getDecorView();
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            this.getWindow().setStatusBarColor(Color.WHITE);
        }

        final Animation[] animation = new Animation[1];
        animation[0] = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_to_original);

        // View to be animated
        ImageView logo = findViewById(R.id.niceNiceLogo);
        logo.setAnimation(animation[0]);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        },2000);
    }
}