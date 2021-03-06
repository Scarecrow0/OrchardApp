package com.scarecrow.root.orchardapp.data_util;

import android.os.AsyncTask;
import android.util.Log;

import com.scarecrow.root.orchardapp.activties.ActivityMainInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-8-11.

 *
 */

public class OrchardEvent implements Serializable{
    public String img_url,name,bref,long_intro,date,target,orchardname;
    public List<String> bannerList,joineduser;
    public List<OrchardEvent> mEventList;
    private onEventListReadyListener mERL;
    // get curr orchard event



    public OrchardEvent(){

    }

    public OrchardEvent(OrchardEvent oe){
        img_url = oe.img_url;
        name = oe.name;
        bref = oe.bref;
        long_intro = oe.long_intro;
        date = oe.date;
        target = oe.target;
        orchardname = oe.orchardname;
        bannerList = oe.bannerList;
        joineduser = oe.joineduser;

    }

    public void updateEventList() {
        //set on a listener first
        //then execute
        new getEventList().execute(1);
    }

    public void updateAllEventList() {
        new getEventList().execute(1);
    }
    public interface onEventListReadyListener{
        void onEventListReady(List<OrchardEvent> eventList);
    }
    public void setOnEventListReadyListener(onEventListReadyListener erl){
        mERL =erl;
    }

    private class getEventList extends AsyncTask<Integer,Void,Void>{
        @Override
        protected Void doInBackground(Integer...params){
            try{
                String request_url;
                if(params[0] == 1)
                    request_url = ActivityMainInterface.Server_ip + "/app/get_curr_eventlist";
                else
                    request_url = ActivityMainInterface.Server_ip + "/app/get_all_eventlist";

                String str_res = new ServerContacter()
                        .getURLString(request_url
                                , "");
                if(str_res != null){
                    processJSON(str_res);
                }

            }catch (Exception ee){
                Log.d(TAG, "doInBackground: error in download event list" + ee);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void param){
            if (mERL != null)
                mERL.onEventListReady(mEventList);
            cancel(false);
        }

        private void processJSON(String json_str) throws JSONException{
            mEventList = new ArrayList<>();
            JSONArray Jsonarr = new JSONArray(json_str);
            for(int i = 0;i < Jsonarr.length();i++){
                mEventList.add(parserEachEventJSON(Jsonarr.getJSONObject(i)));
            }

        }

    }

    public void randomKickEventListItem() {
        Random rm = new Random(100);
        for (int i = 0; i < rm.nextInt(5); i++) {
            int index = rm.nextInt(50);
            mEventList.remove(index % mEventList.size());
        }
    }


    public OrchardEvent parserEachEventJSON (JSONObject item)throws JSONException{
        OrchardEvent tmp_event = new OrchardEvent();
        tmp_event.bref = item.getString("bref");
        tmp_event.date = item.getString("date");
        tmp_event.img_url = item.getString("image_url");
        tmp_event.name = item.getString("name");
        tmp_event.orchardname = item.getString("orchard_name");
        tmp_event.long_intro = item.getString("long_intro");
        tmp_event.target = item.getString("target");

        JSONArray tmp_jarr = item.getJSONArray("banner_imagelist");
        tmp_event.bannerList = new ArrayList<>();
        for (int j = 0;j < tmp_jarr.length();j++){
            tmp_event.bannerList.add(tmp_jarr.getString(j));
        }

        tmp_jarr = item.getJSONArray("joined_user");
        tmp_event.joineduser = new ArrayList<>();
        for (int j = 0;j< tmp_jarr.length();j++)
            tmp_event.joineduser.add(tmp_jarr.getString(j));
        return tmp_event;
    }

}
