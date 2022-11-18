package com.blueconnectionz.nicenice.ui.entry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.ui.auth.SignIn;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;

public class LandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(),this);

        TextView signInInstead = findViewById(R.id.signInTextView);
        signInInstead.setOnClickListener(view -> startActivity(new Intent(LandingPage.this, SignIn.class)));

        MaterialButton signUpDriver = findViewById(R.id.signUpDriver);
        signUpDriver.setOnClickListener(view -> startActivity(new Intent(LandingPage.this, ProfileUpload.class)));
    }
}