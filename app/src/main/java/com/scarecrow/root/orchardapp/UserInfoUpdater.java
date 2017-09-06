package com.scarecrow.root.orchardapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-8-21.
 * in order to update user info
 */

public class UserInfoUpdater extends AsyncTask<String,Void,String> {

        private String mresult;
        private JSONObject jsonObject;
        @Override
        @SafeVarargs
        final protected String doInBackground(String...params){
            try {
                jsonObject = new JSONObject();
                jsonObject.put("username",params[0]);
                jsonObject.put("password",params[1]);
                ServerContacter sc = new ServerContacter();
                Log.d(TAG, "doInBackground: upload new userinfo " + jsonObject);
                mresult = sc.getURLString(MainInterfaceActivity.Server_ip+"/app/login","userjson="+jsonObject.toString());
                Log.d(TAG, "doInBackground: get userinfo back : "  + mresult);

            }catch (Exception ee){
                Log.e(TAG, "doInBackground: in login get error:"+ ee );
                return "None";
            }
            return mresult;
        }
}
