package com.zjxxlock.zjxxlock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.zjxxlock.zjxxlock.web.WebService;
import com.zjxxlock.zjxxlock.web.WebServicePost;

import static android.R.id.list;

/**
 * Created by JLW on 2017/10/10.
 */

public class ApplyView extends AppCompatActivity  {
    ListView listview;
    ListView listview0;
    SimpleAdapter simpleAdapter;
    String[] txt = new String[]{"", "", "", ""};
    private ArrayList<String> list;
    private ArrayList<String> list1;
    private ArrayList<String> list2;
    private ArrayList<String> list3;
    JSONObject jsonRoom = new JSONObject();
    String[] classbar = new String[]{"00", "00", "00"};
    private String Apply;
    private TextView roomname;
    private String jpush;
    private String feedback;

    private static Handler handler = new Handler();
    public String convert(String utfString) {//unicode编码字符转为中文字符
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = utfString.indexOf("\\u", pos)) != -1) {
            sb.append(utfString.substring(pos, i));
            if (i + 5 < utfString.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
            }
        }
        sb.append(utfString.substring(pos));

        return sb.toString();
    }
    public class MyThread implements Runnable {

        @Override
        public void run() {


            feedback= WebServicePost.executeHttpPost("8892","8892","ResultServlet",jsonRoom.toString());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ApplyView.this,feedback, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public boolean onRefresh() {
        // TODO Auto-generated method stub
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_admis);
        Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据
        Apply=bundle.getString("apply");

        Apply=convert(Apply);
        JSONArray jsonArray = null;
        list = new ArrayList<>();
        list1 =new ArrayList<>();
        list2=new ArrayList<>();
        list3=new ArrayList<>();

        try {
            jsonArray = new JSONArray(Apply);
            for (int i =0; i < jsonArray.length(); i++) {


                JSONObject jsonObject = jsonArray.getJSONObject(i);

                list.add(jsonObject.get("申请人").toString());
                list1.add(jsonObject.get("申请房间").toString());
                list2.add(jsonObject.get("申请理由").toString());
                list3.add(jsonObject.get("申请时间").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList list4 = new ArrayList();

        for(int k= 0; k< 1; ++k) {
            HashMap i = new HashMap();

            i.put("txt0", "申请人");
            i.put("txt1", "房间");
            i.put("txt2","理由");
            i.put("txt3","时间");
            list4.add(i);
        }

        this.simpleAdapter = new SimpleAdapter(this.getApplicationContext(), list4, R.layout.simpleadapter_admis6, new String[]{"txt0", "txt1","txt2","txt3"}, new int[]{R.id.admistxt6_4, R.id.admistxt6_5, R.id.admistxt6_6,R.id.admistxt6_7});
        listview0 = (ListView) this.findViewById(R.id.applyview);
        listview0.setAdapter(this.simpleAdapter);


        listview = (ListView) findViewById(R.id.applyview1);
        adapter = new MyAdapter(this,list,list1,list2,list3);
        listview.setAdapter(adapter);
    }




    private MyAdapter adapter;

    class MyAdapter extends BaseAdapter {
        private ArrayList<String> roomlist;
        private ArrayList<String> userlist;
        private ArrayList<String> timelist;
        private ArrayList<String> electiclist;
        private Context context;
        private LayoutInflater mInflater;

        public MyAdapter(Context context, ArrayList<String> list,ArrayList<String>list1,ArrayList<String>list2,ArrayList<String>list3) {
            this.context = context;
            this.mInflater = LayoutInflater.from(context);
            this.roomlist = list;
            this.userlist=list1;
            this.timelist=list2;
            this.electiclist=list3;
        }
        public void setData(ArrayList<String> newRoomList,ArrayList<String> newuserlist,ArrayList<String> newtimelist,ArrayList<String> neweeleclist){
            this.roomlist = newRoomList;
            this.userlist=newuserlist;
            this.timelist=newtimelist;
            this.electiclist=neweeleclist;
        }



        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MyViewHolder vh;
            if (convertView == null) {
                convertView = View.inflate(ApplyView.this, R.layout.simpleadapter_admis5, null);
                vh = new MyViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (MyViewHolder) convertView.getTag();
            }
            vh.user.setText(list.get(position));
            vh.roomnumber.setText(list1.get(position));
            vh.applyreason.setText(list2.get(position));
            vh.useTime.setText(list3.get(position));
            vh.reply.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(ApplyView.this, "hahah"+list.get(position), Toast.LENGTH_SHORT).show();
                    jpush=list.get(position);
                    try {
                        jsonRoom.put("学生姓名",jpush);
                        jsonRoom.put("结果","同意");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new Thread(new MyThread()).start();
                }
            });
            return convertView;
        }


        class MyViewHolder {
            View itemView;

            private Button reply;

            TextView user;
            TextView roomnumber;
            TextView applyreason;
            TextView useTime;


            public MyViewHolder(View itemView) {
                this.itemView = itemView;
                user=itemView.findViewById(R.id.applytxt0);
                roomnumber = itemView.findViewById(R.id.applytxt1);
                applyreason=itemView.findViewById(R.id.applytxt2);
                useTime=itemView.findViewById(R.id.applytxt3);
                reply=itemView.findViewById(R.id.simplebutton5_0);

            }
        }
    }
}

