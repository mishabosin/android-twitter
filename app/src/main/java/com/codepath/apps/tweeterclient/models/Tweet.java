package com.codepath.apps.tweeterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Represents the Tweet object returned from the Twitter API
 */
@Table(name = "tweets")
public class Tweet extends Model {
    @Column(name = "user", index = true)
    private TwitterUser user;
    @Column(name = "text")
    private String text;
    @Column(name = "twitter_id", index = true)
    private String twitterId;

    public Tweet() {
        super();
    }

    /**
     * Construct from JSON object
     * @param object - json object representing the user
     */
    public Tweet(JSONObject object) {
        super();

        try {
            this.twitterId = object.getString("id_str");
            this.text = object.getString("text");
            this.user = new TwitterUser(object.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the tweets contained in the json objects
     * @param jsonArray - array of json objects
     * @return array of Tweets
     */
    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            tweet.save();
            tweets.add(tweet);
        }

        return tweets;
    }


    public TwitterUser getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public String getTwitterId() {
        return twitterId;
    }

    // Finders
    public static TwitterUser byTwitterId(String twitterId) {
        return new Select().from(TwitterUser.class).where("twitter_id = ?", twitterId).executeSingle();
    }
}
