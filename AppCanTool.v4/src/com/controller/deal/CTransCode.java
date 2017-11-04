package com.controller.deal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.model.entity.MECoding;

import android.annotation.SuppressLint;
import android.content.Context;

public class CTransCode {
	@SuppressWarnings("unused")
	private Context mContext;
	//	构造函数;
	public CTransCode(Context mContext) {
		this.mContext=mContext;
	}
	
	@SuppressLint("UseSparseArrays")
	public String compute(ArrayList<MECoding> listCoding,int id){
		String caninfo="";
		Map<Integer, String> map=new HashMap<Integer, String>();
		for(MECoding meCoding:listCoding){
			//	phy值;
			double value	=	meCoding.getValue();
			//	A值;
			double avalue	=	meCoding.getAvalue();
			//	B值;
			double bvalue	=	meCoding.getBvalue();
			//	X值-10进制;
			long xvalue10	=	getXvalue(value, avalue, bvalue);
			//	X值-10转化成2进制;
			String xvalue2	=	Long.toBinaryString(xvalue10) ;
			//	
			/*获得有关纠正的值*/	
			//01.起始位置;
			int sindex		=meCoding.getSindex();	  
			//02.位数总和;
			int bcount		=meCoding.getBcount();	  
			//03.方向;
			int direction	=meCoding.getDirection();
			char[] getDatas =reGetXValue2(xvalue2, sindex,bcount, direction);
			int    sline 	=sindex/8;
			int    scolum	=sindex%8;
			//	进行方向的定位;
			switch (direction) {
			//	摩托罗拉算法
			case 0:
				
				for(int i=0;i<bcount;i++){
					int index=8*sline+scolum;
					String tmp=getDatas[i]+"";
					map.put(index, tmp);		
					if(scolum==0){
						sline=sline+1;
						scolum=8;
					}
					scolum--;
				}
				break;
			//	因特尔算法;
			case 1:
				for(int i=0;i<bcount;i++){
					int index=8*sline+scolum;
					String tmp=getDatas[bcount-i]+"";
					map.put(index, tmp);
					if(scolum==7){
						sline=sline+1;
						scolum=-1;
					}
					scolum++;
				}
				break;

			default:
				break;
			}
		}
		//	修改标签值位;
		for(int i=0;i<64;i++){
			String value=map.get(i);
			if(value==null){
				map.put(i, "0");
			}
		}
		//	将64格位数据转化成2进制的数据包;
		ArrayList<String> list=new ArrayList<String>();
		for(int i=0;i<8;i++){
			String tmp="";
			for(int j=0;j<8;j++){
				int index=i*8+(7-j);
				String value=map.get(index);
				tmp+=value+"";
			}
			list.add(tmp);
		}

		String result="";
		for(String item:list){
			String t1=item.substring(0, 4);
			String t2=item.substring(4, 8);
			result+=Long.toString (Long.parseLong(t1, 2), 16)+Long.toString (Long.parseLong (t2, 2), 16);
		}
		String FLAG	=	getFlag(id);
		String ID	=	getID(Long.toString(Long.parseLong(id+"", 10), 16));
		int    DLC	=	getDLC(result);
		String data =   getReData(result, DLC);
		caninfo=FLAG+ID+DLC+data;
		return caninfo;
	}
	//	重新获取2进制数;
	private char[] reGetXValue2(String xvalue2,int sindex,int bcount,int direction){
		//	实际的2进制字符长度;
		int 	fcount=xvalue2.length();
		char[]	chs	  =xvalue2.toCharArray();
		String  head="";
		for(int i=0;i<bcount-fcount;i++){
			head+="0";
		}
		String  tail="";
		for(int i=0;i<fcount;i++){
			tail+=chs[i]+"";
		}
		String  data=head+tail;
		return data.toCharArray();
	}
	//	数符转化:phy=Ax+B;
	private long getXvalue(double phy,double a,double b){
		long xvalue=(long) ((phy-b)/a);
		return xvalue;	
	}
	//	判断标识符;
	private String getFlag(int id){
		String flag ="T";
		String fbit =Integer.toString(Integer.parseInt(id+"", 10), 16);
		byte[] bytes=fbit.getBytes();
		int	   size =bytes.length;
		switch (size) {
		case 1:
		case 2:
		case 3:
			flag="t";
			break;
		case 8:
			flag="T";
			break;
		default:
			break;
		}
		return flag;
	}
	//	获取ID的值;
	private String getID(String id){
		String result="";
		byte[] b	 =id.getBytes();
		int    size  =b.length;
		int count=0;
		if(size<3){
			count=3-size;
		}
		String head="";
		for(int i=0;i<count;i++){
			head+="0";
		}
		result=head+id;
		return result;
	}
	
	
	//	获取长度值;
	private int	getDLC(String param){
		int 	size	=0;
		char[] 	chs		=param.toCharArray();
		int 	length	=chs.length;
		int 	count	=0;
		for(int i=length-1;i>=0;i--){
			char c=chs[i];
			if(c!='0'){
				break;
			}
			count++;
		}
		//	商数;
		int quotient=(length-count)/2;
		//	余数;
		int remainder=(length-count)%2;
		if(remainder==0)
			size=quotient;
		else
			size=quotient+1;

		return size;
	}

	private String getReData(String param,int dlc){
		String result="";
		char[] params=param.toCharArray();
		for(int i=0;i<dlc*2;i++){
			result+=params[i]+"";
		}
		return result;
	}
}
