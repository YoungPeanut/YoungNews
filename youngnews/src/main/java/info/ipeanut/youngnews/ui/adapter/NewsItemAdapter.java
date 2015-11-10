package info.ipeanut.youngnews.ui.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import info.ipeanut.youngnews.R;
import info.ipeanut.youngnews.YoungNewsApp;
import info.ipeanut.youngnews.api.NewsPageBean;
import info.ipeanut.youngnews.ui.NewsDetailActivity;

/**
 * Created by chenshaosina on 15/11/10.
 */

public class NewsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<NewsPageBean.NewsItem> news;
    private final ArrayList<NewsPageBean.TopNewsItem> topnews;
    private final Activity context;
    public boolean isLoadingMore = false;

    public NewsItemAdapter(@Nullable Activity context,
            @Nullable ArrayList<NewsPageBean.NewsItem> news
            , @Nullable ArrayList<NewsPageBean.TopNewsItem> topnews) {
        this.context = context;
        this.news = news;
        this.topnews = topnews;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && position < getListCount()){

            if (position > topnews.size()-1) {
                return 2;
            } else {
                return 1;
            }
        }
        return 3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (1 == viewType){
            return new VHtop(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_newsitem_top, null));

        } else if (2 == viewType){
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_newsitem, null));
        }
            return new VHloadmore(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loadmore, null));
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

            ((VH)holder).title.setText(news.get(position - topnews.size()).title);
            ((VH)holder).time.setText(news.get(position - topnews.size()).pubdate);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toNewsDetail(news.get(position).url);
                }
            });
        } else if (3 == getItemViewType(position)){
            ((VHloadmore)holder).pro.setVisibility(isLoadingMore ? View.VISIBLE : View.INVISIBLE);
        }
    }
    void toNewsDetail(String url){

        Bundle arg = new Bundle();
        arg.putString(YoungNewsApp.KEY_URL,url);
        YoungNewsApp.jumpTo(context,NewsDetailActivity.class,arg,null);
    }

    @Override
    public int getItemCount() {
        return getListCount() +1 ;
    }
    public int getListCount() {
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
    public class VHloadmore extends RecyclerView.ViewHolder {

        ProgressBar pro;

        public VHloadmore(View itemView) {
            super(itemView);
            pro = (ProgressBar) itemView.findViewById(R.id.pro);
        }
    }
}
