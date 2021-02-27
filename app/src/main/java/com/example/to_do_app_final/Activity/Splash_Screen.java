package com.example.to_do_app_final.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.to_do_app_final.R;
import com.example.to_do_app_final.manager.PrefranceManager;

public class Splash_Screen extends AppCompatActivity {
    ImageView ivTOp,ivHeart,ivBottom;
    TextView tvTextview;

    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        ivTOp = findViewById(R.id.iv_top);
        ivBottom = findViewById(R.id.iv_bottom);
        ivHeart = findViewById(R.id.iv_heart);
        tvTextview = findViewById(R.id.tv_text);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // for animation top

        Animation animationtop = AnimationUtils.loadAnimation(this,R.anim.top_wave);
        ivTOp.setAnimation(animationtop);


        // for animation bottom
        Animation animationBottom = AnimationUtils.loadAnimation(this,R.anim.bottom_wave);
        ivBottom.setAnimation(animationBottom);

        // for heart wave
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(ivHeart,
                PropertyValuesHolder.ofFloat("scaleX",1.2f),
                PropertyValuesHolder.ofFloat("scaleY",1.2f));
        objectAnimator.setDuration(200);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();
        animatText("WELCOME");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(Splash_Screen.this, Login.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                new PrefranceManager(Splash_Screen.this).loginChecker();
                finish();
            }
        },4000);

    }


    // for Runnable method
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tvTextview.setText(charSequence.subSequence(0,index++));
            if (index<=charSequence.length()){
                handler.postDelayed(runnable,delay);
            }
        }
    };





    private void animatText(CharSequence cs) {
        charSequence = cs;
        index = 0;
        tvTextview.setText("");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,delay);


    }
}