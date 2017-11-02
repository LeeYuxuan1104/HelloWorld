package com.model.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class MTDataBaseTool {
	private String driver 	= "com.mysql.jdbc.Driver";
	private String dbName 	= "mydb";
	private String password = "root";
	private String userName = "root";
	private String url 		= "jdbc:mysql://localhost:3306/" + dbName;
	
	public MTDataBaseTool() {
	
	}
	
	public MTDataBaseTool(String driver, String dbName, String password,
			String userName, String url) {
		super();
		this.driver = driver;
		this.dbName = dbName;
		this.password = password;
		this.userName = userName;
		this.url = url;
	}

	//	检查数据库是否有链接;
	public Connection doCheckDB(){
		Connection conn=null;
		try {
	       Class.forName(driver);
	       conn= DriverManager.getConnection(url, userName, password);
	    } catch (Exception e) {
	       return null;
	    }
		return conn;
	}
	//	数据库更新操作;
	public int doDBUpdate(String sqlLanguage){
		int 		 	  nCount	=	0;
		Connection		  conn		=	doCheckDB();
		PreparedStatement ptmt		=	null;;
		try {
			ptmt 	= conn.prepareStatement(sqlLanguage);
			nCount 	= ptmt.executeUpdate();
			if(conn!=null){
				conn.close();
			}
			if(ptmt!=null){
				ptmt.close();
			}
		} catch (SQLException e) {
			return 0;
		}
		return nCount;
	}
	/*
	 * sql的查询内容;
	 * */
//	public ArrayList<Workerinfo> doDBQueryWorkerinfo(String sqlLanguage){
//		Connection conn		=	doCheckDB();
//		Statement  stmt		=	null;
//		
//		ArrayList<Workerinfo> list=new ArrayList<>();
//		try {
//			stmt 		 = conn.createStatement();
//			ResultSet rs = stmt.executeQuery(sqlLanguage);//创建数据对象
//			while (rs.next()){
//				Workerinfo workerinfo=new Workerinfo(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5));
//				list.add(workerinfo);
//				System.out.println();
//			}
//			if(rs!=null){
//				rs.close();				
//			}
//			if(stmt!=null){				
//				stmt.close();
//			}
//			if(conn!=null){				
//				conn.close();
//			}
//		} catch (SQLException e) {
//			
//		}
//		return list; 
//	}

}
