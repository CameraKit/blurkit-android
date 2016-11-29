package com.wonderkiln.blurkit.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wonderkiln.blurkit.BlurLayout;

public class MainActivity extends AppCompatActivity {

    private BlurLayout blurLayout;
    private Button showFragmentButton;
    private float movement = 1.6f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blurLayout = (BlurLayout) findViewById(R.id.blurLayout);
        showFragmentButton = (Button)findViewById(R.id.showFragmentButton);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.animate().scaleX(movement).scaleY(movement).setDuration(1500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                movement = movement > 1.2f ? 1.2f : 1.6f;
                imageView.animate().scaleX(movement).scaleY(movement).setDuration(1500).setListener(this).start();
            }
        }).start();

        showFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.pull_in_top, R.anim.push_out_bottom, R.anim.pull_in_bottom, R.anim.push_out_top)
                    .add(android.R.id.content, new TestFragment())
                    .addToBackStack(null)
                    .commit();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        blurLayout.startBlur();
    }

    @Override
    protected void onStop() {
        super.onStop();
        blurLayout.pauseBlur();
    }
}
