package com.zjxxlock.zjxxlock;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import java.util.UUID;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.zjxxlock.zjxxlock.web.WebServicePost;

import static android.R.id.list;

/**
 * Created by JLW on 2017/10/10.
 */

public class RecordView extends AppCompatActivity  {
    private final static String TAG = "JlwLock";
    private BluetoothAdapter mBluetoothAdapter; // = bluetoothManager.getAdapter();
    private Handler mHandler = new Handler();
    private boolean mScanning = false;
    private static final long SCAN_PERIOD = 1000000;
    //  private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = 0;
    private int mServiceState;
    private BluetoothGattService mBluetoothGattService;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristic1;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristic2;
    private boolean mRescan;
    private Dialog Mydialog;
    String doornumber;
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
    private String roomrecord;
    private String selectroom;
    private TextView roomname;
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

            handler.post(new Runnable() {
                @Override
                public void run() {

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
        setContentView(R.layout.record_admis);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "ble_not_supported");
            finish();
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        Log.i(TAG, "ble_ok_1");
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Adapter null", Toast.LENGTH_SHORT).show();
        }
        mBluetoothAdapter.enable();
        Log.i(TAG, "mBluetoothAdapter.enable");

        Log.i(TAG, "ble_ok_2");

        Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据
        roomrecord=bundle.getString("roomrecord");
        selectroom=bundle.getString( "selectroom" );
        doornumber=selectroom;
        roomname=(TextView)this.findViewById(R.id.select_room) ;
        roomname.setText(selectroom);
        roomrecord=convert(roomrecord);
        int j=0;
        JSONArray jsonArray = null;
        list = new ArrayList<>();
        list3 =new ArrayList<>();
        list4 =new ArrayList<>();
        list5 =new ArrayList<>();
        try {
            jsonArray = new JSONArray(roomrecord);
            for (int i =0; i < jsonArray.length(); i++) {


                JSONObject jsonObject = jsonArray.getJSONObject(i);

                list.add(jsonObject.get("使用人").toString());
                list3.add(jsonObject.get("开锁时间").toString());
                list4.add(jsonObject.get("开锁方式").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList list1 = new ArrayList();

        for(int list2 = 0; list2 < 1; ++list2) {
            HashMap i = new HashMap();

            i.put("txt", "使用人");
            i.put("img_to", "使用时间");
            i.put("txt1","开门方式");
            list1.add(i);
        }

        this.simpleAdapter = new SimpleAdapter(this.getApplicationContext(), list1, R.layout.simpleadapter_admis4, new String[]{"txt", "img_to","txt1"}, new int[]{R.id.admistxt4_4, R.id.admistxt4_5, R.id.admistxt4_6});
        listview0 = (ListView) this.findViewById(R.id.recordview0);
        listview0.setAdapter(this.simpleAdapter);
        listview = (ListView) findViewById(R.id.recordview);
        adapter = new MyAdapter(this,list,list3,list4,list5);
        listview.setAdapter(adapter);
        View button1 = findViewById(R.id.record_btn);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO 自动生成的方法存根
                if (mConnectionState != BluetoothProfile.STATE_CONNECTED) {
                    return;
                }
                if (mBluetoothGattService == null) {
                    return;
                }
                if (mBluetoothGattCharacteristic1 == null) {
                    return;
                }

                long currentTime = System.currentTimeMillis()/1000;
                int milis=(int)currentTime;
                String milis2=Integer.toHexString(milis);
                byte[]milisbyte=hexStringToBytes(milis2);
                message(String.valueOf(currentTime));

                byte[] buffer1 = {(byte)0xa5, (byte)milisbyte[3], (byte)milisbyte[2], (byte)milisbyte[1], (byte)milisbyte[0], 0x08,0x5a};
                mBluetoothGattCharacteristic1.setValue(buffer1);
                if (mBluetoothGatt.writeCharacteristic(mBluetoothGattCharacteristic1)) {
                    Toast.makeText(RecordView.this,"匹配成功",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RecordView.this,"匹配失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        View button2 = findViewById(R.id.record_btn1);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO 自动生成的方法存根
                if (mConnectionState != BluetoothProfile.STATE_CONNECTED) {
                    return;
                }
                if (mBluetoothGattService == null) {
                    return;
                }
                if (mBluetoothGattCharacteristic2 == null) {
                    return;
                }
                byte[] buffer2 = {(byte) 0xa5, 0x02, 0x5a};
                mBluetoothGattCharacteristic2.setValue(buffer2);
                if (mBluetoothGatt.writeCharacteristic(mBluetoothGattCharacteristic2)) {
                    Toast.makeText(RecordView.this,"开锁成功",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RecordView.this,"开锁失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                convertView = View.inflate(RecordView.this, R.layout.simpleadapter_admis3, null);
                vh = new MyViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (MyViewHolder) convertView.getTag();
            }
            vh.roomnumber.setText(list.get(position));
            vh.user.setText(list3.get(position));
            vh.useTime.setText(list4.get(position));


            return convertView;
        }


        class MyViewHolder {
            View itemView;

            private Button record;
            TextView roomnumber;
            TextView user;
            TextView useTime;

            public MyViewHolder(View itemView) {
                this.itemView = itemView;
                user=itemView.findViewById(R.id.admistxt3_1);
                useTime=itemView.findViewById(R.id.admistxt3_2);
                roomnumber = itemView.findViewById(R.id.admistxt3_0);

            }
        }
    }
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    private void scanLeDevice(final boolean enable) {

        if (enable) {
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback); //开始搜索

        }
        else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);//停止搜索

        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            final String dstname = doornumber;
            if (!(dstname.equals(device.getName())))
                return;
            mBluetoothDeviceAddress = device.getAddress();
            scanLeDevice(false);
            connect(mBluetoothDeviceAddress);
        }
    };

    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device. Try to reconnect. (先前连接的设备。 尝试重新连接)
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = BluetoothProfile.STATE_CONNECTING;

                return true;
            }
            else {

                scanLeDevice(true);
                return false;
            }
        }
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {

            Log.w(TAG, "Device not found.  Unable to connect.");
            scanLeDevice(true);
            return false;
        }

        mBluetoothGatt = device.connectGatt(this, false, mGattCallback); //该函数才是真正的去进行连接
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = BluetoothProfile.STATE_CONNECTING;
        if (mBluetoothGatt == null) {

            scanLeDevice(true);
            return false;
        }

        return true;
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override  //当连接上设备或者失去连接时会回调该函数
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            mConnectionState = newState;


            if (newState == BluetoothProfile.STATE_CONNECTED) { //连接成功
                Toast.makeText(RecordView.this,"连接成功",Toast.LENGTH_SHORT).show();
                mBluetoothGatt.discoverServices(); //连接成功后就去找出该设备中的服务 private BluetoothGatt mBluetoothGatt;
            }
            else if (newState == BluetoothProfile.STATE_CONNECTING) {  //连接失败
            }
            else if (newState == BluetoothProfile.STATE_DISCONNECTED) {  //连接失败
                if (mRescan) scanLeDevice(true);
            }
            else if (newState == BluetoothProfile.STATE_DISCONNECTING) {  //连接失败
            }
        }

        @Override  //当设备是否找到服务时，会回调该函数
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {   //找到服务了
                //在这里可以对服务进行解析，寻找到你需要的服务
                message("ServicesDiscovered");
                final List<BluetoothGattService> service_list = mBluetoothGatt.getServices();
                final String service_uuid = new String("aac0");
                final String characteristic1_uuid = new String("aac7");
                final String characteristic2_uuid = new String("aac1");
                for (BluetoothGattService service : service_list) {
                    message(service.getUuid().toString());
                    if (service.getUuid().toString().substring(4, 8).equals(service_uuid)) {
                        mBluetoothGattService = service;
                        break;
                    }
                }

                final List<BluetoothGattCharacteristic> characteristic_list = mBluetoothGattService.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : characteristic_list) {
                    if (characteristic.getUuid().toString().substring(4, 8).equals(characteristic1_uuid)) {
                        mBluetoothGattCharacteristic1 = characteristic;

                    }
                    if (characteristic.getUuid().toString().substring(4, 8).equals(characteristic2_uuid)) {
                        mBluetoothGattCharacteristic2 = characteristic;
                        setCharacteristicNotification(mBluetoothGattCharacteristic2, true);
                    }
                }
            }
            else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override  //当读取设备时会回调该函数
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            System.out.println("onCharacteristicRead");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //读取到的数据存在characteristic当中，可以通过characteristic.getValue();函数取出。然后再进行解析操作。
                //int charaProp = characteristic.getProperties();if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0)表示可发出通知。  判断该Characteristic属性
                if (characteristic == mBluetoothGattCharacteristic1) {
                }
                if (characteristic == mBluetoothGattCharacteristic2) {
                }
            }
        }

        @Override //当向设备Descriptor中写数据时，会回调该函数
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            System.out.println("onDescriptorWriteonDescriptorWrite = " + status + ", descriptor =" + descriptor.getUuid().toString());
            message("成功匹配可开锁");
        }

        @Override //设备发出通知时会调用到该接口
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

            if (characteristic == mBluetoothGattCharacteristic2) {
            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        }

        @Override //当向Characteristic写数据时会回调该函数
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        }
    };

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID
                .fromString("00002902-0000-1000-8000-00805f9b34fb"));
        if (descriptor != null) {
            System.out.println("write descriptor");
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public void message(final String msg) {
        if (msg != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RecordView.this,msg,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public String char2hex(byte[] buffer) {
        char[] num = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        String ret = "";
        for (int i = 0; i < (char) buffer.length; i++) {
            byte x = buffer[i];
            ret = ret + num[(x >> 4) & 0x0f] + num[x & 0x0f] + " ";
        }

        return ret;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBluetoothGatt != null) {
            mRescan = false;
            mBluetoothGatt.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mRescan = true;
        if (mConnectionState != BluetoothProfile.STATE_CONNECTED)
            scanLeDevice(true);
    }

}



