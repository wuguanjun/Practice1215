package com.example.practice1215.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.practice1215.R;
import com.example.practice1215.activity.baseActivity.BaseActivity;

import java.util.Objects;

/**
 * Created by guanjun on 2015/12/29.
 */
public class AnimSelectActivity extends BaseActivity {
    ImageView img_ball;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("Animation");
        setTitleLeftText("return");

        initView();
    }

    private void initView() {
        img_ball = (ImageView) findViewById(R.id.img_ball);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.img_anim:
                propertyValuesHolder(view);
                break;
            case R.id.btn_vertival:
                verticalRun(img_ball);
                break;
            case R.id.btn_drop:
                droplRun(img_ball);
                break;
            case R.id.btn_together:
//                togetherRun(img_ball);

                playWithAfter(img_ball);
                break;
        }
    }


    public void togetherRun(View view) {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX",
                1.0f, 2f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY",
                1.0f, 2f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(2000);
        animSet.setInterpolator(new LinearInterpolator());
        //两个动画同时执行
        animSet.playTogether(anim1, anim2);
        animSet.start();
    }

    public void playWithAfter(View view)
    {
        float cx = view.getX();

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX",
                1.0f, 2f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY",
                1.0f, 2f);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(view,
                "x",  cx ,  0f);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(view,
                "x", cx);

        /**
         * anim1，anim2,anim3同时执行
         * anim4接着执行
         */
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1).with(anim2);
        animSet.play(anim2).with(anim3);
        animSet.play(anim4).after(anim3);
        animSet.setDuration(1000);
        animSet.start();
    }

    public void verticalRun(final View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 90);
        animator.setTarget(view);
        animator.setDuration(1000).start();
//      animator.setInterpolator(value)
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
    }

    public void droplRun(final View view) {
        ValueAnimator valueAnimator = new ValueAnimator();
//        valueAnimator.setTarget(view);
        valueAnimator.setDuration(1000);
        valueAnimator.setObjectValues(new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue,
                                   PointF endValue) {
                // x方向200px/s ，则y方向0.5 * 10 * t
                PointF point = new PointF();
                point.x = 200 * fraction * 3;
                point.y = 0.5f * 200 * (fraction * 3) * (fraction * 3);
                return point;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                view.setX(point.x);
                view.setY(point.y);
            }
        });
    }

    public void propertyValuesHolder(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,
                0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,
                0, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,
                0, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(1000).start();
    }

    public void rotateyAnimRun(final View view) {
        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(view, "zhy", 1.0F, 0.0F, 1.0f)//
                .setDuration(1000);//
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                view.setAlpha(cVal);
                view.setScaleX(cVal);
                view.setScaleY(cVal);
            }
        });
    }


    /**
     * 旋转
     *
     * @param view
     */
    private void rotateY(View view) {
        ObjectAnimator.ofFloat(view, "rotationZ", 0.0f, 360.0f, 0.0f).setDuration(2000).start();
    }
}
