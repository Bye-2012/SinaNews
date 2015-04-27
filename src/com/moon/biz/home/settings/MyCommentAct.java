package com.moon.biz.home.settings;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.biz.home.adapter.MyCommentAdapter;
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
 * Date:2015/4/25
 */
public class MyCommentAct extends Activity {

    @ViewInject(R.id.pullToRefreshListView_set_comment)
    private PullToRefreshListView pullToRefreshListView_set_comment;

    @ViewInject(R.id.img_set_noneComment)
    private ImageView img_set_noneComment;

    private List<Map<String, String>> cmt_list;
    private MyCommentAdapter adapter;

    private AppCtx appCtx;
    private int pageNum = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycomment);

        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView() {
        appCtx = AppCtx.getInstance();

        ListView listView_cmt = pullToRefreshListView_set_comment.getRefreshableView();
        cmt_list = new ArrayList<Map<String, String>>();
        adapter = new MyCommentAdapter(this, cmt_list);
        listView_cmt.setAdapter(adapter);
        pullToRefreshListView_set_comment.setEmptyView(img_set_noneComment);

        //设置pullToRefreshListView监听
        pullToRefreshListView_set_comment.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                initData();
            }
        });
        pullToRefreshListView_set_comment.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                pageNum++;
                initData();
            }
        });
    }

    private void initData() {
        String url = UrlUtils.MY_COMMENT + appCtx.getToken() + UrlUtils.NEWS_END + pageNum;

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    List<Map<String, String>> myCmt = JsonInfoUtils.getMyCmt(response);
                    if (myCmt != null) {
                        if (pageNum == 1){
                            cmt_list.clear();
                        }
                        cmt_list.addAll(myCmt);
                        adapter.notifyDataSetChanged();
                        pullToRefreshListView_set_comment.onRefreshComplete();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("1111-mycmt","error.....");
            }
        });
        appCtx.getRequestQueue().add(request);
    }

}