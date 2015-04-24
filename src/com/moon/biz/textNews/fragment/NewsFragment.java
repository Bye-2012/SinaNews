package com.moon.biz.textNews.fragment;

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
import com.moon.adapter.NewsPicPagerAdapter;
import com.moon.adapter.TextNewsAdapter;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.common.utils.GetJsonString;
import com.moon.common.utils.JsonInfoUtils;
import com.moon.common.utils.UrlUtils;
import com.moon.common.widgets.views.pulltorefresh.PullToRefreshBase;
import com.moon.common.widgets.views.pulltorefresh.PullToRefreshListView;

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
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        cate_id = getArguments().getInt("cate_id");
        newsList = new ArrayList<Map<String, Object>>();
        adapter = new TextNewsAdapter(newsList, getActivity());

        //获得Fragment布局
        View ret = inflater.inflate(R.layout.fragment_news, container, false);
        pullToRefreshListView_main_news = (PullToRefreshListView) ret.findViewById(R.id.pullToRefreshListView_main_news);
        ListView listView_news = pullToRefreshListView_main_news.getRefreshableView();

        //获得HeadView
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_pic, null);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout_news_pic);
        viewPager_pic = (ViewPager) view.findViewById(R.id.viewPager_headline);
        layout_viewpager = (LinearLayout) view.findViewById(R.id.layout_viewpager);
        txt_pic_title = (TextView) view.findViewById(R.id.textView_headerimage_title);

        //初始化数据
        initData();

        //设置ListView及监听
        listView_news.addHeaderView(layout);
        listView_news.setAdapter(adapter);
        listView_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idd = newsList.get(position).get("id").toString();
                int com_count = Integer.parseInt(newsList.get(position).get("comment_total").toString());

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
                updateData();
            }
        });

        return ret;
    }

    /**
     * 第一次加载数据
     */
    private void initData() {
        url = UrlUtils.NEWS_LIST + cate_id + UrlUtils.NEWS_END + (pageNum);
        GetJsonString.getJsonString(url, new GetJsonString.StringListener() {
            @Override
            public void stringListener(String jsonStr) {
                List<Map<String, Object>> list = JsonInfoUtils.getNewsList(jsonStr, cate_id, pageNum);
                if (list != null && list.size() != 0 && cate_id != 7 && pageNum == 1) {
                    List<Map<String, String>> p_list = (List<Map<String, String>>) list.get(0).get("map_pic");
                    list.remove(0);
                    setHeadView(p_list);
                }
                newsList.addAll(list);
                pageNum++;
            }
        });
    }

    /**
     * 更新数据
     */
    private void updateData() {
        if (pageNum == 1) {
            newsList.clear();
        }

        url = UrlUtils.NEWS_LIST + cate_id + UrlUtils.NEWS_END + (pageNum);

        GetJsonString.getJsonString(url, new GetJsonString.StringListener() {
            @Override
            public void stringListener(String jsonStr) {
                List<Map<String, Object>> list = JsonInfoUtils.getNewsList(jsonStr, cate_id, pageNum);

                if (list != null && list.size() != 0 && cate_id != 7 && pageNum == 1) {
                    List<Map<String, String>> p_list = (List<Map<String, String>>) list.get(0).get("map_pic");
                    list.remove(0);
                    setHeadView(p_list);
                }

                newsList.addAll(list);
                pageNum++;
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
    private void setHeadView(List<Map<String, String>> p_list) {
        if (p_list != null && p_list.size() != 0) {
            final int count = p_list.size();

            pic_list = new ArrayList<View>();
            picAdapter = new NewsPicPagerAdapter(pic_list);

            pic_titles = new String[count];

            for (int i = 0; i < count; i++) {
                String p_title = p_list.get(i).get("p_title");
                pic_titles[i] = p_title;
            }
            txt_pic_title.setText(pic_titles[0]);

            for (int i = 0; i < count; i++) {
                String url = p_list.get(i).get("p_cover_pic");
                ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        ImageView img = new ImageView(AppCtx.getInstance());
                        img.setImageBitmap(bitmap);
                        pic_list.add(img);
                        picAdapter.notifyDataSetChanged();
                    }
                }, 400, 200, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("news_pic", "error...1111");
                    }
                });
                AppCtx.getInstance().getRequestQueue().add(request);
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