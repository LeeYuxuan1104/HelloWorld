package com.view;

import java.util.ArrayList;

import com.controller.deal.CExportData;
import com.model.entity.MEElement;
import com.model.tool.MTGetOrPostHelper;
import com.model.tool.MTDBHelper;
import com.model.tool.MTTreeViewAdapter;
import com.model.tool.MTTreeViewItemClickListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class VManageActivity extends Activity implements OnClickListener{
	/*上下文声明*/
	private Context				 mContext;
	/*控件的声明*/
	private TextView 			 vTopic;
	//	返回键;
	private Button 	 			 vBack,vExportCan,vExportStruction,vFlush,vRemote,vDelAll,vSignalDB;
	//	显示列表;
	private ListView 			 vlvShow;
	//	声明的控件;	
	private ArrayList<MEElement> elements;
	private ArrayList<MEElement> elementsData;
	private ProgressDialog 		 vDialog; // 对话方框;
	private AlertDialog.Builder  vBuilder;
	private Spinner 			 spinner;
	private ArrayAdapter<String> adapter;
	/*参量的声明*/
	private	String 				 format; 
	private String[] 			 formats	=	{"csv","json","txt"};
	/*定义类声明*/
	private CExportData			 cExportData;
	private MTTreeViewAdapter 	 mTreeViewAdapter;
	private MTTreeViewItemClickListener mTreeViewItemClickListener;
	private MTDBHelper			 mhelper;
	private MyThread			 myThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vmanage);
		initView();
		initEvent();
	}

	//	初始化控件;
	private void initView(){
		vBack			=(Button) findViewById(R.id.btnBack);
		vTopic			=(TextView) findViewById(R.id.tvTopic);
		vlvShow			=(ListView) findViewById(R.id.lvShow);
		vExportCan		=(Button) findViewById(R.id.btnExportCan);
		vExportStruction=(Button) findViewById(R.id.btnExportStruction);
		
		vFlush			=(Button) findViewById(R.id.btnFlush);
		vRemote			=(Button) findViewById(R.id.btnRemote);
		vDelAll			=(Button) findViewById(R.id.btnDelAll);
		
		vSignalDB		=(Button) findViewById(R.id.btnSignalDB);
	}
	//	初始化方法;
	private void initEvent(){
		/*定义方法内容*/
		//	上下文初始化;
		mContext	=	VManageActivity.this;
		//	定义的类内容;
		mhelper		=	new MTDBHelper(mContext);
		cExportData	=	new CExportData(mContext, mhelper);
		
		/*初始化文字信息*/
		vTopic.setText(R.string.tip_manage);
		vBack.setText(R.string.act_back);
		vFlush.setVisibility(View.VISIBLE);
		vRemote.setVisibility(View.VISIBLE);
		vDelAll.setVisibility(View.VISIBLE);
		
		/*添加事件的监听*/
		//	下方按钮添加事件监听;
		vBack.setOnClickListener(this);
		//	导出表单数据;
		vExportCan.setOnClickListener(this);
		vExportStruction.setOnClickListener(this);
		vFlush.setOnClickListener(this);
		vRemote.setOnClickListener(this);
		vDelAll.setOnClickListener(this);
		vSignalDB.setOnClickListener(this);
		//	进行数据显示;
		showData();
	}
	//	显示数据;
	private void showData(){
		initTreeView();
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mTreeViewAdapter 		= new MTTreeViewAdapter(elements, elementsData, inflater);
		mTreeViewItemClickListener = new MTTreeViewItemClickListener(mContext,mTreeViewAdapter);
		vlvShow.setAdapter(mTreeViewAdapter);
		vlvShow.setOnItemClickListener(mTreeViewItemClickListener);
	}
	@Override
	protected void onResume() {
		super.onResume();
//		Log.i("MyLog", "chong");
	}
	private void initTreeView() {
		elements = new ArrayList<MEElement>();
		elementsData = new ArrayList<MEElement>();
		String sql="select * from can_message";
		ArrayList<String[]> datasMessage=mhelper.query(sql);
		for(String[] items:datasMessage){
			//	标记号;
			String bo_flag=items[1];
			//	ID编号;
			String id=items[2];
			//	message名称;
			String message_name=items[3];
			//	长度;
			String dlc=items[4];
			//	节点名称;
			String node_name=items[5];
			String s1="select * from can_signal where id="+id;
			ArrayList<String[]> itemss=mhelper.query(s1);	
			int size1=itemss.size();
			String s2="select * from signalinfo where id="+id;
			ArrayList<String[]> itemss2=mhelper.query(s2);	
			int size2=itemss2.size()/size1;
			
			//	条目名称;
			String topic=bo_flag+id+" "+message_name+":"+dlc+" "+node_name+"  ["+size1+"项参数]  ["+size2+"条数据]";
			MEElement e=new MEElement(topic, MEElement.TOP_LEVEL, Integer.parseInt(id),MEElement.NO_PARENT, true, false);
			elements.add(e);
			elementsData.add(e);
		}
		sql="select * from can_signal";
		ArrayList<String[]> datasSignal=mhelper.query(sql);
		for(String[] items:datasSignal){
			//	ID编号;
			int _id=Integer.parseInt(items[0]);
			//	信号标记;
			String sg_flag=items[1];
			//	信号名称;
			String signal_name=items[2];
			//	父亲节点;
			int id=Integer.parseInt(items[8]);
			//	条目信息;
			String topic=sg_flag+signal_name;
			MEElement e=new MEElement(topic, MEElement.TOP_LEVEL+1, _id,id, false, false);
			elementsData.add(e);
		}
	}
	
	@Override
	public void onClick(View view) {
		int pId=view.getId();
		switch (pId) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnExportCan:
			vBuilder = new AlertDialog.Builder(mContext);
			vBuilder.setTitle(R.string.act_format);
			spinner  = new Spinner(mContext);
			adapter  = new ArrayAdapter<String>(mContext,R.layout.act_item_line,R.id.content, formats);
			
			spinner.setAdapter(adapter);
			
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					format=formats[position];
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					format="txt";
				}
			});
			vBuilder.setPositiveButton(R.string.act_ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					cExportData.exportCanInfo(format);					
					Toast.makeText(mContext, R.string.tip_export_finished, Toast.LENGTH_SHORT).show();
				}
			});
			
			vBuilder.setView(spinner);
			vBuilder.setNegativeButton(R.string.act_no, null);
			vBuilder.create();
			vBuilder.show();
			
			break;
		case R.id.btnExportStruction:
			vBuilder = new AlertDialog.Builder(mContext);
			vBuilder.setTitle(R.string.act_format);
			spinner  = new Spinner(mContext);
			adapter  = new ArrayAdapter<String>(mContext,R.layout.act_item_line,R.id.content, formats);
			
			spinner.setAdapter(adapter);
			
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					format=formats[position];
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					format="txt";
				}
			});
			vBuilder.setPositiveButton(R.string.act_ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					cExportData.exportStruction(format);
					Toast.makeText(mContext, R.string.tip_export_finished, Toast.LENGTH_SHORT).show();
				}
			});
			
			vBuilder.setView(spinner);
			vBuilder.setNegativeButton(R.string.act_no, null);
			vBuilder.create();
			vBuilder.show();
			break;
		//	刷新操作;
		case R.id.btnFlush:
			//	进行数据显示;
			showData();
			Toast.makeText(mContext, R.string.tip_flush_finished, Toast.LENGTH_SHORT).show();
			break;
		//	同步操作;
		case R.id.btnRemote:
			String sql="select * from signalinfo ";
			ArrayList<String[]> datas=mhelper.query(sql);
			if(myThread==null){
				// 进度条的内容;
				final CharSequence strDialogTitle = getString(R.string.tip_dialog_wait);
				final CharSequence strDialogBody = getString(R.string.tip_dialog_done);
				vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
				myThread=new MyThread(datas);
				myThread.start();
			}
			break;
		case R.id.btnDelAll:
			vBuilder=new Builder(mContext);
			vBuilder.setTitle(R.string.act_ok);
			vBuilder.setPositiveButton(R.string.act_ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					String sql="delete from signalinfo";
					mhelper.oper(sql);
					showData();					
				}
			});
			vBuilder.setNegativeButton(R.string.act_no, null);
			vBuilder.create();
			vBuilder.show();
			break;
		case R.id.btnSignalDB:
			Intent intent=new Intent(mContext, VSignalDBActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
	//	控制线程;
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int nFlag = msg.what;
			vDialog.dismiss();
			switch (nFlag) {
			// 01.成功;
			case 1:
				Toast.makeText(mContext, R.string.tip_remote_finished,Toast.LENGTH_SHORT).show();
				break;
			// 02.失败;
			case 2:
				Toast.makeText(mContext, R.string.tip_remote_failed, Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
			if(myThread!=null){
				myThread.interrupt();
				myThread=null;
			}
		}
	};
	// 定义的线程——自定义的线程内容;
	public class MyThread extends Thread {
		private String url, param, response;
		private ArrayList<String[]> list;
		private MTGetOrPostHelper mGetOrPostHelper;
		public MyThread(ArrayList<String[]> list) {
			this.list=list;
			this.mGetOrPostHelper=new MTGetOrPostHelper();
		}
		
		@Override
		public void run() {
			int nFlag = 1;
			for(String[] items:list){
				String name = items[1];
				String value= items[2];
				String unit = items[3];
				String note = items[4];
				String id   = items[5];
				String time = items[6];	
				// 进行相应的登录操作的界面显示;
				// 01.Http 协议中的Get和Post方法;
				url 	  = "http://172.23.87.96:8888/WebCanTool.v1/remote";
				param 	  = "name="+name+"&value="+value+"&unit="+unit+"&note="+note+"&id="+id+"&time="+time;
				response  = mGetOrPostHelper.sendGet(url, param);

				if (response.equalsIgnoreCase("fail")) {
					nFlag = 2;
					break;
				}
			}
			
			mHandler.sendEmptyMessage(nFlag);
		}
	}
	//
}
