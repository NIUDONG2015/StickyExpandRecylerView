package com.ljflemon.study.stickyexpandrecylerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


public class advancedRVactivity extends AppCompatActivity {
    StickyHeaderLayout TestRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_rvactivity);
        //ButterKnife.bind(this);
        TestRV=(StickyHeaderLayout)findViewById(R.id.list);
    }
}
