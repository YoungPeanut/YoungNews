package info.ipeanut.youngnews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import info.ipeanut.youngnews.api.TopicTabBean;
import info.ipeanut.youngnews.ui.adapter.TopicTabAdapter;
import info.ipeanut.youngnews.ui.base.BaseFragment;

/**
 * Created by chenshaosina on 15/11/4.
 */
public class TopicTabFragment extends BaseFragment {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;

    String url;
    TopicTabBean topicTabBean;
    StringRequest request;
    Gson gson;
    TopicTabAdapter adapter;
    private List<TopicTabBean.TopicItem> topic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_topictab,container,false);
        return view;
    }

    @Override
    public void onDestroyView() {
        if (null != request && !request.isCanceled()) request.cancel();
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != getArguments()){
            url = getArguments().getString(YoungNewsApp.KEY_URL,"");
            if (TextUtils.isEmpty(url)) return;
        }

        preReq();
        requestData();

    }

    private void preReq() {
        gson = new Gson();


        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        topic = new ArrayList<>();
        adapter = new TopicTabAdapter(getActivity(),topic);
        recyclerview.setAdapter(adapter);
    }

    private void requestData() {
        if (null != request && !request.isCanceled()) request.cancel();
        request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        YoungNewsApp.showToast("topic--err");

                    }
                }
        );
        YoungNewsApp.getRequestQueue().add(request);

    }

    void handleResponse( String response){
        topicTabBean = gson.fromJson(response,TopicTabBean.class);
        topic.clear();
        if (topicTabBean != null && topicTabBean.data != null){
            topic.addAll(topicTabBean.data.topic);
        }
        //ttest
        topic.addAll(topic);
        topic.addAll(topic);
        topic.addAll(topic);
        topic.addAll(topic);
        adapter.notifyDataSetChanged();
    }
}
