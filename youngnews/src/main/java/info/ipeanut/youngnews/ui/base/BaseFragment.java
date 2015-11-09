package info.ipeanut.youngnews.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by chenshaosina on 15/11/4.
 */
public class BaseFragment extends Fragment{
    protected View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view1, Bundle savedInstanceState) {
        super.onViewCreated(view1, savedInstanceState);
        ButterKnife.bind(this, view);


    }
}
