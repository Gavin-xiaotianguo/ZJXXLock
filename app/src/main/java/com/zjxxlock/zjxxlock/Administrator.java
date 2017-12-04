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

import com.zjxxlock.zjxxlock.mylogin.LoginActivity;
import com.zjxxlock.zjxxlock.web.WebServicePost;

import cn.jpush.android.api.JPushInterface;

import static android.R.id.list;

/**
 * Created by JLW on 2017/10/10.
 */

public class Administrator extends AppCompatActivity  {
    ListView listview;
    ListView listview0;
    SimpleAdapter simpleAdapter;
    String[] txt = new String[]{"", "", "", ""};
    ArrayList my1 = new ArrayList();
    ArrayList my2=new ArrayList();
    ArrayList my3=new ArrayList();
    private Spinner building;
    private Spinner floor;
    private ArrayList<String> list;
    private ArrayList<String> list3;
    private ArrayList<String> list4;
    private ArrayList<String> list5;
    JSONObject jsonRoom = new JSONObject();
    String[] classbar = new String[]{"00", "00", "00"};
    private String s;
    private String usr;
    private String schoolbuilding;
    private static Handler handler = new Handler();
    private String currentuser;
    private Button convey;
    private  String selectroom;
    private String roomrecord;
    private String Issued;
    private Button Applyrecord;
    private Button TimeKey;
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
            currentuser = WebServicePost.executeHttpPost("admis", "admis", "AdmisServlet", jsonRoom.toString());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONArray jsonArray1 = null;
                    currentuser=convert(currentuser);
                    try {
                        jsonArray1 = new JSONArray(currentuser);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            Map<String, Object> map = new HashMap<String, Object>();

                            JSONObject jsonObject = jsonArray1.getJSONObject(i);
                       /*     list4=new ArrayList<>();
                            list5=new ArrayList<>();
                            list3=new ArrayList<>();*/
                            list3.add(jsonObject.getString("房间号").toString());
                            for(int j=0;j<list.size();j++){
                                if(list.get(j).equals(jsonObject.getString("房间号").toString()))
                                {
                                    list4.set(j, jsonObject.getString("老师").toString());
                                    list5.set(j, jsonObject.getString("使用时间").toString());
                                }
                               else{
                                    list4.set(j," ");
                                    list5.set(j," ");

                                }
                            }

                        }
                        adapter.setData(list,list4,list5,list3);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
    }
    }
    public class MyThread2 implements Runnable {

        @Override
        public void run() {
            roomrecord = WebServicePost.executeHttpPost("admis", "admis", "NoteServlet", selectroom);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent Jump = new Intent();
                    Jump.putExtra("selectroom", selectroom);
                    Jump.putExtra("roomrecord",roomrecord);
                    Jump.setClass(Administrator.this, RecordView.class);
                    startActivity(Jump);
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
        setContentView(R.layout.adminis_main);
       Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据
        usr=bundle.getString("usr");
        schoolbuilding=bundle.getString( "building" );
        Issued=bundle.getString("Issued");
        convey=(Button) this.findViewById(R.id.admis_reply);
        Applyrecord=(Button)this.findViewById(R.id.admis_Issued);
        TimeKey=(Button)this.findViewById(R.id.Amain_issued);
        if(Issued.equals("不存在")){
            Applyrecord.setText("无申请");
        }
        else{
            Applyrecord.setText("有申请");
        }
        s=schoolbuilding;
        s=convert(s);
        int j=0;
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String ,Object> map=new HashMap<String, Object>();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                map.put("a",jsonObject.get("教学楼"));
                my1.add(map.get("a"));
                j++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonArray = new JSONArray(s);
            for (int i =j; i < jsonArray.length(); i++) {


                JSONObject jsonObject = jsonArray.getJSONObject(i);

                my2.add(jsonObject.get("楼层"));
                j++;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        list = new ArrayList<>();
        list3 =new ArrayList<>();
        list4 =new ArrayList<>();
        list5 =new ArrayList<>();
        try {
            jsonArray = new JSONArray(s);
            for (int i =j; i < jsonArray.length(); i++) {


                JSONObject jsonObject = jsonArray.getJSONObject(i);

                list.add(jsonObject.get("房间").toString());
                list3.add(" ");
                list4.add(" ");
                list5.add(" ");
                j++;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        building = (Spinner) this.findViewById(R.id.admis_building);
        floor = (Spinner) this.findViewById(R.id.admis_floor);
        building.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, my1));
        floor.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, my2));

        building.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) my1.get(position);
                classbar[0] = str;
                try {
                    jsonRoom.put("教学楼", classbar[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        floor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String str1 = (String)my2.get(position);
                classbar[1] = str1;
                try {
                    jsonRoom.put("楼层", classbar[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayList list1 = new ArrayList();

        for(int list2 = 0; list2 < 1; ++list2) {
            HashMap i = new HashMap();
            i.put("img", "教室号");
            i.put("txt", "使用人");
            i.put("img_to", "使用时间");
            i.put("txt1","剩余电量");
            list1.add(i);
        }

        this.simpleAdapter = new SimpleAdapter(this.getApplicationContext(), list1, R.layout.simpleadapter_admis2, new String[]{"img", "txt", "img_to","txt1"}, new int[]{R.id.admistxt3, R.id.admistxt4, R.id.admistxt5,R.id.admistxt6});
        listview0 = (ListView) this.findViewById(R.id.admisview0);
        listview0.setAdapter(this.simpleAdapter);
        listview = (ListView) findViewById(R.id.admisview);
        adapter = new MyAdapter(this,list,list3,list4,list5);
        listview.setAdapter(adapter);
        convey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new Thread(new MyThread()).start();
            }
        });
        Applyrecord.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent JumpApply = new Intent();
                JumpApply.putExtra("apply", Issued);
                JumpApply.setClass(Administrator.this, ApplyView.class);
                startActivity(JumpApply);

            }
        });
        TimeKey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent JumpAmain = new Intent();
               // JumpApply.putExtra("apply", Issued);
                JumpAmain.setClass(Administrator.this, Amain.class);
                startActivity(JumpAmain);

            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?>parent , View view , int position , long id)
            {

                Toast.makeText(Administrator.this, "hahah"+list.get(position), Toast.LENGTH_SHORT).show();
                selectroom=classbar[0]+" "+classbar[1]+list.get(position);
                new Thread(new MyThread2()).start();
            }

        });


    }



    private void initData(){

    }
    private MyAdapter adapter;

    class MyAdapter extends BaseAdapter {
        private ArrayList<String> roomlist;
        private ArrayList<String> userlist;
        private ArrayList<String> timelist;
        private ArrayList<String> electiclist;
        private Context context;
        private LayoutInflater mInflater;

        public MyAdapter(Context context, ArrayList<String> list,ArrayList<String>list3,ArrayList<String>list4,ArrayList<String>list5) {
            this.context = context;
            this.mInflater = LayoutInflater.from(context);
            this.roomlist = list;
            this.userlist=list3;
            this.timelist=list4;
            this.electiclist=list5;
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
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MyViewHolder vh;
            if (convertView == null) {
                convertView = View.inflate(Administrator.this, R.layout.simpleadapter_admis, null);
                vh = new MyViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (MyViewHolder) convertView.getTag();
            }
            vh.roomnumber.setText(list.get(position));
            vh.user.setText(list3.get(position));
            vh.useTime.setText(list4.get(position));
            vh.electric.setText(list5.get(position));
          /*  vh.record.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(Administrator.this, "hahah"+list.get(position), Toast.LENGTH_SHORT).show();
                    selectroom=classbar[0]+classbar[1]+list.get(position);

                    new Thread(new MyThread2()).start();
                }
            });*/



            return convertView;
        }


        class MyViewHolder {
            View itemView;

            private Button record;
            TextView roomnumber;
            TextView user;
            TextView useTime;
            TextView electric;
            public MyViewHolder(View itemView) {
                this.itemView = itemView;
                user=itemView.findViewById(R.id.admistxt1);
                useTime=itemView.findViewById(R.id.admistxt2);
                roomnumber = itemView.findViewById(R.id.admistxt0);
                electric=itemView.findViewById(R.id.admistxt7);
//                record = itemView.findViewById(R.id.simplebutton1);
            }
        }
    }
    }

