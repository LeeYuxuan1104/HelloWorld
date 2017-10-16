package com.view;


import java.util.ArrayList;

import com.example.modetest.R;
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
		mdbHelper	=	new MDBHelper(mContext);
		list		=	mdbHelper.query("select _id,id from can_message");

		for(String[] strs:list){
			Log.i("MyLog", strs[0]+"|"+strs[1]);
		}		
	}
}
