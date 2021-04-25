package com.osmile.russiablockgame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

      /*直接在MainActivity里进行消息传递，注册OnClickListener事件*/
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private GridView gv_game,gv_next;
    private boolean gameStop=false;
    private TextView tv_score;
    private List<ItemBean> itemBeenList = new ArrayList<>();
    private List<ItemBean> itemBeenListnext = new ArrayList<>();
    private MyAdapter myAdapter,myAdapternext;
    private Block[][] map;
    private Handler handler;
    private int blocklength;
    private int score=0;
    private ImageView mimageview;
    private int dividerPad=2;
    private RussiaBlockGame russiaBlockGame=new RussiaBlockGame();//新建游戏对象
    private Block[][] nextBlock;
    private boolean isGameLost;
    private int time=1000;
    private SoundPool soundPool;//音频通知声音播放器
    private SoundPool soundPool2;//播放器2
    private int soundID;//音频资源ID
    private int soundID_left_right_downastep;
    private int soundID_down;
    private int soundID_delete;
    private int soundID_Loss;
    private MusicPl musicPl=new MusicPl();
    Point p;

    private void hideActionBar() {
        /**
         * hide action bar
         */
        // Hide UI
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void setFullScreen() {
        /**
         * set the activity display in full screen
         */
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @SuppressLint("NewApi")
    private void initSound() {
        soundPool = new SoundPool.Builder().build();
        soundPool2 = new SoundPool.Builder().build();
        soundID = soundPool.load(this,R.raw.change , 1);
        soundID_left_right_downastep=soundPool.load(this,R.raw.move2,1);
        soundID_down = soundPool.load(this,R.raw.down , 1);
        soundID_Loss=soundPool.load(this,R.raw.gameover , 1);
        soundID_delete=soundPool2.load(this,R.raw.delete , 1);

    }//实例化soundPool和soundID  R.raw.qipao为音频资源位置
    private void playSound(int i) {
        switch(i)
        {
            case 0:
                //change
                soundPool.play(
                        soundID,
                        0.1f,      //左耳道音量【0~1】
                        0.5f,      //右耳道音量【0~1】
                        0,         //播放优先级【0表示最低优先级】
                        0,         //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                        2          //播放速度【1是正常，范围从0~2】
                );
                break;
            case 1:
                //move
                soundPool.play(
                        soundID_left_right_downastep,
                        1f,      //左耳道音量【0~1】
                        1f,      //右耳道音量【0~1】
                        0,         //播放优先级【0表示最低优先级】
                        0,         //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                        2          //播放速度【1是正常，范围从0~2】
                );
                break;
            case 2:
                //down
                soundPool.play(
                        soundID_down,
                        0.1f,      //左耳道音量【0~1】
                        0.5f,      //右耳道音量【0~1】
                        0,         //播放优先级【0表示最低优先级】
                        0,         //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                        1          //播放速度【1是正常，范围从0~2】
                );
                break;
            case 3:
                //loss
                soundPool.play(
                        soundID_Loss,
                        0.1f,      //左耳道音量【0~1】
                        0.5f,      //右耳道音量【0~1】
                        0,         //播放优先级【0表示最低优先级】
                        0,         //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                        1          //播放速度【1是正常，范围从0~2】
                );
                break;
            case 4:
                //delete
                soundPool2.play(
                        soundID_delete,
                        0.1f,      //左耳道音量【0~1】
                        0.5f,      //右耳道音量【0~1】
                        0,         //播放优先级【0表示最低优先级】
                        0,         //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                        1          //播放速度【1是正常，范围从0~2】
                );
                break;

            default:
                break;

        }

    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideActionBar();
        setFullScreen();
        p=new Point();/*点集类，并赋值给p*/
        WindowManager wm=(WindowManager) getApplicationContext().getSystemService(getApplicationContext().WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(p);
        /*Toast.makeText(this,String.valueOf(p.x)+" "+String.valueOf(p.y),Toast.LENGTH_LONG).show();*/
        /*相对坐标与绝对坐标转换*/
        p.y=((p.y*5/6)-(dividerPad*21))/20;
        p.x=(((p.x-20)*2/3)-(dividerPad*11))/10;
        blocklength=p.x<p.y?p.x:p.y;
        tv_score=findViewById(R.id.tv_score);
        gv_game=findViewById(R.id.gv_game);
        gv_next=findViewById(R.id.gv_next);
        gv_game.setBackgroundColor(Color.BLACK);
        gv_next.setBackgroundColor(Color.BLACK);
        gv_game.setHorizontalSpacing(dividerPad);
        gv_next.setHorizontalSpacing(dividerPad);
        gv_game.setVerticalSpacing(dividerPad);
        gv_next.setVerticalSpacing(dividerPad);
        gv_game.setStretchMode(GridView.NO_STRETCH);
        gv_next.setStretchMode(GridView.NO_STRETCH);
        gv_game.setColumnWidth(blocklength);
        gv_next.setColumnWidth(blocklength);
        gv_game.setPadding(dividerPad,dividerPad,dividerPad,dividerPad);
        gv_next.setPadding(dividerPad*10,dividerPad,dividerPad,dividerPad);

        for (int i = 0; i < 200; i++) {
            /*加入两百个方块*/
            itemBeenList.add(new ItemBean(Color.argb(255,255,255,255)));
        }

        for (int i=0;i<16;i++)
        {
            itemBeenListnext.add(new ItemBean(Color.argb(255,255,255,255)));
        }
        myAdapter=new MyAdapter(this,itemBeenList,blocklength);
        myAdapternext=new MyAdapter(this,itemBeenListnext,blocklength);
        gv_game.setAdapter(myAdapter);
        gv_next.setAdapter(myAdapternext);

        handler=new Handler(){
            @SuppressLint("SetTextI18n")
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        playSound(3);
                        Toast.makeText(MainActivity.this,"游戏结束，确定重开一局",Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("游戏结束：");
                        builder.setMessage("您本局的分数为："+score+",点击确定重开一局。");
                        SharedPreferences.Editor editor = getSharedPreferences("data",
                                MODE_PRIVATE).edit();
                        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                        int scoreNew = pref.getInt("score", 0);
                        if (scoreNew<score){
                            editor.putInt("score", score);
                        }
                        editor.commit();

                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                gameStop=false;
                            }
                        });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();
                        break;
                    case 2:
                        tv_score.setText("分数："+score);
                        myAdapternext.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }

        };
        findViewById(R.id.ibtn_left).setOnClickListener(this);
        findViewById(R.id.ibtn_right).setOnClickListener(this);
        findViewById(R.id.ibtn_change).setOnClickListener(this);
        findViewById(R.id.ibtn_down).setOnClickListener(this);
        findViewById(R.id.ibtn_downAstep).setOnClickListener(this);
        initSound();
        new LoopThread().start();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ibtn_left:
                playSound(1);
                russiaBlockGame.playerMove(3,musicPl);
                map=russiaBlockGame.getMapDisplay();
                freshWindow();
                break;
            case R.id.ibtn_right:
                playSound(1);
                russiaBlockGame.playerMove(4,musicPl);
                map=russiaBlockGame.getMapDisplay();
                freshWindow();
                break;
            case R.id.ibtn_change:
                playSound(0);
                russiaBlockGame.playerMove(1,musicPl);
                map=russiaBlockGame.getMapDisplay();
                freshWindow();
                break;
            case R.id.ibtn_down:
                playSound(2);
                russiaBlockGame.playerMove(0,musicPl);
                if(musicPl.weatherPlay)
                {
                    playSound(4);
                    musicPl.weatherPlay=false;
                }
                map=russiaBlockGame.getMapDisplay();
                freshWindow();
                break;
            case R.id.ibtn_downAstep:
                playSound(1);
                russiaBlockGame.playerMove(2,musicPl);
                map=russiaBlockGame.getMapDisplay();
                freshWindow();
                break;
            default:
                break;
        }
    }
    public void freshWindow()
    {
        for(int i=0;i<map.length;i++)
        {
            for(int p=0;p<map[i].length;p++)
            {
                itemBeenList.get(i*10+p).color=map[i][p].color;
                //jlbs[i][p].setBackground(map[i][p].color);
            }
        }
        //myAdapter.notifyDataSetChanged();
        Message msg=new Message();
        msg.what=0;
        handler.sendMessage(msg);
    }
    private class LoopThread extends Thread{

        @Override
        public void run() {

            while (true) {
                try {
                    time=1000-(russiaBlockGame.getscore()/5);//这样时间就越来越快了
                    if(time<=0)time=1;
                    Thread.sleep(time);

                    isGameLost=russiaBlockGame.dropAStep(musicPl);
                    if(musicPl.weatherPlay)
                    {
                        playSound(4);
                        musicPl.weatherPlay=false;
                    }
                    if(isGameLost)
                    {
                        gameStop=true;
                        Message msg=new Message();
                        msg.what=1;
                        handler.sendMessage(msg);
                        while(gameStop){
                            Thread.sleep(1000);
                        }
                        russiaBlockGame.resetGame();
                    }
                    map=russiaBlockGame.getMapDisplay();
                    freshWindow();
                    score=russiaBlockGame.getscore();
                    nextBlock=russiaBlockGame.getNextType();
                    for(int i=0;i<nextBlock.length;i++)
                    {
                        for(int p=0;p<nextBlock.length;p++)
                        {
                            if(nextBlock[i][p].BlockType==1)
                            {
                                itemBeenListnext.get(i*4+p).color=nextBlock[i][p].color;
                            }else {
                                itemBeenListnext.get(i*4+p).color=Color.BLACK;
                            }
                        }
                    }
                    Message msg=new Message();
                    msg.what=2;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}