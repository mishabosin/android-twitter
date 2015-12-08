package com.codepath.apps.restclienttemplate;

import android.content.Context;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 */
public class RestApplication extends com.activeandroid.app.Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		RestApplication.context = this;
	}

	/**
	 * Provides a twitter REST client that's accessible anywhere within the app.
	 * Usage:
	 * TwitterClient client = RestApplication.getRestClient();
	 * client.makeSomeRequest(...);
	 * @return a twitter rest client
	 */
	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, RestApplication.context);
	}
}