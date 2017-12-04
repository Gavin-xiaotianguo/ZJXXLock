package com.zjxxlock.zjxxlock.listview;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//



import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.zjxxlock.zjxxlock.R;

public class InitActivity extends Activity {
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            InitActivity.this.finish();
            super.handleMessage(msg);
        }
    };

    public InitActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_init);
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InitActivity.this.handler.sendEmptyMessage(1);
            }
        }, 6000L);
    }

    public void tapme(View v) {
        this.handler.sendEmptyMessage(1);
    }
}

