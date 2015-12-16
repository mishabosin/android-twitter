package com.codepath.apps.tweeterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents the twitter User object returned from the Twitter API
 */
@Table(name="users")
public class TwitterUser extends Model {
    // Fields
    @Column(name = "twitter_id", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String twitterId;
    @Column(name = "screen_name")
    private String screenName;
    @Column(name = "name")
    private String name;
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    @Column(name = "description")
    private String description;
    @Column(name = "followers_count")
    private int followersCount;
    @Column(name = "following")
    private int following;


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
            this.name = object.getString("name");
            this.profileImageUrl = object.getString("profile_image_url");
            this.description = object.getString("description");
            this.followersCount = object.getInt("followers_count");
            this.following = object.getInt("friends_count");
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowing() {
        return following;
    }

    // Finders
    public static TwitterUser byTwitterId(String twitterId) {
        return new Select().from(TwitterUser.class).where("twitter_id = ?", twitterId).executeSingle();
    }

    public static void deleteAll() {
        new Delete().from(TwitterUser.class).execute();
    }
}
