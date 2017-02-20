package com.zh.oademo.oademo.plugins.materiallist;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardLayout;


public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private RecyclerView mRecyclerView;

    public interface OnItemClickListener {
        void onItemClick(@NonNull final Card card, int position);

        void onItemLongClick(@NonNull final Card card, int position);
    }

    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                CardLayout childView = (CardLayout) mRecyclerView.findChildViewUnder(e.getX(), e.getY());

                if (childView != null && mListener != null) {
                    mListener.onItemLongClick(childView.getCard(), mRecyclerView.getChildPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {

        View v = view.findChildViewUnder(e.getX(), e.getY());

        if (v instanceof CardLayout) {
            CardLayout childView = (CardLayout) v;

            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView.getCard(), view.getChildPosition(childView));
            }
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}