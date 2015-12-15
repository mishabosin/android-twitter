package com.codepath.apps.tweeterclient.fragments;

import android.os.Bundle;

import com.codepath.apps.tweeterclient.RestApplication;
import com.codepath.apps.tweeterclient.TwitterClient;
import com.codepath.apps.tweeterclient.models.Tweet;
import com.codepath.apps.tweeterclient.utils.Constants;
import com.codepath.apps.tweeterclient.utils.Network;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class UserTimelineFragment extends TweetsListFragment {

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString(Constants.SCREEN_NAME, screenName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    void fetchTimeline(final int page) {
        TwitterClient client = RestApplication.getRestClient();
        String screenName = getArguments().getString(Constants.SCREEN_NAME, "");
        client.getUserTimeline(page, screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                ArrayList<Tweet> tweets = Tweet.fromJson(json);
                updateTweets(tweets, page);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                handleError(statusCode, Network.parseJsonErrorResponse(errorResponse));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                handleError(statusCode, responseString);
            }

            private void handleError(int statusCode, String errorMessage) {
                handleFetchError("Failed to get user feed: " + String.valueOf(statusCode) + " " + errorMessage);
            }
        });
    }
}
