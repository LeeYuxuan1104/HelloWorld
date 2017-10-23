package com.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class VSendActivity extends Activity implements OnClickListener{
	/*上下文声明*/
	private Context	mContext;
	private Intent	mIntent;
	/*控件的声明*/
	private TextView vTopic;
	private Button 	 vBack,vReceive,vShow,vSend,vManage;
	private Builder	 vBuilder;
	
	/*参量的声明*/
	
	/*定义类声明*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vsend);
		initView();
		initEvent();
	}
	//	初始化控件;
	private void initView(){
		vBack=(Button) findViewById(R.id.btnBack);
		vTopic=(TextView) findViewById(R.id.tvTopic);
		vReceive=(Button) findViewById(R.id.btnReceive);
		vShow=(Button) findViewById(R.id.btnShow);
		vSend=(Button) findViewById(R.id.btnSend);
		vManage=(Button) findViewById(R.id.btnManage);
	}
	//	初始化方法;
	private void initEvent(){
		//	上下文初始化;
		mContext=VSendActivity.this;
		vTopic.setText(R.string.tip_send);
		
		//	下方按钮添加事件监听;
		vBack.setOnClickListener(this);
		vReceive.setOnClickListener(this);
		vShow.setOnClickListener(this);
		vSend.setOnClickListener(this);
		vManage.setOnClickListener(this);
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
			mIntent=new Intent(mContext, VReceiveActivity.class);
			startActivity(mIntent);
//			finish();
			break;
		case R.id.btnShow:
			mIntent=new Intent(mContext, VShowActivity.class);
			startActivity(mIntent);
//			finish();
			break;
		case R.id.btnSend:
		
			break;
		case R.id.btnManage:
			mIntent=new Intent(mContext, VManageActivity.class);
			startActivity(mIntent);
//			finish();
			break;
		default:
			break;
		}
		
	}

}
