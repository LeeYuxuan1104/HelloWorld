package com.tool;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//	数据库的管理帮助类;
public class MDbHelper extends SQLiteOpenHelper {
	//	CAN信息表;
	private String sql_caninfo = 
			"create table can_info (" +
			"_id integer primary key ," +
			"flag varchar(2) not null," +
			"id varchar(32) not null," +
			"dlc integer ," +
			"data varchar(32) not null" +
			")";
	//	CAN的Message信息表;
	private String sql_canmessage=
			"create table can_message (" +
			"_id integer primary key ," +
			"bo_flag varchar(32) not null," +
			"id long not null," +
			"message_name varchar(100) not null," +
			"dlc integer ," +
			"node_name varchar(32) not null" +
			")";	

	//	提货信息表;
	private String sql_cansignal=
			"create table can_signal (" +
			"_id integer primary key ," +
			"sg_flag varchar(32) not null," +
			"signal_name varchar(100) not null," +
			"way varchar(32)," +
			"judge varchar(32)," +
			"rank varchar(32)," +
			"unit varchar(32)," +
			"node_name varchar(255)," +
			"id long" +
			")";
///////
	private String sql_insert01=
			"insert into can_message ('bo_flag','id','message_name','dlc','node_name') values ('bo_',856,'cdu_1',8,'cdu')";
		private String sql_insert01_01=
				"insert into can_signal " +
				"('sg_flag','signal_name'		 ,'way'		,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacoffbuttonst','0|1@0+'	,'1,0'  ,'0|1' ,'\"\"','hvac'	  ,856 )";
		private String sql_insert01_02=
				"insert into can_signal " +
				"('sg_flag','signal_name'		   ,'way'		,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacoffbuttonstvd','1|1@0+'	,'1,0'  ,'0|1' ,'\"\"','hvac'	  ,856 )";
		private String sql_insert01_03=
				"insert into can_signal " +
				"('sg_flag','signal_name'		   		,'way'		,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacautomodebuttonst'  ,'2|1@0+'	,'1,0'  ,'0|1' ,'\"\"','hvac'	  ,856 )";
		private String sql_insert01_04=
				"insert into can_signal " +
				"('sg_flag','signal_name'		   		,'way'		,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacautomodebuttonstvd','3|1@0+'	,'1,0'  ,'0|1' ,'\"\"','hvac'	  ,856 )";
		private String sql_insert01_05=
				"insert into can_signal " +
				"('sg_flag','signal_name'		   		,'way'		,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacfdefrostbuttonst'  ,'6|1@0+'	,'1,0'  ,'0|1' ,'\"\"','hvac'	  ,856 )";
		private String sql_insert01_06=
				"insert into can_signal " +
				"('sg_flag','signal_name'		   		  ,'way'	,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacfdefrostbuttonstvd'  ,'7|1@0+'	,'1,0'  ,'0|1' ,'\"\"','hvac'	  ,856 )";
		private String sql_insert01_07=
				"insert into can_signal " +
				"('sg_flag','signal_name'		   	,'way'	,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacdualbuttonst'  ,'10|1@0+'	,'1,0'  ,'0|1' ,'\"\"','hvac'	  ,856 )";
		private String sql_insert01_08=
				"insert into can_signal " +
				"('sg_flag','signal_name'		   	  ,'way'	,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacdualbuttonstvd'  ,'11|1@0+','1,0'  ,'0|1' ,'\"\"','hvac'	  ,856 )";
		private String sql_insert01_09=
				"insert into can_signal " +
				"('sg_flag','signal_name'		   ,'way'	,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacionbuttonst'  ,'12|1@0+','1,0'  ,'0|1' ,'\"\"','hvac'	  ,856 )";
		private String sql_insert01_10=
				"insert into can_signal " +
				"('sg_flag','signal_name'		    ,'way'	,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacionbuttonstvd' ,'13|1@0+','1,0'  ,'0|1' ,'\"\"','hvac'	  ,856 )";
		private String sql_insert01_11=
				"insert into can_signal " +
				"('sg_flag','signal_name'		    	  ,'way'	,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvaccirculationbuttonst' ,'18|1@0+','1,0'  ,'0|1' ,'\"\"','hvac'	  ,856 )";
		private String sql_insert01_12=
				"insert into can_signal " +
				"('sg_flag','signal_name'		    	    ,'way'	,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvaccirculationbuttonstvd' ,'19|1@0+','1,0'  ,'0|1' ,'\"\"','hvac'	  ,856 )";
		private String sql_insert01_13=
				"insert into can_signal " +
				"('sg_flag','signal_name'		 ,'way'	   ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacacbuttonst' ,'20|1@0+','1,0'  ,'0|1' ,'\"\"','hvac'     ,856 )";
		private String sql_insert01_14=
				"insert into can_signal " +
				"('sg_flag','signal_name'		   ,'way'	 ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacacbuttonstvd' ,'21|1@0+','1,0'  ,'0|1' ,'\"\"','hvac'     ,856 )";
		private String sql_insert01_15=
				"insert into can_signal " +
				"('sg_flag','signal_name'		   ,'way'	 ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacacmaxbuttonst','22|1@0+','1,0'  ,'0|1' ,'\"\"','hvac'     ,856 )";
		private String sql_insert01_16=
				"insert into can_signal " +
				"('sg_flag','signal_name'		     ,'way'	   ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacacmaxbuttonstvd','23|1@0+','1,0'  ,'0|1' ,'\"\"','hvac'     ,856 )";
		private String sql_insert01_17=
				"insert into can_signal " +
				"('sg_flag','signal_name'		  ,'way'	,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'cdu_hvacmodebuttonst','26|3@0+','1,0'  ,'0|7' ,'\"\"','hvac'     ,856 )";
		private String sql_insert01_18=
				"insert into can_signal " +
				"('sg_flag','signal_name'	  ,'way'	,'judge','rank' ,'unit','node_name' ,'id') values " +
				"('sg_'	   ,'hvac_windexitspd','30|4@0+','1,0'  ,'0|15' ,'\"\"','vector_xxx',856 )";
		private String sql_insert01_19=
				"insert into can_signal " +
				"('sg_flag','signal_name'	  		   ,'way'	,'judge','rank' ,'unit','node_name' ,'id') values " +
				"('sg_'	   ,'cdu_hvac_drivertempselect','36|5@0+','0.5,18'  ,'18|32' ,'\"℃\"','vector_xxx',856 )";
		private String sql_insert01_20=
				"insert into can_signal " +
				"('sg_flag','signal_name'	    ,'way'	  ,'judge'	 ,'rank' ,'unit','node_name' ,'id') values " +
				"('sg_'	   ,'hvac_psntempselect','44|5@0+','0.5,18'  ,'18|32' ,'\"\"','vector_xxx',856 )";
		private String sql_insert01_21=
				"insert into can_signal " +
				"('sg_flag','signal_name'	    ,'way'	  ,'judge','rank' ,'unit','node_name' ,'id') values " +
				"('sg_'	   ,'cdu_hvacctrlmodest','54|3@0+','1,0'  ,'0|7' ,'\"\"','hvac',856 )";
		private String sql_insert01_22=
				"insert into can_signal " +
				"('sg_flag','signal_name'  ,'way'	 ,'judge','rank','unit','node_name' ,'id') values " +
				"('sg_'	   ,'cdu_controlst','55|1@0+','1,0'  ,'0|1' ,'\"\"','hvac'		,856 )";
///////
	private String sql_insert02=
		"insert into can_message ('bo_flag','id','message_name','dlc','node_name') values ('bo_',61,'cdu_4',8,'cdu')";
		private String sql_insert02_01=
				"insert into can_signal " +
				"('sg_flag','signal_name'  ,'way'	,'judge','rank','unit','node_name' ,'id') values " +
				"('sg_'	   ,'cdu_hvacaccfg','1|2@0+','1,0'  ,'0|3' ,'\"\"','hvac'		,61 )";
		private String sql_insert02_02=
				"insert into can_signal " +
				"('sg_flag','signal_name'      ,'way'	,'judge','rank','unit','node_name' ,'id') values " +
				"('sg_'	   ,'cdu_hvacaircircfg','3|2@0+','1,0'  ,'0|3' ,'\"\"','hvac'		,61 )";
		private String sql_insert02_03=
				"insert into can_signal " +
				"('sg_flag','signal_name'      ,'way'	,'judge','rank','unit','node_name' ,'id') values " +
				"('sg_'	   ,'cdu_hvacaircircfg','3|2@0+','1,0'  ,'0|3' ,'\"\"','hvac'		,61 )";
///////
	private String sql_insert03=
			"insert into can_message ('bo_flag','id','message_name','dlc','node_name') values ('bo_',1067,'cdu_nm',8,'cdu')";
		private String sql_insert03_01=
				"insert into can_signal " +
				"('sg_flag','signal_name'      ,'way'	,'judge','rank'  ,'unit','node_name' 		,'id') values " +
				"('sg_'	   ,'cdu_nmdestaddress','7|8@0+','1,0'  ,'0|255' ,'\"\"','bcm,peps,icm,cdu'	,1067 )";
	
		private String sql_insert03_02=
				"insert into can_signal " +
				"('sg_flag','signal_name','way'	  ,'judge','rank','unit','node_name' 		,'id') values " +
				"('sg_'	   ,'cdu_nmalive','8|1@0+','1,0'  ,'0|1' ,'\"\"','bcm,peps,icm,cdu'	,1067 )";
		private String sql_insert03_03=
				"insert into can_signal " +
				"('sg_flag','signal_name','way'	  ,'judge','rank','unit','node_name' 		,'id') values " +
				"('sg_'	   ,'cdu_nmring' ,'9|1@0+','1,0'  ,'0|1' ,'\"\"','bcm,peps,icm,cdu'	,1067 )";
		private String sql_insert03_04=
				"insert into can_signal " +
				"('sg_flag','signal_name'	 ,'way'	   ,'judge','rank','unit','node_name' 		,'id') values " +
				"('sg_'	   ,'cdu_nmlimphome' ,'10|1@0+','1,0'  ,'0|1' ,'\"\"','bcm,peps,icm,cdu',1067 )";
		private String sql_insert03_05=
				"insert into can_signal " +
				"('sg_flag','signal_name'	 ,'way'	   ,'judge','rank','unit','node_name' 		,'id') values " +
				"('sg_'	   ,'cdu_nmsleepind' ,'12|1@0+','1,0'  ,'0|1' ,'\"\"','bcm,peps,icm,cdu',1067 )";
		private String sql_insert03_06=
				"insert into can_signal " +
				"('sg_flag','signal_name'	 	  ,'way','judge','rank','unit','node_name' 		,'id') values " +
				"('sg_'	   ,'cdu_nmsleepack' ,'13|1@0+' ,'1,0'  ,'0|1' ,'\"\"','bcm,peps,icm,cdu',1067 )";
		private String sql_insert03_07=
				"insert into can_signal " +
				"('sg_flag','signal_name'	 	  ,'way'	,'judge','rank','unit','node_name' 		,'id') values " +
				"('sg_'	   ,'cdu_nmwakeuporignin' ,'23|8@0+','1,0'  ,'0|255' ,'\"\"','bcm,peps,icm,cdu',1067 )";
		private String sql_insert03_08=
				"insert into can_signal " +
				"('sg_flag','signal_name'	  ,'way'	 ,'judge','rank','unit','node_name' 	  ,'id') values " +
				"('sg_'	   ,'cdu_nmdatafield' ,'31|40@0+','1,0'  ,'0|1' ,'\"\"','bcm,peps,icm,cdu',1067 )";
///////	
	private String sql_insert04=
			"insert into can_message ('bo_flag','id','message_name','dlc','node_name') values ('bo_',1056,'bcm_nm',8,'bcm')";
		private String sql_insert04_01=
				"insert into can_signal " +
				"('sg_flag','signal_name'	   ,'way'	 ,'judge','rank'  ,'unit','node_name' 	    ,'id') values " +
				"('sg_'	   ,'bcm_nmdestaddress','7|8@0+' ,'1,0'  ,'0|255' ,'\"\"','bcm,peps,icm,cdu',1056 )";
		private String sql_insert04_02=
				"insert into can_signal " +
				"('sg_flag','signal_name'  ,'way'	,'judge','rank','unit','node_name' 	  ,'id') values " +
				"('sg_'	   ,'bcm_nmalive'  ,'8|1@0+','1,0'  ,'0|1' ,'\"\"','bcm,peps,icm,cdu',1056 )";
		private String sql_insert04_03=
				"insert into can_signal " +
				"('sg_flag','signal_name'  ,'way'	,'judge','rank','unit','node_name' 	     ,'id') values " +
				"('sg_'	   ,'bcm_nmring'   ,'9|1@0+','1,0'  ,'0|1' ,'\"\"','bcm,peps,icm,cdu',1056 )";
		private String sql_insert04_04=
				"insert into can_signal " +
				"('sg_flag','signal_name'   ,'way'	  ,'judge','rank','unit','node_name' 	   ,'id') values " +
				"('sg_'	   ,'bcm_nmlimphome','10|1@0+','1,0'  ,'0|1' ,'\"\"','bcm,peps,icm,cdu',1056 )";
		private String sql_insert04_05=
				"insert into can_signal " +
				"('sg_flag','signal_name'   ,'way'	  ,'judge','rank','unit','node_name' 	   ,'id') values " +
				"('sg_'	   ,'bcm_nmsleepind','12|1@0+','1,0'  ,'0|1' ,'\"\"','bcm,peps,icm,cdu',1056 )";
		private String sql_insert04_06=
				"insert into can_signal " +
				"('sg_flag','signal_name'   ,'way'	  ,'judge','rank','unit','node_name' 	   ,'id') values " +
				"('sg_'	   ,'bcm_nmsleepack','13|1@0+','1,0'  ,'0|1' ,'\"\"','bcm,peps,icm,cdu',1056 )";
		private String sql_insert04_07=
				"insert into can_signal " +
				"('sg_flag','signal_name'   	 ,'way'	   ,'judge','rank'  ,'unit','node_name' 	  ,'id') values " +
				"('sg_'	   ,'bcm_nmwakeuporignin','23|8@0+','1,0'  ,'0|255' ,'\"\"','bcm,peps,icm,cdu',1056 )";
		private String sql_insert04_08=
				"insert into can_signal " +
				"('sg_flag','signal_name'    ,'way'	    ,'judge','rank','unit','node_name' 	     ,'id') values " +
				"('sg_'	   ,'bcm_nmdatafield','31|40@0+','1,0'  ,'0|1' ,'\"\"','bcm,peps,icm,cdu',1056 )";
///////
	private String sql_insert05=
			"insert into can_message ('bo_flag','id','message_name','dlc','node_name') values ('bo_',792,'bcm_bcan_1',8,'bcm')";
		private String sql_insert05_01=
				"insert into can_signal " +
				"('sg_flag','signal_name' ,'way'   ,'judge','rank','unit','node_name' 	         ,'id') values " +
				"('sg_'	   ,'bcm_keyst'	  ,'1|2@0+','1,0'  ,'1|3' ,'\"\"','peps,icm,avm,cdu,hvac',792 )";
///////
	private String sql_insert06=
			"insert into can_message ('bo_flag','id','message_name','dlc','node_name') values ('bo_',837,'bcm_esc_2',8,'bcm')";
		private String sql_insert06_01=
				"insert into can_signal " +
				"('sg_flag','signal_name' ,'way'    ,'judge','rank','unit','node_name' 	         ,'id') values " +
				"('sg_'	   ,'esc_vehspdvd','37|1@0+','1,0'  ,'0|1' ,'\"\"','bcm,peps,icm,avm,cdu',837 )";
		private String sql_insert06_02=
				"insert into can_signal " +
				"('sg_flag','signal_name' ,'way'     ,'judge'      ,'rank'  ,'unit','node_name' 	      ,'id') values " +
				"('sg_'	   ,'esc_vehspd'  ,'36|13@0+','0.05625,0'  ,'0|240' ,'\"\"','bcm,peps,icm,avm,cdu',837 )";
		
	
///////
	private String sql_insert07=
			"insert into can_message ('bo_flag','id','message_name','dlc','node_name') values ('bo_',915,'bcm_vcu_2',8,'bcm')";
		private String sql_insert07_01=
				"insert into can_signal " +
				"('sg_flag','signal_name' 			 ,'way'    ,'judge' ,'rank'  ,'unit' ,'node_name','id') values " +
				"('sg_'	   ,'vcu_compressorpwrlimit' ,'21|6@0+','100,0' ,'0|6000','\"w\"','hvac'		,915 )";
		private String sql_insert07_02=
				"insert into can_signal " +
				"('sg_flag','signal_name' 			 	,'way'    ,'judge' ,'rank','unit','node_name','id') values " +
				"('sg_'	   ,'vcu_compressorpwrlimitact' ,'32|1@0+','1,0'   ,'0|1' ,'\"\"','hvac'	 ,915 )";
		private String sql_insert07_03=
				"insert into can_signal " +
				"('sg_flag','signal_name' 	  ,'way'    ,'judge' ,'rank'   ,'unit','node_name','id') values " +
				"('sg_'	   ,'vcu_ptcpwrlimit' ,'29|6@0+','100,0' ,'0|6000' ,'\"w\"'   ,'hvac'	 ,915 )";
		private String sql_insert07_04=
				"insert into can_signal " +
				"('sg_flag','signal_name' 	     ,'way'    ,'judge' ,'rank','unit','node_name','id') values " +
				"('sg_'	   ,'vcu_ptcpwrlimitact' ,'33|1@0+','1,0'   ,'0|1' ,'\"\"','hvac'	 ,915 )";
		private String sql_insert07_05=
				"insert into can_signal " +
				"('sg_flag','signal_name' 	      ,'way'    ,'judge' ,'rank','unit','node_name','id') values " +
				"('sg_'	   ,'vcu_aircompressorreq','36|1@0+','1,0'   ,'0|1' ,'\"\"','hvac'	 ,915 )";
		private String sql_insert07_06=
				"insert into can_signal " +
				"('sg_flag','signal_name' 	        ,'way'    ,'judge' ,'rank','unit','node_name','id') values " +
				"('sg_'	   ,'vcu_aircompressorreqvd','37|1@0+','1,0'   ,'0|1' ,'\"\"','hvac'	 ,915 )";
///////
	private String sql_insert08=
			"insert into can_message ('bo_flag','id','message_name','dlc','node_name') values ('bo_',800,'hvac_1',8,'hvac')";
		private String sql_insert08_01=
				"insert into can_signal " +
				"('sg_flag','signal_name' 	         ,'way'   ,'judge' ,'rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_aircompressorreqvd','2|3@0+','1,0'   ,'0|1' ,'\"\"','cdu'	     ,800 )";
		private String sql_insert08_02=
				"insert into can_signal " +
				"('sg_flag','signal_name' 	           ,'way'   ,'judge' ,'rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_correctedextertempvd','3|1@0+','1,0'   ,'0|1' ,'\"\"','bcm,cdu'  ,800 )";
		private String sql_insert08_03=
				"insert into can_signal " +
				"('sg_flag','signal_name' 	     ,'way'   ,'judge' ,'rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_rawextertempvd','4|1@0+','1,0'   ,'0|1' ,'\"\"','cdu'      ,800 )";
		private String sql_insert08_04=
				"insert into can_signal " +
				"('sg_flag','signal_name' 	     		  ,'way'   ,'judge' ,'rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_engidlestopprohibitreq' ,'5|1@0+','1,0'   ,'0|1' ,'\"\"','cdu'      ,800 )";
		private String sql_insert08_05=
				"insert into can_signal " +
				"('sg_flag','signal_name','way'   ,'judge' ,'rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_acst'  ,'6|1@0+','1,0'   ,'0|1' ,'\"\"','cdu'      ,800 )";
		private String sql_insert08_06=
				"insert into can_signal " +
				"('sg_flag','signal_name'   ,'way'   ,'judge' ,'rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_acmaxst'  ,'7|1@0+','1,0'   ,'0|1' ,'\"\"','cdu'      ,800 )";
		private String sql_insert08_07=
				"insert into can_signal " +
				"('sg_flag','signal_name'   		 ,'way'    ,'judge'  	,'rank'		,'unit' ,'node_name','id') values " +
				"('sg_'	   ,'hvac_correctedextertemp','15|8@0+','0.5,-40'   ,'-40|87.5' ,'\"℃\"','bcm,cdu'  ,800 )";
		private String sql_insert08_08=
				"insert into can_signal " +
				"('sg_flag','signal_name'      ,'way'    ,'judge'  	  ,'rank'		,'unit' ,'node_name','id') values " +
				"('sg_'	   ,'hvac_rawextertemp','23|8@0+','0.5,-40'   ,'-40|87.5'   ,'\"℃\"','cdu'  ,800 )";
		private String sql_insert08_09=
				"insert into can_signal " +
				"('sg_flag','signal_name'      ,'way'    ,'judge' ,'rank' ,'unit' ,'node_name','id') values " +
				"('sg_'	   ,'hvac_tempselect'  ,'28|5@0+','0.5,18','18|32','\"℃\"','cdu'  ,800 )";
		private String sql_insert08_10=
				"insert into can_signal " +
				"('sg_flag','signal_name'  ,'way'    ,'judge' ,'rank' ,'unit','node_name','id') values " +
				"('sg_'	   ,'hvac_dualst'  ,'29|1@0+','1,0'   ,'0|1'  ,'\"\"','cdu'      ,800 )";
		private String sql_insert08_11=
				"insert into can_signal " +
				"('sg_flag','signal_name'  ,'way'    ,'judge' ,'rank' ,'unit','node_name','id') values " +
				"('sg_'	   ,'hvac_autost'  ,'30|1@0+','1,0'   ,'0|1'  ,'\"\"','cdu'      ,800 )";
		private String sql_insert08_12=
				"insert into can_signal " +
				"('sg_flag','signal_name'  ,'way'    ,'judge' ,'rank' ,'unit','node_name','id') values " +
				"('sg_'	   ,'hvac_type'    ,'31|1@0+','1,0'   ,'0|1'  ,'\"\"','cdu'      ,800 )";
		private String sql_insert08_13=
				"insert into can_signal " +
				"('sg_flag','signal_name'  		,'way'    ,'judge' ,'rank' ,'unit','node_name','id') values " +
				"('sg_'	   ,'hvac_windexitmode' ,'34|3@0+','1,0'   ,'0|7'  ,'\"\"','cdu'      ,800 )";
		private String sql_insert08_14=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	 ,'way'    ,'judge' ,'rank' ,'unit','node_name','id') values " +
				"('sg_'	   ,'hvac_spdfanreq' ,'36|2@0+','1,0'   ,'0|1'  ,'\"\"','cdu'      ,800 )";
		private String sql_insert08_15=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	    ,'way'    ,'judge' ,'rank' ,'unit','node_name','id') values " +
				"('sg_'	   ,'hvac_telematicsst' ,'42|3@0+','1,0'   ,'0|7'  ,'\"\"','cdu'      ,800 )";
		private String sql_insert08_16=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	         ,'way'    ,'judge' ,'rank' ,'unit','node_name','id') values " +
				"('sg_'	   ,'hvac_aircirculationhst' ,'46|2@0+','1,0'   ,'0|3'  ,'\"\"','cdu'      ,800 )";
		private String sql_insert08_17=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	       ,'way'    ,'judge' ,'rank' ,'unit','node_name','id') values " +
				"('sg_'	   ,'hvac_popupdisplayreq' ,'47|1@0+','1,0'   ,'0|1'  ,'\"\"','cdu'      ,800 )";
		private String sql_insert08_18=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	        ,'way'    ,'judge' ,'rank' ,'unit','node_name','id') values " +
				"('sg_'	   ,'hvac_drivertempselect' ,'53|5@0+','0.5,18'   ,'18|32'  ,'\"℃\"','cdu'      ,800 )";
		private String sql_insert08_19=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	        ,'way'    ,'judge' ,'rank' ,'unit','node_name','id') values " +
				"('sg_'	   ,'hvac_ionmode' 			,'55|2@0+','1,0'   ,'0|3'  ,'\"\"','cdu'      ,800 )";
		private String sql_insert08_20=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	        ,'way'    ,'judge' ,'rank' ,'unit','node_name','id') values " +
				"('sg_'	   ,'hvac_windexitspd' 		,'59|4@0+','1,0'   ,'0|15'  ,'\"\"','cdu'      ,800 )";
	
		private String sql_insert08_21=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	        ,'way'    ,'judge' ,'rank' ,'unit','node_name','id') values " +
				"('sg_'	   ,'hvac_psntempselect'	,'48|5@0+','0.5,18','18|32'  ,'\"\"','cdu'    ,800 )";
///////
	private String sql_insert09=
			"insert into can_message ('bo_flag','id','message_name','dlc','node_name') values ('bo_',801,'hvac_2',8,'hvac')";
		private String sql_insert09_01=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	    ,'way'    ,'judge' ,'rank' ,'unit','node_name','id') values " +
				"('sg_'	   ,'hvac_rawcabintemp'	,'7|8@0+','0.5,-40','-40|87.5'  ,'\"℃\"','cdu'    ,801 )";
		private String sql_insert09_02=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	    		,'way'    ,'judge'  ,'rank'    ,'unit' ,'node_name','id') values " +
				"('sg_'	   ,'hvac_collectedcabintemp'	,'15|8@0+','0.5,-40','-40|87.5','\"℃\"','cdu'      ,801 )";
		private String sql_insert09_03=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	    	,'way'    ,'judge','rank','unit' ,'node_name','id') values " +
				"('sg_'	   ,'hvac_rawcabintempvd'	,'19|1@0+','1,0'  ,'0|1' ,'\"\"','cdu'      ,801 )";
		private String sql_insert09_04=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	    	   ,'way'     ,'judge','rank'  ,'unit' ,'node_name','id') values " +
				"('sg_'	   ,'hvac_compressorcomsumppwr','17|10@0+','10,0' ,'0|8000','\"w\"','bcm'      ,801 )";
		private String sql_insert09_05=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	,'way'     ,'judge','rank'  ,'unit' ,'node_name','id') values " +
				"('sg_'	   ,'hvac_ptcpwract','33|10@0+','10,0' ,'0|8000','\"w\"','bcm'      ,801 )";
		private String sql_insert09_06=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	,'way'    ,'judge','rank','unit' ,'node_name','id') values " +
				"('sg_'	   ,'hvac_stptcact' ,'55|3@0+','1,0'  ,'0|1' ,'\"\"' ,'bcm'      ,801 )";
		private String sql_insert09_07=
				"insert into can_signal " +
				"('sg_flag','signal_name'  				,'way'    ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_correctedcabintempvd' ,'18|1@0+','1,0'  ,'0|1' ,'\"\"','cdu'      ,801 )";
///////
	private String sql_insert10=
			"insert into can_message ('bo_flag','id','message_name','dlc','node_name') values ('bo_',797,'hvac_3',8,'hvac')";
		private String sql_insert10_01=
				"insert into can_signal " +
				"('sg_flag','signal_name'  ,'way'   ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_accfgst' ,'0|1@0+','1,0'  ,'0|1' ,'\"\"','cdu'      ,797 )";
		private String sql_insert10_02=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	   ,'way'   ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_aircircfgst' ,'1|1@0+','1,0'  ,'0|1' ,'\"\"','cdu'      ,797 )";
		private String sql_insert10_03=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	   ,'way'   ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_comfortcfgst' ,'3|2@0+','1,0'  ,'0|1' ,'\"\"','cdu'     ,797 )";
////////		
	private String sql_insert11=
			"insert into can_message ('bo_flag','id','message_name','dlc','node_name') values ('bo_',864,'hvac_4',8,'acp')";
		private String sql_insert11_01=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	    ,'way'   ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_acpcommandvd' ,'0|1@0+','1,0'  ,'0|1' ,'\"\"','acp'      ,864 )";
		private String sql_insert11_02=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	  ,'way'   ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_acpcommand' ,'2|2@0+','1,0'  ,'0|3' ,'\"\"','acp'      ,864 )";
		private String sql_insert11_03=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	  ,'way'   ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_acpspeedset' ,'14|7@0+','100,0'  ,'0|8600' ,'\"\"','acp'      ,864 )";
		private String sql_insert11_04=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	  ,'way'   ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_acphighsidepress' ,'21|6@0+','0.5,0'  ,'0|31' ,'\"\"','acp'      ,864 )";
		private String sql_insert11_05=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	  	,'way'    ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_ptcpowerratio','31|8@0+','1,0'  ,'0|100' ,'\"\"','ptc'      ,864 )";
		private String sql_insert11_06=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	  	,'way'    ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'hvac_checksum','39|8@0+','1,0'  ,'155|255' ,'\"\"','ptc'      ,864 )";
		
////////	
	private String sql_insert12=
			"insert into can_message ('bo_flag','id','message_name','dlc','node_name') values ('bo_',867,'acp_1',8,'acp')";
	
		private String sql_insert12_01=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	  	,'way'    ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'acp_speed','6|7@0+','100,0'  ,'0|8600' ,'\"\"','hvac'      ,867 )";
		private String sql_insert12_02=
				"insert into can_signal " +
				"('sg_flag','signal_name'  	  	,'way'    ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'acp_comsumppwr','15|10@0+','10,0'  ,'0|8000' ,'\"\"','hvac'      ,867 )";
		private String sql_insert12_03=
				"insert into can_signal " +
				"('sg_flag','signal_name','way'    ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'acp_current','16|9@0+','0.1,0'  ,'0|51' ,'\"\"','hvac'      ,867 )";
		private String sql_insert12_04=
				"insert into can_signal " +
				"('sg_flag','signal_name','way'    ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'acp_mototemp','39|8@0+','1,-40'  ,'-40|140' ,'\"\"','hvac'      ,867 )";
		private String sql_insert12_05=
				"insert into can_signal " +
				"('sg_flag','signal_name','way'    ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'acp_hearbeat','55|4@0+','1,0'  ,'0|15' ,'\"\"','hvac'      ,867 )";
		private String sql_insert12_06=
				"insert into can_signal " +
				"('sg_flag','signal_name','way'    ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'acp_extstate','58|3@0+','1,0'  ,'0|7' ,'\"\"','hvac'      ,867 )";
		private String sql_insert12_07=
				"insert into can_signal " +
				"('sg_flag','signal_name','way'    ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'acp_failgrade','60|2@0+','1,0'  ,'0|3' ,'\"\"','hvac'      ,867 )";
		private String sql_insert12_08=
				"insert into can_signal " +
				"('sg_flag','signal_name','way'    ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'acp_basestate','63|3@0+','1,0'  ,'0|7' ,'\"\"','hvac'      ,867 )";
/////////		
	private String sql_insert13=
			"insert into can_message ('bo_flag','id','message_name','dlc','node_name') values ('bo_',868,'ptc_1',8,'ptc')";
		private String sql_insert13_01=
				"insert into can_signal " +
				"('sg_flag','signal_name','way'    ,'judge','rank','unit','node_name','id') values " +
				"('sg_'	   ,'ptc_elementerror','7|4@0+','1,0'  ,'0|15' ,'\"\"','hvac'      ,868 )";
		private String sql_insert13_02=
				"insert into can_signal " +
				"('sg_flag','signal_name'		 ,'way'   ,'judge','rank','unit','node_name' ,'id') values " +
				"('sg_'	   ,'ptc_temperatureover','3|1@0+','1,0'  ,'0|1' ,'\"\"','hvac'      ,868 )";
		private String sql_insert13_03=
				"insert into can_signal " +
				"('sg_flag','signal_name'	  ,'way'   ,'judge','rank','unit','node_name' ,'id') values " +
				"('sg_'	   ,'ptc_voltagefault','2|1@0+','1,0'  ,'0|1' ,'\"\"','hvac'      ,868 )";
		private String sql_insert13_04=
				"insert into can_signal " +
				"('sg_flag','signal_name'	  ,'way'   ,'judge','rank','unit','node_name' ,'id') values " +
				"('sg_'	   ,'ptc_internalerror','1|2@0+','1,0'  ,'0|3' ,'\"\"','hvac'      ,868 )";
		private String sql_insert13_05=
				"insert into can_signal " +
				"('sg_flag','signal_name'	  ,'way'   ,'judge','rank','unit','node_name' ,'id') values " +
				"('sg_'	   ,'ptc_current','15|8@0+','0.2,0'  ,'0|25.4' ,'\"A\"','hvac'      ,868 )";
		private String sql_insert13_06=
				"insert into can_signal " +
				"('sg_flag','signal_name'	  ,'way'   ,'judge','rank','unit','node_name' ,'id') values " +
				"('sg_'	   ,'ptcpwract','23|10@0+','10,0'  ,'0|8000' ,'\"w\"','hvac'      ,868 )";
		private String sql_insert13_07=
				"insert into can_signal " +
				"('sg_flag','signal_name'	  ,'way'   ,'judge','rank','unit','node_name' ,'id') values " +
				"('sg_'	   ,'ptcactst','26|3@0+','1,0'  ,'0|7' ,'\"\"','hvac'      ,868 )";
	//	数据库管理类的构造函数;
	public MDbHelper(Context context, String name, int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		//	01.CAN信息表;
		db.execSQL(sql_caninfo);
		//	02.创建CANmessage表;
		db.execSQL(sql_canmessage);
		//	03.创建CANsignal表;
		db.execSQL(sql_cansignal);
		db.execSQL(sql_insert01);
		db.execSQL(sql_insert01_01);
		db.execSQL(sql_insert01_02);
		db.execSQL(sql_insert01_03);
		db.execSQL(sql_insert01_04);
		db.execSQL(sql_insert01_05);
		db.execSQL(sql_insert01_06);
		db.execSQL(sql_insert01_07);
		db.execSQL(sql_insert01_08);
		db.execSQL(sql_insert01_09);
		db.execSQL(sql_insert01_10);
		db.execSQL(sql_insert01_11);
		db.execSQL(sql_insert01_12);
		db.execSQL(sql_insert01_13);
		db.execSQL(sql_insert01_14);
		db.execSQL(sql_insert01_15);
		db.execSQL(sql_insert01_16);
		db.execSQL(sql_insert01_17);
		db.execSQL(sql_insert01_18);
		db.execSQL(sql_insert01_19);
		db.execSQL(sql_insert01_20);
		db.execSQL(sql_insert01_21);
		db.execSQL(sql_insert01_22);
		db.execSQL(sql_insert02);
		db.execSQL(sql_insert02_01);
		db.execSQL(sql_insert02_02);
		db.execSQL(sql_insert02_03);
		db.execSQL(sql_insert03);
		db.execSQL(sql_insert03_01);
		db.execSQL(sql_insert03_02);
		db.execSQL(sql_insert03_03);
		db.execSQL(sql_insert03_04);
		db.execSQL(sql_insert03_05);
		db.execSQL(sql_insert03_06);
		db.execSQL(sql_insert03_07);
		db.execSQL(sql_insert03_08);
		db.execSQL(sql_insert04);
		db.execSQL(sql_insert04_01);
		db.execSQL(sql_insert04_02);
		db.execSQL(sql_insert04_03);
		db.execSQL(sql_insert04_04);
		db.execSQL(sql_insert04_05);
		db.execSQL(sql_insert04_06);
		db.execSQL(sql_insert04_07);
		db.execSQL(sql_insert04_08);
		db.execSQL(sql_insert05);
		db.execSQL(sql_insert05_01);
		db.execSQL(sql_insert06);
		db.execSQL(sql_insert06_01);
		db.execSQL(sql_insert06_02);
		db.execSQL(sql_insert07);
		db.execSQL(sql_insert07_01);
		db.execSQL(sql_insert07_02);
		db.execSQL(sql_insert07_03);
		db.execSQL(sql_insert07_04);
		db.execSQL(sql_insert07_05);
		db.execSQL(sql_insert07_06);
		db.execSQL(sql_insert08);
		db.execSQL(sql_insert08_01);
		db.execSQL(sql_insert08_02);
		db.execSQL(sql_insert08_03);
		db.execSQL(sql_insert08_04);
		db.execSQL(sql_insert08_05);
		db.execSQL(sql_insert08_06);
		db.execSQL(sql_insert08_07);
		db.execSQL(sql_insert08_08);
		db.execSQL(sql_insert08_09);
		db.execSQL(sql_insert08_10);
		db.execSQL(sql_insert08_11);
		db.execSQL(sql_insert08_12);
		db.execSQL(sql_insert08_13);
		db.execSQL(sql_insert08_14);
		db.execSQL(sql_insert08_15);
		db.execSQL(sql_insert08_16);
		db.execSQL(sql_insert08_17);
		db.execSQL(sql_insert08_18);
		db.execSQL(sql_insert08_19);
		db.execSQL(sql_insert08_20);
		db.execSQL(sql_insert08_21);
		db.execSQL(sql_insert09);
		db.execSQL(sql_insert09_01);
		db.execSQL(sql_insert09_02);
		db.execSQL(sql_insert09_03);
		db.execSQL(sql_insert09_04);
		db.execSQL(sql_insert09_05);
		db.execSQL(sql_insert09_06);
		db.execSQL(sql_insert09_07);
		db.execSQL(sql_insert10);
		db.execSQL(sql_insert10_01);
		db.execSQL(sql_insert10_02);
		db.execSQL(sql_insert10_03);
		db.execSQL(sql_insert11);
		db.execSQL(sql_insert11_01);
		db.execSQL(sql_insert11_02);
		db.execSQL(sql_insert11_03);
		db.execSQL(sql_insert11_04);
		db.execSQL(sql_insert11_05);
		db.execSQL(sql_insert11_06);
		db.execSQL(sql_insert12);
		db.execSQL(sql_insert12_01);
		db.execSQL(sql_insert12_02);
		db.execSQL(sql_insert12_03);
		db.execSQL(sql_insert12_04);
		db.execSQL(sql_insert12_05);
		db.execSQL(sql_insert12_06);
		db.execSQL(sql_insert12_07);
		db.execSQL(sql_insert12_08);
		db.execSQL(sql_insert13);
		db.execSQL(sql_insert13_01);
		db.execSQL(sql_insert13_02);
		db.execSQL(sql_insert13_03);
		db.execSQL(sql_insert13_04);
		db.execSQL(sql_insert13_05);
		db.execSQL(sql_insert13_06);
		db.execSQL(sql_insert13_07);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {


	}
}
