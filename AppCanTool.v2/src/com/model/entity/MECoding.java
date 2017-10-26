package com.model.entity;

/*将发送值进行编码的类*/
public class MECoding {
	private double value;
	/*纠正值*/
	private String judge;
	//	1.A值;
	private double avalue;
	//	2.B值;
	private double bvalue;
	
	/*位置值*/
	private String way;
	//	获取其中的Way的方式;
	private int sindex;	  //01.起始位置;
	private int bcount;	  //02.位数总和;
	private int direction;//03.方向;
	
	private String rank;
	private int 	id;
	
	
	
	
	public MECoding(double value, String way, String judge, String rank, int id) {
		super();
		this.value = value;
		this.way = way;
		this.judge = judge;
		this.rank = rank;
		this.id = id;
		//	处理;
		dealwithjudge();
		//	方向;
		dealwithway();
	}
	//	纠正数的处理;
	private void dealwithjudge(){
		String pA=judge.substring(0, judge.indexOf(","));
		avalue=Double.parseDouble(pA);
		String pB=judge.substring(judge.indexOf(",")+1, judge.length());
		bvalue=Double.parseDouble(pB);
	}
	//	方向值得处理;
	private void dealwithway(){
		//	拆解路径方式;
		//	起始位置;
		sindex	  =Integer.parseInt(way.substring(0, way.indexOf("|")));
		//	偏移量;
		bcount	  =Integer.parseInt(way.substring(way.indexOf("|")+1,way.indexOf("@")));
		//	方向;
		String dir=way.substring(way.indexOf("@")+1, way.length());
		//	摩托罗拉算法;
		if(dir.equals("0+"))
			direction=0;
		//	因特尔算法;
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
	public double getAvalue() {
		return avalue;
	}
 
	public double getBvalue() {
		return bvalue;
	}
 
	public MECoding() {
		super();
	
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
