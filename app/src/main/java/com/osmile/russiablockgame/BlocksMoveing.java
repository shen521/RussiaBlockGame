package com.osmile.russiablockgame;


import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class BlocksMoveing extends Blocks{
    private int blsType;//第一维标记是哪种方块类型
    private int blsState;//第二维标记 哪种方块状态
    public int left;//定位坐标，x坐标，横坐标→
    public int top;//定位坐标，y坐标，纵坐标↓
    int color;     //方块颜色
    Block[][] statemap;//？存储方块的数据二维数组
    //方块移动类构造函数
    public BlocksMoveing()
    {
        this(0,0,5,0, Color.argb(255,251, 197, 49));
    }
    /*刚开始横坐标为5*/

    public BlocksMoveing(int blsType,int blsState,int left,int top,int color)
    {
        this.blsType=blsType%7;//只有7种方块
        this.blsState=blsState;

        this.left=left;/*横行*/
        this.top=top;/*竖列*/
        this.color=color;
    }

    /*初始化准备get的下落方块状态图，有两个值参，1.方块类型，2.方块形态*/
    private void putInStateMap(int blsType,int blsState)
    {
        statemap=new Block[blocksint[blsType][blsState].length][blocksint[blsType][blsState][0].length];//Block[横行][竖列]
        /*读取第三，第四维的数据，其数据存储在statemap中*//*每一个数据项代表一个方块？*/

        for(int i=0;i<blocksint[blsType][blsState].length;i++)
        {
            for(int p=0;p<blocksint[blsType][blsState][i].length;p++)
            {
                //初始化准备get的下落方块状态图
                statemap[i][p]=new Block(color,blocksint[blsType][blsState][i][p]);
            }
        }

    }

    /*得到此时下落方块的形状*/
    public Block[][] getThisStateMap()
    {
        putInStateMap(blsType%7, blsState);/*调用私有方法putInStateMap得到此时下落方块的形状数据*/
        return statemap;
    }

    /*同类型方块的下一个变化形态*/
    public Block[][] getNextStateMap()   /*Java类 类型 二维数组的定义（已解决）*/
    {
        putInStateMap(blsType, (blsState+1)%blocksint[blsType].length);/*同类型方块的下一个变化形态*/
        return statemap;
    }


    public Block[][] getNextStateByAll(int blsType,int state,int color)
    {
        putInStateMap(blsType, (state)%blocksint[blsType].length);//得到方块类型，的状态，% 取模运算
        for(int i=0;i<statemap.length;i++)
        {
            for(int p=0;p<statemap[i].length;p++)
            {
                if(statemap[i][p].BlockType==1)
//                int BlockType;//0空白方块 1属于移动的方块  2属于预测下落位置的方块    -1表示墙 -2表示地面 -3属于地图上的方块
                {
                    statemap[i][p].color=color;/*给方块赋值颜色*/
                }
            }
        }
        return statemap;
    }

    //从方块状态中变换另一个形态
    public void nextState()
    {
        blsState=(blsState+1)%blocksint[blsType].length;
    }

    //得到方块类型的形态数量
    public int getStateLengthFromType(int type)
    {
        return blocksint[type].length;//得到方块类型的形态数量
    }
}