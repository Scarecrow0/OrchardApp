package com.scarecrow.root.orchardapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-8-1.
 * no need set layout file.
 * in method instantiateItem(),
 * has already set a ImageView and its image resource.
 *
 */

public class AdsBannerAdapter extends PagerAdapter {
    private WeakReference<Context> mContext;
    private List<String> mimageUrlList,mAdsUrlList;
    private List<ImageView> IVList;
    //need to change to list<Bitmap>


    public AdsBannerAdapter(Context context,List<String> l1,List<String> l2) {
        this.mContext = new WeakReference<Context>(context);
        mimageUrlList = l1;
        mAdsUrlList = l2;
        IVList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mimageUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public List<String> getAbsUrlList(){
        return mAdsUrlList;
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView;
        if(IVList.size() <= position && IVList.size() < mimageUrlList.size()) {
            imageView = new ImageView(mContext.get());
            final int size = mimageUrlList.size();
            Picasso.with(mContext.get())
                    .load(mimageUrlList.get(position % size))
                    .into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            IVList.add(imageView);
        }else {
            imageView = IVList.get(position);
        }
        container.removeView(imageView);
        container.addView(imageView);
        return imageView;
    }

}
