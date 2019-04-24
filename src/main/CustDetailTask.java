package main;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import javafx.concurrent.Task;
import utility.HtmlReport;
import utility.StringUtil;

public class CustDetailTask extends Task<String> {
	String From_DT;
	String To_DT;
	String LOB;
	List<HashMap<String, String>>  ListDetailErrorMsg;
	List<HashMap<String, String>>  ListCustomers;
	String ReportPath;

	DataAnalyzer dataAnalyzer ;

	CustDetailTask(DataAnalyzer objDA,String from_DT,String To_Date, String reportPath, String sLOB  ){
		this.dataAnalyzer=objDA;
		this.To_DT=To_Date;
		this.From_DT=from_DT;
		this.ReportPath=reportPath;
		this.LOB=sLOB;

		switch (LOB){

		case "ACIS":
			this.LOB="AC";
			break;

		case "PRIME":
			this.LOB="PR";
			break;

		case "CIRRUS":
			this.LOB="CR";
			break;

		case "CDB":
			this.LOB="CDB";
			break;
		}
	}


	@Override
	protected String call() throws Exception {

		String ExternalSourceID="";
		String Error_Msg="";
		String ILM_DT="";
		String Status="";

		String timestamp=StringUtil.getTimeStamp();
		String ReportName=this.LOB +"_DataAnalyzer_CustDetailReport"  + timestamp +".html";

		if (ReportPath.contains("\\"))
			ReportPath.replace("\\", "\\");

		HtmlReport.CustDetailHtmlReport();
		HtmlReport.htmlFilePath=ReportPath + File.separator+ReportName;


		ListCustomers=dataAnalyzer.getTotalCustomers(From_DT, To_DT, LOB);
		for (int icust=0;icust<ListCustomers.size();icust++){

			if (ListCustomers.get(icust).get("BO_STATUS_CD").trim().toUpperCase().contains("PROCESSED")){
				ExternalSourceID=ListCustomers.get(icust).get("CM_EXT_SOURCE_ID");
				Status=ListCustomers.get(icust).get("BO_STATUS_CD");
				ILM_DT=ListCustomers.get(icust).get("ILM_DATE");
			}
			
			else{
				ListDetailErrorMsg=dataAnalyzer.getCustomerErrorMessagesDetail(ListCustomers.get(icust).get("CUSTOMERNUMBR"), LOB);
				
				for (int iList=0; iList<ListDetailErrorMsg.size(); iList++){
					ExternalSourceID=ListDetailErrorMsg.get(iList).get("CM_EXT_SOURCE_ID");
					Status=ListDetailErrorMsg.get(iList).get("BO_STATUS_CD");
					ILM_DT=ListDetailErrorMsg.get(iList).get("ILM_DATE");
					Error_Msg=ListDetailErrorMsg.get(iList).get("ERROR_MSG");
					
				}
				
				if (ListDetailErrorMsg.size()==0){
					ExternalSourceID=ListCustomers.get(icust).get("CM_EXT_SOURCE_ID");
					Status=ListCustomers.get(icust).get("BO_STATUS_CD");
					ILM_DT=ListCustomers.get(icust).get("ILM_DATE");
				}
			}
			
			if (Status.trim().toUpperCase().equalsIgnoreCase("PROCESSED") || Status.trim().toUpperCase().equalsIgnoreCase("PENDING") || Status.trim().toUpperCase().equalsIgnoreCase("REJECTED"))
				HtmlReport.AddCustDetailSteps("Customer Number: '" + ExternalSourceID + "'",Status,ILM_DT,Error_Msg);
			
			this.updateProgress(icust, ListCustomers.size()-1);
			
			//Refresh the Variable values
			ExternalSourceID="";
			Status="";
			ILM_DT="";
			Error_Msg="";
			
		}
		

		if (ListCustomers.size()==0){
			this.updateProgress(0, ListCustomers.size());

		}
		if (ListCustomers.size()==1){
			this.updateProgress(1, ListCustomers.size());
		}
		HtmlReport.endCustDetailHtmlReport();

		if (ListCustomers.size()==0){
			HtmlReport.htmlFilePath="Empty Records";
		}
		System.out.println(HtmlReport.htmlFilePath);

		return HtmlReport.htmlFilePath;

	} 

}

