package com.osmile.russiablockgame;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class RussiaBlockGame {
    private Block[][] map=new Block[26][14];//左右下各多两格  上多4格     /*Block类型的二维数组*/
    private BlocksMoveing blocksMoveing;
    private boolean isLostnow;
    private int score;
    private int nexttype;
    private int nextstate;
    private int nextcolor;
    public RussiaBlockGame(){
        resetGame();
    }/*重新开始游戏*/

    public void resetGame(){
        score=0;
        isLostnow=false; /*方块是否已经堆满，否，为未堆满，游戏继续*/

        for(int i=0;i<map.length;i++)
        {
            for(int p=0;p<map[i].length;p++)
            {
                map[i][p]=new Block(Color.argb(255,127,143,166),0); //设置空白方块 rgba(127, 143, 166,1.0)
            }
        }
        for(int i=0;i<map.length;i++)
        {
            map[i][0].setBlockType(-1); //设置第一列为墙方块
            map[i][1].setBlockType(-1);//设置第二列为墙方块
            map[i][12].setBlockType(-1);/*设置第13列为墙方块*/
            map[i][13].setBlockType(-1);/*设置第14列为墙方块*/
        }
        for(int i=0;i<map[0].length;i++)
        {
            map[24][i].setBlockType(-2);/*设置第25横行，为地面方块*/
            map[25][i].setBlockType(-2);//设置第26横行，为地面方块
        }
        blocksMoveing=new BlocksMoveing();//暂时没有随机数设置下落方块属性
        nexttype=suijishu(0, 6);/*设置随机的下落方块类型，代号0~6共七种方块类型*/

        int statelength=blocksMoveing.getStateLengthFromType(nexttype);

        nextstate=suijishu(0, statelength-1);/*方块的状态初始随机*/
        nextcolor=Color.argb(255,suijishu(150,255),suijishu(150,255),suijishu(150,255));
        getNewOneMovingBlocks();

    }
    public void playerMove(int moveType,MusicPl mp)//0.直接下落 1.↑ 2.↓ 3.← 4.→
    {
        if(isLostnow)
        {
            return;/*判定游戏方块是否下落，false游戏继续，true游戏结束*/
        }
        switch(moveType)
        {
            //0.直接下落 1.↑ 2.↓ 3.← 4.→
            case 0:
                while(!isBlockTouch())/*检测当前是否有重合的方块*/
                {
                    blocksMoveing.top++;/*未碰撞，一直下落，竖列一直增加*/
                }
                blocksMoveing.top--;/*跳出while循环，说明已经触碰到方块，此时的top要做减一操作*/
                mp.weatherPlay=fixedMovingBlocks();
                getNewOneMovingBlocks();/*得到下一方块，并开始下落*/

                if(isLost())/*正在下落的方块是地图上的方块*/
                {
                    isLostnow=true;
                }
                //判断胜负，方块堆满

                break;
            case 1:
                //方块旋转
                /*若当前位置不能旋转，找定位点，左右移动，依次试六次判断是否有重合*/
                if(isBlockTouch2())//isBlockTouch2()检测旋转位置是否有重合的方块
                {
                    if(blocksMoveing.left>map[0].length/2)
                    {
                        //定位点在右边
                        blocksMoveing.left--;
                        if(isBlockTouch2())
                        {
                            blocksMoveing.left--;
                            if(isBlockTouch2())
                            {
                                blocksMoveing.left++;
                                blocksMoveing.left++;
                            }else {
                                blocksMoveing.nextState();
                            }
                        }else {
                            blocksMoveing.nextState();
                        }

                    }else {
                        //定位点在左边
                        blocksMoveing.left++;
                        if(isBlockTouch2())
                        {
                            blocksMoveing.left--;
                        }else {
                            blocksMoveing.nextState();
                        }
                    }
                }else{
                    blocksMoveing.nextState();
                }
                break;
            case 2:
                //下落一格

                blocksMoveing.top++;
                if(isBlockTouch())
                {
                    //碰撞了
                    blocksMoveing.top--;
                }
                break;
            case 3:
                /*左移*/
                blocksMoveing.left--;
                if(isBlockTouch())
                {
                    //碰撞了
                    blocksMoveing.left++;
                }
                break;

            case 4:
                /*右移*/
                blocksMoveing.left++;
                if(isBlockTouch())
                {
                    //碰撞了
                    blocksMoveing.left--;
                }
                break;
            default:
                break;
        }
    }
    public boolean isLost()/*判断是否是地图上的方块，是为：地图上的方块。| 否为：不是地图上的方块*/
    {
        for(int i=0;i<4;i++)
        {
            for(int p=0;p<map[i].length;p++)
            {
                if(map[i][p].BlockType==-3)/*判断是否是地图上的方块*/
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean dropAStep(MusicPl mp)
    {
        //下落一格
        if(isLostnow)/*判断是否堆满*/
        {
            return true;
        }

        blocksMoveing.top++;

        if(isBlockTouch())
        {
            //碰撞了 返回并把移动方块固定成地图方块
            blocksMoveing.top--;
            mp.weatherPlay=fixedMovingBlocks();
            getNewOneMovingBlocks();
            //判断胜负
            return(isLost());
        }
        return false;/*？*/
    }

    public Block[][] getNextType()
    {
        return blocksMoveing.getNextStateByAll(nexttype, nextstate,nextcolor);
    }

    private void getNewOneMovingBlocks()
    {
        int type=nexttype;
        int state=nextstate;
        int color=nextcolor;
        nexttype=suijishu(0, 6);
        int statelength=blocksMoveing.getStateLengthFromType(nexttype);
        nextstate=suijishu(0, statelength-1);
        nextcolor=Color.argb(255,suijishu(150,255),suijishu(150,255),suijishu(150,255));
        blocksMoveing=new BlocksMoveing(type,state,5,0,color);
        blocksMoveing.top++;
        if(isBlockTouch())
        {
            //碰撞了
            blocksMoveing.top--;
        }
    }

    private boolean checkFullblock(int a)
    {
        boolean fullOf;//是否已满一行
        boolean fullreal=false;//是否真的满

        //检查一行一行的方块满没
        for(int i=20;i>=0;i--)
        {
            fullOf=true;
            for(int p=0;p<10;p++)
            {
                if(map[i+4][p+2].BlockType!=-3)/*top横坐标的取值范围是4~24，所以i+4（i为0~20）| left竖坐标取值范围2~12，所以p+2（p为0~10） */
                {
                    /*开始从top24，left2~12。即最底层开始检查，-3代表地图上的方块*/
                    fullOf=false;
                }
            }

            /*每一行都判断一遍，消去满行的方块*/
            if(fullOf)
            {
                for(int p=i+4;p>4;p--)
                {
                    for(int t=0;t<10;t++)
                    {
                        map[p][t+2]=map[p-1][t+2];
                    }
                }
                for(int p=0;p<10;p++)
                {
                    map[4][p+2]=new Block(Color.argb(255,127,143,166),0);//rgba(127, 143, 166,1.0)
                }
                score+=20*a;//四行200分  三行120分  两行60分  一行 20分
                fullreal=true;
                checkFullblock(a+1);
            }
        }
        return fullreal;
    }

    public int getscore()
    {
        return score;
    }


    /*适合移动的方块*/
    private boolean fixedMovingBlocks()
    {
        Block[][] thisTimeState=blocksMoveing.getThisStateMap();
        for(int i=0;i<thisTimeState.length;i++)
        {
            for(int p=0;p<thisTimeState[i].length;p++)
            {
                if(thisTimeState[i][p].BlockType==1)
                {
                    map[blocksMoveing.top+i][blocksMoveing.left+p].color=thisTimeState[i][p].color;
                    map[blocksMoveing.top+i][blocksMoveing.left+p].BlockType=-3;
                }

            }
        }
        return checkFullblock(1);
    }


    public Block[][] getMapDisplay()
    {
        //获取显示数据
        Block[][] thisTimeState=blocksMoveing.getThisStateMap();
        Block[][] mapp=new Block[20][10];
        for(int i=0;i<20;i++)
        {
            for(int p=0;p<10;p++)
            {
                mapp[i][p]=new Block(map[i+4][p+2].color, map[i+4][p+2].BlockType);
            }
        }

        for(int i=0;i<thisTimeState.length;i++)
        {
            for(int p=0;p<thisTimeState[i].length;p++)
            {

                /*定义的方块与展示的方块坐标的转化（其中有判断的过程）*/
                if(thisTimeState[i][p].BlockType==1 && i+blocksMoveing.top-4>=0 && p+blocksMoveing.left-2>=0)
                {
                    mapp[i+blocksMoveing.top-4][p+blocksMoveing.left-2].color=thisTimeState[i][p].color;
                    mapp[i+blocksMoveing.top-4][p+blocksMoveing.left-2].BlockType=thisTimeState[i][p].BlockType;
                }

            }
        }

        return mapp;
    }

    /*true为：没有越界且是正常下落的方块*/
    private boolean blockTouch(Block[][] thisTimeState)/*Block[][] thisTimeState传值参数*/
        /*blocksMoveing.getThisStateMap()*/ /*blocksMoveing.getNextStateMap()*//*供isBlockTouch()，isBlockTouch2()调用*/
    {        /*遍历Block【】【】二维数组*/

        for(int i=0;i<thisTimeState.length;i++)
        {
            for(int p=0;p<thisTimeState[i].length;p++)
            {
                if(thisTimeState[i][p].BlockType==1 && map[blocksMoveing.top+i][blocksMoveing.left+p].BlockType<0)
                {
                    /*map[blocksMoveing.top+i][blocksMoveing.left+p].BlockType<0判断方块是否越界*/
                    /*thisTimeState[i][p].BlockType==1判断当前的方块状态是否为移动下落的方块*/
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBlockTouch() {
        //检测当前位置是否有重合的方块
        return blockTouch(blocksMoveing.getThisStateMap());/*getThisStateMap()：得到当前位置的方块*/
    }

    private boolean isBlockTouch2() {
        //检测旋转位置是否有重合的方块
        return blockTouch(blocksMoveing.getNextStateMap());/*getNextStateMap()：得到下次状态转换的方块*/
    }

    public static int suijishu(int min ,int max) {
        // 产生随机数。范围为min~max
        return (int) (Math.random() * (max-min+1))+min;
    }
}
