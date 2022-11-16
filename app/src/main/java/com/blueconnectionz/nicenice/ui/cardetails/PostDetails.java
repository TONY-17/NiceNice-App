package com.blueconnectionz.nicenice.ui.cardetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.blueconnectionz.nicenice.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class PostDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        // Set the status bar color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            View view = window.getDecorView();
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            this.getWindow().setStatusBarColor(Color.WHITE);
        }
        // Animate the bottom card from bottom to its current location
        final Animation[] animation = new Animation[1];
        animation[0] = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_to_original);
        MaterialCardView checkInCardView = findViewById(R.id.checkInCardView);
        checkInCardView.setAnimation(animation[0]);

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

            }else{
                runOnUiThread(() -> {
                    carName.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                });

            }

        });

        MaterialButton connect = findViewById(R.id.connect);
        connect.setOnClickListener(view1 -> startActivity(new Intent(PostDetails.this,Checkout.class)));
    }
}