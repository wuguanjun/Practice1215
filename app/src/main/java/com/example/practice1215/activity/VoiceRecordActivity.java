package com.example.practice1215.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.practice1215.R;
import com.example.practice1215.activity.baseActivity.BaseActivity;
import com.example.practice1215.adapter.VoiceRecordAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by guanjun on 2016/1/27.
 */
public class VoiceRecordActivity extends BaseActivity {
    EditText et_save_name;
    Button btn_start, btn_save;
    ListView lv_record;
    VoiceRecordAdapter adapter;

    //录音
    MediaRecorder mRecorder;

    String savePath;
    List<String> paths;

    File recordFile;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("录音");
        initValue();
        initView();

//        initTools();
    }

    private void initView() {
        btn_save = (Button) findViewById(R.id.btn_pause_save);
        btn_start = (Button) findViewById(R.id.btn_start_record);
        lv_record = (ListView) findViewById(R.id.lv_record_item);
        et_save_name = (EditText) findViewById(R.id.et_file_name);

        lv_record.setAdapter(adapter);

        btn_start.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    private void initValue() {
        paths = new ArrayList<String>();
        adapter = new VoiceRecordAdapter(this, paths);
        try {
            savePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/XIONGRECORDERS/";
            File file = new File(savePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTools() {
        mRecorder = new MediaRecorder();
        //从麦克风进行录音
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        //设置输出格式
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //设置编码格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_record:
                startRecord();
                break;
            case R.id.btn_pause_save:
                pauseAndSave();
                break;
        }
    }

    /**
     * 开始录音
     */
    private void startRecord() {
//        mRecorder.start();
        if (mRecorder == null) {
            initTools();
        }
        btn_start.setEnabled(false);
        if (null == et_save_name.getText() || ((et_save_name.getText()).toString().trim().equals(""))) {
            savePath += getFileName() + ".amr";
            Toast.makeText(this, "file  " + savePath, Toast.LENGTH_SHORT).show();
        } else {
            savePath += et_save_name.getText() + ".amr";
            Toast.makeText(this, "et_save_name" + savePath, Toast.LENGTH_SHORT).show();
        }
        try {
            recordFile = new File(savePath);
            mRecorder.setOutputFile(recordFile.getAbsolutePath());
            recordFile.createNewFile();
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
        } catch (IllegalStateException e) {
        }
    }

    private void pauseAndSave() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("是否保存文件？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onsave();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recordFile.delete();
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if (mRecorder != null) {
            mRecorder.release();
        }
        super.onDestroy();
    }

    private void onsave() {
        btn_start.setEnabled(true);
        paths.add(savePath);
        adapter.notifyDataSetChanged();
    }

    private String getFileName() {
        String fileName = "";
        Date date = new Date();
        long million = date.getTime();
        fileName += million;
        return fileName;
    }
}
