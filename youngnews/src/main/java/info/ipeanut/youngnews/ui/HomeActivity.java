package info.ipeanut.youngnews.ui;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import info.ipeanut.youngnews.R;
import info.ipeanut.youngnews.YoungNewsApp;
import info.ipeanut.youngnews.api.ApiConstants;
import info.ipeanut.youngnews.api.NewsAllDataBean;
import info.ipeanut.youngnews.ui.adapter.HomeRvendAdapter;
import info.ipeanut.youngnews.ui.base.BaseActivity;
import info.ipeanut.youngnews.ui.fragment.AlbumFragment;
import info.ipeanut.youngnews.ui.fragment.InteractionFragment;
import info.ipeanut.youngnews.ui.fragment.NewsTabFragment;
import info.ipeanut.youngnews.ui.fragment.TopicFragment;
import info.ipeanut.youngnews.ui.fragment.VoteFragment;
import info.ipeanut.youngnews.utils.PreferenceUtils;

/**
 * Created by Chenshao_Young on 15/9/4.
 */
public class HomeActivity extends BaseActivity implements HomeRvendAdapter.OnSelectedItemChangedListener {

    @Bind(R.id.home_drawer)
    DrawerLayout home_drawer;
    @Bind(R.id.home_rv_end)
    RecyclerView home_rv_end;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(android.R.id.empty)
    ProgressBar empty;

    HomeRvendAdapter homeRvendAdapter;
    NewsTabFragment newsTabFragment;
    TopicFragment topicFragment;
    AlbumFragment albumFragment;
    InteractionFragment interactionFragment;
    VoteFragment voteFragment;
    private List<NewsAllDataBean.DataItem> drawData;
    Gson gson;
    StringRequest request;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViewsAndEvents() {

        initViews();
        initData(true);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        //the visibility of the status bar or other screen/window decorations be changed.
        home_drawer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        drawData = new ArrayList<>();
        homeRvendAdapter = new HomeRvendAdapter(HomeActivity.this, drawData);
        homeRvendAdapter.setOnSelectedItemChangedListener(HomeActivity.this);
        home_rv_end.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        home_rv_end.setAdapter(homeRvendAdapter);
        gson = new Gson();

    }

    @Override
    protected void onDestroy() {

        if (request != null && !request.isCanceled())
            request.cancel();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_filter:
                home_drawer.openDrawer(home_rv_end);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void handleResponse(String response) {

        empty.setVisibility(View.GONE);
        NewsAllDataBean newsAllDataBean = gson.fromJson(response, NewsAllDataBean.class);
        drawData.clear();
        drawData.addAll(newsAllDataBean.data);
        homeRvendAdapter.notifyDataSetChanged();

        YoungNewsApp.setNewsAllDataBean(newsAllDataBean);
    }

    private void initData(final boolean needRetry) {
        final String cache = PreferenceUtils.getPrefString(HomeActivity.this, YoungNewsApp.PRE_NEWSALLDATABEAN, "");
        if (!TextUtils.isEmpty(cache)) {
            handleResponse(cache);
            showFragment(newsTabFragment, NewsTabFragment.class);
        }

        if (request != null && !request.isCanceled()) request.cancel();
        request = new StringRequest(Request.Method.GET, ApiConstants.Urls.URL_NEWS_ALL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!TextUtils.isEmpty(response)) {
                            if (!response.equals(cache)) {

                                handleResponse(response);
                                PreferenceUtils.setPrefString(HomeActivity.this, YoungNewsApp.PRE_NEWSALLDATABEAN, response);
                            }
                        } else {
                            if (needRetry) {
                                initData(false);
                            }
                            //first use && request failed
                            if (TextUtils.isEmpty(cache)) {
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, "请求数据失败-home", Toast.LENGTH_SHORT).show();
                        if (needRetry) {
                            initData(false);
                        }
                    }
                }
        );
        YoungNewsApp.getRequestQueue().add(request);
    }

    @Override
    public void onSelectedItemChanged(NewsAllDataBean.DataItem dataItem, int position) {
        handleFragment(dataItem.id);
        home_drawer.closeDrawer(home_rv_end);
    }

    public void handleFragment(int id) {
        if (null != getSupportFragmentManager().getFragments()
                && getSupportFragmentManager().getFragments().size() > 0) {
            for (Fragment f : getSupportFragmentManager().getFragments()) {
                getSupportFragmentManager().beginTransaction()
                        .hide(f).commitAllowingStateLoss();
            }
        }
        switch (id) {
            case NewsAllDataBean.ID_NEWS:
                showFragment(newsTabFragment, NewsTabFragment.class);

                break;
            case NewsAllDataBean.ID_TOPIC:
                showFragment(topicFragment, TopicFragment.class);

                break;
            case NewsAllDataBean.ID_ALBUM:
                showFragment(albumFragment, AlbumFragment.class);

                break;
            case NewsAllDataBean.ID_INTERACTION:
                showFragment(interactionFragment, InteractionFragment.class);

                break;
            case NewsAllDataBean.ID_VOTE:
                showFragment(voteFragment, VoteFragment.class);

                break;
        }

    }

    /**
     * 我都把f给你了，你竟然不知道f的classname，我还得传个类型
     *
     * @param f
     */
    public void showFragment(Fragment f, Class cls) {

        if (null == f) {
            f = Fragment.instantiate(this, cls.getName());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, f, cls.getSimpleName())
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .show(f)
                    .commitAllowingStateLoss();
        }
    }
}
