package com.scarecrow.root.orchardapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.scarecrow.root.orchardapp.R;
import com.scarecrow.root.orchardapp.activties.ActivityMainInterface;
import com.scarecrow.root.orchardapp.data_util.ResDownloader;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-8-15.
 */

public class ViewMapDisplay extends View {
    private Paint paint;
    int mMapnum = 0;

    private Bitmap canvas_bg = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder);
            // BitmapFactory.decodeFile(ALBUM_PATH+"placeholder.png");
    ResDownloader rd;

    public ViewMapDisplay(Context context, AttributeSet attrs) {
        super(context,attrs);
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    public void init_canvas(final int num){
        if(mMapnum != 0 && num == mMapnum){
            return;
        }
        mMapnum = num;
        Handler outer = new Handler(){
            @Override
            public void handleMessage(Message msg){
                canvas_bg = (Bitmap) msg.obj;
                canvas_bg = canvas_bg.copy(Bitmap.Config.ARGB_8888,true);
                postInvalidate();
            }
        };
        rd = new ResDownloader(outer);
        rd.setLooperReadyListener(new ResDownloader.LooperReadyListener() {
            @Override
            public void onlooperReady() {
                rd.queueDownload("canvas", ActivityMainInterface.Server_ip + "/image/map.png", "", 1);
            }
        });
        rd.start();
        rd.getLooper();
    }
    int mx,my;
    @Override
    public void onDraw(Canvas canvas){

        Log.d(TAG, "onDraw:  x = " + mx + ",y = " + my);
        canvas.drawBitmap(canvas_bg,0,0,paint);
        canvas.drawCircle(mx,my,10,paint);
    }

    public void drawDot(double x,double y){
        mx = (int)(x*10);
        my = (int)(y*10);
        postInvalidate();
    }

    public void onDestroy(){

        rd.quit();
    }


}
