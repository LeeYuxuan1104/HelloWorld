package com.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class ReceiveData extends Service{
	//
	private MBlueTooth mBlueTooth;
	//
	private AcceptThread acceptThread;
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i("MyLog", "Service onBind");
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mBlueTooth=new MBlueTooth();
		Log.i("MyLog", "Service onCreate");
		startThread();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("MyLog", "flags------->");
		Log.i("MyLog", "startId------->");
		Log.i("MyLog", "Service onStartCommand");
		return START_NOT_STICKY;
	}

	
	@Override
	public void onDestroy() {
		Log.i("MyLog", "Service onDestory");
		closeThread();
		super.onDestroy();
	}
	
	public class AcceptThread extends Thread{
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
						@SuppressWarnings("unused")
						Message msg = new Message();
						// 发送一个String的数据，让他向上转型为obj类型
						String result=new String(buffer, 0, count, "utf-8");
						Log.i("MyLog", "接收数据------>"+result);

						
		            } catch (IOException e) {
		                break;
		            }
		        }
		        
		    }
		
	} 
	
	private void startThread(){
		acceptThread=new AcceptThread(mBlueTooth.getmBluetoothAdapter());
		acceptThread.start();
	}
	private void closeThread(){
		if(acceptThread!=null){
			Log.i("MyLog", "关闭接收");
			acceptThread.interrupt();
			acceptThread=null;
		}
	}
}
