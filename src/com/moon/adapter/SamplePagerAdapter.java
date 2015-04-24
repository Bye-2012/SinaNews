package com.moon.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.moon.biz.R;
import com.moon.common.widgets.views.uk.co.senab.photoview.PhotoView;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/24
 */
public class SamplePagerAdapter extends PagerAdapter {

    private List<Bitmap> list;

    public SamplePagerAdapter(List<Bitmap> list) {
        this.list = list;
    }

    private static final int[] sDrawables = { R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper,
            R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper };

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setImageBitmap(list.get(position));

        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}