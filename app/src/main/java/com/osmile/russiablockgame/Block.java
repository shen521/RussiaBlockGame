package com.osmile.russiablockgame;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class Block {
    int color;//方块颜色

    int BlockType;//0空白方块 1属于移动的方块  2属于预测下落位置的方块    -1表示墙 -2表示地面 -3属于地图上的方块

    public Block() {//定义第一个构造函数
        color=Color.argb(255,255,255,255);/*红绿蓝均不透明*/
        //每个数值范围0-255，alpha 表示透明度，0表示透明，255 表示不透明。
        //分别代表透明度（alpha）,红色（red）,绿色（green）,蓝色（blue）四个颜色值ARGB，
        BlockType=0;//0表示空白方块
//		BID=0;
    }
    public Block(int color,int BlockType) {//定义第二个构造函数，传入两个参数，方块颜色，方块类型
        this.color=color;
        this.BlockType=BlockType;
    }
    public void setBlockType(int Type) {
        BlockType=Type;
    }//设置方块是那种类型的方法
    public void setBlockColor(int color) {
        this.color=color;
    }//设置方块是那种颜色的方法
}
