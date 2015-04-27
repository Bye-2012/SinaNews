package com.moon.biz.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.biz.home.fragment.MainNewsFragment;
import com.moon.biz.home.fragment.MainPicFragment;
import com.moon.biz.home.fragment.MainSettingFragment;
import com.moon.biz.home.fragment.MainVdoFragment;
import com.moon.common.utils.JsonInfoUtils;
import com.moon.common.utils.UrlUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity {
    private ImageView img_tab_news;
    private ImageView img_tab_pic;
    private ImageView img_tab_vdo;
    private ImageView img_tab_set;

    private TextView txt_tab_news;
    private TextView txt_tab_pic;
    private TextView txt_tab_vdo;
    private TextView txt_tab_set;
    private int[] ids;
    private int[] pic1;
    private int[] pic2;
    private ImageView[] imgs;
    private TextView[] txts;

    private MainNewsFragment mainNewsFragment;
    private MainPicFragment  mainPicFragment;
    private MainVdoFragment mainVdoFragment;
    private MainSettingFragment mainSettingFragment;

    private FragmentManager fragmentManager;

    private boolean isConnected;

    private AppCtx appCtx = AppCtx.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();

        fragmentManager = getSupportFragmentManager();

        //网络判断
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            isConnected = true;

            //判断是否有登录记录
            SharedPreferences prefs = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
            final String username = prefs.getString("userName", "");
            final String password = prefs.getString("password", "");
            if (!"".equals(username) && !"".equals(password)) {
                StringRequest request = new StringRequest(Request.Method.POST, UrlUtils.LOGIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            Map<String, String> loginInfo = JsonInfoUtils.getLoginInfo(response);
                            checkInfo(loginInfo);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("username", username);
                        map.put("password", password);
                        return map;
                    }
                };
                appCtx.getRequestQueue().add(request);
            }else{
                Toast.makeText(AppCtx.getInstance(), "亲,还没登陆呢", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "网络异常,请检查", Toast.LENGTH_SHORT).show();
        }

        initData();
    }

    public void initView() {
        img_tab_news = (ImageView) findViewById(R.id.img_tab_news);
        img_tab_pic = (ImageView) findViewById(R.id.img_tab_pic);
        img_tab_vdo = (ImageView) findViewById(R.id.img_tab_vdo);
        img_tab_set = (ImageView) findViewById(R.id.img_tab_set);
        imgs = new ImageView[]{img_tab_news, img_tab_pic, img_tab_vdo, img_tab_set};

        txt_tab_news = (TextView) findViewById(R.id.txt_tab_news);
        txt_tab_pic = (TextView) findViewById(R.id.txt_tab_pic);
        txt_tab_vdo = (TextView) findViewById(R.id.txt_tab_vdo);
        txt_tab_set = (TextView) findViewById(R.id.txt_tab_set);
        txts = new TextView[]{txt_tab_news, txt_tab_pic, txt_tab_vdo, txt_tab_set};
    }

    public void initData() {
        ids = new int[]{R.id.img_tab_news, R.id.img_tab_pic, R.id.img_tab_vdo, R.id.img_tab_set};
        pic1 = new int[]{R.drawable.tabbar_news, R.drawable.tabbar_picture, R.drawable.tabbar_video, R.drawable.tabbar_setting};
        pic2 = new int[]{R.drawable.tabbar_news_hl, R.drawable.tabbar_picture_hl, R.drawable.tabbar_video_hl, R.drawable.tabbar_setting_hl};
        setFragment(0);
        setImage(ids[0]);
    }

    /**
     * 图片点击事件
     *
     * @param v
     */
    public void clickImage(View v) {
        if (isConnected) {
            int id = v.getId();
            switch (id) {
                case R.id.img_tab_news:
                    setFragment(0);
                    break;
                case R.id.img_tab_pic:
                    setFragment(1);
                    break;
                case R.id.img_tab_vdo:
                    setFragment(2);
                    break;
                case R.id.img_tab_set:
                    setFragment(3);
                    break;
            }
            setImage(id);
        } else {
            Toast.makeText(this, "网络异常,请检查", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置Fragment
     *
     * @param position
     */
    private void setFragment(int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (position) {
            case 0:
                if (mainNewsFragment == null) {
                    mainNewsFragment = new MainNewsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("tabIndex", position);
                    mainNewsFragment.setArguments(bundle);
                    transaction.add(R.id.layout_main_fragment,mainNewsFragment);
                }else{
                    transaction.show(mainNewsFragment);
                }
                break;
            case 1:
                if (mainPicFragment == null) {
                    mainPicFragment = new MainPicFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("tabIndex", position);
                    mainPicFragment.setArguments(bundle);
                    transaction.add(R.id.layout_main_fragment,mainPicFragment);
                }else{
                    transaction.show(mainPicFragment);
                }
                break;
            case 2:
                if (mainVdoFragment == null) {
                    mainVdoFragment = new MainVdoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("tabIndex", position);
                    mainVdoFragment.setArguments(bundle);
                    transaction.add(R.id.layout_main_fragment,mainVdoFragment);
                }else{
                    transaction.show(mainVdoFragment);
                }
                break;
            case 3:
                if (mainSettingFragment == null) {
                    mainSettingFragment = new MainSettingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("tabIndex", position);
                    mainSettingFragment.setArguments(bundle);
                    transaction.add(R.id.layout_main_fragment,mainSettingFragment);
                }else{
                    transaction.show(mainSettingFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 设置图片文字点击变红
     *
     * @param id
     */
    private void setImage(int id) {
        for (int i = 0; i < ids.length; i++) {
            if (id == ids[i]) {
                imgs[i].setImageResource(pic2[i]);
                txts[i].setTextColor(Color.RED);
            } else {
                imgs[i].setImageResource(pic1[i]);
                txts[i].setTextColor(Color.BLACK);
            }
        }
    }

    /**
     * 登录检测
     */
    private void checkInfo(Map<String, String> loginInfo) {
        if (loginInfo != null) {
            if (loginInfo.get("code").equals("200")) {
                appCtx.setToken(loginInfo.get("token"));
                appCtx.setUsername(loginInfo.get("username"));
                Toast.makeText(this, "登录成功,用户名为" + loginInfo.get("username"), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(AppCtx.getInstance(), "亲,还没登陆呢", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(AppCtx.getInstance(), "亲,还没登陆呢", Toast.LENGTH_LONG).show();
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mainNewsFragment != null) {
            transaction.hide(mainNewsFragment);
        }
        if (mainPicFragment != null) {
            transaction.hide(mainPicFragment);
        }
        if (mainVdoFragment != null) {
            transaction.hide(mainVdoFragment);
        }
        if (mainSettingFragment != null) {
            transaction.hide(mainSettingFragment);
        }
    }
}