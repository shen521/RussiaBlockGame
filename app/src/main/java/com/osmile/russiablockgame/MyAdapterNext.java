package com.osmile.russiablockgame;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

public class MyAdapterNext extends BaseAdapter {

    private List<ItemBean> mList;
    private LayoutInflater mInflater;
    private int blockheight;

    // 通过构造方法，将数据源与数据适配器进行关联；
    public MyAdapterNext(Context context, List<ItemBean> list,int blockheight) {
        this.blockheight=blockheight;
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    // 返回ListView需要显示的数据数量
    @Override
    public int getCount() {
        return mList.size();
    }

    // 返回指定索引对应的数据项
    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    // 对应的索引项
    @Override
    public long getItemId(int i) {
        return i;
    }
    // 返回每一项的显示内容
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.gv_item, null);
            viewHolder = new ViewHolder();

            viewHolder.imageView = (ImageView) view.findViewById(R.id.item_image);
            AbsListView.LayoutParams param=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,blockheight);
            view.setLayoutParams(param);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ItemBean bean = mList.get(i);
        viewHolder.imageView.setBackgroundColor(bean.color);
        viewHolder.imageView.setImageResource(R.mipmap.none);
        return view;
    }
    class ViewHolder {
        public ImageView imageView;
    }


}
