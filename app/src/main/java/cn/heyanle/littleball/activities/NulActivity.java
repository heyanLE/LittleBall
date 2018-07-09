package cn.heyanle.littleball.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cn.heyanle.littleball.R;

public class NulActivity extends AppCompatActivity {

    public static final String NUL_KEY = "LITTLE_BALL_NUL_LOG_KEY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nul_layout);

        Intent intent = getIntent();

        String nul = "";

        if(intent != null){
            try {
                nul = intent.getStringExtra(NUL_KEY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ((TextView) findViewById(R.id.activity_nul_tv_nul)).setText(nul);
        String androidText = "安卓版本：" + android.os.Build.VERSION.RELEASE;
        ((TextView) findViewById(R.id.activity_nul_tv_android)).setText(androidText);
        String modelText = "手机型号:" + android.os.Build.MODEL;
        ((TextView) findViewById(R.id.activity_nul_tv_model)).setText(modelText);

    }
}
