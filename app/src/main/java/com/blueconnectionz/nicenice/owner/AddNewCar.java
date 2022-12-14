package com.blueconnectionz.nicenice.owner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.entry.DocumentUpload;
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.driver.entry.ProfileUpload;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewCar extends AppCompatActivity implements PickiTCallbacks {

    ImageView checkBox1;
    TextView selectedFileTXT;
    MaterialCardView disk, license;
    TextInputEditText make, model, year, city, weeklyTarget, description;
    MaterialCheckBox depositRequired, hasInsurance, hasTracker, activeOnPlatforms;

    MultipartBody.Part document;
    File file = null;

    int SELECT_PICTURE = 200;
    PickiT pickiT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_car);
        Common.setStatusBarColor(getWindow(), this, Color.WHITE);

        pickiT = new PickiT(this, this, this);

        ImageView backButton = findViewById(R.id.imageView13);
        backButton.setOnClickListener(view -> AddNewCar.super.onBackPressed());
        setUpViews();


        license.setOnClickListener(view -> openGalleryFolder());

        MaterialButton submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                validateFields();
                try {
                    System.out.println("JSON REQ 1 " +  LandingPage.userID);
                    System.out.println("JSON REQ 2 " +  requestJSONObject());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Call<ResponseBody> newCar = RetrofitClient.getRetrofitClient().getAPI().addNewCar(
                            LandingPage.userID,
                            requestJSONObject(),
                            document);
                    newCar.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                try {
                                    System.out.println("CAR UPLOAD MSG " + response.body().string() );
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                try {
                                    System.out.println("CAR UPLOAD ERR " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                            System.out.println("CAR UPLOAD ERR  2" + t.getMessage());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private void setUpViews() {
        checkBox1= findViewById(R.id.cb1);
        selectedFileTXT= findViewById(R.id.selectedFile);
        disk = findViewById(R.id.uploadLicense);
        license = findViewById(R.id.uploadIdCopy);
        make = findViewById(R.id.make);
        model = findViewById(R.id.model);
        year = findViewById(R.id.year);
        city = findViewById(R.id.city);
        weeklyTarget = findViewById(R.id.target);
        description = findViewById(R.id.description);

        depositRequired = findViewById(R.id.materialCheckBox);
        hasInsurance = findViewById(R.id.materialCheckBox3);
        hasTracker = findViewById(R.id.materialCheckBox4);
        activeOnPlatforms = findViewById(R.id.materialCheckBox33);

    }

    private void validateFields() {
        if (make.getText().length() <= 0) {
            make.requestFocus();
            make.setError("Make required");
            return;
        }
        if (model.getText().length() <= 0) {
            model.requestFocus();
            model.setError("Model required");
            return;
        }
        if (year.getText().length() <= 0) {
            year.requestFocus();
            year.setError("Year required");
            return;
        }
        if (city.getText().length() <= 0) {
            city.requestFocus();
            city.setError("Year required");
            return;
        }
        if (weeklyTarget.getText().length() <= 0) {
            weeklyTarget.requestFocus();
            weeklyTarget.setError("Target required");
            return;
        }
        if (description.getText().length() <= 0) {
            description.requestFocus();
            description.setError("Description required");
            return;
        }

        if (file == null) {
            license.setStrokeColor(Color.RED);
            license.setStrokeWidth(2);
            return;
        }
    }

    private void openGalleryFolder() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                if (null != data) {
                    convertURI(data);
                }
            }
        }
    }


    private void convertURI(Intent data) {
        ClipData clipData = Objects.requireNonNull(data).getClipData();
        if (clipData != null) {
            int numberOfFilesSelected = clipData.getItemCount();
            if (numberOfFilesSelected > 1) {
                pickiT.getMultiplePaths(clipData);
                StringBuilder allPaths = new StringBuilder("Multiple Files Selected:" + "\n");
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    allPaths.append("\n\n").append(clipData.getItemAt(i).getUri());
                }
            } else {
                pickiT.getPath(clipData.getItemAt(0).getUri(), Build.VERSION.SDK_INT);
            }
        } else {
            pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
        }
    }


    private Map<String,Object> requestJSONObject() throws JSONException {
        Map<String,Object> newCar = new HashMap<>();

        newCar.put("make", make.getText().toString().trim());
        newCar.put("model", model.getText().toString().trim());
        newCar.put("year", year.getText().toString().trim());
        newCar.put("city", city.getText().toString().trim());
        newCar.put("weeklyTarget", weeklyTarget.getText().toString().trim());

        newCar.put("depositRequired", depositRequired.isChecked());
        newCar.put("hasInsurance", hasInsurance.isChecked());
        newCar.put("hasTracker", hasTracker.isChecked());
        newCar.put("activeOnHailingPlatforms", activeOnPlatforms.isChecked());

        newCar.put("approved", false);
        newCar.put("numConnections", 0);
        newCar.put("age", 0);
        newCar.put("views", 0);

        newCar.put("description", description.getText().toString().trim());
        newCar.put("ownerID", 0);

        return newCar;

    }


    ProgressBar mProgressBar;
    TextView percentText;
    private AlertDialog mdialog;
    ProgressDialog progressBar;


    @Override
    public void PickiTonUriReturned() {
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Waiting to receive file...");
        progressBar.setCancelable(false);
        progressBar.show();
    }

    @Override
    public void PickiTonStartListener() {
        if (progressBar.isShowing()) {
            progressBar.cancel();
        }
        final AlertDialog.Builder mPro = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        @SuppressLint("InflateParams") final View mPView = LayoutInflater.from(this).inflate(R.layout.dailog_layout, null);
        percentText = mPView.findViewById(R.id.percentText);

        percentText.setOnClickListener(view -> {
            pickiT.cancelTask();
            if (mdialog != null && mdialog.isShowing()) {
                mdialog.cancel();
            }
        });

        mProgressBar = mPView.findViewById(R.id.mProgressBar);
        mProgressBar.setMax(100);
        mPro.setView(mPView);
        mdialog = mPro.create();
        mdialog.show();

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {
        String progressPlusPercent = progress + "%";
        percentText.setText(progressPlusPercent);
        mProgressBar.setProgress(progress);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String reason) {
        if (mdialog != null && mdialog.isShowing()) {
            mdialog.cancel();
        }
        //  Chick if it was successful
        if (wasSuccessful) {
            //  Set returned path to TextView
            if (path.contains("/proc/")) {
                // "Sub-directory inside Downloads was selected." + "\n" + " We will be making use of the /proc/ protocol." + "\n" + " You can use this path as you would normally." + "\n\n" + "PickiT path:" + "\n" +
            } else {
                System.out.println("FILE CONTENT 2" + path);
            }
            try {
                convertToFile(path);
                selectedFileTXT.setText(path);
                checkBox1.setImageResource(R.drawable.ic_baseline_check_circle_24);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, reason, Toast.LENGTH_LONG).show();

        }
    }

    private void convertToFile(String path) {
        file = new File(path);
        RequestBody fileBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        document = MultipartBody.Part.createFormData("document", file.getName(), fileBody);
    }

    @Override
    public void PickiTonMultipleCompleteListener(ArrayList<String> paths, boolean wasSuccessful, String Reason) {
        if (mdialog != null && mdialog.isShowing()) {
            mdialog.cancel();
        }
        StringBuilder allPaths = new StringBuilder();
        for (int i = 0; i < paths.size(); i++) {
            allPaths.append("\n").append(paths.get(i)).append("\n");
        }
    }

    @Override
    public void onBackPressed() {
        pickiT.deleteTemporaryFile(this);
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            pickiT.deleteTemporaryFile(this);
        }
    }
}