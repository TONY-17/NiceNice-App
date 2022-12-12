package com.blueconnectionz.nicenice.driver.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.utils.Common;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(),this, Color.WHITE);
        TextView signUp = findViewById(R.id.signUpTextView);
        signUp.setOnClickListener(view -> startActivity(new Intent(SignUp.this, LandingPage.class)));
    }


}