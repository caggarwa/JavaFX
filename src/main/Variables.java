package main;

public class Variables {
	private String Environment;
	private String LOB="";
	private String FromDate="";
	private String ToDate="";
	private String ReportPath="";
	private String CustomerNumber="";

	public String getEnvironment() {
		return Environment;
	}

	public void setEnvironment(String environment) {
		Environment = environment;
	}

	public String getLOB() {
		return LOB;
	}

	public void setLOB(String lOB) {
		LOB = lOB;
	}

	public String getFromDate() {
		return FromDate;
	}

	public void setFromDate(String fromDate) {
		FromDate = fromDate;
	}

	public String getToDate() {
		return ToDate;
	}

	public void setToDate(String toDate) {
		ToDate = toDate;
	}

	public String getReportPath() {
		return ReportPath;
	}

	public void setReportPath(String reportPath) {
		ReportPath = reportPath;
	}

	public String getCustomerNumber() {
		return CustomerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		CustomerNumber = customerNumber;
	}

}
