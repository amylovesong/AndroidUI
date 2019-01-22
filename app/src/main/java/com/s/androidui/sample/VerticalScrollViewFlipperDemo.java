package com.s.androidui.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.s.androidui.R;
import com.s.androidui.VerticalScrollViewFlipper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerticalScrollViewFlipperDemo extends AppCompatActivity {

    @BindView(R.id.s_vertical_scroll_view_flipper)
    VerticalScrollViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_scroll_view_flipper_demo);
        ButterKnife.bind(this);

        viewFlipper.setAdapter(new FlipperAdapter(this));
    }
}
