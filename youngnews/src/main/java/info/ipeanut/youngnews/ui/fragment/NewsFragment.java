package info.ipeanut.youngnews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.ipeanut.youngnews.R;
import info.ipeanut.youngnews.YoungNewsApp;
import info.ipeanut.youngnews.api.NewsAllDataBean;
import info.ipeanut.youngnews.ui.base.BaseFragment;

/**
 * Created by chenshaosina on 15/11/4.
 */
public class NewsFragment extends BaseFragment {
    View view;
    NewsAllDataBean.DataItem dataItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_news,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (null != YoungNewsApp.getNewsAllDataBean()
                && null != YoungNewsApp.getNewsAllDataBean().data){
            for(NewsAllDataBean.DataItem d : YoungNewsApp.getNewsAllDataBean().data){
                if (NewsAllDataBean.ID_NEWS == d.id){
                    dataItem = d;
                }
            }
            if (null == dataItem) return;
        }



    }
}
