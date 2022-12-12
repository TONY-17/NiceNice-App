package com.blueconnectionz.nicenice.owner;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.utils.Common;

public class AddNewCar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_car);
        Common.setStatusBarColor(getWindow(),this, Color.WHITE);
    }

}