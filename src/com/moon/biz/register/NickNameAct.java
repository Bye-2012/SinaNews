package com.moon.biz.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.biz.R;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/25
 */
public class NickNameAct extends Activity {

    @ViewInject(R.id.editText_regm_nickname)
    private EditText editText_regm_nickname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        ViewUtils.inject(this);
    }

    public void clickButton(View v) {
        if (v.getId() == R.id.textView_regm_nicksend) {
            Intent intent = new Intent();
            intent.putExtra("nickname", editText_regm_nickname.getText().toString());
            setResult(1, intent);
            this.finish();
        }
    }
}