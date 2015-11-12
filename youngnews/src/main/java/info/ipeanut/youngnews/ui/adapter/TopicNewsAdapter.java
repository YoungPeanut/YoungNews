package info.ipeanut.youngnews.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.ipeanut.youngnews.R;
import info.ipeanut.youngnews.api.NewsItemBean;

/**
 * Created by chenshaosina on 15/11/11.
 */
public class TopicNewsAdapter extends RecyclerView.Adapter<TopicNewsAdapter.VH> {
    private Context context;
    private final List<NewsItemBean> news;

    public TopicNewsAdapter(Context context, @Nullable List<NewsItemBean> news) {
        this.context = context;
        this.news = news;
    }

    @Override
    public TopicNewsAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_topicnews,parent,false));
    }

    @Override
    public void onBindViewHolder(TopicNewsAdapter.VH holder, int position) {
        final NewsItemBean item = news.get(position);
        holder.tv_title.setText(item.title);
        holder.tv_date.setText(item.pubdate);

    }

    @Override
    public int getItemCount() {
        return news.size();
    }
    
    public static class VH extends RecyclerView.ViewHolder{

        TextView tv_title,tv_date;
        ImageView iv;
        public VH(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            
        }
    }
}
