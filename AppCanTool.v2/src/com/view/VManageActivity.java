package com.view;

import java.util.ArrayList;

import com.controller.CExportData;
import com.model.entity.MEElement;
import com.model.tool.MTDBHelper;
import com.model.tool.MTTreeViewAdapter;
import com.model.tool.MTTreeViewItemClickListener;

import android.os.Bundle;
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
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class VManageActivity extends Activity implements OnClickListener{
	/*上下文声明*/
	private Context				 mContext;
	private MTTreeViewAdapter 	 mTreeViewAdapter;
	private MTTreeViewItemClickListener mTreeViewItemClickListener;
	private MTDBHelper			 helper;
	/*控件的声明*/
	private TextView 			 vTopic;
	//	返回键;
	private Button 	 			 vBack,vExportCan,vExportStruction;
	//	显示列表;
	private ListView 			 vlvShow;
	//	声明的控件;	
	private ArrayList<MEElement> elements;
	private ArrayList<MEElement> elementsData;
	/*参量的声明*/
	private	String 				 format; 
	private String[] 			 formats	=	{"csv","json","txt"};
	/*定义类声明*/
	private CExportData			 cExportData;
	private AlertDialog.Builder  mBuilder;
	private Spinner 			 spinner;
	private ArrayAdapter<String> adapter;
	
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
	}
	//	初始化方法;
	private void initEvent(){
		/*定义方法内容*/
		//	上下文初始化;
		mContext	=	VManageActivity.this;
		//	定义的类内容;
		helper		=	new MTDBHelper(mContext);
		cExportData	=	new CExportData(mContext, helper);
		
		/*初始化文字信息*/
		vTopic.setText(R.string.tip_manage);
		vBack.setText(R.string.act_back);
		
		/*添加事件的监听*/
		//	下方按钮添加事件监听;
		vBack.setOnClickListener(this);
		//	导出表单数据;
		vExportCan.setOnClickListener(this);
		vExportStruction.setOnClickListener(this);
		
		initTreeView();
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mTreeViewAdapter 		= new MTTreeViewAdapter(elements, elementsData, inflater);
		mTreeViewItemClickListener = new MTTreeViewItemClickListener(mContext,mTreeViewAdapter);
		vlvShow.setAdapter(mTreeViewAdapter);
		vlvShow.setOnItemClickListener(mTreeViewItemClickListener);		
	}
	private void initTreeView() {
		elements = new ArrayList<MEElement>();
		elementsData = new ArrayList<MEElement>();
		String sql="select * from can_message";
		ArrayList<String[]> datasMessage=helper.query(sql);
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
			ArrayList<String[]> itemss=helper.query(s1);	
			int size1=itemss.size();
			String s2="select * from signalinfo where id="+id;
			ArrayList<String[]> itemss2=helper.query(s2);	
			int size2=itemss2.size()/size1;
			
			//	条目名称;
			String topic=bo_flag+id+" "+message_name+":"+dlc+" "+node_name+"  ["+size1+"项参数]  ["+size2+"条数据]";
			MEElement e=new MEElement(topic, MEElement.TOP_LEVEL, Integer.parseInt(id),MEElement.NO_PARENT, true, false);
			elements.add(e);
			elementsData.add(e);
		}
		sql="select * from can_signal";
		ArrayList<String[]> datasSignal=helper.query(sql);
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
			mBuilder = new AlertDialog.Builder(mContext);
			mBuilder.setTitle(R.string.act_format);
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
			mBuilder.setPositiveButton(R.string.act_ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					cExportData.exportCanInfo(format);					
					Toast.makeText(mContext, R.string.tip_export_finished, Toast.LENGTH_SHORT).show();
				}
			});
			
			mBuilder.setView(spinner);
			mBuilder.setNegativeButton(R.string.act_no, null);
			mBuilder.create();
			mBuilder.show();
			
			break;
		case R.id.btnExportStruction:
			mBuilder = new AlertDialog.Builder(mContext);
			mBuilder.setTitle(R.string.act_format);
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
			mBuilder.setPositiveButton(R.string.act_ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					cExportData.exportStruction(format);
					Toast.makeText(mContext, R.string.tip_export_finished, Toast.LENGTH_SHORT).show();
				}
			});
			
			mBuilder.setView(spinner);
			mBuilder.setNegativeButton(R.string.act_no, null);
			mBuilder.create();
			mBuilder.show();
			
			
			break;
		default:
			break;
		}
	}
}
