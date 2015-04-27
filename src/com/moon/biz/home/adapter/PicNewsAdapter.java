package com.moon.biz.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.moon.app.AppCtx;
import com.moon.biz.R;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/22
 */
public class PicNewsAdapter extends BaseAdapter {
    private List<Map<String, Object>> list;
    private Context context;
    private LayoutInflater inflater;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public PicNewsAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.requestQueue = AppCtx.getInstance().getRequestQueue();
        this.imageLoader = AppCtx.getInstance().getImageLoader();
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
            ret = inflater.inflate(R.layout.item_listview_picnews, parent, false);
        }

        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.img_pic_1 = (ImageView) ret.findViewById(R.id.img_pic_1);
            holder.img_pic_2 = (ImageView) ret.findViewById(R.id.img_pic_2);
            holder.img_pic_3 = (ImageView) ret.findViewById(R.id.img_pic_3);
            holder.img_pic_4 = (ImageView) ret.findViewById(R.id.img_pic_4);
            holder.txt_pic_title = (TextView) ret.findViewById(R.id.txt_pic_title);
            holder.txt_pic_descript = (TextView) ret.findViewById(R.id.txt_pic_descript);
            holder.layout_pic34 = (LinearLayout) ret.findViewById(R.id.layout_pic34);
            holder.layout_picall = (LinearLayout) ret.findViewById(R.id.layout_picall);
            ret.setTag(holder);
        }

        holder.txt_pic_title.setText(list.get(position).get("title").toString());
        holder.txt_pic_descript.setText(list.get(position).get("descript").toString());

        List<Map<Integer, String>> pic_list = (List<Map<Integer, String>>) list.get(position).get("pic_list");
        if (pic_list != null) {

            holder.layout_picall.setVisibility(View.VISIBLE);
            holder.img_pic_2.setVisibility(View.VISIBLE);
            holder.img_pic_4.setVisibility(View.VISIBLE);
            holder.layout_pic34.setVisibility(View.VISIBLE);

            if (pic_list.size() == 4) {
                imageLoader.get(pic_list.get(0).get(0), ImageLoader.getImageListener(holder.img_pic_1, R.drawable.logo, R.drawable.error));
                imageLoader.get(pic_list.get(1).get(1), ImageLoader.getImageListener(holder.img_pic_2, R.drawable.logo, R.drawable.error));
                imageLoader.get(pic_list.get(2).get(2), ImageLoader.getImageListener(holder.img_pic_3, R.drawable.logo, R.drawable.error));
                imageLoader.get(pic_list.get(3).get(3), ImageLoader.getImageListener(holder.img_pic_4, R.drawable.logo, R.drawable.error));
            } else if (pic_list.size() == 3) {
                imageLoader.get(pic_list.get(0).get(0), ImageLoader.getImageListener(holder.img_pic_1, R.drawable.logo, R.drawable.error));
                imageLoader.get(pic_list.get(1).get(1), ImageLoader.getImageListener(holder.img_pic_3, R.drawable.logo, R.drawable.error));
                imageLoader.get(pic_list.get(2).get(2), ImageLoader.getImageListener(holder.img_pic_4, R.drawable.logo, R.drawable.error));
                holder.img_pic_2.setVisibility(View.GONE);
            } else if (pic_list.size() == 2) {
                imageLoader.get(pic_list.get(0).get(0), ImageLoader.getImageListener(holder.img_pic_1, R.drawable.logo, R.drawable.error));
                imageLoader.get(pic_list.get(1).get(1), ImageLoader.getImageListener(holder.img_pic_2, R.drawable.logo, R.drawable.error));
                holder.layout_pic34.setVisibility(View.GONE);
            } else if (pic_list.size() == 1) {
                imageLoader.get(pic_list.get(0).get(0), ImageLoader.getImageListener(holder.img_pic_1, R.drawable.logo, R.drawable.error));
                holder.img_pic_2.setVisibility(View.GONE);
                holder.layout_pic34.setVisibility(View.GONE);
            }
        }else{
            holder.layout_picall.setVisibility(View.GONE);
        }

        return ret;
    }

    class ViewHolder {
        ImageView img_pic_1;
        ImageView img_pic_2;
        ImageView img_pic_3;
        ImageView img_pic_4;
        TextView txt_pic_title;
        TextView txt_pic_descript;
        LinearLayout layout_pic34;
        LinearLayout layout_picall;
    }
}
