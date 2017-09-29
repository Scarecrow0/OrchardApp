package com.scarecrow.root.orchardapp.data_util;

import android.os.AsyncTask;
import android.util.Log;

import com.scarecrow.root.orchardapp.activties.ActivityMainInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-9-7.
 */

public class PlaceInfoSingle implements Serializable {

    public String orc_name,
            info_url,
            pos_url,
            orc_rules,
            ticket_remain,
            ticket_price,
            orch_info,
            orch_bannernum,
            orch_img_url,
            orch_bref,
            orch_id;
    PlaceInfoSingle this_ = this;


    public PlaceInfoSingle(boolean for_who) {
        //true is for SingleTone, false for List;
        if (for_who)
            new GETOrchardInfo().execute();
    }

    public void parseSingleJSONObj(JSONObject jsonobj, PlaceInfoSingle elem) throws JSONException {

        elem.orc_name = jsonobj.getString("orch_name");
        elem.orc_rules = jsonobj.getString("orch_rules");
        elem.info_url = jsonobj.getString("orch_info");
        elem.pos_url = jsonobj.getString("orch_pos");
        elem.ticket_remain = jsonobj.getString("ticket_remain");
        elem.ticket_price = jsonobj.getString("ticket_price");
        elem.orch_info = jsonobj.getString("orch_info");
        elem.orch_img_url = jsonobj.getString("orch_imgurl");
        elem.orch_bref = jsonobj.getString("orch_bref");
        elem.orch_bannernum = jsonobj.getString("orch_banner_num");
        elem.orch_id = jsonobj.getString("orch_id");

    }

    protected class GETOrchardInfo extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... params) {
            ServerContacter sc = new ServerContacter();
            JSONArray json_arr = null;
            try {
                String res_str
                        = sc.getURLString(ActivityMainInterface.Server_ip + "/app/get_orchard_info", "");
                json_arr = new JSONArray(res_str);
            } catch (Exception ee) {
                Log.d("", "doInBackground: error in : " + ee);
            }
            return json_arr;
        }

        protected void onPostExecute(JSONArray param) {
            try {
                parseSingleJSONObj(param.getJSONObject(0), this_);
            } catch (Exception ee) {
                Log.d(TAG, "processJSON: error in process");
            }
        }

    }
}
