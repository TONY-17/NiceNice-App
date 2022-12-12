package com.blueconnectionz.nicenice.driver.entry;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.network.model.OwnerRegisterReq;
import com.blueconnectionz.nicenice.owner.OwnerSignUp;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyNumber extends AppCompatActivity {

    View loadingView;
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number);

        Common.setStatusBarColor(getWindow(),this,Color.WHITE);

        loadingView = findViewById(R.id.loadingView);
        avLoadingIndicatorView = findViewById(R.id.avi);

        LinearProgressIndicator progressBar = findViewById(R.id.linearProgressIndicator);

        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 75, 100);
        animation.setDuration(1000); // 3.5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        MaterialButton verifyNumber = findViewById(R.id.verifyNumber);
        verifyNumber.setOnClickListener(view -> {
            // check if otp is valid

            createAccount();
        });
    }

    private void createAccount() {
        Common.hideKeyboard(this);
        String email = Objects.requireNonNull(OwnerSignUp.emailField.getText()).toString().trim();
        String number = Objects.requireNonNull(OwnerSignUp.phoneNumberField.getText()).toString().trim();
        String password = Objects.requireNonNull(OwnerSignUp.passwordField.getText()).toString().trim();


        Common.setStatusBarColor(getWindow(), VerifyNumber.this, getResources().getColor(R.color.background, null));
        loadingView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        //signUp.setVisibility(View.GONE);

        OwnerRegisterReq registerReq = new OwnerRegisterReq();
        registerReq.setEmail(email);
        registerReq.setPhoneNumber(number);
        registerReq.setPassword(password);

/*        Call<ResponseBody> registerOwner = RetrofitClient.getRetrofitClient().getAPI().registerOwner(registerReq);
        registerOwner.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Snackbar.make(getCurrentFocus(), "Account created", Snackbar.LENGTH_LONG).show();
                        startActivity(new Intent(VerifyNumber.this, LandingPage.class));
                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        Snackbar.make(getCurrentFocus(), "Account already exists", Snackbar.LENGTH_LONG).show();
                        loadingView.setVisibility(View.GONE);
                        avLoadingIndicatorView.setVisibility(View.GONE);
                        Common.setStatusBarColor(getWindow(), VerifyNumber.this, Color.WHITE);
                        //signUp.setVisibility(View.VISIBLE);
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                runOnUiThread(() -> {
                    Snackbar.make(getCurrentFocus(), "Server error", Snackbar.LENGTH_LONG).show();
                    loadingView.setVisibility(View.GONE);
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    //signUp.setVisibility(View.VISIBLE);
                    Common.setStatusBarColor(getWindow(), VerifyNumber.this, Color.WHITE);
                });
            }
        });*/
    }
}