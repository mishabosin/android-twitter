package com.codepath.apps.tweeterclient.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.tweeterclient.R;
import com.codepath.apps.tweeterclient.RestApplication;
import com.codepath.apps.tweeterclient.TwitterClient;
import com.codepath.apps.tweeterclient.fragments.UserTimelineFragment;
import com.codepath.apps.tweeterclient.models.TwitterUser;
import com.codepath.apps.tweeterclient.utils.Constants;
import com.codepath.apps.tweeterclient.utils.Network;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    TwitterUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String screenName = getIntent().getStringExtra(Constants.SCREEN_NAME);
        initUserData(screenName);

        if (savedInstanceState == null) {
            UserTimelineFragment fragment = UserTimelineFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fl_container, fragment);
            ft.commit();
        }
    }

    private void populateUserHeader(TwitterUser user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(user.getName());
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        tvTagline.setText(user.getDescription());
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvFollowers.setText(String.valueOf(user.getFollowersCount()) + " Followers");
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        tvFollowing.setText(String.valueOf(user.getFollowing()) + " Following");
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }

    void initUserData(String screenName) {
        TwitterClient client = RestApplication.getRestClient();
        client.getUserInfo(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                user = new TwitterUser(json);
                renderUserData(user);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                if (json.length() > 0) {
                    try {
                        user = new TwitterUser(json.getJSONObject(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                renderUserData(user);
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
                String msg = "Failed to get user info: " +
                        String.valueOf(statusCode) + " " +
                        errorMessage;
                Toast.makeText(ProfileActivity.this, msg, Toast.LENGTH_LONG).show();
            }

            private void renderUserData(TwitterUser user) {
                populateUserHeader(user);

                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle("@" + user.getScreenName());
                }
            }
        });
    }
}
