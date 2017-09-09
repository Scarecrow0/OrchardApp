package com.scarecrow.root.orchardapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-9-8.
 */

public class SurrdPlaceAdapter extends RecyclerView.Adapter<SurrdPlaceAdapter.ViewHolder> {
    Context mContext;
    List<PlaceInfoSingle> mPlaces = new ArrayList<>();

    public SurrdPlaceAdapter(Context context) {
        super();
        mContext = context;

    }

    public void updateData(List<PlaceInfoSingle> Places) {
        mPlaces = Places;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SurrdPlaceAdapter.ViewHolder holder, final int position) {
        holder.PlaceName.setText(mPlaces.get(position).orc_name);
        holder.PlaceBref.setText(mPlaces.get(position).orch_bref);
        holder.PlaceRemain.setText("今日余票数 : " + mPlaces.get(position).ticket_remain);
        Picasso.with(mContext).load(mPlaces.get(position).orch_img_url)
                .into(holder.PlaceImage);
        holder.Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext, OrchardSingleActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("OrchardInfo", mPlaces.get(holder.getAdapterPosition()));
                mContext.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView PlaceName, PlaceBref, PlaceRemain;
        LinearLayout Item;
        ImageView PlaceImage;

        public ViewHolder(View view) {
            super(view);
            PlaceName = view.findViewById(R.id.place_item_name);
            PlaceBref = view.findViewById(R.id.place_item_bref);
            PlaceImage = view.findViewById(R.id.place_item_image);
            PlaceRemain = view.findViewById(R.id.place_item_remain);
            Item = view.findViewById(R.id.place_item_layout);
        }
    }
}
