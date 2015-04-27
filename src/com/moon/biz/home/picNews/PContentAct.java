package com.moon.biz.home.picNews;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.moon.biz.home.adapter.SamplePagerAdapter;
import com.moon.common.utils.GetJsonString;
import com.moon.common.utils.JsonInfoUtils;
import com.moon.common.utils.UrlUtils;
import com.moon.common.widgets.views.uk.co.senab.photoview.HackyViewPager;
import com.moon.common.widgets.views.uk.co.senab.photoview.PhotoView;
import com.moon.db.model.Collection;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/24
 */
public class PContentAct extends Activity {

    private static final String ISLOCKED_ARG = "isLocked";

    private ViewPager mViewPager;
    private MenuItem menuLockItem;
    private TextView textView_pic_title;
    private TextView textView_pic_count;
    private TextView textView_pic_content;
    private SamplePagerAdapter adapter;
    private List<PhotoView> imageList;
    private LinearLayout layout_pic_txtcnt;
    private ImageView pic_cot_collectIcon;
    private EditText txt_pic_cmt;

    private boolean isCollected;
    private AppCtx appCtx;
    private int count;
    private Map<String, Object> picDetail;
    private String id;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_news);
        appCtx = AppCtx.getInstance();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//默认不打开小键盘

        initView(savedInstanceState);
        initData();
    }

    /**
     * 初始化界面
     * @param savedInstanceState
     */
    private void initView(Bundle savedInstanceState) {
        textView_pic_title = (TextView) findViewById(R.id.textView_pic_title);
        textView_pic_count = (TextView) findViewById(R.id.textView_pic_count);
        textView_pic_content = (TextView) findViewById(R.id.textView_pic_content);
        layout_pic_txtcnt = (LinearLayout) findViewById(R.id.layout_pic_txtcnt);
        pic_cot_collectIcon = (ImageView) findViewById(R.id.pic_cot_collectIcon);
        txt_pic_cmt = (EditText) findViewById(R.id.txt_pic_cmt);

        mViewPager = (HackyViewPager) findViewById(R.id.pcontent_viewpager);

        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) mViewPager).setLocked(isLocked);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        imageList = new ArrayList<PhotoView>();

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        count = bundle.getInt("count");

        url = UrlUtils.PIC_NEWS_DETAIL + id;
        GetJsonString.getJsonString(url, new GetJsonString.StringListener() {
            @Override
            public void stringListener(String jsonStr) {
                if (jsonStr != null) {
                    picDetail = JsonInfoUtils.getPicDetail(jsonStr);
                    if (picDetail != null) {
                        setImages(picDetail);
                        textView_pic_title.setText(picDetail.get("title").toString());
                        textView_pic_count.setText(1 + "/" + count);
                        textView_pic_content.setText(picDetail.get("content").toString());
                    }
                }
            }
        });
    }

    /**
     * 设置展示图片的ViewPager
     * @param picDetail
     */
    private void setImages(Map<String, Object> picDetail) {
        List<Map<Integer, String>> picList = (List<Map<Integer, String>>) picDetail.get("pic_list");

        adapter = new SamplePagerAdapter(imageList);
        mViewPager.setAdapter(adapter);

        if (picList != null) {
            for (int i = 0; i < picList.size(); i++) {
                String url = picList.get(i).get(i);

                PhotoView photoView = new PhotoView(this);
                Picasso.with(this).load(url).error(R.drawable.error).into(photoView);
                imageList.add(photoView);
                adapter.notifyDataSetChanged();
            }
        }


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                textView_pic_count.setText((i + 1) + "/" + count);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }

    /**
     * 控件点击事件
     * @param v
     */
    public void clickIcon(View v) {
        final String token = appCtx.getToken();
        switch (v.getId()) {
            case R.id.pic_cot_backIcon:
                this.finish();
                break;
            case R.id.pic_cot_collectIcon:
                if (token != null && !"".equals(token)) {
                    DbUtils db = appCtx.getDb();
                    if (db != null && !isCollected) {
                        Collection collection = new Collection();
                        collection.setHead_img(picDetail.get("cover_pic").toString());
                        collection.setTitle(picDetail.get("title").toString());
                        collection.setContent(picDetail.get("content").toString());
                        collection.setItem_id(Integer.parseInt(id));
                        collection.setCmt_count(count);
                        collection.setUser(appCtx.getUsername());
                        collection.setType("pic");
                        try {
                            db.save(collection);
                            pic_cot_collectIcon.setImageResource(R.drawable.collected);
                            isCollected = true;
                            Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    } else if (db != null && isCollected) {
                        WhereBuilder b = WhereBuilder.b();
                        try {
                            db.delete(Collection.class, b.expr("item_id", "=", id));
                            pic_cot_collectIcon.setImageResource(R.drawable.collect);
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
            case R.id.pic_cot_shareIcon:
                showShare();
                break;
            case R.id.img_pic_sendCmt:
                final String s = txt_pic_cmt.getText().toString();
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
                            Log.e("1111-paddcomment", "error...");
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
                    txt_pic_cmt.setText("");
                } else {
                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
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
        oks.setText(textView_pic_title.getText().toString());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

    /**
     * 检查评论返回数据
     *
     * @param cmtAddInfo
     */
    private void checkInfo(Map<String, String> cmtAddInfo) {
        if (cmtAddInfo != null) {
            Toast.makeText(this, cmtAddInfo.get("message"), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewpager_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuLockItem = menu.findItem(R.id.menu_lock);
        toggleLockBtnTitle();
        menuLockItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                toggleViewPagerScrolling();
                toggleLockBtnTitle();
                return true;
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    private void toggleViewPagerScrolling() {
        if (isViewPagerActive()) {
            ((HackyViewPager) mViewPager).toggleLock();
        }
    }

    private void toggleLockBtnTitle() {
        boolean isLocked = false;
        if (isViewPagerActive()) {
            isLocked = ((HackyViewPager) mViewPager).isLocked();
        }
        String title = (isLocked) ? getString(R.string.menu_unlock) : getString(R.string.menu_lock);
        if (menuLockItem != null) {
            menuLockItem.setTitle(title);
        }
    }

    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }
}