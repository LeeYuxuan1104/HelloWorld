package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.model.tool.MTBlueTooth;
import com.model.tool.MTDBHelper;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class VSendActivity extends Activity implements OnClickListener{
	/*上下文声明*/
	private Context	mContext;
	/*控件的声明*/
	private TextView vTopic;
	private Button 	 vBack;
	private Spinner  vSpinner;
	private ListView lvMessages;
	private SimpleAdapter mDevicesAdapter;
	private SimpleAdapter mMessagesAdapter;	
	/*参量的声明*/
	private ArrayList<Map<String, String>> listDevices;
	private ArrayList<Map<String, String>> listMessages;
	private String 	pAddress;
	/*定义类声明*/
	private MTBlueTooth mtBlueTooth;
	private MTDBHelper  mtdbHelper;
	
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
		vSpinner=(Spinner) findViewById(R.id.spDevices);
		lvMessages=(ListView) findViewById(R.id.lvMessages);
	}
	//	初始化方法;
	private void initEvent(){
		//	上下文初始化;
		mContext=VSendActivity.this;
		vTopic.setText(R.string.tip_send);
		vBack.setText(R.string.act_back);
		//	下方按钮添加事件监听;
		vBack.setOnClickListener(this);
		mtBlueTooth=new MTBlueTooth();
		mtdbHelper =new MTDBHelper(mContext);
		listDevices=mtBlueTooth.getListDevices();
		//	显示设备的适配器;
		mDevicesAdapter=new SimpleAdapter(mContext,listDevices , R.layout.act_item, new String[]{"name","address"},new int[]{R.id.count,R.id.content});
		vSpinner.setAdapter(mDevicesAdapter);
		listMessages=loadData();
		mMessagesAdapter=new SimpleAdapter(mContext, listMessages, R.layout.act_item, new String[]{"count","id"}, new int[]{R.id.count,R.id.id});
		lvMessages.setAdapter(mMessagesAdapter);
		
		vSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				pAddress=listDevices.get(position).get("address");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		lvMessages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent=new Intent(mContext, VSendDetailActivity.class);
				Bundle bundle=new Bundle();
				int nid=Integer.parseInt(listMessages.get(position).get("id"));
				bundle.putInt("id", nid);
				bundle.putString("address", pAddress);
				intent.putExtras(bundle);
				startActivity(intent);	
			}
		});
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
	private ArrayList<Map<String, String>> loadData(){
		ArrayList<Map<String, String>> list=new ArrayList<Map<String,String>>();
		String sql="select * from can_message";
		ArrayList<String[]> datas=mtdbHelper.query(sql);
		for(String[] items:datas){
			
			String bo_flag  =items[1];
			String id	    =items[2];
			String message_name=items[3];
			String dlc	    =items[4];
			String node_name=items[5];

			Map<String, String> map=new HashMap<String, String>();
			map.put("count", bo_flag+id+" "+message_name+":"+dlc+" "+node_name);
			map.put("id", id);
			list.add(map);
		}
		return list;
	}

	
}
