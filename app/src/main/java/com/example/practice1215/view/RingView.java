package com.example.practice1215.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.practice1215.R;

/**
 * Created by guanjun on 2016/5/10.
 */
public class RingView extends View {
    public final String TAG = "RingView";
    private int[] colors;
    private int part;//分成几份:默认4份
    private float values[]; //每一份的份额,默认等分
    private float total = 0;
    private int radiusN;   //内圆半径:默认比外圆小10
    private int radiusW;   //外圆半径
    private Paint mPaint;
    private int textColor;
    Context context;
    int centerPointX;
    int centerPointY;
    private float textSize;

    private String totalMoney = "0.00";
    private boolean textIsDisplayable = true;

    public RingView(Context context) {
        this(context, null);
    }

    public RingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Log.e(TAG, "super(context, attrs);");
//        this.context = context;

//        radiusN = dip2px(context, 100);
//        radiusW = dip2px(context, 120);
    }

    public RingView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
        Log.e(TAG, "super(context, attrs, defStyleAttr);");
        this.context = context;
        mPaint = new Paint();
        colors = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.GRAY, Color.CYAN};//默认颜色
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.RingView);
        textSize = t.getDimension(R.styleable.RingView_ringtextSize, sp2px(context, 18));
        textColor = t.getColor(R.styleable.RingView_ringtextColor, Color.BLACK);
        textIsDisplayable = t.getBoolean(R.styleable.RingView_ringtextIsDisplayable, true);
        part = t.getInteger(R.styleable.RingView_part, 4);
        t.recycle();
        values = new float[part];
        radiusN = dip2px(context, 50);
        radiusW = dip2px(context, 80);
        for (int i = 0; i < part; i++) {
            values[i] = 10 * (i + 1);
            total += values[i];
        }

        Log.e(TAG, values.length + "");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        Log.e(TAG, "super(context, attrs, defStyleAttr, defStyleRes);");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "super.onDraw(canvas);");
        Log.e(TAG, part + "");
        Log.e(TAG, total + "");
        radiusN = dip2px(context, 50);
        radiusW = dip2px(context, 80);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        centerPointX = getWidth() / 2;
        centerPointY = getHeight() / 2;
        float textWidth = mPaint.measureText("总资产" + totalMoney);   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        mPaint.setStrokeWidth(0);
        mPaint.setColor(textColor);
        canvas.drawText("总资产" + totalMoney, centerPointX - textWidth / 2, centerPointY + textSize / 2, mPaint);
        mPaint.setStrokeWidth(dip2px(context, 10));
        drawRing(canvas);

        super.onDraw(canvas);
    }

    private void drawRing(Canvas canvas) {
        int curAngel = 0;
        RectF oval = null;
        for (int i = 0; i < part; i++) {
            mPaint.setColor(colors[i % (colors.length)]);
            Log.e(TAG, i + "," + curAngel + "");
            oval = new RectF(centerPointX - radiusW, centerPointY - radiusW, centerPointX + radiusW, centerPointY + radiusW);  //用于定义的圆弧的形状和大小的界限
            canvas.drawArc(oval, curAngel, values[i] / total * 360, false, mPaint);
            curAngel += values[i] / total * 360;
            if (curAngel >= 360) {
                curAngel = 0;
            }
        }
    }

    public void setTotalMoney(String str) {
        this.totalMoney = str;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setValues(float[] values) {
        this.values = values;
        this.part = values.length;
        total = 0;
        for (int i = 0; i < part; i++) {
            total += values[i];
            Log.e(TAG, "setvalue: " + values[i]);
        }

        Log.e(TAG, "setvalue: " + total);
        Log.e(TAG, "setvalue: " + part);
    }

    public float[] getValues() {
        return values;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public int[] getColors() {
        return colors;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getTextSize() {
        return textSize;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
