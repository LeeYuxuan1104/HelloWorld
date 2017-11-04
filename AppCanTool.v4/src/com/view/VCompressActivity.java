package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.model.tool.MTDBHelper;
import com.model.tool.MTDialView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class VCompressActivity extends Activity implements OnClickListener{
	/*声明的对象*/
	private Context	mContext;
	/*控件的自定义*/
	private Button vBack;
	private TextView vTopic,vResult;
	private EditText vChoose;
	private Spinner	 vSpinner;
	private SimpleAdapter	mAdapter; 
	private GetInfoReceiver	getInfoReceiver;
	private IntentFilter 	getInfoFilter;
	/////////////////////
	private String     		pTargetFromService="com.from.service.to.activity";
	private String 	   		name,
					   		id,
					   		choosename
					   		;
	private int 	   		column,
							chooseposition,
							choosevalue
							;
	private ArrayList<Map<String, String>> list;
	/*自定义类*/
	private MTDBHelper	mHelper;
	private MTDialView	dialView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_compress);
		initView();
		initEvent();
	}
	
	private void initView(){
		vBack   =(Button) findViewById(R.id.btnBack);
		vTopic  =(TextView) findViewById(R.id.tvTopic);
		vResult =(TextView) findViewById(R.id.tvResult);
		vChoose =(EditText) findViewById(R.id.etChoose);
		vSpinner=(Spinner) findViewById(R.id.spSignal);
		dialView=(MTDialView) findViewById(R.id.dialView);
	}
	
	private void initEvent(){
		mContext=VCompressActivity.this;
		vBack.setText(R.string.act_back);
		vTopic.setText(R.string.act_compress);
		
		/*添加事件监听*/
		//	返回键的内容;
		vBack.setOnClickListener(this);
	
		/*与广播有关的内容*/
		getInfoReceiver = new GetInfoReceiver();
		getInfoFilter 	= new IntentFilter();
		getInfoFilter.addAction(pTargetFromService);
		registerReceiver(getInfoReceiver, getInfoFilter);
		//
		Intent	intent=getIntent();
		Bundle	bundle=intent.getExtras();
		name		  =bundle.getString("name");
		id			  =bundle.getString("id");
		column		  =bundle.getInt("column");
		/*数据信息的列表*/
		list=loadSignals();
		mAdapter=new SimpleAdapter(mContext, list, R.layout.act_item2, new String[]{"name"}, new int[]{R.id.content});
		vSpinner.setAdapter(mAdapter);
		/*进行事件的选择*/	
		vSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				String tmp=list.get(position).get("name");
				chooseposition=position;
				choosename=tmp;
				vChoose.setText(choosename+" "+chooseposition);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}

	@Override
	public void onClick(View view) {
		int vId=view.getId();
		switch (vId) {
		case R.id.btnBack:
			if(getInfoReceiver!=null){			
				unregisterReceiver(getInfoReceiver);
				getInfoReceiver=null;
			}
			finish();
			break;
		default:
			break;
		}
		
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
			map.put("name", name);
			list.add(map);
		}
		mHelper.closedb();
		return list;
	}

	private String loadData(String id,int ncolumn){
		String sql="select * from signalinfo where id="+id+" order by time desc limit 0,"+column;
		mHelper=new MTDBHelper(mContext);
		ArrayList<String[]> datainfos=mHelper.query(sql);
	
		String   tmp  = "  "+name+"\r\n";
		String[] datas= datainfos.get(column-1-chooseposition);
		choosevalue	  = Integer.parseInt(datas[2]);
		
		int nCount=1;
		
		for(int i=column-1;i>=0;i--){
			String [] items=datainfos.get(i);
			String name =items[1];
			String value=items[2];
			String unit =items[3];
			if(unit.equals("\"\"")){
				unit="";
			}
			tmp+="  "+name+":"+value+unit+"  ";
			if(nCount%4==0){
				tmp+="\r\n";
			}
			nCount++;
		}
		mHelper.closedb();
		return tmp;
	}
	//	接收器;
	//	数据进行注册;
	public class GetInfoReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent){
			Bundle bundle=intent.getExtras();			
			int	pCount=bundle.getInt("count");
			//	接收信息的内容;
			if(pCount!=0){
				String str=loadData(id, column);
				vResult.setText(str);
				int v=(int) (1.9*choosevalue);
				dialView.setBigDialDegrees(v);
				dialView.myDraw();
			}	
		}
	}
}
