package com.scarecrow.root.orchardapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 17-9-7.
 */

public class OrchardInfoSingle {

    public String orc_name,
            info_url,
            pos_url,
            orc_rules,
            ticket_remain,
            ticket_price,
            orch_info,
            orch_img_url;

    public OrchardInfoSingle() {
        new GETOrchardInfo().execute();
    }

    public void processJSON(String res_str) throws JSONException {
        JSONArray json_arr = new JSONArray(res_str);
        JSONObject jsonobj = json_arr.getJSONObject(0);
        orc_name = jsonobj.getString("orch_name");
        orc_rules = jsonobj.getString("orch_rules");
        info_url = jsonobj.getString("orch_info");
        pos_url = jsonobj.getString("orch_pos");
        ticket_remain = jsonobj.getString("ticket_remain");
        ticket_price = jsonobj.getString("ticket_price");
        orch_info = jsonobj.getString("orch_info");
        orch_img_url = jsonobj.getString("orch_imgurl");
    }

    private class GETOrchardInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ServerContacter sc = new ServerContacter();
            try {
                String res_str
                        = sc.getURLString(MainInterfaceActivity.Server_ip + "/app/get_orchard_info", "");
                processJSON(res_str);
            } catch (Exception ee) {
                Log.d("", "doInBackground: error in : " + ee);
            }
            return null;
        }

    }
}
