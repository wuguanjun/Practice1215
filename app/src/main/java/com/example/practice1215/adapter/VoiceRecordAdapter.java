package com.example.practice1215.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice1215.R;

import java.io.IOException;
import java.util.List;

/**
 * Created by guanjun on 2016/1/27.
 */
public class VoiceRecordAdapter extends BaseAdapter implements View.OnClickListener {
    List<String> paths;
    Context context;
    MediaPlayer mPlayer;

    public VoiceRecordAdapter(Context context, List<String> paths) {
        this.paths = paths;
        this.context = context;
        initPlayer();
    }

    private void initPlayer() {
        mPlayer = new MediaPlayer();
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.record_item, null);
            holder = new ViewHolder();
            holder.tv_path = (TextView) convertView.findViewById(R.id.tv_record_name);
            holder.btn_show = (Button) convertView.findViewById(R.id.btn_show);
            holder.btn_stop = (Button) convertView.findViewById(R.id.btn_stop);

            pos = position;
            holder.tv_path.setText(paths.get(position));
            holder.btn_show.setOnClickListener(this);
            holder.btn_stop.setOnClickListener(this);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show:
                startPlay();
                break;
            case R.id.btn_stop:
                stopPlay();
                break;
        }
    }

    int pos;

    private void startPlay() {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(paths.get(pos));
            Toast.makeText(context, "startPlay  ", Toast.LENGTH_SHORT).show();
            if (!mPlayer.isPlaying()) {
                mPlayer.prepare();
                mPlayer.start();
            } else {
                mPlayer.pause();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlay() {
        mPlayer.stop();
        Toast.makeText(context, "stopPlay  ", Toast.LENGTH_SHORT).show();
    }

    class ViewHolder {
        TextView tv_path;
        Button btn_show;
        Button btn_stop;
    }
}
