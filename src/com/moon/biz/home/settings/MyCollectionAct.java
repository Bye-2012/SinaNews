package com.moon.biz.home.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.biz.home.adapter.TextNewsAdapter;
import com.moon.biz.home.picNews.PContentAct;
import com.moon.biz.home.textNews.TContentAct;
import com.moon.common.utils.UrlUtils;
import com.moon.common.widgets.views.pulltorefresh.PullToRefreshBase;
import com.moon.common.widgets.views.pulltorefresh.PullToRefreshListView;
import com.moon.db.model.Collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/25
 */
public class MyCollectionAct extends Activity {

    @ViewInject(R.id.pullToRefreshListView_set_collect)
    private PullToRefreshListView pullToRefreshListView_set_collect;

    @ViewInject(R.id.img_set_noneCollect)
    private ImageView img_set_noneCollect;

    private AppCtx appCtx;
    private List<Map<String,Object>> collect_list;
    private TextNewsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView(){
        ListView listView_collect = pullToRefreshListView_set_collect.getRefreshableView();
        collect_list = new ArrayList<Map<String, Object>>();
        adapter = new TextNewsAdapter(collect_list,this);
        listView_collect.setAdapter(adapter);
        pullToRefreshListView_set_collect.setEmptyView(img_set_noneCollect);

        listView_collect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idd = collect_list.get(position).get("id").toString();
                int com_count = Integer.parseInt(collect_list.get(position).get("comment_total").toString());
                String type = collect_list.get(position).get("type").toString();

                Intent intent = new Intent();

                if (type.equals("txt")) {
                    intent.setClass(MyCollectionAct.this, TContentAct.class);
                }else if(type.equals("pic")){
                    intent.setClass(MyCollectionAct.this, PContentAct.class);
                }

                Bundle bundle = new Bundle();
                bundle.putString("id", idd);
                bundle.putInt("com_count", com_count);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void initData(){
        appCtx = AppCtx.getInstance();
        DbUtils db = appCtx.getDb();
        collect_list.clear();
        if (db != null) {
            try {
                List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
                List<Collection> all = db.findAll(Selector.from(Collection.class).where(WhereBuilder.b().expr("user","=",appCtx.getUsername())));
                if (all != null && all.size() != 0) {
                    for (Collection collection : all) {
                        Map<String,Object> map = new HashMap<String, Object>();
                        map.put("descript",collection.getContent());
                        map.put("title",collection.getTitle());
                        map.put("comment_total",collection.getCmt_count());
                        map.put("cover_pic",collection.getHead_img());
                        map.put("id",collection.getItem_id());
                        map.put("type",collection.getType());
                        list.add(map);
                    }
                    collect_list.addAll(list);
                    adapter.notifyDataSetChanged();
                    pullToRefreshListView_set_collect.onRefreshComplete();
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }
}