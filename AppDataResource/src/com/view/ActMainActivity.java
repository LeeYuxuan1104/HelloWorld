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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;


@SuppressLint("HandlerLeak")
public class ActMainActivity extends Activity implements OnClickListener{
	//	上下文;
	private Context	   		mContext;
	//	标题控件;
	private TextView   		vback,vtopic,vdevice,vstatus;
	//	按钮;
	private Button	   		vbtooth;
	//	列表;
	private ListView   		vlist;
	//	对话框;
	private Builder	   		vbuilder;
	private MBlueTooth 	    mBlueTooth;
	private ArrayList<Map<String, String>> listDevices=null;
	private SimpleAdapter	mAdapter;
	private ProgressDialog  mDialog; // 对话方框;
	
	private final String uUid = "ffffffff-ea31-5a0f-0617-ee160033c587";
	private final String NAME = "Bluetooth_Socket";
	// 服务端利用线程不断接受客户端信息
	private AcceptThread 	athread;
	private MyThread		mthread;
	// 获取到向设备写的输出流，全局变量，否则连接在方法执行完就结束了
//	private OutputStream os;
	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int nFlag = msg.what;
			mDialog.dismiss();
			switch (nFlag) {
			// 01.成功;
			case 1:
				Toast.makeText(mContext, R.string.tip_success,
						Toast.LENGTH_SHORT).show();
				break;
			// 02.失败;
			case 0:
				Toast.makeText(mContext, R.string.tip_fail, Toast.LENGTH_LONG)
						.show();
				break;
			default:
				break;
			}
			closeThread();
		}
	};
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
		//	列表监听;
		vlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, final int position,
					long id) {
				vbuilder = new Builder(mContext);
				vbuilder.setTitle("参数信息");
				final EditText edit = new EditText(mContext);
				edit.setSingleLine(false);
				edit.setLines(6);
				vbuilder.setView(edit);
				vbuilder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0,
									int arg1) {
								String tmp = edit.getText().toString()
										.trim();
								if (!tmp.equals("")) {
									// 对其进行分割，获取到这个设备的地址
									if(mthread==null){
										// 进度条的内容;
										final CharSequence strDialogTitle = getString(R.string.tip_dialog_wait);
										final CharSequence strDialogBody = getString(R.string.tip_dialog_done);
										mDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
										mthread	= new MyThread(listDevices, position, mBlueTooth, uUid,tmp);
										mthread.start();
									}
								}
							}
						});
				vbuilder.setNegativeButton("取消", null);
				vbuilder.create();
				vbuilder.show();
				
			}
		});
	}
	//	生命周期03——消亡
	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeThread();
		finish();
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
		
		//	上下文赋值;
		mContext=ActMainActivity.this;
		//	返回键监听;
		vback.setOnClickListener(this);
		//	蓝牙键监听;
		vbtooth.setOnClickListener(this);
		//	标题栏命名;
		vtopic.setText("蓝牙——数据源工具");
		mBlueTooth= new MBlueTooth();
		
		// 实例接收客户端传过来的数据线程
		athread   = new AcceptThread();
		// 线程开始
		athread.start();

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
		listDevices	=	mBlueTooth.getListDevices();
		mAdapter	=	new SimpleAdapter(mContext, listDevices, R.layout.item,new String[]{"name","address"}, new int[]{R.id.name,R.id.address});
		vlist.setAdapter(mAdapter);
	}

	// 创建handler，因为我们接收是采用线程来接收的，在线程中无法操作UI，所以需要handler
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 通过msg传递过来的信息，吐司一下收到的信息
			Toast.makeText(mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
		}
	};
	public class AcceptThread extends Thread {
		private BluetoothServerSocket serverSocket;// 服务端接口
		private BluetoothSocket 	  socket;	   // 获取到客户端的接口
		private InputStream 		  is;	 	   // 获取到输入流
//		private OutputStream 		  os;
		
		public void run() {
			try {
				serverSocket = mBlueTooth.getBluetoothAdapter().listenUsingRfcommWithServiceRecord(NAME,UUID.fromString(uUid));
				// 接收其客户端的接口
				socket 		 = serverSocket.accept();
				// 获取到输入流
				is 	   		 = socket.getInputStream();
				// 获取到输出流
//				os 	   		 = socket.getOutputStream();

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
			} catch (Exception e) {
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
					closeThread();
					if(athread!=null){
						athread.interrupt();
						athread=null;
					}
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
	public class MyThread extends Thread{
		private MBlueTooth 		mBlueTooth	=null;
		private ArrayList<Map<String, String>> list;
		private String 			uUid,param;
		private int position;
		public MyThread(ArrayList<Map<String, String>> list,int position,MBlueTooth mBlueTooth,String uUid,String param) {
			this.list=list;
			this.position=position;
			this.mBlueTooth=mBlueTooth;
			this.uUid=uUid;
			this.param=param;
		}
		
		@Override
		public void run() {
			BluetoothDevice selectDevice= null;
			BluetoothSocket clientSocket= null;
			OutputStream 	os			= null;
			String 			address 	= list.get(position).get("address");
			Log.i("MyLog", "address="+address);
			int nFlag=0;
			// 判断当前是否还是正在搜索周边设备，如果是则暂停搜索
			if (mBlueTooth.getBluetoothAdapter().isDiscovering()) {
				mBlueTooth.getBluetoothAdapter().cancelDiscovery();
			}
			// 如果选择设备为空则代表还没有选择设备
			if (selectDevice == null) {
				// 通过地址获取到该设备
				selectDevice = mBlueTooth.getBluetoothAdapter().getRemoteDevice(address);
				Log.i("MyLog", "selectDevice0="+selectDevice);
			}
			// 这里需要try catch一下，以防异常抛出
			try {
				Log.i("MyLog", "clientSocket0="+clientSocket);
				// 判断客户端接口是否为空
				if (clientSocket == null) {
					Log.i("MyLog", "clientSocket1="+clientSocket);
					// 获取到客户端接口
					clientSocket = selectDevice.createRfcommSocketToServiceRecord(UUID.fromString(uUid));
					Log.i("MyLog", "clientSocket2="+clientSocket);
					// 向服务端发送连接
					clientSocket.connect();
					// 获取到输出流，向外写数据
					os = clientSocket.getOutputStream();
					Log.i("MyLog", "os1="+os);
				}
				// 判断是否拿到输出流
				if (os != null) {
					Log.i("MyLog", "os2="+os);
					// 以utf-8的格式发送出去
					os.write(param.getBytes("UTF-8"));
					nFlag=1;
				}
			} catch (IOException e) {
				
				e.printStackTrace();
				nFlag=0;
			}
			mHandler.sendEmptyMessage(nFlag);
		}
	}
	private void closeThread() {
		if (mthread != null) {
			mthread.interrupt();
			mthread = null;
		}
	}
}
