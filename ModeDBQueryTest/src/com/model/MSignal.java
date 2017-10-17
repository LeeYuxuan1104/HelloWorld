package com.model;

public class MSignal {
	private int _id;
	private String sg_flag;
	private String signal_name;
	private String way;
	private String judge;
	private String rank;
	private String unit;
	private String node_name;
	private int id;
	//	获取其中的Way的方式;
	private int sindex;	  //01.起始位置;
	private int bcount;	  //02.位数总和;
	private int direction;//03.方向;
	
	
	
	public MSignal(int _id, String sg_flag, String signal_name, String way,
			String judge, String rank, String unit, String node_name, int id) {
		super();
		this._id = _id;
		this.sg_flag = sg_flag;
		this.signal_name = signal_name;
		this.way = way;
		this.judge = judge;
		this.rank = rank;
		this.unit = unit;
		this.node_name = node_name;
		this.id = id;
		dealwithway();
	}
	private void dealwithway(){
		//	拆解路径方式;
		//	起始位置;
		sindex	  =Integer.parseInt(way.substring(0, way.indexOf("|")));
		//	偏移量;
		bcount	  =Integer.parseInt(way.substring(way.indexOf("|")+1,way.indexOf("@")));
		//	方向;
		String dir=way.substring(way.indexOf("@")+1, way.length());
		if(dir.equals("0+"))
			direction=0;
		else direction=1;
	}
	
	
	public int getSindex() {
		return sindex;
	}
	public int getBcount() {
		return bcount;
	}
	public int getDirection() {
		return direction;
	}
	public MSignal() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getSg_flag() {
		return sg_flag;
	}
	public void setSg_flag(String sg_flag) {
		this.sg_flag = sg_flag;
	}
	public String getSignal_name() {
		return signal_name;
	}
	public void setSignal_name(String signal_name) {
		this.signal_name = signal_name;
	}
	public String getWay() {
		return way;
	}
	public void setWay(String way) {
		this.way = way;
	}
	public String getJudge() {
		return judge;
	}
	public void setJudge(String judge) {
		this.judge = judge;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getNode_name() {
		return node_name;
	}
	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
