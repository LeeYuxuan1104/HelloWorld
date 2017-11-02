package com.model.tool;

import java.util.ArrayList;

import com.model.tool.MTConfig;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MTDataBase extends SQLiteOpenHelper{

	private ArrayList<String> list;
	private MTConfig 		  mConfig;
	
	public MTDataBase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	//	数据库管理类的构造函数;
	public MTDataBase(Context context, String name, int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		mConfig=new MTConfig();
		list=mConfig.getListSQlInit();
		for(String query:list){
			db.execSQL(query);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
