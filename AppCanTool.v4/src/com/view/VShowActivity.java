package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.model.tool.MTDBHelper;
import com.model.tool.MTSharedPreference;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class VShowActivity extends Activity implements OnClickListener{
	/*上下文声明*/
	private Context	mContext;
	private Intent	mIntent;
	private Bundle	mBundle;
	private SimpleAdapter mAdapter,mAdapterDraw;
	/*控件的声明*/
	private TextView vTopic;
	private Button 	 vBack,vFlush,vDelAll,vCompress,vLine,vMode;
	private ListView vListView;
	private Spinner	 vSpMessage;
	
	/*参量的声明*/
	private ArrayList<Map<String, String>> list,list2;
	private String   pTargetFromService="com.from.service.to.activity",
					 name,id;
	private int 	 column;
	private GetInfoReceiver	getInfoReceiver;
	private IntentFilter 	getInfoFilter;

	
	/*定义类声明*/
	private MTDBHelper mHelper;
	//	模式识别;
	private MTSharedPreference	mSPrefer; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vshow);
		initView();
		initEvent();
	}
	@Override
	protected void onResume() {
		super.onResume();
		showData();
		checkMod();
	}
	//	初始化控件;
	private void initView(){
		vBack		=(Button) findViewById(R.id.btnBack);
		vTopic		=(TextView) findViewById(R.id.tvTopic);
		vListView	=(ListView) findViewById(R.id.lvShow);
		
		vFlush		=(Button) findViewById(R.id.btnFlushShow);
		vDelAll		=(Button) findViewById(R.id.btnDelallShow);
		
		vCompress	=(Button) findViewById(R.id.btnCompress);
		vLine		=(Button) findViewById(R.id.btnLine);
		//	类型选择;
		vSpMessage	= (Spinner) findViewById(R.id.spMessage);
		//	模式按钮;
		vMode		= (Button) findViewById(R.id.btnMode);
	}
	//	初始化方法;
	private void initEvent(){
		//	上下文初始化;
		mContext=VShowActivity.this;
		vTopic.setText(R.string.tip_show);
		vBack.setText(R.string.act_back);
		vCompress.setVisibility(View.VISIBLE);
		vLine.setVisibility(View.VISIBLE);
		vSpMessage.setVisibility(View.VISIBLE);
		//	下方按钮添加事件监听;
		vBack.setOnClickListener(this);
		vFlush.setOnClickListener(this);
		vDelAll.setOnClickListener(this);
		vCompress.setOnClickListener(this);
		vLine.setOnClickListener(this);
		vMode.setOnClickListener(this);
		//	进行列表;
		list=new ArrayList<Map<String, String>>();
		//	进行声明;
		mHelper=new MTDBHelper(mContext);
		//	模式识别;
		mSPrefer=new MTSharedPreference(mContext, "receive_mode", MODE_PRIVATE);
		/*与广播有关的内容*/
		getInfoReceiver = new GetInfoReceiver();
		getInfoFilter 	= new IntentFilter();
		getInfoFilter.addAction(pTargetFromService);
		registerReceiver(getInfoReceiver, getInfoFilter);
		//	数据显示;
		showData();
		list2=loadMessageList();
		mAdapterDraw=new SimpleAdapter(mContext, list2, R.layout.act_item2, new String[]{"id","name"},new int[]{R.id.id,R.id.content} );
		vSpMessage.setAdapter(mAdapterDraw);
		
		
		vSpMessage.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				name	=list2.get(position).get("name");
				id		=list2.get(position).get("id");
				String s1="select * from can_signal where id="+id;
				Log.i("MyLog", "s="+s1);
				ArrayList<String[]> itemss=mHelper.query(s1);	
				column	=itemss.size();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				name=list2.get(0).get("name");
				id=list2.get(0).get("id");
			}
		});
	}
	@Override
	public void onClick(View view) {
		int pId=view.getId();
		switch (pId) {
		case R.id.btnBack:
			if(getInfoReceiver!=null){			
				unregisterReceiver(getInfoReceiver);
				getInfoReceiver=null;
			}
			finish();
			break;
		case R.id.btnFlushShow:
			showData();
			break;
		case R.id.btnDelallShow:
			String sql	=	"delete from mess_info";
			mHelper.oper(sql);
			showData();
			break;
		case R.id.btnCompress:
			mIntent		=	new Intent(mContext, VCompressActivity.class);
			mBundle		=	new Bundle();
			mBundle.putString("name", name);
			mBundle.putString("id", id);
			mBundle.putInt("column", column);
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
			break;
		case R.id.btnLine:
			mIntent		=	new Intent(mContext, VDrawLineActivity.class);
			mBundle		=	new Bundle();
			mBundle.putString("name", name);
			mBundle.putString("id", id);
			mBundle.putInt("column", column);
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
			break;
		case R.id.btnMode:
			//	当前为手动;
			if(checkMod().equals("hand")){
				//	点击为自动;
				mSPrefer.putValue("mode", "auto");
			}else 
			//	当前为自动;
			if(checkMod().equals("auto")){
				//	点击为手动;
				mSPrefer.putValue("mode", "hand");
				
			}
			checkMod();
			break;
		default:
			break;
		}
	}
	private void showData(){
		list=loadData();
		int size=list.size();
		if(size>10){
			String sql="delete from mess_info ";
			mHelper.oper(sql);
			list=loadData();
		}
		
		mAdapter=new SimpleAdapter(mContext, list, R.layout.act_itemtable, new String[]{"time","chn","id","name","dir","dlc","data"}, new int[]{R.id.time,R.id.chn,R.id.id,R.id.name,R.id.dir,R.id.dlc,R.id.data});
		vListView.setAdapter(mAdapter);
	}
	//	检测模式内容;
	private String checkMod(){
		String mode=mSPrefer.getValue("mode");
		if(mode==null){
			mSPrefer.putValue("mode", "hand");
			mode=mSPrefer.getValue("mode");
		}
		//	手动模式
		if(mode.equals("hand")){
			vMode.setText(R.string.tip_auto);
		}else if(mode.equals("auto")){
			vMode.setText(R.string.tip_hand);
		}
		return mode;
	}
	//	信号信息的选择;
	private ArrayList<Map<String, String>> loadMessageList(){
		ArrayList<Map<String, String>> list=new ArrayList<Map<String,String>>();
		String 				sql		=	"select * from can_message";
		ArrayList<String[]> datas	=	mHelper.query(sql);
		for(String[] items:datas){
			String bo_flag		=items[1];
			String id	  		=items[2];
			String message_name	=items[3];
			Map<String, String> map=new HashMap<String, String>();
			map.put("name", bo_flag+id+" "+message_name);
			map.put("id", id);
			list.add(map);
		}
		return list;
	}
	
	
	//	加载数据信息的内容;
	private ArrayList<Map<String, String>> loadData(){
		ArrayList<Map<String, String>> list=new ArrayList<Map<String,String>>();
		String sql="select * from mess_info";
		ArrayList<String[]> datas=mHelper.query(sql);
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
	//	接收器;
	//	数据进行注册;
	public class GetInfoReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent){
			Bundle bundle=intent.getExtras();			
			int	pCount=bundle.getInt("count");
			//	接收信息的内容;
			if(pCount!=0){
				if(checkMod().equals("auto")){
					showData();
				}
			}	
		}
	}
}
