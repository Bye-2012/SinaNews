package com.moon.biz.home.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.moon.adapter.ContentPageAdapter;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.biz.textNews.fragment.NewsFragment;
import com.moon.biz.picNews.fragment.PicFragment;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/22
 */
public class MainBaseFragment extends Fragment {
    protected AppCtx appCtx;
    protected List<String[]> titleList;
    protected int tabIndex;
    protected String[] titles;
    protected HorizontalScrollView scrollView;
    protected TextView[] titleViews;
    protected int index;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tabIndex = getArguments().getInt("tabIndex");
        appCtx = AppCtx.getInstance();
        titleList = appCtx.getTitleList();
    }

    protected void setTitleView(LinearLayout layout_title, final ViewPager viewPager_content, int tabIndex, final HorizontalScrollView scrollView) {
        if (tabIndex != 3) {
            titles = titleList.get(tabIndex);
            titleViews = new TextView[titles.length + 1];
            for (index = 0; index < titles.length; index++) {
                final int a = index;
                titleViews[index] = new TextView(getActivity());
                titleViews[index].setText(titles[index]);
                titleViews[index].setTextSize(22);
                titleViews[index].setPadding(15, 5, 15, 5);
                titleViews[index].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager_content.setCurrentItem(a);
                        titleViews[a].setTextColor(Color.RED);
                        if (a > 2) {
                            scrollView.scrollTo(40 * a, 0);
                        }
                    }
                });
                layout_title.addView(titleViews[index]);
            }
            titleViews[0].setTextColor(Color.RED);
            titleViews[0].setBackgroundResource(R.drawable.textbg);
        }
    }

    protected void setViewPager(final TextView[] titleViews, final String[] titles, List<Fragment> list_contents, ViewPager viewPager_content, int tabIndex,final HorizontalScrollView scrollView) {
        for (int j = 0; j < titles.length; j++) {
            list_contents.clear();
            switch (tabIndex) {
                case 0:
                    NewsFragment newsFragment = new NewsFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("cate_id", j + 1);
                    newsFragment.setArguments(bundle1);
                    list_contents.add(newsFragment);
                    break;
                case 1:
                    PicFragment picFragment = new PicFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("cate_id", j + 1);
                    picFragment.setArguments(bundle2);
                    list_contents.add(picFragment);
                    break;
                case 2:
                    PicFragment pFragment = new PicFragment();
                    Bundle bundle3 = new Bundle();
                    bundle3.putInt("cate_id", j + 1);
                    pFragment.setArguments(bundle3);
                    list_contents.add(pFragment);
                    break;
            }
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
                if (i > 2) {
                    scrollView.scrollTo(40 * i, 0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }
}