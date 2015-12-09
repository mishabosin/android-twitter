package com.codepath.apps.tweeterclient.utils;


import com.codepath.apps.tweeterclient.models.Tweet;
import com.codepath.apps.tweeterclient.models.TwitterUser;

public class Storage {
    public static void deleteAll() {
        Tweet.deleteAll();
        TwitterUser.deleteAll();
    }
}
