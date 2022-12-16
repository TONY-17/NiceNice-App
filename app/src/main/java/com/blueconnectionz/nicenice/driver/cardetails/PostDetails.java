package com.blueconnectionz.nicenice.driver.cardetails;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.utils.Common;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

public class PostDetails extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(), this, Color.WHITE);


        ImageView backButton = findViewById(R.id.imageView3);
        backButton.setOnClickListener(view -> PostDetails.super.onBackPressed());

        // Animate the bottom card from bottom to its current location
        MaterialCardView checkInCardView = findViewById(R.id.checkInCardView);
        checkInCardView.setAnimation(Common.viewBottomToOriginalAnim(this));

        // Manipulate views based on scrolling events
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView2);
        TextView carName = findViewById(R.id.carNameTop);
        View view = findViewById(R.id.view6);
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            double scrollViewHeight = nestedScrollView.getChildAt(0).getBottom() - nestedScrollView.getHeight();
            double getScrollY = nestedScrollView.getScrollY();
            double scrollPosition = (getScrollY / scrollViewHeight) * 100d;

            if (scrollPosition >= 20) {
                runOnUiThread(() -> {
                    carName.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                });

            } else {
                runOnUiThread(() -> {
                    carName.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                });

            }

        });


        TextView headerName = findViewById(R.id.textView11);
        TextView car = findViewById(R.id.textView12);
        ImageView carImage = findViewById(R.id.imageView5);
        TextView description = findViewById(R.id.textView17);
        TextView price = findViewById(R.id.price);
        TextView location = findViewById(R.id.textView7);

        headerName.setText(getIntent().getStringExtra("make"));
        car.setText(getIntent().getStringExtra("model"));
        Glide.with(carImage)
                .asBitmap()
                .placeholder(new ColorDrawable(carImage.getContext().getResources().getColor(R.color.light_gray, null)))
                .load(getIntent().getStringExtra("image"))
                .into(carImage);

        description.setText(getIntent().getStringExtra("description"));
        price.setText("R " + getIntent().getStringExtra("price"));
        location.setText(getIntent().getStringExtra("location"));
        carName.setText(getIntent().getStringExtra("model") + " " + getIntent().getStringExtra("make"));
        MaterialButton connect = findViewById(R.id.connect);
        connect.setOnClickListener(view1 -> /*Toast.makeText(this,"IN DEV",Toast.LENGTH_LONG).show()*/
                featureNotAvailable());
    }

    private void featureNotAvailable() {
     startActivity(new Intent(PostDetails.this,Checkout.class));
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PopupDialog.getInstance(PostDetails.this)
                        .setStyle(Styles.FAILED)
                        .setHeading("Uh-Oh")
                        .setDescription("The car is not available")
                        .setCancelable(false)
                        .showDialog(new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                            }
                        });
            }
        });*/
    }
}