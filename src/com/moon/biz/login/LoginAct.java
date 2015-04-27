package com.moon.biz.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.moon.biz.R;
import com.moon.biz.register.RegisterAct;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/24
 */
public class LoginAct extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void clickButton(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_register:
                intent.setClass(this, RegisterAct.class);
                break;
            case R.id.btn_login:
                intent.setClass(this,DoLoginAct.class);
                break;
        }
        startActivity(intent);
        this.finish();
    }
}