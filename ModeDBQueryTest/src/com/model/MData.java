package com.model;

import java.util.ArrayList;

public class MData {
	private String iData;
	private char[] datas;
	private int    size;
	private int    index;
	//	属性参数;
	private char   FLAG;
	private String ID;
	private int	   DLC;
	private ArrayList<char[]> DATA;
	
	
	//	数据分解;
	public MData() {
		
	}
	
	//	进行数据的加载;
	public String getiData() {
		return iData;
	}

	//	设置数据的容器;
	public void setiData(String iData) {
		this.iData = iData;
	}

	
	public char[] getDatas() {
		datas=iData.toCharArray();
		size=datas.length-1;
		return datas;
	}

	public void setDatas(char[] datas) {
		this.datas = datas;
	}
	//	取出长度;
	public int getSize() {
		return size;
	}
	//	是否有下一项;
	public boolean hasNext(){
		if(index<size)
			return true;
		else 
			return false;
	}
	//	下一个数;
	public char next(){
		char data=datas[index];
		index++;
		return data;
	}

	public char getFLAG() {
		return FLAG;
	}

	public void setFLAG(char fLAG) {
		FLAG = fLAG;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public int getDLC() {
		return DLC;
	}

	public void setDLC(int dLC) {
		DLC = dLC;
	}

	public ArrayList<char[]> getDATA() {
		return DATA;
	}

	public void setDATA(ArrayList<char[]> dATA) {
		DATA = dATA;
	}
}
