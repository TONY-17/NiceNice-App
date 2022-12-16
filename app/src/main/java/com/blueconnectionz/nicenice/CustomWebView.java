package com.blueconnectionz.nicenice;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blueconnectionz.nicenice.utils.Common;

public class CustomWebView extends Activity {
    WebView webView;
    SwipeRefreshLayout swipe;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_web_view);

        Common.setStatusBarColor(getWindow(),this, Color.WHITE);

        webView = findViewById(R.id.webView);
        swipe = findViewById(R.id.swipe);
        swipe.setOnRefreshListener(() -> WebAction());
        swipe.setColorSchemeColors(
                getResources().getColor(R.color.main,null),
                getResources().getColor(R.color.black2,null),
                getResources().getColor(R.color.colorBlue,null),
                getResources().getColor(R.color.green,null)
        );
        WebAction();

    }

    public void WebAction(){
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setAppCacheEnabled(true);
        webView.loadUrl("https://pos.snapscan.io/qr/ylMvTKMB");

        swipe.setRefreshing(true);
        webView.setWebViewClient(new WebViewClient(){
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webView.loadUrl("https://pos.snapscan.io/qr/ylMvTKMB");
            }
            public void onPageFinished(WebView view, String url) {
                swipe.setRefreshing(false);
            }
        });



        // Owner after they found a driver they should deativate the car and then the drivers that did not get the car they
        // will be refunded
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            finish();
        }
    }
}