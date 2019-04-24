package main;

import java.awt.Dimension;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.concurrent.Task;
import utility.HtmlReport;
import utility.StringUtil;

// To initialize a new thread/Task
public class SummaryTask extends Task<String> {
	String FromDate;
	String ToDate;
	List<HashMap<String, String>> ListCustomers;
	String ReportPath;
	String LOB;
	DataAnalyzer dataAnalyzer ;
	
	SummaryTask(DataAnalyzer objDA,String from_DT,String To_Date, String reportPath, String sLOB ){
		this.dataAnalyzer=objDA;
		this.ToDate=To_Date;
		this.FromDate=from_DT;
		this.ReportPath=reportPath;
		
		switch (sLOB){
		
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

		String CustomerNumber="";
		String TotalMembersReceived="";
		String TotalProcessedMembers="";
		String TotalPendingRejectedMembers="";

		ListCustomers=dataAnalyzer.getTotalCustomers(FromDate,ToDate,LOB);

		String timestamp=StringUtil.getTimeStamp();
		String ReportName=LOB +"_Data_Analyzer_SummaryReport"  + timestamp +".html";

		if (ReportPath.contains("\\"))
			ReportPath.replace("\\", "\\");

		new HtmlReport();
		HtmlReport.htmlFilePath=ReportPath + File.separator+ReportName;

		
		for (int iList=0; iList<ListCustomers.size(); iList++){
			
			CustomerNumber=ListCustomers.get(iList).get("CUSTOMERNUMBR");
			TotalMembersReceived=dataAnalyzer.getTotalMebership(dataAnalyzer.getConnect(),FromDate,ToDate,CustomerNumber,LOB);
			TotalProcessedMembers=dataAnalyzer.getTotalProcessedMebership(dataAnalyzer.getConnect(),FromDate,ToDate,CustomerNumber,LOB);
			TotalPendingRejectedMembers=dataAnalyzer.getTotalUnProcessedMebership(dataAnalyzer.getConnect(),FromDate,ToDate,CustomerNumber,LOB);

			HtmlReport.AddSteps(CustomerNumber,TotalMembersReceived,TotalProcessedMembers,TotalPendingRejectedMembers);

			this.updateProgress(iList, ListCustomers.size()-1);
		}
		
		if (ListCustomers.size()==0){
			this.updateProgress(0, ListCustomers.size());
			
		}
		
		if (ListCustomers.size()==1){
			this.updateProgress(1, ListCustomers.size());
			
		}

		HtmlReport.endHtmlReport();
		
		if (ListCustomers.size()==0){
			HtmlReport.htmlFilePath="Empty Records";
		}
		return HtmlReport.htmlFilePath;

	} 

}
