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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.Bind;
import info.ipeanut.youngnews.R;
import info.ipeanut.youngnews.YoungNewsApp;
import info.ipeanut.youngnews.api.NewsPageBean;
import info.ipeanut.youngnews.ui.NewsDetailActivity;
import info.ipeanut.youngnews.ui.base.BaseFragment;

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
 * Created by chenshao on 15/11/5.
 */
public class NewsListFragment extends BaseFragment {
    private int id;
    private String url;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipe_refresh;

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
//            Snackbar.make(view,id+"--",Snackbar.LENGTH_SHORT).show();
        }
//        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        //请求url
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "succc", Toast.LENGTH_SHORT).show();
                        if (!TextUtils.isEmpty(response)) {
                            Gson gson = new Gson();
                            final NewsPageBean newsPageBean = gson.fromJson(response, NewsPageBean.class);

                            recyclerview.setAdapter(new NewsItemAdapter( newsPageBean.data.news, newsPageBean.data.topnews));
                            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                @Override
                                public int getSpanSize(int position) {
                                    if (position > newsPageBean.data.topnews.size()-1) {
                                        return 2;
                                    }
                                    return 1;
                                }
                            });
                            recyclerview.setLayoutManager(layoutManager);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "errr", Toast.LENGTH_SHORT).show();

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

    public class NewsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final ArrayList<NewsPageBean.NewsItem> news;
        private final ArrayList<NewsPageBean.TopNewsItem> topnews;

        public NewsItemAdapter(@Nullable ArrayList<NewsPageBean.NewsItem> news
                , @Nullable ArrayList<NewsPageBean.TopNewsItem> topnews) {
            this.news = news;
            this.topnews = topnews;
        }

        @Override
        public int getItemViewType(int position) {
            if (position > topnews.size()-1) {
                return 2;
            }
            return 1;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (1 == viewType){
                return new VHtop(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_newsitem_top, null));

            }
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_newsitem, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (1 == getItemViewType(position)){
                ((VHtop)holder).tv_top.setText(topnews.get(position).title);
                ImageLoader.getInstance().displayImage(topnews.get(position).topimage
                        , ((VHtop) holder).iv_top, YoungNewsApp.getOpt());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toNewsDetail(topnews.get(position).url);
                    }
                });

            } else if (2 == getItemViewType(position)){

                ((VH)holder).title.setText(news.get(position).title);
                ((VH)holder).time.setText(news.get(position).pubdate);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toNewsDetail(news.get(position).url);
                    }
                });

            }
        }
        void toNewsDetail(String url){

            Bundle arg = new Bundle();
            arg.putString(YoungNewsApp.KEY_URL,url);
            YoungNewsApp.jumpTo(getActivity(),NewsDetailActivity.class,arg,null);
        }

        @Override
        public int getItemCount() {
            return topnews.size() + news.size() ;
        }

        public class VH extends RecyclerView.ViewHolder {

            TextView title;
            TextView time;

            public VH(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                time = (TextView) itemView.findViewById(R.id.time);
            }
        }
        public class VHtop extends RecyclerView.ViewHolder {

            ImageView iv_top;
            TextView tv_top;

            public VHtop(View itemView) {
                super(itemView);
                iv_top = (ImageView) itemView.findViewById(R.id.iv_top);
                tv_top = (TextView) itemView.findViewById(R.id.tv_top);
            }
        }
    }
}
