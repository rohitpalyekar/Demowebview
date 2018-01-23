package com.demo.rohit.demowebview;

/**
 * Created by Rohit on 23/01/16.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


public class WebViewResposive extends Activity {

    AlertDialog.Builder altDialog;

    private WebView wv;
    private ProgressBar progress;



    String NetworkAlert="Check Network Connection";
    boolean doubleBackToExitPressedOnce = false;
    String intialUrl= "https://www.google.co.in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_all_modules);

        altDialog = new AlertDialog.Builder(this);
        altDialog.setCancelable(false);



        wv = (WebView) findViewById(R.id.webView);
        progress = (ProgressBar) findViewById(R.id.progressBar);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.RED));
        }

        checkPermission();

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
            }

        });

            wv.loadUrl(intialUrl);


        wv.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) { //url, userAgent,contentDescription, mimetype, contentLength

                DownloadManager.Request db_request=new DownloadManager.Request(Uri.parse(s));
                db_request.allowScanningByMediaScanner();
                db_request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                String fileName = URLUtil.guessFileName(s,s1,s3);
                db_request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);

                DownloadManager dManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dManager.enqueue(db_request);

            }
        });

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
    protected void checkPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    // show an alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Storage Permission");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    WebViewResposive.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    123
                            );
                        }
                    });
                    builder.setNeutralButton("Cancel",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    // Request permission
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            123
                    );
                }
            }else {
                // Permission already granted
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case 123:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Permission granted
                }else {
                    // Permission denied
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        // adding urls for back press functionality

        if(wv.canGoBack()){
            wv.goBack();
        }else{
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            wv.loadUrl(intialUrl);
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to Exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}