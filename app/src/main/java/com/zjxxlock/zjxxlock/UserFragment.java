package com.zjxxlock.zjxxlock;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.zjxxlock.zjxxlock.listview.PersonBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/*
* *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    Context context;
    ListView listview0;
    ListView listview;
    ListView listview2;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapter2;
    SimpleAdapter simpleAdapter;
    String[] txt = new String[]{"朋友圈", "游戏", "购物", "发现"};
    SimpleAdapter simpleAdapter2;
    String[] txt2 = new String[]{"扫一扫", "摇一摇", "通讯录"};
    SimpleAdapter simpleAdapter0;
    BaseAdapter baseAdapter;
    private Button exit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getActivity().setContentView(R.layout.listview);
        int[] img = new int[]{17301586, 17301561, 17301547, 17301559};
        int[] img2 = new int[]{17301558, 17301581, 17301588};
        this.arrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.itemview, new String[]{"朋友圈", "游戏", "购物", "发现"});
        ArrayList list0 = new ArrayList();
        HashMap map0 = new HashMap();
        map0.put("img", Integer.valueOf(R.drawable.ic_lock_open));
        map0.put("nickname", "昵称：TiKi-TaKa");
        map0.put("name", "微信号：lssjzmn");
        map0.put("mine", Integer.valueOf(R.drawable.code));
        list0.add(map0);
        this.baseAdapter = new PersonBaseAdapter(getContext());
        this.simpleAdapter0 = new SimpleAdapter(getActivity().getApplicationContext(), list0, R.layout.simpleadapter_person, new String[]{"img", "nickname", "name", "mine"}, new int[]{R.id.simpleimg0, R.id.simpletxt0, R.id.simpletxt01, R.id.simpleimg0_mine});
        ArrayList list = new ArrayList();

        for(int list2 = 0; list2 < 4; ++list2) {
            HashMap i = new HashMap();
            i.put("img", Integer.valueOf(img[list2]));
            i.put("txt", this.txt[list2]);
            i.put("img_to", Integer.valueOf(R.drawable.go));
            list.add(i);
        }

        this.simpleAdapter = new SimpleAdapter(getActivity().getApplicationContext(), list, R.layout.simpleadapter, new String[]{"img", "txt", "img_to"}, new int[]{R.id.simpleimg, R.id.simpletxt, R.id.simpleimg_to});
        ArrayList var10 = new ArrayList();

        for(int var11 = 0; var11 < 3; ++var11) {
            HashMap map = new HashMap();
            map.put("img", Integer.valueOf(img2[var11]));
            map.put("txt", this.txt2[var11]);
            map.put("img_to", Integer.valueOf(R.drawable.go));
            var10.add(map);
        }

        this.simpleAdapter2 = new SimpleAdapter(getActivity().getApplicationContext(), var10, R.layout.simpleadapter, new String[]{"img", "txt", "img_to"}, new int[]{R.id.simpleimg, R.id.simpletxt, R.id.simpleimg_to});
        this.listview0 = (ListView)getActivity().findViewById(R.id.listview0);
        this.listview = (ListView) getActivity().findViewById(R.id.listview);
        this.listview2 = (ListView)getActivity().findViewById(R.id.listview2);
        this.listview0.setAdapter(this.baseAdapter);
        this.listview.setAdapter(this.simpleAdapter);
        this.listview2.setAdapter(this.simpleAdapter2);
        this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.getChildAt(position);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


//        exit=(Button) getActivity().findViewById(R.id.btn_log_out);
//        exit.setOnClickListener(exitClick);
//        ExitApplication.getInstance().addActivity(getActivity());
    }
//    View.OnClickListener exitClick=new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//            ExitApplication.getInstance().exit();
//        }
//    };




