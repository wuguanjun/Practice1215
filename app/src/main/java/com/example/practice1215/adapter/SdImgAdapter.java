package com.example.practice1215.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.practice1215.R;
import com.example.practice1215.utils.BitmapUtils;
import com.example.practice1215.utils.ImageShowManager;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by guanjun on 2015/12/17.
 */
public class SdImgAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> imgUrls;
    private ImageShowManager imageManager;

    public SdImgAdapter(Context context, ArrayList<String> imgUrls) {
        this.context = context;
        this.imgUrls = imgUrls;

        imageManager = ImageShowManager.from(context);
    }

    @Override
    public int getCount() {
        return imgUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imgUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.sd_img_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img_sd);
            holder.tv_img_info = (TextView) convertView.findViewById(R.id.tv_img_info);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setTag(holder);

        //1.直接用picasso加载
//        Picasso.with(context).load(new File(imgUrls.get(position))).noFade()
//                .into(holder.img);
        //glide
        Glide.with(context).load(new File(imgUrls.get(position))).centerCrop().into(holder.img);
        //3.用ImageLoader加载
//        ImageLoaderUtils.getInstance().displayFromSDCard(imgUrls.get(position), holder.img);
//        2.本地缓存，用线程加载(失真比较严重)
//        if (cancelPotentialLoad(imgUrls.get(position), holder.img)) {
//            AsyncLoadImageTask task = new AsyncLoadImageTask(holder.img);
//            holder.img.setImageDrawable(new LoadingDrawable(task));
//            task.execute(imgUrls.get(position));
//        }
        holder.tv_img_info.setText(imgUrls.get(position));
        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView tv_img_info;
    }


    private boolean cancelPotentialLoad(String url, ImageView imageView) {
        AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageView);
        if (loadImageTask != null) {
            String bitmapUrl = loadImageTask.url;
            if ((bitmapUrl == null) || bitmapUrl.equals(url)) {
                loadImageTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    class AsyncLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String url = null;

        public AsyncLoadImageTask(ImageView imageView) {
            super();
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            this.url = params[0];

            bitmap = imageManager.getBitmapFromMemory(url);
            if (bitmap != null) {
                return bitmap;
            }

            bitmap = imageManager.getBitmapFormDisk(url);
            if (bitmap != null) {
                imageManager.putBitmapToMemery(url, bitmap);
                return bitmap;
            }

            bitmap = BitmapUtils.getBitmapThumbnail(url, ImageShowManager.bitmap_width, ImageShowManager.bitmap_height);
            imageManager.putBitmapToMemery(url, bitmap);
            imageManager.putBitmapToDisk(url, bitmap);

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap resultBitmap) {
            if (isCancelled()) {
                resultBitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();

                AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageView);
                if (this == loadImageTask) {
                    imageView.setImageDrawable(null);
                    imageView.setImageBitmap(resultBitmap);
                }
            }

            super.onPostExecute(resultBitmap);
        }
    }

    private AsyncLoadImageTask getAsyncLoadImageTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof LoadingDrawable) {
                LoadingDrawable loadingDrawable = (LoadingDrawable) drawable;
                return loadingDrawable.getLoadImageTask();
            }
        }
        return null;
    }


    public static class LoadingDrawable extends ColorDrawable {
        private final WeakReference<AsyncLoadImageTask> loadImageTaskReference;

        public LoadingDrawable(AsyncLoadImageTask loadImageTask) {
            super(Color.BLUE);
            loadImageTaskReference = new WeakReference<AsyncLoadImageTask>(
                    loadImageTask);
        }

        public AsyncLoadImageTask getLoadImageTask() {
            return loadImageTaskReference.get();
        }
    }
}
