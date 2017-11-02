package com.controller;

import java.util.ArrayList;

import com.model.MData;

public class CAnalData extends MData{
//	private char[] 	  datas;
	private ArrayList<char[]> list;
	private MData	  mData;
	private String    iData;
	
	//	控制类的构造方法;
	public CAnalData(String iData) {
		this.iData=iData;
//		setiData(iData);
		list=new ArrayList<char[]>();
		//	进行数据的截取;
//		computeData();	
	}

	
	public MData getmData() {
		return mData;
	}


	public void setmData(MData mData) {
		this.mData = mData;
	}


	public void computeData(){
		mData.setiData(iData);
		char[] 	  datas=mData.getDatas();
		
		char FLAG=datas[0];
		//	设置ID编号
		mData.setFLAG(FLAG);
		String temp="";
		int    size=0;
		//	位数的选择;
		switch (FLAG) {
		//	标准位;
		case 't':
			for(int i=1;i<4;i++){
				temp+=""+datas[i];
			}
			mData.setDLC(Integer.parseInt(datas[4]+""));
			size=mData.getDLC();
			
			for(int i=0;i<size;i++){
				int index=0;
				char[] data=new char[2];
				for(int j=i*2+5;j<(i*2+5)+2;j++){
					data[index]=datas[j];
					index++;
				}
				list.add(data);
			}
			break;
		//	扩展位;
		case 'T':
			for(int i=1;i<9;i++){
				temp+=""+datas[i];
			}
			mData.setDLC(Integer.parseInt(datas[9]+""));
			size=mData.getDLC();
			for(int i=0;i<size;i++){
				int index=0;
				char[] data=new char[2];
				for(int j=i*2+9;j<(i*2+9)+2;j++){
					data[index]=datas[j];
					index++;
				}
				list.add(data);
			}
			break;
		default:
			break;
		}
		//	检测补位;
		int dis=8-size;
		while (dis>0) {
			char[] c={'0','0'};
			list.add(c);
			dis--;
		}
		mData.setDATA(list);
		mData.setID(temp);
	}
}
