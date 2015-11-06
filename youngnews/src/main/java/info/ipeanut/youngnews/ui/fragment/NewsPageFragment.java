package info.ipeanut.youngnews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.ipeanut.youngnews.R;
import info.ipeanut.youngnews.ui.base.BaseFragment;

/**
 * Created by chenshao on 15/11/5.
 */
public class NewsPageFragment extends BaseFragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_newspage,null);
        return view;
    }
}
