package com.blueconnectionz.nicenice.driver.profile.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(),this,Color.WHITE);


        ImageView backButton = findViewById(R.id.imageView13);
        backButton.setOnClickListener(view -> ChangePassword.super.onBackPressed());

        TextView header = findViewById(R.id.textView29);
        TextInputLayout current = findViewById(R.id.outlinedTextField);
        TextInputLayout password = findViewById(R.id.textInputLayout5);
        TextInputLayout confirm = findViewById(R.id.textInputLayout6);
        MaterialButton change =  findViewById(R.id.verifyNumber);
        MaterialButton update = findViewById(R.id.update);

        final boolean[] changePassword = {false};
        change.setOnClickListener(view -> {
            current.setEnabled(false);
            // cipher the text
            password.setVisibility(View.VISIBLE);
            update.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.VISIBLE);
            changePassword[0] = true;
            current.setVisibility(View.GONE);
            header.setText("Enter new password. Please ensure the password was not used before.");
        });


    }
}