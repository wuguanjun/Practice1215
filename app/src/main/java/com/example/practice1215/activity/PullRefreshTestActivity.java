package com.example.practice1215.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.practice1215.R;
import com.example.practice1215.activity.baseActivity.BaseActivity;
import com.example.practice1215.adapter.LvAdapter;
import com.example.practice1215.interfaces.OnRefreshListener;
import com.example.practice1215.view.MyListView;
import com.example.practice1215.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by guanjun on 2016/2/25.
 */
public class PullRefreshTestActivity extends BaseActivity {
    private List<String> textList;
    private MyAdapter adapter;
    private RefreshListView rListView;

    private List<String> list;
    private MyListView lv;
    private LvAdapter lvadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleText("RefreshList");
        rListView = (RefreshListView) findViewById(R.id.rlv_test);
        textList = new ArrayList<String>();
        for (int i = 0; i < 25; i++) {
            textList.add("这是一条ListView的数据" + i);
        }

        adapter = new MyAdapter();
        rListView.setAdapter(adapter);
        rListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                addItem();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x111);
                    }
                }, 1000);
            }

            @Override
            public void onLoading() {

            }
        });
    }

    private void addItem() {
        for (int i = 0; i < 5; i++) {
            textList.add("这是一条ListView的数据" + textList.size());
        }

        adapter.notifyDataSetChanged();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            rListView.onRefreshComleted();
        }
    };

    public void initMyList() {

        list = new ArrayList<String>();
        list.add("loonggg");
        list.add("我们都是开发者1");
        list.add("我们都是开发者2");
        list.add("我们都是开发者3");
        list.add("我们都是开发者4");
        list.add("我们都是开发者6");
        list.add("我们都是开发者5");
        list.add("我们都是开发者7");
        list.add("我们都是开发者");
        list.add("我们都是开发者");
        list.add("我们都是开发者");
        list.add("我们都是开发者");
        list.add("我们都是开发者");
        list.add("我们都是开发者");
        list.add("我们都是开发者");
        list.add("我们都是开发者");
        list.add("我们都是开发者");
        lvadapter = new LvAdapter(list, this);
        lv.setAdapter(adapter);

        lv.setonRefreshListener(new MyListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        list.add("刷新后添加的内容");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        adapter.notifyDataSetChanged();
                        lv.onRefreshComplete();
                    }
                }.execute(null, null, null);
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return textList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return textList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            TextView textView = new TextView(PullRefreshTestActivity.this);
            textView.setText(textList.get(position));
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(18.0f);
            return textView;
        }

    }

}
