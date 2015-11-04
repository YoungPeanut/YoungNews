package info.ipeanut.youngnews.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import butterknife.Bind;
import info.ipeanut.youngnews.R;
import info.ipeanut.youngnews.YoungNewsApp;
import info.ipeanut.youngnews.api.ApiConstants;
import info.ipeanut.youngnews.api.NewsAllDataBean;
import info.ipeanut.youngnews.ui.base.BaseActivity;
import info.ipeanut.youngnews.ui.fragment.AlbumFragment;
import info.ipeanut.youngnews.ui.fragment.InteractionFragment;
import info.ipeanut.youngnews.ui.fragment.NewsFragment;
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

    HomeRvendAdapter homeRvendAdapter;
    NewsFragment newsFragment;
    TopicFragment topicFragment;
    AlbumFragment albumFragment;
    InteractionFragment interactionFragment;
    VoteFragment voteFragment;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViewsAndEvents() {

        initViews();
        initData();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        //the visibility of the status bar or other screen/window decorations be changed.
        home_drawer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        // drawer layout treats fitsSystemWindows specially so we have to handle insets ourselves
        // 或者在xml中 android:clipToPadding="false"    android:fitsSystemWindows="true"
//        home_drawer.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
//            @Override
//            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
//                // inset the toolbar down by the status bar height
//                ViewGroup.MarginLayoutParams lpToolbar = (ViewGroup.MarginLayoutParams) toolbar
//                        .getLayoutParams();
//                lpToolbar.topMargin += insets.getSystemWindowInsetTop();
//                lpToolbar.rightMargin += insets.getSystemWindowInsetRight();
//                toolbar.setLayoutParams(lpToolbar);
//
//                // clear this listener so insets aren't re-applied
//                home_drawer.setOnApplyWindowInsetsListener(null);
//
//                return insets.consumeSystemWindowInsets();
//            }
//        });

        //null
//        home_rv_end.setAdapter(homeRvendAdapter);
        home_rv_end.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_filter:
                home_drawer.openDrawer(home_rv_end);
//                home_drawer.openDrawer(GravityCompat.END);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {

        StringRequest request = new StringRequest(Request.Method.GET, ApiConstants.Urls.URL_NEWS_ALL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        NewsAllDataBean newsAllDataBean = gson.fromJson(response, NewsAllDataBean.class);
//                        Toast.makeText(HomeActivity.this, "success"+newsAllDataBean.data.size(), Toast.LENGTH_SHORT).show();


                        //RV怎么刷新
                        homeRvendAdapter = new HomeRvendAdapter(HomeActivity.this,newsAllDataBean.data);
                        homeRvendAdapter.setOnSelectedItemChangedListener(HomeActivity.this);
                        home_rv_end.setAdapter(homeRvendAdapter);

                        YoungNewsApp.setNewsAllDataBean(newsAllDataBean);
                        PreferenceUtils.setPrefString(HomeActivity.this, YoungNewsApp.PRE_NEWSALLDATABEAN, response);

                        showFragment(newsFragment, NewsFragment.class);



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, "failed", Toast.LENGTH_SHORT).show();

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

    public void handleFragment(int id){
        if (null != getSupportFragmentManager().getFragments()
                && getSupportFragmentManager().getFragments().size() > 0){
            for(Fragment f:getSupportFragmentManager().getFragments()){
                getSupportFragmentManager().beginTransaction()
                        .hide(f).commitAllowingStateLoss();
            }
        }
        switch (id){
            case NewsAllDataBean.ID_NEWS:
                showFragment(newsFragment,NewsFragment.class);

                break;
            case NewsAllDataBean.ID_TOPIC:
                showFragment(topicFragment,TopicFragment.class);

                break;
            case NewsAllDataBean.ID_ALBUM:
                showFragment(albumFragment,AlbumFragment.class);

                break;
            case NewsAllDataBean.ID_INTERACTION:
                showFragment(interactionFragment,InteractionFragment.class);

                break;
            case NewsAllDataBean.ID_VOTE:
                showFragment(voteFragment,VoteFragment.class);

                break;
        }

    }

    /**
     * 我都把f给你了，你竟然不知道f的classname，我还得传个类型
     * @param f
     */
    public void showFragment(Fragment f,Class cls){

        if (null == f){
            f = Fragment.instantiate(this,cls.getName());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,f,cls.getSimpleName())
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .show(f)
                    .commitAllowingStateLoss();
        }
    }
}
