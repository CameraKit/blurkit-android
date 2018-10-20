package io.alterac.blurkit.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.alterac.blurkit.BlurLayout;

public class MainActivity extends AppCompatActivity {

    private BlurLayout blurLayout;
    private float movement = 150;

    private ObjectAnimator mAlphaAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blurLayout = findViewById(R.id.blurLayout);

        blurLayout.animate().translationY(movement).setDuration(1500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                movement = movement > 0 ? -150 : 150;
                blurLayout.animate().translationY(movement).setDuration(1500).setListener(this).start();
            }
        }).start();

        final Button alphaAnimationButton = findViewById(R.id.alpha_animation_button);
        alphaAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlphaAnimation != null && !mAlphaAnimation.isPaused()) {
                    mAlphaAnimation.pause();
                } else {
                    if (mAlphaAnimation == null) {
                        mAlphaAnimation = ObjectAnimator.ofFloat(
                                blurLayout, "alpha", 0f, 1f);
                        mAlphaAnimation.setRepeatCount(ObjectAnimator.INFINITE);
                        mAlphaAnimation.setRepeatMode(ObjectAnimator.REVERSE);
                        mAlphaAnimation.setDuration(1000);
                        mAlphaAnimation.start();
                    } else {
                        mAlphaAnimation.resume();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        blurLayout.startBlur();
        blurLayout.lockView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        blurLayout.pauseBlur();
    }
}
