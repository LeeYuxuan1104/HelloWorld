package com.model;

import java.util.ArrayList;

import com.tool.MConfig;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MDataBase extends SQLiteOpenHelper{

	private ArrayList<String> list;
	private MConfig 		  mConfig;
	
	public MDataBase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	//	数据库管理类的构造函数;
	public MDataBase(Context context, String name, int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		mConfig=new MConfig();
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
