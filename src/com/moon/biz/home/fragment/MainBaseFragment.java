package com.moon.biz.home.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.slidingmenu.lib.SlidingMenu;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/22
 */
public class MainBaseFragment extends Fragment {
    public SlidingMenu slidingMenu;

    private void setSlidingMenu() {
        //实例化
        slidingMenu = new SlidingMenu(AppCtx.getInstance());
        //设置显示模式
        slidingMenu.setMode(slidingMenu.RIGHT);
        //设置抽屉宽度
        slidingMenu.setBehindWidth(getResources().getDisplayMetrics().widthPixels * 4 / 5);
        //设置布局
        slidingMenu.setMenu(R.layout.slidingmenu_add);
        //连接到Activity
        slidingMenu.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);
        //设置阴影
        slidingMenu.setShadowDrawable(R.drawable.ic_launcher);
        //设置阴影宽度
        slidingMenu.setShadowWidth(20);
    }
}
