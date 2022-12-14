package com.blueconnectionz.nicenice.driver.profile.pages;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blueconnectionz.nicenice.CustomWebView;
import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;

public class CreditActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        Common.setStatusBarColor(getWindow(),this,getResources().getColor(R.color.colorBlue,null));

        ImageView backButton = findViewById(R.id.imageView13);
        backButton.setOnClickListener(view -> CreditActivity.super.onBackPressed());

        MaterialButton loadCredit = findViewById(R.id.loadMoreCredit);
        loadCredit.setOnClickListener(view -> {
            // Open web view to load credits
            startActivity(new Intent(CreditActivity.this, CustomWebView.class));
        });

        RecyclerView recyclerView = findViewById(R.id.pastTransactions);

        // Show a list of past transactions -> History of the admin loading credits to the owner/driver account
    }
}