package main;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import utility.Env;
import utility.OracleDBUtils;

public class DataAnalyzer {
	private Connection connect=null;
	
	public Connection getConnect() {
		return connect;
	}

	public void setConnect(Connection connect) {
		this.connect = connect;
	}


	static String DBSchema="";
	static OracleDBUtils objOracleDB=new OracleDBUtils();
	
	public DataAnalyzer(String sEnv,String UserName, String Password)	{
		try{
			Env.setEnvName(sEnv);
			System.out.println("Environment is set to: " + Env.getEnvName());		
			Env.setProp(Env.getEnvProperties(Env.getEnvName()));
			Properties Prop = Env.getProp();
			DBSchema=Prop.getProperty("BRMS_DataBase_Schema");

						
			Connection	BRMS_DBConnect = objOracleDB.ConnectOracle(Prop.getProperty("BRMS_DBServer"), Prop.getProperty("BRMS_DBPort"), Prop.getProperty("BRMS_DBSID"), UserName, Password);		
			setConnect(BRMS_DBConnect);		
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	public String connectToDB(String sEnv,String UserName, String Password){
		String Status="";
		try{
			Env.setEnvName(sEnv);
			System.out.println("Environment is set to: " + Env.getEnvName());		
			Env.setProp(Env.getEnvProperties(Env.getEnvName()));
			Properties Prop = Env.getProp();
			DBSchema=Prop.getProperty("BRMS_DataBase_Schema");

						
			Connection	BRMS_DBConnect = objOracleDB.ConnectOracle(Prop.getProperty("BRMS_DBServer"), Prop.getProperty("BRMS_DBPort"), Prop.getProperty("BRMS_DBSID"), UserName, Password);		
			
//			connect= BRMS_DBConnect;
			
			if ((!BRMS_DBConnect.isClosed()) || (BRMS_DBConnect != null)) {
				Status="Oracle connection is established.";
				this.setConnect(BRMS_DBConnect);
			} 
			else 
				Status="Oracle connection is Not established.";
			
			
		}
		catch(Exception ex){
			Status="Oracle connection Failed -" + ex.getMessage() ;
		}
		
		return Status;
		
	}
	
	
	public String connectToDB(){
		String Status="";
		try{
			
			
			if ((!getConnect().isClosed()) || (getConnect() != null)) {
				Status="Oracle connection is established.";
				
			} 
			else 
				Status="Oracle connection is Not established.";
			
			
		}
		catch(Exception ex){
			Status="Oracle connection Failed -" + ex.getMessage() ;
		}
		
		return Status;
		
	}
	
	public String  getTotalMebership(Connection DBconnect,String FromDate, String toDate, String CustomerNumber, String sLOB){
		String totalMemberships="";
		try{
			
			String ssql= "select To_char(count (distinct cm_ext_source_id)) as TotalMembers from "+DBSchema+".cm_inbound_message where src_system_cd = '"+sLOB+"'  and ILM_DT>='sCreationDate' and ILM_DT<='sToDate'  and CM_INBOUND_MSG_TYPE_CD like '%MEMBERSHIP%' and cm_ext_source_id like '"+CustomerNumber+"%'";
			
			ssql=ssql.replace("sCreationDate", FromDate);
			ssql=ssql.replace("sToDate", toDate);
			
			List<HashMap<String, String>> DBRecords=new ArrayList<HashMap<String, String>>();
			DBRecords=objOracleDB.getDBRecords(DBconnect, ssql);
			
			if (DBRecords.size()>0){
				totalMemberships=DBRecords.get(0).get("TOTALMEMBERS");
			}
		}
		catch(Exception ex){
			
		}
		return totalMemberships;
	}
	
	
	
	public String  getTotalProcessedMebership(Connection DBconnect,String FromDate, String toDate, String CustomerNumber, String sLOB){
		String totalProcessedMemberships="";
		try{
			
			String ssql= "select To_char(count (distinct cm_ext_source_id)) as TotalMembers from "+DBSchema+".cm_inbound_message where src_system_cd = '"+sLOB+"'  and ILM_DT>='sCreationDate' and ILM_DT<='sToDate'  and bo_status_Cd = 'PROCESSED' and CM_INBOUND_MSG_TYPE_CD like '%MEMBERSHIP%' and cm_ext_source_id like '"+CustomerNumber+"%'";
			
			ssql=ssql.replace("sCreationDate", FromDate);
			ssql=ssql.replace("sToDate", toDate);
			
			List<HashMap<String, String>> DBRecords=new ArrayList<HashMap<String, String>>();
			DBRecords=objOracleDB.getDBRecords(DBconnect, ssql);
			
			if (DBRecords.size()>0){
				totalProcessedMemberships=DBRecords.get(0).get("TOTALMEMBERS");
			}
		}
		catch(Exception ex){
			
		}
		return totalProcessedMemberships;
	}
	
	public String  getTotalUnProcessedMebership(Connection DBconnect,String FromDate, String toDate, String CustomerNumber, String sLOB){
		String totalUnProcessedMemberships="";
		try{		
			String ssql= "select To_char(count (distinct cm_ext_source_id)) as TotalMembers from "+DBSchema+".cm_inbound_message where src_system_cd = '"+sLOB+"'  and ILM_DT>='sCreationDate' and ILM_DT<='sToDate'  and CM_INBOUND_MSG_TYPE_CD like '%MEMBERSHIP%' and bo_status_Cd in ('PENDING','REJECTED') and cm_ext_source_id like '"+CustomerNumber+"%'";
			
			ssql=ssql.replace("sCreationDate", FromDate);
			ssql=ssql.replace("sToDate", toDate);
			
			List<HashMap<String, String>> DBRecords=new ArrayList<HashMap<String, String>>();
			DBRecords=objOracleDB.getDBRecords(DBconnect, ssql);
			
			if (DBRecords.size()>0){
				totalUnProcessedMemberships=DBRecords.get(0).get("TOTALMEMBERS");
			}
		}
		catch(Exception ex){
			
		}
		return totalUnProcessedMemberships;
	}
	
	public List<HashMap<String, String>>  getTotalCustomers(String CreationDate, String sLOB){
	
		List<HashMap<String, String>> DBRecords=new ArrayList<HashMap<String, String>>();
		try{		
			String ssql= "Select distinct SUBSTR(CM_EXT_SOURCE_ID,1,7) as CustomerNumbr from "+DBSchema+".cm_inbound_message " + 
					"where src_system_cd = '"+sLOB+"' and cre_dttm>='sCreationDate' ";
			
			ssql=ssql.replace("sCreationDate", CreationDate);
			System.out.println(ssql);
			DBRecords=objOracleDB.getDBRecords(this.getConnect(), ssql);
			
		}
		catch(Exception ex){
			
		}
		return DBRecords;
	}
	
	public List<HashMap<String, String>>  getTotalCustomers(String FromDate, String toDate,String sLOB){
		
		List<HashMap<String, String>> DBRecords=new ArrayList<HashMap<String, String>>();
		try{		
			String ssql= "Select distinct SUBSTR(CM_EXT_SOURCE_ID,1,7) as CustomerNumbr,CM_EXT_SOURCE_ID,NVL(to_char(ILM_DT),'') as ILM_Date,BO_Status_CD  from "+DBSchema+".cm_inbound_message " + 
					"where src_system_cd = '"+sLOB+"' and ILM_DT>='sCreationDate' and ILM_DT<='sToDate' and BO_Status_CD in ('PROCESSED','PENDING','REJECTED')";
			
			ssql=ssql.replace("sCreationDate", FromDate);
			ssql=ssql.replace("sToDate", toDate);
			System.out.println(ssql);
			DBRecords=objOracleDB.getDBRecords(this.getConnect(), ssql);
			
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return DBRecords;
	}
	
	
	
	public List<HashMap<String, String>> getMembershipErrorMessages(String CustomerNumber, String sLOB){
		List<HashMap<String, String>> DBRecords=new ArrayList<HashMap<String, String>>();
		
		try{
			
			String sql="Select distinct cim.cm_ext_source_id, TDD.TD_ENTRY_ID  ,  " + 
					"Replace( " + 
					"  Replace( " + 
					"      Replace( " + 
					"        Replace " + 
					"            (Replace(ML.message_text,'%5' ,(Select MSG_PARM_VAL from  "+DBSchema+".CI_TD_MSG_PARM where TD_ENTRY_ID=TDD.TD_ENTRY_ID and SEQ_NUM='5')),'%4', " + 
					"                (Select MSG_PARM_VAL from  "+DBSchema+".CI_TD_MSG_PARM where TD_ENTRY_ID=TDD.TD_ENTRY_ID and SEQ_NUM='4') " + 
					"        ),'%3',(Select MSG_PARM_VAL from  "+DBSchema+".CI_TD_MSG_PARM where TD_ENTRY_ID=TDD.TD_ENTRY_ID and SEQ_NUM='3') " + 
					"      ),'%2',(Select MSG_PARM_VAL from  "+DBSchema+".CI_TD_MSG_PARM where TD_ENTRY_ID=TDD.TD_ENTRY_ID and SEQ_NUM='2') " + 
					"  ),'%1',(Select MSG_PARM_VAL from  "+DBSchema+".CI_TD_MSG_PARM where TD_ENTRY_ID=TDD.TD_ENTRY_ID and SEQ_NUM='1') " + 
					") as ERROR_MSG " + 
					"from "+DBSchema+".cm_inbound_message cim " + 
					"join "+DBSchema+".CM_INBOUND_MESSAGE_LOG ciml on ciml.CM_INBOUND_MESSAGE_ID=cim.CM_INBOUND_MESSAGE_ID and ciml.log_entry_type_flg = 'F1EX' " + 
					"join "+DBSchema+".ci_msg_l ML on ML.message_cat_nbr = ciml.message_cat_nbr and ML.message_nbr = ciml.message_nbr " + 
					"join  "+DBSchema+".CI_TD_DRLKEY TDD on TDD.KEY_VALUE=cim.CM_INBOUND_MESSAGE_ID " + 
					"join "+DBSchema+".ci_TD_Entry TDE on TDE.TD_ENTRY_ID= TDD.TD_ENTRY_ID "+
					"where cim.src_system_Cd = '"+sLOB+"' and cim.bo_status_Cd in ('PENDING','REJECTED')  " + 
					"and  cim.cm_ext_source_id like '"+CustomerNumber+"%' " + 
					" and TDE.CRE_DTTM = ( select max(a.CRE_DTTM) from "+DBSchema+".ci_TD_Entry a, "+DBSchema+".CI_TD_DRLKEY b where b.TD_ENTRY_ID = a.TD_ENTRY_ID and b.KEY_VALUE = cim.CM_INBOUND_MESSAGE_ID) " +
					" and ciml.LOG_DTTM = (Select max(LOG_DTTM) from "+DBSchema+".CM_INBOUND_MESSAGE_LOG where cm_inbound_message_id=cim.CM_INBOUND_MESSAGE_ID) " +
					" and cim.ILM_DT=(Select max(ILM_DT) from "+DBSchema+".cm_inbound_message where CM_EXT_SOURCE_ID=cim.cm_ext_source_id) " +
					"order by cim.cm_ext_source_id desc";
			
			System.out.println(sql);
			DBRecords=objOracleDB.getDBRecords(getConnect(), sql);
			
		}
		catch(Exception ex){
			DBRecords=null;
		}
		
		return DBRecords;
	}
	
	public List<HashMap<String, String>> getCustomerErrorMessagesDetail(String Customer, String sLOB){//String From_DT, String To_DT
		List<HashMap<String, String>> DBRecords=new ArrayList<HashMap<String, String>>();
		
		try{
			
			String sql="Select distinct cim.cm_ext_source_id, cim.bo_status_Cd, NVL(to_char(cim.ILM_DT),'') as ILM_Date,    " + 
					"Replace( " + 
					"  Replace( " + 
					"      Replace( " + 
					"        Replace " + 
					"            (Replace(ML.message_text,'%5' ,(Select MSG_PARM_VAL from  "+DBSchema+".CI_TD_MSG_PARM where TD_ENTRY_ID=TDD.TD_ENTRY_ID and SEQ_NUM='5')),'%4', " + 
					"                (Select MSG_PARM_VAL from  "+DBSchema+".CI_TD_MSG_PARM where TD_ENTRY_ID=TDD.TD_ENTRY_ID and SEQ_NUM='4') " + 
					"        ),'%3',(Select MSG_PARM_VAL from  "+DBSchema+".CI_TD_MSG_PARM where TD_ENTRY_ID=TDD.TD_ENTRY_ID and SEQ_NUM='3') " + 
					"      ),'%2',(Select MSG_PARM_VAL from  "+DBSchema+".CI_TD_MSG_PARM where TD_ENTRY_ID=TDD.TD_ENTRY_ID and SEQ_NUM='2') " + 
					"  ),'%1',(Select MSG_PARM_VAL from  "+DBSchema+".CI_TD_MSG_PARM where TD_ENTRY_ID=TDD.TD_ENTRY_ID and SEQ_NUM='1') " + 
					") as ERROR_MSG " + 
					"from "+DBSchema+".cm_inbound_message cim " + 
					"join "+DBSchema+".CM_INBOUND_MESSAGE_LOG ciml on ciml.CM_INBOUND_MESSAGE_ID=cim.CM_INBOUND_MESSAGE_ID and ciml.log_entry_type_flg = 'F1EX' " + 
					"join "+DBSchema+".ci_msg_l ML on ML.message_cat_nbr = ciml.message_cat_nbr and ML.message_nbr = ciml.message_nbr " + 
					"join  "+DBSchema+".CI_TD_DRLKEY TDD on TDD.KEY_VALUE=cim.CM_INBOUND_MESSAGE_ID " + 
					"join "+DBSchema+".ci_TD_Entry TDE on TDE.TD_ENTRY_ID= TDD.TD_ENTRY_ID "+
					"where cim.src_system_Cd = '"+sLOB+"' and cim.bo_status_Cd in ('PENDING','REJECTED')  " + 
					"and  cim.cm_ext_source_id = ('"+Customer+"' ) " + 
					" and TDE.CRE_DTTM = ( select max(a.CRE_DTTM) from "+DBSchema+".ci_TD_Entry a, "+DBSchema+".CI_TD_DRLKEY b where b.TD_ENTRY_ID = a.TD_ENTRY_ID and b.KEY_VALUE = cim.CM_INBOUND_MESSAGE_ID) " +
					" and ciml.LOG_DTTM = (Select max(LOG_DTTM) from "+DBSchema+".CM_INBOUND_MESSAGE_LOG where cm_inbound_message_id=cim.CM_INBOUND_MESSAGE_ID) " +
					" and cim.ILM_DT=(Select max(ILM_DT) from "+DBSchema+".cm_inbound_message where CM_EXT_SOURCE_ID=cim.cm_ext_source_id) " +
					"order by cim.cm_ext_source_id desc";
			
			System.out.println(sql);
			DBRecords=objOracleDB.getDBRecords(getConnect(), sql);
			
		}
		catch(Exception ex){
			DBRecords=null;
		}
		
		return DBRecords;
	}
	
	
	public List<HashMap<String, String>> getErrorMessagesLookUp(String TODO_EntryID){
		List<HashMap<String, String>> DBRecords=new ArrayList<HashMap<String, String>>();
		
		try{
			String sql="Select SEQ_NUM,MSG_PARM_VAL " + 
					"from  "+DBSchema+".CI_TD_MSG_PARM  " + 
					"where TD_ENTRY_ID='"+TODO_EntryID+"'" ;
					
			DBRecords=objOracleDB.getDBRecords(connect, sql);
			
		}
		catch(Exception ex){
			DBRecords=null;
		}
		
		return DBRecords;
	}
	
	
	
	

}
