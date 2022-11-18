package com.blueconnectionz.nicenice.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.blueconnectionz.nicenice.R;

public class Common {

    // Changes the status bar color to white across all the activities
    public static void setStatusBarColor(Window window, Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = window.getDecorView();
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    // Bottom to original position view animation
    public static Animation viewBottomToOriginalAnim(Context context){
        final Animation[] animation = new Animation[1];
        animation[0] = AnimationUtils.loadAnimation(context,
                R.anim.bottom_to_original);
        return animation[0];
    }
}
