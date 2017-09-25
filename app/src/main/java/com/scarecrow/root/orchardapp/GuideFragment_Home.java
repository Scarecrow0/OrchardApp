package com.scarecrow.root.orchardapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;


/**
 * Created by root on 17-7-1.
 *
 */

public class GuideFragment_Home extends Fragment implements Button.OnClickListener {

    View v;
    private ImageView tab_glob, tab_nearby_ochar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewgroup,
                             Bundle saveInstanceState) {
        v = inflater.inflate(R.layout.fragment_guide_home, viewgroup, false);
        ImageView bt[] = new ImageView[]{
                v.findViewById(R.id.home_img_button_fruit),
                v.findViewById(R.id.home_img_button_orchard),
                v.findViewById(R.id.home_img_button_healthy)};
        for (int i = 0; i < 3; i++)
            bt[i].setOnClickListener(this);

        ImageView header = v.findViewById(R.id.home_img_view_top);

        tab_glob = v.findViewById(R.id.tab_switch_global_event);
        tab_glob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), ActivityOrchardExtendService.class);
                in.putExtra("type", 1);
                startActivity(in);

            }
        });
        tab_nearby_ochar = v.findViewById(R.id.tab_switch_nearby_orchard);
        tab_nearby_ochar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), ActivityOrchardExtendService.class);
                in.putExtra("type", 2);
                startActivity(in);
            }
        });
        Picasso.with(getContext()).load(R.mipmap.h2).into(bt[0]);
        Picasso.with(getContext()).load(R.mipmap.h4).into(bt[1]);
        Picasso.with(getContext()).load(R.mipmap.h3).into(bt[2]);
        Picasso.with(getContext()).load(R.mipmap.h5).into(tab_nearby_ochar);
        Picasso.with(getContext()).load(R.mipmap.h7).into(tab_glob);
        /*
        bt[0].setImageDrawable(getResources().getDrawable(R.drawable.h2));
        bt[1].setImageDrawable(getResources().getDrawable(R.drawable.h4));
        bt[2].setImageDrawable(getResources().getDrawable(R.drawable.h3));
        header.setImageDrawable(getResources().getDrawable(R.drawable.h1));
        tab_glob.setImageDrawable(getResources().getDrawable(R.drawable.h5));
        tab_nearby_ochar.setImageDrawable(getResources().getDrawable(R.drawable.h7));
       */
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_img_button_fruit:
                Intent it = new Intent(getContext(), FruitMainActivity.class);
                startActivity(it);
                break;
            case R.id.home_img_button_orchard:
                Log.d(TAG, "onClick: guangguoyuan");
                startActivity(new Intent(getContext(),OrchardMainActivity.class));
                break;
            case R.id.home_img_button_healthy:
                startActivity(new Intent(getContext(), ActivityHealthyMain.class));
                break;
            default:
                break;
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
