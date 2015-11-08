package info.ipeanut.youngnews;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import info.ipeanut.youngnews.api.NewsAllDataBean;

/**
 * Created by chenshaosina on 15/10/29.
 */
public class YoungNewsApp extends Application {
    public static final String PRE_NEWSALLDATABEAN = "NewsAllDataBean";
    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_URL = "KEY_URL";

    private static YoungNewsApp app;
    private static RequestQueue requestQueue;
    private static NewsAllDataBean newsAllDataBean;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        requestQueue = Volley.newRequestQueue(this);

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
}
