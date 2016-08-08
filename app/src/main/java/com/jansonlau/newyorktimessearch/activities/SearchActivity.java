// 1. Parse articles from API

package com.jansonlau.newyorktimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.jansonlau.newyorktimessearch.Article;
import com.jansonlau.newyorktimessearch.ArticleArrayAdapter;
import com.jansonlau.newyorktimessearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    // Declare all the different views
    EditText etQuery;
    GridView gvResults;
    Button btnSearch;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews(); // Create references to all the different views
    }

    // Initialize all the different views
    public void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles); // Initialize array adapter
        gvResults.setAdapter(adapter); // Bind grid view to array adapter

        // Set up listener for grid view click
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Create an intent to display the article
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);

                // Get the article to display
                Article article = articles.get(position);

                // Pass in that article into intent
                i.putExtra("article", article);

                // Launch activity
                startActivity(i);
            }
        });
    }

    public void onArticleSearch(View view) { // click handler for button
        String query = etQuery.getText().toString(); // get string that was typed in

//        Toast.makeText(this, "Searching for " + query, Toast.LENGTH_LONG).show(); // typed in word pops up for testing

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        // To pass in query parameters, make a "params" object
        RequestParams params = new RequestParams();
        params.put("api-key", "9d729b440fcc44c3835812bc2f31c5bd"); // add api key as paramter
        params.put("page", 0);
        params.put("q", query);

        // Make network request
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                try {
                    // Go from JSONObject "response" to JSONArray of "docs"
                    // Put "docs" into articleJsonResults
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
//                    Log.d("DEBUG", articleJsonResults.toString());
                    adapter.addAll(Article.fromJSONArray(articleJsonResults)); // call fromJSONArray function
                    Log.d("DEBUG", articles.toString());
//                    adapter.notifyDataSetChanged(); // Notify the adapter that something changed (Not needed after calling addAll on "adapter" and not "article"
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
