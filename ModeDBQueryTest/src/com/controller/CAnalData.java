package com.controller;

import java.util.ArrayList;

import com.model.MData;

public class CAnalData extends MData{
	private char[] 	  datas;
	private ArrayList<char[]> list;
	private MData	  mData;
	//	控制类的构造方法;
	public CAnalData(String iData) {
		setiData(iData);
		datas=getDatas();
		list=new ArrayList<char[]>();
		//	进行数据的截取;
		computeData();
		
	}

	public MData getmData() {
		return mData;
	}


	public void setmData(MData mData) {
		this.mData = mData;
	}


	private void computeData(){
		char FLAG=datas[0];
		//	设置ID编号
		setFLAG(FLAG);
		String temp="";
		int    size=0;
		//	位数的选择;
		switch (FLAG) {
		//	标准位;
		case 't':
			for(int i=1;i<4;i++){
				temp+=""+datas[i];
			}
			setDLC(Integer.parseInt(datas[4]+""));
			size=getDLC();
			
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
			setDLC(Integer.parseInt(datas[9]+""));
			size=getDLC();
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
		setDATA(list);
		setID(temp);
	}
}
