// 2. Parse each article and create an Article object out of each article

package com.jansonlau.newyorktimessearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by jansonlau on 8/6/16.
 */
@Parcel
public class Article {
    public String getHeadLine() {
        return headLine;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    // fields must be public for Parceler library
    String webUrl;
    String headLine;
    String thumbNail;

    // Empty constructor needed by the Parceler library
    public Article() {

    }

    public Article(JSONObject jsonObject, String input) {
        // INITIALIZE SEARCH RESULTS PARAMETERS
        if (input.equals("searchArticle")) {
            try {
                this.webUrl = jsonObject.getString("web_url");
                this.headLine = jsonObject.getJSONObject("headline").getString("main"); // actual headline is within main (under headline)

                // Extract multimedia (thumbnail)
                JSONArray multimedia = jsonObject.getJSONArray("multimedia"); // get array of multimedia

                if (multimedia.length() > 0) { // if multimedia exists
                    JSONObject multimediaJson = multimedia.getJSONObject(0); // get 1st multimedia of JSONArray
                    this.thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url"); // get "url" in this JSONObject and append with NYTimes link
                } else {
                    this.thumbNail = ""; // if no multimedia, initialize thumbNail with empty string
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // INITIALIZE TOP STORIES PARAMETERS
        else if (input.equals("topStory")) {
            try {
                this.webUrl = jsonObject.getString("url");
                this.headLine = jsonObject.getString("title"); // actual headline is within main (under headline)

                // Extract multimedia (thumbnail)
                JSONArray multimedia = jsonObject.getJSONArray("multimedia"); // get array of multimedia

                if (multimedia.length() > 0) { // if multimedia exists
                    JSONObject multimediaJson = multimedia.getJSONObject(0); // get 1st multimedia of JSONArray
                    this.thumbNail = multimediaJson.getString("url"); // get "url" in this JSONObject and append with NYTimes link
                } else {
                    this.thumbNail = ""; // if no multimedia, initialize thumbNail with empty string
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Get a JSONObject from each index of JSONArray
    // Convert each JSONObject into an Article
    // Add Article into array of Articles (named results)
    public static ArrayList<Article> fromJSONArray(JSONArray array) {
        ArrayList<Article> results = new ArrayList<>(); // initialize array

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Article(array.getJSONObject(x), "searchArticle"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    // TOP STORIES ARRAY
    public static ArrayList<Article> fromJSONArrayTopStories(JSONArray array) {
        ArrayList<Article> results = new ArrayList<>(); // initialize array

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Article(array.getJSONObject(x), "topStory"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
