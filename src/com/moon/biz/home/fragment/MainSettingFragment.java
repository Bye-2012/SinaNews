package com.moon.biz.home.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.moon.biz.R;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/21
 */
public class MainSettingFragment extends Fragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_setting,container,false);

        return ret;
    }
}