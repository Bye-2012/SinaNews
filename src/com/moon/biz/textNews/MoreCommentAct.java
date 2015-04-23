package com.moon.biz.textNews;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.moon.adapter.CommentAdapter;
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
public class MoreCommentAct extends Activity {
    private int pageNum = 0;
    private List<Map<String, String>> commentList;
    private ListView comment_list;
    private PullToRefreshListView pullToRefreshListView_cmt;
    private String id;
    private CommentAdapter cAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morecmt);

        initView();
        initData();
        updateData();
        addListener();
    }

    private void initView() {
        pullToRefreshListView_cmt = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView_cmt);
        comment_list = pullToRefreshListView_cmt.getRefreshableView();
        TextView news_emptyinfo = (TextView) findViewById(R.id.textView_cmt_emptyInfo);
        comment_list.setEmptyView(news_emptyinfo);
    }

    private void initData() {
        id = getIntent().getExtras().getString("id");
        commentList = new ArrayList<Map<String, String>>();
        cAdapter = new CommentAdapter(this, commentList);
        comment_list.setAdapter(cAdapter);
    }

    private void updateData() {
        if (pageNum == 0){
            commentList.clear();
        }
        String url_comment = UrlUtils.NEWS_COMMENT + id + UrlUtils.NEWS_END + (++pageNum);
        GetJsonString.getJsonString(url_comment, new GetJsonString.StringListener() {
            @Override
            public void stringListener(String jsonStr) {
                if (jsonStr != null) {
                    List<Map<String, String>> list = JsonInfoUtils.getCommentList(jsonStr);
                    commentList.addAll(list);
                    cAdapter.notifyDataSetChanged();
                    pullToRefreshListView_cmt.onRefreshComplete();
                }
            }
        });
    }

    private void addListener() {

        //设置pullToRefreshListView监听
        pullToRefreshListView_cmt.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 0;
                updateData();
            }
        });
        pullToRefreshListView_cmt.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                updateData();
            }
        });

    }

    public void clickBackIcon(View v){
        if (v.getId() == R.id.imageView_cmt_back){
            this.finish();
        }
    }
}