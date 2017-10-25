package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.model.tool.MTDBHelper;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

public class VShowActivity extends Activity implements OnClickListener{
	/*上下文声明*/
	private Context	mContext;
	private SimpleAdapter mAdapter;
	/*控件的声明*/
	private TextView vTopic;
	private Button 	 vBack,vFlush,vDelAll,vCompress,vLine;
	private ListView vListView;
	/*参量的声明*/
	private ArrayList<Map<String, String>> list;
	/*定义类声明*/
	private MTDBHelper helper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vshow);
		initView();
		initEvent();
	}
	//	初始化控件;
	private void initView(){
		vBack=(Button) findViewById(R.id.btnBack);
		vTopic=(TextView) findViewById(R.id.tvTopic);
		vListView=(ListView) findViewById(R.id.lvShow);
		
		vFlush=(Button) findViewById(R.id.btnFlush);
		vDelAll=(Button) findViewById(R.id.btnDelAll);
		
		vCompress=(Button) findViewById(R.id.btnCompress);
		vLine=(Button) findViewById(R.id.btnLine);
		
	}
	//	初始化方法;
	private void initEvent(){
		//	上下文初始化;
		mContext=VShowActivity.this;
		vTopic.setText(R.string.tip_show);
		vBack.setText(R.string.act_back);
		vCompress.setVisibility(View.VISIBLE);
		vLine.setVisibility(View.VISIBLE);
		//	下方按钮添加事件监听;
		vBack.setOnClickListener(this);
		vFlush.setOnClickListener(this);
		vDelAll.setOnClickListener(this);
		vCompress.setOnClickListener(this);
		vLine.setOnClickListener(this);
		//	进行列表;
		list=new ArrayList<Map<String, String>>();
		//	进行声明;
		helper=new MTDBHelper(mContext);
		showData();
	}
	@Override
	public void onClick(View view) {
		int pId=view.getId();
		switch (pId) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnFlush:
			showData();
			break;
		case R.id.btnDelAll:
			String sql="delete from mess_info";
			helper.oper(sql);
			showData();
			break;
		case R.id.btnCompress:
			Toast.makeText(mContext, "罗盘", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnLine:
			Toast.makeText(mContext, "曲线", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	private void showData(){
		list=loadData();
		mAdapter=new SimpleAdapter(mContext, list, R.layout.act_itemtable, new String[]{"time","chn","id","name","dir","dlc","data"}, new int[]{R.id.time,R.id.chn,R.id.id,R.id.name,R.id.dir,R.id.dlc,R.id.data});
		vListView.setAdapter(mAdapter);
	}
	
	
	private ArrayList<Map<String, String>> loadData(){
		ArrayList<Map<String, String>> list=new ArrayList<Map<String,String>>();
		String sql="select * from mess_info";
		ArrayList<String[]> datas=helper.query(sql);
		for(String[] items:datas){
			Map<String, String> map=new HashMap<String, String>();
			map.put("time", items[1]);
			map.put("chn", items[2]);
			map.put("id", items[3]);
			map.put("name", items[4]);
			map.put("dir", items[5]);
			map.put("dlc", items[6]);
			map.put("data", items[7]);
			list.add(map);
		}
		
		return list;
		
	}
	
}
