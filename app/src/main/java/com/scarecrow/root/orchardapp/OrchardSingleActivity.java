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

/**
 * Created by root on 17-9-3.
 *
 *
 */

public class OrchardSingleActivity extends AppCompatActivity {

    PlaceInfoSingle mOchardInfo;
    AsyncTask mBookExecuter = new BookingTicket();
    NumberPicker mBookingAmmountPicker,mBookingDateMonthPicker,mBookingDateDayPicker;
    @Override
    public void onCreate(Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
        setContentView(R.layout.activity_orchard_single);
        mOchardInfo = (PlaceInfoSingle) getIntent().getSerializableExtra("OrchardInfo");
        int bannernum = new Integer(mOchardInfo.orch_bannernum);
        new BannerLoader()
                .bannerPreparing
                        (bannernum, (AdsBanner) findViewById(R.id.orchard_single_banner), getBaseContext());
        mBookingAmmountPicker = (NumberPicker) findViewById(R.id.booking_count);
        mBookingDateMonthPicker = (NumberPicker) findViewById(R.id.booking_month);
        mBookingDateDayPicker = (NumberPicker) findViewById(R.id.booking_day);
        mBookingDateMonthPicker.setMaxValue(12);
        mBookingDateDayPicker.setMinValue(1);
        mBookingDateMonthPicker.setMinValue(1);
        mBookingDateDayPicker.setMaxValue(30);
        mBookingAmmountPicker.setMaxValue(50);

        mBookingAmmountPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mBookingDateDayPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mBookingDateMonthPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        TextView tv_ = (TextView) findViewById(R.id.orchard_ticket_price);
        tv_.setText(mOchardInfo.ticket_price + "￥");
        tv_ = (TextView) findViewById(R.id.orchard_single_name);
        tv_.setText(mOchardInfo.orc_name);
        tv_ = (TextView) findViewById(R.id.orchard_rules);
        tv_.setText(mOchardInfo.orc_rules);
        tv_ = (TextView) findViewById(R.id.orchard_ticket_remain);
        tv_.setText("剩余可入园人数 :" + mOchardInfo.ticket_remain);


        LinearLayout ll = (LinearLayout) findViewById(R.id.orchard_intro_button);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(),OrchardDetailActivity.class);
                in.putExtra("info", mOchardInfo.orch_info);
                startActivity(in);
            }
        });
        ll = (LinearLayout) findViewById(R.id.orchard_pos_button);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), mOchardInfo.pos_url, Toast.LENGTH_LONG)
                        .show();
            }
        });
        Button bt_buy = (Button) findViewById(R.id.ticket_book_button);
        bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                 0 -> ammount 1 -> date
                 */

                if(!MainInterfaceActivity.isLogin){
                    Toast.makeText
                            (getBaseContext(), "请登陆后订票!!", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                String ammount = String.valueOf(mBookingAmmountPicker.getValue());
                String date = String.valueOf(mBookingDateMonthPicker.getValue())
                        + "-" + String.valueOf(mBookingDateDayPicker.getValue());
                String[] params = new String[]{ammount, date, mOchardInfo.orch_id};
                Object[] objs = params;
                if (mBookExecuter.isCancelled()) {
                    mBookExecuter = new BookingTicket();
                }
                mBookExecuter.execute(objs);

            }
        });

    }

    @Override
    public void onDestroy() {
        mBookExecuter.cancel(true);
        AdsBanner ab = (AdsBanner) findViewById(R.id.orchard_single_banner);
        ab.onDestory();
        super.onDestroy();
    }

    private class BookingTicket extends AsyncTask<String, Void, Void> {
        String TAG = "BookingTicket";
        String res = null;
        @Override
        protected Void doInBackground(String... params) {
            try{
                ServerContacter sc = new ServerContacter();
                Log.d(TAG, "doInBackground: booking ticket url : "
                            +MainInterfaceActivity.Server_ip+"/app/booking_ticket" +
                            "& username="+MainInterfaceActivity.logined_usr.username+
                        "amount=" + params[0] + "est_date=" + params[1] + "orch_name=" + params[2]);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("username=");
                stringBuilder.append(MainInterfaceActivity.logined_usr.username);
                stringBuilder.append("&amount=");
                stringBuilder.append(params[0]);
                stringBuilder.append("&est_date=");
                stringBuilder.append(params[1]);
                stringBuilder.append("&orch_id=");
                stringBuilder.append(params[2]);
                res = sc.getURLString(MainInterfaceActivity.Server_ip + "/app/booking_ticket", stringBuilder.toString());
                /*
                result code
                        0  fine
                        1 dont have enough code
                        2 out of date

                 */

            }catch (Exception ee){
                cancel(true);
                Log.d(TAG, "doInBackground: error in booking upload : " + ee) ;

            }

            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            if (res.equals("")) {
                Toast.makeText
                        (getBaseContext(), "门票预定失败，可能是网路连接失败", Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
            switch (res){
                case "0":
                    Toast.makeText(getBaseContext(), "门票预定成功", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                    break;
                case "1":
                    Toast.makeText(getBaseContext(), "  余票不足 ", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                    break;
                case "2":
                    Toast.makeText(getBaseContext(), "预定日期晚于当前日期 预定失败", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                    break;
                default:
                    break;

            }
        }



    }

}
