package com.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.controller.CAnalData;
import com.example.appcantool.R;
import com.model.MBlueTooth;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class VmainActivity extends Activity implements OnClickListener{
	//
	private Context	mContext;
	private Intent	mIntent;
	private SimpleAdapter	 mSimpleAdapter,mInfosAdapter;
	
	//
	private TextView vTopic, vBack,vDeceive,vBTooth;
	@SuppressWarnings("unused")
	private Button vSwitch, vTrans, vLoad, vSet;
	private ListView vlvDevices,vlvInfos;
	//	
	private MBlueTooth	 mBlueTooth;
	private AcceptThread acceptThread;
	private CAnalData 	 mCAnalData;
	//
	private String 	 sDevice,sBTooth,sSwitch;
	private ArrayList<Map<String, String>> listDevices,listInfos;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		initView();
		initEvent();
	}

	@Override
	protected void onResume() {
		checkDevice();
		showInfos();
		vlvInfos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				String info=listInfos.get(position).get("content");
				mCAnalData=new CAnalData(info);
				mCAnalData.computeData();
				char 	FLAG= mCAnalData.getFLAG();
				String  ID	= mCAnalData.getID();
				int 	DLC	= mCAnalData.getDLC();
				ArrayList<char[]> lists=mCAnalData.getDATA();
				int 	nSize=lists.size();
				String	tmp = "";
				for(int i=0;i<nSize;i++){
					String c=String.valueOf(lists.get(i));
					tmp+="ox"+c+" ";
				}
				Toast.makeText(mContext, "["+FLAG+"]["+ID+"]["+DLC+"]["+tmp+"]", Toast.LENGTH_SHORT).show();
			}
		});
		
		super.onResume();
	}
	private void initView() {
		vTopic 	   = (TextView) findViewById(R.id.tvTopic);
		vBack 	   = (TextView) findViewById(R.id.btnBack);
		
		vDeceive   = (TextView) findViewById(R.id.tvDevice);
		vBTooth	   = (TextView) findViewById(R.id.tvBTooth);
		
		vSwitch    = (Button) findViewById(R.id.btnSwitch);
		vTrans 	   = (Button) findViewById(R.id.btnTrans);
		vLoad 	   = (Button) findViewById(R.id.btnLoad);
		vSet 	   = (Button) findViewById(R.id.btnSet);
		vlvDevices = (ListView) findViewById(R.id.lvDevices);
		vlvInfos   = (ListView) findViewById(R.id.lvInfos);
	}

	private void initEvent() {
		mContext=VmainActivity.this;
		mBlueTooth=new MBlueTooth();
		listInfos=new ArrayList<Map<String,String>>();
		
		vTopic.setText("CanTool 工具应用App");
		vBack.setOnClickListener(this);
		vSwitch.setOnClickListener(this);
		
		checkDevice();
		showDevices(0);
		startThread();
	}

	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		//	退出键;
		case R.id.btnBack:
			finish();
			break;
		//	蓝牙键;
		case R.id.btnSwitch:
			if(mBlueTooth.isBlueToothOpen()){
				mBlueTooth.setBlueToothClose();
				sBTooth = "蓝牙关闭";
				sSwitch	= "蓝牙开启";
				vBTooth.setText(sBTooth);
				vSwitch.setText(sSwitch);
				showDevices(1);
				closeThread();
			}else{
				mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(mIntent, 0);
			}
			break;
		default:
			break;
		}
		
	}

	// 返回键
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			showDevices(0);
			startThread();
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Map<String, String> map=new HashMap<String, String>();
			map.put("name", "接收数据———>>");
			map.put("content", (String) msg.obj);
			listInfos.add(map);
			// 将信息显示;
			Toast.makeText(mContext, (String) msg.obj,Toast.LENGTH_SHORT).show();
			showInfos();
		}
	};
	public class AcceptThread extends Thread {

	    private final BluetoothServerSocket mmServerSocket;
	    private final String NAME = "Bluetooth_Socket";
	    private String uuid = "ffffffff-ea31-5a0f-0617-ee160033c587";
	    private InputStream is;// 获取到输入流
		@SuppressWarnings("unused")
		private OutputStream os;// 获取到输出流
		
	    public AcceptThread(BluetoothAdapter bluetoothAdapter) {
	        BluetoothServerSocket tmp = null;
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, UUID.fromString(uuid));
	        } catch (IOException e) { }
	        mmServerSocket = tmp;
	    }

	    public void run() {
	        BluetoothSocket socket = null;
	        while (true) {
	            try {
	                socket = mmServerSocket.accept();
	                
	                // 获取到输入流
					is = socket.getInputStream();
					// 获取到输出流
					os = socket.getOutputStream();

					// 创建一个128字节的缓冲
					byte[] buffer = new byte[128];
					// 每次读取128字节，并保存其读取的角标
					int count = is.read(buffer);
					// 创建Message类，向handler发送数据
					Message msg = new Message();
					// 发送一个String的数据，让他向上转型为obj类型
					msg.obj = new String(buffer, 0, count, "utf-8");
					// 发送数据
					handler.sendMessage(msg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            } catch (IOException e) {
	                break;
	            }
	        }
	    }

	    /** Will cancel the listening socket, and cause the thread to finish */
	    public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (IOException e) { }
	    }
	}
	//	本功能自带方法;
	//	01.检测蓝牙设备;
	private void checkDevice(){
		sDevice			  = "无蓝牙设备";
		sBTooth			  = "蓝牙关闭";
		sSwitch			  = "蓝牙开启";
		if (mBlueTooth.hasBlueToothDevice()) {
			sDevice="有蓝牙设备";
		}
		vDeceive.setText(sDevice);
		
		if(mBlueTooth.isBlueToothOpen()){
			sBTooth	="蓝牙开启";
			sSwitch	="蓝牙关闭";
		}
		vBTooth.setText(sBTooth);
		vSwitch.setText(sSwitch);		
	}
	//	02.显示设备信息;
	private void showDevices(int n){
		switch (n) {
		case 0:			
			listDevices=mBlueTooth.getListDevices();
			break;
		case 1:
			listDevices=mBlueTooth.getListDevicesClear();
			break;
		default:
			break;
		}
		mSimpleAdapter=new SimpleAdapter(mContext, listDevices, R.layout.item, new String[]{"name","address"}, new int[]{R.id.tvNum,R.id.tvContent});
		vlvDevices.setAdapter(mSimpleAdapter);
	}
	//	03.显示列表信息;
	private void showInfos(){
		mInfosAdapter=new SimpleAdapter(mContext, listInfos, R.layout.item, new String[]{"name","content"}, new int[]{R.id.tvNum,R.id.tvContent});
		vlvInfos.setAdapter(mInfosAdapter);
	}
	
	//	服务端线程的开启;
	private void startThread(){
		if(acceptThread==null){			
			acceptThread=new AcceptThread(mBlueTooth.getmBluetoothAdapter());
			acceptThread.start();
			Log.i("MyLog", "acceptThread02="+acceptThread);
		}
	}
	//	服务端线程的关闭;
	private void closeThread(){
		if(acceptThread!=null){
			acceptThread.interrupt();
			acceptThread=null;
		}
	}
}
