package com.zjxxlock.zjxxlock.listview;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//



import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjxxlock.zjxxlock.R;

public class PersonBaseAdapter extends BaseAdapter {
    private Context context;

    public PersonBaseAdapter(Context context) {
        this.context = context;
    }

    public int getCount() {
        return 1;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0L;
    }

    public View getView(int position, View inflatedView, ViewGroup parent) {
        if(inflatedView == null) {
            inflatedView = LayoutInflater.from(this.context).inflate(R.layout.simpleadapter_person, (ViewGroup)null);
            ImageView img = (ImageView)inflatedView.findViewById(R.id.simpleimg0);
            TextView nickname = (TextView)inflatedView.findViewById(R.id.simpletxt0);
            TextView name = (TextView)inflatedView.findViewById(R.id.simpletxt01);
            ImageView img_code = (ImageView)inflatedView.findViewById(R.id.simpleimg0_mine);
            ImageView img_to = (ImageView)inflatedView.findViewById(R.id.simpleimg0_to);
            img.setImageResource(R.drawable.ic_lock_open);
            nickname.setText("昵称：Sergio Kun");
            name.setText("微信号：lssjzmn");
            img_code.setImageResource(R.drawable.code);
            img_to.setImageResource(R.drawable.go);
            img_code.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ImageView imgView = new ImageView(PersonBaseAdapter.this.context);
                    imgView.setImageResource(R.drawable.mycode);
                    Builder builder = new Builder(PersonBaseAdapter.this.context);
                    builder.setCancelable(true).setView(imgView).setMessage("扫一扫二维码加我哦");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        return inflatedView;
    }
}
