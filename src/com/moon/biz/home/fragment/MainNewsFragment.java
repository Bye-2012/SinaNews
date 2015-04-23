package com.moon.biz.home.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.moon.adapter.ContentPageAdapter;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.fragment.NewsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/20
 */

public class MainNewsFragment extends Fragment {
    private int tabIndex;
    private AppCtx appctx;
    private List<String[]> titleList;
    private LinearLayout layout_title;
    private ViewPager viewPager_content;
    private List<Fragment> list_contents;

    private TextView[] titleViews;

    private String[] titles;
    private HorizontalScrollView scrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabIndex = getArguments().getInt("tabIndex");
        appctx = AppCtx.getInstance();
        titleList = appctx.getTitleList();
        list_contents = new ArrayList<Fragment>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret;
        ret = inflater.inflate(R.layout.fragment_main, container, false);
        layout_title = (LinearLayout) ret.findViewById(R.id.layout_title);
        viewPager_content = (ViewPager) ret.findViewById(R.id.viewPager_content);
        scrollView = (HorizontalScrollView) ret.findViewById(R.id.horizontalScrollView);
        setTitleView(tabIndex);
        setViewPager();
        return ret;
    }

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

    private void setViewPager() {
        for (int j = 0; j < titles.length; j++) {
            NewsFragment fragment = new NewsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("cate_id", j + 1);
            fragment.setArguments(bundle);
            list_contents.add(fragment);
        }
        viewPager_content.setAdapter(new ContentPageAdapter(getChildFragmentManager(), list_contents));

        viewPager_content.setOffscreenPageLimit(3);

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
}