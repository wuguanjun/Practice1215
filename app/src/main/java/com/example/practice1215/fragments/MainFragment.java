package com.example.practice1215.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.practice1215.R;
import com.example.practice1215.activity.AnimSelectActivity;
import com.example.practice1215.activity.AutoDisplayActivity;
import com.example.practice1215.activity.AutoViewPagerActivity;
import com.example.practice1215.activity.DialogTestActivity;
import com.example.practice1215.activity.MyAnimation;
import com.example.practice1215.activity.NoHttpTestActivity;
import com.example.practice1215.activity.PopTestActivity;
import com.example.practice1215.activity.PullRefreshTestActivity;
import com.example.practice1215.activity.ResOperateTestActivity;
import com.example.practice1215.activity.Sample1;
import com.example.practice1215.activity.Sample2;
import com.example.practice1215.activity.SampleTimesSquareActivity;
import com.example.practice1215.activity.ScanSdImgActivity;
import com.example.practice1215.activity.SelfDefineActivity;
import com.example.practice1215.activity.SlidingTestActivity;
import com.example.practice1215.activity.TakeOrSelectPicActivity;
import com.example.practice1215.activity.TextviewFoldActivity;
import com.example.practice1215.activity.ViewTestActivity;
import com.example.practice1215.activity.VoiceRecordActivity;
import com.example.practice1215.service.DownLoadService;
import com.example.practice1215.view.BadgeView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guanjun on 2015/12/15.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private Button btn_fold_test, btn_scan_sd_img, btn_simple1, btn_simple2, btn_simple_time_square, btn_animator,
            btn_dialog,btn_self_test, btn_auto_viewpager, btn_assess, btn_refresh_view_test, btn_nohttp_test, btn_select_pic, btn_download_service, btn_sliding_test, btn_pop, btn_animation, btn_view_test, btn_voice_record, btn_auto_display, btn_res_test;

    BadgeView downBadge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_act, container,
                false);

        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        btn_fold_test = (Button) view.findViewById(R.id.btn_fold_test);
        btn_scan_sd_img = (Button) view.findViewById(R.id.btn_scan_sd_img);
        btn_simple1 = (Button) view.findViewById(R.id.btn_simple1);
        btn_simple2 = (Button) view.findViewById(R.id.btn_simple2);
        btn_simple_time_square = (Button) view.findViewById(R.id.btn_simple_time_square);
        btn_animator = (Button) view.findViewById(R.id.btn_animator);
        btn_dialog = (Button) view.findViewById(R.id.btn_dialog);
        btn_download_service = (Button) view.findViewById(R.id.btn_download_service);
        btn_pop = (Button) view.findViewById(R.id.btn_pop);
        btn_animation = (Button) view.findViewById(R.id.btn_animation);
        btn_view_test = (Button) view.findViewById(R.id.btn_view_test);
        btn_voice_record = (Button) view.findViewById(R.id.btn_voice_record);
        btn_auto_display = (Button) view.findViewById(R.id.btn_auto_display);
        btn_res_test = (Button) view.findViewById(R.id.btn_res_test);
        btn_sliding_test = (Button) view.findViewById(R.id.btn_sliding_test);
        btn_nohttp_test = (Button) view.findViewById(R.id.btn_nohttp_test);
        btn_select_pic = (Button) view.findViewById(R.id.btn_select_pic);
        btn_refresh_view_test = (Button) view.findViewById(R.id.btn_refresh_view_test);
        btn_assess = (Button) view.findViewById(R.id.btn_assess);
        btn_auto_viewpager = (Button) view.findViewById(R.id.btn_auto_viewpager);
        btn_self_test = (Button) view.findViewById(R.id.btn_self_test);

        btn_assess.setOnClickListener(this);
        btn_self_test.setOnClickListener(this);
        btn_refresh_view_test.setOnClickListener(this);
        btn_select_pic.setOnClickListener(this);
        btn_nohttp_test.setOnClickListener(this);
        btn_scan_sd_img.setOnClickListener(this);
        btn_auto_display.setOnClickListener(this);
        btn_fold_test.setOnClickListener(this);
        btn_simple1.setOnClickListener(this);
        btn_simple2.setOnClickListener(this);
        btn_simple_time_square.setOnClickListener(this);
        btn_animator.setOnClickListener(this);
        btn_dialog.setOnClickListener(this);
        btn_download_service.setOnClickListener(this);
        btn_pop.setOnClickListener(this);
        btn_animation.setOnClickListener(this);
        btn_view_test.setOnClickListener(this);
        btn_voice_record.setOnClickListener(this);
        btn_res_test.setOnClickListener(this);
        btn_sliding_test.setOnClickListener(this);
        btn_auto_viewpager.setOnClickListener(this);
        downBadge = new BadgeView(getActivity(), btn_download_service);
        downBadge.setText("12");
//        downBadge.setBadgeBackgroundColor(Color.RED);
//        downBadge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//        downBadge.setTextColor(Color.BLUE);
//        downBadge.setBadgeMargin(15, 10);
        downBadge.show();
    }

    BroadcastReceiver receiver;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fold_test:
                getActivity().startActivity(new Intent(getActivity(), TextviewFoldActivity.class));
                break;
            case R.id.btn_scan_sd_img:
                getActivity().startActivity(new Intent(getActivity(), ScanSdImgActivity.class));
                break;
            case R.id.btn_simple1:
                getActivity().startActivity(new Intent(getActivity(), Sample1.class));
                break;
            case R.id.btn_simple2:
                getActivity().startActivity(new Intent(getActivity(), Sample2.class));
                break;
            case R.id.btn_simple_time_square:
                getActivity().startActivity(new Intent(getActivity(), SampleTimesSquareActivity.class));
                break;
            case R.id.btn_animator:
                getActivity().startActivity(new Intent(getActivity(), AnimSelectActivity.class));
                break;
            case R.id.btn_dialog:
                getActivity().startActivity(new Intent(getActivity(), DialogTestActivity.class));
                break;
            case R.id.btn_download_service:
                if (downBadge.isShown()) {
                    downBadge.increment(1);
                } else {
                    downBadge.show();
                }
                String url = "http://www.appchina.com/market/d/1603385/cop.baidu_0/com.google.android.apps.maps.apk";
                Intent downloadIntent = new Intent(getActivity(), DownLoadService.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                downloadIntent.putExtras(bundle);
                getActivity().startService(downloadIntent);
                // 设置广播接收器，当新版本的apk下载完成后自动弹出安装界面
                IntentFilter intentFilter = new IntentFilter("com.test.downloadComplete");
                receiver = new BroadcastReceiver() {

                    public void onReceive(Context context, Intent intent) {
//                        Intent install = new Intent(Intent.ACTION_VIEW);
//                        String pathString = intent.getStringExtra("downloadFile");
//                        install.setDataAndType(Uri.fromFile(new File(pathString)), "application/vnd.android.package-archive");
//                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(install);
                        Toast.makeText(context, "Download Completed...", Toast.LENGTH_SHORT).show();
                    }
                };
                getActivity().registerReceiver(receiver, intentFilter);
                break;

            case R.id.btn_pop:
                getActivity().startActivity(new Intent(getActivity(), PopTestActivity.class));
                break;
            case R.id.btn_animation:
                getActivity().startActivity(new Intent(getActivity(), MyAnimation.class));
                break;
            case R.id.btn_view_test:
                List<String> strs = new ArrayList<String>();

                for (int i = 0; i < 10; i++) {
                    strs.add(i + "");
                }
                Intent intent = new Intent();
                intent.setClass(getActivity(), ViewTestActivity.class);
                intent.putExtra("gx", 66);
                intent.putExtra("label", (Serializable) strs);
                startActivity(intent);

//                getActivity().startActivity(new Intent(getActivity(), ViewTestActivity.class).
//                        putExtra("strs", (Serializable) strs));
                break;

            case R.id.btn_voice_record:
                getActivity().startActivity(new Intent(getActivity(), VoiceRecordActivity.class));
                break;
            case R.id.btn_auto_display:
                getActivity().startActivity(new Intent(getActivity(), AutoDisplayActivity.class));
                break;
            case R.id.btn_res_test:
                getActivity().startActivity(new Intent(getActivity(), ResOperateTestActivity.class));
                break;
            case R.id.btn_sliding_test:
                getActivity().startActivity(new Intent(getActivity(), SlidingTestActivity.class));
                break;
            case R.id.btn_nohttp_test:
                getActivity().startActivity(new Intent(getActivity(), NoHttpTestActivity.class));
                break;
            case R.id.btn_select_pic:
                getActivity().startActivity(new Intent(getActivity(), TakeOrSelectPicActivity.class));
                break;
            case R.id.btn_refresh_view_test:
                getActivity().startActivity(new Intent(getActivity(), PullRefreshTestActivity.class));
                break;
            case R.id.btn_assess:
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent1);
                break;
            case R.id.btn_auto_viewpager:
                getActivity().startActivity(new Intent(getActivity(), AutoViewPagerActivity.class));
                break;
            case R.id.btn_self_test:
                getActivity().startActivity(new Intent(getActivity(), SelfDefineActivity.class));
                break;
        }
    }
}
