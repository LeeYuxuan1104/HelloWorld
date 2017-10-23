package com.view;

import com.model.MBlueTooth;
import com.model.SPHelper;
import com.service.ReceiveDataService;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

public class VReceiveActivity extends Activity implements OnClickListener{
	/*上下文声明*/
	private Context			mContext;
	private Intent			mIntent;
	/*控件的声明*/
	private TextView 		vTopic,
					 		vDevice,
					 		vBTooth,
					 		vReceiveState;
	private Button 	 		vBack,vReceive,vShow,vSend,vManage;
	private Builder	 		vBuilder;
	private ProgressBar 	vLoading;
	private ListView 		vlvReceive;
	
	/*参量的声明*/ 
	private String 	 		sDevice,sBTooth;
	private int 	 		pCount;
	private Intent			intentInfo;
	private String   		pTargetToService="com.from.activity.to.service";
	private String   		pTargetFromService="com.from.service.to.activity";
	
	/*定义类声明*/
	private MBlueTooth		mBlueTooth;
	private GetInfoReceiver	getInfoReceiver;
	private IntentFilter 	getInfoFilter;
	//	首选项的类;

	
	//	01.生命周期——创建;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vreceive);
		//	初始化控件;
		initView();
		//	初始化方法;
		initEvent();
	}
	//	02.生命周期——时刻唤醒;
	@Override
	protected void onResume() {
		super.onResume();
		checkDevice();
	}
	//	初始化控件;
	private void initView(){
		vBack=(Button) findViewById(R.id.btnBack);
		vTopic=(TextView) findViewById(R.id.tvTopic);
		vReceive=(Button) findViewById(R.id.btnReceive);
		vShow=(Button) findViewById(R.id.btnShow);
		vSend=(Button) findViewById(R.id.btnSend);
		vManage=(Button) findViewById(R.id.btnManage);
		//
		vDevice=(TextView) findViewById(R.id.tvDevice);
		vBTooth=(TextView) findViewById(R.id.tvBTooth);
		vReceiveState=(TextView) findViewById(R.id.tvReceiveState);
		vLoading=(ProgressBar) findViewById(R.id.pbLoading);
		//
		vlvReceive=(ListView) findViewById(R.id.lvReceive);
	}
	//	初始化方法;
	private void initEvent(){
		//	上下文初始化;
		mContext=VReceiveActivity.this;
		//	首选项存储
		vTopic.setText(R.string.tip_receive);

		//	下方按钮添加事件监听;
		vBack.setOnClickListener(this);
		vReceive.setOnClickListener(this);
		vShow.setOnClickListener(this);
		vSend.setOnClickListener(this);
		vManage.setOnClickListener(this);
		//	蓝牙设备的初始化;
		mBlueTooth=new MBlueTooth();
		//	注册广播信号;
		getInfoReceiver = new GetInfoReceiver();
		getInfoFilter 	= new IntentFilter();

		getInfoFilter.addAction(pTargetFromService);
		registerReceiver(getInfoReceiver, getInfoFilter);
		//	辅助类;
	}
	@Override
	public void onClick(View view) {
		int pId=view.getId();
		switch (pId) {
		case R.id.btnBack:
			vBuilder=new Builder(mContext);
			vBuilder.setTitle(R.string.tip_exit);
			vBuilder.setPositiveButton(R.string.act_ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {

					finish();
				}
			});
			vBuilder.setNegativeButton(R.string.act_no, null);
			vBuilder.create();
			vBuilder.show();
			break;
		case R.id.btnReceive:
			if(intentInfo==null){				
				intentInfo=new Intent(mContext, ReceiveDataService.class);
				startService(intentInfo);
//				Intent i=new Intent();
				intentInfo.putExtra("flag", 1);
				intentInfo.setAction(pTargetToService);
				sendBroadcast(intentInfo);
			}
//
//			if(pCount==0){
//				if(mBlueTooth.isBlueToothOpen()){
//					mIntent = new Intent(mContext,ReceiveDataService.class);
//					vReceive.setText("停止接收");
//					vReceiveState.setText(R.string.tip_receiving);
//					vLoading.setVisibility(View.VISIBLE);
//
//				}else Toast.makeText(mContext, "请先打开蓝牙按钮",Toast.LENGTH_SHORT).show();
//				
//			}else{
//				if(mIntent!=null){					
//					stopService(mIntent);
//					vReceive.setText("接收信息");
//					vReceiveState.setText(R.string.tip_received);
//					vLoading.setVisibility(View.INVISIBLE);
//					Intent intent = new Intent();
//					
//					intent.setAction("lovefang.01");//
//					sendBroadcast(intent);
//					
//					pCount=0;
//				}
//			}

			break;
		case R.id.btnShow:
			mIntent=new Intent(mContext, VShowActivity.class);
			startActivity(mIntent);
			finish();
			break;
		case R.id.btnSend:
			mIntent=new Intent(mContext, VSendActivity.class);
			startActivity(mIntent);
			break;
		case R.id.btnManage:
			mIntent=new Intent(mContext, VManageActivity.class);
			startActivity(mIntent);
			break;
		default:
			break;
		}
	}
	/*自定义的方法*/
	//	本功能自带方法;
	//	01.检测蓝牙设备;
	private void checkDevice(){
		sDevice			  = "无蓝牙设备";
		sBTooth			  = "蓝牙关闭";
		if (mBlueTooth.hasBlueToothDevice()) {
			sDevice="有蓝牙设备";
		}
		vDevice.setText(sDevice);
		
		if(mBlueTooth.isBlueToothOpen()){
			sBTooth	="蓝牙开启";
		}
		vBTooth.setText(sBTooth);

//		//	计数为1;
//		if(pCount==1){
//			vReceive.setText(R.string.act_receive);
//			vReceiveState.setText(R.string.tip_received);
//			vLoading.setVisibility(View.INVISIBLE);
//		}
//		//	计数非1;
//		else{
//			vReceive.setText(R.string.act_received);
//			vReceiveState.setText(R.string.tip_receiving);
//			vLoading.setVisibility(View.VISIBLE);
//		}
	}
	//	数据进行注册;
	public class GetInfoReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent){

			pCount=(Integer) intent.getExtras().get("count");

			if(pCount==0){
				vReceive.setText("接收信息");
				vReceiveState.setText(R.string.tip_received);
				vLoading.setVisibility(View.INVISIBLE);
			}else{
				vReceive.setText("停止接收");
				vReceiveState.setText(R.string.tip_receiving);
				vLoading.setVisibility(View.VISIBLE);
			}
			
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		if(broadcastReceiver!=null){			
//			unregisterReceiver(broadcastReceiver);
//		}
		
	}
}
