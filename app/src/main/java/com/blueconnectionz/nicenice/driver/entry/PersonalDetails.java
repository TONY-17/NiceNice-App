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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


        Common.linearProgressBarAnimator(findViewById(R.id.linearProgressIndicator), 70, 100);

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

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("password", password);
        userMap.put("role", Role.DRIVER);

        Map<String, Object> driverMap = new HashMap<>();
        driverMap.put("fullName", name);
        driverMap.put("phoneNumber", number);
        driverMap.put("location", "Edenvale");
        driverMap.put("approved", false);
        driverMap.put("reported", false);
        driverMap.put("uniqueDocumentId", "IMAGE-ID");
        driverMap.put("creditBalance", 0);
        driverMap.put("platform", "UBER");
        driverMap.put("reference1", "0875643453");//
        driverMap.put("reference2", "0875643453");//


        List<MultipartBody.Part> documents = DocumentUpload.document;

        Call<ResponseBody> registerUser = RetrofitClient.getRetrofitClient().getAPI().registerDriver(
                userMap,
                driverMap,
                documents);
        registerUser.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Common.statusToast(1, "Account created", PersonalDetails.this);
                        startActivity(new Intent(PersonalDetails.this, LandingPage.class));
                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        try {
                            String errMsg = response.errorBody().string();
                            System.out.println("ERROR MESSAGE " + errMsg);
                            if (errMsg.contains("registered")) {
                                Common.statusToast(2, errMsg, PersonalDetails.this);
                            }
                            loadingView.setVisibility(View.GONE);
                            avLoadingIndicatorView.setVisibility(View.GONE);
                            Common.setStatusBarColor(getWindow(), PersonalDetails.this, Color.WHITE);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                runOnUiThread(() -> {
                    Common.statusToast(2, "Network error", PersonalDetails.this);
                    loadingView.setVisibility(View.GONE);
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    Common.setStatusBarColor(getWindow(), PersonalDetails.this, Color.WHITE);


                });
            }
        });
    }
}