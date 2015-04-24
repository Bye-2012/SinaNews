package com.moon.biz.textNews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.moon.adapter.CommentAdapter;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.common.utils.GetJsonString;
import com.moon.common.utils.JsonInfoUtils;
import com.moon.common.utils.UrlUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/23
 */
public class TContentAct extends Activity {

    private ImageView txt_cot_img;
    private TextView txt_cot_cretime;
    private TextView txt_cot_title;
    private TextView txt_cot_content;
    private TextView txt_cot_cmtCount;
    private ListView listView_cnt_comment;
    private int com_count;

    private ImageLoader imageLoader;
    private String id;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt_news);

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//默认不打开小键盘

        initView();
        initData();
    }

    private void initView(){
        txt_cot_img = (ImageView) findViewById(R.id.txt_cot_img);
        txt_cot_cretime = (TextView) findViewById(R.id.txt_cot_cretime);
        txt_cot_title = (TextView) findViewById(R.id.txt_cot_title);
        txt_cot_content = (TextView) findViewById(R.id.txt_cot_content);
        TextView txt_cot_emptyinfo = (TextView) findViewById(R.id.txt_cot_emptyinfo);
        txt_cot_cmtCount = (TextView) findViewById(R.id.txt_cot_cmtCount);
        listView_cnt_comment = (ListView) findViewById(R.id.listView_cnt_comment);
        listView_cnt_comment.setEmptyView(txt_cot_emptyinfo);
    }

    private void initData(){
        bundle = getIntent().getExtras();
        id = bundle.getString("id");
        com_count = bundle.getInt("com_count");

        AppCtx appCtx = AppCtx.getInstance();
        imageLoader = appCtx.getImageLoader();

        String url_news = UrlUtils.NEWS_DETAILS + id;
        String url_comment = UrlUtils.NEWS_COMMENT + id + UrlUtils.NEWS_END + 1;

        GetJsonString.getJsonString(url_news, new GetJsonString.StringListener() {
            @Override
            public void stringListener(String jsonStr) {
                Map<String, Object> newsDetail = JsonInfoUtils.getNewsDetail(jsonStr);
                setViews(newsDetail);
            }
        });

        if (com_count != 0){
            GetJsonString.getJsonString(url_comment, new GetJsonString.StringListener() {
                @Override
                public void stringListener(String jsonStr) {
                    List<Map<String, String>> commentList = JsonInfoUtils.getCommentList(jsonStr);
                    setListView(commentList);
                }
            });
        }
    }

    /**
     * 设置界面
     * @param newsDetail
     */
    private void setViews(Map<String, Object> newsDetail){
        imageLoader.get(newsDetail.get("cover_pic").toString(),ImageLoader.getImageListener(txt_cot_img,R.drawable.logo,R.drawable.error));
        txt_cot_cretime.setText(newsDetail.get("create_time").toString());
        txt_cot_title.setText(newsDetail.get("title").toString());
        txt_cot_content.setText(newsDetail.get("content").toString());
        txt_cot_cmtCount.setText(com_count+"");
    }

    private void setListView(List<Map<String, String>> commentList){
        CommentAdapter adapter = new CommentAdapter(this, commentList);
        listView_cnt_comment.setAdapter(adapter);
    }

    /**
     * 标题栏图标点击事件
     * @param v
     */
    public void clickIcon(View v){
        switch(v.getId()){
            case R.id.txt_cot_backIcon:
                this.finish();
                break;
            case R.id.txt_cot_collectIcon:

                break;
            case R.id.txt_cot_shareIcon:

                break;
        }
    }

    public void getMoreComments(View v){

        if (v.getId() == R.id.txt_cot_moreCmt){
            Intent intent = new Intent(this,MoreCommentAct.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}