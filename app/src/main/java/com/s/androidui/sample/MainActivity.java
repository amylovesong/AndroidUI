package com.s.androidui.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.s.androidui.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.s_main_list_view)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        List<SampleItem> samples = new ArrayList<>();
        samples.add(new SampleItem("AdapterViewFlipper", "", AdapterViewFlipperDemo.class));

        SampleAdapter adapter = new SampleAdapter(this, samples);
        mListView.setAdapter(adapter);
    }

    static class SampleItem {
        String name;
        String desc;
        Class<? extends AppCompatActivity> targetActivity;

        SampleItem(String name, String desc, Class<? extends AppCompatActivity> targetActivity) {
            this.name = name;
            this.desc = desc;
            this.targetActivity = targetActivity;
        }
    }

    static class SampleAdapter extends BaseAdapter implements View.OnClickListener {
        List<SampleItem> data;
        LayoutInflater inflater;
        Context context;

        SampleAdapter(Context context, List<SampleItem> data) {
            this.data = new ArrayList<>(data);
            this.inflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_main_sample, parent, false);
            }
            bindData(convertView, position);

            return convertView;
        }

        private void bindData(View convertView, int position) {
            TextView name = convertView.findViewById(R.id.s_item_sample_name);
            TextView desc = convertView.findViewById(R.id.s_item_sample_desc);

            SampleItem sampleItem = (SampleItem) getItem(position);
            name.setText(sampleItem.name);
            desc.setText(sampleItem.desc);
            desc.setVisibility(TextUtils.isEmpty(desc.getText()) ? View.GONE : View.VISIBLE);

            convertView.setTag(sampleItem);
            convertView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SampleItem sampleItem = (SampleItem) v.getTag();
            if (sampleItem.targetActivity != null) {
                context.startActivity(new Intent(context, sampleItem.targetActivity));
            }
        }
    }
}
