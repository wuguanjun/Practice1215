package com.example.practice1215.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.practice1215.R;
import com.example.practice1215.utils.UtilTools;

/**
 * Created by guanjun on 2016/1/28.
 */
public class AutoDisplayLayout extends ViewGroup {
    private int width;
    private int curLeftPos = 0;
    private int curRightPos = 0;
    private int curRowTopPos = 0;
    private int generalHorizonialMargin = 10;
    private int generalCerticalMargin = 10;
    private int maxChildHeight = 0;

    public AutoDisplayLayout(Context context) {
        super(context);
    }

    public AutoDisplayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoDisplayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.AutoDisplayView);
        generalHorizonialMargin = (int) t.getDimension(R.styleable.AutoDisplayView_child_horizonial_margin, UtilTools.dip2px(getContext(), 10));
        generalCerticalMargin = (int) t.getDimension(R.styleable.AutoDisplayView_child_vertical_margin, UtilTools.dip2px(getContext(), 5));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /* 测量父布局 */
        width = measureSize(widthMeasureSpec, UtilTools.dip2px(getContext(), 240));

        int count = getChildCount();
        int tempMaxChildHeight = 0;
        int tempTotalHeight = 0;
        int tempcurRightPos = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {

                /* 测量子布局 */
                if (child.getMeasuredWidth() > width) {
                    child.measure(width, child.getMeasuredHeight());
                } else
                    child.measure(child.getMeasuredWidth(), child.getMeasuredHeight());

                tempMaxChildHeight = Math.max(tempMaxChildHeight, child.getMeasuredHeight());
                tempcurRightPos += child.getMeasuredWidth();
                if (tempcurRightPos > width) {
                    tempTotalHeight += tempMaxChildHeight;
                    tempMaxChildHeight = child.getMeasuredHeight();
                    tempcurRightPos = child.getMeasuredWidth();
                }
            }
        }

        /* 获取适配子布局后的高度 */
        int parentHeight = tempTotalHeight + tempMaxChildHeight + generalHorizonialMargin;
        setMeasuredDimension(width, parentHeight);
    }

    private int measureSize(int measureSpec, int defaultSize) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int result = defaultSize;

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            result = Math.max(size, result);
        }

        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int count = getChildCount();
        int lineViewCount = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {

                if (i != 0) {
                    /* child 的 left 是上个子 view 的宽加上 generalHorizonialMargin */
                    curLeftPos += getChildAt(i - 1).getMeasuredWidth() + generalHorizonialMargin;
                } else {
                    curLeftPos = 0;
                    curRowTopPos = 0;
                    maxChildHeight = child.getMeasuredHeight();
                }
                /* child 的 right */
                curRightPos = curLeftPos + child.getMeasuredWidth();
                /* 如果 rigth 大于 父布局的宽， 则换行 */
                if (curRightPos > width) {
                    adjustLine(lineViewCount, i); // 调整这一行的子布局的位置
                    lineViewCount = 0;  // 这一行的子 child 的数量充值
                    curRowTopPos += maxChildHeight;
                    curLeftPos = 0;
                    maxChildHeight = child.getMeasuredHeight();
                    curRightPos = child.getMeasuredWidth();
                } else {
                    maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight());
                }

                child.layout(
                        curLeftPos,
                        curRowTopPos,
                        curRightPos,
                        curRowTopPos + child.getMeasuredHeight()
                );

                /* 统计这一行的子view的数量 */
                lineViewCount++;
            }
        }

        /* 调整最后一行子布局的位置 */
        curLeftPos = curRightPos + generalHorizonialMargin;
        adjustLine(lineViewCount, count);

    }

    /* 调整一行，让这一行的子布局水平居中 */
    private void adjustLine(int lineViewCount, int i) {
        curLeftPos = (width - curLeftPos) / 2;
        int generalHorizonialMarginTop;
        for (int lineViewNumber = lineViewCount; lineViewNumber > 0; lineViewNumber--) {
            View lineViewChild = getChildAt(i - lineViewNumber);
            curRightPos = curLeftPos + lineViewChild.getMeasuredWidth();
            if (lineViewChild.getMeasuredHeight() != maxChildHeight) {
                generalHorizonialMarginTop = (maxChildHeight - lineViewChild.getMeasuredHeight()) / 2;
            } else {
                generalHorizonialMarginTop = 0;
            }
            lineViewChild.layout(curLeftPos, curRowTopPos + generalHorizonialMarginTop, curRightPos, curRowTopPos + generalHorizonialMarginTop + lineViewChild.getMeasuredHeight());
            curLeftPos += lineViewChild.getMeasuredWidth() + generalHorizonialMargin;
        }
    }
}
