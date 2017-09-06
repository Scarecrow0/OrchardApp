package com.scarecrow.root.orchardapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by root on 17-9-5.
 */

public class OrchardDetailActivity extends AppCompatActivity {
    AdsBanner adsBanner;
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.orchard_detail_activity);

        adsBanner = (AdsBanner)  findViewById(R.id.orchard_detail_banner);
        new BannerLoader().bannerPreparing(5,adsBanner,this);
        String str_info = getIntent().getStringExtra("info");
        Log.d("", "onCreate: str_info" + str_info);
        TextView tv = (TextView ) findViewById(R.id.orchard_detail_info);
        tv.setText(str_info);

    }
    @Override
    public  void onDestroy(){
        adsBanner.onDestory();
        super.onDestroy();
    }
}
