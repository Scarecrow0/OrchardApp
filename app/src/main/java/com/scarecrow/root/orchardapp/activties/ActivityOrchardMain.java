package com.scarecrow.root.orchardapp.activties;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.scarecrow.root.orchardapp.R;
import com.scarecrow.root.orchardapp.adapters.AdapterEventsRecList;
import com.scarecrow.root.orchardapp.adapters.DividerItemDecoration;
import com.scarecrow.root.orchardapp.data_util.BannerLoader;
import com.scarecrow.root.orchardapp.data_util.OrchardEvent;
import com.scarecrow.root.orchardapp.views.ViewPagerAdsBanner;

import java.util.List;

/**
 * Created by root on 17-8-11.
 *
 */

public class ActivityOrchardMain extends AppCompatActivity {
    private RecyclerView mEventRv;
    public void onCreate(Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
        setContentView(R.layout.activity_orchard_main);
        new BannerLoader()
                .bannerPreparing(2, (ViewPagerAdsBanner) findViewById(R.id.orchard_banner), this);
        mEventRv = (RecyclerView) findViewById(R.id.event_list);
        //mOrchardRv.findViewById(R.id.orchard_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        mEventRv.setLayoutManager(layoutManager);
        mEventRv.addItemDecoration(new DividerItemDecoration(getBaseContext(),1));
        //dont forget set the LayoutManager
        mEventRv.setAdapter(new AdapterEventsRecList(this));

        OrchardEvent oe = new OrchardEvent();
        oe.setOnEventListReadyListener(new OrchardEvent.onEventListReadyListener(){
            @Override
            public void onEventListReady(List<OrchardEvent> oe_curr) {
                AdapterEventsRecList ERLA = (AdapterEventsRecList) mEventRv.getAdapter();
                ERLA.updateData(oe_curr);
            }
        });
        oe.updateEventList();
        Button bt = ( Button) findViewById(R.id.orchard_single_entrybutton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(), ActivityOrchardDetail.class);
                in.putExtra("OrchardInfo", ActivityMainInterface.surroundPlaces.getmPlaces().get(0));
                startActivity(in);
            }
        });
        bt = (Button) findViewById(R.id.LBS_activity_entry);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(), ActivityLBSGuide.class);
                startActivity(in);
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ViewPagerAdsBanner ab = (ViewPagerAdsBanner) findViewById(R.id.orchard_banner);
        ab.onDestory();
    }
}
