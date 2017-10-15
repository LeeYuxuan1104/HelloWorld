package com.view;


import com.example.modetest.R;
import com.tool.MDbHelper;
import com.tool.MSqliteHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	private Context			mContext;
	
	private MSqliteHelper	mSqLiteHelper;//  数据库帮助类;
	private SQLiteDatabase 	mDB;		  //  数据库件;
	private Cursor 		   	mCursor;      //  数据库遍历签;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.mContext=MainActivity.this;
		mSqLiteHelper	=	new MSqliteHelper(mContext);
		mDB 			= 	mSqLiteHelper.getmDB();
		doLoadData();
//		mDB.execSQL("select * from can_message");
		
	}
//	信息加载;
	private void doLoadData(){
		String 
		sql		=	"select * from can_message";
		mCursor	= 	mDB.rawQuery(sql, null);
		while (mCursor.moveToNext()) {	
			String _id=mCursor.getString(0).toString();
			String bo_flag=mCursor.getString(1).toString();
			String id=mCursor.getString(2).toString();
			String message_name=mCursor.getString(3).toString();
			String dlc=mCursor.getString(4).toString();
			String node_name=mCursor.getString(5).toString();
			Log.i("MyLog", "["+_id+"]["+bo_flag+"]["+id+"]["+message_name+"]["+dlc+"]["+node_name+"]");
		}
		if(mCursor!=null){
			mCursor.close();
		}
		
//		tvShow.setText(sResult);		
	}

}
