package com.wonderkiln.blurkit;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * A {@link ViewGroup} that blurs all content behind it. Automatically creates bitmap of parent content
 * and finds its relative position to the top parent to draw properly regardless of where the layout is
 * placed.
 *
 * @attr ref R.styleable.BlurLayout_downscaleFactor
 * @attr ref R.styleable.BlurLayout_blurRadius
 * @attr ref R.styleable.BlurLayout_fps
 */
public class BlurLayout extends FrameLayout {

    public static final float DEFAULT_DOWNSCALE_FACTOR = 0.12f;
    public static final int DEFAULT_BLUR_RADIUS = 12;
    public static final int DEFAULT_FPS = 60;

    // Customizable attributes

    /** Factor to scale the view bitmap with before blurring. */
    private float mDownscaleFactor;

    /** Blur radius passed directly to stackblur library. */
    private int mBlurRadius;

    /** Number of blur invalidations to do per second.  */
    private int mFPS;

    // Calculated class dependencies

    /** Reference to View for top-parent. For retrieval see {@link #getActivityView() getActivityView}. */
    private WeakReference<View> mActivityView;

    public BlurLayout(Context context) {
        super(context, null);
    }

    public BlurLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BlurLayout,
                0, 0);

        try {
            mDownscaleFactor = a.getFloat(R.styleable.BlurLayout_downscaleFactor, DEFAULT_DOWNSCALE_FACTOR);
            mBlurRadius = a.getInteger(R.styleable.BlurLayout_blurRadius, DEFAULT_BLUR_RADIUS);
            mFPS = a.getInteger(R.styleable.BlurLayout_fps, DEFAULT_FPS);
        } finally {
            a.recycle();
        }

        if (mFPS > 0) {
            Choreographer.getInstance().postFrameCallback(invalidationLoop);
        }
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
    public void invalidate() {
        super.invalidate();
        Bitmap bitmap = blur();
        if (bitmap != null) {
            setBackground(new BitmapDrawable(bitmap));
        }
    }

    /**
     * Recreates blur for content and sets it as the background.
     */
    private Bitmap blur() {
        if (getContext() == null) {
            return null;
        }

        // Check the reference to the parent view.
        // If not available, attempt to make it.
        if (mActivityView == null || mActivityView.get() == null) {
            mActivityView = new WeakReference<>(getActivityView());
            if (mActivityView.get() == null) {
                return null;
            }
        }

        // Calculate the relative point to the parent view.
        Point pointRelativeToActivityView = getPositionInScreen();

        // Set alpha to 0 before creating the parent view bitmap.
        // The blur view shouldn't be visible in the created bitmap.
        setAlpha(0);

        // Screen sizes for bound checks
        int screenWidth = mActivityView.get().getWidth();
        int screenHeight = mActivityView.get().getHeight();

        // The final dimensions of the blurred bitmap.
        int width = (int) (getWidth() * mDownscaleFactor);
        int height = (int) (getHeight() * mDownscaleFactor);

        // The X/Y position of where to crop the bitmap.
        int x = (int) (pointRelativeToActivityView.x * mDownscaleFactor);
        int y = (int) (pointRelativeToActivityView.y * mDownscaleFactor);

        // Padding to add to crop pre-blur.
        // Blurring straight to edges has side-effects so padding is added.
        int xPadding = getWidth() / 8;
        int yPadding = getHeight() / 8;

        // Calculate padding independently for each side, checking edges.
        int leftOffset = -xPadding;
        leftOffset = x + leftOffset >= 0 ? leftOffset : 0;

        int rightOffset = xPadding;
        rightOffset = x + getWidth() + rightOffset <= screenWidth ? rightOffset : screenWidth - getWidth() - x;

        int topOffset = -yPadding;
        topOffset = y + topOffset >= 0 ? topOffset : 0;

        int bottomOffset = yPadding;
        bottomOffset = y + height + bottomOffset <= screenHeight ? bottomOffset : 0;

        // Create parent view bitmap, cropped to the BlurLayout area with above padding.
        Bitmap bitmap;
        try {
            bitmap = getDownscaledBitmapForView(
                    mActivityView.get(),
                    new Rect(
                            pointRelativeToActivityView.x + leftOffset,
                            pointRelativeToActivityView.y + topOffset,
                            pointRelativeToActivityView.x + getWidth() + Math.abs(leftOffset) + rightOffset,
                            pointRelativeToActivityView.y + getHeight() + Math.abs(topOffset) + bottomOffset
                    ),
                    mDownscaleFactor
            );
        } catch (NullPointerException e) {
            return null;
        }

        // Blur the bitmap.
        bitmap = BlurKit.getInstance().blur(bitmap, mBlurRadius);

        //Crop the bitmap again to remove the padding.
        bitmap = Bitmap.createBitmap(
                bitmap,
                (int) (Math.abs(leftOffset) * mDownscaleFactor),
                (int) (Math.abs(topOffset) * mDownscaleFactor),
                width,
                height
        );

        // Make self visible again.
        setAlpha(1);

        // Set background as blurred bitmap.
        return bitmap;
    }

    /**
     * Casts context to Activity and attempts to create a view reference using the window decor view.
     * @return View reference for whole activity.
     */
    private View getActivityView() {
        Activity activity;
        try {
            activity = (Activity) getContext();
        } catch (ClassCastException e) {
            return null;
        }

        return activity.getWindow().getDecorView().findViewById(android.R.id.content);
    }

    /**
     * Returns the position in screen. Left abstract to allow for specific implementations such as
     * caching behavior.
     */
    private Point getPositionInScreen() {
        return getPositionInScreen(this);
    }

    /**
     * Finds the Point of the parent view, and offsets result by self getX() and getY().
     * @return Point determining position of the passed in view inside all of its ViewParents.
     */
    private Point getPositionInScreen(View view) {
        if (getParent() == null) {
            return new Point();
        }


        ViewGroup parent;
        try {
            parent = (ViewGroup) view.getParent();
        } catch (Exception e) {
            return new Point();
        }

        if (parent == null) {
            return new Point();
        }

        Point point = getPositionInScreen(parent);
        point.offset((int) view.getX(), (int) view.getY());
        return point;
    }

    /**
     * Users a View reference to create a bitmap, and downscales it using the passed in factor.
     * Uses a Rect to crop the view into the bitmap.
     * @return Bitmap made from view, downscaled by downscaleFactor.
     * @throws NullPointerException
     */
    private Bitmap getDownscaledBitmapForView(View view, Rect crop, float downscaleFactor) throws NullPointerException {
        View screenView = view.getRootView();

        int width = (int) (crop.width() * downscaleFactor);
        int height = (int) (crop.height() * downscaleFactor);

        if (screenView.getWidth() <= 0 || screenView.getHeight() <= 0 || width <= 0 || height <= 0) {
            throw new NullPointerException();
        }

        float dx = -crop.left * downscaleFactor;
        float dy = -crop.top * downscaleFactor;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        Matrix matrix = new Matrix();
        matrix.preScale(downscaleFactor, downscaleFactor);
        matrix.postTranslate(dx, dy);
        canvas.setMatrix(matrix);
        screenView.draw(canvas);

        return bitmap;
    }

    /**
     * Sets downscale factor to use pre-blur.
     * See {@link #mDownscaleFactor}.
     */
    public void setDownscaleFactor(float downscaleFactor) {
        this.mDownscaleFactor = downscaleFactor;
        invalidate();
    }

    /**
     * Sets blur radius to use on downscaled bitmap.
     * See {@link #mBlurRadius}.
     */
    public void setBlurRadius(int blurRadius) {
        this.mBlurRadius = blurRadius;
        invalidate();
    }

    /**
     * Sets FPS to invalidate blur with.
     * See {@link #mFPS}.
     */
    public void setFPS(int fps) {
        this.mFPS = fps;
    }

}
