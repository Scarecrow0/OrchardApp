package com.scarecrow.root.orchardapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-9-8.
 */

public class PlacesSingleTon {
    private List<PlaceInfo> mPlaces;
    static private PlacesSingleTon singleTon = new PlacesSingleTon();

    //TODO singleton

    public static PlacesSingleTon getInstance() {
        return singleTon;
    }

    public List<PlaceInfo> getDataForSurrdPlace() {
        if (mPlaces == null)
            // updatePlacesData();
            List<PlaceInfo> res = new ArrayList<>();
        for (int i = 1; i < mPlaces.size(); i++)
            res.add(mPlaces.get(i));
        return res;
    }


    private class Place {
        public PlaceInfo getDataMainOrchard() {
            if (mPlaces == null)
                updatePlacesData();
            return new PlaceInfo(mPlaces.get(0));
        }


        public void updatePlacesData() {
            mPlaces = new ArrayList<>();
            new GETPlacesInfo().execute();
        }

        private OnPlacesListReadyListener mPLRL;

        public void setOnPlacesListReadyListener(OnPlacesListReadyListener plrl) {
            mPLRL = plrl;
        }

        public interface OnPlacesListReadyListener {
            void onPlacesListReady();
        }


        private class GETPlacesInfo extends AsyncTask<Void, Void, JSONArray> {

            @Override
            protected JSONArray doInBackground(Void... params) {
                ServerContacter sc = new ServerContacter();
                JSONArray json_arr = null;
                try {
                    String res_str
                            = sc.getURLString(MainInterfaceActivity.Server_ip + "/app/get_orchard_info", "");
                    json_arr = new JSONArray(res_str);
                } catch (Exception ee) {
                    Log.d("", "doInBackground: error in : " + ee);
                }
                return json_arr;
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                try {
                    Log.d(TAG, "onPostExecute:  GETSurrPlace jsonarray is : " + jsonArray);
                    PlaceInfo elem;
                    for (int i = 1; i < jsonArray.length(); i++) {
                        elem = new PlaceInfo();
                        parseSingleJSONObj(jsonArray.getJSONObject(i), elem);
                        mPlaces.add(elem);
                    }
                    mPLRL.onPlacesListReady();
                    cancel(false);
                } catch (Exception ee) {
                    Log.e(TAG, "onPostExecute: error in PlacesSingleTon parse json : " + ee);
                }
            }

        }

        private void parseSingleJSONObj(JSONObject jsonobj, PlaceInfo elem) throws JSONException {

            elem.orc_name = jsonobj.getString("orch_name");
            elem.orc_rules = jsonobj.getString("orch_rules");
            elem.pos_url = jsonobj.getString("orch_pos");
            elem.ticket_remain = jsonobj.getString("ticket_remain");
            elem.ticket_price = jsonobj.getString("ticket_price");
            elem.orch_info = jsonobj.getString("orch_info");
            elem.orch_img_url = jsonobj.getString("orch_imgurl");
            elem.orch_bref = jsonobj.getString("orch_bref");
            elem.orch_bannernum = jsonobj.getString("orch_banner_num");
            elem.orch_id = jsonobj.getString("orch_id");

        }

    }


}
