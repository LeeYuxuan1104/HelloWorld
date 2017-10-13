package com.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import com.example.appdatasources.R;
import com.model.MBlueTooth;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener ,OnClickListener{
	private TextView vDevice, vStatus,vTopic,vBack;
	private ListView lvbt;
	private Button 	 vSwitch;
	private BluetoothAdapter mBluetoothAdapter;
	private ArrayList<Map<String, String>> listDevices;
	private SimpleAdapter mAdapter;
	private String uuid;
	// 服务端利用线程不断接受客户端信息
	private AcceptThread thread;
	private final String NAME = "Bluetooth_Socket";
	// 选中发送数据的蓝牙设备，全局变量，否则连接在方法执行完就结束了
	private BluetoothDevice selectDevice;
	// 获取到选中设备的客户端串口，全局变量，否则连接在方法执行完就结束了
	private BluetoothSocket clientSocket;
	// 获取到向设备写的输出流，全局变量，否则连接在方法执行完就结束了
	private OutputStream os;
	//
	private MBlueTooth mBlueTooth;
	private Context	   mContext;
	private Builder	   vBuilder;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		uuid = "ffffffff-ea31-5a0f-0617-ee160033c587";
		initView();
		initEvent();
	}
	
	private void initView(){
		vDevice = (TextView) findViewById(R.id.tvDevice);
		vStatus = (TextView) findViewById(R.id.tvStatus);
		vTopic	= (TextView) findViewById(R.id.tvTopic);
		lvbt 	= (ListView) findViewById(R.id.lvbt);
		vSwitch = (Button) findViewById(R.id.btnSwitch);
		vBack	= (TextView) findViewById(R.id.btnBack);
	}
	
	private void initEvent(){
		mContext		  = MainActivity.this;
		mBlueTooth		  =	new MBlueTooth();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// 实例接收客户端传过来的数据线程
		thread 			  = new AcceptThread();
		// 线程开始
		thread.start();
		vTopic.setText("蓝牙——数据传输工具");
		if (mBlueTooth.isFdevice()) {
			vDevice.setText("本设备支持蓝牙");
		} else 
			vDevice.setText("本设备不支持蓝牙");
		
		if(mBlueTooth.isFbtooth()){
			vStatus.setText("开启蓝牙");
			vSwitch.setText("关闭蓝牙");
		}else{
			vStatus.setText("关闭蓝牙");
			vSwitch.setText("开启蓝牙");
		} 
		
		listDevices	=	mBlueTooth.getListDevices();
		mAdapter = new SimpleAdapter(mContext, listDevices, R.layout.item, new String[]{"name","address"},new int[]{R.id.name,R.id.address});
		lvbt.setAdapter(mAdapter);
		lvbt.setOnItemClickListener(this);
		vBack.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// 开关蓝牙按钮;
		vSwitch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mBlueTooth.isFbtooth()){
					mBlueTooth.setBlueToothClose();
					vStatus.setText("蓝牙关闭");
					vSwitch.setText("开启蓝牙");
				}else{
					mBlueTooth.setBlueToothOpen();
					vStatus.setText("蓝牙开启");
					vSwitch.setText("关闭蓝牙");
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		// 对其进行分割，获取到这个设备的地址
		String address = listDevices.get(position).get("address");
		// 判断当前是否还是正在搜索周边设备，如果是则暂停搜索
		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}

		// 如果选择设备为空则代表还没有选择设备
		if (selectDevice == null) {
			// 通过地址获取到该设备
			selectDevice = mBluetoothAdapter.getRemoteDevice(address);
		}
		// 这里需要try catch一下，以防异常抛出
		try {
			
			// 判断客户端接口是否为空
			if (clientSocket == null) {
				// 获取到客户端接口
				clientSocket = selectDevice.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
				// 向服务端发送连接
				clientSocket.connect();
				// 获取到输出流，向外写数据
				os = clientSocket.getOutputStream();

			}
			// 判断是否拿到输出流
			if (os != null) {
				// 需要发送的信息
				String text = "t12380011121314151617\r";
				// 以utf-8的格式发送出去
				os.write(text.getBytes("UTF-8"));
			}
			// 吐司一下，告诉用户发送成功
			Toast.makeText(this, "发送信息成功，请查收", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 如果发生异常则告诉用户发送失败
			Toast.makeText(this, "发送信息失败", Toast.LENGTH_SHORT).show();
		}

	}

	// 服务端接收信息线程
	// 创建handler，因为我们接收是采用线程来接收的，在线程中无法操作UI，所以需要handler
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// 将信息显示;
			Toast.makeText(MainActivity.this, (String) msg.obj,
					Toast.LENGTH_SHORT).show();
		}
	};

	public class AcceptThread extends Thread {
		private BluetoothServerSocket serverSocket;// 服务端接口
		private BluetoothSocket socket;// 获取到客户端的接口
		private InputStream is;// 获取到输入流
		@SuppressWarnings("unused")
		private OutputStream os;// 获取到输出流

		public AcceptThread() {
			try {
				// 通过UUID监听请求，然后获取到对应的服务端接口
				serverSocket = mBluetoothAdapter
						.listenUsingRfcommWithServiceRecord(NAME,
								UUID.fromString(uuid));
			} catch (Exception e) {

			}
		}

		public void run() {
			try {
				while (true) {
					// 接收其客户端的接口
					socket = serverSocket.accept();
					// 获取到输入流
					is = socket.getInputStream();
					// 获取到输出流
					os = socket.getOutputStream();

					// 无线循环来接收数据
					while (true) {
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
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onClick(View view) {
		int nId=view.getId();
		
		switch (nId) {
		case R.id.btnBack:
			vBuilder=new Builder(mContext);
			vBuilder.setTitle("退出");
			vBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
						finish();
				}
			});
			vBuilder.create();
			vBuilder.show();
			break;

		default:
			break;
		}
		
	}
}
