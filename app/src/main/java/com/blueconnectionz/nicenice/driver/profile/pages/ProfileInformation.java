package com.blueconnectionz.nicenice.driver.profile.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.network.model.ProfileInfo;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wang.avi.AVLoadingIndicatorView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    List<String> documentURLs = new ArrayList<>();


    TextView file1;
    TextView file2;
    TextView file3;
    TextView file4;
    TextView file5;

    MaterialCardView openFile1, openFile2, openFile3, openFile4, openFile5;


    AVLoadingIndicatorView avLoadingIndicatorView;
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

        avLoadingIndicatorView = findViewById(R.id.avi);
        // Doc pdf view
        idCopy = findViewById(R.id.materialCardView19);
        license = findViewById(R.id.materialCardView20);
        report = findViewById(R.id.materialCardView21);
        address = findViewById(R.id.materialCardView22);
        rating = findViewById(R.id.materialCardView23);
        // Show files names
        file1 = findViewById(R.id.textView38);
        file2 = findViewById(R.id.tv3);
        file3 = findViewById(R.id.textView90);
        file4 = findViewById(R.id.tv89);
        file5 = findViewById(R.id.tv4);

        // open pdf's
        openFile1 = findViewById(R.id.materialCardView18);
        openFile2 = findViewById(R.id.mc1);
        openFile3 = findViewById(R.id.mc3);
        openFile4 = findViewById(R.id.mc5);
        openFile5 = findViewById(R.id.mc2);

        // Fetch user profile details based on the supplied ID
        fetchProfileInfo(LandingPage.userID);


        updateProfile.setOnClickListener(view -> updateProfile(LandingPage.userID));

    }

    private String extractFileName(String path) {
        int indexOfFile = path.lastIndexOf('/');
        return path.substring(indexOfFile + 1);
    }

    private void fetchProfileInfo(Long userID) {
        Call<ResponseBody> profileInfo = RetrofitClient.getRetrofitClient().getAPI().getProfileInfo(userID);
        profileInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String data = response.body().string();
                        updateProfile.setEnabled(true);
                        avLoadingIndicatorView.setVisibility(View.GONE);
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.getString("fullName").contains("null")) {
                            fullName.setVisibility(View.GONE);
                            fullName2.setVisibility(View.GONE);
                            license.setVisibility(View.GONE);
                            address.setVisibility(View.GONE);
                            report.setVisibility(View.GONE);
                            rating.setVisibility(View.GONE);


                            JSONArray documents = jsonObject.getJSONArray("documents");
                            JSONObject singleDocument = documents.getJSONObject(0);
                            String url = singleDocument.getString("url");
                            file1.setText(extractFileName(url));
                            openFile1.setOnClickListener(v -> openPDFViewer(url));

                        } else {
                            JSONArray documents = jsonObject.getJSONArray("documents");
                            for (int i = 0; i < documents.length(); i++) {
                                JSONObject singleDocument = documents.getJSONObject(i);
                                String url = singleDocument.getString("url");
                                // Ignore the profile Image
                                if (url.contains(".png") || url.contains(".jpg") || url.contains(".jpeg")) {
                                } else {
                                    documentURLs.add(url);
                                }
                            }
                            // Set the names of the pdfs
                            file1.setText(extractFileName(documentURLs.get(0)));
                            file2.setText(extractFileName(documentURLs.get(1)));
                            file3.setText(extractFileName(documentURLs.get(2)));
                            file4.setText(extractFileName(documentURLs.get(3)));
                            file5.setText(extractFileName(documentURLs.get(4)));
                            // Open a preview of the pdf
                            openFile1.setOnClickListener(v -> openPDFViewer(documentURLs.get(0)));
                            openFile2.setOnClickListener(v -> openPDFViewer(documentURLs.get(1)));
                            openFile3.setOnClickListener(v -> openPDFViewer(documentURLs.get(2)));
                            openFile4.setOnClickListener(v -> openPDFViewer(documentURLs.get(3)));
                            openFile5.setOnClickListener(v -> openPDFViewer(documentURLs.get(4)));

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

    private void openPDFViewer(String filePath) {
        Uri path = Uri.parse(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Common.statusToast(2, "No Application Available to View PDF", ProfileInformation.this);
        }
    }

    private void updateProfile(Long userID) {
        if (fullName.length() < 0 || fullName.getText().toString().trim().indexOf(' ') < 0) {
            fullName2.setHelperText("Full name required");
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