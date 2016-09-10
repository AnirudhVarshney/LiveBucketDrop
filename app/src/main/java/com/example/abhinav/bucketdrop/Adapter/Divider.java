package com.example.abhinav.bucketdrop.Adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.abhinav.bucketdrop.R;

/**
 * Created by ABHINAV on 22-04-2016.
 */
public class Divider extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int mOrientation;
    public Divider(Context context,int orientation){
        mDivider= ContextCompat.getDrawable(context,R.drawable.divider);//context only is giving  error for lesser that lollipop o we are usig ContextCompact for all
if(orientation!= LinearLayoutManager.VERTICAL){
    throw new IllegalArgumentException("This decoration can only be used with vertical recycler view");
}
        mOrientation=orientation;

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
       // super.onDraw(c, parent, state); dont need not giving anything
        if(mOrientation==LinearLayoutManager.VERTICAL){
            drawHorizontalDivider(c,parent,state);
            }
        }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int left, top, right, bottom;
        left = parent.getPaddingLeft();
        right = parent.getWidth() - parent.getPaddingRight();
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            if (AdapterDrop.FOOTER != parent.getAdapter().getItemViewType(i)) {
                View current = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) current.getLayoutParams();
                top = current.getTop() - params.topMargin;
                bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }
}

