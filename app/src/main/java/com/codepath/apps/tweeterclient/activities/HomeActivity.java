package com.codepath.apps.tweeterclient.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.tweeterclient.R;
import com.codepath.apps.tweeterclient.RestApplication;
import com.codepath.apps.tweeterclient.TwitterClient;
import com.codepath.apps.tweeterclient.adapters.TweetFeedAdapter;
import com.codepath.apps.tweeterclient.listeners.NewTweetDialogListener;
import com.codepath.apps.tweeterclient.listeners.RecyclerViewScrollListener;
import com.codepath.apps.tweeterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NewTweetDialogListener {

    private List<Tweet> tweets;
    TweetFeedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initActionButton();
        initAdapter();
        initRecyclerView();

        fetchHomeTimeline();
    }

    private void initActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewTaskDialog();
            }
        });
    }

    private void showNewTaskDialog() {
        FragmentManager fm = getSupportFragmentManager();
        NewTweetDialogFragment newTweetDialogFragment = NewTweetDialogFragment.newWeetDialogFragmentInstance();
        newTweetDialogFragment.show(fm, "fragment_new_tweet");
    }

    @Override
    public void onFinishNewDialog(String inputText) {
        //TODO: tweet this
        Toast.makeText(HomeActivity.this, "Tweet this: " + inputText, Toast.LENGTH_LONG).show();
    }

    private void initAdapter() {
        tweets = new ArrayList<>();
        adapter = new TweetFeedAdapter(tweets);
    }

    private void initRecyclerView() {
        RecyclerView rvComments = (RecyclerView) findViewById(R.id.rvTweets);
        rvComments.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.addOnScrollListener(new RecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchHomeTimeline(page);
            }
        });
    }

    private void fetchHomeTimeline() {
        fetchHomeTimeline(1);
    }

    private void updateTweets(ArrayList<Tweet> newTweets, int page) {
        if (page == 0) {
            tweets.clear();
        }
        tweets.addAll(newTweets);

        if (page == 0) {
            adapter.notifyDataSetChanged();
            return;
        }
        int curSize = adapter.getItemCount();
        adapter.notifyItemRangeInserted(curSize, tweets.size() - 1);
    }

    private void fetchHomeTimeline(final int page) {
        TwitterClient client = RestApplication.getRestClient();
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                ArrayList<Tweet> tweets = Tweet.fromJson(json);
                updateTweets(tweets, page);
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
