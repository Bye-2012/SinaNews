package com.moon.biz.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.common.utils.UrlUtils;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/24
 */
public class RegisterAct extends Activity {

    @ViewInject(R.id.editText_reg_name)
    private EditText editText_reg_name;
    @ViewInject(R.id.editText_reg_pwd)
    private EditText editText_reg_pwd;
    @ViewInject(R.id.editText_reg_check)
    private EditText editText_reg_check;
    @ViewInject(R.id.img_reg_verifyCode)
    private NetworkImageView img_reg_verifyCode;

    private AppCtx appCtx;
    private ImageLoader imageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ViewUtils.inject(this);

        initView();
    }

    private void initView(){
        appCtx = AppCtx.getInstance();
        imageLoader = appCtx.getImageLoader();
        String sequence = appCtx.getSequence();

        img_reg_verifyCode.setDefaultImageResId(R.drawable.logo);
        if (sequence != null) {
            img_reg_verifyCode.setImageUrl(UrlUtils.VERIFY_CODE+sequence,imageLoader);
        }
    }

    public void btnNext(View v){
        if (v.getId() == R.id.textView_reg_next){
            Intent intent = new Intent();
            intent.setClass(this,RegisterMoreInfoAct.class);
            Bundle bundle = new Bundle();
            bundle.putString("name",editText_reg_name.getText().toString());
            bundle.putString("pwd",editText_reg_pwd.getText().toString());
            bundle.putString("check",editText_reg_check.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}