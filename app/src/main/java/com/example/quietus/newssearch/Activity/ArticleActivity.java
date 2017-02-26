package com.example.quietus.newssearch.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.quietus.newssearch.Model.Article;
import com.example.quietus.newssearch.R;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {

    private WebView wvArticle;
    Article article;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        article = (Article) Parcels.unwrap(getIntent().getParcelableExtra("Article"));
        setView();
    }

    private void setView(){
        wvArticle = (WebView)findViewById(R.id.wvArticle);
        String url = article.getWebUrl();
        wvArticle.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wvArticle.loadUrl(url);

    }
}
