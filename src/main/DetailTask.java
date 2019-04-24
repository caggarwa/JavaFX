package main;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import javafx.concurrent.Task;
import utility.HtmlReport;
import utility.StringUtil;

public class DetailTask extends Task<String> {
	String Custome_Num;
	String LOB;
	List<HashMap<String, String>>  ListDetailErrorMsg;
	String ReportPath;
	
	DataAnalyzer dataAnalyzer ;

	DetailTask(DataAnalyzer dataAnalyzer,String Custome_Numbr,String reportPath, String LOB ){
		this.dataAnalyzer=dataAnalyzer;
		
		this.Custome_Num=Custome_Numbr;
		this.ReportPath=reportPath;
		
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

		String timestamp=StringUtil.getTimeStamp();
		String ReportName=this.LOB +"_DataAnalyzer_DetailReport"  + timestamp +".html";

		if (ReportPath.contains("\\"))
			ReportPath.replace("\\", "\\");

		HtmlReport.DetailHtmlReport();
		HtmlReport.htmlFilePath=ReportPath + File.separator+ReportName;

		
		
		
		ListDetailErrorMsg=dataAnalyzer.getMembershipErrorMessages(Custome_Num, LOB);
		HtmlReport.AddSection("Customer Number: " + Custome_Num );
		for (int iList=0; iList<ListDetailErrorMsg.size(); iList++){
			ExternalSourceID=ListDetailErrorMsg.get(iList).get("CM_EXT_SOURCE_ID");
			Error_Msg=ListDetailErrorMsg.get(iList).get("ERROR_MSG");
			
			HtmlReport.AddSteps("Customer Number: '" + Custome_Num + "'",ExternalSourceID,Error_Msg);
			
			this.updateProgress(iList, ListDetailErrorMsg.size()-1);
		}
		HtmlReport.closingsection();
		
		if (ListDetailErrorMsg.size()==0){
			this.updateProgress(0, ListDetailErrorMsg.size());
			
		}
		
		if (ListDetailErrorMsg.size()==1){
			this.updateProgress(1, ListDetailErrorMsg.size());
			
		}
		
		HtmlReport.endHtmlReport();
		
		if (ListDetailErrorMsg.size()==0){
			HtmlReport.htmlFilePath="Empty Records";
		}
		System.out.println(HtmlReport.htmlFilePath);
		
		return HtmlReport.htmlFilePath;

	} 

}

