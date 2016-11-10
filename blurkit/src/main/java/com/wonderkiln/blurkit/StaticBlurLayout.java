package com.wonderkiln.blurkit;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

/**
 * A {@link BlurLayout} that only invalidates on layout changes - not continuously. This extension
 * will provide better performance in cases where the background content is not moving.
 *
 * You can also use this extension if you want to handle when blurring happens yourself. Just call
 * {@link #invalidate} if you want the blur to update.
 */
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
    public void invalidate() {
        mPointRelativeToActivityView = getPositionInScreen(this);
        super.invalidate();
    }

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
