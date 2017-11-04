package com.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

import com.controller.deal.CGetDataColor;
import com.controller.deal.CImportData;
import com.model.tool.MTDBHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VSignalDBActivity extends Activity implements OnClickListener{
	/*内容信息*/
	private Context mContext;
	/*内容控件*/
	private Button 	 vBack,vClearDB,vImportDB;
	private ListView vlvMessage;
	private TextView vTopic,vSignallayout,vTip;
	private Builder	 vBuilder;
	private WebView	 vWvlayout;
	private ProgressDialog 		 vDialog; // 对话方框;
	private ArrayAdapter<String> mAdapter;
	/*声明参量*/
	private ArrayList<String> listMessages;
	private String 	 id;
	private int 	 flag=1;
	/*自定义类*/
	private MTDBHelper 	  		 mHelper;
	private CGetDataColor 		 cGetDataColor;
	private MyThread			 myThread;
	private CImportData			 cImportData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_signaldb);
		initView();
		initEvent();
	}
	//	初始化控件;
	private void initView(){
		vBack	 = (Button) findViewById(R.id.btnBack);
		vClearDB = (Button) findViewById(R.id.btnClearDB);
		vImportDB= (Button) findViewById(R.id.btnImportDB);
		vlvMessage=(ListView) findViewById(R.id.lvMessage);
		vTopic	 = (TextView) findViewById(R.id.tvTopic);
		vWvlayout= (WebView) findViewById(R.id.wvSignalLayout);
		vTip	 = (TextView) findViewById(R.id.tvTip);
		vWvlayout.setVisibility(View.VISIBLE);
		
		vSignallayout= (TextView) findViewById(R.id.tvSignalLayout);
		vSignallayout.setVisibility(View.GONE);
		
	}
	//	初始化事件;
	@SuppressLint("UseSparseArrays")
	private void initEvent(){
		mContext	 =VSignalDBActivity.this;
		/*自定义类的声明*/
		mHelper		 =new MTDBHelper(mContext);
		cGetDataColor=new CGetDataColor(mHelper);
		cImportData  =new CImportData(mContext,mHelper);
		/*控件内容的初始*/
		vTopic.setText(R.string.tip_dbmanage);
		vBack.setText(R.string.act_back);
		//	返回按钮;
		vBack.setOnClickListener(this);
		//	清库按钮;
		vClearDB.setOnClickListener(this);
		//	导库按钮;
		vImportDB.setOnClickListener(this);
		flag=1;
		//	列表信息;
		showListData(flag);
		showLayoutData(null);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		vlvMessage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				String info=listMessages.get(position);
				switch (flag) {
				case 1:					
					id=info.substring(info.indexOf("_")+1, info.indexOf(" "));
					Map<Integer, String> map=cGetDataColor.compute(id);
					showLayoutData(map);
					break;
				case 2:
					File parent=getFileDir(mContext, "tablefile");
					String path=parent+File.separator+info;
					if(myThread==null){
						// 进度条的内容;
						final CharSequence strDialogTitle = getString(R.string.tip_dialog_wait);
						final CharSequence strDialogBody = getString(R.string.tip_dialog_done);
						vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
						myThread=new MyThread(path, mHelper, cImportData);
						myThread.start();
					}
					break;
				default:
					break;
				}

			}
		});
		
	}
	
	@Override
	public void onClick(View view) {
		int vId=view.getId();
		switch (vId) {
		//	返回键;
		case R.id.btnBack:
			finish();
			break;
		//	清库结构;
		case R.id.btnClearDB:
			vBuilder=new Builder(mContext);
			vBuilder.setTitle(R.string.act_ok);
			vBuilder.setPositiveButton(R.string.act_ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					String sql="delete from can_message";
					mHelper.oper(sql);
					sql="delete from can_signal";
					mHelper.oper(sql);
					finish();
				}
			});
			
			vBuilder.setNegativeButton(R.string.act_no, null);
			vBuilder.create();
			vBuilder.show();
			break;
		//	导入库结构;
		case R.id.btnImportDB:
			vBuilder=new Builder(mContext);
			vBuilder.setTitle(R.string.act_ok);
			vBuilder.setPositiveButton(R.string.act_ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					vTip.setVisibility(View.VISIBLE);
					flag=2;
					showListData(flag);
				}
			});
			vBuilder.setNegativeButton(R.string.act_no, null);
			vBuilder.create();
			vBuilder.show();
			break;
		default:
			break;
		}
	}
	private void showListData(int flag){		
		listMessages=loadData(flag);
		mAdapter=new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line,listMessages);
		vlvMessage.setAdapter(mAdapter);
	}
	
	
	private void showLayoutData(Map<Integer, String> map){
		String load=drawTable(map);
		vWvlayout.loadData(load, "text/html", "utf-8");	
	}
	
	private ArrayList<String> loadData(int flag){
		ArrayList<String> list = new ArrayList<String>();
		switch (flag) {
		case 1:
			String 				sql	 = "select bo_flag,message_name,id from can_message";
			ArrayList<String[]> datas= mHelper.query(sql);
			for(String[] items:datas){
				String bo_flag=items[0];
				String message_name=items[1];
				String id=items[2];
				String info=bo_flag+id+" "+message_name;
				list.add(info);
			}
			
			break;
		case 2:
			File fParent=getFileDir(mContext, "tablefile");
			if(!fParent.exists()){
				fParent.mkdirs();
			}
			
			File[] files=fParent.listFiles();
			for(File file:files){
				String name=file.getName();
				if(name.contains("_struction")){					
					list.add(name);
				}
			}
			break;
		default:
			break;
		}
		return list;
	}
	
	private String drawTable(Map<Integer, String> map){
		String content=
				"<html>" +
					"<body>" +
						"<table border=\"1\" align=\"center\">" +
						"<tr>";
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				int index=i*8+(7-j);
				String color=   "#ffffff";
				String tmp	=	null;
				if(map!=null){					
					tmp  	= map.get(index);
					if(tmp!=null){
						color=tmp;
					}
				}
				content+="<td bgcolor=\""+color+"\" align=\"center\" style=\"width:196px;\">"+index+"</td>";
				if(j==7){
					content+=
						"</tr>" +
						"<tr>";
				}
			}
		}
		content+="</tr>" +
						"</table>" +
					"</body>" +
				"</html>";
		return content;
	}
	
	private File getFileDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			cachePath = context.getExternalFilesDir(uniqueName).getPath();
		} else {
			cachePath = context.getFilesDir().getPath();
		}
		return new File(cachePath);
	}
	/*文件加载的内容*/
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
				Toast.makeText(mContext, R.string.tip_readdb_finished,Toast.LENGTH_SHORT).show();
				if(myThread!=null){
					myThread.interrupt();
					myThread=null;
				}
				finish();
				break;
			// 02.失败;
			case 2:
				Toast.makeText(mContext, R.string.tip_readdb_failed, Toast.LENGTH_LONG).show();
				if(myThread!=null){
					myThread.interrupt();
					myThread=null;
				}
				break;
			default:
				break;
			}
		}
	};
	// 定义的线程——自定义的线程内容;
	public class MyThread extends Thread {
		private String 		path;
		private CImportData	cImportData;
		private MTDBHelper	mHelper;
		public MyThread(String path,MTDBHelper mHelper,CImportData cImportData) {
			this.path=path;
			this.mHelper=mHelper;
			this.cImportData=cImportData;
		}
		
		@Override
		public void run() {
			int nFlag = 1;
			BufferedReader in = null;
			String sql="delete from can_message";
			mHelper.oper(sql);
			sql="delete from can_signal";
			mHelper.oper(sql);
			
			try {
				in = new BufferedReader(new FileReader(path));
				String line = null;
				while ((line = in.readLine()) != null) {
					cImportData.inputDataBase_struction(line);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				nFlag=2;
			}finally{
				if(in != null) {  
	                try {  
	                    in.close();  
	                }catch (Exception e) {  
	                   nFlag=2;
	                }  
	            }  
			}
			
			mHandler.sendEmptyMessage(nFlag);
		}
	}
}
