package com.ljflemon.study.stickyexpandrecylerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;


/**
 * Created by liujianfeng on 16-7-21.
 */
public class StickyHeaderHelper {

    ViewGroup mViewGroup;
    RecyclerView mRecyclerView;
    View mHeader,firstVisibleView;
    int stickyHeaderGroup=-1;
    int firstVisibleViewPosition,lastVisibleViewPosition;
    int mHeaderOffset;
    StickyExpandRecyclerViewWrapper mStickyExpandRecyclerviewWrapper;
    StickyHeaderClickListener mStickyHeaderClickListener;
    final int CHILD_VIEW_RECYCLER=0;
    final int CHILD_VIEW_HEADER=1;

    public StickyHeaderHelper(ViewGroup viewgroup)
    {
        mViewGroup = viewgroup;
    }

    public void updateStickyHeader(View firstView, final int position)
    {
        //MyLod.log("enter updateStickyHeader, mHeader= "+mHeader);
        if(firstView==null)
            return ;

        int groupIndex = mStickyExpandRecyclerviewWrapper.getGroupIndex(position);

        if (mHeader==null)
        {
            Logger.d("mHeader is NULL, new mHeader");
            //mHeader = mInflater.inflate(R.layout.list_group_item_sticky, this, false);

            mHeader=((SitckyHeaderViewGroup)mViewGroup).getStickyHeader();
            //mHeader.setClickable(true);
            View click = mHeader.findViewById(R.id.container);
            mStickyHeaderClickListener = new StickyHeaderClickListener(position);
            click.setOnClickListener(mStickyHeaderClickListener);

            ensureHeaderHasCorrectLayoutParams(mHeader);
            ((SitckyHeaderViewGroup)mViewGroup).measureHeader();
            mHeader.setTop(0);
            mViewGroup.addView(mHeader,CHILD_VIEW_HEADER);
        }

        if(stickyHeaderGroup!=groupIndex) {
            stickyHeaderGroup = groupIndex;
            updateHeaderText(position);
            if (mStickyHeaderClickListener!=null)
                mStickyHeaderClickListener.updatePosition(position);
        }

        updateSickyHeaderOffset(position, lastVisibleViewPosition);
        //MyLod.log("child 1 = "+mViewGroup.getChildAt(1));
        //if (getChildAt(1)==null)
    }

    public void updateOrClearHeader()
    {
        //MyLod.log("updateOrClearHeader: "+firstVisibleView+"  :"+firstVisibleViewPosition);
        if (firstVisibleView == null || firstVisibleViewPosition<0)
            return;

        if (mStickyExpandRecyclerviewWrapper.isExpanded(firstVisibleViewPosition) &&
                mStickyExpandRecyclerviewWrapper.getChildCount(firstVisibleViewPosition) >0) {
            //groupItem's top <0, add stickyHeader

            updateStickyHeader(firstVisibleView, firstVisibleViewPosition);
        }
        else {
            //clear StickyHeader when Group is not expanded
            //if (firstVisibleView.getTop()<=0)
            removeStickyHeader();
            //else
            //  updateSickyHeaderOffset(firstVisibleViewPosition,lastVisibleViewPosition);
        }
    }


    public void updateFirstView()
    {
        firstVisibleViewPosition = mStickyExpandRecyclerviewWrapper.getFirstVisibleViewPosition();
        firstVisibleView = mStickyExpandRecyclerviewWrapper.getItemViewByPosition(firstVisibleViewPosition);
        lastVisibleViewPosition = mStickyExpandRecyclerviewWrapper.getLastVisibleViewPosition();
    }

    private void updateSickyHeaderOffset(int firstViewPosition, int lastViewPosition)
    {
        if (mHeader==null)
            return;
        for (int i=firstViewPosition; i<=lastViewPosition; i++) {
            //tempView = layoutManager.findViewByPosition(i);
            RecyclerViewItemInfo tempItem = RecyclerViewItemInfo.getInstance(i);

            /*if (mStickyExpandRecyclerviewWrapper.isGroupItem(i)) {
                if (stickyHeaderGroup == mStickyExpandRecyclerviewWrapper.getGroupIndex(i)) {
                    if (i == firstViewPosition && mStickyExpandRecyclerviewWrapper.getTop(i) > 0) {
                        //firstGroupView is visible completely, so remove stickyHeader
                        removeStickyHeader();
                        return;
                    }
                } else {
                    if ((mStickyExpandRecyclerviewWrapper.getTop(i) - mStickyExpandRecyclerviewWrapper.getMarginTop(i)) <= mHeader.getHeight()) {
                        mHeaderOffset = (mStickyExpandRecyclerviewWrapper.getTop(i) - mStickyExpandRecyclerviewWrapper.getMarginTop(i)) - mHeader.getHeight();
                        MyLod.log("next header top= " + tempItem.getTop() + ", so mHeader top change to= " + mHeaderOffset);
                        mHeader.setTranslationY(mHeaderOffset);
                        break;
                    }
                }
            } else {
                if (tempItem.getBottom() > mHeader.getHeight())
                    break;
            }
        }

            if (mViewGroup.getChildAt(CHILD_VIEW_HEADER) == null)
                mViewGroup.addView(mHeader,CHILD_VIEW_HEADER);
            return;*/


            if (i==firstViewPosition)
            {
                if (tempItem.isGroupItem() && stickyHeaderGroup==tempItem.getGroupIndex()) {
                    if(tempItem.getTop()>0)
                        removeStickyHeader();
                    else
                    {
                        mHeaderOffset=0;
                        mHeader.setTranslationY(mHeaderOffset);
                        //if (mViewGroup.getChildAt(CHILD_VIEW_HEADER)==null) {
                          //  mViewGroup.addView(mHeader,CHILD_VIEW_HEADER);
                        //}
                        setStickyHeaderVisible(true);
                    }
                    return;
                }

            }
            else if (!tempItem.isGroupItem() && tempItem.getBottom()>mHeader.getHeight())
                break;
            else if (tempItem.isGroupItem() && stickyHeaderGroup!=tempItem.getGroupIndex() && (tempItem.getTop()-tempItem.getMarginTop())<=mHeader.getHeight())
            {
                mHeaderOffset = (tempItem.getTop()-tempItem.getMarginTop())-mHeader.getHeight();
                Logger.d("next header top= "+tempItem.getTop()+", so mHeader top change to= "+mHeaderOffset);
                mHeader.setTranslationY(mHeaderOffset);
                break;
            }else
            {
                mHeaderOffset=0;
                mHeader.setTranslationY(mHeaderOffset);
            }

        }
        //if (mViewGroup.getChildAt(CHILD_VIEW_HEADER)==null) {
          //  MyLod.log("updateSickyHeaderOffset getChildAT=null,so add mHeader");
            //mViewGroup.addView(mHeader,CHILD_VIEW_HEADER);
        //}
        setStickyHeaderVisible(true);
        return;
    }


    private void updateHeaderText(int position)
    {
        if (mHeader!=null)
        {
            String t = mStickyExpandRecyclerviewWrapper.getGroupItemText(position);
            TextView mTextView = (TextView) mHeader.findViewById(android.R.id.text1);
            mTextView.setText(t);
        }
    }

    private void removeStickyHeader()
    {
        /*View tmp= mViewGroup.getChildAt(CHILD_VIEW_HEADER);
        if (tmp!=null && tmp==mHeader)
            mHeader.setVisibility(View.INVISIBLE);
        if ()
        if (mHeader!=null) {
            Log.e("LJF","mHeader != null, remove header");
            MyLod.log("child 1 = "+mViewGroup.getChildAt(1));
            if (mViewGroup.getChildAt(CHILD_VIEW_HEADER)!=null)
                mHeader.setVisibility(View.INVISIBLE);
                //mViewGroup.removeViewAt(CHILD_VIEW_HEADER);


            //mViewGroup.
        }*/
        setStickyHeaderVisible(false);
    }

    private void ensureHeaderHasCorrectLayoutParams(View header) {
        if (header==null)
            return;
        ViewGroup.LayoutParams lp = header.getLayoutParams();
        if (lp == null) {
            lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            header.setLayoutParams(lp);
        } else if (lp.height == FrameLayout.LayoutParams.MATCH_PARENT || lp.width == FrameLayout.LayoutParams.WRAP_CONTENT) {
            lp.height = FrameLayout.LayoutParams.WRAP_CONTENT;
            lp.width = FrameLayout.LayoutParams.MATCH_PARENT;
            header.setLayoutParams(lp);
        }
    }

    public void registerStickyExpandRecyclerView(StickyExpandRecyclerViewWrapper stickyexpandrecyclerviewwrapper)
    {
        mStickyExpandRecyclerviewWrapper = stickyexpandrecyclerviewwrapper;
        RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                updateFirstView();
                updateOrClearHeader();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView,newState);
            }
        };
        mStickyExpandRecyclerviewWrapper.setScrollListener(mOnScrollListener);
        mViewGroup.addView(stickyexpandrecyclerviewwrapper.getRealRecyclerView(),CHILD_VIEW_RECYCLER);
    }

    public interface SitckyHeaderViewGroup
    {
        public View getStickyHeader();
        public void measureHeader();
    }

    public void setStickyHeaderVisible(boolean visible)
    {
        View tmp= mViewGroup.getChildAt(CHILD_VIEW_HEADER);
        if (tmp!=null && tmp==mHeader)
            tmp.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
        return;
    }

    class StickyHeaderClickListener implements View.OnClickListener
    {
        int mPosition;
        public StickyHeaderClickListener(int position)
        {
            mPosition = position;
        }
        @Override
        public void onClick(View v) {
            Logger.d("mHeader onClick: " + mPosition+", HeaderGroup"+ stickyHeaderGroup);
            Logger.d("mHeader onClick: " + mStickyExpandRecyclerviewWrapper.isExpanded(stickyHeaderGroup));
            if (mStickyExpandRecyclerviewWrapper.isExpanded(stickyHeaderGroup)) {
                mStickyExpandRecyclerviewWrapper.collapseGroup(mPosition);
                Logger.d("click OK");
            }
        }
        public void updatePosition(int positon)
        {
            mPosition=positon;
        }
    }
}
