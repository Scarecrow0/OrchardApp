package com.scarecrow.root.orchardapp.activties;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scarecrow.root.orchardapp.R;
import com.scarecrow.root.orchardapp.adapters.AdapterFruitRecList;
import com.scarecrow.root.orchardapp.adapters.DividerItemDecoration;
import com.scarecrow.root.orchardapp.data_util.BannerLoader;
import com.scarecrow.root.orchardapp.views.ViewPagerAdsBanner;

/**
 * Created by root on 17-7-2.
 *
 */

public class ActivityFruitMain extends AppCompatActivity {
    ViewPagerAdsBanner ab;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        final Context mc = getBaseContext();
        setContentView(R.layout.activity_fruit_main);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mc);
        final RecyclerView rv = (RecyclerView) findViewById(R.id.fruit_list);
        ab = (ViewPagerAdsBanner) findViewById(R.id.fruit_banner);
        new BannerLoader().bannerPreparing(1, ab, mc);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(mc, 1));
        AdapterFruitRecList FRLA = new AdapterFruitRecList(getBaseContext());
        rv.setAdapter(FRLA);
        FRLA.setmFruitList();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        setContentView(R.layout.fragment_empty);
        if(ab != null)
            ab.onDestory();
    }
}
