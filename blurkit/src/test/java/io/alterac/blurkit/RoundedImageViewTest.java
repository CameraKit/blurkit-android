package io.alterac.blurkit;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;

public class RoundedImageViewTest {

    @Mock
    private Context mockContext;

    private static final float TEST_FLOAT = 1.1f;

    private RoundedImageView roundedImageView;

    @Before
    public void setupTests() {
        MockitoAnnotations.initMocks(this);

        roundedImageView = new RoundedImageView(mockContext);
    }

    @Test
    public void setCornerRadiusTest() {
        roundedImageView.setCornerRadius(TEST_FLOAT);
        assertEquals(TEST_FLOAT, roundedImageView.getCornerRadius());
    }
}
