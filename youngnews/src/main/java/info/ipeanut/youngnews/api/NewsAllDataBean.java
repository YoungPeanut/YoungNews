package info.ipeanut.youngnews.api;

import java.util.ArrayList;

/**
 * Created by chenshaosina on 15/10/29.
 * http://zhbj.qianlong.com/static/api/news/categories.json
 */
public class NewsAllDataBean {

    public static final int ID_NEWS = 10000;
    public static final int ID_TOPIC = 10002;
    public static final int ID_ALBUM = 10003;
    public static final int ID_INTERACTION = 10004;
    public static final int ID_VOTE = 10005;

    public int retcode;
    public ArrayList<DataItem> data;
    public int[] extend;


    public static class DataItem {
        public int id;
        public String title = "";
        public int type;
        public String url = "";
        public String url1 = "";
        public String excurl;
        public String weekurl;
        public String dayurl;
        public ArrayList<Child> children = new ArrayList<Child>();
    }

    /**
     * "id":10014,
     * "title":"文娱",
     * "type":1,
     * "url":"http://zhbj.qianlong.com/static/api/news/10014/list_1.json"
     */
    public static class Child {
        public int id;
        public String title = "";
        public int type;
        public String url = "";


    }

}
