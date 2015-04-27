package com.moon.biz.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.biz.R;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/25
 */
public class SetSexAct extends Activity {
    @ViewInject(R.id.radioGroup_regm_sex)
    private RadioGroup radioGroup_regm_sex;
    @ViewInject(R.id.textView_regm_sexsend)
    private TextView textView_regm_sexsend;

    private String sex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setsex);

        ViewUtils.inject(this);

        addListener();
    }

    private void addListener(){
        radioGroup_regm_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getId() == R.id.radioGroup_regm_sex){
                    switch(checkedId){
                        case R.id.radioButton_regm_m:
                            sex = "M";
                            break;
                        case R.id.radioButton_regm_w:
                            sex = "W";
                            break;
                        case R.id.radioButton_regm_s:
                            sex = "";
                            break;
                    }
                }
            }
        });

        textView_regm_sexsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("sex",sex);
                setResult(2,intent);
                SetSexAct.this.finish();
            }
        });
    }
}