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
    private List<String[]> mTicketList = new ArrayList<>();
    private WeakReference<Context> mContext;
    boolean isSpan = false, isForBookedList = false;
    public TicketBoughtListAdapter(Context context){
        mContext = new WeakReference<>(context);
        if(MainInterfaceActivity.isLogin)
            mTicketList = MainInterfaceActivity.logined_usr.TicketboughtList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket_list, parent, false);
        return new ViewHolder(view);
    }

    //   todo update ticket name
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String[] userinfo = mTicketList.get(position);
        PlaceInfoSingle pis = MainInterfaceActivity.surroundPlaces.getPlaceInfoById(userinfo[2]);
        holder.mTicketName.setText(pis.orc_name);
        String str_tmp = "入园人数 : " + userinfo[0];
        holder.mTicketAmount
                .setText(str_tmp);
        Picasso.with(mContext.get())
                .load(pis.orch_img_url)
                .into(holder.mTicketImage);
        holder.mTicketEstTime
                .setText(appendTicketState(userinfo[1]));

    }

    @Override
    public int getItemCount(){
        if (isForBookedList) {
            if (isSpan || mTicketList.size() <= 3)
                return mTicketList.size();
            else
                return 3;
        }
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

    public void updataData(List<String[]> TicketBoughtList) {
        mTicketList = TicketBoughtList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mTicketImage;
        TextView mTicketName, mTicketEstTime, mTicketAmount;

        public ViewHolder(View view) {
            super(view);
            mTicketImage = view.findViewById(R.id.ticket_list_image);
            mTicketName = view.findViewById(R.id.ticket_list_orchname);
            mTicketEstTime = view.findViewById(R.id.ticket_list_esttime);
            mTicketAmount = view.findViewById(R.id.ticket_list_tourist_amount);

        }

    }

}
