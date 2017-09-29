package com.scarecrow.root.orchardapp.activties;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scarecrow.root.orchardapp.R;
import com.scarecrow.root.orchardapp.data_util.BannerLoader;
import com.scarecrow.root.orchardapp.data_util.OrchardEvent;
import com.scarecrow.root.orchardapp.data_util.ServerContacter;
import com.scarecrow.root.orchardapp.data_util.StringPair;
import com.scarecrow.root.orchardapp.views.ViewPagerAdsBanner;

import java.util.List;

/**
 * Created by root on 17-8-12.

 */

public class ActivityEventDetail extends AppCompatActivity {
    OrchardEvent or;
    Handler after_join;
    ViewPagerAdsBanner mEventBanner;
    @Override
    public void onCreate(Bundle onSaveinstanteState){
        super.onCreate(onSaveinstanteState);
        setContentView(R.layout.activity_event_detail);
        Intent in = getIntent();
        mEventBanner = (ViewPagerAdsBanner) findViewById(R.id.event_banner);
        or = (OrchardEvent) in.getSerializableExtra("clickedEvent");
        new BannerLoader()
                .bannerPreparing
                        (or.bannerList,mEventBanner,this);
        TextView tv = (TextView) findViewById(R.id.event_name);
        tv.setText(or.name);
        tv = (TextView) findViewById(R.id.event_target);
        tv.setText(or.target);
        tv = (TextView) findViewById(R.id.event_publish_orchard_n_date);
        tv.setText( or.orchardname + " : " + or.date);
        tv = (TextView) findViewById(R.id.event_bref);
        tv.setText(or.bref);
        tv = (TextView)findViewById(R.id.event_longintro);
        tv.setText(or.long_intro);
        ListView lv = (ListView) findViewById(R.id.event_joineduser_list);
        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<String>
                (getBaseContext(), R.layout.item_user_list, or.joineduser);
        lv.setAdapter(arrayAdapter);
        Button bt = (Button) findViewById(R.id.event_join_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityMainInterface.isLogin) {
                    if (check_joined(or)) {
                        Toast.makeText(getBaseContext(), "参加任务失败! 请勿重复参加任务!", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    new join_event().execute();
                } else
                    Toast.makeText(getBaseContext(), "参加任务失败! 请登录后参加任务!", Toast.LENGTH_SHORT)
                            .show();

            }
        });
        after_join = new Handler(){
            @Override
            public void handleMessage(Message msg){
                Toast.makeText(getBaseContext(), "参加任务成功 !", Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        };

    }

    private boolean check_joined(OrchardEvent oe) {
        List<StringPair> user_joined = ActivityMainInterface.logined_usr.EventjoinedList;
        for (int i = 0; i < user_joined.size(); i++) {
            if (user_joined.get(i).getMfirst().equals(oe.name)
                    && user_joined.get(i).getMnext().equals((oe.date))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEventBanner.onDestory();
    }

    private class join_event extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void...params){
            try {
                Log.d("Join_Event", "doInBackground: join the event" );
                String res_str = new ServerContacter()
                        .getURLString(ActivityMainInterface.Server_ip + "/app/join_the_event",
                                "username=" + ActivityMainInterface.logined_usr.username
                                +"&eventname="+or.name
                                        + "&currorchard=" + ActivityMainInterface.logined_usr.currorchard);
                //Log.d("", "doInBackground: get new user info after join event : \n" + res_str);
                ActivityMainInterface.UpdateUInfobyJSONstr(res_str);
            }catch (Exception ee){
                Log.d("Join_event", "doInBackground: error in join event " + or.name);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param){
            after_join.obtainMessage().sendToTarget();
        }
    }

}
