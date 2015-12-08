package com.codepath.apps.tweeterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents the twitter User object returned from the Twitter API
 */
@Table(name="users")
public class TwitterUser extends Model {
    // Fields
    @Column(name = "twitter_id", index = true)
    private String twitterId;
    @Column(name = "screen_name")
    private String screenName;
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    public TwitterUser() {
        super();
    }

    /**
     * Construct from JSON object
     * @param object - json object representing the user
     */
    public TwitterUser(JSONObject object) {
        super();

        try {
            this.twitterId = object.getString("id_str");
            this.screenName = object.getString("screen_name");
            this.profileImageUrl = object.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Getters
    public String getTwitterId() {
        return twitterId;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    // Finders
    public static TwitterUser byTwitterId(String twitterId) {
        return new Select().from(TwitterUser.class).where("twitter_id = ?", twitterId).executeSingle();
    }

}
