package com.wonderkiln.blurkit;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

public class StaticBlurLayout extends BlurLayout {

    // Calculated class dependencies

    /** Relative position of this view inside top-parent view. For calculation see {@link #getPositionInScreen() getPositionInScreen}. */
    private PointF mPointRelativeToActivityView;

    public StaticBlurLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        getViewTreeObserver().addOnGlobalLayoutListener(relativePointUpdater);
    }

    /** Rebuilds the relative point of this view inside of all parent layouts. */
    private ViewTreeObserver.OnGlobalLayoutListener relativePointUpdater = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            mPointRelativeToActivityView = getPositionInScreen(StaticBlurLayout.this);
            invalidate();
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    protected PointF getPositionInScreen() {
        if (mPointRelativeToActivityView == null) {
            mPointRelativeToActivityView = getPositionInScreen(this);
        }

        return mPointRelativeToActivityView;
    }

}
