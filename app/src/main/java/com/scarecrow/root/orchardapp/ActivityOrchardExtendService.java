package com.scarecrow.root.orchardapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by root on 17-9-19.
 */

public class ActivityOrchardExtendService extends AppCompatActivity {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_orchard_extend_service);
        Intent intent = getIntent();
        int type = intent.getIntExtra("type", 0);
        FragmentManager fm = getSupportFragmentManager();
        // 1 -> event 2 -> hotel service
        if (type == 1) {
            ImageView header = (ImageView) findViewById(R.id.extend_activity_header_iv);
            header.setImageDrawable(getResources().getDrawable(R.mipmap.event_header));
            FragmentTransaction tf = fm.beginTransaction();
            tf.add(R.id.extend_activity_fragment, new FragmentGlobOrchEvent());
            tf.commit();
        } else if (type == 2) {
            ImageView header = (ImageView) findViewById(R.id.extend_activity_header_iv);
            header.setImageDrawable(getResources().getDrawable(R.mipmap.hotel_service_header));
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.extend_activity_fragment, new FragmentSurroundingTickets());
            ft.commit();
        }
    }
}
