package com.moon.biz.picNews;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.moon.adapter.SamplePagerAdapter;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.common.utils.GetJsonString;
import com.moon.common.utils.JsonInfoUtils;
import com.moon.common.utils.UrlUtils;
import com.moon.common.widgets.views.uk.co.senab.photoview.HackyViewPager;

import java.util.ArrayList;
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
    private List<Bitmap> imageList;

    private RequestQueue requestQueue;
    private int count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_news);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//默认不打开小键盘

        initView(savedInstanceState);
        initData();
    }

    private void initView(Bundle savedInstanceState) {
        textView_pic_title = (TextView) findViewById(R.id.textView_pic_title);
        textView_pic_count = (TextView) findViewById(R.id.textView_pic_count);
        textView_pic_content = (TextView) findViewById(R.id.textView_pic_content);

        mViewPager = (HackyViewPager) findViewById(R.id.pcontent_viewpager);

        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) mViewPager).setLocked(isLocked);
        }
    }

    private void initData() {
        imageList = new ArrayList<Bitmap>();
        requestQueue = AppCtx.getInstance().getRequestQueue();

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        count = bundle.getInt("count");

        String url = UrlUtils.PIC_NEWS_DETAIL + id;
        GetJsonString.getJsonString(url, new GetJsonString.StringListener() {
            @Override
            public void stringListener(String jsonStr) {
                if (jsonStr != null) {

                    Map<String, Object> picDetail = JsonInfoUtils.getPicDetail(jsonStr);
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

    private void setImages(Map<String, Object> picDetail) {
        List<Map<Integer, String>> picList = (List<Map<Integer, String>>) picDetail.get("pic_list");

        if (picList != null) {
            for (int i = 0; i < picList.size(); i++) {
                String url = picList.get(i).get(i);
                ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        if (response != null) {
                            imageList.add(response);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, 400, 400, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("PContentAct", "error..1111");
                    }
                });
                requestQueue.add(imageRequest);
            }
        }

        adapter = new SamplePagerAdapter(imageList);
        mViewPager.setAdapter(adapter);
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