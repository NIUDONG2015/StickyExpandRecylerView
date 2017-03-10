package com.ljflemon.study.stickyexprecylerveiw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ljflemon.study.stickyexpandrecylerview.ExampleExpandableDataProvider;
import com.ljflemon.study.stickyexpandrecylerview.advancedRVactivity;
import com.ljflemon.study.stickyexpandrecylerview.StickyHeaderLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button start;
    StickyHeaderLayout mstickyHeaderLayout;
    ExampleExpandableDataProvider dataProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mstickyHeaderLayout = (StickyHeaderLayout)findViewById(R.id.list);
        dataProvider = new ExampleExpandableDataProvider();

        //user can set customize data as follow:
        List children = Arrays.asList("apple", "orange", "banana","pear","peach","watermelon","persimmon",
                "tomato","carrot");

        dataProvider.addData("one",children);
        dataProvider.addData("two",children);
        dataProvider.addData("three",children);
        dataProvider.addData("four",children);
        dataProvider.addData("five",children);
        dataProvider.addData("six",children);
        dataProvider.addData("seven",children);
        dataProvider.addData("eight",children);
        dataProvider.addData("nine",children);
        dataProvider.addData("ten",children);
        dataProvider.addData("zero",children);
        
        mstickyHeaderLayout.getWrapper().setDataProvider(dataProvider);
    }
}
