package com.blueconnectionz.nicenice.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.ui.entry.DocumentUpload;
import com.google.android.material.button.MaterialButton;


public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        MaterialButton signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomePage.this, DocumentUpload.class));
            }
        });

        MaterialButton signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomePage.this,DocumentUpload.class));
            }
        });
    }
}