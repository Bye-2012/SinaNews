package com.moon.app.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/20
 */
public abstract class BaseActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        doCreate();
    }

    private void doCreate(){
        initView();
        initData();
        addListener();
    }

    public abstract void initView();

    public abstract void initData();

    public abstract void addListener();
}