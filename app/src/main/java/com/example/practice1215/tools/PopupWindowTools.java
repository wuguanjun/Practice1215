package com.example.practice1215.tools;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * Created by guanjun on 2016/1/5.
 */
public class PopupWindowTools {
    static PopupWindowTools popTool;
    static PopupWindow pop;

    static Context context;
    boolean isTouchOutSideDismiss = true;

    private PopupWindowTools(Context context) {
        this.context = context;
    }

    public static PopupWindowTools getInstances(Context context) {
        popTool = new PopupWindowTools(context);
        pop = new PopupWindow(context);
        return popTool;
    }

    public void show(View view) {
        pop.showAsDropDown(view);
    }

    public static PopupWindowTools initPop(int resid) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        final View contentView = LayoutInflater.from(context).inflate(resid, null);
        pop.setContentView(contentView);
        pop.setWidth((int) (100 * metrics.density));
        pop.setHeight((int) (150 * metrics.density));

//        pop.setWidth(contentView.getMeasuredWidth());
//        pop.setHeight(contentView.getMeasuredHeight());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        return popTool;
    }

    public void setTouchOutSideDismiss(boolean isTouchOutSideDismiss) {
        this.isTouchOutSideDismiss = isTouchOutSideDismiss;
    }

    ;

    public void showAtViewLeft(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Toast.makeText(context, pop.getWidth() + "", Toast.LENGTH_SHORT).show();
        pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - pop.getWidth(), location[1] + 15);
    }

    public void showAtViewRight(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth(), location[1]);
    }

    public void showUponView(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - pop.getHeight());
    }

    public void showBelowView(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] + view.getHeight());
    }
}
