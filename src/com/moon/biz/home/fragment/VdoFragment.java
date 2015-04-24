package com.moon.biz.home.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.moon.adapter.PicNewsAdapter;
import com.moon.adapter.VdoNewsAdapter;
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
 * Date:2015/4/23
 */
public class VdoFragment extends Fragment {

    private ListView listView_vdo;
    private int cate_id;
    private int pageNum = 1;

    private List<Map<String, String>> vdo_list;
    private VdoNewsAdapter vAdapter;
    private PullToRefreshListView pullToRefreshListView_main_news;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cate_id = getArguments().getInt("cate_id") + 14;
        vdo_list = new ArrayList<Map<String, String>>();
        vAdapter = new VdoNewsAdapter(AppCtx.getInstance(),vdo_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret;
        ret = inflater.inflate(R.layout.fragment_news,container,false);

        pullToRefreshListView_main_news = (PullToRefreshListView) ret.findViewById(R.id.pullToRefreshListView_main_news);
        listView_vdo = pullToRefreshListView_main_news.getRefreshableView();
        listView_vdo.setAdapter(vAdapter);

        updateData();

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

    private void updateData(){
        if (pageNum == 1){
            vdo_list.clear();
        }

        String url = UrlUtils.VDO_NEWS_LIST + cate_id + UrlUtils.NEWS_END + pageNum;

        GetJsonString.getJsonString(url, new GetJsonString.StringListener() {
            @Override
            public void stringListener(String jsonStr) {
                if (jsonStr != null) {
                    List<Map<String, String>> list = JsonInfoUtils.getVdoList(jsonStr);
                    vdo_list.addAll(list);
                    vAdapter.notifyDataSetChanged();
                }
                pullToRefreshListView_main_news.onRefreshComplete();
            }
        });
    }
}
