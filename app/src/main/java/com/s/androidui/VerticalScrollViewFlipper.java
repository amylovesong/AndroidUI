package com.s.androidui;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.Adapter;
import android.widget.ViewFlipper;

public class VerticalScrollViewFlipper extends ViewFlipper {

    private Adapter mAdapter;
    private DataSetObserver mDataSetObserver;

    public VerticalScrollViewFlipper(Context context) {
        super(context);
        init(context);
    }

    public VerticalScrollViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setAdapter(@NonNull Adapter adapter) {
        if (this.mAdapter == adapter) {
            return;
        }
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        this.mAdapter = adapter;
        this.mAdapter.registerDataSetObserver(mDataSetObserver);
    }

    private void init(Context context) {
        initDataSetObserver();

        setInAnimation(context, R.anim.anim_in_translate_y);
        setOutAnimation(context, R.anim.anim_out_translate_y);

        setFlipInterval(3000);
        setAnimateFirstView(false);
    }

    private void initDataSetObserver() {
        if (this.mDataSetObserver == null) {
            this.mDataSetObserver = new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    onDataChanged(mAdapter);
                }
            };
        }
    }

    private void onDataChanged(Adapter adapter) {
        if (isFlipping()) {
            stopFlipping();
        }
        removeAllViews();
        for (int i = 0; i < adapter.getCount(); i++) {
            addView(adapter.getView(i, null, this));
        }
        if (adapter.getCount() > 1) {
            startFlipping();
        }
    }

}
