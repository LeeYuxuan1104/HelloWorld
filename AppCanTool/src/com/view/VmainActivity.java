package com.view;

import com.example.appcantool.R;
import com.model.ReceiveData;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class VmainActivity extends Activity implements OnClickListener{
	private TextView vTopic, vBack;
	private Button vRecive, vTrans, vLoad, vSet;
	private ListView vListView;
	private Context	mContext;
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

}
