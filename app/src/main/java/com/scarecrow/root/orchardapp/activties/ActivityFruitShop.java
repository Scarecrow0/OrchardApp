package com.scarecrow.root.orchardapp.activties;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.scarecrow.root.orchardapp.R;
import com.scarecrow.root.orchardapp.data_util.BannerLoader;
import com.scarecrow.root.orchardapp.data_util.FruitsInfo;
import com.scarecrow.root.orchardapp.data_util.ServerContacter;
import com.scarecrow.root.orchardapp.views.ViewPagerAdsBanner;

/**
 * Created by root on 17-8-7.
 *
 */

public class ActivityFruitShop extends AppCompatActivity {
    FruitsInfo fr;
    int buy_num;
    Handler handler;
    ViewPagerAdsBanner ab;
    buyFruitUploader buyer = new buyFruitUploader();
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_fruit_shop);
        Intent in = getIntent();
        ab = (ViewPagerAdsBanner) findViewById(R.id.fruitshop_banner);
        fr = (FruitsInfo) in.getSerializableExtra("clickedFruit");
        new BannerLoader().
                bannerPreparing
                        (fr.bannerimageList,ab,getBaseContext());
        TextView tv = (TextView)findViewById(R.id.fruitshop_price);
        tv.setText(fr.price );
        tv = (TextView)findViewById(R.id.fruitshop_bref);
        tv.setText(fr.bref);
        tv = (TextView)findViewById(R.id.fruitshop_stockremain);
        tv.setText("当前库存: "+fr.stock_rest);
        tv = (TextView)findViewById(R.id.fruitshop_longintro);
        tv.setText(fr.long_intro);
        tv = (TextView)findViewById(R.id.fruitshop_name);
        tv.setText(fr.name);
        final NumberPicker np = (NumberPicker) findViewById(R.id.fruit_buy_num);
        np.setMaxValue(99);
        np.setMinValue(1);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        Button bt = (Button) findViewById(R.id.fruit_buy_button);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                ActivityMainInterface.logined_usr = ActivityMainInterface.UpdateUInfobyJSONstr((String) msg.obj);
                Toast.makeText
                        (getBaseContext(),
                                "预定水果成功 !", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
        };

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buy_num = np.getValue();
                if (!ActivityMainInterface.isLogin) {
                    Toast.makeText
                            (getBaseContext(),
                                    "请登录后购买水果", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                buyer.execute();
            }
        });
        bt = (Button) findViewById(R.id.to_donate_tree_btn);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(), ActivityDonateTree.class);
                startActivity(in);
            }
        });
    }

    private void processJSON(String jsonstr){
        if (jsonstr.equals("1")){
            Toast.makeText
                    (getBaseContext(),
                            "doesn't have enough money", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (jsonstr.equals("2")){
            Toast.makeText
                    (getBaseContext(),
                            "水果库存不足 ", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (jsonstr.equals("None")){
            Toast.makeText
                    (getBaseContext(),
                            "预定失败", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        handler.obtainMessage(0,jsonstr).sendToTarget();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        setContentView(R.layout.fragment_empty);
        buyer.cancel(true);
        if(ab != null)
            ab.onDestory();
    }

    private class buyFruitUploader extends AsyncTask<Void, Void, Void> {
        String feedback_str;

        //update usr info
        @Override
        protected Void doInBackground(Void... params) {
            //usr info update
            try {
                Log.d("TAG", "doInBackground( buy fruit): buying :"
                        + fr.name + buy_num + ActivityMainInterface.logined_usr.username);

                feedback_str = new ServerContacter().getURLString
                        (ActivityMainInterface.Server_ip + "/app/buy_fruit",
                                "fruitname=" + fr.name + "&amount=" + buy_num +
                                        "&username=" + ActivityMainInterface.logined_usr.username);
                Log.d("TAG", "doInBackground(buy fruit): feedback : " + feedback_str);
                processJSON(feedback_str);
            } catch (Exception ee) {
                Log.d("", "onClick: buy fruit " + ee);
            }
            return null;
        }


    }
}
