package com.model.entity;

import java.util.ArrayList;

public class MEMess extends MEData {
	private int _id;
	private String time;
	private String chn;
	private String id;
	private String name;
	private String dir;
	private int dlc;
//	private String datas;
	private ArrayList<char[]> DATA;
	private MEData meData;
	private String intime;
	private String initdata;

	// 含参数的构造函数;
	public MEMess(int _id, String time, String name, String dir, MEData meData,String initime,String initdata) {
		super();
		this._id = _id;
		this.time = time;
		this.chn = "no";
		this.name = name;
		this.dir = dir;
		this.meData = meData;
		this.id = meData.getID();
		this.dlc = meData.getDLC();
		this.DATA = meData.getDATA();
		this.intime=initime;
		this.initdata=initdata;
	}

	public MEData getMeData() {
		return meData;
	}

	public String getIntime() {
		return intime;
	}

	public void setIntime(String intime) {
		this.intime = intime;
	}

	public String getInitdata() {
		return initdata;
	}

	public void setInitdata(String initdata) {
		this.initdata = initdata;
	}

	public void setMeData(MEData meData) {
		this.meData = meData;
	}

	// 不含参的构造函数;
	public MEMess() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getChn() {
		return chn;
	}

	public void setChn(String chn) {
		this.chn = chn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public int getDlc() {
		return dlc;
	}

	public void setDlc(int dlc) {
		this.dlc = dlc;
	}

	public ArrayList<char[]> getDATA() {
		return DATA;
	}

	public void setDATA(ArrayList<char[]> dATA) {
		DATA = dATA;
	}

}
