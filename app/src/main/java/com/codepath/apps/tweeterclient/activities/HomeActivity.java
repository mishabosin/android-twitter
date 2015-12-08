package com.codepath.apps.tweeterclient.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.codepath.apps.tweeterclient.R;
import com.codepath.apps.tweeterclient.RestApplication;
import com.codepath.apps.tweeterclient.TwitterClient;
import com.codepath.apps.tweeterclient.adapters.TweetFeedAdapter;
import com.codepath.apps.tweeterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private List<Tweet> tweets;
    TweetFeedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initAdapter();
        initRecyclerView();

        fetchHomeTimeline();
    }

    private void initAdapter() {
        tweets = new ArrayList<>();
        adapter = new TweetFeedAdapter(tweets);
    }

    private void initRecyclerView() {
        RecyclerView rvComments = (RecyclerView) findViewById(R.id.rvTweets);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchHomeTimeline() {
        fetchHomeTimeline(1);
    }

    private void updateTweets(ArrayList<Tweet> newTweets) {
        tweets.clear();
        tweets.addAll(newTweets);
        adapter.notifyDataSetChanged();
    }

    private void fetchHomeTimeline(int page) {
        TwitterClient client = RestApplication.getRestClient();
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                ArrayList<Tweet> tweets = Tweet.fromJson(json);
                updateTweets(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                handleError(statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                handleError(statusCode);
            }

            private void handleError(int statusCode) {
                String msg = "Failed to get Twitter feed: " + String.valueOf(statusCode);
                Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
