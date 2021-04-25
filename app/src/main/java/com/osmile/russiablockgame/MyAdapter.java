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
//widget为窗口小组件
import java.util.List;

/*用到了绝对布局，和线性布局*/

public class MyAdapter extends BaseAdapter {

    private List<ItemBean> mList;
    private LayoutInflater mInflater;/*LayoutInflater? 具体啥意思不知道*/
    private int blockheight;    /*方块高度*/

    /*Layout一个用于加载布局的系统服务，就是实例化与Layout XML文件对应的View对象，不能直接使用，
     需要通过getLayoutInflater( )方法或getSystemService( )方法来获得与当前Context绑定的 LayoutInflater实例!*/

    /*①获取LayoutInflater实例的三种方法：

       LayoutInflater inflater1 = LayoutInflater.from(this);
       LayoutInflater inflater2 = getLayoutInflater();
       LayoutInflater inflater3 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);  */


    // 通过构造方法，将数据源与数据适配器进行关联；
    public MyAdapter(Context context, List<ItemBean> list,int blockheight) {
        this.blockheight=blockheight;
        mList = list;
        mInflater = LayoutInflater.from(context);/*获取LayoutInflater实例（xml布局实例化）*/
    }


    // 返回ListView需要显示的数据数量/*导入的Java.util.List包*/
    @Override
    public int getCount() {
        return mList.size();
    }
     /*返回List<ItemBean> list中的数据条目*/
     //这里的getCount()方法是程序在加载显示到UI上时就要先读取的，这里获得的值决定了List<ItemBean> list中显示多少行

    @Override
    public Object getItem(int i) {
        return mList.get(i);// 返回List<ItemBean> list指定索引对应的数据项
    }
    /*根据一个索引（位置）获得该位置的对象*/

    @Override
    public long getItemId(int i) {
        return i;
    }// 对应的索引项，即索引ID
    /* 获取条目的id*/


    // 返回每一项的显示内容，即，获取该条目要显示的界面，这里对getView进行了优化
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
          /*在getView方法中，Adapter先从xml中用inflate方法创建view对象，然后在这个view找到每一个控件
            其中getView方法中的三个参数，position（i）是指现在是第几个条目；convertView（view）是旧视图，就是绘制好了的视图；
            parent（viewGroup）是父级视图，也就是ListView之类的。*/

        ViewHolder viewHolder;

        /*这里的findViewById操作是一个树查找过程，也是一个耗时的操作，所以这里也需要优化，
        就是使用viewHolder，把每一个控件都放在Holder中，当第一次创建convertView对象时，把这些控件找出来。
        然后用convertView的setTag将viewHolder设置到Tag中，以便系统第二次绘制ListView时从Tag中取出。
        当第二次重用convertView时，只需从convertView中getTag取出来就可以*/

        if (view == null) {
            view = mInflater.inflate(R.layout.gv_item, null);

            viewHolder = new ViewHolder();

            viewHolder.imageView = (ImageView) view.findViewById(R.id.item_image);

            AbsListView.LayoutParams param=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,blockheight);
              /*？？？*/

            view.setLayoutParams(param);/*设置显示界面属性*/

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
        public ImageView imageView;/*对BaseAdapter的优化使用->convertView优化，将imageView加入到viewHolder*/
    }


}
