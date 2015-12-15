# Android Twitter Client

## Overview

This is a Twitter Client Project.
 
Submitted by: **Misha Bosin**

### User stories

* [x] User can sign in to Twitter using OAuth login
* [x] User can view the tweets from their home timeline
    * [x] User should be displayed the username, name, and body for each tweet
    * [x] User should be displayed the relative timestamp for each tweet "8m", "7h"
    * [x] User can view more tweets as they scroll with infinite pagination
* [x] User can compose a new tweet
    * [x] User can click a “Compose” icon in the Action Bar on the top right
    * [x] User can then enter a new tweet and post this to twitter
    * [x] User is taken back to home timeline with new tweet visible in timeline

* [x] User can switch between Timeline and Mention views using tabs.
    * [x] User can view their home timeline tweets.
    * [x] User can view the recent mentions of their username.
* [x] User can navigate to view their own profile
    * [x] User can see picture, tagline, # of followers, # of following, and tweets on their profile.
* [ ] User can click on the profile image in any tweet to see another user's profile.
    * [ ] User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
    * [ ] Profile view should include that user's timeline
* [x] User can infinitely paginate any of these timelines (home, mentions, user) by scrolling to the bottom

### Advanced

* [x] User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh)
* [x] Advanced: User can open the twitter app offline and see last loaded tweets
    - Tweets are persisted into sqlite and can be displayed from the local DB

### Bonus
 
* [x] Compose activity is replaced with a modal overlay

## Video Walkthrough 

![Imgur](http://i.imgur.com/n8gxDGr.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## License

    Copyright 2015 Misha Bosin

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
