package com.scarecrow.root.orchardapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-8-6.
 */

public class Fruit implements Serializable{
    public String image_url,price,name,bref,long_intro,stock_rest,boughtamount;
    public List<String> bannerimageList;
    public  List<Fruit> FruitList ;
    private  ProcessCompleteListener mPCListenner;
    public Fruit(){}
    public Fruit (Fruit fruit){
        this.name = fruit.name;
        this.image_url = fruit.image_url;
        this.price = fruit.price;
        this.bref = fruit.bref;
        this.long_intro = fruit.long_intro;
        this.stock_rest = fruit.stock_rest;
        this.boughtamount = fruit.boughtamount;

        this.FruitList = fruit.FruitList;
        this.bannerimageList = fruit.bannerimageList;
    }

    public interface ProcessCompleteListener{
        void onProcessComplete(List<Fruit> frlist);
    }
    public void setOnProcessCompleteListener(
            ProcessCompleteListener pcl){
        mPCListenner = pcl;
    }

    public void loadFruitList(){
        Fruitinfo getFruitinf = new Fruitinfo();
        getFruitinf.execute();
    }

    private class Fruitinfo extends AsyncTask<Void,Void,Void> {
        String json_str;
        @Override
        protected Void doInBackground(Void...Params){
            ServerContacter sc = new ServerContacter();
            try {
                json_str = sc.getURLString(MainInterfaceActivity.Server_ip + "/app/getfruitlist","");
                Log.d(TAG, "doInBackground: get json back" + json_str);
                FruitList = new ArrayList<>();
                processJSON(new JSONArray(json_str));
            }catch (Exception ee){
                Log.d(TAG, "doInBackground: error = " + ee );
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            try {
                mPCListenner.onProcessComplete(FruitList);
            }catch (Exception ee){
                Log.d(TAG, "onPostExecute: error in create json" + ee);
            }

        }
        private void processJSON(JSONArray jsonarr){
            try {
                Fruit tmpfruit;
                JSONObject tmpjo;
                JSONArray tmpja;
                for(int i = 0;i < jsonarr.length();i++){
                    tmpjo = jsonarr.getJSONObject(i);
                    tmpfruit = new Fruit();
                    tmpfruit.name = tmpjo.getString("name");
                    tmpfruit.bref = tmpjo.getString("bref");
                    tmpfruit.image_url = tmpjo.getString("image_url");
                    tmpfruit.long_intro = tmpjo.getString("long_intro");
                    tmpfruit.price = tmpjo.getString("price") + "￥";
                    tmpfruit.stock_rest = tmpjo.getString("stock_rest");
                    tmpja = tmpjo.getJSONArray("bannerimageList");
                    tmpfruit.bannerimageList = new ArrayList<>();
                    for (int j = 0;j < tmpja.length();j++){
                        tmpfruit.bannerimageList.add(tmpja.getString(j));
                    }
                    FruitList.add(tmpfruit);
                }
                Log.d(TAG, "download fruit list processJSON: done! ");

            }catch (Exception ee){
                Log.d(TAG, "onPostExecute: error in process json(download fruit list)" + ee);
            }
        }

    }
}
