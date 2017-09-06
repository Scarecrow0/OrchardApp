package com.scarecrow.root.orchardapp;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by root on 17-8-1.
 * a auto play adsbanner
 *  GOT YOU !!!!!  LEAK!!!
 *
 */

public class AdsBanner extends ViewPager {

    public AdsBanner(Context context){
        super(context);

    }
    public AdsBanner(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    private Handler playrec = new Handler() ;
    // this Handler is keeping the view auto play
    // use the handler which create by myself,
    // which is different from postDelay() functions owns handler which is untouchable for me
    // the Handler need to send Messages to keep auto play (android's asynchronous thread system)
    // those Messages have references of this activity , and they can't destroy themselves
    // when activity destroy, so it will cause mem leak .
    // use this Handler ,i can remove all the messages when this activity destory .
    // which solve this mem leak problem XD
    /**
     * 播放时间
     */
    private int showTime = 3 * 1000;
    /**
     * 滚动方向
     */
    private Direction direction = Direction.LEFT;

    /**
     * 设置播放时间，默认3秒
     *
     * @param showTimeMillis 毫秒
     */
    public void setShowTime(int showTimeMillis) {
        this.showTime = showTime;
    }

    /**
     * 设置滚动方向，默认向左滚动
     *
     * @param direction 方向
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * 开始
     */
    public void start() {
        stop();
        playrec.postDelayed(player, showTime);
        // post a Message which can let task perform delayed


        // postDelayed(player, showTime);
        // if go like this ,it will leak
    }

    /**
     * 停止
     */
    public void stop() {
        playrec.removeCallbacksAndMessages(null);
    }

    /**
     * 播放上一个
     */
    public void previous() {
        if (direction == Direction.RIGHT) {
            play(Direction.LEFT);
        } else if (direction == Direction.LEFT) {
            play(Direction.RIGHT);
        }
    }

    /**
     * 播放下一个
     */
    public void next() {
        play(direction);
    }

    /**
     * 执行播放
     *
     * @param direction 播放方向
     */
    private synchronized void play(Direction direction) {
        PagerAdapter pagerAdapter = getAdapter();
        if (pagerAdapter != null) {
            int count = pagerAdapter.getCount();
            int currentItem = getCurrentItem();
            switch (direction) {
                case LEFT:
                    currentItem++;
                    if (currentItem >= count)
                        currentItem = 0;
                    break;
                case RIGHT:
                    currentItem--;
                    if (currentItem <= 0)
                        currentItem = count;
                    break;
            }
            setCurrentItem(currentItem);
        }
        start();
    }

    /**
     * 播放器
     */
    private Runnable player = new Runnable() {
        @Override
        public void run() {
            play(direction);
        }
    };

    public enum Direction {
        LEFT,
        RIGHT
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == SCROLL_STATE_IDLE)
                    start();
                else if (state == SCROLL_STATE_DRAGGING)
                    stop();
            }
        });
    }

    public void onDestory(){
        playrec.removeCallbacksAndMessages(null);
        // remove the Message which have activity reference
        // avoid the mem leak when activity destroyed
    }
}

