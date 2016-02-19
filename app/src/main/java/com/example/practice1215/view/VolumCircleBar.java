package com.example.practice1215.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.practice1215.R;

/**
 * Created by guanjun on 2016/2/3.
 */
public class VolumCircleBar extends View {
    private double volumRate; // 音量百分比
    private boolean isRecording; // 录音标志
    private Object lock = new Object();
    private Thread uiThread;
    private Paint mPaint;
    private RectF arcRect;
    private Matrix matrix = new Matrix();
    private final int VOLUM_INDICATE_LENGTH = 5; // 音量大小线长度
    private final int CIRCLE_INNER_DISTANCE_TO_OUTSIDE = 5; // 内切圆距离外圆的距离

    public VolumCircleBar(Context context) {
        this(context, null);
    }

    public VolumCircleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumCircleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VolumCircleBar, defStyle, 0);
        init(typedArray);
    }

    private int recordingColor; // 录音背景色
    private int stoppedColor; // 停止背景色
    private Bitmap centerRes; // 中间麦克风图片
    private int totalBlockCount; // 块数量
    private int spliteAngle; // 块之间的间隔角度大小
    private int circleWidth; // 直径

    /**
     * 初始化
     */
    private void init(TypedArray typedArray) {
        for (int i = 0; i < typedArray.length(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.VolumCircleBar_recordingColor:
                    recordingColor = typedArray.getColor(i, Color.GREEN);
                    break;
                case R.styleable.VolumCircleBar_stoppedColor:
                    stoppedColor = typedArray.getColor(i, Color.GRAY);
                    break;
                case R.styleable.VolumCircleBar_centerRes:
                    centerRes = BitmapFactory.decodeResource(getContext().getResources(), typedArray.getResourceId(i, R.drawable.ball));
                    break;
                case R.styleable.VolumCircleBar_blockCount:
                    totalBlockCount = typedArray.getInt(i, 50);
                    break;
                case R.styleable.VolumCircleBar_splitAngle:
                    spliteAngle = typedArray.getInt(i, 2);
                    break;
            }
        }
        typedArray.recycle();
        uiThread = Thread.currentThread();
        mPaint = new Paint();
        if (spliteAngle * totalBlockCount > 360) {
            throw new IllegalArgumentException("spliteAngle * blockCount > 360, while the result should be less than 360.");
        }

        // debug for test
        isRecording = true;
        volumRate = 0.5;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 直径
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        circleWidth = width > height ? width : height;
        if (arcRect == null) {
            arcRect = new RectF(CIRCLE_INNER_DISTANCE_TO_OUTSIDE, CIRCLE_INNER_DISTANCE_TO_OUTSIDE,
                    circleWidth - CIRCLE_INNER_DISTANCE_TO_OUTSIDE, circleWidth - CIRCLE_INNER_DISTANCE_TO_OUTSIDE); // 音量显示区域, 内偏移几个像素
            // 图片处理矩阵
            initBitmapMatrix();
        }
        // 强制设置view大小
        setMeasuredDimension(circleWidth, circleWidth);
    }

    /**
     * 中间图片压缩处理
     */
    private void initBitmapMatrix() {
        float innerCircleRadius = (circleWidth - 2 * (VOLUM_INDICATE_LENGTH + CIRCLE_INNER_DISTANCE_TO_OUTSIDE)) / 2f; // 内圆的半径
        float innerRectangleWidth = (float) Math.cos((Math.PI / 180) * 45) * innerCircleRadius * 2; // 内圆的内切正方形的边长
        float translateOffset = VOLUM_INDICATE_LENGTH + CIRCLE_INNER_DISTANCE_TO_OUTSIDE + innerCircleRadius - innerRectangleWidth / 2; // 偏移的offset
        if (centerRes.getWidth() > (innerRectangleWidth) || centerRes.getHeight() > (innerRectangleWidth)) {
            // 图片宽度或高度大于(直径-内偏移), 等比压缩
            if (centerRes.getWidth() > centerRes.getHeight()) {
                // 按照宽度压缩
                float ratio = innerRectangleWidth / centerRes.getWidth();
                matrix.postScale(ratio, ratio);
                float translateY = (innerRectangleWidth - (centerRes.getHeight() * ratio)) / 2f;
                // 在纵坐标方向上进行偏移，以保证图片居中显示
                matrix.postTranslate(translateOffset, translateY + translateOffset);
            } else {
                // 按照高度压缩
                float ratio = innerRectangleWidth / (centerRes.getHeight() * 1.0f);
                matrix.postScale(ratio, ratio);
                float translateX = (innerRectangleWidth - (centerRes.getWidth() * ratio)) / 2f;
                // 在横坐标方向上进行偏移，以保证图片居中显示
                matrix.postTranslate(translateX + translateOffset, translateOffset);
            }
        } else {
            // 当图片的宽高都小于屏幕宽高时，直接让图片居中显示
            float translateX = (innerRectangleWidth - centerRes.getWidth()) / 2f;
            float translateY = (innerRectangleWidth - centerRes.getHeight()) / 2f;
            matrix.postTranslate(translateX + translateOffset, translateY + translateOffset);
        }
    }

    /**
     * 设置音量百分比
     *
     * @param rate
     */
    public void updateVolumRate(double rate) {
        synchronized (lock) {
            this.volumRate = rate;
            if (Thread.currentThread() != uiThread) {
                postInvalidate();
            } else {
                invalidate();
            }
        }
    }

    /**
     * 开始、停止录音
     */
    public void toggleRecord() {
        synchronized (lock) {
            isRecording = !isRecording;
            if (Thread.currentThread() != uiThread) {
                postInvalidate();
            } else {
                invalidate();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        synchronized (lock) {
            if (isRecording) // 正在录音
            {
                //1.绘制绿色圆圈
                mPaint.setAntiAlias(true); //消除锯齿
                mPaint.setColor(recordingColor);
                mPaint.setStrokeWidth(1);
                mPaint.setStyle(Paint.Style.FILL); // 填充
                canvas.drawCircle(circleWidth / 2f, circleWidth / 2f, circleWidth / 2f, mPaint);
                //2.根据音量百分比、块数量、块间隔大小计算角度动态绘制音量大小
                // 计算块的角度
                float blockAngle = (360 * 1.0f - spliteAngle * totalBlockCount) / totalBlockCount;
                int drawBlockCount = (int) (totalBlockCount * volumRate); // 绘制的block数量
                mPaint.setStrokeWidth(VOLUM_INDICATE_LENGTH);
                mPaint.setColor(stoppedColor);
                mPaint.setStyle(Paint.Style.STROKE); // 空心
                for (int i = 0; i < drawBlockCount; i++) {
                    canvas.drawArc(arcRect, i * (blockAngle + spliteAngle) - 90, blockAngle, false, mPaint);
                }
            } else // 录音停止
            {
                //1.绘制灰色圆圈
                mPaint.setColor(stoppedColor);
                mPaint.setStrokeWidth(1);
                mPaint.setStyle(Paint.Style.FILL); // 填充
                canvas.drawCircle(circleWidth / 2f, circleWidth / 2f, circleWidth / 2f, mPaint);
            }
        }
        // 绘制中间话筒
        canvas.drawBitmap(centerRes, matrix, null);
    }
}
