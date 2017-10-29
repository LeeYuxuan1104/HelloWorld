package com.model.tool;

import java.io.File;
import java.util.ArrayList;

import com.model.tool.MTDataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MTDBHelper {
	private SQLiteDatabase 	mDB;		//  数据库件;
    private final int 		NID_DB_VERSION=	3;
	private final String 	SNAME_DB	  =	"myDB.db";
	private int   column;	
	private int   line;

	public MTDBHelper(Context mContext) {
		File 	  file 		=	mContext.getFilesDir();
		String 	  path 		= 	file.getAbsolutePath() + "/"+SNAME_DB;
		MTDataBase mDBhelper	=	new MTDataBase(mContext, path, NID_DB_VERSION);
		this.mDB 			= 	mDBhelper.getReadableDatabase();
	}
	//	数据库操作类;	
	public void oper(String sql){
		mDB.execSQL(sql);
	}
	//	TODO 数据库的关闭;
	public void closedb(){
		if(mDB!=null){			
			mDB.close();
		}
	}
	//	查询结果;
	public ArrayList<String[]> query(String sql){
		column=0;
		line=0;
		ArrayList<String[]> list=new ArrayList<String[]>();
		Cursor mCursor	= 	mDB.rawQuery(sql, null);
		column			=	mCursor.getColumnCount();
		while (mCursor.moveToNext()) {
			String[] arrays	=	new String[column];
			for(int i=0;i<column;i++){
				arrays[i]=mCursor.getString(i);
			}
			list.add(arrays);
			line++;
		}
		if(mCursor!=null){
			mCursor.close();
			//TODO 数据库的查询关闭;
			mCursor=null;
		}
		return list;
	}
	
	//	查询结果的列数;
	public int getColumn() {
		return column;
	}
	
	//	查询的行数;
	public int getLine() {
		return line;
	}
	
}
