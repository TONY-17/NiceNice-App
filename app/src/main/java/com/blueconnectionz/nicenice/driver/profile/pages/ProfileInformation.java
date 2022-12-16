package com.blueconnectionz.nicenice.driver.profile.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.driver.profile.ProfileFragment;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.network.model.ProfileInfo;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileInformation extends AppCompatActivity {

    TextInputEditText fullName;
    TextInputLayout fullName2;
    TextInputEditText emailAddress;
    TextInputEditText phoneNumber;
    MaterialButton updateProfile;

    ImageView backButton;

    // Documents
    MaterialCardView idCopy, license, report, address, rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_information);
        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(), this, Color.WHITE);
        backButton = findViewById(R.id.imageView13);
        backButton.setOnClickListener(v -> ProfileInformation.super.onBackPressed());
        // Check if the driver opened this page
        fullName = findViewById(R.id.userFullName);
        fullName2 = findViewById(R.id.outlinedTextField);
        emailAddress = findViewById(R.id.userEmailAddress);
        phoneNumber = findViewById(R.id.userPhoneNumber);
        updateProfile = findViewById(R.id.verifyNumber);
        // Doc pdf view
        idCopy = findViewById(R.id.materialCardView19);
        license = findViewById(R.id.materialCardView20);
        report = findViewById(R.id.materialCardView21);
        address = findViewById(R.id.materialCardView22);
        rating = findViewById(R.id.materialCardView23);


        // Fetch user profile details based on the supplied ID
        fetchProfileInfo(LandingPage.userID);

        updateProfile.setOnClickListener(view -> updateProfile(LandingPage.userID));

    }

    private void fetchProfileInfo(Long userID) {
        Call<ResponseBody> profileInfo = RetrofitClient.getRetrofitClient().getAPI().getProfileInfo(userID);
        profileInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String data = response.body().string();
                        System.out.println("PROFILE INFO " + data);
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.getString("fullName").contains("null")) {
                            fullName.setVisibility(View.GONE);
                            fullName2.setVisibility(View.GONE);
                            license.setVisibility(View.GONE);
                            address.setVisibility(View.GONE);
                            report.setVisibility(View.GONE);
                            rating.setVisibility(View.GONE);
                        } else {


                        }
                        fullName.setText(jsonObject.getString("fullName"));
                        emailAddress.setText(jsonObject.getString("email"));
                        phoneNumber.setText(jsonObject.getString("phoneNumber"));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ProfileInformation.this, "FAILED TO RETRIEVE INFO", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileInformation.this, "FAILED TO RETRIEVE INFO", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateProfile(Long userID) {
        if (fullName.length() < 0 || fullName.getText().toString().trim().indexOf(' ') < 0) {
            fullName.setError("Full name required");
            fullName.requestFocus();
            return;
        }

        if (emailAddress.length() < 0) {
            emailAddress.setError("Email required");
            emailAddress.requestFocus();
            return;
        }

        if (phoneNumber.length() < 0) {
            phoneNumber.setError("Phone number required");
            phoneNumber.requestFocus();
            return;
        }

        ProfileInfo request = new ProfileInfo();
        request.setEmail(emailAddress.getText().toString().trim());
        request.setCreditBalance(0);
        request.setFullName(fullName.getText().toString().trim());
        request.setPhoneNumber(phoneNumber.getText().toString().trim());

        Call<ResponseBody> updateAccount = RetrofitClient.getRetrofitClient().getAPI().updateProfileInfo(userID, request);
        updateAccount.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileInformation.this, "PROFILE UPDATE", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileInformation.this, "ERROR OCCURRED", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileInformation.this, "ERROR OCCURRED", Toast.LENGTH_SHORT).show();
            }
        });

    }
}