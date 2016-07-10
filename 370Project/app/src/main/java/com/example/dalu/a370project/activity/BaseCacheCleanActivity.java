package com.example.dalu.a370project.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.dalu.a370project.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * Created by DALU on 2016/7/9.
 */
@ContentView(R.layout.base_cache_clean)
public class BaseCacheCleanActivity extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        TabHost.TabSpec tabSpec1 = getTabHost().newTabSpec("clear_cache").setIndicator("缓存清理");
        TabHost.TabSpec tabSpec2 = getTabHost().newTabSpec("SD_lear_cache").setIndicator("SD缓存清理");
        tabSpec1.setContent(new Intent(this,CacheCleanActivity.class));
        tabSpec2.setContent(new Intent(this,SDCacheCleanActivity.class));
        getTabHost().addTab(tabSpec1);
        getTabHost().addTab(tabSpec2);
    }
}
