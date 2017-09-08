package com.scarecrow.root.orchardapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private TextView tab_glob, tab_nearby_ochar;
    private FragmentManager mFragmentManager;
    private View fragmentHolder;
    private Fragment[] mTabFragments;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewgroup,
                             Bundle saveInstanceState) {
        v = inflater.inflate(R.layout.fragment_guide_home, viewgroup, false);
        LinearLayout bt[] = new LinearLayout[]{
                v.findViewById(R.id.bottom_chishuiguo),
                v.findViewById(R.id.bottom_guangguoyuan),
                v.findViewById(R.id.bottom_xiaojiangkang)};
        for (int i = 0; i < 3; i++)
            bt[i].setOnClickListener(this);
        fragmentHolder = v.findViewById(R.id.fragment_others_event);
        tab_glob = (TextView) v.findViewById(R.id.tab_switch_global_event);
        tab_glob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab_glob.setBackgroundColor(getResources().getColor(R.color.buttongreen));
                tab_nearby_ochar.setBackgroundColor(Color.WHITE);
                changeTabFragment(1);
            }
        });
        tab_nearby_ochar = (TextView) v.findViewById(R.id.tab_switch_nearby_orchard);
        tab_nearby_ochar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab_nearby_ochar.setBackgroundColor(getResources().getColor(R.color.buttongreen));
                tab_glob.setBackgroundColor(Color.WHITE);
                changeTabFragment(0);
            }
        });
        mTabFragments = initTabFragment();
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

    private Fragment[] initTabFragment() {
        Fragment[] fragments = new Fragment[]{
                new FragmentSurroundingTickets(),
                new FragmentGlobOrchEvent()
        };
        mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_others_event, fragments[0]);
        fragmentTransaction.add(R.id.fragment_others_event, fragments[1]);
        fragmentTransaction.hide(fragments[1]);
        tab_nearby_ochar.setBackgroundColor(getResources()
                .getColor(R.color.buttongreen));
        fragmentTransaction.commit();
        return fragments;

    }

    private void changeTabFragment(int i) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.hide(mTabFragments[0]);
        ft.hide(mTabFragments[1]);
        ft.show(mTabFragments[i]);
        ft.commit();
    }
    @Override
    public void onDestroy(){
        for(AdsBanner ab:mBannerList)
            ab.onDestory();
        super.onDestroy();
    }

}
