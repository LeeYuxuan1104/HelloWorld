package com.controller;

import java.util.ArrayList;

import com.model.MAnalData;

public class CAnalData extends MAnalData{
	private char[] 	  roomData;
	private ArrayList<char[]> list;
	//	控制类的构造方法;
	public CAnalData(String importData) {
		setImportData(importData);
		roomData=getRoomData();
		list=new ArrayList<char[]>();
	}
	
	public void computeData(){
		char FLAG=roomData[0];
		System.out.println("flag="+FLAG);
		//	设置ID编号
		setFLAG(FLAG);
		String temp="";
		int    size=0;
		//	位数的选择;
		switch (FLAG) {
		//	标准位;
		case 't':
			for(int i=1;i<4;i++){
				temp+=""+roomData[i];
			}
			setDLC(Integer.parseInt(roomData[4]+""));
			size=getDLC();
			
			for(int i=0;i<size;i++){
				int index=0;
				char[] data=new char[2];
				for(int j=i*2+5;j<(i*2+5)+2;j++){
					data[index]=roomData[j];
					index++;
				}
				list.add(data);
				setDATA(list);
			}
			break;
		//	扩展位;
		case 'T':
			for(int i=1;i<9;i++){
				temp+=""+roomData[i];
			}
			setDLC(Integer.parseInt(roomData[9]+""));
			size=getDLC();
			for(int i=0;i<size;i++){
				int index=0;
				char[] data=new char[2];
				for(int j=i*2+9;j<(i*2+9)+2;j++){
					data[index]=roomData[j];
					index++;
				}
				list.add(data);
				setDATA(list);
			}
			break;
		default:
			break;
		}
		setID(temp);
	}
}
