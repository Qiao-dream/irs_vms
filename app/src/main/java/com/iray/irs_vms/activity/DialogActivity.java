package com.iray.irs_vms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iray.irs_vms.R;

public class DialogActivity extends BaseActivity {
    private TextView tvAlarm;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Log.e("DialogActivity","DialogActivity on Created_____________________");
        tvAlarm=findViewById(R.id.tv_alarm_value);
        confirm=findViewById(R.id.btn_alarm);
        confirm.setOnClickListener(mOnClickListener);
        Intent intent =getIntent();
        tvAlarm.setText(intent.getStringExtra("displayDetail"));

    }
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_alarm:
                    finish();
                default:
                    break;
            }
        }
    };
}
