package com.codepath.apps.tweeterclient.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private TweetFeedAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initActionButton();
        initAdapter();
        initRecyclerView();
        initSwipeContainer();

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
        postTweet(inputText);
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
        if (page == 1) {
            tweets.clear();
        }
        tweets.addAll(newTweets);

        if (page == 1) {
            adapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
            return;
        }
        int curSize = adapter.getItemCount();
        adapter.notifyItemRangeInserted(curSize, tweets.size() - 1);
    }

    private void addPostedTweet(Tweet newTweet) {
        tweets.add(0, newTweet);
        adapter.notifyDataSetChanged();
    }

    private void initSwipeContainer() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchHomeTimeline();
            }
        });
        // Progress animation colors. The first color is also used in the
        // refresh icon that shows up when the user makes the initial gesture
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
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
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void postTweet(String tweetText) {
        TwitterClient client = RestApplication.getRestClient();
        client.postTweet(tweetText, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Tweet tweet = new Tweet(json);
                addPostedTweet(tweet);
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
                String msg = "Failed to tweet: " + String.valueOf(statusCode);
                Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
