package com.scarecrow.root.orchardapp.activties;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scarecrow.root.orchardapp.R;
import com.scarecrow.root.orchardapp.adapters.AdapterNewsRecyclerView;
import com.scarecrow.root.orchardapp.adapters.DividerItemDecoration;
import com.scarecrow.root.orchardapp.data_util.News;

/**
 * Created by root on 17-9-24.
 */

public class ActivityHealthyMain extends AppCompatActivity {
    private News mNews;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_healthy_main);
        final RecyclerView RV_HealthyNews, RV_KnowledgeNews;
        RV_HealthyNews = (RecyclerView) findViewById(R.id.healthy_list);
        RV_KnowledgeNews = (RecyclerView) findViewById(R.id.knowledge_list);

        RV_HealthyNews.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        RV_KnowledgeNews.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        RV_HealthyNews.setAdapter(new AdapterNewsRecyclerView(getBaseContext()));
        RV_KnowledgeNews.setAdapter(new AdapterNewsRecyclerView(getBaseContext()));

        RV_HealthyNews.addItemDecoration(new DividerItemDecoration(getBaseContext(), 1));
        RV_KnowledgeNews.addItemDecoration(new DividerItemDecoration(getBaseContext(), 1));
        mNews = new News();

        mNews.setOnDataReadyListenner(new News.onDataReadyListenner() {
            @Override
            public void onDataReady() {
                AdapterNewsRecyclerView newsRecyclerViewAdapter;
                newsRecyclerViewAdapter = (AdapterNewsRecyclerView) RV_HealthyNews.getAdapter();
                newsRecyclerViewAdapter.updateDataSet(mNews.getHealthyNews());

                newsRecyclerViewAdapter = (AdapterNewsRecyclerView) RV_KnowledgeNews.getAdapter();
                newsRecyclerViewAdapter.updateDataSet(mNews.getKnowledgeews());
            }
        });
        mNews.getData();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        mNews.getData();
    }
}
