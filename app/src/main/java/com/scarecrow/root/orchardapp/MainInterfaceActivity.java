package com.scarecrow.root.orchardapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.leakcanary.RefWatcher;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-7-1.
 * V0.0.1
 */

public class MainInterfaceActivity extends AppCompatActivity implements Button.OnClickListener{
    public static String Server_ip = "http://47.94.227.86:8080/appserver";
    public static UserInfo logined_usr = new UserInfo();
    public static boolean isLogin = false;
    private int curr_page = 0;
    public static PlaceInfoSingle TheOrchard = new PlaceInfoSingle(true);
    public static OrchardEvent orchardEvent = new OrchardEvent();
    public static SurroundPlaces surroundPlaces = new SurroundPlaces();
    public static boolean isGetEventList = false;
    List<Fragment> mFraList;
    TextView tv[];


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main_interface);
        String[] param  = new String[]{
                "aaa123","aaa"
        };
        RefWatcher refWatcher = LeakDetecter.getRefWatcher(getApplication());
        refWatcher.watch(this);
        new AutoLogin().execute(param);
        surroundPlaces.updatePlacesData();

        mFraList = new ArrayList<>();
        //init guide bar buttoms
        LinearLayout ll[] = new LinearLayout[]{
                (LinearLayout) findViewById(R.id.zhuyebuttom),
                (LinearLayout) findViewById(R.id.faxianbuttom),
                (LinearLayout) findViewById(R.id.wodebuttom)};
        tv = new TextView[]{
                (TextView) findViewById(R.id.zhuye_tv),
                (TextView) findViewById(R.id.faxian_tv),
                (TextView) findViewById(R.id.wode_tv) };

        for (int i = 0; i < 3; i++) {
            ll[i].setOnClickListener(this);
        }
        //init fragment
        mFraList.add(new GuideFragment_Home());
        mFraList.add(new GuideFragment_Discover());
        mFraList.add(new GuideFragment_My());
        initFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zhuyebuttom:
                changeFragment(0);
                break;
            case R.id.faxianbuttom:
                changeFragment(1);
                break;
            case R.id.wodebuttom:
                changeFragment(2);
                break;
            default:
                break;
        }
    }

    public void changeFragment(int fragment) {
        //fragment change method
        if (fragment == 2) {
            GuideFragment_My gfm = (GuideFragment_My) mFraList.get(fragment);
            gfm.refreshUserDataShow();
        }
        FragmentManager fragmentmanager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentmanager.beginTransaction();
        transaction.hide(mFraList.get(0));
        transaction.hide(mFraList.get(1));
        transaction.hide(mFraList.get(2));
        transaction.show(mFraList.get(fragment));
        curr_page = fragment;
        transaction.commit();
        for (int i = 0; i < 3; i++)
            tv[i].setTextColor(Color.BLACK);
        tv[fragment].setTextColor(Color.WHITE);

    }

    private void initFragment(){
        FragmentManager fragmentmanager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentmanager.beginTransaction();
        transaction.replace(R.id.guide_fragment,mFraList.get(0));
        transaction.add(R.id.guide_fragment, mFraList.get(1));
        transaction.add(R.id.guide_fragment, mFraList.get(2));
        transaction.hide(mFraList.get(0));
        transaction.hide(mFraList.get(1));
        transaction.hide(mFraList.get(2));
        tv[0].setTextColor(Color.WHITE);
        transaction.show(mFraList.get(0));
        curr_page = 0;
        transaction.commitAllowingStateLoss();
    }

    private void cleanFragment() {
        FragmentManager fragmentmanager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentmanager.beginTransaction();
        for (Fragment fra : mFraList) {
            transaction.remove(fra);
        }
        transaction.add(R.id.guide_fragment, new EmptyFragment());
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "MainInterface onPause: ");
        cleanFragment();
    }
    @Override
    public void onRestart() {
        super.onRestart();
        initFragment();
        Log.d(TAG, "MainInterface onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        //   initFragment();
        changeFragment(curr_page);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    private class AutoLogin extends UserInfoUpdater{
        @Override
        protected void onPostExecute(String mresult){
            if (!mresult.equals("None")) {
                Toast.makeText
                        (getBaseContext(), "login success! login automaticlly", Toast.LENGTH_SHORT)
                        .show();
                logined_usr = MainInterfaceActivity.UpdateUInfobyJSONstr(mresult);
                isLogin = true;
                cancel(false);
                return;
            }
            Toast.makeText
                    (getBaseContext(), "login failed! please check your username and password", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    static public UserInfo UpdateUInfobyJSONstr(String JSONstr) {
        UserInfo uinfo = MainInterfaceActivity.logined_usr;
        try {
            Log.d(TAG, "UpdateUInfobyJSONstr: new usrinfo: " + JSONstr);
            JSONObject jsonObject = new JSONObject(JSONstr);
            uinfo.setAll
                    (jsonObject.getString("username"),
                            jsonObject.getString("address"),
                            jsonObject.getString("currorchard"),
                            jsonObject.getInt("account"),
                            jsonObject.getInt("exp"));
            uinfo.password = jsonObject.getString("password");
            uinfo.curr_part = jsonObject.getInt("curr_part");
            uinfo.setFruitboughtListbyJSON(jsonObject.getJSONArray("boughtfruit"));
            uinfo.setTicketboughtListbyJSON(jsonObject.getJSONArray("boughtticket"));
            uinfo.setEventjoinedListbyJSON(jsonObject.getJSONArray("eventjoined"));
            Log.d(TAG, "UpdateUInfobyJSONstr: update usrinfo success ");
        } catch (Exception ee) {
            Log.d(TAG, "UpdateUInfobyJSONstr: " + ee);
        }
        return uinfo;
    }

}
