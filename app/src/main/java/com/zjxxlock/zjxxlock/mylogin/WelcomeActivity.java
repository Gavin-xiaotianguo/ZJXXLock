package com.zjxxlock.zjxxlock.mylogin;

/**
 * Created by JLW on 2017/8/11.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.zjxxlock.zjxxlock.R;

import cn.jpush.android.api.JPushInterface;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        new Handler().postDelayed(new Runnable() {
            //在Runnable被传入消息队列之前，延迟一定的时间，在这里传入的是2000毫秒
            @Override
            public void run() {
                Intent loginIntent = new Intent(WelcomeActivity.this, com.zjxxlock.zjxxlock.mylogin.LoginActivity.class);
               // Intent loginIntent = new Intent(WelcomeActivity.this, Administrator.class);
                startActivity(loginIntent);
                finish();
            }
        },2000);
    }
}