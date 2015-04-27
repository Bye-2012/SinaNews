package com.moon.common.utils;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/21
 */
public class UrlUtils {
    public static final String BASE = "http://1000phone.net:8088/qfapp/index.php/juba/";
    public static final String NEWS_END = "&p=";

    //个人资料
    public static final String LOGIN = BASE + "index/do_login";
    public static final String REGISTER = BASE + "index/do_register";
    public static final String VERIFY_CODE = BASE + "index/verify_code?sequence=";
    public static final String UPLOAD_HEADERIMG = BASE + "user/upload_header";
    public static final String MY_COLLECT = BASE + "news/get_stow_news?token=";
    public static final String MY_COMMENT = BASE + "news/get_my_comment?token=";
    public static final String LOGOUT = BASE + "user/logout";
    public static final String EXIT = BASE + "user/logout?token=";

    //文字新闻
    public static final String CATE_LIST = BASE + "news/cate_list?type=";
    public static final String NEWS_LIST = BASE + "news/get_news_list?cate_id=";
    public static final String NEWS_DETAILS = BASE + "news/news_detail?news_id=";
    public static final String NEWS_COMMENT = BASE + "news/get_comment_list?item_id=";
    public static final String SEND_COMMENT = BASE + "news/add_comment";
    public static final String SEND_TXT_NEWS = BASE + "news/add_news";

    //图片新闻
    public static final String PIC_NEWS_LIST = BASE + "news/get_pic_news_list?cate_id=";
    public static final String PIC_NEWS_DETAIL = BASE + "news/news_detail?news_id=";

    //视频新闻
    public static final String VDO_NEWS_LIST = BASE + "news/get_video_news_list?cate_id=";

}