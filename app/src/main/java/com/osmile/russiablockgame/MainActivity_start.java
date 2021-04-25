package com.osmile.russiablockgame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity_start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_start);

        TextView textView_start= (TextView)findViewById(R.id.start);

        textView_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity_start.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

//    利用吐司显示最高分，取到sharedPreferences共享数组
    public void maxScore(View view) {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int scoreNew = pref.getInt("score", 0);
        Toast.makeText(this, "游戏最高分为：" +scoreNew,Toast.LENGTH_LONG).show();
    }
}