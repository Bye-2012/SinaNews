package com.moon.biz.home.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.biz.home.settings.*;
import com.moon.biz.login.LoginAct;
import com.moon.common.utils.JsonInfoUtils;
import com.moon.common.utils.UrlUtils;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/21
 */
public class MainSettingFragment extends Fragment implements View.OnClickListener {

    private Button btn_set_login;
    private Button btn_set_exit;
    private TextView btn_set_collect;
    private TextView btn_set_comment;
    private TextView btn_set_upLoad;
    private TextView textView_set_name;
    private LinearLayout btn_set_channel;
    private LinearLayout btn_set_txtSize;
    private LinearLayout btn_set_cash;
    private LinearLayout btn_set_grade;
    private LinearLayout btn_set_suggestion;
    private LinearLayout btn_set_about;

    private AppCtx appCtx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCtx = AppCtx.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_setting, container, false);
        initView(ret);
        return ret;
    }

    @Override
    public void onResume() {
        super.onResume();
        String username = appCtx.getUsername();
        if (username != null && !"".equals(username)) {
            textView_set_name.setText(username);
        }
    }

    private void initView(View ret) {
        btn_set_login = (Button) ret.findViewById(R.id.btn_set_login);
        btn_set_exit = (Button) ret.findViewById(R.id.btn_set_exit);
        btn_set_collect = (TextView) ret.findViewById(R.id.btn_set_collect);
        btn_set_comment = (TextView) ret.findViewById(R.id.btn_set_comment);
        btn_set_upLoad = (TextView) ret.findViewById(R.id.btn_set_upLoad);
        textView_set_name = (TextView) ret.findViewById(R.id.textView_set_name);
        btn_set_channel = (LinearLayout) ret.findViewById(R.id.btn_set_channel);
        btn_set_txtSize = (LinearLayout) ret.findViewById(R.id.btn_set_txtSize);
        btn_set_cash = (LinearLayout) ret.findViewById(R.id.btn_set_cash);
        btn_set_grade = (LinearLayout) ret.findViewById(R.id.btn_set_grade);
        btn_set_suggestion = (LinearLayout) ret.findViewById(R.id.btn_set_suggestion);
        btn_set_about = (LinearLayout) ret.findViewById(R.id.btn_set_about);

        btn_set_login.setOnClickListener(this);
        btn_set_exit.setOnClickListener(this);
        btn_set_collect.setOnClickListener(this);
        btn_set_comment.setOnClickListener(this);
        btn_set_upLoad.setOnClickListener(this);
        btn_set_channel.setOnClickListener(this);
        btn_set_txtSize.setOnClickListener(this);
        btn_set_cash.setOnClickListener(this);
        btn_set_grade.setOnClickListener(this);
        btn_set_suggestion.setOnClickListener(this);
        btn_set_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Bundle bundle = null;
        String token = appCtx.getToken();
        if (token != null && !"".equals(token)) {
            bundle = new Bundle();
            bundle.putString("token", token);
        }
        switch (v.getId()) {
            case R.id.btn_set_login:
                intent.setClass(getActivity(), LoginAct.class);
                startActivity(intent);
                break;
            case R.id.btn_set_collect:
                if (bundle != null) {
                    intent.setClass(getActivity(), MyCollectionAct.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(AppCtx.getInstance(), "请先登陆", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_set_comment:
                if (bundle != null) {
                    intent.setClass(getActivity(), MyCommentAct.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(AppCtx.getInstance(), "请先登陆", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_set_upLoad:
                if (bundle != null) {
                    intent.setClass(getActivity(), UpLoadRecordAct.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(AppCtx.getInstance(), "请先登陆", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_set_channel:
                if (token != null && !"".equals(token)) {
                    Toast.makeText(AppCtx.getInstance(), "亲,您还不是VIP,无法使用此功能", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AppCtx.getInstance(), "请先登陆", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_set_txtSize:
                if (token != null && !"".equals(token)) {
                    Toast.makeText(AppCtx.getInstance(), "亲,您还不是VIP,无法使用此功能", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AppCtx.getInstance(), "请先登陆", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_set_cash:
                final ProgressDialog pDialog = new ProgressDialog(AppCtx.getInstance());
                pDialog.setMessage("正在清除,请稍后.....");
                pDialog.show();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        pDialog.dismiss();
                    }
                },3000);
                break;
            case R.id.btn_set_grade:
                if (token != null && !"".equals(token)) {
                    Toast.makeText(AppCtx.getInstance(), "亲,您还不是VIP,无法使用此功能", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AppCtx.getInstance(), "请先登陆", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_set_suggestion:
                intent.setClass(getActivity(), SuggestAct.class);
                startActivity(intent);
                break;
            case R.id.btn_set_about:
                intent.setClass(getActivity(), AboutAct.class);
                startActivity(intent);
                break;
            case R.id.btn_set_exit:
                if (token != null && !"".equals(token)) {
                    StringRequest request = new StringRequest(UrlUtils.EXIT + token, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null) {
                                Map<String, String> exitInfo = JsonInfoUtils.getExitInfo(response);
                                checkInfo(exitInfo);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    appCtx.getRequestQueue().add(request);
                } else {
                    Toast.makeText(AppCtx.getInstance(), "亲,还没登陆呢", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 检查退出信息
     * @param exitInfo
     */
    private void checkInfo(Map<String, String> exitInfo){
        if (exitInfo != null) {
            if (exitInfo.get("code").equals("200")){
                appCtx.setUsername("");
                appCtx.setToken("");
                Toast.makeText(AppCtx.getInstance(), "亲,记得下次登录哦", Toast.LENGTH_SHORT).show();
                textView_set_name.setText("登录账号");
            }else{
                Toast.makeText(AppCtx.getInstance(), exitInfo.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}