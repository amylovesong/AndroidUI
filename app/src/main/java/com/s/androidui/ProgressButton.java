package com.s.androidui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 带 Loading 的 Button 控件
 * @author sxl
 * @since 2019-12-12.
 */
public class ProgressButton extends AppCompatImageButton implements View.OnClickListener {

    private static final String TAG = "ProgressButton";

    private Drawable mLoadingDrawable;
    private TextPaint mTextPaint;
    private String mText;
    private Rect mTextBounds;
    private boolean mShowLoading;
    private ObjectAnimator mLoadingAnimator;

    public ProviderProgressButton(Context context) {
        this(context, null);
    }

    public ProviderProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    @SuppressLint("ResourceType")
    public ProviderProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mLoadingDrawable = getDrawable();
        if (mLoadingDrawable == null) {
            mLoadingDrawable = getResources().getDrawable(R.anim.anim_progress_btn_loading);
            setImageDrawable(mLoadingDrawable);
        }
        mLoadingDrawable.setAlpha(0);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);

        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.density = getResources().getDisplayMetrics().density;

        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(typedArray.getColor(R.styleable.CustomButton_fontColor, Color.WHITE));
        mTextPaint.setTextSize(typedArray.getDimension(R.styleable.CustomButton_fontSize, 28));

        mText = typedArray.getString(R.styleable.CustomButton_textCustom);
        if (TextUtils.isEmpty(mText)) {
            mText = "Progress Button";
        }

        mLoadingAnimator = ObjectAnimator.ofInt(mLoadingDrawable, "Level", 0, 10000);
        mLoadingAnimator.setInterpolator(new LinearInterpolator());
        mLoadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mLoadingAnimator.setDuration(800);

        typedArray.recycle();

        mTextBounds = new Rect();
        final int padding = getResources().getDimensionPixelSize(R.dimen.12dp);
        sLogger.debug("ProviderProgressButton padding: " + padding);
        setPadding(0, padding, 0, padding);
        setOnClickListener(this);
    }

//    @Override
//    public boolean performClick() {
//        sLogger.debug("performClick");
//        final boolean isClicked = super.performClick();
//        if (isClicked) {
//            mShowLoading = true;
//            this.invalidate();
//        }
//        return isClicked;
//    }

    public void startLoading() {
        setClickable(false);
        mShowLoading = true;
        showLoadingAnimation(true);
        this.invalidate();
    }

    public void stopLoading() {
        setClickable(true);
        mShowLoading = false;
        showLoadingAnimation(false);
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mShowLoading) {
            mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);
            canvas.drawText(mText, getWidth() / 2, getHeight() / 2 + (mTextBounds.bottom - mTextBounds.top) / 2,
                    mTextPaint);
        }
        super.onDraw(canvas);
    }

    private void showLoadingAnimation(boolean show) {
        if (show) {
            mLoadingDrawable.setAlpha(255);
            if (!mLoadingAnimator.isRunning()) {
                mLoadingAnimator.start();
            }
        } else {
            mLoadingAnimator.end();
            mLoadingDrawable.setAlpha(0);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
