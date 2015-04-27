package com.moon.biz.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.biz.home.MainActivity;
import com.moon.common.utils.JsonInfoUtils;
import com.moon.common.utils.UrlUtils;
import com.squareup.picasso.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/25
 */
public class DoLoginAct extends Activity {

    @ViewInject(R.id.editText_login_name)
    private EditText editText_login_name;
    @ViewInject(R.id.editText_login_pwd)
    private EditText editText_login_pwd;

    private AppCtx appCtx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dologin);
        appCtx = AppCtx.getInstance();

        ViewUtils.inject(this);
    }

    public void clickButton(View v){
        switch (v.getId()){
            case R.id.img_dologin_back:
                this.finish();
                break;
            case R.id.textView_login_forget:

                break;
            case R.id.btn_login_login:
                final String name = editText_login_name.getText().toString();
                final String pwd = editText_login_pwd.getText().toString();
                if (!name.equals("") && !pwd.equals("")) {
                    StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, UrlUtils.LOGIN, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null) {
                                Map<String, String> loginInfo = JsonInfoUtils.getLoginInfo(response);
                                checkInfo(loginInfo);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("1111-login","error...");
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("username",name);
                            map.put("password",pwd);
                            return map;
                        }
                    };
                    appCtx.getRequestQueue().add(request);
                }else{
                    Toast.makeText(this,"请输入完整的用户名和密码",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void checkInfo(Map<String, String> loginInfo){
        if (loginInfo != null) {
            if(loginInfo.get("code").equals("0")){
                Toast.makeText(this,loginInfo.get("message"),Toast.LENGTH_SHORT).show();
            }else{
                String token = loginInfo.get("token");
                String username = loginInfo.get("username");
                appCtx.setToken(token);
                appCtx.setUsername(username);
                Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();

                // TODO 保存到SharedPreferences
                SharedPreferences prefs = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("userName", editText_login_name.getText().toString());
                editor.putString("password", editText_login_pwd.getText().toString());
                editor.commit();

                this.finish();
            }
        }
    }
}