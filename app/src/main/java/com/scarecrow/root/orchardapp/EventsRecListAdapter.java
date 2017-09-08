package com.scarecrow.root.orchardapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-8-11.

 */

public class EventsRecListAdapter extends RecyclerView.Adapter<EventsRecListAdapter.ViewHolder> {
    private List<OrchardEvent> mEventList;
    private WeakReference<Context> mContext;
    private static Intent getIntent2EventDetailActivity(Context context, OrchardEvent clickedEvent){
        Intent in = new Intent(context,EventDetailActivity.class);
        in.putExtra("clickedEvent",clickedEvent);
        return in;
    }

    public EventsRecListAdapter (Context context){
        mContext = new WeakReference<Context>(context);
        mEventList = new ArrayList<>();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,bref,date;
        ImageView image;
        View item ;
        public ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.event_item_name);
            bref = view.findViewById(R.id.event_item_bref);
            date = view.findViewById(R.id.event_occur_orchard_n_date);
            image = view.findViewById(R.id.event_item_image);
            item = view;
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventsRecListAdapter.ViewHolder holder, int position){
        holder.name.setText(mEventList.get(position).name);
        holder.bref.setText(mEventList.get(position).bref);
        holder.date.setText(mEventList.get(position).date);
        Picasso.with(mContext.get()).load(mEventList.get(position).img_url)
                .into(holder.image);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onItemClicked(mEventList.get(holder.getAdapterPosition()));

            }
        });
    }
    /*
    can be overwrite when extents ,
    extent to shop activity,and my_fragment
    */
    protected void onItemClicked(OrchardEvent oe){
        Log.d(TAG, "onClick: click"+oe.name);
        Intent in = getIntent2EventDetailActivity
                (mContext.get(),oe);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.get().startActivity(in);
        //add intent ,open the fruit detail activty;
    }
    @Override
    public int getItemCount(){

        return  mEventList.size();
    }

    public void updateData(List<OrchardEvent> eventlist){
        mEventList.clear();
        mEventList = eventlist;
        notifyDataSetChanged();
    }
    private List<OrchardEvent> newEvnetList ;

    public void updateDatabyUInfo(UserInfo uinfo,List<OrchardEvent> oe_list){
        List<String> joinedUserList;
        mEventList = oe_list;
        String curr_user = MainInterfaceActivity.logined_usr.username;
        newEvnetList = new LinkedList<>();
        for (int i = 0;i < mEventList.size();i++){
            joinedUserList = mEventList.get(i).joineduser;
            for (int j = 0;j < joinedUserList.size();j++ ){
                if(joinedUserList.get(j).equals(curr_user)){
                    OrchardEvent new_oe = (new OrchardEvent(mEventList.get(i)));
                    Log.d(TAG, "updateDatabyUInfo: add joined evnent" + new_oe.name);
                    append_finish_state( MainInterfaceActivity.logined_usr,new_oe);
                    break;
                }
            }
        }

        updateData(newEvnetList);
    }

    private void append_finish_state(UserInfo curr_usr,OrchardEvent event_item){
        List<StringPair> joined_booklist = curr_usr.EventjoinedList;
        StringPair usr_event_state = null;
        for(int i = 0;i < joined_booklist.size();i++){
            if(joined_booklist.get(i).getMfirst().equals(event_item.name)){
                usr_event_state = joined_booklist.get(i);
                Log.d(TAG, "append_finish_state: get user event state" + usr_event_state);
                String event_state,EventJoinedDate ;
                if (usr_event_state.getMnext().charAt(0) == 'a'){
                    event_state = "状态:未完成";
                    EventJoinedDate = "参加活动时间: " + usr_event_state.getMnext().substring(1);
                }else {
                    event_state = "状态:已完成";
                    EventJoinedDate = "参加活动时间: " + usr_event_state.getMnext().substring(1);
                }
                event_item.bref = event_state;
                event_item.date = EventJoinedDate;
                newEvnetList.add(event_item);
            }
        }

    }
}
