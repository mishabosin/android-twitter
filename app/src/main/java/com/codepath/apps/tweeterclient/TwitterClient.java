package com.codepath.apps.tweeterclient;

import android.content.Context;

import com.codepath.apps.tweeterclient.utils.Constants;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/**
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "8pnNuy0yoXh9t9VEUJL9aC1vE";
	public static final String REST_CONSUMER_SECRET = "fTg1evTuvfA7WIDd8Y61hPy0Iq1HHp4Gx29ktZtwgrGdEgeQkx"; // TODO: DO NOT CHECK THIS IN!
	public static final String REST_CALLBACK_URL = Constants.REDIRECT_URI;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	/**
	 * Get the twitter home timeline.
	 * Docs: https://dev.twitter.com/rest/reference/get/statuses/home_timeline
	 * URL: https://api.twitter.com/1.1/statuses/home_timeline.json
	 * @param handler - handle the async response
	 */
	public void getHomeTimeline(int page, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(page));
		getClient().get(apiUrl, params, handler);
	}

	/**
	 * Post a tweet
	 * Docs: https://dev.twitter.com/rest/reference/post/statuses/update
	 * URL: https://api.twitter.com/1.1/statuses/update.json
	 * @param body - tweet content
	 * @param handler - handle the async response
	 */
	public void postTweet(String body, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", body);
		getClient().post(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}