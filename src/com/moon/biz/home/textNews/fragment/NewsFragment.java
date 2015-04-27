package com.moon.biz.home.textNews.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.moon.biz.home.adapter.NewsPicPagerAdapter;
import com.moon.biz.home.adapter.TextNewsAdapter;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.biz.home.textNews.TContentAct;
import com.moon.common.utils.GetJsonString;
import com.moon.common.utils.JsonInfoUtils;
import com.moon.common.utils.UrlUtils;
import com.moon.common.widgets.views.pulltorefresh.PullToRefreshBase;
import com.moon.common.widgets.views.pulltorefresh.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/21
 */
public class NewsFragment extends Fragment {

    private List<Map<String, Object>> newsList;//ListView的数据源
    private TextNewsAdapter adapter;

    private String url;
    private int cate_id;
    private int pageNum = 1;
    private List<View> pic_list;
    private NewsPicPagerAdapter picAdapter;

    private ImageView[] dots;
    private String[] pic_titles;
    private ViewPager viewPager_pic;
    private LinearLayout layout_viewpager;
    private TextView txt_pic_title;
    private PullToRefreshListView pullToRefreshListView_main_news;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cate_id = getArguments().getInt("cate_id");
        newsList = new ArrayList<Map<String, Object>>();
        adapter = new TextNewsAdapter(newsList, getActivity());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //获得Fragment布局
        View ret = inflater.inflate(R.layout.fragment_news, container, false);

        //初始化界面
        initView(ret);

        //初始化数据
        initData();

        return ret;
    }

    /**
     * 初始化界面并添加监听
     *
     * @param ret
     */
    private void initView(View ret) {
        pullToRefreshListView_main_news = (PullToRefreshListView) ret.findViewById(R.id.pullToRefreshListView_main_news);
        ListView listView_news = pullToRefreshListView_main_news.getRefreshableView();
        LinearLayout progressBar = (LinearLayout) ret.findViewById(R.id.progressBar);

        //获得HeadView
        View view = LayoutInflater.from(AppCtx.getInstance()).inflate(R.layout.news_pic, null);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout_news_pic);
        viewPager_pic = (ViewPager) view.findViewById(R.id.viewPager_headline);
        layout_viewpager = (LinearLayout) view.findViewById(R.id.layout_viewpager);
        txt_pic_title = (TextView) view.findViewById(R.id.textView_headerimage_title);

        pullToRefreshListView_main_news.setEmptyView(progressBar);

        //设置ListView及监听
        listView_news.addHeaderView(layout);
        listView_news.setAdapter(adapter);
        listView_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 1) {
                    String idd = newsList.get(position - 1).get("id").toString();
                    int com_count = Integer.parseInt(newsList.get(position - 1).get("comment_total").toString());
                    if (idd != null && com_count != -1) {
                        Intent intent = new Intent();
                        intent.setAction("com.moon.activity.txt.content");
                        intent.addCategory("android.intent.category.DEFAULT");
                        Bundle bundle = new Bundle();
                        bundle.putString("id", idd);
                        bundle.putInt("com_count", com_count);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });

        //设置pullToRefreshListView监听
        pullToRefreshListView_main_news.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                updateData();
            }
        });
        pullToRefreshListView_main_news.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                pageNum++;
                updateData();
            }
        });
    }


    /**
     * 第一次加载数据
     */
    private void initData() {
        pic_list = new ArrayList<View>();
        picAdapter = new NewsPicPagerAdapter(pic_list);
        pageNum = 1;

        url = UrlUtils.NEWS_LIST + cate_id + UrlUtils.NEWS_END + (pageNum);
        GetJsonString.getJsonString(url, new GetJsonString.StringListener() {
            @Override
            public void stringListener(String jsonStr) {
                List<Map<String, Object>> list = JsonInfoUtils.getNewsList(jsonStr, cate_id, pageNum);
                if (list != null && list.size() != 0 && pageNum == 1) {
                    List<Map<String, String>> p_list = (List<Map<String, String>>) list.get(0).get("map_pic");
                    list.remove(0);
                    setHeadView(p_list);
                }
                newsList.addAll(list);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 更新数据
     */
    private void updateData() {
        url = UrlUtils.NEWS_LIST + cate_id + UrlUtils.NEWS_END + (pageNum);

        GetJsonString.getJsonString(url, new GetJsonString.StringListener() {
            @Override
            public void stringListener(String jsonStr) {
                List<Map<String, Object>> list = JsonInfoUtils.getNewsList(jsonStr, cate_id, pageNum);
                if (list != null && list.size() != 0 && pageNum == 1) {
                    newsList.clear();
                    List<Map<String, String>> p_list = (List<Map<String, String>>) list.get(0).get("map_pic");
                    list.remove(0);
                    setHeadView(p_list);
                }
                newsList.addAll(list);
                adapter.notifyDataSetChanged();
                pullToRefreshListView_main_news.onRefreshComplete();
            }
        });
    }

    /**
     * 设置上方ViewPager
     *
     * @param p_list
     */
    private void setHeadView(final List<Map<String, String>> p_list) {
        if (p_list != null && p_list.size() != 0) {
            final int count = p_list.size();

            pic_titles = new String[count];

            for (int i = 0; i < count; i++) {
                String p_title = p_list.get(i).get("p_title");
                pic_titles[i] = p_title;
            }
            txt_pic_title.setText(pic_titles[0]);

            for (int i = 0; i < count; i++) {
                String url = p_list.get(i).get("p_cover_pic");
                final String p_id = p_list.get(i).get("p_id");

                ImageView img = new ImageView(AppCtx.getInstance());
                Picasso.with(AppCtx.getInstance()).load(url).error(R.drawable.error).into(img);
                img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                img.setScaleType(ImageView.ScaleType.FIT_XY);

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction("com.moon.activity.txt.content");
                        intent.addCategory("android.intent.category.DEFAULT");
                        Bundle bundle = new Bundle();
                        bundle.putString("id", p_id);
                        bundle.putInt("com_count", 5);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                pic_list.add(img);
                picAdapter.notifyDataSetChanged();
            }

            viewPager_pic.setAdapter(picAdapter);

            //设置监听
            viewPager_pic.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                }

                @Override
                public void onPageSelected(int i) {
                    for (int j = 0; j < count; j++) {
                        dots[j].setEnabled(true);//首先让所有"点"都可用
                    }
                    dots[i].setEnabled(false);//让当前图片所代表的"点"失效
                    txt_pic_title.setText(pic_titles[i]);//根据page加载title
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                }
            });

            initDot(count);
        }
    }

    /**
     * 初始化下方的点
     *
     * @param count
     */
    private void initDot(int count) {
        //设置"点"
        dots = new ImageView[count];
        layout_viewpager.removeAllViews();
        for (int i = 0; i < count; i++) {
            dots[i] = new ImageView(AppCtx.getInstance());
            dots[i].setImageResource(R.drawable.dot);
            layout_viewpager.addView(dots[i]);
            dots[i].setEnabled(true);//设置为可点击
            dots[i].setTag(i);//设置标签
            dots[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager_pic.setCurrentItem((Integer) v.getTag());//根据标签跳转
                }
            });
        }
        dots[0].setEnabled(false);//默认将第一个"点"设置为不可用
    }
}