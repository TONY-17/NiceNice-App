package com.blueconnectionz.nicenice;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Common.setStatusBarColor(getWindow(),this, Color.WHITE);

        TextInputLayout email = findViewById(R.id.textInputLayout8);
        TextInputLayout password = findViewById(R.id.textInputLayout9);
        TextInputLayout password2 = findViewById(R.id.confirmPassword);

        TextView header = findViewById(R.id.textView33);
        TextView description  = findViewById(R.id.textView34);

        MaterialButton send = findViewById(R.id.sendInstructions);
        MaterialButton update = findViewById(R.id.updatePassword);
        send.setOnClickListener(view -> {
            email.setVisibility(View.GONE);
            send.setVisibility(View.GONE);
            password.setVisibility(View.VISIBLE);
            update.setVisibility(View.VISIBLE);
            password2.setVisibility(View.VISIBLE);
            header.setText("Create new password");
            description.setText("Your new password must be different from previous used passwords.");
        });
    }
}