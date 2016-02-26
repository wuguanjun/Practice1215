package com.example.practice1215.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.practice1215.R;
import com.example.practice1215.activity.baseActivity.BaseActivity;
import com.example.practice1215.utils.BitmapUtils;

import java.io.IOException;
import java.lang.annotation.Target;
import java.net.URI;

/**
 * Created by guanjun on 2016/2/19.
 */
public class TakeOrSelectPicActivity extends BaseActivity {
    private static final int FROM_PHOTO = 0x0001;
    private static final int FROM_CAMERA = 0x0002;

    private ImageView img_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("图片");
        initView();
    }

    private void initView() {
        img_pic = (ImageView) findViewById(R.id.img_pic);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_photo:
                getPicFromPhoto();
                break;
            case R.id.btn_camera:
                getPicFromCamera();
                break;
        }
    }

    /**
     * 从相册
     */
    private void getPicFromPhoto() {
        Intent photoIntent = new Intent(Intent.ACTION_PICK);
        photoIntent.setType("image/*");
        startActivityForResult(photoIntent, FROM_PHOTO);
    }

    /**
     * 拍照
     */
    private void getPicFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(cameraIntent, FROM_CAMERA);
        } else {
            Toast.makeText(this, "请检查SD卡是否插好...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FROM_CAMERA) {
            Uri uri = data.getData();
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            img_pic.setImageBitmap(bitmap);
        } else if (requestCode == FROM_PHOTO) {
            if (data == null) return;
            Uri uri = data.getData();
            ContentResolver resolver = getContentResolver();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
                if (bitmap != null) {
                    //防止图片过大而溢出
                    Bitmap pressBitmap = BitmapUtils.compressByScaleWithBitmap(bitmap);
                    img_pic.setImageBitmap(pressBitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
