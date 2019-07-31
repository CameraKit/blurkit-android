package io.alterac.blurkit;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;

public class BlurLayoutTest {

    @Mock
    private Context mockContext;

    public static final int TEST_INT = 1;
    private static final float TEST_FLOAT = 1.1f;
    private static final LifecycleOwner LIFECYCLE_OWNER = new LifecycleOwner() {

        private final Lifecycle lifecycle = new Lifecycle() {
            @Override
            public void addObserver(@NonNull LifecycleObserver observer) {

            }

            @Override
            public void removeObserver(@NonNull LifecycleObserver observer) {

            }

            @NonNull
            @Override
            public Lifecycle.State getCurrentState() {
                return State.DESTROYED;
            }
        };

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return lifecycle;
        }
    };

    private BlurLayout blurLayout;

    @Before
    public void setupTests() {
        MockitoAnnotations.initMocks(this);

        blurLayout = new BlurLayout(mockContext);
    }

    @Test
    public void setFPSTest() {
        blurLayout.setFPS(TEST_INT);
        assertEquals(TEST_INT, blurLayout.getFPS());
    }

    @Test
    public void setDownscaleFactor() {
        blurLayout.setDownscaleFactor(TEST_FLOAT);
        assertEquals(TEST_FLOAT, blurLayout.getDownscaleFactor());
    }

    @Test
    public void setBlurRadiusTest() {
        blurLayout.setBlurRadius(TEST_INT);
        assertEquals(TEST_INT, blurLayout.getBlurRadius());
    }

    @Test
    public void setCornerRadiusTest() {
        blurLayout.setCornerRadius(TEST_FLOAT);
        assertEquals(TEST_FLOAT, blurLayout.getCornerRadius());
    }

    @Test
    public void unlockViewTest() {
        blurLayout.unlockView();
    }

    @Test
    public void lockViewTest() {
        blurLayout.lockView();
    }

    @Test
    public void setLifecycleOwnerTest() {
        blurLayout.setLifecycleOwner(LIFECYCLE_OWNER);
        assertEquals(blurLayout.getLifecycle(), LIFECYCLE_OWNER.getLifecycle());
    }
}
