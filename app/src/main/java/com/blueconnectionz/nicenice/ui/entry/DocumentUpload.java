package com.blueconnectionz.nicenice.ui.entry;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.blueconnectionz.nicenice.MainActivity;
import com.blueconnectionz.nicenice.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class DocumentUpload extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            View view = window.getDecorView();
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            this.getWindow().setStatusBarColor(Color.WHITE);
        }
        LinearProgressIndicator progressBar = findViewById(R.id.linearProgressIndicator);

        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 25, 50);
        animation.setDuration(1000); // 3.5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();


        MaterialButton uploadDocuments = findViewById(R.id.uploadDocuments);
        uploadDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DocumentUpload.this, PersonalDetails.class));
            }
        });



    }
}