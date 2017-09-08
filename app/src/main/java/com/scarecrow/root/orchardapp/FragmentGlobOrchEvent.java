package com.scarecrow.root.orchardapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-9-8.
 */

public class FragmentGlobOrchEvent extends Fragment {
    View mView;
    OrchardEvent mOE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewgroup,
                             Bundle saveInstanceState) {
        mView = inflater.inflate(R.layout.fragment_glob_orch_event, viewgroup, false);
        final RecyclerView RV_GlobOrchEvent
                = (RecyclerView) mView.findViewById(R.id.recycleview_glob_orch);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        RV_GlobOrchEvent.setLayoutManager(lm);
        RV_GlobOrchEvent.setAdapter(new EventsRecListAdapter(getContext()));
        RV_GlobOrchEvent.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        mOE = new OrchardEvent();
        mOE.setOnEventListReadyListener(new OrchardEvent.onEventListReadyListener() {
            @Override
            public void onEventListReady(List<OrchardEvent> eventList) {
                EventsRecListAdapter era = (EventsRecListAdapter) RV_GlobOrchEvent.getAdapter();
                List<OrchardEvent> EventList = new ArrayList<OrchardEvent>();
                for (OrchardEvent each : mOE.mEventList)
                    if (each.orchardname.equals("global"))
                        EventList.add(each);
                era.updateData(EventList);
            }
        });
        mOE.updateAllEventList();
        return mView;
    }
}
