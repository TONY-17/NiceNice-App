package com.blueconnectionz.nicenice.driver.entry;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.network.Role;
import com.blueconnectionz.nicenice.network.model.DriverRegisterReq;
import com.blueconnectionz.nicenice.owner.OwnerSignUp;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDetails extends AppCompatActivity {

    View loadingView;
    AVLoadingIndicatorView avLoadingIndicatorView;
    // Register form field
    TextInputEditText fullNameField;
    TextInputEditText phoneNumberField;
    TextInputEditText emailField;
    TextInputEditText passwordField;
    MaterialButton verifyNumber;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        Common.setStatusBarColor(getWindow(), this, Color.WHITE);

        loadingView = findViewById(R.id.loadingView);
        avLoadingIndicatorView = findViewById(R.id.avi);

        fullNameField = findViewById(R.id.fullNameTxT);
        phoneNumberField = findViewById(R.id.phoneNumberTxT);
        emailField = findViewById(R.id.emailTxT);
        passwordField = findViewById(R.id.passwordTxT);


        Common.linearProgressBarAnimator(findViewById(R.id.linearProgressIndicator));

        verifyNumber = findViewById(R.id.verifyNumber);
        verifyNumber.setOnClickListener(view -> {
            try {
                register();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void register() throws JSONException {
        Common.hideKeyboard(this);
        String name = Objects.requireNonNull(fullNameField.getText()).toString().trim();
        String email = Objects.requireNonNull(emailField.getText()).toString().trim();
        String number = Objects.requireNonNull(phoneNumberField.getText()).toString().trim();
        String password = Objects.requireNonNull(passwordField.getText()).toString().trim();

        if (name.isEmpty() || name.indexOf(' ') < 0) {
            fullNameField.setError("Full Name required");
            fullNameField.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailField.setError("Email required");
            emailField.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Email invalid");
            emailField.requestFocus();
            return;
        }
        if (number.isEmpty()) {
            phoneNumberField.setError("Phone number required");
            phoneNumberField.requestFocus();
            return;

        } else if (!PhoneNumberUtils.isGlobalPhoneNumber(number)) {
            phoneNumberField.setError("Phone number invalid");
            phoneNumberField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordField.setError("Password required");
            passwordField.requestFocus();
            return;
        }
        if (password.length() < 6) {
            passwordField.setError("Password less than 6 characters");
            passwordField.requestFocus();
            return;
        }

        Common.setStatusBarColor(getWindow(), PersonalDetails.this, getResources().getColor(R.color.background, null));
        loadingView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        //verifyNumber.setVisibility(View.GONE);

        // User JSON-OBJECT
        // {"email":"209@gmail.com","password":"12345678","role":null}
        JSONObject user = new JSONObject();
        user.put("email", email);
        user.put("password", password);
        user.put("role", Role.DRIVER);

        // Driver JSON-OBJECT
        JSONObject driver = new JSONObject();
        driver.put("fullName", name);
        driver.put("phoneNumber", number);
        driver.put("location", "Edenvale");
        driver.put("approved", false);
        driver.put("reported", false);
        driver.put("uniqueDocumentId", "IMAGE-ID");
        driver.put("creditBalance", 0);
        driver.put("platform", "UBER");
        driver.put("reference1", "0875643453");//
        driver.put("reference2", "0875643453");//

        List<MultipartBody.Part> documents = DocumentUpload.document;

        Call<ResponseBody> registerUser = RetrofitClient.getRetrofitClient().getAPI().registerDriver(user.toString(),
                driver.toString(),
                documents);
        registerUser.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        startActivity(new Intent(PersonalDetails.this, LandingPage.class));
                        Snackbar.make(getCurrentFocus(), "Account registered", Snackbar.LENGTH_LONG).show();
                    });
                } else {
                    runOnUiThread(() -> {
                        try {
                            System.out.println("RESPONSE " + response.errorBody().string());
                            Snackbar.make(getCurrentFocus(), "Account already exists", Snackbar.LENGTH_LONG).show();
                            loadingView.setVisibility(View.GONE);
                            avLoadingIndicatorView.setVisibility(View.GONE);
                            Common.setStatusBarColor(getWindow(), PersonalDetails.this, Color.WHITE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //verifyNumber.setVisibility(View.VISIBLE);
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                runOnUiThread(() -> {
                    Snackbar.make(getCurrentFocus(), "Server error", Snackbar.LENGTH_LONG).show();
                    loadingView.setVisibility(View.GONE);
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    Common.setStatusBarColor(getWindow(), PersonalDetails.this, Color.WHITE);
                    //verifyNumber.setVisibility(View.VISIBLE);
                });
            }
        });
    }
}