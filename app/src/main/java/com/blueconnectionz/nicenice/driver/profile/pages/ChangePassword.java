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
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    TextInputLayout current;
    TextInputEditText current1;
    TextInputLayout password;
    TextInputLayout confirm;
    TextInputEditText password1;
    TextInputEditText confirm1;
    MaterialButton change;
    MaterialButton update;

    TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(), this, Color.WHITE);


        ImageView backButton = findViewById(R.id.imageView13);
        backButton.setOnClickListener(view -> ChangePassword.super.onBackPressed());

        header = findViewById(R.id.textView29);
        current = findViewById(R.id.outlinedTextField);
        current1 = findViewById(R.id.enteredPassword);
        password = findViewById(R.id.textInputLayout5);
        confirm = findViewById(R.id.textInputLayout6);
        password1 = findViewById(R.id.newPassword);
        confirm1 = findViewById(R.id.confirmNewPassword);
        change = findViewById(R.id.verifyNumber);
        update = findViewById(R.id.update);

        change.setOnClickListener(view -> {
            if (current1.getText().length() <= 0) {
                current.requestFocus();
                current.setError("Password required");
            } else {
                passwordValid();
            }
        });

        update.setOnClickListener(v -> changePassword());

    }

    private void passwordValid() {
        Call<ResponseBody> checkIfPasswordValid = RetrofitClient.getRetrofitClient().getAPI()
                .checkIfPasswordIsValid(LandingPage.userID, String.valueOf(current1.getText().toString().trim()));
        checkIfPasswordValid.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String data = response.body().string();
                        System.out.println("API RESPONSE VALID " + data);
                        if (Boolean.valueOf(data) == true) {
                            runOnUiThread(() -> {
                                current.setEnabled(false);
                                password.setVisibility(View.VISIBLE);
                                update.setVisibility(View.VISIBLE);
                                confirm.setVisibility(View.VISIBLE);
                                current.setVisibility(View.GONE);
                                header.setText("Enter new password. Please ensure the password was not used before.");
                            });
                        } else if(Boolean.valueOf(data) == false) {
                            runOnUiThread(() -> {
                                current1.requestFocus();
                                current1.setError("Password is wrong");
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Common.statusToast(2, "Try again later", ChangePassword.this);
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Common.statusToast(2, "Try again later", ChangePassword.this);
                return;
            }
        });
    }

    private void changePassword(){
        if(password1.getText().length() <= 0){
            password1.setError("Password required");
            password.requestFocus();
        }

        if(confirm1.getText().length() <= 0){
            confirm1.setError("Password required");
            confirm.requestFocus();
        }

        if(!password1.getText().toString().trim().equals(confirm1.getText().toString().trim())){
            confirm.requestFocus();
            password.requestFocus();
            Common.statusToast(2, "Passwords not the same", ChangePassword.this);
        }

        Call<ResponseBody> changePassword = RetrofitClient.getRetrofitClient().getAPI().changePassword(LandingPage.userID,
                confirm1.getText().toString().trim());
        changePassword.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Common.statusToast(1, "Password updated", ChangePassword.this);
                    finish();
                }else{
                    Common.statusToast(2, "Try again later", ChangePassword.this);
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Common.statusToast(2, "Try again later", ChangePassword.this);
                return;
            }
        });
    }
}