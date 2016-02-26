package com.example.practice1215.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.practice1215.R;
import com.example.practice1215.interfaces.OnRefreshListener;

import java.text.SimpleDateFormat;

/**
 * Created by guanjun on 2016/2/25.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    private int firstVisibleItemPosition = 0; // 屏幕显示在第一个的item的索引
    private View rootView;       //顶部刷新布局
    private View footerView;     //底部加载布局
    private ImageView img_root;
    private ProgressBar pb_root;
    private TextView tv_root_state;
    private TextView tv_refresh_time;

    private ImageView img_footer;
    private ProgressBar pb_footer;
    private TextView tv_footer_state;
    private TextView tv_loading_time;

    private int rootHeight;     //顶部布局高度
    private int footerHeight;   //底部布局高度

    private OnRefreshListener onRefreshListener;   //刷新和加载的监听

    private int downY;    //下拉时y轴的偏移量

    private final int NORMAL = 0;          //正常状态
    private final int DOWN_REFRESH = 1;    //下拉
    private final int RELEASH_TO_REFRESH = 5;    //松开刷新
    private final int UP_DOWNLOAD = 2;     //上拉加载
    private final int REFRESHING = 3;      //刷新中
    private final int REFRESH_DONE = 6;     //刷新完成
    private final int LOADING = 4;         //加载中

    private int state = NORMAL;  //默认正常状态
    private boolean isLoadingMore = false;  //是否加载更多
    private boolean isScrollToBottom = false; //是否滑动到底部

    private Animation refreshAnim;    //刷新动画：箭头由朝下变成朝上
    private Animation loadingAnim;    //加载动画：箭头由朝上变成朝下

    /**
     * 设置刷新加载的监听
     *
     * @param onRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public RefreshListView(Context context) {
        super(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRootView();
        initFooterView();
        initAnim();
        this.setOnScrollListener(this);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化顶部rootView
     */
    private void initRootView() {
        rootView = View.inflate(getContext(), R.layout.pull_refresh_root, null);
        img_root = (ImageView) rootView.findViewById(R.id.img_refresh_icon);
        pb_root = (ProgressBar) rootView.findViewById(R.id.pb_refresh);
        tv_root_state = (TextView) rootView.findViewById(R.id.tv_remain_refresh);
        tv_refresh_time = (TextView) rootView.findViewById(R.id.tv_current_refresh_time);

        if (getLastRefreshTiem().equals("")) {
            tv_refresh_time.setText("以前未刷新过");
        } else {
            tv_refresh_time.setText("上次刷新时间：" + getLastRefreshTiem());
        }

        rootView.measure(0, 0);
        rootHeight = rootView.getMeasuredHeight();
        rootView.setPadding(0, -rootHeight, 0, 0);
        this.addHeaderView(rootView);
    }

    /**
     * 获取上次刷新时间
     *
     * @return
     */
    private String getLastRefreshTiem() {
        SharedPreferences sp = getContext().getSharedPreferences("RefreshView", Context.MODE_PRIVATE);
        return sp.getString("LastRefreshTime", "");
    }

    /**
     * 保存本次刷新时间
     *
     * @param time
     */
    private void saveRefreshTime(String time) {
        SharedPreferences sp = getContext().getSharedPreferences("RefreshView", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("LastRefreshTime", time);
        editor.commit();
    }

    /**
     * 初始化底部footerView
     */
    private void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.pull_refresh_footer, null);
        img_footer = (ImageView) footerView.findViewById(R.id.img_loading_icon);
        pb_footer = (ProgressBar) footerView.findViewById(R.id.pb_loading);
        tv_footer_state = (TextView) footerView.findViewById(R.id.tv_remain_loading);
        tv_loading_time = (TextView) footerView.findViewById(R.id.tv_current_loading_time);

        if (getLastRefreshTiem().equals("")) {
            tv_loading_time.setText("");
        } else {
            tv_loading_time.setText("上次加载时间：" + getLastRefreshTiem());
        }

        footerView.measure(0, 0);
        footerHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerHeight, 0, 0);
        this.addFooterView(rootView);
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        refreshAnim = new RotateAnimation(0, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        refreshAnim.setDuration(500);
        refreshAnim.setFillAfter(true);

        loadingAnim = new RotateAnimation(-180, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        loadingAnim.setDuration(500);
        loadingAnim.setFillAfter(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                state = DOWN_REFRESH;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                int diff = (moveY - downY);
                int padingTop = -rootHeight + diff;

                if (firstVisibleItemPosition == 0 && -rootHeight < padingTop) {
                    if ((-padingTop) >= 0) {
                        img_root.startAnimation(refreshAnim);
                    } else {
                    }
                    if (padingTop >= 0 && state == DOWN_REFRESH) {
                        state = RELEASH_TO_REFRESH;
                        refresh();
                    } else if (padingTop < 0 && state == RELEASH_TO_REFRESH) {
                        state = DOWN_REFRESH;
                        refresh();
                    }
                    rootView.setPadding(0, padingTop, 0, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELEASH_TO_REFRESH) {
                    rootView.setPadding(0, 0, 0, 0);
                    state = REFRESHING;
                    refresh();
                    if (onRefreshListener != null) {
                        onRefreshListener.onRefresh();
                    }
                } else if (state == DOWN_REFRESH) {
                    rootView.setPadding(0, -rootHeight, 0, 0);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新结束
     */
    public void onRefreshComleted() {
        state = REFRESH_DONE;
        refresh();
    }

    /**
     * 不同刷新状态的处理
     */
    private void refresh() {
        switch (state) {
            case DOWN_REFRESH:
                pb_root.setVisibility(View.GONE);
                img_root.setVisibility(View.VISIBLE);
                tv_root_state.setText("下拉刷新");
                break;
            case RELEASH_TO_REFRESH:
                tv_root_state.setText("松开后刷新...");
                if (getLastRefreshTiem().equals("")) {
                    tv_refresh_time.setText("未刷新过..." + getLastRefreshTiem());
                } else {
                    tv_refresh_time.setText("上次刷新时间：" + getLastRefreshTiem());
                }
                break;
            case REFRESHING:
                img_root.clearAnimation();
                img_root.setVisibility(View.GONE);
                pb_root.setVisibility(View.VISIBLE);
                tv_root_state.setText("正在刷新...");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String curTime = sdf.format(System.currentTimeMillis());
                tv_refresh_time.setText("当前刷新时间：" + curTime);
                saveRefreshTime(curTime);
                break;
            case REFRESH_DONE:
                rootView.setPadding(0, -rootHeight, 0, 0);
                img_root.clearAnimation();
                img_root.setVisibility(View.VISIBLE);
                pb_root.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        firstVisibleItemPosition = firstVisibleItem;
        if (getLastVisiblePosition() == (totalItemCount - 1)) {
            isScrollToBottom = true;
        } else {
            isScrollToBottom = false;
        }
    }
}
