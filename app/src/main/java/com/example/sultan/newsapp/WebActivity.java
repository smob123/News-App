package com.example.sultan.newsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    WebView webView;
    String articleTitle, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = findViewById(R.id.web_view);
        //enable javascript on visited websites
        webView.getSettings().setJavaScriptEnabled(true);

        //set the app's webview to view web pages instead of the user's browser
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                articleTitle = title;
                getSupportActionBar().setTitle(articleTitle);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });

        //get the url from the calling activity
        url = getIntent().getExtras().getString("url");

        //go the specified url
        webView.loadUrl(url);
    }

    /*implement my costume options menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.webview_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /*handles menu item selection*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //if the share button is clicked
            case R.id.shareWebsite:
                //run the share intent
            onShareSelection();
            break;
            //if the action bar's return button is pressed
            case android.R.id.home:
                //finish the activity
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*handles displaying the share intent*/
    private void onShareSelection() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, articleTitle);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(intent, "Share using"));
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }
}
