package com.controller.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.controller.deal.CAnalData;
import com.controller.deal.CImportData;
import com.controller.deal.CTransData;
import com.model.entity.MEData;
import com.model.tool.MTBlueTooth;
import com.model.tool.MTDBHelper;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.IBinder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class ReceiveDataService extends Service {
	private Context				 mContext;
	private boolean    		 	 threadDisable;
	private int 	   		 	 count;
	private int 				 sleeptime=1000;
	private MTBlueTooth 		 	 mBlueTooth;
	private BluetoothAdapter 	 adapter;
	private AcceptThread	 	 acceptThread;
	private CountTread		 	 countTread;
	//	向服务发送服务的链接;
	private String   		 	 pTargetToService="com.from.activity.to.service";
	//	从服务获得服务的链接;
	private String   		 	 pTargetFromService="com.from.service.to.activity";
	private GetBroadCastReceiver getBroadCastReceiver;
	private IntentFilter 	 	 filter;
	private MTDBHelper			 mHelper;
	
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onCreate() {
		super.onCreate();
		mContext	 = getApplication();
		//	蓝牙功能的声明;
		mBlueTooth 	 = new MTBlueTooth();
		//	蓝牙适配器的获取;
		adapter		 = mBlueTooth.getmBluetoothAdapter();
		mHelper		 = new MTDBHelper(mContext);
		//	发送包的内容;
		getBroadCastReceiver=new GetBroadCastReceiver();
		filter 		 = new IntentFilter();
		
		filter.addAction(pTargetToService);
		registerReceiver(getBroadCastReceiver, filter);
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
					Intent intent=new Intent();
					Bundle	bundle=new Bundle();
					bundle.putInt("count", count);
					intent.putExtras(bundle);//
					intent.setAction(pTargetFromService);//
					sendBroadcast(intent);
					Thread.sleep(sleeptime);
					count++;
				} catch (InterruptedException e) {

				}
//				Log.i("MyLog", "Count is" + count);
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
			while (threadDisable) {
				try {
					socket = mmServerSocket.accept();
					long l1=System.currentTimeMillis();
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
					String param = new String(buffer, 0, count, "utf-8");
//					Log.i("MyLog", "param="+param);
					long l2 = System.currentTimeMillis();
					// 发送数据
					if(param!=null){					
						//	进行数据的抓取;
						MEData meData=new MEData();
						meData		=analData(meData,param);
						inputData(mContext,l1, l2, "no", "in", meData);
						transData(mContext, meData);
//						meData		=transData(meData);
						//	传输前台的操作;
						Intent intent=new Intent();
						Bundle	bundle=new Bundle();
						bundle.putString("info", param);
						intent.putExtras(bundle);//
						intent.setAction(pTargetFromService);//
						sendBroadcast(intent);
					}

					try {
						Thread.sleep(sleeptime);
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
		if(getBroadCastReceiver!=null){			
			unregisterReceiver(getBroadCastReceiver);
		}
		this.threadDisable = false;
	}

	public class GetBroadCastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context content, Intent intent) {
			Bundle bundle=intent.getExtras();
			boolean flag=bundle.getBoolean("flag");
			if(flag){				
				threadDisable= true;

				if(acceptThread==null){			
					acceptThread = new AcceptThread(adapter);
					acceptThread.start();
				}
				
				//	计数的线程进行开启;
				if(countTread==null){
					count=0;
					countTread=new CountTread();
					countTread.start();
				}
			}else{
				threadDisable= false;
				if(acceptThread!=null){
					acceptThread.interrupt();
					acceptThread=null;
				}
				if(countTread!=null){
					countTread.interrupt();
					countTread=null;
				}
				count=0;
				Intent i=new Intent();
				Bundle	b=new Bundle();
				b.putInt("count", 0);
				i.putExtras(bundle);//
				i.setAction(pTargetFromService);//
				sendBroadcast(i);
			}
		}
	}
	/////////////////
	//	数据解析;
	private MEData	analData(MEData  meData,String param){
		if(param!=null&&!param.equals("")){			
			CAnalData  cAnalData =new CAnalData(param);
			cAnalData.setmData(meData);
			cAnalData.computeData();
			meData	 =cAnalData.getmeData();
		}
		
		return meData;
	}
	//	数据插入库;
	private void inputData(Context context, long l1,long l2,String chn,String dir,MEData meData){
		CImportData 		cInputData	=	new CImportData(context,mHelper);
		cInputData.inputDataBase_Mes(l1, l2, chn, dir, meData);
	}
	
	//	数据转码;
	private MEData	transData(Context context, MEData meData){
		
		CTransData cTransData=new CTransData(context,mHelper);
		cTransData.setmData(meData);
		cTransData.compute();
				
		return meData;
	}

}
