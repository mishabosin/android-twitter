package com.codepath.apps.tweeterclient.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.tweeterclient.R;
import com.codepath.apps.tweeterclient.RestApplication;
import com.codepath.apps.tweeterclient.TwitterClient;
import com.codepath.apps.tweeterclient.activities.NewTweetDialogFragment;
import com.codepath.apps.tweeterclient.adapters.TweetFeedAdapter;
import com.codepath.apps.tweeterclient.listeners.NewTweetDialogListener;
import com.codepath.apps.tweeterclient.listeners.RecyclerViewScrollListener;
import com.codepath.apps.tweeterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TweetsListFragment extends Fragment implements NewTweetDialogListener {

    private static final int FIRST_PAGE = 1;
    private List<Tweet> tweets;
    private TweetFeedAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    public TweetsListFragment() {
        // Required empty public constructor
    }

    abstract void fetchTimeline(int page);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        initActionButton(view);
        initRecyclerView(view);
        initSwipeContainer(view);

        fetchTimeline(FIRST_PAGE);

        return view;
    }

    private void initActionButton(View view) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewTaskDialog();
            }
        });
    }

    private void showNewTaskDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        NewTweetDialogFragment newTweetDialogFragment = NewTweetDialogFragment.newWeetDialogFragmentInstance();
        newTweetDialogFragment.show(fm, "fragment_new_tweet");
    }

    @Override
    public void onFinishNewDialog(String inputText) {
        postTweet(inputText);
    }

    private void initAdapter() {
        tweets = Tweet.getAll();
        adapter = new TweetFeedAdapter(tweets);
    }

    private void initRecyclerView(View view) {
        RecyclerView rvComments = (RecyclerView) view.findViewById(R.id.rvTweets);
        rvComments.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.addOnScrollListener(new RecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchTimeline(page + FIRST_PAGE);
            }
        });
    }

    protected void updateTweets(ArrayList<Tweet> newTweets, int page) {
        if (page == FIRST_PAGE) {
            tweets.clear();
        }
        tweets.addAll(newTweets);
        swipeContainer.setRefreshing(false);

        if (page == FIRST_PAGE) {
            adapter.notifyDataSetChanged();
            return;
        }
        int curSize = adapter.getItemCount();
        adapter.notifyItemRangeInserted(curSize, tweets.size() - 1);
    }

    private void addPostedTweet(Tweet newTweet) {
        tweets.add(0, newTweet);
        adapter.notifyDataSetChanged();
    }

    private void initSwipeContainer(View view) {
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimeline(FIRST_PAGE);
            }
        });
        // Progress animation colors. The first color is also used in the
        // refresh icon that shows up when the user makes the initial gesture
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    protected void handleFetchError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        swipeContainer.setRefreshing(false);
    }

    private void postTweet(String tweetText) {
        TwitterClient client = RestApplication.getRestClient();
        client.postTweet(tweetText, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Tweet tweet = new Tweet(json);
                addPostedTweet(tweet);
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
                String msg = "Failed to tweet: " + String.valueOf(statusCode);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

}
