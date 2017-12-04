package com.zjxxlock.zjxxlock;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.zjxxlock.zjxxlock.web.WebServicePost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.example.web.WebServicePost;


/*
* *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment {

    private Spinner building;
    private Spinner floor;
    private Spinner classroom;
    private TextView tv_feedback;
    private EditText use_time;
    private Button datebtn;
    private Button datebtn1;
    private EditText use_time1;
    private Button datebtn2;
    private Button datebtn3;
    private EditText applyreason;

    private Button key;
    private String userid;
    private String mClassroom;
    // 返回主线程更新数据
    private static Handler handler = new Handler();
    private String s;
    private String startTime;
    private String endTime;
    ArrayList my1 = new ArrayList();
    ArrayList my2=new ArrayList();
    ArrayList my3=new ArrayList();
    List<Map<String,Object>> mList;
    JSONObject jsonRoom = new JSONObject();
    String[] classbar = new String[]{"00", "00", "00"};
    private Calendar calendar;
    private String dateuu;
    private String timeuu;

    // 子线程接收数据，主线程修改数据
    public class MyThread implements Runnable {

        @Override
        public void run() {
            s = WebServicePost.executeHttpPost(userid, mClassroom, "ApplyServlet", jsonRoom.toString());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record, container, false);

    }

    private void showTimeDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                use_time.setText(dateuu+" "+hourOfDay+":"+minute);

            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        // 显示 Dialog
        timePickerDialog.show();
    }

    /**
     * 显示日期Dialog
     */
    private void showDateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateuu=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                showTimeDialog();

            }
        },  calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // 显示Dialog
        datePickerDialog.show();
    }

    private void showDateDialog1() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                timeuu=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                showTimeDialog1();

            }
        },  calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // 显示Dialog
        datePickerDialog.show();
    }
    private void showTimeDialog1() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                use_time1.setText(timeuu+" "+hourOfDay+":"+minute);

            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        // 显示 Dialog
        timePickerDialog.show();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Bundle bundle = getArguments();
        userid = bundle.getString("DATA1");

        s = bundle.getString("DATA3");
        s = convert(s);
        JSONArray jsonArray = null;
        int j=0;
        calendar = Calendar.getInstance();
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

        try {
            jsonArray = new JSONArray(s);
            for (int i =j; i < jsonArray.length(); i++) {


                JSONObject jsonObject = jsonArray.getJSONObject(i);

                my3.add(jsonObject.get("房间"));
                j++;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        tv_feedback = (TextView) getActivity().findViewById(R.id.tv_feedback);
        applyreason = (EditText) getActivity().findViewById(R.id.tv_apply_reason);
        use_time = (EditText) getActivity().findViewById(R.id.use_time);
        datebtn = (Button) getActivity().findViewById(R.id.dateBtn);
        datebtn1 = (Button) getActivity().findViewById(R.id.dateBtn1);
        use_time1 = (EditText) getActivity().findViewById(R.id.use_time1);
        key = (Button) getActivity().findViewById(R.id.btn_send_request);

        building = (Spinner) getActivity().findViewById(R.id.spinner);
        floor = (Spinner) getActivity().findViewById(R.id.spinner1);
        classroom = (Spinner) getActivity().findViewById(R.id.spinner2);

        building.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, my1));
        floor.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, my2));
        classroom.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, my3));

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

        classroom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str2 = (String)my3.get(position);
                classbar[2] = str2;
                try {
                    jsonRoom.put("房间号", classbar[2]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        datebtn.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                showDateDialog();
            }
        });

        datebtn1.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                showDateDialog1();
            }
        });


        key.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                try {
                    jsonRoom.put("申请理由", applyreason.getText().toString());
                    jsonRoom.put("开始时间", use_time.getText().toString());
                    jsonRoom.put("结束时间", use_time1.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new Thread(new MyThread()).start();
            }
        });

    }

}




