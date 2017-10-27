package com.model.tool;

import java.util.ArrayList;

import android.util.Log;

public class MTListRoom {
	private static MTListRoom mtListRoom=new MTListRoom();
	private ArrayList<String> datas;
	
	public MTListRoom() {
		Log.i("MyLog", "生成一个实例");
	}

	public static MTListRoom getInstatnce() {
		return mtListRoom;
	}

	public static void setMtListRoom(MTListRoom mtListRoom) {
		MTListRoom.mtListRoom = mtListRoom;
	}

	public ArrayList<String> getDatas() {
		return datas;
	}

	public void addDatas(String str){
		datas.add(str);
	}
	public void setDatas(ArrayList<String> datas) {
		this.datas = datas;
	}
	
}
