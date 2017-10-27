package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model.tool.MTDataBaseTool;

public class CAN_CheckRemote extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private String  sResult;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter 	pWriter = 	resp.getWriter();
		MTDataBaseTool 	mDB		=	new MTDataBaseTool();
		String 			name	=	req.getParameter("name");
		String 			value	=	req.getParameter("value");
		String 			unit	=	req.getParameter("unit");
		if(unit.equals("")){
			unit="null";
		}
		String			note	=	req.getParameter("note");
		String 			id		=	req.getParameter("id");
		String 			time	=	req.getParameter("time");
		String sql=
				"insert into signalinfo (name,value,unit,note,id,time) values ('"+name+"','"+value+"','"+unit+"','"+note+"','"+id+"','"+time+"')";
		
		int n=mDB.doDBUpdate(sql);
		System.out.println("sql="+sql);
		System.out.println("count="+n);
		sResult="ok";
		pWriter.print(sResult)	;
		pWriter.flush();
		pWriter.close();
	}
	
	public String getID(){
		String sid=null;
		Calendar Cld=Calendar.getInstance();
		int YY=Cld.get(Calendar.YEAR);
		int mm=Cld.get(Calendar.MONTH)+1;
		int dd=Cld.get(Calendar.DAY_OF_MONTH);
		int h=Cld.get(Calendar.HOUR_OF_DAY);
		int m=Cld.get(Calendar.MINUTE);
		sid=YY+""+mm+dd+h+m;
		return sid;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
