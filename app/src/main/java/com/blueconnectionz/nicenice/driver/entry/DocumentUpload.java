package com.blueconnectionz.nicenice.driver.entry;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.camera2.TotalCaptureResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DocumentUpload extends AppCompatActivity implements PickiTCallbacks {

    // Request code for selecting a PDF document.
    private static final int PICK_PDF_FILE_1 = 1;
    private static final int PICK_PDF_FILE_2 = 2;
    private static final int PICK_PDF_FILE_3 = 3;
    private static final int PICK_PDF_FILE_4 = 4;
    private static final int PICK_PDF_FILE_5 = 5;

    int documentsUpload = 1;
    // Views to upload files
    MaterialCardView idCopy;
    MaterialCardView report;
    MaterialCardView address;
    MaterialCardView rating;
    MaterialButton uploadDocuments;

    TextView idCopyFILE;
    TextView reportFILE;
    TextView addressFILE;
    TextView ratingFILE;

    // Checkboxes for each card view
    ImageView idCopyCB;
    ImageView reportCB;
    ImageView addressCB;
    ImageView ratingCB;
    ImageView uploadDocumentsCB;
    // Text views

    TextView idCopyTXT;
    TextView uploadLicenseTXT;
    TextView reportTXT;
    TextView addressTXT;
    TextView ratingTXT;


    View loadingView;
    AVLoadingIndicatorView avLoadingIndicatorView;
    ScrollView scrollView;

    int count = 0;
    // Stores all the driver documentation including the profile image
    public static List<MultipartBody.Part> files = new ArrayList<>();

    PickiT pickiT;
    List<Intent> documents = ProfileUpload.documents;


    public static List<MultipartBody.Part> document = new ArrayList<>();
    File file = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);

        Common.setStatusBarColor(getWindow(), this, Color.WHITE);

        pickiT = new PickiT(this, this, this);

        loadingView = findViewById(R.id.loadingView);
        avLoadingIndicatorView = findViewById(R.id.avi);
        scrollView = findViewById(R.id.nestedScrollView);

        Common.linearProgressBarAnimator(findViewById(R.id.linearProgressIndicator),35,70);

        MaterialCardView license = findViewById(R.id.uploadLicense);
        idCopy = findViewById(R.id.uploadIdCopy);
        report = findViewById(R.id.uploadReport);
        address = findViewById(R.id.uploadAddress);
        rating = findViewById(R.id.uploadRating);


        idCopyFILE = findViewById(R.id.uploadIdCopyFILE);
        ;
        reportFILE = findViewById(R.id.uploadReportFILE);
        addressFILE = findViewById(R.id.uploadAddressFILE);
        ratingFILE = findViewById(R.id.uploadRatingFILE);

        uploadLicenseTXT = findViewById(R.id.uploadLicenseTXT);
        idCopyTXT = findViewById(R.id.uploadIdCopyTXT);
        reportTXT = findViewById(R.id.uploadReportTXT);
        addressTXT = findViewById(R.id.uploadAddressTXT);
        ratingTXT = findViewById(R.id.uploadRatingTXT);


        idCopyCB = findViewById(R.id.checkBox);
        reportCB = findViewById(R.id.cb1);
        addressCB = findViewById(R.id.cb2);
        ratingCB = findViewById(R.id.cb3);
        uploadDocumentsCB = findViewById(R.id.cb55);

        license.setOnClickListener(view -> pdfIntent(PICK_PDF_FILE_1));
        idCopy.setOnClickListener(view -> pdfIntent(PICK_PDF_FILE_2));
        report.setOnClickListener(view -> pdfIntent(PICK_PDF_FILE_3));
        address.setOnClickListener(view -> pdfIntent(PICK_PDF_FILE_4));
        rating.setOnClickListener(view -> pdfIntent(PICK_PDF_FILE_5));


        uploadDocuments = findViewById(R.id.uploadDocuments);
        uploadDocuments.setOnClickListener(view -> {
            System.out.println("DOCUMENTS SIZE " + documents.toString());
            convertToMultipart(documents);
        });


    }

    private void pdfIntent(int pdf_file) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, pdf_file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PDF_FILE_1) {
                Uri sUri = data.getData();
                documents.add(data);

                String sPath = sUri.getPath();
                uploadLicenseTXT.setText(sPath);
                //loadingView.setVisibility(View.VISIBLE);
                avLoadingIndicatorView.setVisibility(View.VISIBLE);
                //Common.setStatusBarColor(getWindow(),DocumentUpload.this,getResources().getColor(R.color.background,null));
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    idCopyTXT.setVisibility(View.VISIBLE);
                    idCopy.setVisibility(View.VISIBLE);
                    idCopyCB.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    //idCopyCB.setBackgroundColor(getResources().getColor(R.color.main,null));
                    avLoadingIndicatorView.hide();
                }, 1000);


            }
            if (requestCode == PICK_PDF_FILE_2) {
                Uri sUri = data.getData();
                documents.add(data);

                //loadingView.setVisibility(View.VISIBLE);
                avLoadingIndicatorView.setVisibility(View.VISIBLE);
                //Common.setStatusBarColor(getWindow(),DocumentUpload.this,getResources().getColor(R.color.background,null));
                String sPath = sUri.getPath();
                idCopyFILE.setText(sPath);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    report.setVisibility(View.VISIBLE);
                    reportTXT.setVisibility(View.VISIBLE);
                    reportCB.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    avLoadingIndicatorView.hide();
                    //idCopyCB.setBackgroundColor(getResources().getColor(R.color.main,null));
                }, 1000);

            }
            if (requestCode == PICK_PDF_FILE_3) {
                Uri sUri = data.getData();
                documents.add(data);

                address.setVisibility(View.VISIBLE);
                addressTXT.setVisibility(View.VISIBLE);

                //loadingView.setVisibility(View.VISIBLE);
                avLoadingIndicatorView.setVisibility(View.VISIBLE);
                //Common.setStatusBarColor(getWindow(),DocumentUpload.this,getResources().getColor(R.color.background,null));

                String sPath = sUri.getPath();
                reportFILE.setText(sPath);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    address.setVisibility(View.VISIBLE);
                    addressTXT.setVisibility(View.VISIBLE);
                    addressCB.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    avLoadingIndicatorView.hide();
                    //idCopyCB.setBackgroundColor(getResources().getColor(R.color.main,null));
                }, 1000);

            }
            if (requestCode == PICK_PDF_FILE_4) {
                Uri sUri = data.getData();
                documents.add(data);

                //loadingView.setVisibility(View.VISIBLE);
                avLoadingIndicatorView.setVisibility(View.VISIBLE);
                //Common.setStatusBarColor(getWindow(),DocumentUpload.this,getResources().getColor(R.color.background,null));
                String sPath = sUri.getPath();
                addressFILE.setText(sPath);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    rating.setVisibility(View.VISIBLE);
                    ratingTXT.setVisibility(View.VISIBLE);
                    avLoadingIndicatorView.hide();
                    ratingCB.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
                    //idCopyCB.setBackgroundColor(getResources().getColor(R.color.main,null));
                }, 1000);
            }
            if (requestCode == PICK_PDF_FILE_5) {
                Uri sUri = data.getData();
                documents.add(data);
                avLoadingIndicatorView.setVisibility(View.VISIBLE);
                //Common.setStatusBarColor(getWindow(),DocumentUpload.this,getResources().getColor(R.color.background,null));
                String sPath = sUri.getPath();
                ratingFILE.setText(sPath);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    avLoadingIndicatorView.hide();
                    uploadDocumentsCB.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    // Enable button to continue to next page
                    uploadDocuments.setVisibility(View.VISIBLE);
                    // Scroll to bottom of view to make it easier for the user to see the button
                    scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
                }, 1000);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void convertToMultipart(List<Intent> documents) {

        for(Intent data : documents){
            try{

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
            }catch (NullPointerException e){
                documents.stream().skip(documents.indexOf(data));
                e.printStackTrace();
            }

        }

        startActivity(new Intent(DocumentUpload.this, PersonalDetails.class));
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
        System.out.println("PickiTonCompleteListener "  + count++);


        if (wasSuccessful) {
            try {
                convertToFile(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(this,reason,Toast.LENGTH_LONG).show();
            //selectedDocumentTXT.setText(reason);
        }
    }

    private void convertToFile(String path) {
        System.out.println("METHOD RUNNING " + count++);
        file = new File(path);
        Random random = new Random();
        String fileName = random.nextInt(1000) + file.getName();
        RequestBody fileBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("documents", fileName, fileBody);
        document.add(filePart);
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

        System.out.println("ALL PATHS " + allPaths.toString());
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