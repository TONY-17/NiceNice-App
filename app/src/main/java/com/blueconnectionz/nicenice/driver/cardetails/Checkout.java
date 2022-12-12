package com.blueconnectionz.nicenice.driver.cardetails;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.wang.avi.AVLoadingIndicatorView;

public class Checkout extends AppCompatActivity {

    TextView offerValue;
    Slider changeOfferValue;
    MaterialButton connect;
    View loadingView;
    AVLoadingIndicatorView avLoadingIndicatorView;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(),this,Color.WHITE);
        loadingView = findViewById(R.id.loadingView);
        avLoadingIndicatorView = findViewById(R.id.avi);
        connect = findViewById(R.id.connect);
        offerValue = findViewById(R.id.offerValue);
        changeOfferValue = findViewById(R.id.seekBar);
        changeOfferValue.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                offerValue.setText("R " + changeOfferValue.getValue());
            }
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                offerValue.setText("R " + changeOfferValue.getValue());
            }
        });

        connect.setOnClickListener(view -> {
            Common.setStatusBarColor(getWindow(),Checkout.this,getResources().getColor(R.color.background,null));
            loadingView.setVisibility(View.VISIBLE);
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(() -> runOnUiThread(() -> {
                //startActivity(new Intent(Checkout.this, AmityChatHomePageActivity.class));
                finish();
                loadingView.setVisibility(View.GONE);
                avLoadingIndicatorView.setVisibility(View.GONE);
                Common.setStatusBarColor(getWindow(),this,Color.WHITE);
            }),2000);
        });

    }
}