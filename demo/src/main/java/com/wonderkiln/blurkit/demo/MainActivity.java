package com.wonderkiln.blurkit.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.wonderkiln.blurkit.BlurLayout;

public class MainActivity extends AppCompatActivity {

    private BlurLayout blurLayout;
    private float movement = 1.6f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blurLayout = (BlurLayout) findViewById(R.id.blurLayout);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.animate().scaleX(movement).scaleY(movement).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                movement = movement > 1.2f ? 1.2f : 1.6f;
                imageView.animate().scaleX(movement).scaleY(movement).setDuration(1000).setListener(this).start();
            }
        }).start();
    }

}
