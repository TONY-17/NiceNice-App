package com.blueconnectionz.nicenice.driver.profile.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.utils.Common;

public class CreditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        Common.setStatusBarColor(getWindow(),this,Color.BLUE);
    }
}