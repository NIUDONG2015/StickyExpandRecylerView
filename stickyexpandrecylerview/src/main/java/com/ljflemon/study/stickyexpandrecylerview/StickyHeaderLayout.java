package com.ljflemon.study.stickyexpandrecylerview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.orhanobut.logger.Logger;

/**
 * Created by liujianfeng on 16-7-21.
 */
public class StickyHeaderLayout extends FrameLayout implements StickyHeaderHelper.SitckyHeaderViewGroup,
        RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener{
    Context mContext;
    LayoutInflater mInflater;
    View mHeader;
    StickyExpandRecyclerViewWrapper mStickyExpandRecyclerViewWrapper;
    StickyHeaderHelper mStickyHeaderHelper;
    //Handler mHander=new testHander();
    testHander handler;
    //ljf test
    int groupposition=0;

    public StickyHeaderLayout(Context context) {
        this(context, null);
    }

    public StickyHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.StickyHeaderLayout);
    }
    public StickyHeaderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
        mContext = context;
        mStickyExpandRecyclerViewWrapper = new StickyExpandRecyclerViewWrapper(this.getContext());
        mStickyHeaderHelper = new StickyHeaderHelper(this);
        mStickyHeaderHelper.registerStickyExpandRecyclerView(mStickyExpandRecyclerViewWrapper);
        //addView(mRecyclerView);
        mInflater = LayoutInflater.from(context);
        mStickyExpandRecyclerViewWrapper.setOnGroupCollapseListener(this);
        mStickyExpandRecyclerViewWrapper.setOnGroupExpandListener(this);
        //HandlerThread handlerThread=new HandlerThread("handler_hread");
        //handlerThread.start();

        handler=new testHander(Looper.getMainLooper());
    }

    public View getStickyHeader()
    {
        if (mHeader == null)
            mHeader = mInflater.inflate(R.layout.list_group_item_sticky, this, false);
        return mHeader;
    }

    public void measureHeader() {
        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        if (mHeader != null) {
            final int width = getMeasuredWidth() - mPaddingLeft - mPaddingRight;
            final int parentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    width, MeasureSpec.EXACTLY);
            final int parentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
            measureChild(mHeader, parentWidthMeasureSpec,
                    parentHeightMeasureSpec);
        }
    }
    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser) {
        if(fromUser){
            //user click normal groupItem, so do nothing
        }else
        {
            //user click StickyHeader, so scroll GroupItem to head
            mStickyExpandRecyclerViewWrapper.scrollToPositionWithOffset(groupPosition, 16);
            //groupposition = groupPosition;
            //handler.sendEmptyMessage(1);
        }

    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser) {
        if (fromUser) {
            adjustScrollPositionOnGroupExpanded(groupPosition);
        }
    }

    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = mContext.getResources().getDimensionPixelSize(R.dimen.list_item_height);
        int topMargin = (int) (mContext.getResources().getDisplayMetrics().density * 16); // top-spacing: 16dp
        int bottomMargin = topMargin; // bottom-spacing: 16dp

        mStickyExpandRecyclerViewWrapper.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }

    class testHander extends Handler
    {
        public testHander(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage (Message msg) {
            switch(msg.what)
            {
                case 1:
                    Logger.d("handle msg 1");
                    mStickyExpandRecyclerViewWrapper.scrollToPositionWithOffset(groupposition, 16);
                    //}
                    break;
            }
        }
    }

    public StickyExpandRecyclerViewWrapper getWrapper()
    {
        return (mStickyExpandRecyclerViewWrapper!=null)? mStickyExpandRecyclerViewWrapper:null;
    }
}
