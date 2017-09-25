package com.scarecrow.root.orchardapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by root on 17-9-25.
 */

public class NewsSingleActivity extends AppCompatActivity {
    NewsItem ni;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_news_single);
        ni = (NewsItem) getIntent().getSerializableExtra("clicked_news");
        TextView tv = (TextView) findViewById(R.id.news_text_long);
        tv.setText(ni.long_text);

        tv = (TextView) findViewById(R.id.news_single_title);
        tv.setText(ni.title);

        tv = (TextView) findViewById(R.id.news_view_times);
        String t = getResources().getString(R.string.lilacishu) + String.valueOf(ni.view_times);
        tv.setText(t);

        ImageView iv = (ImageView) findViewById(R.id.news_image_big);
        Picasso.with(getBaseContext())
                .load(ni.image_url)
                .into(iv);
        new Uploader().execute();

    }

    private class Uploader extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                new ServerContacter().getURLString(MainInterfaceActivity.Server_ip + "/app/view_news", "id=" + ni.id);
            } catch (Exception ee) {
                Log.d("", "error in upload view info");
            }
            return null;
        }
    }
}
