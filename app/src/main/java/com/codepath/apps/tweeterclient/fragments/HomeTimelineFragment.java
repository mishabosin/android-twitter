package com.codepath.apps.tweeterclient.fragments;

import android.os.Bundle;

import com.codepath.apps.tweeterclient.RestApplication;
import com.codepath.apps.tweeterclient.TwitterClient;
import com.codepath.apps.tweeterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void fetchTimeline(final int page) {
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
                handleFetchError("Failed to get Twitter feed: " + String.valueOf(statusCode));
            }
        });
    }

}
