package com.blueconnectionz.nicenice;

import static com.blueconnectionz.nicenice.utils.Common.featureComingSoon;
import static com.blueconnectionz.nicenice.utils.Common.statusToast;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    TextInputEditText emailField;
    TextInputLayout email;
    TextInputLayout password;
    TextInputLayout password2;
    TextView header;
    TextView description;
    MaterialButton send;
    MaterialButton update;

    MaterialButton openMailApp;
    MaterialCardView parentMailSentCard;


    TextView didNotReceiveEmail;

    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);
        Common.setStatusBarColor(getWindow(), this, Color.WHITE);

        avLoadingIndicatorView = findViewById(R.id.avi);
        didNotReceiveEmail = findViewById(R.id.textView44);
        // Did not receive the email? Check your spam filter, or try another email address
        String text = "<font color=#FF000000>Did not receive the email? Check your spam filter, or </font> <font color=#C117BC> try another email address.</font>";
        didNotReceiveEmail.setText(Html.fromHtml(text));
        didNotReceiveEmail.setOnClickListener(view -> {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        });

        parentMailSentCard = findViewById(R.id.parentMail);
        openMailApp = findViewById(R.id.materialButton2);
        openMailApp.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                statusToast(2, "There is no email client installed.", ForgotPassword.this);
            }
        });
        emailField = findViewById(R.id.emailTxT);
        email = findViewById(R.id.textInputLayout8);
        password = findViewById(R.id.textInputLayout9);
        password2 = findViewById(R.id.confirmPassword);

        header = findViewById(R.id.textView33);
        description = findViewById(R.id.textView34);

        send = findViewById(R.id.sendInstructions);
        update = findViewById(R.id.updatePassword);
        send.setOnClickListener(view -> {
            checkIfEmailExists();
        });
        // ATTENTION: This was auto-generated to handle app links.


        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
/*            String recipeId = appLinkData.getLastPathSegment();
            Uri appData = Uri.parse("content://com.recipe_app/recipe/").buildUpon()
                    .appendPath(recipeId).build();
            showRecipe(appData);*/

            parentMailSentCard.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            send.setVisibility(View.GONE);
            password.setVisibility(View.VISIBLE);
            update.setVisibility(View.VISIBLE);
            password2.setVisibility(View.VISIBLE);
            header.setText("Create new password");
            description.setText("Your new password must be different from previous used passwords.");


        }
    }

    private void checkIfEmailExists() {
        if (emailField.getText().length() <= 0) {
            email.requestFocus();
            email.setError("Email required");
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailField.getText()).matches()) {
            emailField.setError("Email invalid");
            emailField.requestFocus();
            return;
        }

        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        Call<ResponseBody> checkEmail = RetrofitClient.getRetrofitClient().getAPI().checkIfEmailExists(
                emailField.getText().toString()
        );
        checkEmail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String data = response.body().string();
                        statusToast(1, data, ForgotPassword.this);

                        avLoadingIndicatorView.setVisibility(View.GONE);
                        Common.hideKeyboard(ForgotPassword.this);
                        send.setVisibility(View.GONE);
                        Handler handler = new Handler();
                        handler.postDelayed(() -> parentMailSentCard.setVisibility(View.VISIBLE), 1000);

                    } catch (NullPointerException | IOException e) {
                        statusToast(2, "Account does not exist", ForgotPassword.this);
                        avLoadingIndicatorView.setVisibility(View.GONE);
                    }
                } else {
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    statusToast(2, "Try again", ForgotPassword.this);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                avLoadingIndicatorView.setVisibility(View.GONE);
                statusToast(2, "Server Error", ForgotPassword.this);
                finish();
            }
        });


    }


}