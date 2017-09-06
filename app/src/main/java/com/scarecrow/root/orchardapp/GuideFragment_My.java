package com.scarecrow.root.orchardapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by root on 17-7-1.
 *
 */

public class GuideFragment_My extends Fragment {
    private final static String ALBUM_PATH
            = Environment.getExternalStorageDirectory() + "/download_test/";
    RecyclerView RV_boughtFruitList,RV_boughtticketList,RV_joinedEvent;
    private View mfragmentview;
    private static int request_code = 0 ;
    //always try to login
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewgroup,
                             Bundle saveInstanceState) {
        final View v = inflater.inflate(R.layout.guide_fragment_my, viewgroup, false);
        mfragmentview = v;
        TextView loginLL =  v.findViewById(R.id.loginreg_interface_button);
        RV_boughtFruitList = v.findViewById(R.id.fruit_bought_list);
        RV_joinedEvent = v.findViewById(R.id.event_joined_list);
        RV_boughtticketList = v.findViewById(R.id.ticket_bought_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RV_boughtFruitList.setLayoutManager(layoutManager);
        layoutManager = new LinearLayoutManager(getContext());
        RV_joinedEvent.setLayoutManager(layoutManager);
        layoutManager = new LinearLayoutManager(getContext());
      //  RV_boughtticketList.setLayoutManager(layoutManager);

        //TODO ticket booking  ,Adapter

        FruitRecListAdapter fra = new FruitRecListAdapter(getContext());
        RV_boughtFruitList.addItemDecoration(new DividerItemDecoration(getContext(),1));
        RV_boughtFruitList.setAdapter(fra);

        EventsRecListAdapter eRLAdapter = new EventsRecListAdapter(getContext());
        RV_joinedEvent.setAdapter(eRLAdapter);
        RV_joinedEvent.addItemDecoration(new DividerItemDecoration(getContext(),1));
        if(MainInterfaceActivity.isLogin) {
            setIntoLoginedState(MainInterfaceActivity.logined_usr);
        }
        loginLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !MainInterfaceActivity.isLogin) {
                    Intent intent = new Intent(getContext(), LoginRegsitActivity.class);
                    startActivityForResult(intent, request_code);
                }
                else {
                    MainInterfaceActivity.isLogin = false;
                    TextView tv = v.findViewById(R.id.loginreg_interface_button);
                    tv.setText("登录/注册");
                }
            }
        });
        Button bt = v.findViewById(R.id.user_info_refresh);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshUserDataShow();
            }
        });

        bt = v.findViewById(R.id.LBS_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(),LBSGuideActivity.class);
                getActivity().startActivity(in);

            }
        });
        return v;
    }
    public void refreshUserDataShow(){
        if (MainInterfaceActivity.isLogin) {
            String[] user = new String[]{MainInterfaceActivity.logined_usr.username,
                    MainInterfaceActivity.logined_usr.password};
            new UserInfoUpdaterRefresh().execute(user);
        } else
            return;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK ) {
            return;
        }
        final UserInfo uinfo = (UserInfo)data.getSerializableExtra("userinfo");
        setIntoLoginedState(uinfo);

    }
    private void setIntoLoginedState(final UserInfo uinfo){
        TextView tv = mfragmentview.findViewById(R.id.loginreg_interface_button);
        tv.setGravity(View.FOCUS_LEFT);
        tv.setText(uinfo.toString());
        final FruitRecListAdapter fra = (FruitRecListAdapter) RV_boughtFruitList.getAdapter();
        fra.setOnAdapterReadyListenner(new FruitRecListAdapter.AdapterReadyListenner() {
            @Override
            public void onAdapterReady() {
                fra.updateFruitlistbyUserInfo(uinfo);
                RV_boughtFruitList.setAdapter(fra);
            }
        });
        fra.setmFruitList();
        //update bought fruitlist

        OrchardEvent oe = new OrchardEvent();
        oe.setOnEventListReadyListener(new OrchardEvent.onEventListReadyListener() {
            @Override
            public void onEventListReady(List<OrchardEvent> oe) {
                EventsRecListAdapter ERLA
                        = (EventsRecListAdapter) RV_joinedEvent.getAdapter();
                ERLA.updateDatabyUInfo(uinfo,oe);
            }
        });
        oe.updateAllEventList();
    }
    private class UserInfoUpdaterRefresh extends UserInfoUpdater{
        @Override
        protected void onPostExecute(String mresult){
            MainInterfaceActivity.UpdateUInfobyJSONstr(mresult);
            setIntoLoginedState(MainInterfaceActivity.logined_usr);
        }
    }
}











/*
       final TextView tv = mfragmentview.findViewById(R.id.request_result_str);
        final ImageView iv = mfragmentview.findViewById(R.id.request_result_img);
        mainthread = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    //update ui
                    case 0:
                        saveFile((Bitmap) msg.obj, "test_2.jpg");
                        iv.setImageBitmap((Bitmap) msg.obj);
                        Log.d(TAG, "handleMessage: update Imageview complete");
                        break;
                    case 1:
                        tv.setText((String)msg.obj);
                        break;
                }
            }
        };

        mresDownloader = new ResDownloader(mainthread);

      set listener method cannot work ,image arg passing failed

        mresDownloader.setResDownloaderStringListener(
                new ResDownloader.ResDownloaderStringListener<TextView>() {
                    @Override
                    public void onResDownloadedstring(TextView tv, String str) {
                        tv.setText(str);
                    }
                });
                new ResDownloader.ResDownloaderImageListener<ImageView>() {
                    @Override
                    public void onResDownloadedImage(ImageView iv, Bitmap bm) {
                        saveFile(bm,"test_2.jpg");
                        iv.setImageBitmap(bm);
                        Log.d(TAG, "handleMessage: update Imageview complete");
                    }
                }
        );

        mresDownloader.start();
        mresDownloader.getLooper();
    Button bt = (Button) v.findViewById(R.id.my_buttom);
        bt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: my_buttom");

         //**********************************************************************
                this is a test section
         //**********************************************************************
                mresDownloader.queueDownload
                        (tv, "http://192.168.0.106:8080/app/hhhhhh", "", 0);
                mresDownloader.queueDownload
                        (iv, "http://192.168.0.106:8080/image/apple1.jpg", "", 1);
                //need to update object , request url
                //post arg, resource type : 0 -> string ,1 -> image

        }
    });






    ////**********************************************************************
    //save file model , very useful while testing downloading
    public void saveFile(Bitmap bm, String fileName) {
        try {
            File dirFile = new File(ALBUM_PATH);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            File myCaptureFile = new File(ALBUM_PATH + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (Exception ee) {
            Log.d(TAG, "saveFile: " + ee);
        }
    }
    */

