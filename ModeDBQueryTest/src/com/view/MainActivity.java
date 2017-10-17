package com.view;


import java.util.ArrayList;

import com.controller.CAnalData;
import com.controller.CTransData;
import com.example.modetest.R;
import com.model.MData;
//import com.tool.MDbHelper;
import com.tool.MDBHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;


public class MainActivity extends Activity {
	private Context			mContext;
	
	private MDBHelper		mdbHelper;//  数据库帮助类;
	private ArrayList<String[]> list;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.mContext=MainActivity.this;
		Log.i("MyLog", "1");
		MData  mData=analysData();
		Log.i("MyLog", "mData="+mData);
		transData(mData);
	}
	//	数据解析;
	private MData analysData(){
		String str="t358701010201010101\t";
		System.out.println(str);
		CAnalData  cAnalData =new CAnalData(str);
		MData  mData=cAnalData;
		return mData;
		
	}
	//	数据解码;
	private void transData(MData  mData){
		CTransData cTransData=new CTransData(mContext, mData);
//		mdbHelper=new MDBHelper(mContext);
//		list=mdbHelper.query("select _id,id from can_message");
////		for(String[] item:list){
//			Log.i("MyLog", item[0]+"|"+item[1]);
//		}
		
	}
}
