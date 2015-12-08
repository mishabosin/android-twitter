package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.RestApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fetchHomeTimeline();
    }

    private void fetchHomeTimeline() {
        fetchHomeTimeline(1);
    }

    private void fetchHomeTimeline(int page) {
        TwitterClient client = RestApplication.getRestClient();
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                // Response is automatically parsed into a JSONArray
                // json.getJSONObject(0).getLong("id");
                // TODO: implement
                Toast.makeText(HomeActivity.this, "Got a home timeline!!!", Toast.LENGTH_LONG).show();
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
