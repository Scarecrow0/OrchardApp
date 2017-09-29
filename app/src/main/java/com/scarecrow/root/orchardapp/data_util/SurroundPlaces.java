package com.scarecrow.root.orchardapp.data_util;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-9-8.
 */

public class SurroundPlaces extends PlaceInfoSingle {


    List<PlaceInfoSingle> mPlaces = new ArrayList<>();
    private OnPlacesListReadyListener mPLRL;

    public SurroundPlaces() {
        super(false);
    }

    public void updatePlacesData() {
        new GETSurrPlace().execute();
    }

    public List<PlaceInfoSingle> getmPlaces() {
        return mPlaces;
    }

    public void setOnPlacesListReadyListener(OnPlacesListReadyListener plrl) {
        mPLRL = plrl;
    }

    public interface OnPlacesListReadyListener {
        void onPlacesListReady();
    }

    protected class GETSurrPlace extends GETOrchardInfo {
        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            try {
                Log.d(TAG, "onPostExecute:  GETSurrPlace jsonarray is : " + jsonArray);
                PlaceInfoSingle elem;
                mPlaces.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    elem = new PlaceInfoSingle(false);
                    parseSingleJSONObj(jsonArray.getJSONObject(i), elem);
                    mPlaces.add(elem);
                }
                if (mPLRL != null)
                    mPLRL.onPlacesListReady();

            } catch (Exception ee) {
                Log.e(TAG, "onPostExecute: error in SurroundPlaces parse json : " + ee);
            }
        }
    }

    public PlaceInfoSingle getPlaceInfoById(String ID) {
        for (PlaceInfoSingle each : mPlaces) {
            if (each.orch_id.equals(ID))
                return each;
        }
        return null;
    }
}
