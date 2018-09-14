package com.alterac.blurkit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedImageView extends ImageView {

    private float mCornerRadius = 0;
    public static final int DEFAULT_COLOR = 0xff000000;

    private RectF rectF;
    private PorterDuffXfermode porterDuffXfermode;

    public RoundedImageView(Context context) {
        super(context, null);
        rectF = new RectF();
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    public RoundedImageView(Context context, AttributeSet attributes) {
        super(context, attributes);
        rectF = new RectF();
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable myDrawable = getDrawable();
        if (myDrawable!=null && myDrawable instanceof BitmapDrawable && mCornerRadius > 0) {
            Paint paint = ((BitmapDrawable) myDrawable).getPaint();
            rectF.set(myDrawable.getBounds());
            int prevCount = canvas.saveLayer(rectF, null, Canvas.ALL_SAVE_FLAG);
            getImageMatrix().mapRect(rectF);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(DEFAULT_COLOR);
            canvas.drawRoundRect(rectF, mCornerRadius, mCornerRadius, paint);

            Xfermode prevMode = paint.getXfermode();
            paint.setXfermode(porterDuffXfermode);
            super.onDraw(canvas);

            paint.setXfermode(prevMode);
            canvas.restoreToCount(prevCount);
        } else {
            super.onDraw(canvas);
        }
    }

    public void setCornerRadius(float cornerRadius) {
        this.mCornerRadius = cornerRadius;
    }

}