package com.tool;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MSqliteHelper {
	private SQLiteDatabase 		 	mDB;		//  数据库件;
    private MDbHelper 	   		 	mDBhelper;	//  辅助控件;
    public static final int 	NID_DB_VERSION=3;
	public static final String 	SNAME_DB	  	 =	"myDB.db";
	public SQLiteDatabase getmDB() {
		return mDB;
	}

	public void setmDB(SQLiteDatabase mDB) {
		this.mDB = mDB;
	}

	public MSqliteHelper(Context mContext) {
		File 	file 	=	mContext.getFilesDir();
		String 	path 	= 	file.getAbsolutePath() + "/"+SNAME_DB;
		mDBhelper  		= 	new MDbHelper(mContext, path, NID_DB_VERSION);
		mDB 			= 	mDBhelper.getReadableDatabase();
	}
	
	public void doCloseDataBase(){
		if(mDB!=null){
			mDB.close();
			mDB=null;
		}
		if(mDBhelper!=null){
			mDBhelper.close();
			mDBhelper=null;
		}
	}
}
