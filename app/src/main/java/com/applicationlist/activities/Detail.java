package com.applicationlist.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.applicationlist.pojo.Contact;
import com.applicationlist.R;

public class Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(getIntent()!=null){
            Contact c = getIntent().getExtras().getParcelable("contact");
            ((TextView)findViewById(R.id.name)).setText(c.getName());
            ((TextView)findViewById(R.id.number)).setText(Integer.toString(c.getNumber()));
            ((TextView)findViewById(R.id.note)).setText(c.getNote());
        }

        startWebView("https://www.amazon.com/");
    }


    private WebView webView;
    private ProgressDialog pd;

    private void startWebView(String url){

        new AlertDialog.Builder(this)
                .setMessage("");

        webView = (WebView) findViewById(R.id.wv);
        webView.setWebViewClient(new WebViewClient(){

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onLoadResource(WebView webView, String url){
                if(pd==null){
                    pd = new ProgressDialog(Detail.this);
                    pd.setMessage("Loading...");
                    pd.show();
                }
            }

            public void onPageFinished(WebView webView, String url){
                if(pd!=null && pd.isShowing()){
                    pd.dismiss();
                }
            }

        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
