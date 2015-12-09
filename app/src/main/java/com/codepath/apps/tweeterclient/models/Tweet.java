package com.codepath.apps.tweeterclient.models;

import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.codepath.apps.tweeterclient.utils.Storage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Represents the Tweet object returned from the Twitter API
 */
@Table(name = "tweets")
public class Tweet extends Model {
    @Column(name = "user", index = true)
    private TwitterUser user;
    @Column(name = "text")
    private String text;
    @Column(name = "twitter_id", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String twitterId;
    @Column(name = "created_at", index = true)
    private String createdAt;

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
            this.createdAt = object.getString("created_at");
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
        ArrayList<Tweet> tweets = new ArrayList<>(jsonArray.length());

        // Throw away all previous tweets and users
        Storage.deleteAll();

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            tweet.user.save();
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

    public String getCreatedAt() {
        return createdAt;
    }

    public String getRelativeCreatedAt() {
        final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
        Date date;
        try {
            date = sf.parse(createdAt);
        } catch (ParseException e) {
            return "Unknown";
        }

        CharSequence relative = DateUtils.getRelativeTimeSpanString(date.getTime());
        return relative.toString();
    }

    public static List<Tweet> getAll() {
        return new Select()
                .from(Tweet.class)
                .execute();
    }

    public static void deleteAll() {
        new Delete().from(Tweet.class).execute();
    }

}
