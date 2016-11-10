package com.wonderkiln.blurkit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.Choreographer;

/**
 * A {@link BlurLayout} that invalidates continuously synced with the UI thread. You can set the frames
 * per second using the fps xml attribute or by using {@link #setFPS(int)}.
 *
 * @attr ref R.styleable.BlurLayout_fps
 */
public class DynamicBlurLayout extends BlurLayout {

    // Customizable attributes

    /** Number of blur invalidations to do per second.  */
    private int mFPS;

    public DynamicBlurLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BlurLayout,
                0, 0);

        try {
            mFPS = a.getInteger(R.styleable.BlurLayout_fps, 60);
        } finally {
            a.recycle();
        }

        Choreographer.getInstance().postFrameCallback(invalidationLoop);
    }

    /** Choreographer callback that re-draws the blur and schedules another callback. */
    private Choreographer.FrameCallback invalidationLoop = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
            invalidate();
            Choreographer.getInstance().postFrameCallbackDelayed(this, 1000 / mFPS);
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    protected PointF getPositionInScreen() {
        return getPositionInScreen(this);
    }

    /**
     * Sets FPS to invalidate blur with.
     * See {@link #mFPS}.
     */
    public void setFPS(int fps) {
        this.mFPS = fps;
    }

}
