package com.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import com.example.btoothtest01.R;
import com.model.MBlueTooth;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class VmainActivity extends Activity implements OnClickListener,OnItemClickListener{
	//	01.系统信息的内容;
	private Context	 mContext;
	private Intent	 mIntent;	
	private SimpleAdapter	 mSimpleAdapter;
	//	02.控件信息的内容;
	private TextView vDevice,vBTooth,vBack,vTopic;
	private Button	 vTest;
	private Builder  vBuilder;
	private ListView vlvDevices; 
	//	03.参数信息的内容;
	private String 	 sDevice,sBTooth,sSwitch;
	private ArrayList<Map<String, String>> listDevices;
	//	04.自定义类的内容;
	private MBlueTooth mBlueTooth;
	private AcceptThread acceptThread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		initView();
		initEvent();
	}

	private void initView(){
		vDevice	  =(TextView) findViewById(R.id.tvDevice);
		vBTooth	  =(TextView) findViewById(R.id.tvBTooth);
		vTest	  =(Button) findViewById(R.id.btnTest);
		vBack	  =(TextView) findViewById(R.id.btnBack);
		vTopic	  =(TextView) findViewById(R.id.tvTopic);
		vlvDevices=(ListView) findViewById(R.id.lvDevices);
	}
	private void initEvent(){
		mContext		  = VmainActivity.this;
		mBlueTooth		  = new MBlueTooth();
		vBack.setOnClickListener(this);
		vlvDevices.setOnItemClickListener(this);
		vTopic.setText("蓝牙——数据传输工具");
		//	检测设备信息;
		checkDevice();
		//	显示设备信息;
		showDevices(0);
		acceptThread=new AcceptThread(mBlueTooth.getmBluetoothAdapter());
		acceptThread.start();
	}
	@Override
	protected void onResume() {
		checkDevice();
		super.onResume();
	}
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btnBack:
			vBuilder=new Builder(mContext);
			vBuilder.setIcon(R.drawable.ic_launcher);
			vBuilder.setTitle("退出?");
			vBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
				}
			});
			vBuilder.setNegativeButton("取消", null);
			vBuilder.create();
			vBuilder.show();
			break;
		case R.id.btnTest:

			if(mBlueTooth.isBlueToothOpen()){
				mBlueTooth.setBlueToothClose();
				sBTooth = "蓝牙关闭";
				sSwitch	= "蓝牙开启";
				vBTooth.setText(sBTooth);
				vTest.setText(sSwitch);
				showDevices(1);
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
		if(requestCode==0){
			showDevices(0);
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
		vDevice.setText(sDevice);
		
		if(mBlueTooth.isBlueToothOpen()){
			sBTooth	="蓝牙开启";
			sSwitch	="蓝牙关闭";
		}
		vBTooth.setText(sBTooth);
		vTest.setText(sSwitch);		
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
	public class ClientThread extends Thread{
		private BluetoothAdapter bluetoothAdapter;
		private BluetoothDevice selectDevice;
		private BluetoothSocket mmSocket,tmpSocket;
	    private String uuid = "ffffffff-ea31-5a0f-0617-ee160033c587";
	    private OutputStream os;
	    
		public ClientThread(BluetoothAdapter bluetoothAdapter,String address) {
			this.bluetoothAdapter=bluetoothAdapter;
			tmpSocket=null;
			if (selectDevice == null) {
				// 通过地址获取到该设备
				selectDevice = bluetoothAdapter.getRemoteDevice(address);
			}
			// Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmpSocket =  selectDevice.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
	        } catch (IOException e) { }
	        mmSocket = tmpSocket;
			
		}
		
		@Override
		public void run() {
			Log.i("MyLog", "bluetoothAdapter="+bluetoothAdapter);
			if (bluetoothAdapter.isDiscovering()) {
				bluetoothAdapter.cancelDiscovery();
			}
			try {
				
		        if(mmSocket!=null){		        	 
		        	Log.i("MyLog", "mmSocket="+mmSocket);
		        	mmSocket.connect();
		        	
		        	// 获取到输出流，向外写数据
					os = mmSocket.getOutputStream();
					Log.i("MyLog", "os="+os);
		        }
		        if(os!=null){
		        	// 需要发送的信息
					String text = "t12380011121314151617\r";
					// 以utf-8的格式发送出去
					os.write(text.getBytes("UTF-8"));
		        }
	        } catch (IOException connectException) {
	            try {
	            	if(os!=null){
	            		os.close();
	            	}
	            	if(mmSocket!=null){	            		
	            		mmSocket.close();
	            	}
	            } catch (IOException closeException) { }
	            return;
	        }
			try {
				if (os != null) {
					os.close();
				}
				if (mmSocket != null) {
					mmSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// 将信息显示;
			Toast.makeText(mContext, (String) msg.obj,
					Toast.LENGTH_SHORT).show();
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
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		String address=listDevices.get(position).get("address");
		Toast.makeText(mContext, "address:\n"+address, Toast.LENGTH_LONG).show();
		ClientThread clientThread=new ClientThread(mBlueTooth.getmBluetoothAdapter(), address);
		clientThread.start();
		
	}
}
