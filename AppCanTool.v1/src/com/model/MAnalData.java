package com.model;

public class MAnalData {
	private String importData;
	private byte[] roomData;
	
	//	数据分解;
	public MAnalData() {
		
	}
	//	进行数据的加载;

	public String getImportData() {
		return importData;
	}

	public void setImportData(String importData) {
		this.importData = importData;
	}

	public byte[] getRoomData() {
		roomData=importData.getBytes();
		return roomData;
	}

	public void setRoomData(byte[] roomData) {
		this.roomData = roomData;
	}
	
}
