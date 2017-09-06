package com.scarecrow.root.orchardapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-8-5.
 *
 * load the banner
 */

public class BannerLoader {
    private String TAG = "BannerLoader";
    private String mUrlrequest;
    //in order to open ads web page;
    private JSONArray jsonobj;
    private int mBannerNum;
    private List<String> mImageurlList;
    private List<String> mAdsurlList;
    private WeakReference<Context> mContext;
    private WeakReference<AdsBanner> mTargetBanner;
    bannerimageJSONrequest mBm;

    private  class bannerimageJSONrequest extends AsyncTask<Void, Integer, Void> {
        private String JSONstring;
        @Override
        protected Void doInBackground(Void... params) {
            ServerContacter sc = new ServerContacter();
            try {
                Log.d(TAG, "doInBackground: get json str");
                JSONstring = sc.getURLString(mUrlrequest + mBannerNum, "");
                proccessJSONString(JSONstring);
            } catch (Exception ee) {
                Log.d(TAG, "doInBackground: error in get JSON");
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer...params){
            Log.d(TAG, "onProgressUpdate: now progress : " + params[0]);
        }
        @Override
        protected void onPostExecute(Void param) {
            AdsBannerAdapter aba_ontarget = (AdsBannerAdapter) mTargetBanner.get().getAdapter();
            if (aba_ontarget == null){
                mTargetBanner.get().setAdapter(new AdsBannerAdapter
                        (mContext.get(), mImageurlList, mAdsurlList));
            }
            Log.d(TAG, "onPostExecute: set adapter");
            mTargetBanner.get().start();
        }
    }
    public BannerLoader() {
        mUrlrequest = MainInterfaceActivity.Server_ip + "/app/banner_url/";
        mImageurlList = new ArrayList<>();
        mAdsurlList = new ArrayList<>();
        mBm = new bannerimageJSONrequest();
    }

    public void bannerPreparing(int bannernum,AdsBanner target,Context context) {
        mBannerNum = bannernum;
        mContext = new WeakReference<>(context);
        mTargetBanner = new WeakReference<AdsBanner>(target);
        mBm.execute();
    }
    public void bannerPreparing(List<String> images,AdsBanner target,Context context) {
        mContext = new WeakReference<>(context);
        mTargetBanner = new WeakReference<>(target);
        mImageurlList = images;
        mAdsurlList = new ArrayList<>();
        mTargetBanner.get().setAdapter(new AdsBannerAdapter
                (mContext.get(), mImageurlList, mAdsurlList));
    }

    private void proccessJSONString(String JSONstring) {
        try {
            Log.d(TAG, "proccessJSONString: decoding jsonstr to obj");
            jsonobj = new JSONArray(JSONstring);
            Log.d(TAG, "proccessJSONString: json download complete ");
            for (int i = 0; i < jsonobj.length(); i++) {
                mImageurlList.add(jsonobj.getJSONObject(i).getString("image_url"));
                mAdsurlList.add(jsonobj.getJSONObject(i).getString("ads_url"));
                Log.d(TAG, "proccessJSONString: get image url" + mImageurlList.get(i));
            }
            Log.d(TAG, "proccessJSONString: done!");
        } catch (Exception ee) {
            Log.d(TAG, "proccessJSONString: " + ee);
        }
    }

    public List<String> getBannerimageUrlList() {
        return mImageurlList;
    }

    public List<String> getBanneradsUrlList() {
        return mAdsurlList;
    }
}
