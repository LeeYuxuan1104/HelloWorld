package com.view;

import java.util.ArrayList;

import com.controller.deal.CExportData;
import com.model.tool.MTDBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VManageDetailActivity extends Activity implements OnClickListener{
	private Context				 mContext;
	private Button  			 vBack,vExportInfo;
	private TextView			 vTopic;
	private ArrayList<String> 	 list;
	private String 				 tablename;
	private MTDBHelper			 helper;
	private CExportData			 cExportData;
	private Builder				 mBuilder;
	private Spinner				 spinner;
	
	private ArrayAdapter<String> exAdapter;
	private	String 				 format; 
	private String[] 			 formats	=	{"csv","json","txt"};
	private WebView				 wvShow;
	private String				 pcontent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vmanage_detail);
		initView();
		initEvent();
	}
	
	private void initView(){
		vBack	   =	(Button) findViewById(R.id.btnBack);
		vTopic	   =	(TextView) findViewById(R.id.tvTopic);
		vExportInfo=	(Button) findViewById(R.id.btnExportInfo);
		wvShow	   =	(WebView) findViewById(R.id.wvShow);
	}
	
	private void initEvent(){
		mContext	=	VManageDetailActivity.this;
		helper		=	new MTDBHelper(mContext);
		cExportData	=	new CExportData(mContext, helper);
		
		//	设置初始值;
		vBack.setText(R.string.act_back);
		
		vBack.setOnClickListener(this);
		vExportInfo.setOnClickListener(this);
		//	获得前边过程传过来的数据;
		Intent intent	=	getIntent();
		Bundle bundle	=	intent.getExtras();
		String id		=	bundle.getString("id");
		String column	=	bundle.getString("column");
		tablename		=	bundle.getString("table");
		vTopic.setText(tablename+" 详情");
		int    ncolumn	=	Integer.parseInt(column);
		list			=	loadData(id, ncolumn);
		wvShow.getSettings().setDefaultTextEncodingName("utf-8") ;
		wvShow.loadDataWithBaseURL(null, pcontent, "text/html", "utf-8", null);
	}

	private ArrayList<String> loadData(String id,int ncolumn){
		ArrayList<String> list=new ArrayList<String>();
		String sql="select * from can_signal where id="+id;
		ArrayList<String[]> datastitle=helper.query(sql);
		String item="";
		pcontent="<html>" +
					"<head> " +
					"</head>" +
					"<body>" +
						"<table border=\"1\">" +
						"<tr>";
		for(String[] items:datastitle){
			item+=items[2]+",";
			pcontent+="<th bgcolor=\"#00FF00\" align=\"center\">"+items[2]+"</th>";
		}
		pcontent+="</tr><tr>";
		list.add(item);
		sql="select * from signalinfo where id="+id;
		ArrayList<String[]> datascontent=helper.query(sql);
		int index=1;
		String tmp="";
		
		for(String[] items:datascontent){
			 String name=items[2];
			 String unit=items[3];
			 tmp+=items[2]+items[3]+",";
			 if(unit.equals("\"\"")){
				 unit="";
			 }
			 pcontent+="<td align=\"center\">"+name+unit+"</td>";
			index++;
			if(index>ncolumn){
				list.add(tmp);
				tmp="";
				pcontent+="</tr><tr>";
				index=1;
			}
		}
		
		pcontent+="</tr></table></body></html>";
		return list;
	}
	
	
	@Override
	public void onClick(View view) {
		int vid=view.getId();
		switch (vid) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnExportInfo:
			mBuilder = new AlertDialog.Builder(mContext);
			mBuilder.setTitle(R.string.act_format);
			spinner  = new Spinner(mContext);
			exAdapter  = new ArrayAdapter<String>(mContext,R.layout.act_item_line,R.id.content, formats);
			
			spinner.setAdapter(exAdapter);
			
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
					cExportData.exportTable(list, tablename, format);
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
