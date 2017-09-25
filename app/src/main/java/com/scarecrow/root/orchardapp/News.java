package com.scarecrow.root.orchardapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-9-25.
 */

public class News implements Serializable {


    private List<NewsItem> mNewsList_K;
    private List<NewsItem> mNewsList_H;

    public List<NewsItem> getHealthyNews() {
        return mNewsList_H;
    }

    public List<NewsItem> getKnowledgeews() {
        return mNewsList_K;
    }

    public News() {
        mNewsList_K = new ArrayList<>();
        mNewsList_H = new ArrayList<>();
    }

    public void getData() {
        mNewsList_H.clear();
        mNewsList_K.clear();
        new Downloader().execute();
    }

    private class Downloader extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                String res = new ServerContacter().getURLString
                        (MainInterfaceActivity.Server_ip + "/app/get_news_list", "");
                if (res.equals("")) {
                    throw new Exception();
                }
                parseJSON(res);
            } catch (Exception ee) {
                Log.d(TAG, "doInBackground: error when downloading news list" + ee);
            }


            return null;

        }

        @Override
        protected void onPostExecute(Void param) {
            mOnDataReady.onDataReady();
        }

    }

    private void parseJSON(String jsonstr) throws JSONException {
        JSONArray json_arr = new JSONArray(jsonstr);
        for (int i = 0; i < json_arr.length(); i++) {
            JSONObject jo = json_arr.getJSONObject(i);
            NewsItem n = new NewsItem();
            n.title = jo.getString("title");
            n.image_url = jo.getString("image_url");
            n.long_text = jo.getString("long_text");
            n.thumbnail_url = jo.getString("thumbnail_url");
            n.type = jo.getInt("type");
            n.view_times = jo.getInt("view_times");
            n.id = jo.getInt("id");
            switch (n.type) {
                case 0:
                    mNewsList_H.add(n);
                    break;
                case 1:
                    mNewsList_K.add(n);
                    break;
                default:
                    break;

            }


        }
    }

    public interface onDataReadyListenner {
        void onDataReady();
    }

    private onDataReadyListenner mOnDataReady;

    public void setOnDataReadyListenner(onDataReadyListenner odl) {
        mOnDataReady = odl;
    }

}
