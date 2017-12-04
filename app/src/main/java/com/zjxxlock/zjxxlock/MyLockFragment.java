package com.zjxxlock.zjxxlock;

import android.app.AlertDialog;
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
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.numberprogressbar.NumberProgressBar;
import com.zjxxlock.zjxxlock.web.ACache;
import com.zjxxlock.zjxxlock.web.WebService;
import com.zjxxlock.zjxxlock.web.WebServicePost;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


/*
* *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyLockFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyLockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLockFragment extends Fragment {


    //-------------------------------------------
    private final static String TAG = "JlwLock";
    private BluetoothAdapter mBluetoothAdapter; // = bluetoothManager.getAdapter();
    private Handler mHandler = new Handler();
    private boolean mScanning = false;
    private static final long SCAN_PERIOD = 1000000;
    //  private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = 0;
    private int mConnectionState1 =1;
    private int mServiceState;
    private BluetoothGattService mBluetoothGattService;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristic1;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristic2;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristic3;
    private boolean mRescan;
    private Dialog Mydialog;
    private Context context;
    public RrogressBar mProgressHUD;
    private int i = -1;
    private static Handler handler = new Handler();
    //-------------------------------------------
    private NumberProgressBar bnp;
    private TextView infotvg;
    private String bname;
    String userMAC;
    String userid;
    String infog;
    private boolean flage = true;
    private Spinner avail_room;
    //private Button key;
    String[] availableroom = new String[]{"JLW Lock2", "JLW Lock"};
    String[] classbar = new String[]{"00"};
    private Timer timer;
    private String update;
    private String startroom;
    private String reply;
    SimpleAdapter simpleAdapter;
    String[] txt = new String[]{"", "", "", ""};
    String[] listnumber = new String[]{"reply", "reply1", "reply2", "reply3"};
    ListView listview;
    public static String reply2;
    private ACache mCache;
    private String feedback;
    private String upload;
    private TextView secret;
    private boolean determin=false;




//    private Runnable runnable = new Runnable() {
//        public void run() {
//
//            this.update();
//            handler.postDelayed(this, 1000 * 30);
//
//        }
//        void update() {
//            update = WebServicePost.executeHttpPost(userid, "1", "RoomServlet", " ");
//            infotvg.setText(update);
//        }
//    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_lock, container, false);
        return view;
    }

    public String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public void showMyDialog(boolean isCancelable) {
        mProgressHUD = RrogressBar.show(getActivity(), "正在加载", true, isCancelable, null);
    }
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                // 使用postInvalidate可以直接在线程中更新界面
                //getView().postInvalidate();
                update = WebServicePost.executeHttpPost(userid, "1", "RoomServlet", " ");
//                update = WebServicePost.executeHttpPost(userid, "1", "RoomServlet", " ");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //getView().invalidate();
                        infotvg.setText(update);

                    }

                });
            }
        }
    }
    public class MyThread3 implements Runnable{
        @Override
        public void run() {
           reply = WebServicePost.executeHttpPost(userid, "1", "ReplyServlet", " ");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    /*secret.setText(reply);*/
                    Toast.makeText(getActivity(), reply, Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    public class MyThread2 implements Runnable {
        boolean flage = false;
        JSONObject obj=new JSONObject();

        @Override
        public void run() {
            while (!flage) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (mConnectionState != BluetoothProfile.STATE_CONNECTED) {
                    return;
                }
                if (mBluetoothGattService == null) {
                    return;
                }

                if (mBluetoothGattCharacteristic3 == null) {
                    return;
                }
                mBluetoothGatt.readCharacteristic(mBluetoothGattCharacteristic3);
                final byte[] data = mBluetoothGattCharacteristic3.getValue();
                if (data == null) {
                    flage=true;
                }

                if (data != null) {
                    upload = bytesToHexString(data);
                    try {
                        obj.put("开锁房间",bname);
                        obj.put("开锁记录",upload);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    feedback = WebServicePost.executeHttpPost("gugu","3", "RecordServlet", obj.toString());
                }

//                update = WebServicePost.executeHttpPost(userid, "1", "RoomServlet", " ");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (feedback.equals("已经成功保存") ) {
                            if (mConnectionState != BluetoothProfile.STATE_CONNECTED) {
                                return;
                            }
                            if (mBluetoothGattService == null) {
                                return;
                            }
                            if (mBluetoothGattCharacteristic2 == null) {
                                return;
                            }
                            byte[] buffer3 = {(byte) 0xa5, 0x02, data[1], data[2], 0x5a};
                            mBluetoothGattCharacteristic2.setValue(buffer3);
                            if (mBluetoothGatt.writeCharacteristic(mBluetoothGattCharacteristic2)) {

                            }

                        }
                        else{
                            flage=true;
                        }

                    }

                });
            }
        }
    }



    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Bundle bundle = getArguments();
        bname = bundle.getString("DATA");
        userid = bundle.getString("DATA1");
        bnp = (NumberProgressBar)getActivity().findViewById(R.id.number_progress_bar);
        int[] img = new int[]{17301561, 17301561, 17301561, 17301561};
        timer = new Timer();
        infotvg = (TextView)getActivity().findViewById( R.id.tv_avail_room);
        secret = (TextView)getActivity().findViewById( R.id.replyview);
        infotvg.setText(bname);
        ArrayList list = new ArrayList();
        new Thread(new MyThread()).start();

        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "ble_not_supported");
            getActivity().finish();
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        Log.i(TAG, "ble_ok_1");
        if (mBluetoothAdapter == null) {
//		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//		    startActivityForResult(enableBtIntent, 0);
//			mBluetoothAdapter.enable();
            Toast.makeText(getActivity(), "Adapter null", Toast.LENGTH_SHORT).show();
        }

        mBluetoothAdapter.enable();
        Log.i(TAG, "mBluetoothAdapter.enable");
        Log.i(TAG, "ble_ok_2");
        View button1 = getActivity().findViewById(R.id.btn_ol);
        View button2 = getActivity().findViewById(R.id.btn_o2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new MyThread3()).start();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO 自动生成的方法存根
               byte[] buffer1 = {(byte) 0xa5, 0x02, 0x5a};
                mBluetoothGattCharacteristic1.setValue(buffer1);
                if (mBluetoothGatt.writeCharacteristic(mBluetoothGattCharacteristic1)) {
                    Toast.makeText(getActivity(), "成功开锁！", Toast.LENGTH_SHORT).show();

//                    timer.cancel();
//                    bnp.setProgress(0);
                    //message(*//*Write OK*//*"成功开锁!");
                }
                else {
                    Toast.makeText(getActivity(), "开锁失败！", Toast.LENGTH_SHORT).show();
                    //message(*//*Write fail*//*"开锁失败！");

                }
                while(determin!=true){
                    writemessage();
                }
            }
        });
    }

       public void writemessage(){
           if (mConnectionState != BluetoothProfile.STATE_CONNECTED) {
               message("未找到蓝牙连接！" + mConnectionState);
               return;
           }
           if (mBluetoothGattService == null) {
               message("未找到蓝牙服务！");
               return;
           }
           if(mBluetoothGattCharacteristic2 == null){

               return;
           }
           byte[] buffer2 = {(byte) 0xa5, 0x02, (byte) 0xff, (byte) 0xff, 0x5a};
           mBluetoothGattCharacteristic2.setValue(buffer2);
           if (mBluetoothGatt.writeCharacteristic(mBluetoothGattCharacteristic2))
           {
               determin=true;
               new Thread(new MyThread2()).start();
           }
           else {


           }


    }








    private void scanLeDevice(final boolean enable) {

        if (enable) {
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback); //开始搜索
            message(/*Start scanning*/"开始搜索门锁！");
        }
        else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);//停止搜索
            message(/*Stop scanning*/"停止搜索门锁！");
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            final String dstname = infotvg.getText().toString();
//        	message(device.getName());
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
        message(/*Start Connect*/"开始连接门锁！");

        // Previously connected device. Try to reconnect. (先前连接的设备。 尝试重新连接)
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = BluetoothProfile.STATE_CONNECTING;
                message(/*Reconnect*/"重新连接！");
                return true;
            }
            else {
                message(/*Rescan*/"重新搜索！");
                scanLeDevice(true);
                return false;
            }
        }
//		if (mBluetoothGatt != null)
//		{
//			mBluetoothGatt.disconnect();
//			message("Disconnecting");
//		}

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            message(/*Device not found*/"");
            Log.w(TAG, "Device not found.  Unable to connect.");
            scanLeDevice(true);
            return false;
        }
        // We want to directly connect to the device, so we are setting the
        // autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(getActivity(), false, mGattCallback); //该函数才是真正的去进行连接
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = BluetoothProfile.STATE_CONNECTING;
        if (mBluetoothGatt == null) {
            message(/*Connecting fail*/"连接失败！");
            scanLeDevice(true);
            return false;
        }
        message(/*Connecting done*/"正在连接！");
        return true;
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override  //当连接上设备或者失去连接时会回调该函数
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            mConnectionState = newState;
            message(/*ConnectionStateChange*/"");

            if (newState == BluetoothProfile.STATE_CONNECTED) { //连接成功
                message(/*Connected*/"已连接！");
                mBluetoothGatt.discoverServices(); //连接成功后就去找出该设备中的服务 private BluetoothGatt mBluetoothGatt;
            }
            else if (newState == BluetoothProfile.STATE_CONNECTING) {  //连接失败
                message(/*Connecting*/"正在连接！");
            }
            else if (newState == BluetoothProfile.STATE_DISCONNECTED) {  //连接失败
                message(/*Disconnected - Rescan*/"");
                if (mRescan) scanLeDevice(true);
            }
            else if (newState == BluetoothProfile.STATE_DISCONNECTING) {  //连接失败
                message(/*Disconnecting*/"");
            }
        }

        @Override  //当设备是否找到服务时，会回调该函数
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {   //找到服务了
                //在这里可以对服务进行解析，寻找到你需要的服务
                message(/*ServicesDiscovered*/"");
                final List<BluetoothGattService> service_list = mBluetoothGatt.getServices();
                final String service_uuid = new String("aac0");
                final String characteristic1_uuid = new String("aac1");
                final String characteristic2_uuid = new String("aac5");
                final String characteristic3_uuid = new String("aac6");
                for (BluetoothGattService service : service_list) {
                    message(/*service.getUuid().toString()*/"");
                    if (service.getUuid().toString().substring(4, 8).equals(service_uuid)) {
                        mBluetoothGattService = service;
                        message(/*Service Found*/"");
                        break;
                    }
                }

                final List<BluetoothGattCharacteristic> characteristic_list = mBluetoothGattService.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : characteristic_list) {
                    if (characteristic.getUuid().toString().substring(4, 8).equals(characteristic1_uuid)) {
                        mBluetoothGattCharacteristic1 = characteristic;
                        message(/*"Characteristic1 found"*/"连接成功，可开锁！");
                    }
                    if (characteristic.getUuid().toString().substring(4, 8).equals(characteristic2_uuid)) {
                        mBluetoothGattCharacteristic2 = characteristic;
                        message(/*Characteristic2 found*/"");
                     ;
                    }
                    if (characteristic.getUuid().toString().substring(4, 8).equals(characteristic3_uuid)) {
                        mBluetoothGattCharacteristic3 = characteristic;
                        message(/*Characteristic2 found*/"");
                        setCharacteristicNotification(mBluetoothGattCharacteristic3, true);
                        message(/*Set notify enable*/"");

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
                message(/*"Char read: " + characteristic.getUuid()*/"");
                if (characteristic == mBluetoothGattCharacteristic1) {
                    message(/*"Char AAC1: " + char2hex(characteristic.getValue())*/"");
                }
                if (characteristic == mBluetoothGattCharacteristic2) {
                    message(/*"Char AAC2: " + char2hex(characteristic.getValue())*/"");
                }
                if (characteristic == mBluetoothGattCharacteristic3) {
                    message(/*"Char AAC2: " + char2hex(characteristic.getValue())*/"");
                }
            }
        }

        @Override //当向设备Descriptor中写数据时，会回调该函数
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            System.out.println("onDescriptorWriteonDescriptorWrite = " + status + ", descriptor =" + descriptor.getUuid().toString());
            message("Descriptor Write");
        }

        @Override //设备发出通知时会调用到该接口
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

            message(/*"Characteristic Changed: " + characteristic.getUuid().toString()*/"");
            if (characteristic == mBluetoothGattCharacteristic2) {
                message(/*"Char AAC2: " + char2hex(characteristic.getValue())*/"");
            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
//			System.out.println("rssi = " + rssi);
        }

        @Override //当向Characteristic写数据时会回调该函数
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//			System.out.println("--------write success----- status:" + status);
            message(/*Characteristic Write*/"");
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
        final TextView textView3 = (TextView) getActivity().findViewById(R.id.textView3);
        if (textView3 != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CharSequence text = textView3.getText();
                    text = msg + "\r\n" + text;
                    textView3.setText(text);
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


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
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



