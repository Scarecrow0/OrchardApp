package com.scarecrow.root.orchardapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by root on 17-7-2.
 *
 */

public class FruitMainActivity extends AppCompatActivity {
    AdsBanner ab;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        final Context mc = getBaseContext();
        setContentView(R.layout.activity_fruit_main);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mc);
        final RecyclerView rv = (RecyclerView) findViewById(R.id.fruit_list);
        ab = (AdsBanner) findViewById(R.id.fruit_banner);
        new BannerLoader().bannerPreparing(1, ab, mc);
        rv.setLayoutManager(layoutManager);
        FruitRecListAdapter FRLA = new FruitRecListAdapter(getBaseContext());
        rv.setAdapter(FRLA);
        rv.addItemDecoration(new DividerItemDecoration(mc, 1));
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
