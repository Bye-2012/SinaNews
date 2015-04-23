package com.moon.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.moon.adapter.PicNewsAdapter;
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
 * Date:2015/4/22
 */
public class PicFragment extends Fragment {

    private ListView listView_pic;
    private int cate_id;
    private int pageNum = 0;

    private List<Map<String, Object>> pic_list;
    private PicNewsAdapter pAdapter;
    private PullToRefreshListView pullToRefreshListView_main_news;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cate_id = getArguments().getInt("cate_id") + 10;
        pic_list = new ArrayList<Map<String, Object>>();
        pAdapter = new PicNewsAdapter(pic_list, AppCtx.getInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret;
        ret = inflater.inflate(R.layout.fragment_news, container, false);
        pullToRefreshListView_main_news = (PullToRefreshListView) ret.findViewById(R.id.pullToRefreshListView_main_news);
        listView_pic = pullToRefreshListView_main_news.getRefreshableView();
        listView_pic.setAdapter(pAdapter);
        updateData();

        //设置pullToRefreshListView监听
        pullToRefreshListView_main_news.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 0;
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
     * 加载数据
     */
    private void updateData() {
        if (pageNum == 0){
            pic_list.clear();
        }

        String url = UrlUtils.PIC_NEWS_LIST + cate_id + UrlUtils.NEWS_END + (++pageNum);

        GetJsonString.getJsonString(url, new GetJsonString.StringListener() {
            @Override
            public void stringListener(String jsonStr) {
                if (jsonStr != null) {
                    List<Map<String, Object>> list = JsonInfoUtils.getPicList(jsonStr);
                    pic_list.addAll(list);
                    pAdapter.notifyDataSetChanged();
                }
                pullToRefreshListView_main_news.onRefreshComplete();
            }
        });
    }
}