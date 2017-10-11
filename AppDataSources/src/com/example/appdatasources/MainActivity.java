package com.example.appdatasources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {
	private TextView btdevice, btstate;
	private ListView lvbt;
	private Button btBtn;
	private boolean fbtstate;
	private BluetoothAdapter mBluetoothAdapter;
	private ArrayList<String> list;
	private ArrayAdapter<String> mAdapter;
	private String uuid;
	// 服务端利用线程不断接受客户端信息
	private AcceptThread thread;
	private final String NAME = "Bluetooth_Socket";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);
		uuid = getMyUUID();
		btdevice = (TextView) findViewById(R.id.btdevice);
		btstate = (TextView) findViewById(R.id.btstate);
		lvbt = (ListView) findViewById(R.id.lvbt);

		btBtn = (Button) findViewById(R.id.btBtn);

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			btdevice.setText("本设备不支持蓝牙");
		} else {
			btdevice.setText("本设备支持蓝牙");
			fbtstate = mBluetoothAdapter.isEnabled();
			list = new ArrayList<String>();
			// 获取所有已经绑定的蓝牙设备
			Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
			if (devices.size() > 0) {
				for (BluetoothDevice device : devices) {
					list.add(device.getName() + ":" + device.getAddress());
				}
			}

			mAdapter = new ArrayAdapter<String>(MainActivity.this,
					android.R.layout.simple_dropdown_item_1line, list);
			lvbt.setAdapter(mAdapter);
			lvbt.setOnItemClickListener(this);
			if (fbtstate) {
				btstate.setText("蓝牙已开启");
				btBtn.setText("关闭蓝牙");
			} else {
				btstate.setText("蓝牙已关闭");
				btBtn.setText("开启蓝牙");
			}
			// 实例接收客户端传过来的数据线程
			thread = new AcceptThread();
			// 线程开始
			thread.start();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 开关蓝牙按钮;
		btBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				fbtstate = mBluetoothAdapter.isEnabled();
				if (fbtstate) {
					mBluetoothAdapter.disable();
					btstate.setText("蓝牙已关闭");
					btBtn.setText("开启蓝牙");
				} else {
					mBluetoothAdapter.enable();
					btstate.setText("蓝牙已开启");
					btBtn.setText("关闭蓝牙");
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// 选中发送数据的蓝牙设备，全局变量，否则连接在方法执行完就结束了
	private BluetoothDevice selectDevice;
	// 获取到选中设备的客户端串口，全局变量，否则连接在方法执行完就结束了
	private BluetoothSocket clientSocket;
	// 获取到向设备写的输出流，全局变量，否则连接在方法执行完就结束了
	private OutputStream os;

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		// 获取到这个设备的信息
		String s = mAdapter.getItem(position);
		// 对其进行分割，获取到这个设备的地址
		String address = s.substring(s.indexOf(":") + 1).trim();
		// 判断当前是否还是正在搜索周边设备，如果是则暂停搜索
		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}
		Toast.makeText(MainActivity.this, address + "|" + uuid,
				Toast.LENGTH_SHORT).show();
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
				clientSocket = selectDevice
						.createRfcommSocketToServiceRecord(UUID
								.fromString(uuid));
				// 向服务端发送连接
				clientSocket.connect();
				// 获取到输出流，向外写数据
				os = clientSocket.getOutputStream();

			}
			// 判断是否拿到输出流
			if (os != null) {
				// 需要发送的信息
				String text = "成功发送信息";
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

	private String getMyUUID() {
		final TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		Log.d("debug", "uuid=" + uniqueId);
		return uniqueId;
	}


	// 服务端接收信息线程
	// 创建handler，因为我们接收是采用线程来接收的，在线程中无法操作UI，所以需要handler
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// 通过msg传递过来的信息，吐司一下收到的信息
			Toast.makeText(MainActivity.this, (String) msg.obj, 0).show();
		}
	};

	private class AcceptThread extends Thread {
		private BluetoothServerSocket serverSocket;// 服务端接口
		private BluetoothSocket socket;// 获取到客户端的接口
		private InputStream is;// 获取到输入流
		private OutputStream os;// 获取到输出流

		public AcceptThread() {
			try {
				// 通过UUID监听请求，然后获取到对应的服务端接口
				serverSocket = mBluetoothAdapter
						.listenUsingRfcommWithServiceRecord(NAME,
								UUID.fromString(uuid));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		public void run() {
			try {
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
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}
}
