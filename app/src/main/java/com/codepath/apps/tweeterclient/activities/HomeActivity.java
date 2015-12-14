package com.codepath.apps.tweeterclient.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.tweeterclient.R;
import com.codepath.apps.tweeterclient.adapters.TweetsPagerAdapter;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initTabs();
    }

    private void initTabs() {
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        TweetsPagerAdapter adapter = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapter);

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);
    }
}
