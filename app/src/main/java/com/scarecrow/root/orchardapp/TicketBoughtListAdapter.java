package com.scarecrow.root.orchardapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-9-7.
 */

public class TicketBoughtListAdapter extends RecyclerView.Adapter<TicketBoughtListAdapter.ViewHolder> {
    private List<StringPair> mTicketList = new ArrayList<>();
    private WeakReference<Context> mContext;
    public TicketBoughtListAdapter(Context context){
        mContext = new WeakReference<>(context);
        if(MainInterfaceActivity.isLogin)
            mTicketList = MainInterfaceActivity.logined_usr.TicketboughtList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mTicketImage;
        TextView mTicketName,mTicketEstTime,mTicketAmount;
        public ViewHolder(View view){
            super(view);
            mTicketImage = view.findViewById(R.id.ticket_list_image);
            mTicketName = view.findViewById(R.id.ticket_list_orchname);
            mTicketEstTime  = view.findViewById(R.id.ticket_list_esttime);
            mTicketAmount = view.findViewById(R.id.ticket_list_tourist_amount);

        }

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_list_item_layout,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTicketName.setText("园区基本门票");
        String str_tmp = "入园人数 : " + mTicketList.get(position).getMfirst();
        holder.mTicketAmount
                .setText(str_tmp);
        Picasso.with(mContext.get())
                .load(MainInterfaceActivity.TheOrchard.orch_img_url)
                .into(holder.mTicketImage);
        holder.mTicketEstTime
                .setText(appendTicketState(mTicketList.get(position).getMnext()));

    }
    @Override
    public int getItemCount(){
        return mTicketList.size();
    }

    private String appendTicketState(String RawStr){
        String Res ;
        if(RawStr.charAt(0) == 'a'){
            Res = "未使用 预定入园时间: " + RawStr.substring(1);
        }else {
            Res = "已使用 入园时间: " + RawStr.substring(1);
        }
        return  Res;

    }

    public void updataData(List<StringPair> TicketBoughtList){
        mTicketList = TicketBoughtList;
        notifyDataSetChanged();
    }

}