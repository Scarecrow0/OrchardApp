package com.scarecrow.root.orchardapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

/**
 * Created by root on 17-7-2.
 *
 */

public class FruitMainActivity extends AppCompatActivity implements Button.OnClickListener{
    AdsBanner ab;
    Handler UIupdate;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        final Context mc = getBaseContext();
        final FruitRecListAdapter FRLA = new FruitRecListAdapter(getBaseContext());

        UIupdate = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(mc);
                setContentView(R.layout.fruit_main_activity_layout);
                final RecyclerView rv = (RecyclerView) findViewById(R.id.fruit_list);
                ab = (AdsBanner) findViewById(R.id.fruit_banner);
                new BannerLoader().bannerPreparing(3,ab,mc);
                rv.setLayoutManager(layoutManager);
                rv.setAdapter(FRLA);
                rv.addItemDecoration( new DividerItemDecoration(mc,1));
            }
        };
        FRLA.setOnAdapterReadyListenner(new FruitRecListAdapter.AdapterReadyListenner() {
            @Override
            public void onAdapterReady() {
                UIupdate.obtainMessage().sendToTarget();
            }
        });
        FRLA.setmFruitList();

    }


    @Override
    public void onClick(View view) {

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        setContentView(R.layout.empty_fragment);
        if(ab != null)
            ab.onDestory();
        UIupdate.removeCallbacksAndMessages(null);
    }
}
