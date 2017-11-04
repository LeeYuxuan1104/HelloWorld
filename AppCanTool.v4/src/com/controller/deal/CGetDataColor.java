package com.controller.deal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

import com.model.entity.MESignal;
import com.model.tool.MTDBHelper;

@SuppressLint("UseSparseArrays")
public class CGetDataColor {
	private MTDBHelper helper;
	private String[] colorbgs={"#CDBFB5","#CD69C6","#CD6839","#000080",
							   "#CD3278","#CD2626","#CD2990","#8B1A1A",
							   "#BF3EFF","#CD3700","#9ACD32","#B22222",
							   "#8B4513","#8B3E2F","#8B008B","#282828",
							   "#707070","#5F9EA0","#548B54","#4EEE94",
							   "#FF6A6A","#F08080","#EE3B3B","#EE3A8C"
								};
	
	public CGetDataColor(MTDBHelper helper) {
		this.helper=helper;
	}
	
	public  Map<Integer, String> compute(String sid){
		Map<Integer, String>  color	 =new HashMap<Integer, String>();
		ArrayList<MESignal> datas=getSingalList(sid);
		int count=0;
		for(MESignal signal:datas){
			//	获取其中的Way的方式;
			int sindex	 = signal.getSindex();
			int bcount	 = signal.getBcount();
			int direction= signal.getDirection();
			int    sline 	=sindex/8;
			int    scolum	=sindex%8;
			//	进行方向的定位;
			switch (direction) {
			//	摩托罗拉算法
			case 0:
				
				for(int i=0;i<bcount;i++){
					int index=8*sline+scolum;
					String tmp=colorbgs[count];
					if(scolum==0){
						sline=sline+1;
						scolum=8;
					}
					scolum--;
					color.put(index, tmp);		
				}
				break;
			//	因特尔算法;
			case 1:
				for(int i=0;i<bcount;i++){
					int index=8*sline+scolum;
					String tmp=colorbgs[count];
					color.put(index, tmp);
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
			
			count++;
		}
		return color;
	}

	private ArrayList<MESignal> getSingalList(String sid){
		String sql="select  * from can_signal where id="+sid;
		ArrayList<MESignal> list=new ArrayList<MESignal>();
		ArrayList<String[]> datas=helper.query(sql);
		for(String[] items:datas){
			String _id=items[0];
			String sg_flag=items[1];
			String signal_name=items[2];
			String way=items[3];
			String judge=items[4];
			String rank=items[5];
			String unit=items[6];
			String node_name=items[7];
			String id=items[8];
			MESignal meSignal=new MESignal(Integer.parseInt(_id), sg_flag, signal_name, way, judge, rank, unit, node_name, Integer.parseInt(id));
			list.add(meSignal);
		}
		return list;
	}
}
