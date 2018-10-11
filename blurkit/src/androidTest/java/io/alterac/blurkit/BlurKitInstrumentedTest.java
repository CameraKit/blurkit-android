package io.alterac.blurkit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class BlurKitInstrumentedTest {

    private Context context;
    BlurKit blurKit;

    private Bitmap original;
    private Bitmap blur1;
    private Bitmap blur2;

    @Before
    public void setupTests() {
        context = InstrumentationRegistry.getContext();
        blurKit = new BlurKit();
        BlurKit.init(context);

        original = BitmapFactory.decodeResource(context.getResources(), R.drawable.original_bk_logo);
        blur1 = original.copy(original.getConfig(), true);
        blur2 = original.copy(original.getConfig(), true);
    }

    @Test
    public void blurTest() {
        blur1 = blurKit.blur(blur1, 10);
        blur2 = blurKit.blur(blur2, 10);

        assertTrue(blur1.sameAs(blur2));
        assertFalse(blur1.sameAs(original));
    }
}
