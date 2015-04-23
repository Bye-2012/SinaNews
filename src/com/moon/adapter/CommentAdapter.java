package com.moon.adapter;

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
 * Date:2015/4/23
 */
public class CommentAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;
    private LayoutInflater inflater;

    public CommentAdapter(Context context, List<Map<String, String>> list) {
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
        View ret = null;

        if (convertView != null) {
            ret = convertView;
        } else {
            ret = inflater.inflate(R.layout.item_news_comment, parent, false);
        }

        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.item_comment_user = (TextView) ret.findViewById(R.id.item_comment_user);
            holder.item_comment_time = (TextView) ret.findViewById(R.id.item_comment_time);
            holder.item_comment_content = (TextView) ret.findViewById(R.id.item_comment_content);
            ret.setTag(holder);
        }

        holder.item_comment_user.setText(list.get(position).get("user"));
        holder.item_comment_time.setText(list.get(position).get("create_time"));
        holder.item_comment_content.setText(list.get(position).get("content"));

        return ret;
    }

    class ViewHolder {
        TextView item_comment_user;
        TextView item_comment_time;
        TextView item_comment_content;
    }
}