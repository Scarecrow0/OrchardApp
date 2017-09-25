package com.scarecrow.root.orchardapp;

import android.content.Context;
import android.content.Intent;
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
 * Created by root on 17-9-25.
 */

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {

    WeakReference<Context> mContext;
    List<NewsItem> mNewsList = new ArrayList<>();

    public NewsRecyclerViewAdapter(Context context) {
        super();
        mContext = new WeakReference<Context>(context);
    }

    public void updateDataSet(List<NewsItem> newdata) {
        mNewsList = newdata;
        notifyDataSetChanged();
    }


    @Override
    public NewsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsRecyclerViewAdapter.ViewHolder holder, int position) {
        NewsItem currNews = mNewsList.get(position);
        holder.title.setText(currNews.title);
        holder.type.setText(getTypeString(currNews.type));
        Picasso.with(mContext.get())
                .load(currNews.thumbnail_url)
                .into(holder.image);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                processClick(mNewsList.get(pos));
            }
        });
    }

    private void processClick(NewsItem ni) {
        Intent in = new Intent(mContext.get(), NewsSingleActivity.class);
        in.putExtra("clicked_news", ni);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.get().startActivity(in);
    }

    private String getTypeString(int type) {
        switch (type) {
            case 0:
                return mContext.get().getResources().getString(R.string.jiangkangzhishi);
            case 1:
                return mContext.get().getResources().getString(R.string.kupushizhishi);
            default:
                return "";
        }
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, type;
        ImageView image;
        View item;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.news_item_title);
            type = view.findViewById(R.id.news_item_type);
            image = view.findViewById(R.id.news_item_image);
            item = view;
        }
    }
}