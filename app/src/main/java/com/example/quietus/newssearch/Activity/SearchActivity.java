package com.example.quietus.newssearch.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.quietus.newssearch.Adapter.ArticleAdapter;
import com.example.quietus.newssearch.EndlessScrollListener;
import com.example.quietus.newssearch.Model.Article;
import com.example.quietus.newssearch.Model.Filter;
import com.example.quietus.newssearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.Asserts;

public class SearchActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20;
    private Filter filter;

    public String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    public String API_KEY = "ca92c53e76884fe8ba6b605e6d6cf806";
    String sQuery;

    private ArrayList<Article> articles;
    private ArticleAdapter articleAdapter;

    GridView gvReslut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setView();

    }

    // Initialize menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_actionbar, menu);
        // menusearchview init
        MenuItem searchItem = menu.findItem(R.id.mnuSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                articles.clear();
                sQuery = query;
                onArticleSearch(0);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // filter
        filter = new Filter();

        return super.onCreateOptionsMenu(menu);
    }

    // setView
    public void setView(){
        // Setting gridview
        gvReslut = (GridView)findViewById(R.id.gvResult);
        articles = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, articles);
        gvReslut.setAdapter(articleAdapter);
        gvReslut.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                onArticleSearch(page);
                return true;
            }
        });
        gvReslut.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                //intent.putExtra("Article", Parcels.wrap(articles.get(position)));
                //startActivity(intent);
                openBrowser(position);

            }
        });
    }

    public void openBrowser(int position){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.addDefaultShareMenuItem();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(articles.get(position).getWebUrl()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            filter = (Filter) Parcels.unwrap(data.getParcelableExtra("filter"));
        }
    }

    public void loadOnStart(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void onArticleSearch(int page){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);

        if (filter.sDate != null && !filter.sDate.trim().isEmpty()){
            params.put("begin_date", filter.sDate);
        }
        if (filter.sSort != null && !filter.sSort.trim().isEmpty()){
            params.put("sort", filter.sSort);
        }
        if (filter.sDesk != null && !filter.sDesk.trim().isEmpty()){
            params.put("fq", filter.sDesk);
        }
        if (sQuery != null){
            params.put("q", sQuery);
        }
        for (int i = 0; i < 2; i++) {
            String sPage = String.valueOf(page * 2 + i);
            params.put("page", sPage);
            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG", response.toString());
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONObject("response").getJSONArray("docs");
                        articles.addAll(Article.fromJson(jsonArray));
                        articleAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("WARNING", throwable.toString());
                }
            });
        }
    }


    public void FilterAction(MenuItem item) {
        // Filter Onclick action
        // 1. new intent
        Intent intent = new Intent(this, FilterActivity.class);
        // 2. startActivityforResult
        startActivityForResult(intent, REQUEST_CODE);
    }
}
