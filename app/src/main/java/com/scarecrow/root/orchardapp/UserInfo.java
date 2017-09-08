package com.scarecrow.root.orchardapp;

import android.util.Log;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-8-8.
 *
 */

public class UserInfo implements Serializable{
    public String username, address,currorchard,password;
    public int account, exp,curr_part;
    public List<StringPair> FruitboughtList = new ArrayList<>();
    //first is fruit name ,next is bought amount
    public List<StringPair> TicketboughtList = new ArrayList<>();
    // first
    public List<StringPair> EventjoinedList = new ArrayList<>();
    // first is event name ,second is finish state and date ;
    // append finish state as a alphabet at next_str first letter;
    //       a is unfinish , b is finished
    // pop it from the front when it need to be processed or display

    //public postion coodinate;


    public String toString() {
        return "用户名: " + username + "\n"
                + "余额: " + account + '\n'
                + "优惠等级: " + exp + '\n'
                + "地址: " + address + '\n';
    }

    public void setAll(String u, String ad,String or, int ac, int e) {
        username = u;
        currorchard = or;
        address = ad;
        account = ac;
        exp = e;
    }

    public void setFruitboughtListbyJSON(JSONArray fruitjson) {
        try {
            Log.d(TAG, "setFruitboughtListbyJSON: bought fruit json : " + fruitjson);
            FruitboughtList.clear();
            JSONArray jsana ;
            for (int i = 0; i < fruitjson.length(); i++){
                jsana = fruitjson.getJSONArray(i);
                FruitboughtList
                        .add(new StringPair(jsana.getString(0),jsana.getString(1)));
            }
        } catch (Exception ee) {
            Log.e(TAG, "setFruitboughtListbyJSON: errro : " + ee);
        }
    }
    public void setTicketboughtListbyJSON(JSONArray ticketjson){
        try{
            Log.d(TAG, "setTicketboughtListbyJSON: Ticket bought json : " + ticketjson);
            TicketboughtList.clear();
            StringPair tmp_pair;
            JSONArray tmp_jobj;
            for (int i = 0;i < ticketjson.length();i++){
                tmp_jobj = ticketjson.getJSONArray(i);
                tmp_pair = new StringPair
                        (tmp_jobj.getString(0),tmp_jobj.getString(1));
                TicketboughtList.add(tmp_pair);
            }
        }catch (Exception ee){
            Log.e(TAG, "setTicketboughtListbyJSON: " + ee );
        }
    }
    public void setEventjoinedListbyJSON(JSONArray fruitjson) {
        try {
            Log.d(TAG, "setEventjoinedListbyJSON: bought fruit json : " + fruitjson);
            EventjoinedList.clear();
            JSONArray jsana ;
            for (int i = 0; i < fruitjson.length(); i++){
                jsana = fruitjson.getJSONArray(i);
                EventjoinedList
                        .add(new StringPair(jsana.getString(0),jsana.getString(1)));
            }
        } catch (Exception ee) {
            Log.e(TAG, "setEventjoinedListbyJSON: errro : " + ee);
        }
    }
    //// TODO: 17-8-13  location update

}

