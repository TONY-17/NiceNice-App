package com.blueconnectionz.nicenice.driver.entry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.card.MaterialCardView;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class OnBoarding extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;
    SliderAdapter sliderAdapter;
    TextView[] dots;
    MaterialCardView letsGetStarted;
    Animation animation;
    int currentPos;
    CircularSeekBar circularSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        Common.setStatusBarColor(getWindow(),this, Color.WHITE);

        //Hooks
        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
        letsGetStarted = findViewById(R.id.get_started_btn);
        letsGetStarted.setOnClickListener(view -> {
            runOnUiThread(() -> {
                startActivity(new Intent(OnBoarding.this, LandingPage.class));
                finish();
            });
        });
        //Call adapter
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        //Dots
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
        circularSeekBar = findViewById(R.id.seek_bar);
        circularSeekBar.setProgress(1);
    }


    public void next(View view) {
        viewPager.setCurrentItem(currentPos + 1);
    }

    private void addDots(int position) {
        runOnUiThread(() -> {
            dots = new TextView[2];
            dotsLayout.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(OnBoarding.this);
                dots[i].setText(Html.fromHtml("&#8226;", 0));
                dots[i].setTextSize(35);
                dots[i].setTextColor(getResources().getColor(R.color.black, null));
                dotsLayout.addView(dots[i]);
            }
            if (dots.length > 0) {
                //&#8212;
                dots[position].setText(Html.fromHtml("&#8226;", 0));
                dots[position].setTextColor(getResources().getColor(R.color.card_stroke, null));
            }
        });
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPos = position;


            System.out.println("POSITION " + position);

            dots[position].setTextColor(getResources().getColor(R.color.main, null));

            if (position == 0) {
                circularSeekBar.setProgress(5);
                letsGetStarted.setVisibility(View.GONE);
            } else if (position == 1) {
                circularSeekBar.setProgress(9);
                //letsGetStarted.setVisibility(View.VISIBLE);
                animation = AnimationUtils.loadAnimation(OnBoarding.this, R.anim.bottom_to_original);
                letsGetStarted.setAnimation(animation);
                letsGetStarted.setVisibility(View.VISIBLE);
            }

            /* else if (position == 2) {
                circularSeekBar.setProgress(5);
                letsGetStarted.setVisibility(View.VISIBLE);
            } else {
                circularSeekBar.setProgress(9);
                animation = AnimationUtils.loadAnimation(OnBoarding.this, R.anim.bottom_to_original);
                // letsGetStarted.setAnimation(animation);
                letsGetStarted.setVisibility(View.VISIBLE);
            }*/

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}