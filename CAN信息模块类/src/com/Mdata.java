/**
 * CAN��Ϣģ����
 */
package com;

import java.util.ArrayList;

public class Mdata {
	//CanToolװ�ý��ܵ���Ϣ���ַ�������
	private String iData;
	//��iDATaת����datas�ַ�����
	private char[] datas;
	//�ַ����鳤��
	private int size;
	//�ַ���������
	private int index;
	//FLAG��ID��DLC��DATA���ֱ�ΪCAN���ߵ�����
	//FLAG[��־λ] 
	//A.t:��׼֡��������IDλ��Ϊ3λ;
	//B.T:��չ֡��������IDλ��Ϊ8λ;
	private char FLAG;
	//[ID]:ID��ţ���Ӳ���豸����
	//A.��tʱ��λ��Ϊ3λ(000~FFF)
	//B.��Tʱ��λ��Ϊ8λ(00000000~FFFFFFFF)
	private String ID;
	//[DLC]�������DATA���ݵĳ��ȣ�
	private int DLC;
	//A.��������ݵ�ʵ�����ݣ�B.���˸��ֽ�
	private ArrayList<char[]> DATA;
	
	//�޲ι���
	public Mdata() {
		super();	
	}
	//��ȡiData
	public String getiData(){
		return iData;
	}
	//����iData
	public void setiData(String iData){
		this.iData = iData;
	}	
	//��ȡdatas��ʹ��String����public char[] toCharArray()���������ַ���ת��Ϊһ���ַ�����
	public char[] getDatas() {
		datas=iData.toCharArray();
		return datas;
	}
	//�����ַ�����datas
	public void setDatas(char[] datas) {
		this.datas = datas;
	}
	
    //��ȡ�ַ����鳤��
	public int getSize() {
		return datas.length;
	}
	//�ж��Ƿ�����һ��
	public boolean hasNext(){
		if(index<size)
			return true;
		else 
			return false;
	}
	//�����һ���ַ�
	public char next(){
		char data=datas[index];
		index++;
		return data;
	}
	//��ȡFLAG
	public char getFLAG() {
		return FLAG;
	}
	//����FLAG
	public void setFLAG(char FLAG) {
		this.FLAG = FLAG;
	}
	//��ȡID
	public String getID() {
		return ID;
	}
	//����ID
	public void setID(String ID) {
		this.ID = ID;
	}
	//��ȡDLC
	public int getDLC() {
		return DLC;
	}
	//����DLC
	public void setDLC(int DLC) {
		this.DLC = DLC;
	}
	//��ȡDATA
	public ArrayList<char[]> getDATA() {
		return DATA;
	}
	//����DATA
	public void setDATA(ArrayList<char[]> DATA) {
		this.DATA = DATA;
	}
}
