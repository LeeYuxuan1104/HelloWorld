package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.model.MBlueTooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.IBinder;
import android.os.Binder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class ReceiveDataService extends Service {

	private boolean    		 threadDisable;
	private int 	   		 count;
	private Intent     		 intent;
	private MBlueTooth 		 mBlueTooth;
	private BluetoothAdapter adapter;
	private AcceptThread	 acceptThread;
	private CountTread		 countTread;
	private String   		 pTargetToService="com.from.activity.to.service";
	private String   		 pTargetFromService="com.from.service.to.activity";
	private getBroadCastReceiver broadCastReceiver;
	private IntentFilter 	 filter;
	
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onCreate() {
		super.onCreate();
		mBlueTooth 	 = new MBlueTooth();
		adapter		 = mBlueTooth.getmBluetoothAdapter();
		intent 		 = new Intent();
		broadCastReceiver=new getBroadCastReceiver();
		filter 		 = new IntentFilter();
		
		filter.addAction(pTargetToService);
		registerReceiver(broadCastReceiver, filter);
		
		//	服务端的线程进行开启;
		if(acceptThread==null){			
			acceptThread = new AcceptThread(adapter);
			acceptThread.start();
		}
		
		//	计数的线程进行开启;
		if(countTread==null){
			threadDisable= true;
			countTread=new CountTread();
			countTread.start();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	//	计数的线程;
	public class CountTread extends Thread{
		@Override
		public void run() {
			while (threadDisable) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

				}
				count++;
				intent.putExtra("count", count);//
				intent.setAction(pTargetFromService);//
				sendBroadcast(intent);
				Log.v("MyLog", "Count is" + count);
				
			}
		}
	}
	
	
	//	接收服务的线程;
	public class AcceptThread extends Thread {

		private final BluetoothServerSocket mmServerSocket;
		private final String NAME = "Bluetooth_Socket";
		private String uuid = "00001101-0000-1000-8000-00805F9B34FB";
		private InputStream is;// 获取到输入流
		@SuppressWarnings("unused")
		private OutputStream os;// 获取到输出流

		public AcceptThread(BluetoothAdapter bluetoothAdapter) {
			BluetoothServerSocket tmp = null;
			try {
				// MY_UUID is the app's UUID string, also used by the client
				// code
				tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME,
						UUID.fromString(uuid));
			} catch (IOException e) {
			}
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
					// Message msg = new Message();
					// 发送一个String的数据，让他向上转型为obj类型
					String str = new String(buffer, 0, count, "utf-8");
					// 发送数据
					// handler.sendMessage(msg);
					intent.putExtra("CONTENT", "This is a Braodcast demo");
					intent.setAction("lovefang.stadyService");
					sendBroadcast(intent);

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
			} catch (IOException e) {
			}
		}
	}

	public void onDestroy() {
		super.onDestroy();
		if(broadCastReceiver!=null){			
			unregisterReceiver(broadCastReceiver);
		}
		this.threadDisable = false;
	}

	public int getConunt() {
		return count;
	}

	class ServiceBinder extends Binder {
		public ReceiveDataService getService() {
			return ReceiveDataService.this;
		}
	}
	public class getBroadCastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(pTargetToService)){				
				Log.i("MyLog", "接收到");
				int flag=intent.getExtras().getInt("flag");
				if(flag==1){				
					Log.i("MyLog", "线程开关");
				}
				threadDisable=false;
				count=0;
			}
		}	
	}
	
}
