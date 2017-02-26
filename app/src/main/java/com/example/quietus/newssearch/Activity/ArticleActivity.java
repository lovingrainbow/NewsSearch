package com.example.quietus.newssearch.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.quietus.newssearch.Model.Article;
import com.example.quietus.newssearch.R;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {

    private ShareActionProvider miShareAction;
    private Intent shareIntent;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article_actionbar, menu);
        MenuItem item = menu.findItem(R.id.mnuShare);
        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, article.getWebUrl());
        miShareAction.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }
}
