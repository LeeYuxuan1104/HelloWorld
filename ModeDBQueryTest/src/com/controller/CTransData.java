package com.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.model.MData;
import com.model.MInfo;
import com.model.MMessage;
import com.model.MSignal;
import com.tool.MDBHelper;

public class CTransData extends MData{
	private MData 				mData;
	private MDBHelper 			mdbHelper;

	//	16进制数据包;
	private ArrayList<char[]>   datas16;
	private Map<Integer, String> datas2;
	//	信号信息列表;
	private ArrayList<MSignal>  listSignals;
	

	public CTransData(Context context) {
		//	数据库控制类;
		this.mdbHelper	=   new  MDBHelper(context);
	}
	
	public MData getmData() {
		return mData;
	}

	public void setmData(MData mData) {
		this.mData = mData;
	}

	public void compute(){

		//	ID编号;
		String 	  ID16	=	mData.getID();
		//	ID 16进制转化为10进制;
		int 	  ID10	=	Integer.parseInt(ID16, 16);
		datas16			=	mData.getDATA();
		this.datas2		=	create8list();
	
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				int index=8*i+(7-j);
				String s=datas2.get(index);
				Log.i("MyLog", s);
			}
		}
		
		this.listSignals=	queryInfofromSignal(ID10);
		destory8Matrix();
	}
	//	进行破阵法;
	private void destory8Matrix(){
		//	破阵之法;
		/*
		 *对其中的每一行的信息值进行重新的翻转;
		 * */
		for(MSignal mSignal:listSignals){
			//	初始位置;
			int sindex=mSignal.getSindex();
			//	位数;
			int bcount=mSignal.getBcount();
			//	反向;
			int direction=mSignal.getDirection();
			ArrayList<String> listData=new ArrayList<String>();
			int    sline =sindex/8;
			int    scolum=sindex%8;
			String tmp	 ="";
			switch (direction) {
			//	摩托罗拉算法->向右
			case 0:
				for(int i=0;i<bcount;i++){
					int index=8*sline+scolum;
					String value=datas2.get(index);
					listData.add(value);
					if(scolum==0){
						sline=sline+1;
						scolum=7;
					}
					scolum--;
				}
				for(int i=0;i<bcount;i++){
					tmp+=listData.get(i);
				}
				break;
			//	因特尔算法->向左;
			case 1:
				for(int i=0;i<bcount;i++){
					int index=8*sline+scolum;
					String value=datas2.get(index);
					listData.add(value);
					if(scolum==7){
						sline=sline+1;
						scolum=0;
					}
					scolum++;
				}
				for(int i=bcount;i>0;i--){
					tmp+=listData.get(i);
				}
				break;
			default:
				break;
			}
			int v10=Integer.valueOf(tmp, 2);
			
			double Avalue=mSignal.getAvalue();
			//	位数;
			double Bvalue=mSignal.getBvalue();
			
			double V10=Avalue*v10+Bvalue;
			String unit=mSignal.getUnit();
			/////	Info数据表中的内容;
			long time=System.currentTimeMillis();
			MInfo mInfo=new MInfo(mSignal.getSignal_name(), V10, unit, mSignal.getNode_name(), mSignal.getId(), time);
			Log.i("MyLog", "name="+mInfo.getName()+"|value="+mInfo.getValue()+"|unit="+mInfo.getUnit()+"|node_name="+mInfo.getNote()+"|id="+mInfo.getId()+"|time="+mInfo.getTime());
			
		}
	}
	
	
	
	
	//	查询message大表中的id信息;
	private ArrayList<MSignal> queryInfofromSignal(int id10){
		ArrayList<MSignal>  listSignals	=new ArrayList<MSignal>();
		MMessage  			mMessage	=null;
		String				sql;
		//	大类的sql语句;
		sql="select * from can_message where id="+id10;
		//	结果集;
		ArrayList<String[]> listMesssage=mdbHelper.query(sql);
		//	message查询对象;
		for(String[] strs:listMesssage){
			mMessage=new MMessage(Integer.parseInt(strs[0]), strs[1], Integer.parseInt(strs[2]),strs[3], Integer.parseInt(strs[4]),strs[5]);
		}
		//	判断Signal表的信息;
		if(mMessage==null){
			return null;
		}
		
		sql="select * from can_signal where id="+id10;
		ArrayList<String[]> listSignal=mdbHelper.query(sql);
		for(String[] strs:listSignal){
			MSignal mSignal=new MSignal(Integer.parseInt(strs[0]),strs[1], strs[2], strs[3], strs[4], strs[5],strs[6], strs[7],Integer.parseInt(strs[8]));
			listSignals.add(mSignal);
		}
		return listSignals;
	}
	//	形成8阵图(16进制转化为2进制);
	@SuppressLint("UseSparseArrays")
	private Map<Integer, String> create8list(){
		Map<Integer, String> datas2=new HashMap<Integer, String>();
		//	每行是
		int i=0;
		for(char[] chs:datas16){
			String tmp  =chs[0]+""+chs[1];
			String tmp2 =hexString2binaryString(tmp);
			for(int j=0;j<8;j++){
				int index=i*8+(7-j);
				String value=tmp2.substring(j, j+1);
				datas2.put(index, value);
			}
			i++;
		}
		return datas2;
	}
	//	将16进制数转化为2进制数的方式;	
	private String hexString2binaryString(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000"
					+ Integer.toBinaryString(Integer.parseInt(
							hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}
	
	
}
