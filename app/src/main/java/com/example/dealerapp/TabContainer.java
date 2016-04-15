package com.example.dealerapp;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TabHost;

public class TabContainer extends AppCompatActivity {

    TabHost tabHost;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabHost = (TabHost) findViewById(R.id.tabHost);
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(TabContainer.this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(mLocalActivityManager);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Third Tab");
        Resources res=getResources();

        url=this.getIntent().getStringExtra("url");

        tab1.setIndicator("Current Week");
        tab1.setContent(new Intent(getApplicationContext(), WeekList.class).putExtra("url",url));

        tab2.setIndicator("Last Week");
        tab2.setContent(new Intent(getApplicationContext(), WeekList.class).putExtra("url",url));

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
    }

}
