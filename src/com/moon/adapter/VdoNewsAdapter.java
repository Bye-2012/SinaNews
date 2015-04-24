package com.moon.adapter;

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

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/23
 */
public class VdoNewsAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;
    private LayoutInflater inflater;
    private AppCtx appCtx;
    private ImageLoader imageLoader;

    public VdoNewsAdapter(Context context, List<Map<String, String>> list) {
        if (context != null && list != null) {
            this.context = context;
            this.list = list;
            this.inflater = LayoutInflater.from(context);
            this.appCtx = AppCtx.getInstance();
            this.imageLoader = appCtx.getImageLoader();
        } else {
            throw new RuntimeException("VdoAdapter get a null");
        }
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
        } else {
            ret = inflater.inflate(R.layout.item_listview_vdonews, parent, false);
        }

        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.imageView_vdo_show = (ImageView) ret.findViewById(R.id.imageView_vdo_show);
            holder.textView_vdo_time = (TextView) ret.findViewById(R.id.textView_vdo_time);
            holder.textView_vdo_title = (TextView) ret.findViewById(R.id.textView_vdo_title);
            holder.textView_vdo_count = (TextView) ret.findViewById(R.id.textView_vdo_count);
            ret.setTag(holder);
        }

        if (list.size() > 0) {
            imageLoader.get(list.get(position).get("pic"), ImageLoader.getImageListener(holder.imageView_vdo_show, R.drawable.logo, R.drawable.error));
            holder.textView_vdo_title.setText(list.get(position).get("title"));
            holder.textView_vdo_count.setText(list.get(position).get("play_num") + "播放");
        }

        return ret;
    }

    class ViewHolder {
        ImageView imageView_vdo_show;
        TextView textView_vdo_time;
        TextView textView_vdo_title;
        TextView textView_vdo_count;
    }
}