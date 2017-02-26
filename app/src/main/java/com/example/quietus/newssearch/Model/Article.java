package com.example.quietus.newssearch.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Quietus on 2017/2/24.
 */
@Parcel
public class Article {
    public String headline;
    public String webUrl;
    public String thumbnail;

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public Article(){

    }

    public Article(JSONObject jsonObject) {
        try{
            webUrl = jsonObject.getString("web_url");
            headline = jsonObject.getJSONObject("headline").getString("main");
            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() > 0){
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                thumbnail = "http://www.nytimes.com/" + multimediaJson.getString("url");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJson(JSONArray jsonArray){
        ArrayList<Article> articleArrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            try{
                articleArrayList.add(new Article(jsonArray.getJSONObject(i)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return articleArrayList;
    }
}
