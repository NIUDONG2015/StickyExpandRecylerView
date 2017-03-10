package com.ljflemon.study.stickyexpandrecylerview;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * Created by liujianfeng on 16-7-21.
 */
public class StickyExpandRecyclerViewWrapper {
    RecyclerView mRecyclerView=null;
    LinearLayoutManager mLayoutManager;
    RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    RecyclerView.Adapter mWrappedAdapter;
    AbstractExpandableDataProvider dataProvider=null;
    Parcelable eimSavedState;
    ExpandableExampleAdapter myItemAdapter;
    RecyclerView.OnScrollListener mOnScrollListener;
    View mHeader,tempHeader;
    Context mContext;
    LayoutInflater mInflater;
    //test
    ArrayList<View> temp;
    View firstVisibleView;
    int firstVisibleViewPosition, lastVisibleViewPosition;
    int mHeaderOffset=0;
    int stickyHeaderGroup=-1;

    //ljf test
    StickyHeaderHelper mStickyHeaderHelper;

    public StickyExpandRecyclerViewWrapper(Context context)
    {
        mContext=context;
        createStickyExpandRecyclerView();
    }

    private void createStickyExpandRecyclerView()
    {
        mRecyclerView = new RecyclerView(mContext);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mLayoutManager = (layoutManager instanceof LinearLayoutManager)? (LinearLayoutManager)layoutManager:null;
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        // wrap for expanding

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Need to disable them when using animation indicator.
        animator.setSupportsChangeAnimations(false);


        //mExpandCollapse = new expandAndcollapseListener();
        //mRecyclerViewExpandableItemManager.setOnGroupExpandListener(mExpandCollapse);
        //mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(mExpandCollapse);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setHasFixedSize(false);

        // additional decorations
        //noinspection StatementWithEmptyBody
        /*if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(advancedRVactivity.this, R.drawable.material_shadow_z1)));
        }*/
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(mContext, R.drawable.list_divider_h), true));

        mRecyclerViewExpandableItemManager.attachRecyclerView(mRecyclerView);
        RecyclerViewItemInfo.init(mLayoutManager, mRecyclerView, mRecyclerViewExpandableItemManager);
    }


    public RecyclerView getRealRecyclerView()
    {
        return mRecyclerView;
    }

    public int getFirstVisibleViewPosition()
    {
        return mLayoutManager.findFirstVisibleItemPosition();
    }

    public int getLastVisibleViewPosition()
    {
        return mLayoutManager.findLastVisibleItemPosition();
    }

    public void setScrollListener(RecyclerView.OnScrollListener mOnScrollListener)
    {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }
    public View getItemViewByPosition(int position)
    {
        return mLayoutManager.findViewByPosition(position);
    }
    public int getChildCount(int position)
    {
        int groupIndex = getGroupIndex(position);
        return mRecyclerViewExpandableItemManager.getChildCount(groupIndex);
    }

    public boolean collapseGroup(int position) {

        int groupIndex = getGroupIndex(position);
        Logger.d("collapseGroup: "+position+" : "+groupIndex);
        return mRecyclerViewExpandableItemManager.collapseGroup(groupIndex);
    }

    /*get iteminfo by position*/
    public boolean isGroupItem(int position)
    {
        RecyclerViewItemInfo tempItem = RecyclerViewItemInfo.getInstance(position);
        return tempItem.isGroupItem();
    }
    public int getGroupIndex(int position)
    {
        RecyclerViewItemInfo tempItem = RecyclerViewItemInfo.getInstance(position);
        return tempItem.getGroupIndex();
    }
    public int getTop(int position)
    {
        RecyclerViewItemInfo tempItem = RecyclerViewItemInfo.getInstance(position);
        return tempItem.getTop();
    }
    public String getGroupItemText(int position)
    {
        RecyclerViewItemInfo temp = RecyclerViewItemInfo.getInstance(position);
        int groupIndex = temp.getGroupIndex();
        return dataProvider.getGroupItem(groupIndex).getText();
    }
    public int getBottom(int position)
    {
        RecyclerViewItemInfo tempItem = RecyclerViewItemInfo.getInstance(position);
        return tempItem.getBottom();
    }
    public boolean isExpanded(int position)
    {
        RecyclerViewItemInfo tempItem = RecyclerViewItemInfo.getInstance(position);
        return tempItem.isExpanded();
    }
    public int getMarginTop(int position)
    {
        RecyclerViewItemInfo tempItem = RecyclerViewItemInfo.getInstance(position);
        return tempItem.getMarginTop();
    }
    public int getMarginBottom(int position)
    {
        RecyclerViewItemInfo tempItem = RecyclerViewItemInfo.getInstance(position);
        return tempItem.getMarginBottom();
    }

    public void scrollToGroup(int groupPosition, int childItemHeight, int topMargin, int bottomMargin)
    {
        mRecyclerViewExpandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }

    public void setOnGroupExpandListener(RecyclerViewExpandableItemManager.OnGroupExpandListener listener)
    {
        mRecyclerViewExpandableItemManager.setOnGroupExpandListener(listener);
    }
    public void setOnGroupCollapseListener(RecyclerViewExpandableItemManager.OnGroupCollapseListener listener)
    {
        mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(listener);
    }

    public void scrollToPositionWithOffset(int groupPosition, int offset)
    {
        long packedPosition = RecyclerViewExpandableItemManager.getPackedPositionForGroup(groupPosition);
        int flatPosition = mRecyclerViewExpandableItemManager.getFlatPosition(packedPosition);
        mLayoutManager.scrollToPositionWithOffset(flatPosition,offset);
    }

    public void setDataProvider(AbstractExpandableDataProvider dataprovider)
    {
        dataProvider = dataprovider;
        if (dataProvider==null)
            dataProvider = new ExampleExpandableDataProvider();
        myItemAdapter = new ExpandableExampleAdapter(dataProvider);
        //adapter
        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(myItemAdapter);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
    }
}
