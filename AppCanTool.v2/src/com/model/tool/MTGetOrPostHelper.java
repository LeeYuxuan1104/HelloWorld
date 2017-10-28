package com.model.tool;

import java.io.*;
import java.net.*;
import java.util.*;

public class MTGetOrPostHelper {
	/**
	 *	跟网络请求有关的方法;
	 *其中主要包括的方法有:
	 *	1.Get方法;
	 *	2.Post方法;
	 */
	public static int NTIMEOUT=6000;
	
	public MTGetOrPostHelper() {
	
	}
	//	Get进行内容发送get方法和post方法;
	public String sendGet(String url, String params) {
		//	结果的字符串;
		String 			result    = "";
		//	输入流;
		BufferedReader  input	  = null;
		//	每行的读取字符串;
		String 			readLine  = null;
		try {
			//	01.拼接字符串;
			String 		sUrl	  = url + "?" + params;
			
			//	02.新建字符串并且创立网络连接;
			URL 		realUrl   = new URL(sUrl);
			HttpURLConnection conn=(HttpURLConnection) realUrl.openConnection();	
			
			//  03.设置网络使用的通用属性;
			conn.setRequestMethod("GET");	  		 //	03.1Get方法;
			conn.setRequestProperty("accept", "*/*");//	03.2设置方式;
			conn.setRequestProperty("connection", "Keep-Alive");	//03.3连接方式;
			conn.setRequestProperty("Accept-Language", Locale.getDefault().toString());
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setConnectTimeout(NTIMEOUT);    	 //	03.4网络连接的超时时间设置;
			conn.setReadTimeout(NTIMEOUT); 			 //	04.5读网络信息的超时间设置;
			
			//	04.网络连接;
			conn.connect();
			
//			//	05.调试过程可以重新启用,目前属于非必须的内容;
//			// 获取所有响应头字段
//			Map<String, List<String>> map = conn.getHeaderFields();
//			// 遍历所有的响应头字段
//			for (String key : map.keySet()) {
//				System.out.println(key + "--->" + map.get(key));
//			}
			
			// 06.定义BufferedReader输入流来读取URL的响应
			input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((readLine = input.readLine()) != null) {
				result += "\n" + readLine;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	public String sendPost(String url, String params) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(params); //
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
			}
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	 //	上传文件;
	 public String uploadFile(String sUrl,String path,String name){
		 	String sResult	  =	"fail";
	        String end 		  =	"\r\n";
	        String twoHyphens =	"--";
	        String boundary   =	"*****";
	    
	    try{
	          URL 	url 	  =	new URL(sUrl);

	          HttpURLConnection con	=	(HttpURLConnection)url.openConnection();

	          con.setDoInput(true);

	          con.setDoOutput(true);

	          con.setUseCaches(false);
	          con.setRequestMethod("POST");
	          con.setRequestProperty("Connection", "Keep-Alive");
	          con.setRequestProperty("Charset", "UTF-8");
	          con.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
	          /*********************/
	          //	超链接的时间;
	          con.setConnectTimeout(NTIMEOUT);    	 //	03.4网络连接的超时时间设置;
	          con.setReadTimeout(NTIMEOUT); 			 //	04.5读网络信息的超时间设置;
				
	          //	04.网络连接;
	          con.connect();
	          /*********************/
	          
	          DataOutputStream 	ds 			=	new DataOutputStream(con.getOutputStream());
	          ds.writeBytes(twoHyphens + boundary + end);
	          ds.writeBytes("Content-Disposition: " +
	          				"form-data; "+
	                        "name=\"file1\";" +
	                        "filename=\""+
	                        name +"\""+ 
	                        end);
	          
	          ds.writeBytes(end);  
	          FileInputStream 	fStream 	=	new FileInputStream(path);
	          int 				bufferSize 	=	1024;

	          byte[] 			buffer 		=	new byte[bufferSize];
	          int 				length 		=	-1;
	          while((length = fStream.read(buffer)) !=-1){
	            ds.write(buffer, 0, length);
	          }
	          ds.writeBytes(end);
	          ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
	          fStream.close();
	          ds.flush();
	          InputStream 		is 			= 	con.getInputStream();
	          int 				ch;
	          StringBuffer 		b 			=	new StringBuffer();
	          while( ( ch = is.read() ) !=-1 ){
	            b.append( (char)ch );
	          }
	          sResult	=	b.toString();
	          ds.close();
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
			return sResult;
	 }
}
