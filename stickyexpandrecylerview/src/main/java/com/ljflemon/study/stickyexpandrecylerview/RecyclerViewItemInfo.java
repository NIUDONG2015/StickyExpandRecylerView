package com.ljflemon.study.stickyexpandrecylerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

/**
 * Created by liujianfeng on 16-7-18.
 */
public class RecyclerViewItemInfo {
    private int position;
    private int groupIndex;
    private boolean GropuItem;
    private boolean Expanded;
    private int top;
    private int bottom;
    private int marginTop;
    private int marginBottom;
    public static RecyclerViewItemInfo mRecyclerViewItemInfo;

    static LinearLayoutManager layoutManager;
    static RecyclerView mRecyclerView;
    static RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;


    public void setValues(int p,int g,boolean ig,boolean ie,int t,int b,int tm,int bm)
    {
        position = p;
        groupIndex = g;
        GropuItem = ig;
        Expanded = ie;
        top=t;
        bottom=b;
        marginTop = tm;
        marginBottom = bm;
    }

    public static RecyclerViewItemInfo getInstance(int position)
    {
        if (position>=0 && mRecyclerViewItemInfo!=null&&mRecyclerViewItemInfo.getPosition() == position)
            mRecyclerViewItemInfo.updateItemInfo(position);
        else
        {
            mRecyclerViewItemInfo = new RecyclerViewItemInfo();
            mRecyclerViewItemInfo.updateItemInfo(position);
        }
        return mRecyclerViewItemInfo;
    }

    static public void init(LinearLayoutManager llm, RecyclerView rv, RecyclerViewExpandableItemManager rvem)
    {
        layoutManager = llm;
        mRecyclerView = rv;
        mRecyclerViewExpandableItemManager = rvem;
    }

    public void updateItemInfo(int position)
    {
        View v = layoutManager.findViewByPosition(position);
        int top=-1;
        int bottom=-1;
        int margintop=-1;
        int marginbottom=-1;

        if (v!=null) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();

            mRecyclerViewItemInfo = new RecyclerViewItemInfo();
            top = v.getTop();
            bottom = v.getBottom();
            margintop = params.topMargin;
            marginbottom = params.bottomMargin;
        }


        int realType = mRecyclerView.getAdapter().getItemViewType(position);
        boolean isGropuItem = RecyclerViewExpandableItemManager.isGroupViewType(realType);
        long mExpandableposition = mRecyclerViewExpandableItemManager.getExpandablePosition(position);
        int groupIndex = RecyclerViewExpandableItemManager.getPackedPositionGroup(mExpandableposition);
        boolean isExpanded = mRecyclerViewExpandableItemManager.isGroupExpanded(groupIndex);
        mRecyclerViewItemInfo.setValues(position,groupIndex,isGropuItem,isExpanded,top,bottom,margintop,marginbottom);
    }


    /*static public RecyclerViewItemInfo getInstance(int position)
    {
        View v = layoutManager.findViewByPosition(position);
        if (v==null) {
            return null;
        }
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();

        RecyclerViewItemInfo temp = new RecyclerViewItemInfo();
        int top = v.getTop();
        int bottom = v.getBottom();
        int margintop = params.topMargin;
        int marginbottom = params.bottomMargin;
        int realType = mRecyclerView.getAdapter().getItemViewType(position);
        boolean isGropuItem = RecyclerViewExpandableItemManager.isGroupViewType(realType);
        long mExpandableposition = mRecyclerViewExpandableItemManager.getExpandablePosition(position);
        int groupIndex = RecyclerViewExpandableItemManager.getPackedPositionGroup(mExpandableposition);
        boolean isExpanded = mRecyclerViewExpandableItemManager.isGroupExpanded(groupIndex);

        temp.setValues(position,groupIndex,isGropuItem,isExpanded,top,bottom,margintop,marginbottom);
        return temp;
    }*/

    public int getPosition(){
        return position;
    }
    public int getGroupIndex(){
        return groupIndex;
    }
    public int getTop(){
        return top;
    }
    public int getBottom(){
        return bottom;
    }
    public boolean isGroupItem(){return GropuItem;}
    public boolean isExpanded(){
        return Expanded;
    }
    public int getMarginTop(){
        return marginTop;
    }
    public int getMarginBottom(){
        return marginBottom;
    }

    /*public int getPosition(int pos){
        return position;
    }
    public int getGroupIndex(int pos){
        return groupIndex;
    }
    public int getTop(int pos){
        return top;
    }
    public int getBottom(int pos){
        return bottom;
    }
    public boolean getIsGroupItem(int pos){
        return isGropuItem;
    }
    public boolean getIsExpanded(int pos){
        return isExpanded;
    }*/
}
