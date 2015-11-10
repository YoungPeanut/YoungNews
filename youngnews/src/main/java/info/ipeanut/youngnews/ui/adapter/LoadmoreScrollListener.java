package info.ipeanut.youngnews.ui.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by chenshaosina on 15/11/10.
 */
public abstract class LoadmoreScrollListener extends RecyclerView.OnScrollListener {
    private final GridLayoutManager layoutManager;
    private final int TOLOAD = 1;

    public LoadmoreScrollListener(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        final int cur = recyclerView.getChildCount();
        final int total = layoutManager.getItemCount();
        final int first = layoutManager.findFirstVisibleItemPosition();
        if (first+cur+TOLOAD >= total){
            onLoadmore();
        }
    }

    public abstract void onLoadmore();
}
