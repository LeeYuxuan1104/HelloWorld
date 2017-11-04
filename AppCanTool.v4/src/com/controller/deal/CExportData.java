package com.controller.deal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.model.tool.MTDBHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

@SuppressLint("SimpleDateFormat")
public class CExportData {
	private Context 			 context;
	private FileOutputStream 	 fos;
	/*参量的声明*/
	private File 				 fParent,fChild;
	private MTDBHelper			 helper;
	
	
	public CExportData(Context context,MTDBHelper helper) {
		this.context=context;
		this.helper=helper;
	}
	
	private File getFileDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			cachePath = context.getExternalFilesDir(uniqueName).getPath();
		} else {
			cachePath = context.getFilesDir().getPath();
		}
		return new File(cachePath);
	}
	
	private void operFile(Context mContext,String uniqueName,String childName,String format,String content){
		fParent=getFileDir(mContext, uniqueName);
		if(!fParent.exists()){
			fParent.mkdirs();
		}
		
		fChild=new File(fParent+File.separator+childName+"."+format);
		
		if(!fChild.exists()){
			fChild.getParentFile().mkdirs();
			try {
				fChild.createNewFile();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		try {
			fos = new FileOutputStream(fChild);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			osw.write(content);
			osw.flush();
			osw.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void exportCanInfo(String format){
		String sql="select * from mess_info";
		
		ArrayList<String[]> datas=helper.query(sql);
		String content="编号,时间,chn,id,name,dir,dlc,data,引入时间,初始数据\r\n";
		for(String[] items:datas){
			content+=items[0]+","+items[1]+","+items[2]+","+items[3]+","+items[4]+","+items[5]+","+items[6]+","+items[7]+","+items[8]+","+items[9]+"\r\n";
		}
		operFile(context, "tablefile", "table_caninfo", format, content);
	}
	
	public void exportStruction(String format){
		String sql="select * from can_message";
		ArrayList<String[]> datasMessage=helper.query(sql);
		String content01="";
		for(String[] messages:datasMessage){
			String bo_flag=messages[1];
			String id=messages[2];
			String message_name=messages[3];
			String dlc=messages[4];
			String node_name=messages[5];
			content01+=bo_flag+id+" "+message_name+":"+dlc+" "+node_name+"\r\n";
			String sql1="select * from can_signal where id="+id;
			ArrayList<String[]> datasSignal=helper.query(sql1);
			
			for(String[] signals:datasSignal){
				String sg_flag	  = signals[1];
				String signal_name= signals[2];
				String way		  = signals[3];
				
				String judge	  = signals[4];
				String rank		  = signals[5];
				String unit		  = signals[6];
				String node_name2 = signals[7];
				content01+=" "+sg_flag+signal_name+":"+way+"("+judge+")["+rank+"]"+unit+node_name2+"\r\n";
			}
			content01+="\r\n";
		}
		operFile(context, "tablefile", "table_struction", format, content01);
	}
	
	public void exportTable(ArrayList<String> list,String tablename,String format){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		long l=System.currentTimeMillis();
		String content="";
		for(String str:list){
			String c=str.substring(0, str.lastIndexOf(","));
			content+=c+"\r\n";
		}
		operFile(context, "tablefile", tablename+df.format(l), format, content);
	}
}
