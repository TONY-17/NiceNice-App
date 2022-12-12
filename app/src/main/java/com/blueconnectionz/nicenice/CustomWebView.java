package com.blueconnectionz.nicenice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blueconnectionz.nicenice.utils.Common;

public class CustomWebView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_web_view);

        Common.setStatusBarColor(getWindow(),this, Color.WHITE);

        WebView webView = findViewById(R.id.webView);
/*        webView.setWebViewClient(new WebViewClient());

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        String url = getIntent().getStringExtra("SITE");
        webView.loadUrl(url);*/
        String url = getIntent().getStringExtra("SITE");
        webView.loadUrl(url);
        //webView.loadUrl("https://abhiandroid.com/ui/");


/*        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webView.setWebViewClient(webViewClient);

        webView.loadUrl("https://www.journaldev.com");*/

    }



    private class WebViewClientImpl extends WebViewClient {
        private Activity activity = null;
        public WebViewClientImpl(Activity activity) {
            this.activity = activity;
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if(url.indexOf("journaldev.com") > -1 ) return false;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
            return true;
        }

    }

}