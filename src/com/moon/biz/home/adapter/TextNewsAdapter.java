package com.moon.biz.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/21
 */
public class TextNewsAdapter extends BaseAdapter {
    private List<Map<String,Object>> list;
    private Context context;
    private LayoutInflater inflater;

    public TextNewsAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
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

        if (convertView != null){
            ret = convertView;
        }else{
            ret = inflater.inflate(R.layout.item_listview_news,parent,false);
        }

        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.txt_title = (TextView) ret.findViewById(R.id.txt_news_title);
            holder.txt_descript = (TextView) ret.findViewById(R.id.txt_news_descript);
            holder.txt_comment = (TextView) ret.findViewById(R.id.txt_news_comment);
            holder.icon = (ImageView) ret.findViewById(R.id.img_news_icon);
            ret.setTag(holder);
        }

        holder.txt_title.setText(list.get(position).get("title").toString());
        holder.txt_descript.setText(list.get(position).get("descript").toString());
        holder.txt_comment.setText(list.get(position).get("comment_total").toString()+"评论");

        String url = list.get(position).get("cover_pic").toString();
        if (url != null) {
            Picasso.with(AppCtx.getInstance()).load(url).error(R.drawable.error).into(holder.icon);
        }

        return ret;

    }

    class ViewHolder{
        ImageView icon;
        TextView txt_title;
        TextView txt_descript;
        TextView txt_comment;
    }
}
