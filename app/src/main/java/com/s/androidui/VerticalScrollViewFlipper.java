package com.s.androidui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Adapter;
import android.widget.ViewFlipper;

public class VerticalScrollViewFlipper extends ViewFlipper {

    public VerticalScrollViewFlipper(Context context) {
        super(context);
        init(context);
    }

    public VerticalScrollViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setInAnimation(context, R.anim.anim_in);
        setOutAnimation(context, R.anim.anim_out);

        setFlipInterval(3000);
        setAnimateFirstView(false);
    }

    public void setAdapter(Adapter adapter) {
        if (isFlipping()) {
            stopFlipping();
        }
        for (int i = 0; i < adapter.getCount(); i++) {
            addView(adapter.getView(i, null, this));
        }
        if (adapter.getCount() > 1) {
            startFlipping();
        }
    }

}
