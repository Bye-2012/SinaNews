package com.moon.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import com.moon.adapter.PicNewsAdapter;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.common.utils.GetJsonString;
import com.moon.common.utils.JsonInfoUtils;
import com.moon.common.utils.UrlUtils;

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
        listView_pic = (ListView) ret.findViewById(R.id.listView_news);
        listView_pic.setAdapter(pAdapter);
        updateData();

        listView_pic.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean isBottom = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isBottom && scrollState == SCROLL_STATE_IDLE) {
                    updateData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isBottom = (firstVisibleItem + visibleItemCount) == totalItemCount;
            }
        });

        return ret;
    }

    /**
     * 加载数据
     */
    private void updateData() {
        String url = UrlUtils.PIC_NEWS_LIST + cate_id + UrlUtils.NEWS_END + (++pageNum);

        GetJsonString.getJsonString(url, new GetJsonString.StringListener() {
            @Override
            public void stringListener(String jsonStr) {
                if (jsonStr != null) {
                    List<Map<String, Object>> list = JsonInfoUtils.getPicList(jsonStr);
                    pic_list.addAll(list);
                    pAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}