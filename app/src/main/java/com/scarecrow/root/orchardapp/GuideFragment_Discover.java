package com.scarecrow.root.orchardapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by root on 17-7-1.
 */

public class GuideFragment_Discover extends Fragment implements Button.OnClickListener{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewgroup,
                             Bundle saveInstanceState){
        View v = inflater.inflate(R.layout.fragment_guide_discover, viewgroup, false);
        AdsBanner adsBanner = v.findViewById(R.id.discover_banner);
        new BannerLoader().bannerPreparing(1,adsBanner,getContext());
        final RecyclerView rcmFruitList
                = v.findViewById(R.id.discover_recview_recmdfrt);
        rcmFruitList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcmFruitList.addItemDecoration(new DividerItemDecoration(getContext(),1));
        rcmFruitList.setAdapter(new FruitRecListAdapter(getContext()));
        final FruitRecListAdapter fra
                = (FruitRecListAdapter) rcmFruitList.getAdapter();
        fra.setOnAdapterReadyListenner(new FruitRecListAdapter.AdapterReadyListenner() {
            @Override
            public void onAdapterReady() {
                fra.randomKickFruitListItem();
                rcmFruitList.setAdapter(fra);
                fra.notifyDataSetChanged();
            }
        });
        fra.setmFruitList();
        return v;

    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            default:
                break;
        }
    }
}
