package com.blueconnectionz.nicenice.driver.entry;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.wang.avi.AVLoadingIndicatorView;

public class ProfileUpload extends AppCompatActivity {

    int SELECT_PICTURE = 200;
    int REQUEST_IMAGE_CAPTURE = 100;

    View loadingView;
    AVLoadingIndicatorView avLoadingIndicatorView;
    MaterialButton selectPictureFromGallery;
    MaterialButton openCamera;

    // Driver profile image
    public static Intent driverProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_upload);
        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(), this, Color.WHITE);

        loadingView = findViewById(R.id.loadingView);
        avLoadingIndicatorView = findViewById(R.id.avi);

        LinearProgressIndicator progressBar = findViewById(R.id.linearProgressIndicator);
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 25);
        animation.setDuration(1000); // 3.5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        openCamera = findViewById(R.id.signUpUser);
        openCamera.setOnClickListener(view -> {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, REQUEST_IMAGE_CAPTURE);
        });

        // Enables a driver to select an image from gallery
        selectPictureFromGallery = findViewById(R.id.signUpOwner);
        selectPictureFromGallery.setOnClickListener(view -> openGalleryFolder());
    }


    private void openGalleryFolder() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Select image from camera
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                /* driverProfilePicture = (Bitmap) extras.get("data");*/
                driverProfilePicture = data;
                runOnUiThread(() -> {
                    //imageView.setImageBitmap(imageBitmap);
                    loadingView.setVisibility(View.VISIBLE);
                    avLoadingIndicatorView.setVisibility(View.VISIBLE);
                    openCamera.setVisibility(View.GONE);
                    selectPictureFromGallery.setVisibility(View.GONE);
                    Common.setStatusBarColor(getWindow(), ProfileUpload.this, getResources().getColor(R.color.background, null));
                    Handler handler = new Handler();
                    handler.postDelayed(() -> startActivity(new Intent(ProfileUpload.this, DocumentUpload.class)), 2000);

                });
            }
            // Select image from gallery
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                //driverProfilePicture = Common.uriToBitmap(selectedImageUri, this);
                driverProfilePicture = data;
                if (null != selectedImageUri) {
                    runOnUiThread(() -> {
                        //IVPreviewImage.setImageURI(selectedImageUri);
                        loadingView.setVisibility(View.VISIBLE);
                        avLoadingIndicatorView.setVisibility(View.VISIBLE);
                        Common.setStatusBarColor(getWindow(), ProfileUpload.this, getResources().getColor(R.color.background, null));
                        openCamera.setVisibility(View.GONE);
                        selectPictureFromGallery.setVisibility(View.GONE);

                        Handler handler = new Handler();
                        handler.postDelayed(() -> startActivity(new Intent(ProfileUpload.this, DocumentUpload.class)), 2000);

                    });
                }
            }
        }

    }
}