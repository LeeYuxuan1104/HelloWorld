package com.tool;

import java.io.InputStream;
import java.util.UUID;

import com.model.MBlueTooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class AcceptThread extends Thread{
	private Context mContext;
	private MBlueTooth mBlueTooth;
	private String 	 conName;
	private String 	 uUid;
	public AcceptThread(Context mContext,MBlueTooth mBlueTooth,String conName,String uUid) {
		this.mContext=mContext;
		this.mBlueTooth=mBlueTooth;
		this.conName=conName;
		this.uUid=uUid;
	}
	
	// 创建handler，因为我们接收是采用线程来接收的，在线程中无法操作UI，所以需要handler
	private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// 通过msg传递过来的信息，吐司一下收到的信息
				Toast.makeText(mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
			}
		};
	public class MyThread extends Thread {
		private BluetoothServerSocket serverSocket;// 服务端接口
		private BluetoothSocket socket;// 获取到客户端的接口
		private InputStream is;// 获取到输入流
		
		public void run() {
			try {
				serverSocket = mBlueTooth.getBluetoothAdapter().listenUsingRfcommWithServiceRecord(conName,UUID.fromString(uUid));
				// 接收其客户端的接口
				socket = serverSocket.accept();
				// 获取到输入流
				is = socket.getInputStream();
				// 获取到输出流
//				os = socket.getOutputStream();

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
}
