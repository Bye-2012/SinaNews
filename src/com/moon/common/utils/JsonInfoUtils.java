package com.moon.common.utils;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/21
 */
public class JsonInfoUtils {

    /**
     * 获取导航分类
     *
     * @param json
     * @return
     */
    public static List<Map<String, String>> getCateList(String json) {

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (json != null) {
            try {
                JSONObject object1 = new JSONObject(json);
                JSONArray jsonArray = object1.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject temp = jsonArray.getJSONObject(i);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("cate_id", temp.getString("cate_id"));
                    map.put("name", temp.getString("name"));
                    list.add(map);
                    map = null;
                    temp = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取新闻列表
     *
     * @param json
     * @return
     */
    public static List<Map<String, Object>> getNewsList(String json, int id, int num) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (json != null) {
            try {
                JSONObject object1 = new JSONObject(json);
                JSONObject object2 = object1.getJSONObject("data");

                if (num == 1) {
                    JSONArray jsonArray_pic = object2.optJSONArray("top_news");
                    Map<String, Object> map_pic = new HashMap<String, Object>();
                    List<Map<String, String>> list_pic = new ArrayList<Map<String, String>>();
                    if (jsonArray_pic != null) {
                        for (int i = 0; i < jsonArray_pic.length(); i++) {
                            Map<String, String> map = new HashMap<String, String>();
                            JSONObject temp = jsonArray_pic.getJSONObject(i);
                            map.put("p_title", temp.getString("title"));
                            map.put("p_cover_pic", temp.getString("cover_pic"));
                            map.put("p_id",temp.getString("id"));
                            list_pic.add(map);
                            map = null;
                            temp = null;
                        }
                    }
                    map_pic.put("map_pic", list_pic);
                    list.add(map_pic);
                }

                JSONArray jsonArray = object2.getJSONArray("down_news");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    JSONObject temp = jsonArray.getJSONObject(i);
                    map.put("comment_total", temp.getString("comment_total"));
                    map.put("content", temp.getString("content"));
                    map.put("cover_pic", temp.getString("cover_pic"));
                    map.put("create_time", temp.getString("create_time"));
                    map.put("descript", temp.getString("descript"));
                    map.put("id", temp.getString("id"));
                    map.put("title", temp.getString("title"));
                    list.add(map);
                    map = null;
                    temp = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 除了pic_list参数是list类型，其余的都是String类型，
     *
     * @param json
     * @return
     */
    public static Map<String, Object> getNewsDetail(String json) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (json != null) {
            try {
                JSONObject object1 = new JSONObject(json);
                JSONObject object2 = object1.getJSONObject("data");

                map.put("content", object2.getString("content"));
                map.put("cover_pic", object2.getString("cover_pic"));
                map.put("create_time", object2.getString("create_time"));
                map.put("news_id", object2.getString("news_id"));
                map.put("title", object2.getString("title"));

                JSONArray jsonArray = object2.getJSONArray("pic_list");

                map.put("pic_list", picList2List(jsonArray));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private static List<Map<Integer, String>> picList2List(JSONArray jsonArray) throws JSONException {
        List<Map<Integer, String>> maps = new ArrayList<Map<Integer, String>>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Map<Integer, String> temp = new HashMap<Integer, String>();
            temp.put(i, jsonArray.getString(i));
            maps.add(temp);
            temp = null;
        }
        return maps;
    }

    /**
     * 获取图片新闻列表
     *
     * @param json
     * @return
     */
    public static List<Map<String, Object>> getPicList(String json) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (json != null) {
            try {
                JSONObject object1 = new JSONObject(json);
                JSONArray jsonArray = object1.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject temp = jsonArray.getJSONObject(i);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", temp.getString("id"));
                    map.put("title", temp.getString("title"));
                    map.put("content", temp.getString("content"));
                    map.put("descript", temp.getString("descript"));
                    map.put("pic_total", temp.getString("pic_total"));
                    JSONArray arrTemp = temp.optJSONArray("pic_list");
                    if (arrTemp != null) {
                        map.put("pic_list", picList2List(arrTemp));
                    }
                    list.add(map);
                    map = null;
                    temp = null;
                    arrTemp = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取图片新闻详情
     *
     * @param json
     * @return
     */
    public static Map<String, Object> getPicDetail(String json) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (json != null) {
            try {
                JSONObject object1 = new JSONObject(json);
                JSONObject object2 = object1.optJSONObject("data");

                if (object2 != null) {
                    for (int i = 0; i < object2.length(); i++) {
                        map.put("cover_pic",object2.getString("cover_pic"));
                        map.put("news_id",object2.getString("news_id"));
                        map.put("content", object2.getString("content"));
                        map.put("title", object2.getString("title"));
                        JSONArray jsonArray = object2.optJSONArray("pic_list");
                        if (jsonArray != null) {
                            map.put("pic_list", picList2List(jsonArray));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 获取视频新闻列表
     *
     * @param json
     * @return
     */
    public static List<Map<String, String>> getVdoList(String json) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (json != null) {
            try {
                JSONObject object1 = new JSONObject(json);
                JSONArray jsonArray = object1.optJSONArray("data");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject temp = jsonArray.getJSONObject(i);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("pic", temp.getString("pic"));
                        map.put("play_num", temp.getString("play_num"));
                        map.put("title", temp.getString("title"));
                        map.put("video_url", temp.optString("video_url"));
                        list.add(map);
                        map = null;
                        temp = null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获得评论列表
     *
     * @param json
     * @return
     */
    public static List<Map<String, String>> getCommentList(String json) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                Map<String, String> map = new HashMap<String, String>();
                map.put("content", jsonObject.getString("content"));
                map.put("create_time", jsonObject.getString("create_time"));
                map.put("user", jsonObject.getString("user"));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获得登录信息
     * @param json
     * @return
     */
    public static Map<String, String> getLoginInfo(String json) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            map.put("code", jsonObject.getString("code"));
            map.put("message", jsonObject.getString("message"));
            JSONObject data = jsonObject.optJSONObject("data");
            if (data != null) {
                map.put("id", data.getString("id"));
                map.put("username", data.getString("username"));
                map.put("nickname", data.getString("nickname"));
                map.put("token", data.getString("token"));
                map.put("last_login_time", data.getString("last_login_time"));
                map.put("header_img", data.optString("last_login_time"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获得注册信息
     * @param json
     * @return
     */
    public static Map<String, String> getRegInfo(String json) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            map.put("code", jsonObject.getString("code"));
            map.put("message", jsonObject.getString("message"));
            JSONObject data = jsonObject.optJSONObject("data");
            if (data != null) {
                map.put("id", data.getString("id"));
                map.put("username", data.getString("username"));
                map.put("password", data.getString("password"));
                map.put("nickname", data.getString("nickname"));
                map.put("token", data.getString("token"));
                map.put("sex", data.getString("sex"));
                map.put("create_time", data.getString("create_time"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获得发表评论信息
     * @param json
     * @return
     */
    public static Map<String, String> getCmtAddInfo(String json) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            map.put("code", jsonObject.getString("code"));
            map.put("message", jsonObject.getString("message"));
            JSONObject data = jsonObject.optJSONObject("data");
            if (data != null) {
                map.put("content", data.getString("content"));
                map.put("create_time", data.getString("create_time"));
                map.put("item_id", data.getString("item_id"));
                map.put("nickname", data.getString("nickname"));
                map.put("user", data.getString("user"));
                map.put("sex", data.getString("sex"));
                map.put("create_time", data.getString("create_time"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获得我的评论列表
     * @param json
     * @return
     */
    public static List<Map<String, String>> getMyCmt(String json) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.optJSONArray("data");

            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    Map<String, String> map = new HashMap<String, String>();
                    JSONObject jsonObject = data.getJSONObject(i);
                    map.put("content", jsonObject.getString("content"));
                    map.put("title", jsonObject.getString("title"));
                    map.put("create_time", jsonObject.getString("create_time"));
                    list.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获得发布新闻信息
     * @param json
     * @return
     */
    public static Map<String,String> getSendNewsInfo(String json){
        Map<String,String> map = new HashMap<String, String>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            map.put("code",jsonObject.getString("code"));
            map.put("message",jsonObject.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获得退出返回信息
     * @param json
     * @return
     */
    public static Map<String,String> getExitInfo(String json){
        return getSendNewsInfo(json);
    }
}