package com.moon.biz.home.settings;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.biz.R;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/27
 */
public class SuggestAct extends Activity {
    @ViewInject(R.id.txt_set_suggest)
    private EditText txt_set_suggest;
    @ViewInject(R.id.txt_set_txtNum)
    private TextView txt_set_txtNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        ViewUtils.inject(this);

        addListener();
    }

    private void addListener() {
        txt_set_suggest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                txt_set_txtNum.setText(111 - txt_set_suggest.getText().length() + "");
            }
        });
    }

    public void clickBack(View v) {
        this.finish();
    }

    public void clickToSend(View v) {
        Toast.makeText(this, "亲,您的建议我们已获得，我们会认真改正问题的", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}