package com.view;

import com.controller.CAnalData;
import com.controller.CTransData;
import com.example.modetest.R;
import com.model.MData;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;


public class MainActivity extends Activity {
	private Context			mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.mContext=MainActivity.this;
		
		MData  mData=new MData();
		
		mData		=analData(mData);
		mData		=transData(mData);
		
	}
	//	数据解析;
	private MData	analData(MData  mData){
		
		String str="t358701010201010101\t";
		CAnalData  cAnalData =new CAnalData(str);
		cAnalData.setmData(mData);
		cAnalData.computeData();
		mData	 =cAnalData.getmData();
		
		return mData;
	}
	//	数据转码;
	private MData	transData(MData	mData){
		
		CTransData cTransData=new CTransData(mContext);
		cTransData.setmData(mData);
		cTransData.compute();
				
		return mData;
	}

}
