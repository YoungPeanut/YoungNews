package info.ipeanut.youngnews.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.ipeanut.youngnews.R;
import info.ipeanut.youngnews.api.NewsItemBean;
import info.ipeanut.youngnews.api.TopicTabBean;

/**
 * Created by chenshaosina on 15/11/11.
 */
public class TopicTabAdapter extends RecyclerView.Adapter<TopicTabAdapter.VH> {
    private Context context;
    private List<TopicTabBean.TopicItem> topic;

    public TopicTabAdapter(Context context, List<TopicTabBean.TopicItem> topic1) {
        this.context = context;
        this.topic = topic1;
    }

    @Override
    public TopicTabAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_topictab,parent,false));
    }

    @Override
    public void onBindViewHolder(TopicTabAdapter.VH holder, int position) {
        final TopicTabBean.TopicItem item = topic.get(position);
        holder.textView.setText(item.title);

        handleRV(holder.recyclerview, item.news);
    }

    private void handleRV(RecyclerView recyclerview,List<NewsItemBean> news) {
        recyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        recyclerview.setAdapter(new TopicNewsAdapter(context,news));
    }

    @Override
    public int getItemCount() {
        return topic.size();
    }

    public static class VH extends RecyclerView.ViewHolder{

        TextView textView;
        RecyclerView recyclerview;
        public VH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            recyclerview = (RecyclerView) itemView.findViewById(R.id.recyclerview);
        }
    }

}
