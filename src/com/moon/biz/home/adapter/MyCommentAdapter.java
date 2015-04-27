package com.moon.biz.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.moon.biz.R;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/26
 */
public class MyCommentAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,String>> list;
    private LayoutInflater inflater;

    public MyCommentAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret;
        if (convertView != null) {
            ret = convertView;
        }else{
            ret = inflater.inflate(R.layout.item_listview_mycmt,parent,false);
        }

        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.txt_cmt_title = (TextView) ret.findViewById(R.id.txt_cmt_title);
            holder.txt_cmt_content = (TextView) ret.findViewById(R.id.txt_cmt_content);
            holder.txt_cmt_time = (TextView) ret.findViewById(R.id.txt_cmt_time);
            ret.setTag(holder);
        }

        holder.txt_cmt_title.setText(list.get(position).get("title"));
        holder.txt_cmt_content.setText(list.get(position).get("content"));
        holder.txt_cmt_time.setText(list.get(position).get("create_time"));

        return ret;
    }

    class ViewHolder{
        TextView txt_cmt_title;
        TextView txt_cmt_content;
        TextView txt_cmt_time;
    }
}
