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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-8-6.
 *
 */

public class FruitRecListAdapter extends RecyclerView.Adapter<FruitRecListAdapter.ViewHolder> {
    private List<Fruit> mFruitList = new ArrayList<>();
    private Context mContext;
    private AdapterReadyListenner mARl;
    private boolean isForBroughtList = false;
    //Fruit --> imageurl ,all kinds of text;


    public  FruitRecListAdapter(Context context){
        super();
        mContext = context;
    }

    public static Intent getIntent2FruitshopActivity(Context context, Fruit clickedFruit) {
        //intent to "buy_fruit_activity"
        Intent in = new Intent(context, FruitShopActivity.class);
        in.putExtra("clickedFruit", clickedFruit);
        return in;
    }

    public void setmFruitList(){
        Fruit fr = new Fruit();
        fr.setOnProcessCompleteListener(new Fruit.ProcessCompleteListener() {
            @Override
            public void onProcessComplete(List<Fruit> frlist) {
                Log.d(TAG, "onProcessComplete: onProcessComplete trigged");
                mFruitList = frlist;
                mARl.onAdapterReady();
            }
        });
        fr.loadFruitList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fruit_list, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        final Fruit fr = mFruitList.get(position);
        //load each item in holder(list item)
        //download image ..set text;
        if(!isForBroughtList)
            holder.fruitPrice.setText(fr.price);
        else {
            holder.fruitPrice.setText("购买数量: " + fr.boughtamount);
        }
        holder.fruitBref.setText(fr.bref);
        holder.fruitName.setText(fr.name);
        Picasso.with(mContext)
                .load(fr.image_url)
                .into(holder.fruitImage);
        Log.d(TAG, "onBindViewHolder: load image: " + fr.image_url);
        holder.fruitItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            onItemClicked(mFruitList.get(holder.getAdapterPosition()));
            }
        });
    }
    /*
    can be overwrite when extents ,
    extent to shop activity,and my_fragment
     */
    protected void onItemClicked(Fruit fr){
        Log.d(TAG, "onClick: click"+fr.name);
        Intent in = getIntent2FruitshopActivity
                (mContext,fr);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(in);
        //add intent ,open the fruit detail activty;
    }

    public void setOnAdapterReadyListenner(AdapterReadyListenner ARL){

        mARl = ARL;
    }

    @Override
    public int getItemCount(){
        return mFruitList.size();
    }

    public void randomKickFruitListItem(){
        Random rm = new Random(100);
        for (int i = 0;i < 1;i++){
            int index = rm.nextInt(50);
            Log.d(TAG, "randomKickFruitListItem: kick num : " + index);
            mFruitList.remove(index%mFruitList.size());
        }
    }

    public void updateFruitlistbyUserInfo(UserInfo Uinfo){
        isForBroughtList = true;
        List<StringPair> fruitboughtList = Uinfo.FruitboughtList;
        List<Fruit> newFruitList = new ArrayList<>();
        String fruit_name;
        String bought_num;
        Fruit iterFruitItem;
        for(int i = 0;i < fruitboughtList.size();i++){
            fruit_name = fruitboughtList.get(i).getMfirst();
            bought_num = fruitboughtList.get(i).getMnext();
            for (int j = 0;j < mFruitList.size();j++){
                iterFruitItem = mFruitList.get(j);
                if(iterFruitItem.name.equals(fruit_name)){
                    iterFruitItem = new Fruit(mFruitList.get(j));
                    iterFruitItem.boughtamount = bought_num;
                    newFruitList.add(iterFruitItem);
                    break;
                }
            }
        }
        mFruitList.clear();
        mFruitList.addAll(newFruitList);
        notifyDataSetChanged();
        //when recycleview's items changes, need to call this function
        //to refresh list
    }

    public interface AdapterReadyListenner {
        void onAdapterReady();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView fruitImage;
        TextView fruitName, fruitBref, fruitPrice;
        View fruitItem;

        public ViewHolder(View view) {
            super(view);
            fruitItem = view;
            fruitImage = (ImageView) view.findViewById(R.id.fruit_item_image);
            fruitName = (TextView) view.findViewById(R.id.fruit_item_name);
            fruitBref = (TextView) view.findViewById(R.id.fruit_item_text);
            fruitPrice = (TextView) view.findViewById(R.id.fruit_item_price);
        }
    }
}
