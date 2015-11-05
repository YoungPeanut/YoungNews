package info.ipeanut.youngnews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.ipeanut.youngnews.R;
import info.ipeanut.youngnews.YoungNewsApp;
import info.ipeanut.youngnews.api.NewsAllDataBean;
import info.ipeanut.youngnews.ui.base.BaseFragment;
import info.ipeanut.youngnews.ui.widget.SlidingTabLayout;

/**
 * Created by chenshaosina on 15/11/4.
 */
public class NewsFragment extends BaseFragment {
    View view;
    NewsAllDataBean.DataItem dataItem;

    @Bind(R.id.sliding_tab)
    SlidingTabLayout sliding_tab;
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_news,null);
        ButterKnife.bind(this,view);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (null != YoungNewsApp.getNewsAllDataBean()
                && null != YoungNewsApp.getNewsAllDataBean().data){
            for(NewsAllDataBean.DataItem d : YoungNewsApp.getNewsAllDataBean().data){
                if (NewsAllDataBean.ID_NEWS == d.id){
                    dataItem = d;
                }
            }
            if (null == dataItem) return;
            if (null == dataItem.children) return;
        }

        viewPager.setAdapter(new NewsVPAdapter(getChildFragmentManager()));
        sliding_tab.setViewPager(viewPager);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public class NewsVPAdapter extends FragmentStatePagerAdapter{
        public NewsVPAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            NewsPageFragment pageFragment = new NewsPageFragment();


            return pageFragment;
        }

        @Override
        public int getCount() {
            return dataItem.children.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return dataItem.children.get(position).title;
        }
    }

}
