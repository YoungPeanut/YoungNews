package info.ipeanut.youngnews;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import info.ipeanut.youngnews.api.NewsAllDataBean;

/**
 * Created by chenshaosina on 15/10/29.
 */
public class YoungNewsApp extends Application {
    public static final String PRE_NEWSALLDATABEAN = "NewsAllDataBean";
    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_URL = "KEY_URL";


    public static final String PATH_IMG = "images" + File.separatorChar  + "imageCache" ;

    private static YoungNewsApp app;
    private static RequestQueue requestQueue;
    private static NewsAllDataBean newsAllDataBean;
    private static DisplayImageOptions opt;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        requestQueue = Volley.newRequestQueue(this);

        initImageLoader(getApplicationContext());

        opt = new  DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
//                .showImageForEmptyUri(getResources().getDrawable(R.drawable.ic_item_news_load))
//                .showImageOnFail(getResources().getDrawable(R.drawable.ic_item_news_load))
                .build();
    }

    private static void initImageLoader(Context context) {

        File cacheDir = StorageUtils.getOwnCacheDirectory(app,PATH_IMG);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                        // .displayer(new FadeInBitmapDisplayer(500))
                .imageScaleType(ImageScaleType.EXACTLY).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                        // default
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // .writeDebugLogs() // Remove for release app
                .defaultDisplayImageOptions(options).build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }


    //out
    public static synchronized Application getApp() {
        if (null == app){
            throw new RuntimeException("fatal exception");
        }
        return app;
    }

    public static RequestQueue getRequestQueue() {
        if (null == requestQueue){
            requestQueue = Volley.newRequestQueue(app);
        }
        return requestQueue;
    }

    public static NewsAllDataBean getNewsAllDataBean() {
        return newsAllDataBean;
    }

    public static void setNewsAllDataBean(NewsAllDataBean newsAllDataBean) {
        YoungNewsApp.newsAllDataBean = newsAllDataBean;
    }

    public static DisplayImageOptions getOpt() {
        if (null == opt){
            opt = new  DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
//                .showImageForEmptyUri(getResources().getDrawable(R.drawable.ic_item_news_load))
//                .showImageOnFail(getResources().getDrawable(R.drawable.ic_item_news_load))
                    .build();
        }
        return opt;
    }

    public static void jumpTo(Activity act,Class cls,Bundle arg,ActivityOptions options){

        Intent intent = new Intent();
        intent.setClass(act, cls);
        intent.putExtras(arg);
//                        ActivityOptions options =
//                                ActivityOptions.makeSceneTransitionAnimation(host,
//                                        Pair.create(view, host.getString(R.string.transition_shot)),
//                                        Pair.create(view, host.getString(R.string
//                                                .transition_shot_background)));
//                        getActivity().startActivity(intent, options.toBundle());
        if (null == options){

            act.startActivity(intent);
        } else {

            act.startActivity(intent,options.toBundle());
        }
    }
}
