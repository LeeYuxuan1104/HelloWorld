/**
 * CAN信息模块类
 */
package com;

import java.util.ArrayList;

public class Mdata {
	//CanTool装置接受的信息，字符串类型
	private String iData;
	//将iDATa转换成datas字符数组
	private char[] datas;
	//字符数组长度
	private int size;
	//字符数组索引
	private int index;
	//FLAG，ID，DLC，DATA，分别为CAN总线的数据
	//FLAG[标志位] 
	//A.t:标准帧——其后的ID位数为3位;
	//B.T:扩展帧——其后的ID位数为8位;
	private char FLAG;
	//[ID]:ID编号，由硬件设备传输
	//A.当t时，位数为3位(000~FFF)
	//B.当T时，位数为8位(00000000~FFFFFFFF)
	private String ID;
	//[DLC]：传输的DATA数据的长度；
	private int DLC;
	//A.传输的数据的实际内容；B.最多八个字节
	private ArrayList<char[]> DATA;
	
	//无参构造
	public Mdata() {
		super();	
	}
	//获取iData
	public String getiData(){
		return iData;
	}
	//设置iData
	public void setiData(String iData){
		this.iData = iData;
	}	
	//获取datas，使用String类中public char[] toCharArray()方法，将字符串转变为一个字符数组
	public char[] getDatas() {
		datas=iData.toCharArray();
		return datas;
	}
	//设置字符数组datas
	public void setDatas(char[] datas) {
		this.datas = datas;
	}
	
    //获取字符数组长度
	public int getSize() {
		return datas.length;
	}
	//判断是否有下一项
	public boolean hasNext(){
		if(index<size)
			return true;
		else 
			return false;
	}
	//输出下一个字符
	public char next(){
		char data=datas[index];
		index++;
		return data;
	}
	//获取FLAG
	public char getFLAG() {
		return FLAG;
	}
	//设置FLAG
	public void setFLAG(char FLAG) {
		this.FLAG = FLAG;
	}
	//获取ID
	public String getID() {
		return ID;
	}
	//设置ID
	public void setID(String ID) {
		this.ID = ID;
	}
	//获取DLC
	public int getDLC() {
		return DLC;
	}
	//设置DLC
	public void setDLC(int DLC) {
		this.DLC = DLC;
	}
	//获取DATA
	public ArrayList<char[]> getDATA() {
		return DATA;
	}
	//设置DATA
	public void setDATA(ArrayList<char[]> DATA) {
		this.DATA = DATA;
	}
}
