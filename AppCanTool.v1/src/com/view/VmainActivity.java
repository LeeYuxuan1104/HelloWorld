package com.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.example.appcantool.R;
import com.model.MBlueTooth;
import com.model.ReceiveData;

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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VmainActivity extends Activity implements OnClickListener{
	private TextView vTopic, vBack;
	private Button vRecive, vTrans, vLoad, vSet;
	private ListView vListView;
	private Context	mContext;
	
	private MBlueTooth	 mBlueTooth;
	private AcceptThread acceptThread;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		initView();
		initEvent();
	}

	private void initView() {
		vTopic = (TextView) findViewById(R.id.tvTopic);
		vBack = (TextView) findViewById(R.id.btnBack);
		vRecive = (Button) findViewById(R.id.btnReceive);
		vRecive.setText("接收");
		vTrans = (Button) findViewById(R.id.btnTrans);
		vLoad = (Button) findViewById(R.id.btnLoad);
		vSet = (Button) findViewById(R.id.btnSet);
		vListView = (ListView) findViewById(R.id.listView);

	}

	private void initEvent() {
		mContext=VmainActivity.this;
		vTopic.setText("CanTool 工具应用App");
		vBack.setOnClickListener(this);
		vRecive.setOnClickListener(this);
		mBlueTooth=new MBlueTooth();
		acceptThread=new AcceptThread(mBlueTooth.getmBluetoothAdapter());
		acceptThread.start();
	}

	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnReceive:
			String rSwitch=vRecive.getText().toString();
			// 创建启动Service的Intent

			Intent intent = new Intent();
			intent.setClass(mContext, ReceiveData.class);
			if(rSwitch.equals("接收")){				
				// 为Intent设置Action属性
				startService(intent);
				vRecive.setText("关闭");
			}else{				
				// 停止指定Serivce
				stopService(intent);
				vRecive.setText("接收");
			}
			break;
		default:
			break;
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// 将信息显示;
			Toast.makeText(mContext, (String) msg.obj,Toast.LENGTH_SHORT).show();
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
}
