package com.moon.biz.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.app.AppCtx;
import com.moon.biz.R;
import com.moon.biz.home.MainActivity;
import com.moon.common.utils.JsonInfoUtils;
import com.moon.common.utils.UrlUtils;
import com.moon.db.model.UserInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/25
 */
public class RegisterMoreInfoAct extends Activity {
    @ViewInject(R.id.img_reg_headimg)
    private ImageView img_reg_headimg;

    @ViewInject(R.id.textView_reg_phoneNum)
    private TextView textView_reg_phoneNum;

    private int RequestCode_NickName = 222;
    private int RequestCode_Sex = 223;
    private String name;
    private String pwd;
    private String check;
    private String nickname = "";
    private String sex = "M";
    private AppCtx appCtx;

    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;

    private byte[] imgByte;
    private String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);

        ViewUtils.inject(this);

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        appCtx = AppCtx.getInstance();

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        pwd = bundle.getString("pwd");
        check = bundle.getString("check");
    }

    /**
     * 控件点击事件
     *
     * @param v
     */
    public void clickButton(View v) {
        Intent intent = new Intent();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (v.getId()) {
            /**
             * 选择上传图片
             */
            case R.id.img_reg_headimg:
                builder.setItems(new String[]{"From Album", "From Camera"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                gallery();
                                break;
                            case 1:
                                camera();
                                break;
                        }
                    }
                });
                builder.show();
                break;
            /**
             * 输入手机号
             */
            case R.id.textView_reg_phoneNum:
                LinearLayout view = new LinearLayout(this);
                final EditText editText_phonenum = new EditText(this);
                editText_phonenum.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                editText_phonenum.setHint("请输入手机号");
                editText_phonenum.setInputType(InputType.TYPE_CLASS_NUMBER);
                view.addView(editText_phonenum);
                builder.setView(view);
                builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textView_reg_phoneNum.setText(editText_phonenum.getText().toString());
                    }
                });
                builder.show();
                break;
            /**
             * 回退按钮
             */
            case R.id.img_regm_back:
                this.finish();
                break;
            /**
             * "昵称"
             */
            case R.id.btn_reg_nickname:
                intent.setClass(this, NickNameAct.class);
                startActivityForResult(intent, RequestCode_NickName);
                break;
            /**
             * "性别"
             */
            case R.id.btn_reg_sex:
                intent.setClass(this, SetSexAct.class);
                startActivityForResult(intent, RequestCode_Sex);
                break;
            /**
             * "发送"
             */
            case R.id.textView_reg_send:
                StringRequest request = new StringRequest(Request.Method.POST, UrlUtils.REGISTER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            Map<String, String> regInfo = JsonInfoUtils.getRegInfo(response);
                            checkInfo(regInfo);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("1111-reg", "error...");
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("username", name);
                        map.put("password", pwd);
                        map.put("sequence", AppCtx.getInstance().getSequence());
                        map.put("verify_code", "");
                        map.put("nickname", nickname);
                        map.put("sex", sex);
                        return map;
                    }
                };
                AppCtx.getInstance().getRequestQueue().add(request);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode_NickName && resultCode == 1) {
            nickname = data.getStringExtra("nickname");
        } else if (requestCode == RequestCode_Sex && resultCode == 2) {
            sex = data.getStringExtra("sex");
        }

        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                path = data.getStringExtra("dat");
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (hasSdcard()) {
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                imgByte = Bitmap2Bytes(bitmap);
                this.img_reg_headimg.setImageBitmap(bitmap);
            }
            try {
                // 将临时文件删除
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 检查网络返回数据
     *
     * @param regInfo
     */
    private void checkInfo(Map<String, String> regInfo) {
        if (regInfo != null) {
            if (regInfo.get("code").equals("0")) {
                Toast.makeText(this, regInfo.get("message"), Toast.LENGTH_SHORT).show();
            } else {
                upLoadHeaderImg(regInfo.get("token"));
                UserInfo user = new UserInfo();
                user.setUsername(regInfo.get("username"));
                user.setPassword(regInfo.get("password"));
                user.setSex(regInfo.get("sex"));
                user.setCreatetime(regInfo.get("create_time"));
                user.setNickname(regInfo.get("nickname"));
                try {
                    appCtx.getDb().save(user);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                appCtx.setToken(regInfo.get("token"));
                appCtx.setUsername(regInfo.get("username"));
                Toast.makeText(this, "注册成功,已登录", Toast.LENGTH_SHORT).show();

                // TODO 保存到SharedPreferences
                SharedPreferences prefs = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("userName", regInfo.get("username"));
                editor.putString("password", regInfo.get("password"));
                editor.commit();

                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }
        }
    }

    private void upLoadHeaderImg(final String token) {
        StringRequest request = new StringRequest(Request.Method.POST, UrlUtils.UPLOAD_HEADERIMG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("1111-upimg", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("1111-upimg", "error.....");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("token", token);
                return map;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return imgByte;
            }
        };
        AppCtx.getInstance().getRequestQueue().add(request);

    }


    /*
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
     * 从相机获取
     */
    public void camera() {
        // 激活相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
            // 从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    /*
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");

//        String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
//        cursor.moveToFirst();
//
//        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//        path = cursor.getString(columnIndex);

        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /*
     * 判断sdcard是否被挂载
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}