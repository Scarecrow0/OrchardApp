package com.scarecrow.root.orchardapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-8-15.
 */

public class LBSGuideActivity extends AppCompatActivity {
    private static String TAG = "In LBS_Service";
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private MapDisplayView mapDisplay;
    private RecyclerView best_event_rv;

    @Override
    public void onCreate(Bundle onSaveInstanceState) {
        super.onCreate(onSaveInstanceState);
        setContentView(R.layout.activity_lbs);
        mapDisplay = (MapDisplayView) findViewById(R.id.map_display);
        mapDisplay.init_canvas(1);

        best_event_rv = (RecyclerView) findViewById(R.id.recommd_event);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getBaseContext());
        best_event_rv.setLayoutManager(lm);
        best_event_rv.setAdapter(new EventsRecListAdapter(getBaseContext()));
        Button bt = (Button) findViewById(R.id.start_update_pos);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainInterfaceActivity.isLogin) {
                    Toast.makeText(getBaseContext(), "请登录后再使用园区导游服务"
                            , Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                mLocationClient.startLocation();
                // with out setting options , one clicked calls one locate;
            }
        });

        bt = (Button) findViewById(R.id.stpo_update_pos);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationClient.stopLocation();
            }
        });

        getPermission();
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                // update the location info on to android here;
                String res = "";
                Double[] params = new Double[]
                        {aMapLocation.getLongitude(),
                                aMapLocation.getLatitude()};
                res += ("longitude : " + String.valueOf(params[0]) + "\n");
                res += ("latitude : " + String.valueOf(params[1]) + "\n");
                res += ("loca_type : " + String.valueOf(aMapLocation.getLocationType() + "\n"));
                TextView tv = (TextView) findViewById(R.id.loc_info);
                tv.setText(res);
                new lbs_request().execute(params);
            }
        });


        AMapLocationClientOption mLocationOption = null;
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        // 2s locate once
        mLocationOption.setLocationCacheEnable(false);
        mLocationClient.setLocationOption(mLocationOption);

    }

    private void processJSON(String jsonstr) {
        OrchardTouristCnt mPartTouristCnt;
        try {
            Log.d(TAG, "processJSON: get LBS res: " + jsonstr);
            JSONObject json_obj = new JSONObject(jsonstr);

            JSONArray jsonArray = json_obj.getJSONArray("tourist_cnt");
            mPartTouristCnt = new OrchardTouristCnt();
            for (int i = 0; i < jsonArray.length(); i++)
                mPartTouristCnt.add(jsonArray.getInt(i));
            //draw dot
            jsonArray = json_obj.getJSONArray("rv_coodi");
            mapDisplay.drawDot(jsonArray.getInt(0), jsonArray.getInt(1));

            //display orchard status

            String str = "";
            str += mPartTouristCnt.toString() ;
            str += "当前所在园区  : 园区" + json_obj.getString("partnum") + "\n";
            str += "//debug \n relative coordinate: x: " + jsonArray.getInt(0) + ",y: " + jsonArray.getInt(1) + "\n";


            String hasBestEvent = json_obj.getString("has_best_event");
            //show best event
            OrchardEvent bestEvent;
            if (hasBestEvent.equals("yes")) {
                bestEvent = new OrchardEvent()
                        .parserEachEventJSON(json_obj.getJSONObject("best_event"));
                str += "best event : " + bestEvent.name + "\n";
                EventsRecListAdapter eventsRecListAdapter
                        = (EventsRecListAdapter) best_event_rv.getAdapter();
                List<OrchardEvent> list_or = new ArrayList<>();
                list_or.add(bestEvent);
                eventsRecListAdapter.updateData(list_or);

            } else
                str += "best event : " + "None" + "\n";
            TextView tv = (TextView) findViewById(R.id.curr_orch_info);
            tv.setText(str);
        } catch (Exception ee) {
            Log.d("lbsActivity : ", "processJSON: errro : " + ee);
        }

    }

    private void getPermission() {
        List<String> permissionList = new ArrayList<>();
       /*
          // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission)
                */
        if (PermissionChecker.checkSelfPermission
                (getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);

        }
        if (PermissionChecker.checkSelfPermission
                (getApplicationContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);

        }
        if (PermissionChecker.checkSelfPermission
                (getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else
            Toast.makeText(getBaseContext(), "已获得所有所需权限"
                    , Toast.LENGTH_SHORT)
                    .show();

    }

    @Override

    public void onRequestPermissionsResult(int requestCode, String[] permissions
            , int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText
                                    (getBaseContext(), "需要获得所需的权限", Toast.LENGTH_SHORT)
                                    .show();
                            finish();
                            return;
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapDisplay.onDestroy();
        mLocationClient.onDestroy();
    }

    private class lbs_request extends AsyncTask<Double, Void, String> {
        @Override
        protected String doInBackground(Double... params) {
            String res_str = null;
            try {
                res_str = new ServerContacter()
                        .getURLString(MainInterfaceActivity.Server_ip + "/app/lbs_request"
                                , "pos_x=" + params[0] + "&pos_y=" + params[1] + "&orchardnum=" + 1
                                        + "&username=" + MainInterfaceActivity.logined_usr.username);
            } catch (Exception ee) {
                Log.d("LBSGuideActivity", "doInBackground: error in lbs activity" + ee);
            }
            return res_str;
        }

        @Override
        protected void onPostExecute(String params) {
            if (params == null) {
                Toast.makeText(getBaseContext(), "获取坐标时出错!", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (params.equals("coodination invaild")) {
                Toast.makeText(getBaseContext(), "坐标非法，您正处在园区外，无法使用园区内导航!", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            processJSON(params);
        }
    }

    private class OrchardTouristCnt extends ArrayList<Integer>{
        @Override
        public String toString(){
            String str = "园区各区域人数 :";
            for(int i = 0;i < this.size();i++){
                str += "园区" + i+1 +": " + this.get(i) + ",";
            }
            str += "\n";
            return str;
        }

    }

}
