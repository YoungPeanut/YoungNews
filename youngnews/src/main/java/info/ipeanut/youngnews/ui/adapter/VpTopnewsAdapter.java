package info.ipeanut.youngnews.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import info.ipeanut.youngnews.R;
import info.ipeanut.youngnews.api.NewsPageBean;

/**
 * Created by chenshao on 15/11/7.
 */
public class VpTopnewsAdapter extends PagerAdapter {
    Context context;
    private  ArrayList<NewsPageBean.TopNewsItem> topnews;
    View view;

    public VpTopnewsAdapter(Context context, ArrayList<NewsPageBean.TopNewsItem> topnews) {
        this.context = context;
        this.topnews = topnews;
        view =  LayoutInflater.from(context).inflate(R.layout.adapter_vp_topnews,null);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(view);
    }

    @Override
    public int getCount() {
        return topnews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
