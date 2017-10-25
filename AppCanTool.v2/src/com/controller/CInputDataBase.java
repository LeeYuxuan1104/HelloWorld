package com.controller;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.model.entity.MEData;
import com.model.tool.MTDBHelper;

public class CInputDataBase {
	//	使用数据库内容;
	private MTDBHelper helper;
	public CInputDataBase(Context mContext) {
		this.helper=new MTDBHelper(mContext);
	}
	
	
	public void inputMes(long l1,long l2,String chn,String dir,MEData meData){
		long 	  ldis  = l2-l1;
		double 	  time  = ldis/1000;
		String 	  id16  = meData.getID();
		int 	  id10	= Integer.parseInt(id16, 16);
		String 	  sql   = "select message_name from can_message where id="+id10;
		ArrayList<String[]> list = helper.query(sql);		
		String    name  = "";
		for(String[] item:list){
			name=item[0];
		}
		int	      dlc   = meData.getDLC();
		ArrayList<char[]> datas=meData.getDATA();
		
		String    data  = "";
		for(char[] chs:datas){
			data+=chs[0]+""+chs[1]+" ";
		}
		sql=
			"insert into mess_info " +
			"('time','chn','id','name','dir','dlc','data') values " +
			"('"+time+"','"+chn+"','"+id10+"','"+name+"','"+dir+"',"+dlc+",'"+data+"')";
		Log.i("MyLog", "sql="+sql);
		helper.oper(sql);
	}
}
