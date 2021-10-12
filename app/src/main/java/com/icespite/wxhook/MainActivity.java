package com.icespite.wxhook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView);
        textView.setText("Hook 微信分享时校验");


        Switch aSwitch = findViewById(R.id.switch_toast);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HookConfig.TOAST_SWITCH = true;
//                    LinearLayout linearLayout = findViewById(R.id.logInfoLinearLayout);
//                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    HookConfig.TOAST_SWITCH = false;
                    findViewById(R.id.logInfoLinearLayout).setVisibility(View.GONE);
                    //todo:应该保存到文件中，读取文件来显示log
                }
            }
        });
    }
}