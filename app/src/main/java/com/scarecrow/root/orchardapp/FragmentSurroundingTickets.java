package com.scarecrow.root.orchardapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 17-9-8.
 */

public class FragmentSurroundingTickets extends Fragment {
    View mView;
    PlacesSingleTon mSurrPlace;
    private RecyclerView RV_SurrdPlace;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewgroup,
                             Bundle saveInstanceState) {
        mView = inflater.inflate(R.layout.fragment_surrounding_tickets, viewgroup, false);
        RV_SurrdPlace = mView.findViewById(R.id.recycleview_surrounding_tickets);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        RV_SurrdPlace.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        RV_SurrdPlace.setLayoutManager(lm);
        RV_SurrdPlace.setAdapter(new SurrdPlaceAdapter(getContext()));
        PlacesSingleTon.getInstance();
        mSurrPlace.setOnPlacesListReadyListener(new PlacesSingleTon.OnPlacesListReadyListener() {
            @Override
            public void onPlacesListReady() {
                SurrdPlaceAdapter spa = (SurrdPlaceAdapter) RV_SurrdPlace.getAdapter();
                spa.updateData(PlacesSingleTon.getInstance().getDataForSurrdPlace());
            }
        });
        mSurrPlace.updatePlacesData();
        return mView;
    }


}
