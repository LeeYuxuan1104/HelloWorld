package com.test;

import com.controller.CAnalData;

public class Test {
	public static void main(String[] args) {
		String str="t12380011121314151617\t";
		System.out.println(str);
		CAnalData cAnalData=new CAnalData(str);
		cAnalData.computeData();
		char flag=cAnalData.getFLAG();
		String id=cAnalData.getID();
		int dlc=cAnalData.getDLC();
		int length=cAnalData.getDATA().size();
		System.out.print("["+flag+"]");
		System.out.print("["+id+"]");
		System.out.print("["+dlc+"]");
		for(int i=0;i<length;i++){
			String str2=String.valueOf(cAnalData.getDATA().get(i));
			System.out.print("ox"+str2+" ");
		}
		
	}
}
