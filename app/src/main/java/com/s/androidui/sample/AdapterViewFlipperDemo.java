package com.s.androidui.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterViewFlipper;

import com.s.androidui.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterViewFlipperDemo extends AppCompatActivity {

    @BindView(R.id.s_android_ui_adapter_view_flipper)
    AdapterViewFlipper adapterViewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_view_flipper_demo);
        ButterKnife.bind(this);

        // 这里的动画数据不好控制
        adapterViewFlipper.setInAnimation(this, R.animator.animator_in);
        adapterViewFlipper.setOutAnimation(this, R.animator.animator_out);

        adapterViewFlipper.setFlipInterval(3000);
        adapterViewFlipper.setAutoStart(true);

        adapterViewFlipper.setAdapter(new FlipperAdapter(this));
    }
}
