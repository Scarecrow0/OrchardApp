package com.scarecrow.root.orchardapp.data_util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 *Created by root on 17-7-29.
 * this is a class who up/download content to/from the server
 * first, it post a request as http string to server ,get or post
 * then server give back something as bytes
 * this class provide some methods resolve those bytes
 * (pictures,string,json object) and return it to other models
 * */

public class ServerContacter{
    private byte[] getURLbyte(String urlrequest,String postarg) {
        URL url ;
        HttpURLConnection con = null;
        InputStream in ;
        StringBuffer sb ;
        //post arg
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //output to other model
        byte[] output_buffer = new byte[5999999];


        try {
            url = new URL(urlrequest);
            con = (HttpURLConnection) url.openConnection();
            //connect the url
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            Log.d(TAG, "getURLbyte: connetion set up");
            if(postarg.length() != 0){
                //post,use osw send the post args
                sb = new StringBuffer();
                sb.append(postarg);
                con.setDoInput(true);
                OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(),"UTF-8");
                osw.write(sb.toString());
                osw.flush();
                osw.close();
                Log.d(TAG, "getURLbyte: do post");
            }
            //if get ,just ready to receive
            Log.d(TAG, "getURLbyte: do get");
            int byte_num = 0;
            //receive data
            in = con.getInputStream();
            Log.d(TAG, "getURLbyte: writing...");
            while( (byte_num = in.read(output_buffer) ) > 0){
                out.write(output_buffer,0,byte_num);
            }
            Log.d(TAG, "getURLbyte: got back data : ");

        }
        catch (Exception ee){
            Log.e(TAG, "getURLbyte: error in: "+ ee);
        }
        finally {
            con.disconnect();
            return out.toByteArray();
        }
    }
    public String getURLString(String urlRequest,String postarg) throws IOException{
        Log.d(TAG, "getURLString: calling");
        String str_tmp = new String(getURLbyte(urlRequest,postarg));
        return str_tmp;
    }
    public Bitmap getURLImage(String urlRequest,String postarg) throws IOException{
        Log.d(TAG, "getURLImage: calling...");
        byte[] bitmapBytes = getURLbyte(urlRequest,postarg);
        Log.d(TAG, "getURLImage: creat bitmap and return it");
        return BitmapFactory.decodeByteArray(bitmapBytes,0,bitmapBytes.length);

    }

}
