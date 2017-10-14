package com.test;

import com.model.MAnalData;

public class Test {
	public static void main(String[] args) {
		String str="t12380011121314151617\t";
		MAnalData mAnalData=new MAnalData();
		mAnalData.setImportData(str);
		byte[] b=mAnalData.getRoomData();
		int nSize=b.length;
		for(int i=0;i<nSize;i++){
			System.out.println(b+"");
		}
	}
}
