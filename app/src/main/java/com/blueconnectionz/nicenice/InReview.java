package com.blueconnectionz.nicenice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.utils.Common;

public class InReview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_review);
        Common.setStatusBarColor(getWindow(),this, Color.WHITE);

        findViewById(R.id.materialButton2).setOnClickListener(view -> {
            startActivity(new Intent(InReview.this, LandingPage.class));
            finish();
        });
    }
}