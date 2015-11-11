package info.ipeanut.youngnews.api;

import java.util.List;

/**
 * Created by chenshaosina on 15/11/11.
 */
public class TopicTabBean {
    public String retcode;
    public TopicData data;

    public static class TopicData{
        public List<TopicItem> topic;
        public String countcommenturl;
    }

    public static class TopicItem{
        public String title;
        public int id;
        public List<NewsItemBean> news;
    }
}
