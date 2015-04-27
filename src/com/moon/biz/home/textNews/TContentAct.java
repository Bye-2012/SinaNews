package com.moon.biz.home.textNews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.*;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.biz.home.adapter.CommentAdapter;
import com.moon.common.utils.GetJsonString;
import com.moon.common.utils.JsonInfoUtils;
import com.moon.common.utils.UrlUtils;
import com.moon.db.model.Collection;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/23
 */
public class TContentAct extends Activity {

    private ImageView txt_cot_img;
    private TextView txt_cot_cretime;
    private TextView txt_cot_title;
    private TextView txt_cot_content;
    private TextView txt_cot_cmtCount;
    private ListView listView_cnt_comment;
    private ImageView txt_cot_collectIcon;
    private int com_count;

    private String id;
    private Bundle bundle;

    private boolean isCollected;
    private EditText txt_cot_write;

    private AppCtx appCtx;
    private Map<String, Object> newsDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt_news);

        appCtx = AppCtx.getInstance();

        bundle = getIntent().getExtras();
        id = bundle.getString("id");
        com_count = bundle.getInt("com_count");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//默认不打开小键盘

        initView();

        initData();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        txt_cot_img = (ImageView) findViewById(R.id.txt_cot_img);
        txt_cot_collectIcon = (ImageView) findViewById(R.id.txt_cot_collectIcon);
        txt_cot_cretime = (TextView) findViewById(R.id.txt_cot_cretime);
        txt_cot_title = (TextView) findViewById(R.id.txt_cot_title);
        txt_cot_content = (TextView) findViewById(R.id.txt_cot_content);
        TextView txt_cot_emptyinfo = (TextView) findViewById(R.id.txt_cot_emptyinfo);
        txt_cot_cmtCount = (TextView) findViewById(R.id.txt_cot_cmtCount);
        listView_cnt_comment = (ListView) findViewById(R.id.listView_cnt_comment);
        listView_cnt_comment.setEmptyView(txt_cot_emptyinfo);

        final LinearLayout layout_noKey = (LinearLayout) findViewById(R.id.layout_noKey);
        final LinearLayout layout_Key = (LinearLayout) findViewById(R.id.layout_Key);

        txt_cot_write = (EditText) findViewById(R.id.txt_cot_write);

        // TODO 判断键盘是否打开
        final RelativeLayout layout_content = (RelativeLayout) findViewById(R.id.layout_content);
        layout_content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = layout_content.getRootView().getHeight() - layout_content.getHeight();
                if (heightDiff > 100) {
                    //大小超过100时，一般为显示虚拟键盘事件
                    //Toast.makeText(TContentAct.this,"kaile",Toast.LENGTH_SHORT).show();
                    layout_noKey.setVisibility(View.GONE);
                    layout_Key.setVisibility(View.VISIBLE);
                    txt_cot_write.setFocusable(true);
                    txt_cot_write.setFocusableInTouchMode(true);
                    txt_cot_write.requestFocus();
                    txt_cot_write.requestFocusFromTouch();
                } else {
                    //大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
                    //Toast.makeText(TContentAct.this,"guanle",Toast.LENGTH_SHORT).show();
                    layout_noKey.setVisibility(View.VISIBLE);
                    layout_Key.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String url_news = UrlUtils.NEWS_DETAILS + id;
        String url_comment = UrlUtils.NEWS_COMMENT + id + UrlUtils.NEWS_END + 1;

        /**
         * 加载详情
         */
        GetJsonString.getJsonString(url_news, new GetJsonString.StringListener() {
            @Override
            public void stringListener(String jsonStr) {
                newsDetail = JsonInfoUtils.getNewsDetail(jsonStr);
                setViews(newsDetail);
            }
        });

        /**
         * 如果有评论就加载
         */
        if (com_count != 0) {
            GetJsonString.getJsonString(url_comment, new GetJsonString.StringListener() {
                @Override
                public void stringListener(String jsonStr) {
                    List<Map<String, String>> commentList = JsonInfoUtils.getCommentList(jsonStr);
                    setListView(commentList);
                }
            });
        }
    }

    /**
     * 设置界面
     *
     * @param newsDetail
     */
    private void setViews(Map<String, Object> newsDetail) {
        Picasso.with(this).load(newsDetail.get("cover_pic").toString()).error(R.drawable.error).into(txt_cot_img);
        txt_cot_cretime.setText(newsDetail.get("create_time").toString());
        txt_cot_title.setText(newsDetail.get("title").toString());
        txt_cot_content.setText(newsDetail.get("content").toString());
        txt_cot_cmtCount.setText(com_count + "");
    }

    /**
     * 设置评论数据
     * @param commentList
     */
    private void setListView(List<Map<String, String>> commentList) {
        CommentAdapter adapter = new CommentAdapter(this, commentList);
        listView_cnt_comment.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView_cnt_comment);
    }

    /**
     * 标题栏图标点击事件
     *
     * @param v
     */
    public void clickIcon(View v) {
        switch (v.getId()) {
            /**
             * 回退
             */
            case R.id.txt_cot_backIcon:
                this.finish();
                break;
            /**
             * 收藏
             */
            case R.id.txt_cot_collectIcon:
                if (appCtx.getToken() != null) {
                    DbUtils db = appCtx.getDb();
                    if (db != null && !isCollected) {
                        Collection collection = new Collection();
                        collection.setHead_img(newsDetail.get("cover_pic").toString());
                        collection.setTitle(newsDetail.get("title").toString());
                        collection.setContent(newsDetail.get("content").toString());
                        collection.setItem_id(Integer.parseInt(id));
                        collection.setCmt_count(com_count);
                        collection.setUser(appCtx.getUsername());
                        collection.setType("txt");
                        try {
                            db.save(collection);
                            txt_cot_collectIcon.setImageResource(R.drawable.collected);
                            isCollected = true;
                            Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }else if(db != null && isCollected){
                        WhereBuilder b = WhereBuilder.b();
                        try {
                            db.delete(Collection.class, b.expr("item_id","=",id));
                            txt_cot_collectIcon.setImageResource(R.drawable.collect);
                            isCollected = false;
                            Toast.makeText(this, "取消收藏", Toast.LENGTH_SHORT).show();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
            /**
             * 分享
             */
            case R.id.txt_cot_shareIcon:
                showShare();
                break;
            /**
             * 发送
             */
            case R.id.txt_cot_send:
                final String s = txt_cot_write.getText().toString();
                final String token = appCtx.getToken();
                if (token != null && !"".equals(token)) {
                    StringRequest request = new StringRequest(Request.Method.POST, UrlUtils.SEND_COMMENT, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null) {
                                Map<String, String> cmtAddInfo = JsonInfoUtils.getCmtAddInfo(response);
                                checkInfo(cmtAddInfo);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("1111-addcomment", "error...");
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("content", s);
                            map.put("news_id", id);
                            map.put("token", token);
                            return map;
                        }
                    };
                    appCtx.getRequestQueue().add(request);
                    txt_cot_write.setText("");
                } else {
                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 点击"下一步"
     *
     * @param v
     */
    public void getMoreComments(View v) {
        if (v.getId() == R.id.txt_cot_moreCmt) {
            Intent intent = new Intent(this, MoreCommentAct.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * 控制ListView高度
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 检查评论返回数据
     *
     * @param cmtAddInfo
     */
    private void checkInfo(Map<String, String> cmtAddInfo) {
        if (cmtAddInfo != null) {
            Toast.makeText(this, cmtAddInfo.get("message"), Toast.LENGTH_SHORT).show();
            initData();
        }
    }

    /**
     * 一键分享
     */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}