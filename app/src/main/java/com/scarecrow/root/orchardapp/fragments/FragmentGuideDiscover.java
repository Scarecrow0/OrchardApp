package com.scarecrow.root.orchardapp.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scarecrow.root.orchardapp.R;
import com.scarecrow.root.orchardapp.activties.ActivityDonateTree;
import com.scarecrow.root.orchardapp.activties.ActivityMainInterface;
import com.scarecrow.root.orchardapp.adapters.AdapterEventsRecList;
import com.scarecrow.root.orchardapp.adapters.AdapterFruitRecList;
import com.scarecrow.root.orchardapp.adapters.DividerItemDecoration;
import com.scarecrow.root.orchardapp.data_util.BannerLoader;
import com.scarecrow.root.orchardapp.data_util.OrchardEvent;
import com.scarecrow.root.orchardapp.data_util.ServerContacter;
import com.scarecrow.root.orchardapp.views.ViewPagerAdsBanner;

import org.json.JSONObject;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-7-1.
 *
 */

public class FragmentGuideDiscover extends Fragment {
    ViewPagerAdsBanner adsBanner;
    View v;
    RecyclerView rcmFruitList;
    RecyclerView rcmEventList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewgroup,
                             Bundle saveInstanceState){
        v = inflater.inflate(R.layout.fragment_guide_discover, viewgroup, false);
        adsBanner = v.findViewById(R.id.discover_banner);
        new BannerLoader().bannerPreparing(1,adsBanner,getContext());
        rcmFruitList = v.findViewById(R.id.discover_recview_recmdfrt);
        rcmEventList = v.findViewById(R.id.discover_recview_recmdevent);
        rcmEventList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcmEventList.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        rcmEventList.setAdapter(new AdapterEventsRecList(getContext()));

        rcmFruitList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcmFruitList.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        rcmFruitList.setAdapter(new AdapterFruitRecList(getContext()));

        return v;

    }


    private class GetDonateInfoFromServer extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                String res = new ServerContacter()
                        .getURLString(ActivityMainInterface.Server_ip + "/app/get_donate_info", "");
                JSONObject json_obj = new JSONObject(res);
                return json_obj;

            } catch (Exception ee) {
                Log.d(TAG, "doInBackground: error in get donate num : " + ee);
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json_obj) {
            try {
                TextView tv = v.findViewById(R.id.donate_block_amount);
                tv.setText(String.valueOf(json_obj.getDouble("sum_up")));
                tv = v.findViewById(R.id.donate_block_apple_amount);
                tv.setText(String.valueOf(json_obj.getInt("now_level")));
            } catch (Exception ee) {
                Log.d(TAG, "onPostExecute: error in process got data in donate tree activity : " + ee);
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adsBanner.onDestory();
    }

    @Override
    public void onResume() {
        super.onResume();

        final OrchardEvent oe = new OrchardEvent();
        oe.setOnEventListReadyListener(new OrchardEvent.onEventListReadyListener() {
            @Override
            public void onEventListReady(List<OrchardEvent> eventList) {
                AdapterEventsRecList era = (AdapterEventsRecList) rcmEventList.getAdapter();
                oe.randomKickEventListItem();
                era.updateData(oe.mEventList);
            }
        });
        oe.updateAllEventList();

        final AdapterFruitRecList fra
                = (AdapterFruitRecList) rcmFruitList.getAdapter();
        fra.setOnAdapterReadyListenner(new AdapterFruitRecList.AdapterReadyListenner() {
            @Override
            public void onAdapterReady() {
                fra.randomKickFruitListItem();
                rcmFruitList.setAdapter(fra);
                fra.notifyDataSetChanged();
            }
        });
        fra.setmFruitList();

        new GetDonateInfoFromServer().execute();
        LinearLayout ll = v.findViewById(R.id.donate_tree_block);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), ActivityDonateTree.class);
                startActivity(in);
            }
        });
    }
}
