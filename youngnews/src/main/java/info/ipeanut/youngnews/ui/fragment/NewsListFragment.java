package info.ipeanut.youngnews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import info.ipeanut.youngnews.R;
import info.ipeanut.youngnews.YoungNewsApp;
import info.ipeanut.youngnews.api.NewsPageBean;
import info.ipeanut.youngnews.ui.adapter.LoadmoreScrollListener;
import info.ipeanut.youngnews.ui.adapter.NewsItemAdapter;
import info.ipeanut.youngnews.ui.base.BaseFragment;
import info.ipeanut.youngnews.utils.PreferenceUtils;

/**
 * bug:
 * 1 fragment被回收的时候，getActivity() 为空
 * 2 页面数据请求与homeActivity的请求集中在一起，容易请求失败
 * 3 homeActivity请求失败的友好提示，重新请求
 * 4 页面的重新请求
 * 5 json数据的缓存
 * 6 android.support.v7.widget.CardView
 * 7 上面top news的解决方案：（因为topnews和news要一块滑动）
 * －recyclerview的header   http://www.tuicool.com/articles/mauMziR
 * http://stackoverflow.com/questions/26530685/is-there-an-addheaderview-equivalent-for-recyclerview/26573338#26573338
 * －recyclerview的item，内嵌viewpager
 * －viewpager与recyclerview并列，放在scrollView中
 * -把recyclerview做成Grid的样式，第一行放两个top news，别的行放一个http://bbs.csdn.net/topics/391073373
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0722/3214.html
 * 8 recyclerview分割线  http://segmentfault.com/q/1010000003942010
 * 9 数据统计
 * Created by chenshao on 15/11/5.
 */
public class NewsListFragment extends BaseFragment {
    private int id;
    private String url;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipe_refresh;
    StringRequest request;
    Gson gson;
    GridLayoutManager layoutManager;

    private  ArrayList<NewsPageBean.NewsItem> newsList;
    private  ArrayList<NewsPageBean.TopNewsItem> topnewsList;
    NewsItemAdapter adapter;
    NewsPageBean newsPageBean;
    public static NewsListFragment getInstance(int id, String url) {

        Bundle arg = new Bundle();
        arg.putInt(YoungNewsApp.KEY_ID, id);
        arg.putString(YoungNewsApp.KEY_URL, url);

        NewsListFragment instance = new NewsListFragment();
        instance.setArguments(arg);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_newspage, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != getArguments()) {
            id = getArguments().getInt(YoungNewsApp.KEY_ID);
            url = getArguments().getString(YoungNewsApp.KEY_URL);
            if (TextUtils.isEmpty(url)) {
                return;
            }
        }
        gson = new Gson();

        preInitData();
        initData(true,url);
    }

    private void preInitData() {
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerview.setLayoutManager(layoutManager);
        newsList = new ArrayList<>();
        topnewsList = new ArrayList<>();
        adapter = new NewsItemAdapter(getActivity(),newsList,topnewsList);
        recyclerview.setAdapter(adapter);
        recyclerview.addOnScrollListener(new LoadmoreScrollListener(layoutManager) {
            @Override
            public void onLoadmore() {
                Toast.makeText(getActivity(), "lllalalalala", Toast.LENGTH_SHORT).show();
                if (newsPageBean != null && !TextUtils.isEmpty(newsPageBean.data.more)) {
                    initData(false, newsPageBean.data.more);
                } else {
                    Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
                }

            }
        });

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(true, url);
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (null != request && !request.isCanceled()) request.cancel();
        super.onDestroyView();
    }

    void handleResponse(String res,boolean isRefresh){

        newsPageBean = gson.fromJson(res, NewsPageBean.class);
        if (null != newsPageBean && null != newsPageBean.data){

            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (null == newsPageBean.data.topnews || position > newsPageBean.data.topnews.size() - 1) {
                        return 2;
                    }
                    return 1;
                }
            });
            if (isRefresh){
                newsList.clear();topnewsList.clear();
            }
            newsList.addAll(newsPageBean.data.news);
            topnewsList.addAll(newsPageBean.data.topnews == null
                    ? new ArrayList<NewsPageBean.TopNewsItem>() : newsPageBean.data.topnews);
            adapter.notifyDataSetChanged();
        }

    }
    private void initData(final boolean isRefresh, final String urll) {

        final String cache = PreferenceUtils.getPrefString(getActivity(),urll,"");
        if (!TextUtils.isEmpty(cache)){
            handleResponse(cache, isRefresh);
        }
        if (null != request && !request.isCanceled()) request.cancel();
        if (isRefresh) {
            swipe_refresh.setRefreshing(true);
        } else {
            adapter.isLoadingMore = true;
        }
        request = new StringRequest(Request.Method.GET, urll,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        swipe_refresh.setRefreshing(false);
                        adapter.isLoadingMore = false;

                        if (isRefresh){
                            recyclerview.smoothScrollToPosition(0);
                        }
                        if (!TextUtils.isEmpty(response) && !response.equals(cache)) {
                            PreferenceUtils.setPrefString(getActivity(), urll, response);
                            handleResponse(response,isRefresh);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipe_refresh.setRefreshing(false);
                        adapter.isLoadingMore = false;

                        Toast.makeText(getActivity(), "请求数据失败-news", Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                response.headers.put(HTTP.CONTENT_TYPE,"text/html; charset=UTF-8");
                return super.parseNetworkResponse(response);
            }
        };

        YoungNewsApp.getRequestQueue().add(request);

    }
}
