package info.ipeanut.youngnews.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import info.ipeanut.youngnews.ui.adapter.VpTopnewsAdapter;
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
 *      －recyclerview的header   http://www.tuicool.com/articles/mauMziR
 *      http://stackoverflow.com/questions/26530685/is-there-an-addheaderview-equivalent-for-recyclerview/26573338#26573338
 *      －recyclerview的item，内嵌viewpager
 *      －viewpager与recyclerview并列，放在scrollView中
 *      -把recyclerview做成Grid的样式，第一行放两个top news，别的行放一个http://bbs.csdn.net/topics/391073373
 *      http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0722/3214.html
 * 8 recyclerview分割线  http://segmentfault.com/q/1010000003942010
 * Created by chenshao on 15/11/5.
 */
public class NewsPageFragment extends BaseFragment {
    private int id;
    private String url;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipe_refresh;

    public static NewsPageFragment getInstance(int id,String url){

        Bundle arg = new Bundle();
        arg.putInt(YoungNewsApp.KEY_ID,id);
        arg.putString(YoungNewsApp.KEY_URL,url);

        NewsPageFragment instance = new NewsPageFragment();
        instance.setArguments(arg);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_newspage,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != getArguments()){
            id = getArguments().getInt(YoungNewsApp.KEY_ID);
            url = getArguments().getString(YoungNewsApp.KEY_URL);
            if (TextUtils.isEmpty(url)){
                return;
            }
//            Snackbar.make(view,id+"--",Snackbar.LENGTH_SHORT).show();
        }
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        //请求url
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "succc", Toast.LENGTH_SHORT).show();
                        if (!TextUtils.isEmpty(response)){
                            Gson gson = new Gson();
                            NewsPageBean newsPageBean = gson.fromJson(response, NewsPageBean.class);

                            //应该提前把vp处理好，然后把vp当成view传进adapter，根据vp是否空来判断itemType
//                            VpTopnewsAdapter adapter = new VpTopnewsAdapter(ct,topnews);
//                            holder.vp.setAdapter(adapter);

                            //判空
                            recyclerview.setAdapter(new NewsItemAdapter(getActivity(),
                                    newsPageBean.data.news,newsPageBean.data.topnews));

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "errr", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        YoungNewsApp.getRequestQueue().add(request);

    }

    public  class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.VH>{
        private final ArrayList<NewsPageBean.NewsItem> news;
        private final ArrayList<NewsPageBean.TopNewsItem> topnews;
        private final Context ct;

        public NewsItemAdapter(Context ct,ArrayList<NewsPageBean.NewsItem> news, ArrayList<NewsPageBean.TopNewsItem> topnews) {
            this.ct = ct;
            this.news = news;
            this.topnews = topnews;
        }

        @Override
        public int getItemViewType(int position) {

            if (topnews == null || topnews.size() == 0){
                //news
                return 1;
            } else {
                if (0 == position){
                    return 0;//topnews
                } else {
                    return 1;
                }
            }
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            if (0 == viewType){
                return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_newsitem_top,null)
                    ,viewType);

            } else {
                return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_newsitem,null)
                    ,viewType);
            }
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            if (0 == getItemViewType(position)){

                VpTopnewsAdapter adapter = new VpTopnewsAdapter(ct,topnews);
                holder.vp.setAdapter(adapter);
            } else {
                holder.title.setText(news.get(position).title);
                holder.time.setText(news.get(position).pubdate);
            }


        }

        @Override
        public int getItemCount() {
            return (topnews == null || topnews.size() == 0)? news.size() : news.size() + 1;
        }

        public class VH extends RecyclerView.ViewHolder{

            int type;
            TextView title;
            TextView time;
            ViewPager vp;
            public VH(View itemView,int type) {
                super(itemView);
                this.type = type;
                if (this.type == 0){
                    initViewTop();
                } else {
                    initView();
                }
            }

            void initView(){

                title = (TextView) itemView.findViewById(R.id.title);
                time = (TextView) itemView.findViewById(R.id.time);
            }
            void initViewTop(){

                vp = (ViewPager) itemView.findViewById(R.id.vp);
            }


        }
    }
}
