package com.tool;

import com.controller.CAnalData;
/*Template Method模式利用委托的形式.将值赋值给参数*/
public class Test {
	public static void main(String[] args) {
		String str="t12380011121314151617\t";
		System.out.println(str);
		CAnalData cAnalData=new CAnalData(str);
		char flag=cAnalData.getFLAG();
		String id=cAnalData.getID();
		int dlc=cAnalData.getDLC();
		int length=cAnalData.getDATA().size();
		System.out.print("["+flag+"]");
		System.out.print("["+id+"]");
		System.out.print("["+dlc+"]");
		System.out.print("[");
		for(int i=0;i<length;i++){
			String str2=String.valueOf(cAnalData.getDATA().get(i));
			System.out.print("ox"+str2+" ");
		}
		System.out.print("]");
	}
}
