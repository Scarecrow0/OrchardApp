package com.scarecrow.root.orchardapp.activties;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.scarecrow.root.orchardapp.R;
import com.scarecrow.root.orchardapp.data_util.BannerLoader;
import com.scarecrow.root.orchardapp.views.ViewPagerAdsBanner;

/**
 * Created by root on 17-9-5.
 */

public class ActivityOrchardInfo extends AppCompatActivity {
    ViewPagerAdsBanner adsBanner;
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_orchard_detail);

        adsBanner = (ViewPagerAdsBanner) findViewById(R.id.orchard_detail_banner);
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
