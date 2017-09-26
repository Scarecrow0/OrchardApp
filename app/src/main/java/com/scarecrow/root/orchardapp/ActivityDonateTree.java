package com.scarecrow.root.orchardapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-9-26.
 */

public class ActivityDonateTree extends AppCompatActivity {
    ViewDonateTree mTreeView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_donate_tree);
        mTreeView = (ViewDonateTree) findViewById(R.id.donate_tree_draw_view);
        mTreeView.spanApplesCoordinations(20);
    }

    private class GetDonateInfoFromServer extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                String res = new ServerContacter()
                        .getURLString(MainInterfaceActivity.Server_ip + "/app/get_donate", "");
                JSONObject json_obj = new JSONObject(res);
                return json_obj;

            } catch (Exception ee) {
                Log.d(TAG, "doInBackground: error in get donate num : " + ee);
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json_obj) {
            try {
                mTreeView.spanApplesCoordinations(json_obj.getInt("now_level"));
                TextView info = (TextView) findViewById(R.id.donate_tree_curr_state);
                String info_str =
                        "距离长出下一个爱心果的爱心捐款额为 : " + json_obj.getDouble("update_bounder");
                info.setText(info_str);
            } catch (Exception ee) {
                Log.d(TAG, "onPostExecute: error in process got data in donate tree activity : " + ee);
            }

        }
    }
}
