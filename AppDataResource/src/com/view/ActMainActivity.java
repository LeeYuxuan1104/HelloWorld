package com.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
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
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;


public class ActMainActivity extends Activity implements OnItemClickListener,OnClickListener{
	//	上下文;
	private Context	   mContext;
	//	标题控件;
	private TextView   vback,vtopic,vdevice,vstatus;
	//	按钮;
	private Button	   vbtooth;
	//	列表;
	private ListView   vlist;
	//	对话框;
	private Builder	   vbuilder;
	private MBlueTooth mBlueTooth;
	private ArrayList<Map<String, String>> listDevices=null;
	private SimpleAdapter	mAdapter;
	
	
	private String 	     uuid;
	// 服务端利用线程不断接受客户端信息
	private AcceptThread thread;
	private final String NAME = "Bluetooth_Socket";
	// 选中发送数据的蓝牙设备，全局变量，否则连接在方法执行完就结束了
	private BluetoothDevice selectDevice;
	// 获取到选中设备的客户端串口，全局变量，否则连接在方法执行完就结束了
	private BluetoothSocket clientSocket;
	// 获取到向设备写的输出流，全局变量，否则连接在方法执行完就结束了
	private OutputStream os;
	
	// 生命周期01——创建;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		//	01.初始化控件;
		initView();
		//	02.初始化监听;
		initEvent();
	}

	// 生命周期02——显示;
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	//	初始化控件;
	private void initView(){
		vback=(TextView) findViewById(R.id.btnBack);
		vtopic=(TextView) findViewById(R.id.tvTopic);
		vdevice=(TextView) findViewById(R.id.tvDevice);
		vstatus=(TextView) findViewById(R.id.tvStatus);
		vbtooth=(Button) findViewById(R.id.btnBlueTooth);
		vlist=(ListView) findViewById(R.id.lvDevices);
	}
	
	//	初始化监听;
	private void initEvent(){
		uuid = "ffffffff-ea31-5a0f-0617-ee160033c587";
		//	上下文赋值;
		mContext=ActMainActivity.this;
		//	返回键监听;
		vback.setOnClickListener(this);
		//	蓝牙键监听;
		vbtooth.setOnClickListener(this);
		//	列表监听;
		vlist.setOnItemClickListener(this);
		//	标题栏命名;
		vtopic.setText("蓝牙——数据源工具");
		mBlueTooth=new MBlueTooth(mContext);
		// 实例接收客户端传过来的数据线程
		thread = new AcceptThread();
		// 线程开始
		thread.start();

		if(mBlueTooth.isFdevice()){
			vdevice.setText("本机有蓝牙设备");
		}else 
			vdevice.setText("本机无蓝牙设备");
		
		if(mBlueTooth.isFbtooth()){
			vstatus.setText("蓝牙开启");
			vbtooth.setText("蓝牙关闭");
		}else{
			vstatus.setText("蓝牙未开启");
			vbtooth.setText("蓝牙开启");			
		} 
		listDevices=mBlueTooth.getListDevices();
		mAdapter=new SimpleAdapter(mContext, listDevices, R.layout.item,new String[]{"name","address"}, new int[]{R.id.name,R.id.address});
		vlist.setAdapter(mAdapter);
	
	}
	//	单条选项选择;
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		// 对其进行分割，获取到这个设备的地址
		String address = listDevices.get(position).get("address");
		// 判断当前是否还是正在搜索周边设备，如果是则暂停搜索
		if (mBlueTooth.getBluetoothAdapter().isDiscovering()) {
			mBlueTooth.getBluetoothAdapter().cancelDiscovery();
		}

		// 如果选择设备为空则代表还没有选择设备
		if (selectDevice == null) {
			// 通过地址获取到该设备
			selectDevice = mBlueTooth.getBluetoothAdapter().getRemoteDevice(
					address);
		}
		// 这里需要try catch一下，以防异常抛出
		try {
			Toast.makeText(mContext, "selectDevice:" + selectDevice,
					Toast.LENGTH_SHORT).show();
			// 判断客户端接口是否为空
			if (clientSocket == null) {

				// 获取到客户端接口
				clientSocket = selectDevice
						.createRfcommSocketToServiceRecord(UUID
								.fromString(uuid));
				// 向服务端发送连接
				clientSocket.connect();
				Toast.makeText(
						mContext,
						"selectDevice:" + selectDevice + "\r\nclientSocket:"
								+ clientSocket, Toast.LENGTH_SHORT).show();
				// 获取到输出流，向外写数据
				os = clientSocket.getOutputStream();

			}
			// 判断是否拿到输出流
			if (os != null) {
				// 需要发送的信息
				String text = "成功发送信息";
				// 以utf-8的格式发送出去
				os.write(text.getBytes("UTF-8"));
				Toast.makeText(mContext, "os=" + os.toString(),
						Toast.LENGTH_LONG).show();
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
	// 创建handler，因为我们接收是采用线程来接收的，在线程中无法操作UI，所以需要handler
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				// 通过msg传递过来的信息，吐司一下收到的信息
				Toast.makeText(mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
			}
		};
	public class AcceptThread extends Thread {
		private BluetoothServerSocket serverSocket;// 服务端接口
		private BluetoothSocket socket;// 获取到客户端的接口
		private InputStream is;// 获取到输入流
		private OutputStream os;// 获取到输出流

		public AcceptThread() {
			try {
				// 通过UUID监听请求，然后获取到对应的服务端接口
				serverSocket = mBlueTooth.getBluetoothAdapter().listenUsingRfcommWithServiceRecord(NAME,
								UUID.fromString(uuid));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		public void run() {
			try {
				while (true) {
				
					
//				System.out.println("xx");
					Log.i("MyLog", "xxxxx");
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
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}
	//	单点击内容;
	@Override
	public void onClick(View view) {
		int nId=view.getId();
		switch (nId) {
		case R.id.btnBack:
			vbuilder=new Builder(mContext);
			vbuilder.setTitle("退出?");
			vbuilder.setPositiveButton("确认?", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
				}
			});
			vbuilder.setNegativeButton("取消", null);
			vbuilder.create();
			vbuilder.show();
			break;
		case R.id.btnBlueTooth:
			if(mBlueTooth.isFbtooth()){
				mBlueTooth.setBlueToothClose();
				vstatus.setText("蓝牙关闭");
				vbtooth.setText("蓝牙开启");
			}else{
				mBlueTooth.setBlueToothOpen();
				vstatus.setText("蓝牙开启");
				vbtooth.setText("蓝牙关闭");			
			} 
			break;
		default:
			break;
		}	
	}
}
