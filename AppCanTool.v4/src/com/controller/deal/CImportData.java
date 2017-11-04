package com.controller.deal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import com.model.entity.MEData;
import com.model.tool.MTDBHelper;

public class CImportData {
	//	使用数据库内容;
	private MTDBHelper helper;
	private Context	   mContext;
	public CImportData(Context mContext,MTDBHelper helper) {
		this.mContext=mContext;
		this.helper=helper;
	}

	@SuppressLint("SimpleDateFormat")
	public void inputDataBase_Mes(long l1,long l2,String chn,String dir,MEData meData){
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");//设置日期格式
		long 	  ldis  	= 	l2-l1;
		double 	  time  	= 	ldis/1000;
		String 	  id16  	= 	meData.getID();
		int 	  id10		= 	Integer.parseInt(id16, 16);
		String 	  initdata	=	meData.getiData();
		String 	  intime	=	df.format(l2);
		String 	  sql   	= 	"select message_name from can_message where id="+id10;
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
		sql="insert into mess_info " +
			"('time','chn','id','name','dir','dlc','data','intime','initdata') values " +
			"('"+time+"','"+chn+"','"+id10+"','"+name+"','"+dir+"',"+dlc+",'"+data+"','"+intime+"','"+initdata+"')";
		helper.oper(sql);
	}
	//	进行数据的解析_入库;
	String id="";
	public void inputDataBase_struction(String datainfo){
		String sql="";
		if(datainfo.contains("bo_")){
			String bo_flag="bo_";
			id			  =datainfo.substring(datainfo.indexOf("_")+1, datainfo.indexOf(" "));
			String message_name=datainfo.substring(datainfo.indexOf(" ")+1, datainfo.indexOf(":"));
			String tmp=datainfo.substring(datainfo.indexOf(":")+1, datainfo.length());
			String dlc=tmp.substring(0, tmp.indexOf(" "));
			String node_name=tmp.substring(tmp.indexOf(" ")+1, tmp.length());
			sql="insert into can_message (bo_flag,id,message_name,dlc,node_name) values ('"+bo_flag+"',"+id+",'"+message_name+"',"+dlc+",'"+node_name+"')";
			helper.oper(sql);
		}else if(datainfo.contains("sg_")){
			String sg_flag		="sg_";
			String signal_name	=datainfo.substring(datainfo.indexOf("_")+1, datainfo.indexOf(":"));
			String way			=datainfo.substring(datainfo.indexOf(":")+1, datainfo.indexOf("("));
			String judge		=datainfo.substring(datainfo.indexOf("(")+1, datainfo.indexOf(")"));
			String rank			=datainfo.substring(datainfo.indexOf("[")+1, datainfo.indexOf("]"));
			String unit			=datainfo.substring(datainfo.indexOf("\""),datainfo.lastIndexOf("\"")+1);
			String node_name	=datainfo.substring(datainfo.lastIndexOf("\"")+1, datainfo.length());
			sql="insert into can_signal (sg_flag,signal_name,way,judge,rank,unit,node_name,id) values ('"+sg_flag+"','"+signal_name+"','"+way+"','"+judge+"','"+rank+"','"+unit+"','"+node_name+"',"+id+")";
			helper.oper(sql);
		}
	}
	
	
	
	
	
	//	导入数据信息;
	public ArrayList<Map<String, String>> readFileList(){
		ArrayList<Map<String, String>> list=new ArrayList<Map<String,String>>();
		File fParent=getFileDir(mContext, "tablefile");
		if(!fParent.exists()){
			fParent.mkdirs();
		}
		
		File[] files=fParent.listFiles();
		for(File file:files){
			String name=file.getName();
			String path=file.getPath();
			Map<String, String> map=new HashMap<String, String>();
			map.put("name", name);
			map.put("path", path);
			list.add(map);
		}
		return list;  
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
}
