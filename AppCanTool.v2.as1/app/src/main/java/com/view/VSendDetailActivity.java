package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.model.tool.MTBlueTooth;
import com.model.tool.MTDBHelper;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class VSendDetailActivity extends Activity implements OnClickListener{
	/*上下文声明*/
	private Context	mContext;
	private Intent	mIntent;
	private Bundle	mBundle;
	/*控件的声明*/
	private TextView vTopic,vAddress;

	private Button 	 vBack,vSend;
	private ListView vlvSignals;
	private SimpleAdapter mSignalAdapter;
	/*参量的声明*/
	private ArrayList<Map<String, String>> listSignals;
	
	
	/*定义类声明*/
	private MTDBHelper  mtdbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vsend_detail);
		initView();
		initEvent();
	}
	//	初始化控件;
	private void initView(){
		vBack=(Button) findViewById(R.id.btnBack);
		vTopic=(TextView) findViewById(R.id.tvTopic);
		vAddress=(TextView) findViewById(R.id.tvAddress);
		vlvSignals= (ListView) findViewById(R.id.lvSignals);
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
		//	进行内容的编辑;
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		int pId=bundle.getInt("id");
		String pAddress=bundle.getString("address");
		vAddress.setText(pAddress);
		listSignals=loadData(pId);
		mSignalAdapter=new SimpleAdapter(mContext, listSignals, R.layout.act_item_send, new String[]{"signal_name","way","judge","rank"}, new int[]{R.id.signal_name,R.id.way,R.id.judge,R.id.rank});
		vlvSignals.setAdapter(mSignalAdapter);
	}
	@Override
	public void onClick(View view) {
		int pId=view.getId();
		switch (pId) {
		case R.id.btnBack:
			finish();
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

	
}
