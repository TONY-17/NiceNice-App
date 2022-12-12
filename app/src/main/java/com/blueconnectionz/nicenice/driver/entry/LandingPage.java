package com.blueconnectionz.nicenice.driver.entry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.blueconnectionz.nicenice.ForgotPassword;
import com.blueconnectionz.nicenice.MainActivity;
import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.network.model.LoginReq;
import com.blueconnectionz.nicenice.owner.OwnerMainActivity;
import com.blueconnectionz.nicenice.owner.OwnerSignUp;
import com.blueconnectionz.nicenice.utils.Common;
import com.droidbyme.dialoglib.AnimUtils;
import com.droidbyme.dialoglib.DroidDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingPage extends AppCompatActivity {

    TextInputEditText emailField;
    TextInputEditText passwordField;
    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;
    View loadingView;
    AVLoadingIndicatorView avLoadingIndicatorView;
    // Authentication buttons
    MaterialButton signInUser;
    MaterialButton signUpOwner;
    MaterialButton signUpDriver;

    public static Long userID;
    public static String userEmail;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(), this, Color.WHITE);
        // Login text fields
        emailField = findViewById(R.id.emailTxT);
        passwordField = findViewById(R.id.passwordTxt);
        emailLayout = findViewById(R.id.textInputLayout8);
        passwordLayout = findViewById(R.id.textInputLayout9);
        // un-clickable loading views
        loadingView = findViewById(R.id.loadingView);
        avLoadingIndicatorView = findViewById(R.id.avi);

        TextView signInInstead = findViewById(R.id.signInTextView);
        signInInstead.setOnClickListener(view ->
                runOnUiThread(() -> Common.termsAndConditions(LandingPage.this)));

        signInUser = findViewById(R.id.signUpUser);
        signInUser.setOnClickListener(view -> loginUser());

        signUpOwner = findViewById(R.id.signUpOwner);
        signUpOwner.setOnClickListener(view ->
                //startActivity(new Intent(LandingPage.this, OwnerSignUp.class))
                startActivity(new Intent(LandingPage.this, OwnerSignUp.class))
        );

        signUpDriver = findViewById(R.id.signUpDriver);
        signUpDriver.setOnClickListener(view ->
                //startActivity(new Intent(LandingPage.this, ProfileUpload.class))
                startActivity(new Intent(LandingPage.this, MainActivity.class))
        );


        findViewById(R.id.forgotPassword).setOnClickListener(view -> startActivity(new Intent(LandingPage.this, ForgotPassword.class)));
    }

    private void fakeLogin() {
        startActivity(new Intent(LandingPage.this, MainActivity.class));
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void loginUser() {
        Common.hideKeyboard(this);

        String email = Objects.requireNonNull(emailField.getText()).toString().trim();
        String password = Objects.requireNonNull(passwordField.getText()).toString().trim();

        if (email.isEmpty()) {
            emailField.setError("Email required");
            emailField.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Email invalid");
            emailField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordLayout.setError("Password required");
            passwordLayout.requestFocus();
            return;
        }
        if (password.length() < 6) {
            passwordLayout.setError("Password less than 6 characters");
            passwordLayout.requestFocus();
            return;
        }

        LoginReq loginRequest = new LoginReq();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        runOnUiThread(() -> {

            // Hide buttons when login button clicked
            hideOrShowButtons(View.GONE);
            // Show the loading views
            loadingView.setVisibility(View.VISIBLE);
            loadingView.setClickable(false);
            // change status color to loading view color
            Common.setStatusBarColor(getWindow(), LandingPage.this, getResources().getColor(R.color.background, null));
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
        });

        Call<ResponseBody> login = RetrofitClient.getRetrofitClient().getAPI().login(loginRequest);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        redirectToCorrectActivity(response.body().string());
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(() -> {
                        try {
                            System.out.println("RESPONSE " + response.errorBody().string());
                            Snackbar.make(getCurrentFocus(), "Wrong credentials", Snackbar.LENGTH_LONG).show();
                            loadingView.setVisibility(View.GONE);
                            avLoadingIndicatorView.setVisibility(View.GONE);
                            Common.setStatusBarColor(getWindow(), LandingPage.this, Color.WHITE);
                            hideOrShowButtons(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                runOnUiThread(() -> {
                    Snackbar.make(getCurrentFocus(), "Server error", Snackbar.LENGTH_LONG).show();
                    loadingView.setVisibility(View.GONE);
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    hideOrShowButtons(View.VISIBLE);
                    Common.setStatusBarColor(getWindow(), LandingPage.this, Color.WHITE);
                });
            }
        });

    }

    private void hideOrShowButtons(int visibility) {
        signInUser.setVisibility(visibility);
        signUpDriver.setVisibility(visibility);
        signUpOwner.setVisibility(visibility);
    }

    private void redirectToCorrectActivity(String data) throws JSONException {

        System.out.println("LOGIN RESPONSE " + data);

        JSONObject jsonObject = new JSONObject(data);
        /*
         * SharedPreferences will be used to detect if a user has logged in already
         * So the user log in screen is not shown all the time
         */
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();


        userID = jsonObject.getLong("id");
        userEmail = jsonObject.getString("email");
        myEdit.putFloat("ID", userID);

        System.out.println("USER ID " + data);
        JSONArray jsonArray = jsonObject.getJSONArray("role");
        String role = jsonArray.get(0).toString();
        switch (role) {
            case "OWNER":
                myEdit.putString("ROLE", role);

                startActivity(new Intent(LandingPage.this, OwnerMainActivity.class));
                finish();
                break;
            case "DRIVER":
                myEdit.putString("ROLE", role);
                startActivity(new Intent(LandingPage.this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}