package com.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//	数据库的管理帮助类;
public class MDbHelper extends SQLiteOpenHelper {
	//	业务表的结构;
	private String sql_businessinfo = 
			"create table businessinfo (" +
			"bid varchar(20) primary key not null," +
			"bname varchar(20)," +
			"bkind varchar(20)," +
			"bcoman varchar(20)," +
			"bgaddress varchar(500)," +
			"bgoid varchar(20)," +
			"bshipcom varchar(20)," +
			"bpretoportday varchar(50)," +
			"boxid varchar(50)," +
			"boxsize varchar(50)," +
			"boxkind varchar(50)," +
			"boxbelong varchar(100)," +
			"retransway varchar(20))";
	//	货物表的结构;
	private String sql_goodsinfo=
			"create table goodsinfo (" +
			"gid varchar(20) primary key not null," +
			"bid varchar(20) not null," +
			"gname varchar(100) not null," +
			"boxid varchar(20) not null," +
			"boxsize varchar(100) not null," +
			"boxkind varchar(100) not null," +
			"leadnumber varchar(20) not null," +
			"gcount integer," +
			"gunit varchar(20)," +
			"gtotalweight double," +
			"glength double," +
			"gwidth double," +
			"gheight double," +
			"gvolume double," +
			"gstate integer" +
			")";	

	//	提货信息表;
	private String sql_getgoodsinfo=
			"create table getgoodsinfo (" +
			"ggid integer primary key ," +
			"gid varchar(20)," +
			"bid varchar(20)," +
			"gstate varchar(500)," +
			"gsimg varchar(500)," +
			"lkind varchar(20)," +
			"tid varchar(500)," +
			"tkind varchar(100)," +
			"oid varchar(100)," +
			"percount integer," +
			"perweight double," +
			"tformatweight double," +
			"tcount integer," +
			"gtime varchar(20)," +
			"stime varchar(20)" +
			")";
	//	港口信息的表;
	private String sql_portinfo=
			"create table portinfo (" +
					"pid integer primary key ," +
					"gid varchar(20)," +
					"bid varchar(20)," +
					"inporttime varchar(100)," +
					"ctime varchar(20)," +
					"intime varchar(20)," +
					"pboxtime varchar(20),"+
					"islean varchar(10),"+
					"state varchar(20),"+
					"simg varchar(500),"+
					"lkind varchar(20),"+
					"reporttime varchar(20),"+
					"classorderid varchar(20),"+
					"tid varchar(20),"+
					"tkind varchar(20),"+
					"oid varchar(20),"+
					"percount int(11),"+
					"perweight double,"+
					"tformatweight double,"+
					"tcount int(11),"+
					"gtime varchar(20),"+
					"stime varchar(20)"+
					")";
	//	箱管信息的表;
	private String sql_boxmanageinfo=
			"create table boxmanageinfo (" +
				"bmid integer primary key ," +
				"gid varchar(20)," +
				"bid varchar(20)," +
				"getboxspace varchar(100)," +
				"getboxtime varchar(20)," +
				"backchnportime varchar(20)," +
				"backportstorehoustime varchar(20),"+
				"portranstime varchar(20),"+
				"transtid varchar(20),"+
				"downlineovertime varchar(20),"+
				"railwaydownlinetime varchar(20),"+
				"fbacknulltime varchar(20),"+
				"state varchar(500),"+
				"simg varchar(500),"+
				"stime varchar(20)"+
				")";
	//	口岸信息的表;
	private String sql_harborinfo=
			"create table harborinfo (" +
					"hid integer primary key ," +
					"gid varchar(20)," +
					"bid varchar(20)," +
					"state varchar(500)," +
					"simg varchar(500)," +
					"ftochnharbortime varchar(20)," +
					"pboxtime varchar(20),"+
					"senttime varchar(20),"+
					"transtime varchar(20),"+
					"transtid varchar(20),"+
					"transtcount varchar(20),"+
					"pertcount varchar(20),"+
					"pertweight varchar(20),"+
					"gtime varchar(20),"+
					"stime varchar(20)"+
					")";
	//	签收信息的表;
	private String sql_resigninfo=
			"create table resigninfo (" +
			"rid integer primary key," +
			"bid varchar(20)," +
			"gid varchar(20),"+
			"state varchar(500)," +
			"simg varchar(500)" +
			")";
//	//	油料表的结构;
//	private String sql_oilinfo=
//			"create table oilinfo ("+
//			"id integer not null primary key,"+
//			"okind varchar(20) not null,"+
//			"oid varchar(20) not null,"+
//			"oliter double not null,"+
//			"omoney double not null,"+
//			"opayway varchar(20) not null,"+
//			"ocardid varchar(20),"+
//			"olmoney double,"+
//			"omile double not null,"+
//			"olat double not null,"+
//			"olng double not null,"+
//			"ocity varchar(20),"+
//			"oimg varchar(20),"+
//			"otime varchar(20) not null,"+
//			"tid varchar(20) not null,"+
//			"wid varchar(20) not null,"+
//			"wname varchar(20) not null"+
//			")";
	
//	//	主车表的结构;
//	private String sql_truckinfo=
//			"create table truckinfo (" +
//			"tid varchar(20) primary key not null," +
//			"toil01 double," +
//			"wid varchar(20) not null," +
//			"bid varchar(20) not null" +
//			")";
//	//	挂车表的结构;
//	private String sql_struckinfo=
//			"create table struckinfo (" +
//			"sid varchar(20) primary key not null," +
//			"snote varchar(500)," +
//			"tid varchar(20) not null," +
//			"bid varchar(20)" +
//			")";

	//	数据库管理类的构造函数;
	public MDbHelper(Context context, String name, int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		//	编辑列表;
		db.execSQL(sql_businessinfo);
		//	01.创建货物表;
		db.execSQL(sql_goodsinfo);
		//	02.建立提货信息表;
		db.execSQL(sql_getgoodsinfo);
		//	03.建立签收信息表;
		db.execSQL(sql_resigninfo);
		//	05.建立口岸信息表;
		db.execSQL(sql_harborinfo);
		//	06.建立箱管信息表;
		db.execSQL(sql_boxmanageinfo);
		//	07.建立港口信息表;
		db.execSQL(sql_portinfo);
//		//	04.建立车辆信息表;
//		db.execSQL(sql_truckinfo);
//		//	05.建立挂车信息表;
//		db.execSQL(sql_struckinfo);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {


	}
}
