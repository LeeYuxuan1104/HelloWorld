package com.view;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.model.tool.MTDBHelper;
import com.model.tool.MTDrawChart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class VDrawLineActivity extends Activity implements OnClickListener{
	/*上下文的内容*/
	private Context	mContext;
	//	控件控制管理;
	private Handler mHandler;
	/*控件内容的声明*/
	private LinearLayout vLayout;
	private Spinner vSpDrawLines;
	private Button	vBack;
	private TextView vTopic;
	private SimpleAdapter mAdapter;
	
	/*参数定义*/
	private String 	name,
					id;
	private int 	column;
	private ArrayList<Map<String, String>> list;
	
	/*自定义的类*/
	//	相应的控件内容;
	private MTDrawChart vMtDrawChart;
	private MTDBHelper  mHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_drawline);
		initView();
		initEvent();
	}
	/*初始化布局*/
	private void initView() {  
        vLayout		 = (LinearLayout) findViewById(R.id.root);
        vBack		 = (Button) findViewById(R.id.btnBack);
        vTopic		 = (TextView) findViewById(R.id.tvTopic);
        vSpDrawLines = (Spinner) findViewById(R.id.spDrawLines);
	}  
	/*初始化事件*/ 
	private void initEvent(){
		mContext=VDrawLineActivity.this;
		vBack.setText(R.string.act_back);
		vTopic.setText(R.string.act_line);
		vBack.setOnClickListener(this);
		
		
		//	句柄程序;
		Intent	intent=getIntent();
		Bundle	bundle=intent.getExtras();
		name		  =bundle.getString("name");
		id			  =bundle.getString("id");
		column		  =bundle.getInt("column");
		
		list		  =loadSignals();
		mAdapter	  =new SimpleAdapter(mContext, list, R.layout.act_item2, new String[]{"name"}, new int[]{R.id.content});
		
		vSpDrawLines.setAdapter(mAdapter);
		
		vSpDrawLines.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				String name=list.get(position).get("id");
				ArrayList<Integer> listSignal=queryValue(name);
				operChart(listSignal);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

				
			}
		});
	}
	private ArrayList<Integer> queryValue(String signal_name){
		ArrayList<Integer> list=new ArrayList<Integer>();
		mHelper=new MTDBHelper(mContext);
		String sql="select value from signalinfo where name='"+signal_name+"'";
		ArrayList<String[]> datas= mHelper.query(sql);
		
		for(String[] items:datas){
			String value=items[0];
			int nValue=Integer.parseInt(value);
			list.add(nValue);
			
		}
		mHelper.closedb();
		return list;
	} 
	
	private void operChart(ArrayList<Integer> datas){
		//	画图的动态的类;
		vMtDrawChart = new MTDrawChart(this,datas);  
		//	初始化画图的类;
		vMtDrawChart.invalidate();
		//	将布局插入到类中;
		vLayout.addView(vMtDrawChart);	
		
		mHandler = new Handler();
		//	时间线程的开关程序;
		mHandler.post(new TimerProcess());	
	}
	
	private ArrayList<Map<String, String>> loadSignals(){
		ArrayList<Map<String, String>> list=	new ArrayList<Map<String,String>>();
		String 			  sql =	"select sg_flag,signal_name from can_signal where id="+id;
		mHelper				  = new MTDBHelper(mContext);
		ArrayList<String[]> datas=mHelper.query(sql);
		for(String[] items:datas){
			String sg_flag		=items[0];
			String signal_name	=items[1];
			String name			=sg_flag+signal_name;
			Map<String, String> map=new HashMap<String, String>();
			map.put("id", signal_name);
			map.put("name", name);
			list.add(map);
		}
		mHelper.closedb();
		return list;
	}
	
	private class TimerProcess implements Runnable {
		public void run() {
			vMtDrawChart.invalidate();
			mHandler.postDelayed(this, 1000);
		}
	}

	@Override
	public void onClick(View view) {
		int vId=view.getId();
		switch (vId) {
		case R.id.btnBack:
			finish();
			break;
		
		default:
			break;
		}
		
	}
}
