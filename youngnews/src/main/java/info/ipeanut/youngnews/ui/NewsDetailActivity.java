package info.ipeanut.youngnews.ui;

import android.webkit.WebView;

import butterknife.Bind;
import info.ipeanut.youngnews.R;
import info.ipeanut.youngnews.YoungNewsApp;
import info.ipeanut.youngnews.ui.base.BaseActivity;

/**
 * Created by chenshaosina on 15/11/9.
 */
public class NewsDetailActivity extends BaseActivity {
    private String url ;

    @Bind(R.id.webView)
    WebView webView;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_newsdetail;
    }

    @Override
    protected void initViewsAndEvents() {
        if (getIntent() != null){
            url = getIntent().getStringExtra(YoungNewsApp.KEY_URL);
        }


        webView.loadUrl(url);

    }
}
