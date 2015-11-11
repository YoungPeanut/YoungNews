package info.ipeanut.youngnews.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.ipeanut.youngnews.R;
import info.ipeanut.youngnews.api.NewsAllDataBean;

/**
 * Created by chenshao on 15/11/3.
 */
public class HomeRvendAdapter extends RecyclerView.Adapter<HomeRvendAdapter.HomeRvendHolder> {

    private final Context context;
    private final List<NewsAllDataBean.DataItem> data;
    private OnSelectedItemChangedListener onSelectedItemChangedListener;

    public void setOnSelectedItemChangedListener(OnSelectedItemChangedListener onSelectedItemChangedListener) {
        this.onSelectedItemChangedListener = onSelectedItemChangedListener;
    }

    public HomeRvendAdapter(Context context, List<NewsAllDataBean.DataItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public HomeRvendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeRvendHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_homervend,null));
    }

    @Override
    public void onBindViewHolder(HomeRvendHolder holder, final int position) {
        final NewsAllDataBean.DataItem dataItem = data.get(position);
        holder.title.setText(dataItem.title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onSelectedItemChangedListener){
                    onSelectedItemChangedListener.onSelectedItemChanged(dataItem,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class HomeRvendHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView icon;

        public HomeRvendHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv);
            icon = (ImageView) itemView.findViewById(R.id.iv);
        }
    }
    
    
    public interface OnSelectedItemChangedListener{
        void onSelectedItemChanged(NewsAllDataBean.DataItem dataItem,int position);
    }
}
