package com.moon.biz.home.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moon.biz.home.adapter.ContentPageAdapter;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.biz.home.textNews.fragment.NewsFragment;
import com.moon.common.utils.JsonInfoUtils;
import com.moon.common.utils.UrlUtils;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/20
 */

public class MainNewsFragment extends Fragment implements View.OnClickListener {
    private int tabIndex;
    private List<String[]> titleList;
    private LinearLayout layout_title;
    private ViewPager viewPager_content;
    private List<Fragment> list_contents;
    private SlidingMenu slidingMenu;

    private TextView[] titleViews;

    private String[] titles;
    private HorizontalScrollView scrollView;
    private EditText editText_addNews_title;
    private EditText editText_addNews_content;
    private String cate_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabIndex = getArguments().getInt("tabIndex");
        AppCtx appctx = AppCtx.getInstance();
        titleList = appctx.getTitleList();
        list_contents = new ArrayList<Fragment>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret;
        ret = inflater.inflate(R.layout.fragment_main, container, false);

        setSlidingMenu();

        initView(ret);

        setTitleView(tabIndex);

        setViewPager();

        return ret;
    }


    /**
     * 初始化界面
     * @param ret
     */
    private void initView(View ret) {
        layout_title = (LinearLayout) ret.findViewById(R.id.layout_title);
        viewPager_content = (ViewPager) ret.findViewById(R.id.viewPager_content);
        scrollView = (HorizontalScrollView) ret.findViewById(R.id.horizontalScrollView);
        ImageView img_addNews = (ImageView) ret.findViewById(R.id.img_addNews);

        ImageView img_addNews_back = (ImageView) slidingMenu.findViewById(R.id.img_addNews_back);
        ImageView img_addNews_addPic = (ImageView) slidingMenu.findViewById(R.id.img_addNews_addPic);
        ImageView img_addNews_imgs = (ImageView) slidingMenu.findViewById(R.id.img_addNews_imgs);
        TextView textView_addNews_send = (TextView) slidingMenu.findViewById(R.id.textView_addNews_send);
        editText_addNews_title = (EditText) slidingMenu.findViewById(R.id.editText_addNews_title);
        editText_addNews_content = (EditText) slidingMenu.findViewById(R.id.editText_addNews_content);
        Spinner spinner_add_txt = (Spinner) slidingMenu.findViewById(R.id.spinner_add_txt);
        String[] strings = AppCtx.getInstance().getTitleList().get(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, strings);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_add_txt.setAdapter(adapter);
        spinner_add_txt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cate_id = (position + 1) + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        img_addNews.setOnClickListener(this);
        img_addNews_back.setOnClickListener(this);
        img_addNews_addPic.setOnClickListener(this);
        textView_addNews_send.setOnClickListener(this);
    }

    /**
     * 设置滚动条
     * @param tabIndex
     */
    private void setTitleView(int tabIndex) {
        if (tabIndex != 3) {
            titles = titleList.get(tabIndex);
            titleViews = new TextView[titles.length + 1];
            for (int i = 0; i < titles.length; i++) {
                final int a = i;
                titleViews[i] = new TextView(getActivity());
                titleViews[i].setText(titles[i]);
                titleViews[i].setTextSize(22);
                titleViews[i].setPadding(15, 5, 15, 5);
                titleViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager_content.setCurrentItem(a);
                        titleViews[a].setTextColor(Color.RED);
                        scrollView.scrollTo(100 * a, 0);
                    }
                });
                layout_title.addView(titleViews[i]);
            }
            titleViews[0].setTextColor(Color.RED);
            titleViews[0].setBackgroundResource(R.drawable.textbg);
        }
    }

    /**
     * 设置头部ViewPager
     */
    private void setViewPager() {
        for (int j = 0; j < titles.length; j++) {
            NewsFragment fragment = new NewsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("cate_id", j + 1);
            fragment.setArguments(bundle);
            list_contents.add(fragment);
        }
        viewPager_content.setAdapter(new ContentPageAdapter(getChildFragmentManager(), list_contents));

        viewPager_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                for (int j = 0; j < titles.length; j++) {
                    titleViews[j].setTextColor(Color.BLACK);
                    titleViews[j].setBackgroundColor(Color.parseColor("#00000000"));
                }
                titleViews[i].setTextColor(Color.RED);
                titleViews[i].setBackgroundResource(R.drawable.textbg);
                scrollView.scrollTo(100 * i, 0);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    /**
     * 初始化 SlidingMenu
     */
    private void setSlidingMenu() {
        //实例化
        slidingMenu = new SlidingMenu(getActivity());
        //设置显示模式
        slidingMenu.setMode(SlidingMenu.RIGHT);
        //设置抽屉宽度
        slidingMenu.setBehindWidth(getResources().getDisplayMetrics().widthPixels * 9 / 10);
        //设置布局
        slidingMenu.setMenu(R.layout.slidingmenu_add);
        //连接到Activity
        slidingMenu.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);
    }

    /**
     * 控件点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        final String token = AppCtx.getInstance().getToken();
        if (token != null && !"".equals(token)) {
            switch (v.getId()) {
                case R.id.img_addNews:
                    slidingMenu.toggle();
                    break;
                case R.id.img_addNews_back:
                    slidingMenu.toggle();
                    editText_addNews_title.setText("");
                    editText_addNews_content.setText("");
                    break;
                case R.id.img_addNews_addPic:

                    break;
                case R.id.textView_addNews_send:
                    StringRequest request = new StringRequest(Request.Method.POST, UrlUtils.SEND_TXT_NEWS, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null) {
                                Map<String, String> sendNewsInfo = JsonInfoUtils.getSendNewsInfo(response);
                                checkInfo(sendNewsInfo);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("1111-sendnews", "error....");
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("token", token);
                            map.put("title", editText_addNews_title.getText().toString());
                            map.put("content", editText_addNews_content.getText().toString());
                            map.put("cate_id", cate_id);
                            map.put("pic_file", "");
                            return map;
                        }
                    };
                    AppCtx.getInstance().getRequestQueue().add(request);
                    break;
            }
        } else {
            Toast.makeText(AppCtx.getInstance(), "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkInfo(Map<String, String> sendNewsInfo){
        if (sendNewsInfo != null) {
            if (sendNewsInfo.get("code").equals("200")){
                Toast.makeText(AppCtx.getInstance(), "发布成功", Toast.LENGTH_SHORT).show();
                editText_addNews_title.setText("");
                editText_addNews_content.setText("");
                slidingMenu.toggle();
            }else{
                Toast.makeText(AppCtx.getInstance(), sendNewsInfo.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}