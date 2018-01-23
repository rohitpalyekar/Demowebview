package com.demo.rohit.demowebview;

/**
 * Created by Warrior969 on 24/09/16.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class WebViewResposive extends Activity {

    AlertDialog.Builder altDialog;

    private WebView wv;
    private ProgressBar progress;



    private String  loadingUrl;

    String NetworkAlert="Check Network Connection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_all_modules);

        altDialog = new AlertDialog.Builder(this);
        altDialog.setCancelable(false);


        loadingUrl="https://www.google.co.in"; // Url to be loaded

        wv = (WebView) findViewById(R.id.webView);
        progress = (ProgressBar) findViewById(R.id.progressBar);


      loadWebview();




    }

public  void loadWebview(){

    Networkcheck activeNetwork = new Networkcheck(this);
    boolean isConnected = activeNetwork.isConnectingToInternet();

    System.out.println("connection : " + isConnected); // Network Check

    if (isConnected == true) {
        // Making the Web-View Responsive
        progress.setVisibility(View.VISIBLE);
        wv.setVisibility(View.VISIBLE);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.requestFocusFromTouch();
        wv.setWebChromeClient(new WebChromeClient());
        wv.getSettings().setAllowFileAccess(true);
        wv.requestFocusFromTouch();
        wv.setWebChromeClient(new WebChromeClient());
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setDisplayZoomControls(false);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);





        wv.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onLoadResource(view, url);
            }


            @Override
            public void onPageStarted(WebView view, String url,
                                      Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                System.out.println("URL on page finished: " + url);
                progress.setVisibility(View.GONE);
                super.onPageFinished(view, url);

            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                //Your code to do
             Alert(error.toString());
            }

        });
        wv.loadUrl(loadingUrl);

    } else {
        Alert(NetworkAlert);
    }
}

public  void Alert(String Alert){
    progress.setVisibility(View.GONE);
    AlertDialog alertDialog = new AlertDialog.Builder(WebViewResposive.this).create();
    alertDialog.setTitle("Alert");
    alertDialog.setMessage(Alert);
    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Close",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });

    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Try Again",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    loadWebview();  //<- Call the Web view load again

                }
            });


    alertDialog.show();
}


}