package com.scarecrow.root.orchardapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 17-9-3.
 *
 *
 */

public class OrchardSingleActivity extends AppCompatActivity {

    orchardinfo mOrchInfo;
    NumberPicker mBookingAmmountPicker,mBookingDateMonthPicker,mBookingDateDayPicker;
    @Override
    public void onCreate(Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
        setContentView(R.layout.orchard_single_activity);
        new BannerLoader()
                .bannerPreparing
                        (5,(AdsBanner)findViewById(R.id.orchard_single_banner),getBaseContext());
        mBookingAmmountPicker = (NumberPicker) findViewById(R.id.booking_count);
        mBookingDateMonthPicker = (NumberPicker) findViewById(R.id.booking_month);
        mBookingDateDayPicker = (NumberPicker) findViewById(R.id.booking_day);
        mBookingDateMonthPicker.setMaxValue(12);
        mBookingDateDayPicker.setMinValue(1);
        mBookingDateMonthPicker.setMinValue(1);
        mBookingDateDayPicker.setMaxValue(30);
        mBookingAmmountPicker.setMaxValue(50);

        LinearLayout ll = (LinearLayout) findViewById(R.id.orchard_intro_button);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(),OrchardDetailActivity.class);
                in.putExtra("info",mOrchInfo.orch_info);
                startActivity(in);
            }
        });
        ll = (LinearLayout) findViewById(R.id.orchard_pos_button);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(),mOrchInfo.pos_url,Toast.LENGTH_LONG)
                        .show();
            }
        });
        Button bt_buy = (Button) findViewById(R.id.ticket_book_button);
        bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                TODO link up with server  booking count,booking date,
                 0 -> ammount 1 -> date
                 */

                if(!MainInterfaceActivity.isLogin){
                    Toast.makeText
                            (getBaseContext(),"login in before book ticket ", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                String ammount = String.valueOf(mBookingAmmountPicker.getValue());
                String date = String.valueOf(mBookingDateMonthPicker.getValue())
                        + "-" + String.valueOf(mBookingDateDayPicker.getValue());
                String[] params = new String[]{ammount,date};
                new BookingTicket().execute(params);



            }
        });

        new GETOrchardInfo().execute();
    }

    private class BookingTicket extends AsyncTask<String,Void,String>{
        String TAG = "BookingTicket";
        @Override
        protected String doInBackground(String...params){
            String res  = null;
            try{
                ServerContacter sc = new ServerContacter();
                Log.d(TAG, "doInBackground: booking ticket url : "
                            +MainInterfaceActivity.Server_ip+"/app/booking_ticket" +
                            "& username="+MainInterfaceActivity.logined_usr.username+
                                "amount="+params[0]+"est_date="+params[1]);

                res = sc.getURLString(MainInterfaceActivity.Server_ip+"/app/booking_ticket",
                            "username="+MainInterfaceActivity.logined_usr.username+
                            "&amount="+params[0]+"&est_date="+params[1]);


                /*
                result code
                        0  fine
                        1 dont have enough code
                        2 out of date

                 */

            }catch (Exception ee){

                Log.d(TAG, "doInBackground: error in booking upload : " + ee) ;

            }

            return res;
        }
        @Override
        protected void onPostExecute(String res){
            if (res == null) {
                Toast.makeText
                        (getBaseContext(), "booked failed ,cause by connection error", Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
            switch (res){
                case "0":
                    Toast.makeText(getBaseContext(),"booked successed",Toast.LENGTH_SHORT)
                            .show();
                    finish();
                    break;
                case "1":
                    Toast.makeText(getBaseContext(),"dont have enough tickets",Toast.LENGTH_SHORT)
                            .show();
                    finish();
                    break;
                case "2":
                    Toast.makeText(getBaseContext(),"out of date",Toast.LENGTH_SHORT)
                            .show();
                    finish();
                    break;
                default:
                    break;

            }
        }



    }

    private class GETOrchardInfo extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void...params){
            ServerContacter sc = new ServerContacter();
            try {
                String res_str
                        = sc.getURLString(MainInterfaceActivity.Server_ip+"/app/get_orchard_info","");
                processJSON(res_str);
            }catch (Exception ee){
                Log.d("", "doInBackground: error in : " + ee);
            }
            return null;
        }
        protected void onPostExecute(Void param){
            TextView tv_ = (TextView) findViewById(R.id.orchard_ticket_price);
            tv_.setText(mOrchInfo.ticket_price + "￥");
            tv_ = (TextView) findViewById(R.id.orchard_single_name);
            tv_.setText(mOrchInfo.orc_name);
            tv_ = (TextView) findViewById(R.id.orchard_rules);
            tv_.setText(mOrchInfo.orc_rules);
            tv_ = (TextView) findViewById(R.id.orchard_ticket_remain);
            tv_.setText("剩余可入园人数 :"+mOrchInfo.ticket_remain);
        }

        private void processJSON(String res_str) throws JSONException{
            mOrchInfo = new orchardinfo();
            JSONArray json_arr = new JSONArray(res_str);
            JSONObject jsonobj = json_arr.getJSONObject(0);
            mOrchInfo.orc_name = jsonobj.getString("orch_name");
            mOrchInfo.orc_rules = jsonobj.getString("orch_rules");
            mOrchInfo.info_url = jsonobj.getString("orch_info");
            mOrchInfo.pos_url = jsonobj.getString("orch_pos");
            mOrchInfo.ticket_remain = jsonobj.getString("ticket_remain");
            mOrchInfo.ticket_price = jsonobj.getString("ticket_price");
            mOrchInfo.orch_info = jsonobj.getString("orch_info");
        }
    }
    private class orchardinfo{
        public String orc_name,
                info_url,
                pos_url,
                orc_rules,
                ticket_remain,
                ticket_price,
                orch_info;

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        AdsBanner ab = (AdsBanner) findViewById(R.id.orchard_single_banner);
        ab.onDestory();
    }


}
