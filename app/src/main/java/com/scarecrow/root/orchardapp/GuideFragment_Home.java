package com.scarecrow.root.orchardapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * Created by root on 17-7-1.
 *
 */

public class GuideFragment_Home extends Fragment implements Button.OnClickListener {

    View v;
    private List<AdsBanner> mBannerList = new ArrayList<>();
    private List<BannerLoader> mBannLoadList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewgroup,
                             Bundle saveInstanceState) {
        v = inflater.inflate(R.layout.guide_fragment_home, viewgroup, false);
        LinearLayout bt[] = new LinearLayout[]{
                v.findViewById(R.id.bottom_chishuiguo),
                v.findViewById(R.id.bottom_guangguoyuan),
                v.findViewById(R.id.bottom_xiaojiangkang)};
        for (int i = 0; i < 3; i++)
            bt[i].setOnClickListener(this);

        initBannerList();
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_chishuiguo:
                Intent it = new Intent(getContext(), FruitMainActivity.class);
                startActivity(it);
                break;
            case R.id.bottom_guangguoyuan:
                Log.d(TAG, "onClick: guangguoyuan");
                startActivity(new Intent(getContext(),OrchardMainActivity.class));
                break;
            case R.id.bottom_xiaojiangkang:
                break;
            default:
                break;
        }
    }

    /**
     * four banner order:
     * 0:mBannerHeader,
     * 1:mBannerOrchard,,
     * 2:mBannerFruit
     * 3:mBannerHealthy;
     */

    private void initBannerList() {
        mBannerList.clear();
        Log.d(TAG, "initBannerList: init banner list");
        mBannerList.add((AdsBanner) v.findViewById(R.id.main_ads_header));
        mBannerList.add((AdsBanner) v.findViewById(R.id.main_ads_orchard));
        mBannerList.add((AdsBanner) v.findViewById(R.id.main_ads_fruit));
        mBannerList.add((AdsBanner) v.findViewById(R.id.main_ads_healthy));
        for (int i = 0; i < mBannerList.size(); i++) {
            Log.d(TAG, "initBannerList: preparing banner loader");
            new BannerLoader().bannerPreparing(i,mBannerList.get(i),getContext());
        }

    }
    @Override
    public void onDestroy(){
        for(AdsBanner ab:mBannerList)
            ab.onDestory();
        super.onDestroy();
    }

}
