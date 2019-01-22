package com.s.androidui.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.s.androidui.R;

import java.util.ArrayList;
import java.util.List;

public class FlipperAdapter extends BaseAdapter {

    private List<String> data = new ArrayList<>();
    private LayoutInflater inflater;

    FlipperAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);

        this.data.add("测试数据11111111111");
        this.data.add("多条可滚动显示（同屏上限2条，总上限10条，按M后台指定的优先级截取‘生效中’运营位展示）");
        this.data.add("运营位M后台配置：运营消息title+落地页链接（可选）+展示优先级");
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
            convertView = inflater.inflate(R.layout.item_adapter_view_flipper, parent, false);
        }
        bindData(convertView, position);

        return convertView;
    }

    private void bindData(View convertView, int position) {
        TextView name = convertView.findViewById(R.id.s_item_view_flipper_text);
        name.setText(data.get(position));
    }
}
