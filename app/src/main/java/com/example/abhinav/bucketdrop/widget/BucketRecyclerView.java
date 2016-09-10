package com.example.abhinav.bucketdrop.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.example.abhinav.bucketdrop.extras.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ABHINAV on 21-04-2016.
 */
public class BucketRecyclerView extends RecyclerView {
    private List<View> mNonEmptyViews= Collections.emptyList();//to initialise empty list
    private List<View> mEmptyViews=Collections.emptyList();
    private AdapterDataObserver mObserver=new AdapterDataObserver() {
        @Override
        public void onChanged() {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
           // super.onItemRangeChanged(positionStart, itemCount);//remove the super function from all hence removing n all where  if on clickng the function there is nothing inside the func

        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            toggleViews();

        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            toggleViews();
        }
    };

    private void toggleViews() {
        if(getAdapter()!=null && !mEmptyViews.isEmpty() && !mNonEmptyViews.isEmpty());
        {
            if(getAdapter().getItemCount()==0){
                //show all the empty views
                Util.showViews(mEmptyViews);
                //hide the RecyclerView
                setVisibility(View.GONE);

                //hide all the views which are meant to be hidden
                Util.hideViews(mNonEmptyViews);
            } else {
                //hide all the empty views
                Util.showViews(mNonEmptyViews);

                //show the RecyclerView
                setVisibility(View.VISIBLE);

                //hide all the views which are meant to be hidden
                Util.hideViews(mEmptyViews);
            }
        }
    }

    public BucketRecyclerView(Context context) { //to use from code
        super(context);
    }

    public BucketRecyclerView(Context context, AttributeSet attrs) {//to use from xml
        super(context, attrs);
    }

    public BucketRecyclerView(Context context, AttributeSet attrs, int defStyle) {//to use from xml
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

            if(adapter!=null){
                adapter.registerAdapterDataObserver(mObserver);
            }
        mObserver.onChanged();
    }

    public void hideifEmpty(View ...views) {//3 dots if more than 1 view
    mNonEmptyViews= Arrays.asList(views);//bcoz left is an List
    }

    public void showifEmpty(View ...mEmptyView) {
        mEmptyViews=Arrays.asList(mEmptyView);
    }
}
