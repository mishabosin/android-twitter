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
	public static final String REST_CONSUMER_KEY = "HiuTrzkNT8EaFAaa81wRYEazB";
	public static final String REST_CONSUMER_SECRET = "DO_NOT_CHECK_IN";
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
		getPageableTimeline(apiUrl, page, handler);
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

	public void getMentionsTimeline(int page, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		getPageableTimeline(apiUrl, page, handler);
	}

	private RequestParams getPageParams(int page) {
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(page));
		return params;
	}

	private void getPageableTimeline(String url,  int page, JsonHttpResponseHandler handler) {
		getClient().get(url, getPageParams(page), handler);
	}

	public void getUserTimeline(int page, String screenName, JsonHttpResponseHandler handler) {
		RequestParams params = getPageParams(page);
		params.put("screen_name", screenName);
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		getClient().get(apiUrl, params, handler);
	}

	/**
	 * Docs: https://dev.twitter.com/rest/reference/get/account/verify_credentials
	 * URL: https://api.twitter.com/1.1/account/verify_credentials.json
	 * @param handler - handle the async response
	 */
	public void getUserInfo(String screenName, JsonHttpResponseHandler handler) {
		if (screenName == null) {
			String apiUrl = getApiUrl("account/verify_credentials.json");
			getClient().get(apiUrl, handler);
			return;
		}

		String apiUrl = getApiUrl("users/lookup.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		getClient().get(apiUrl, params, handler);
	}
}