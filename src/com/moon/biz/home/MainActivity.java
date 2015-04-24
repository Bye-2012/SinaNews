package com.moon.biz.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.moon.biz.R;
import com.moon.biz.home.fragment.MainNewsFragment;
import com.moon.biz.home.fragment.MainPicFragment;
import com.moon.biz.home.fragment.MainSettingFragment;
import com.moon.biz.home.fragment.MainVdoFragment;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();
        initData();
        addListener();
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

    public void addListener() {
    }

    /**
     * 图片点击事件
     *
     * @param v
     */
    public void clickImage(View v) {
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
    }

    /**
     * 设置Fragment
     *
     * @param position
     */
    private void setFragment(int position) {

        switch(position){
            case 0:
                MainNewsFragment mainNewsFragment = new MainNewsFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("tabIndex", position);
                mainNewsFragment.setArguments(bundle1);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.layout_main_fragment, mainNewsFragment);
                transaction.commit();
                break;
            case 1:
                MainPicFragment mainPicFragment = new MainPicFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putInt("tabIndex", position);
                mainPicFragment.setArguments(bundle2);
                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                transaction2.replace(R.id.layout_main_fragment, mainPicFragment);
                transaction2.commit();
                break;
            case 2:
                MainVdoFragment mainVdoFragment = new MainVdoFragment();
                Bundle bundle3 = new Bundle();
                bundle3.putInt("tabIndex", position);
                mainVdoFragment.setArguments(bundle3);
                FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                transaction3.replace(R.id.layout_main_fragment, mainVdoFragment);
                transaction3.commit();
                break;
            case 3:
                MainSettingFragment mainSettingFragment = new MainSettingFragment();
                Bundle bundle4 = new Bundle();
                bundle4.putInt("tabIndex", position);
                mainSettingFragment.setArguments(bundle4);
                FragmentTransaction transaction4 = getSupportFragmentManager().beginTransaction();
                transaction4.replace(R.id.layout_main_fragment, mainSettingFragment);
                transaction4.commit();
                break;
        }

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
}