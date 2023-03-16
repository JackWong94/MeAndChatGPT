package com.meandchatgpt.meandchatgpt;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.webkit.*;

import com.google.android.gms.ads.*;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSafeBrowsingEnabled(true);
        webView.getSettings().setDomStorageEnabled(true); // Enable DOM storage (needed for some web apps)
        webView.setWebContentsDebuggingEnabled(false);
        webView.loadUrl("https://chat.openai.com/chat");
        String defaultUserAgent = System.getProperty("http.agent");
        webView.getSettings().setUserAgentString(defaultUserAgent);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://")) {
                    // URL is using HTTP
                    return false;
                }
                view.loadUrl(url);
                return true;
            }
        });
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
            webView.pauseTimers();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
            webView.resumeTimers();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewGroup parent = (ViewGroup) webView.getParent();
        if (parent != null) {
            parent.removeView(webView);
        }
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack() && !webView.getUrl().equals("https://chat.openai.com/chat")) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}