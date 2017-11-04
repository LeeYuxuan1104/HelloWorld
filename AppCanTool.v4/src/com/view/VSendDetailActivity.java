package com.view;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.controller.deal.CTransCode;
import com.model.entity.MECoding;
import com.model.tool.MTBlueTooth;
import com.model.tool.MTDBHelper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class VSendDetailActivity extends Activity implements OnClickListener{
	/*上下文声明*/
	private Context	mContext;
	private Builder mBuilder;
	/*控件的声明*/
	private TextView vTopic,vAddress;
	private EditText vCreate,vTime;
	private Button 	 vBack,vSend,vCode;
	private ListView vlvSignals;
	private SimpleAdapter mSignalAdapter;
	/*参量的声明*/
	private ArrayList<Map<String, String>> listSignals;
	private int 	 canId;
	private String   pAddress;
	/*定义类声明*/
	private MTDBHelper  mtdbHelper;
	private CTransCode  mCTransCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vsend_detail);
		initView();
		initEvent();
	}
	//	初始化控件;
	private void initView(){
		/*触发按钮的控件*/
		//	返回键
		vBack	=	(Button) findViewById(R.id.btnBack);
		//	标题键
		vTopic	=	(TextView) findViewById(R.id.tvTopic);
		//	COMM地址;
		vAddress=	(TextView) findViewById(R.id.tvAddress);
		vlvSignals= (ListView) findViewById(R.id.lvSignals);
		//	编码键;
		vCode	=	(Button) findViewById(R.id.btnCode);
		vSend	=	(Button) findViewById(R.id.btnSend);
		//	显示位置;
		vCreate	= 	(EditText) findViewById(R.id.etCreate);
		//	时间补位;
		vTime	= 	(EditText) findViewById(R.id.etTime);
		mCTransCode=new CTransCode(mContext);
	}
	//	初始化方法;
	private void initEvent(){
		//	上下文初始化;
		mContext=VSendDetailActivity.this;
		mtdbHelper=new MTDBHelper(mContext);
		vTopic.setText(R.string.tip_send);
		vBack.setText(R.string.act_back);
		//	下方按钮添加事件监听;
		vBack.setOnClickListener(this);
		vCode.setOnClickListener(this);
		vSend.setOnClickListener(this);
		//	进行内容的编辑;
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		canId			 =bundle.getInt("id");
		pAddress		 =bundle.getString("address");
		vAddress.setText(pAddress);
		listSignals=loadData(canId);
		mSignalAdapter=new SimpleAdapter(mContext, listSignals, R.layout.act_item_send, new String[]{"signal_name","way","judge","rank"}, new int[]{R.id.signal_name,R.id.way,R.id.judge,R.id.rank});
		vlvSignals.setAdapter(mSignalAdapter);
	}
	@Override
	public void onClick(View view) {
		int pId=view.getId();
		switch (pId) {
		//	返回按钮;
		case R.id.btnBack:
			finish();
			break;
		// 	编码按钮;
		case R.id.btnCode:
			
			ArrayList<MECoding> list=catchinfo(vlvSignals, listSignals.size());
			String result	=mCTransCode.compute(list,canId);
			String time10	=vTime.getText().toString();
			int    nTime10	=0;
			try {
				nTime10		=Integer.parseInt(time10);
			} catch (Exception e) {
				nTime10		=0;
			}
			String time16	=Integer.toHexString(nTime10);
			result			=result+get4bit(time16);
			vCreate.setText(result);
			break;
		//	发送按钮;
		case R.id.btnSend:
			mBuilder=new Builder(mContext);
			mBuilder.setTitle(R.string.act_ok);
			
			mBuilder.setPositiveButton(R.string.act_ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					MTBlueTooth mtBlueTooth=new MTBlueTooth();
					BluetoothAdapter bluetoothAdapter= mtBlueTooth.getmBluetoothAdapter();
					ClientThread clientThread=new ClientThread(bluetoothAdapter, pAddress,vCreate.getText().toString());
					clientThread.start();
				}
			});
			mBuilder.setNegativeButton(R.string.act_no, null);
			mBuilder.create();
			mBuilder.show();
			break;
		default:
			break;
		}
	}
	private ArrayList<Map<String, String>> loadData(int pId){
		ArrayList<Map<String, String>> list=new ArrayList<Map<String,String>>();
		String sql="select * from can_signal where id="+pId;
		ArrayList<String[]> datas=mtdbHelper.query(sql);
		for(String[] items:datas){
			
			String signal_name  =items[2];
			String way	    	=items[3];
			String judge		=items[4];
			String rank	    	=items[5];
			

			Map<String, String> map=new HashMap<String, String>();
			map.put("signal_name", signal_name);
			map.put("way", way);
			map.put("judge", judge);
			map.put("rank", rank);
			list.add(map);
		}
		return list;
	}

	//	获取控件中的相应的内容;
	private ArrayList<MECoding> catchinfo(ListView listView,int size){
		ArrayList<MECoding> list=new ArrayList<MECoding>();
		for(int i=0;i<size;i++){			
			
			EditText vValue	=   (EditText) listView.getChildAt(i).findViewById(R.id.value);

			TextView vWay	=	(TextView) listView.getChildAt(i).findViewById(R.id.way);
			TextView vJudge	=	(TextView) listView.getChildAt(i).findViewById(R.id.judge);
			TextView vRank	=	(TextView) listView.getChildAt(i).findViewById(R.id.rank);
			
			String value=vValue.getText().toString();
			if(value.equals("")){
				value="0";
			}

			String way=vWay.getText().toString();
			String judge=vJudge.getText().toString();
			String rank=vRank.getText().toString();
//			Log.i("MyLog", "value="+value+"|way="+way+"|judge="+judge+"|rank="+rank);
			MECoding meCoding=new MECoding(Double.parseDouble(value), way, judge, rank, canId);
			list.add(meCoding);
		}
		return list;
	}
	
	//////
	public class ClientThread extends Thread{
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothDevice selectDevice;
	private BluetoothSocket mmSocket,tmpSocket;
    private String uuid = "00001101-0000-1000-8000-00805F9B34FB";
    private OutputStream os;
    private String content;
	public ClientThread(BluetoothAdapter bluetoothAdapter,String address,String content) {
		this.bluetoothAdapter=bluetoothAdapter;
		tmpSocket=null;
		this.content=content;
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
		if (bluetoothAdapter.isDiscovering()) {
			bluetoothAdapter.cancelDiscovery();
		}
		try {
			
	        if(mmSocket!=null){		        	 
	        	mmSocket.connect();
	        	// 获取到输出流，向外写数据
				os = mmSocket.getOutputStream();
	        }
	        if(os!=null){
	        	Log.i("MyLog", "连接成功!");
	        	// 需要发送的信息
				String text = content;
				// 以utf-8的格式发送出去
				os.write(text.getBytes("UTF-8"));
	        }else Log.i("MyLog", "未连接成功!");
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
			Log.i("MyLog", "有误有误");
		}
	}
}
	private String get4bit(String str){
		char[] chs=str.toCharArray();
		int nSize=chs.length;
		String tmp="";
		for(int i=0;i<4-nSize;i++){
			tmp+="0";
		}
		return tmp+str;
	}
}
