package com.blueconnectionz.nicenice.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.ui.entry.LandingPage;
import com.blueconnectionz.nicenice.utils.Common;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(),this);
        TextView signUp = findViewById(R.id.signUpTextView);
        signUp.setOnClickListener(view -> startActivity(new Intent(SignIn.this, LandingPage.class)));
    }


}