package com.scarecrow.root.orchardapp;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by root on 17-7-30.
 * the class create a new sub thread
 * which downloading resource from server
 *
 */

public class ResDownloader extends HandlerThread {
    private final static String ALBUM_PATH
            = Environment.getExternalStorageDirectory() + "/download_test/";


    private int ACTION = 0;
    //0 -> String ,1 -> image,3 -> last one image download
    private static final String TAG = "ResDownloader";
    private boolean mhasquit = false;
    private Handler mRequestHandler;
    //sub thread handler,deal with download mission which given by main thread
    private Handler mOuterHandler;
    //main thread handler, able to update UI by this handler
    private ConcurrentMap<String,StringPair> mRequestMap = new ConcurrentHashMap<>();
    //set a mapping relationship from target to requestURL
    private  ServerContacter mserverContacter = new ServerContacter();
    public  ResDownloader(Handler responsHandler){
        super(TAG);
        mOuterHandler = responsHandler;
        // holds the handler of main thread
        // in order to finish the ui update by send Message.
        // update by setting listener is failed
        // listener function's calling back may process by this sub thread

    }

    private LooperReadyListener mLooperReadyListener;
    public interface LooperReadyListener<T>{
        void onlooperReady();
    }
    public void setLooperReadyListener(LooperReadyListener listener){
        mLooperReadyListener = listener;
    }
/*
    private ResDownloaderImageListener mResDownloaderImageListener;
    public interface ResDownloaderImageListener<T>{
        void onResDownloadedImage(T target,Bitmap content);
    }
    public void setResDownloaderImageListener(ResDownloaderImageListener listener){
        mResDownloaderImageListener = listener;
    }

*/
    @Override
    public boolean quit(){
        mhasquit = true;
        mOuterHandler = null;
        mRequestHandler = null;
        return super.quit();
    }


    public void queueDownload(String target,String url,String postarg,int action){
        //target -> UI object or some objects need to be updated
        //url -> request url
        //postarg -> if action is post,need some args
        //action -> download string or image ?
        Log.d(TAG, "queuedownload: got a task ,url: " + url + postarg +"action:" + action);
        if(url == null)
            mRequestMap.remove(target,url);
        else{
            mRequestMap.put(target,new StringPair(url,postarg));
            ACTION = action;
            mRequestHandler.obtainMessage(ACTION,target).sendToTarget();
            //post this task to queue
            // wait for download handler ready to deal with it
        }
    }

    @Override
    //the onLoopPrepared() may like a run() in the new create thread.
    //the sub thread handler ,which process the download ,must create in this section.
    //this means ,when time is ready to handle the task which carried by Message
    //take it up,and give it to target handler deals with it
    public void onLooperPrepared(){
        mRequestHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                String target;
                ACTION = msg.what;
                switch (msg.what){
                    case 0:
                        //download string
                        target = (String) msg.obj;
                        Log.d(TAG, "handleMessage: ready to handle request"
                                + target + " \n "
                                + mRequestMap.get(target).getMfirst()
                                + mRequestMap.get(target).getMnext()
                                + "'s stringdownload");
                        handleRequeststring(target);
                        break;
                    case 1:
                        //download image
                        target = (String) msg.obj;
                        Log.d(TAG, "handleMessage: ready to handle"
                                + mRequestMap.get(target).getMfirst()
                                + "'s image download action : "+ACTION );
                        handleRequestimage(target);
                        break;
                    default:

                        break;
                }
            }
        };
        mLooperReadyListener.onlooperReady();
    }

    private void handleRequeststring(final String target){
        try{
            final StringPair resqsp = new StringPair(mRequestMap.get(target));

            if(resqsp.getMfirst() == null)
                return;
            Log.d(TAG, "handleRequeststring: "+ resqsp.getMfirst() + resqsp.getMnext());


            final String str = mserverContacter
                    .getURLString(resqsp.getMfirst(),resqsp.getMnext());
            Log.d(TAG, "handleRequeststring: successfully get string respons");
            if(str == null)
                return;
            mRequestMap.remove(target);
            mOuterHandler.obtainMessage(0,new String(str)).sendToTarget();


            //**********************************************************************
            //directly send Message to main thread handler
            //method one
            //**********************************************************************
/*
            mUIupdateHandler.post(new Runnable(){
            //**********************************************************************
            //method two
            // set a listener to callback
            // cannot work
            //**********************************************************************
                @Override
                public void run(){
                   if(resqsp.getMfirst() == null || mhasquit)
                       return;
                   else{
                       mRequestMap.remove(target);
                       mResDownloaderStringListener.onResDownloadedstring(target,str);
                   }
               }
            });
*/

        }catch (Exception ee){
            Log.d(TAG, "handleRequeststring: "+ ee);
        }
    }

    private void handleRequestimage(final String target){
        final StringPair sp = mRequestMap.get(target);
        try{
            if (sp.getMfirst() == null)
                return;
            Log.d(TAG, "handleRequestimage: " + sp.getMfirst());
            final Bitmap bitmap_tmp = mserverContacter
                    .getURLImage(sp.getMfirst(), sp.getMnext());
            Log.d(TAG, "handleRequestimage: successfully download image ,updating UI");
            if(bitmap_tmp == null)
                return;
            mRequestMap.remove(target);
            mOuterHandler.obtainMessage(1,bitmap_tmp).sendToTarget();
            //**********************************************************************
        //directly send Message to main thread handler
        //method one
        //**********************************************************************

 /*
 //**********************************************************************

            //method 2  listener ,
            //cannot work
          //**********************************************************************
            mUIupdateHandler.post(new Runnable() {
                @Override
                public void run() {
                   if(mRequestMap.get(target).getMfirst() != sp.getMfirst()
                            || mhasquit )
                    return;
                    mRequestMap.remove(target);
                    saveFile(bitmap_tmp,"test_1.jpg");
                    mResDownloaderImageListener.onResDownloadedImage(target,bitmap_tmp);
                }
            });
*/
        }catch(Exception ee) {
            Log.d(TAG, "handleRequestimage: " +ee);
            return;
        }
    }

    public void saveFile(Bitmap bm, String fileName) {
        try {
            File dirFile = new File(ALBUM_PATH);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            File myCaptureFile = new File(ALBUM_PATH + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        }catch (Exception ee){
            Log.d(TAG, "saveFile: " + ee);
        }
    }

}
