package info.ipeanut.youngnews.api;

import java.util.ArrayList;

/**
 * Created by chenshao on 15/11/7.
 * http://zhbj.qianlong.com/static/api/news/10007/list_1.json
 */
public class NewsPageBean {
    public int retcode;
    public Data data;

    public static class Data{
        public String title;
        public String countcommenturl;
        public String more;
        public ArrayList<Object> topic;
        public ArrayList<NewsItem> news;
        public ArrayList<TopNewsItem> topnews;

    }

    public static class NewsItem{
        public int id;
        public String title;
        public String url;
        public String commenturl;
        public String pubdate;
        public String type;
        public String commentlist;
        public boolean comment;
    }
    public static class TopNewsItem extends NewsItem{
        public String topimage;
    }
}
