package com.codepath.apps.tweeterclient.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweeterclient.R;
import com.codepath.apps.tweeterclient.models.Tweet;
import com.codepath.apps.tweeterclient.models.TwitterUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetFeedAdapter extends
        RecyclerView.Adapter<TweetFeedAdapter.TweetViewHolder> {

    private List<Tweet> tweets;

    public TweetFeedAdapter(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    public static class TweetViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivAvatar;
        public TextView tvUserScreenName;
        public TextView tvText;

        public TweetViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
            tvUserScreenName = (TextView) itemView.findViewById(R.id.tvUserScreenName);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
        }
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_item_tweet, parent, false);

        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        TwitterUser user = tweet.getUser();

        holder.tvText.setText(tweet.getText());
        holder.tvUserScreenName.setText(String.format("@%s", user.getScreenName()));

        renderAvatar(holder, user.getProfileImageUrl());
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    private void renderAvatar(TweetViewHolder holder, String avatarUrl) {
        Picasso.with(holder.itemView.getContext())
                .load(avatarUrl)
                .fit()
                .placeholder(R.drawable.ic_avatar_default)
                .into(holder.ivAvatar);
    }
}
