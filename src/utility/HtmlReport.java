package utility;

import java.io.File; 
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;



public class HtmlReport {

	public static StringBuilder ts ;
	public static File file = null;
	public static int intpasscnt = 0;
	public static int intfailcnt = 0;
	public static int totalcnt = 0;
	public static String screenShotFolder;
	private static boolean printHeader = true;
	public static List<String> failureList ;
	
	public static int totalProcessed = 0;
	public static int totalPending = 0;
	public static int totalRejected = 0;

	public static String htmlFilePath;
	
	public HtmlReport() {
		if (printHeader) {
			
			try {
				ts = new StringBuilder();
				failureList = new ArrayList<String>();
				header();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//			printHeader = false;

		}
	}
	
	public static void DetailHtmlReport() {
		if (printHeader) {
			
			try {
				ts = new StringBuilder();
				failureList = new ArrayList<String>();
				detailheader();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//			printHeader = false;

		}
	}
	
	public static void CustDetailHtmlReport() {
		if (printHeader) {
			
			try {
				ts = new StringBuilder();
				failureList = new ArrayList<String>();
				custDetailheader();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//			printHeader = false;

		}
	}
	
//	static  {
//		if (printHeader) {
//			
//			try {
//				ts = new StringBuilder();
//				header();
//
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
////			printHeader = false;
//
//		}
//	}

	/**
	 * This method will define the Header Layout for the HTML Report
	 * 
	 * @throws IOException
	 */
	public static void header() throws IOException {

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		
		ts.append("<!DOCTYPE html>");
		ts.append("<html>");
		ts.append("<head>");
		ts.append("<title>DataAnalyser Membership Summary Report</title>");
		ts.append("<h1 bgcolor='#000000' align='center'>DataAnalyser Membership Summary Report</h1>");

		ts.append("<table>");
		ts.append("<tr>");
		ts.append(
				"<td width='10%' bgcolor='orange' align='center'><b><font color='#FFFFFF' face='Castellar' size='2'>Executed on :"
						+ formatter.format(date) + "</font></b></td>");
		ts.append("</tr>");
		ts.append("</table>");
		ts.append("<table border='1' width='100%' CELLPADDING=1 CELLSPACING=1 height='47'>");
		ts.append("<tr>");
		ts.append(
				"<td width='20%' bgcolor='orange'><b><font color='#FFFFFF' face='Castellar' size='2'>Customer Number</font></b></td>");
		ts.append(
				"<td width='20%' bgcolor='orange'><b><font color='#FFFFFF' face='Castellar' size='2'>Count of Members Received</font></b></td>");
		ts.append(
				"<td width='20%' bgcolor='orange'><b><font color='#FFFFFF' face='Castellar' size='2'>Count of Members Processed</font></b></td>");
		ts.append(
				"<td width='20%' bgcolor='orange'><b><font color='#FFFFFF' face='Castellar' size='2'>Count of Members in Pending/Rejected Status</font></b></td>");
		ts.append("</tr>");
		ts.append("</table>");

		ts.append("<style>");
		ts.append("button.accordion {");
		ts.append("background-color: lightgrey;");
		ts.append("color: #444;");
		ts.append("cursor: pointer;");
		ts.append("padding: 10px;");
		ts.append("width: 100%;");
		ts.append("border: 5;");
		ts.append("text-align: left;");
		ts.append("outline: none;");
		ts.append("font-size: 15px;");
		ts.append("transition: 0.4s;");
		ts.append("}");

		ts.append("button.accordion.active, button.accordion:hover {");
		ts.append("background-color: #grey;");
		ts.append("}");

		ts.append("button.accordion:after {");
		ts.append("content: '+';");
		ts.append("font-size: 15px;");
		ts.append("color: #777;");
		ts.append("float: left;");
		ts.append("margin-left: 5px;");

		ts.append("}");

		ts.append("button.accordion.active:after {");
		ts.append("content: '- ';");
		ts.append("}");

		ts.append("div.panel {");
		ts.append("padding: 0 18px;");
		ts.append("background-color: white;");
		ts.append("max-height: 0;");
		ts.append("overflow: hidden;");
		ts.append("transition: 0.6s ease-in-out;");
		ts.append("opacity: 0;");

		ts.append("}");

		ts.append("div.panel.show {");
		ts.append("opacity: 1;");
		ts.append("max-height: 250000px;");
		ts.append("}");
		ts.append("</style>");
		ts.append("</head>");
		ts.append("<body>");

	}
	
	/**
	 * This method will define the Header Layout for the HTML Report
	 * 
	 * @throws IOException
	 */
	public static void detailheader() throws IOException {

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		
		ts.append("<!DOCTYPE html>");
		ts.append("<html>");
		ts.append("<head>");
		ts.append("<title>DataAnalyser Membership Detail Results</title>");
		ts.append("<h1 bgcolor='#000000' align='center'>DataAnalyser Membership Detail Results</h1>");

		ts.append("<table>");
		ts.append("<tr>");
		ts.append(
				"<td width='10%' bgcolor='orange' align='center'><b><font color='#FFFFFF' face='Castellar' size='2'>Executed on :"
						+ formatter.format(date) + "</font></b></td>");
		ts.append("</tr>");
		ts.append("</table>");
		ts.append("<table border='1' width='100%' CELLPADDING=1 CELLSPACING=1 height='47'>");
		ts.append("<tr>");
		ts.append(
				"<td width='23%' bgcolor='orange'><b><font color='#FFFFFF' face='Castellar' size='2'>Customer Number</font></b></td>");
		ts.append(
				"<td width='23%' bgcolor='orange'><b><font color='#FFFFFF' face='Castellar' size='2'>Membership Not Processed</font></b></td>");
		ts.append(
				"<td width='23%' bgcolor='orange'><b><font color='#FFFFFF' face='Castellar' size='2'>Reason for Failure</font></b></td>");
		ts.append("</tr>");
		ts.append("</table>");

		ts.append("<style>");
		ts.append("button.accordion {");
		ts.append("background-color: lightgrey;");
		ts.append("color: #444;");
		ts.append("cursor: pointer;");
		ts.append("padding: 10px;");
		ts.append("width: 100%;");
		ts.append("border: 5;");
		ts.append("text-align: left;");
		ts.append("outline: none;");
		ts.append("font-size: 15px;");
		ts.append("transition: 0.4s;");
		ts.append("}");

		ts.append("button.accordion.active, button.accordion:hover {");
		ts.append("background-color: #grey;");
		ts.append("}");

		ts.append("button.accordion:after {");
		ts.append("content: '+';");
		ts.append("font-size: 15px;");
		ts.append("color: #777;");
		ts.append("float: left;");
		ts.append("margin-left: 5px;");

		ts.append("}");

		ts.append("button.accordion.active:after {");
		ts.append("content: '- ';");
		ts.append("}");

		ts.append("div.panel {");
		ts.append("padding: 0 18px;");
		ts.append("background-color: white;");
		ts.append("max-height: 0;");
		ts.append("overflow: hidden;");
		ts.append("transition: 0.6s ease-in-out;");
		ts.append("opacity: 0;");

		ts.append("}");

		ts.append("div.panel.show {");
		ts.append("opacity: 1;");
		ts.append("max-height: 250000px;");
		ts.append("}");
		ts.append("</style>");
		ts.append("</head>");
		ts.append("<body>");

	}
	
	/**
	 * This method will define the Header Layout for the HTML Report
	 * 
	 * @throws IOException
	 */
	public static void custDetailheader() throws IOException {

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		
		ts.append("<!DOCTYPE html>");
		ts.append("<html>");
		ts.append("<head>");
		ts.append("<title>DataAnalyser Customer Detail Results</title>");
		ts.append("<h1 bgcolor='#000000' align='center'>DataAnalyser Customer Detail Results</h1>");

		ts.append("<table>");
		ts.append("<tr>");
		ts.append(
				"<td width='10%' bgcolor='orange' align='center'><b><font color='#FFFFFF' face='Castellar' size='2'>Executed on :"
						+ formatter.format(date) + "</font></b></td>");
		ts.append("</tr>");
		ts.append("</table>");
		ts.append("<table border='1' width='100%' CELLPADDING=1 CELLSPACING=1 height='47'>");
		ts.append("<tr>");
		ts.append(
				"<td width='23%' bgcolor='orange'><b><font color='#FFFFFF' face='Castellar' size='2'>Customer Number</font></b></td>");
		ts.append(
				"<td width='23%' bgcolor='orange'><b><font color='#FFFFFF' face='Castellar' size='2'>Status</font></b></td>");
		ts.append(
				"<td width='23%' bgcolor='orange'><b><font color='#FFFFFF' face='Castellar' size='2'>ILM Date </font></b></td>");
		ts.append(
				"<td width='23%' bgcolor='orange'><b><font color='#FFFFFF' face='Castellar' size='2'>Reason for Failure</font></b></td>");
		
		ts.append("</tr>");
		ts.append("</table>");

		ts.append("<style>");
		ts.append("button.accordion {");
		ts.append("background-color: lightgrey;");
		ts.append("color: #444;");
		ts.append("cursor: pointer;");
		ts.append("padding: 10px;");
		ts.append("width: 100%;");
		ts.append("border: 5;");
		ts.append("text-align: left;");
		ts.append("outline: none;");
		ts.append("font-size: 15px;");
		ts.append("transition: 0.4s;");
		ts.append("}");

		ts.append("button.accordion.active, button.accordion:hover {");
		ts.append("background-color: #grey;");
		ts.append("}");

		ts.append("button.accordion:after {");
		ts.append("content: '+';");
		ts.append("font-size: 15px;");
		ts.append("color: #777;");
		ts.append("float: left;");
		ts.append("margin-left: 5px;");

		ts.append("}");

		ts.append("button.accordion.active:after {");
		ts.append("content: '- ';");
		ts.append("}");

		ts.append("div.panel {");
		ts.append("padding: 0 18px;");
		ts.append("background-color: white;");
		ts.append("max-height: 0;");
		ts.append("overflow: hidden;");
		ts.append("transition: 0.6s ease-in-out;");
		ts.append("opacity: 0;");

		ts.append("}");

		ts.append("div.panel.show {");
		ts.append("opacity: 1;");
		ts.append("max-height: 250000px;");
		ts.append("}");
		ts.append("</style>");
		ts.append("</head>");
		ts.append("<body>");

	}
	
	/**
	 * This method will report the Validation Count Summary and add
	 * Collapse/Expand action to each Section.
	 */
	public static void footer() {
		ts.append("<script>");
		ts.append("var acc = document.getElementsByClassName('accordion');");
		ts.append("var i;");

		ts.append("for (i = 0; i < acc.length; i++) {");
		ts.append("acc[i].onclick = function(){");
		ts.append("this.classList.toggle('active');");
		ts.append("this.nextElementSibling.classList.toggle('show');");
		ts.append("}");
		ts.append("}");
		ts.append("</script>");

		totalcnt = intpasscnt + intfailcnt;

		ts.append("<hr>");
		ts.append("<table border='1' width='50%'>");
		ts.append(
				"<tr><td width='100%' colspan='2' bgcolor='orange'><b><font face='ARIAL' size='2' color='#FFFFFF'>Summary</font></b></td></tr>");
		ts.append(
				"<tr><td width='45%' bgcolor='#FFFFFF'><b><font color='#000000' face='ARIAL' size='2'>Total Count</font></b></td><td width='55%' bgcolor='#FFFFFF'><font color='#000000'>"
						+ totalcnt + "</font></td></tr>");
//		ts.append(
//				"<tr><td width='45%' bgcolor='#FFFFFF'><b><font color='#000000' face='ARIAL' size='2'>Passed Count</font></b></td><td width='55%' bgcolor='#FFFFFF'><font color='#000000'>"
//						+ intpasscnt + "</font></td></tr>");
//		ts.append(
//				"<tr><td width='45%' bgcolor='#FFFFFF'><b><font color='#000000' face='ARIAL' size='2'>Failed Count</font></b></td><td width='55%' bgcolor='#FFFFFF'><font color='#000000'>"
//						+ intfailcnt + "</font></td></tr>");
//
		ts.append("</table>");

		ts.append("</body>");
		ts.append("</html>");

		writeToFile(htmlFilePath, ts.toString());
		totalcnt=0;
		intfailcnt=0;
		intpasscnt=0;
		

	}
	
	
	/**
	 * This method will report the Validation Count Summary and add
	 * Collapse/Expand action to each Section.
	 */
	public static void CustDetailfooter() {
		ts.append("<script>");
		ts.append("var acc = document.getElementsByClassName('accordion');");
		ts.append("var i;");

		ts.append("for (i = 0; i < acc.length; i++) {");
		ts.append("acc[i].onclick = function(){");
		ts.append("this.classList.toggle('active');");
		ts.append("this.nextElementSibling.classList.toggle('show');");
		ts.append("}");
		ts.append("}");
		ts.append("</script>");

		totalcnt = totalProcessed + totalPending + totalRejected;

		ts.append("<hr>");
		ts.append("<table border='1' width='50%'>");
		ts.append(
				"<tr><td width='100%' colspan='2' bgcolor='orange'><b><font face='ARIAL' size='2' color='#FFFFFF'>Summary</font></b></td></tr>");
		ts.append(
				"<tr><td width='45%' bgcolor='#FFFFFF'><b><font color='#000000' face='ARIAL' size='2'>Total Count</font></b></td><td width='55%' bgcolor='#FFFFFF'><font color='#000000'>"
						+ totalcnt + "</font></td></tr>");
		ts.append(
				"<tr><td width='45%' bgcolor='#FFFFFF'><b><font color='#000000' face='ARIAL' size='2'>Processed Count</font></b></td><td width='55%' bgcolor='#FFFFFF'><font color='#000000'>"
						+ totalProcessed + "</font></td></tr>");
		ts.append(
				"<tr><td width='45%' bgcolor='#FFFFFF'><b><font color='#000000' face='ARIAL' size='2'>Pending Count</font></b></td><td width='55%' bgcolor='#FFFFFF'><font color='#000000'>"
						+ totalPending + "</font></td></tr>");
		ts.append(
				"<tr><td width='45%' bgcolor='#FFFFFF'><b><font color='#000000' face='ARIAL' size='2'>Rejected Count</font></b></td><td width='55%' bgcolor='#FFFFFF'><font color='#000000'>"
						+ totalRejected + "</font></td></tr>");
		ts.append("</table>");

		ts.append("</body>");
		ts.append("</html>");

		writeToFile(htmlFilePath, ts.toString());
		totalcnt=0;
		totalProcessed=0;
		totalPending=0;
		totalRejected=0;
		

	}

	/**
	 * This method is to close the opened section
	 */
	public static void closingsection() {
		ts.append("</div>");
	}

	/**
	 * This method will Create a section.
	 * 
	 * @param strTest
	 */
	public static void AddSection(String strTest) {
		ts.append("<button class='accordion'>" + strTest + "</button>");
		ts.append("<div class='panel'>");
	}

	/**
	 * This method will add a steps
	 * 
	 * @param strdesc
	 *            : Description of your Validation
	 * @param strexp
	 *            : Expected result
	 * @param stract
	 *            : Actual Result
	 * @param strstatus
	 *            : Status
	 */
	public static void AddSteps(String CustomerNum, String MembershipID,String Reason) {

		ts.append("<p><table border='1' width='100%' CELLPADDING=1 CELLSPACING=1 height='47'>");
		ts.append("<tr>");
		ts.append("<td width='20%' bgcolor='#FFFFFF'><b><font color='#000000' face='Arial' size='2'>" + CustomerNum
				+ "</font></b></td>");
		ts.append("<td width='20%' bgcolor='#FFFFFF'><b><font color='#000000' face='Arial' size='2'>" + MembershipID
				+ "</font></b></td>");
		ts.append("<td width='20%' bgcolor='#FFFFFF'><b><font color='#000000' face='Arial' size='2'>" + Reason
				+ "</font></b></td>");
		ts.append("</tr>");
		ts.append("</table></p>");
		intpasscnt = intpasscnt + 1;
		
	}
	
	public static void AddSteps(String FirstCol, String SecondCol, String ThirdCol, String ForthCol) {

		ts.append("<p><table border='1' width='100%' CELLPADDING=1 CELLSPACING=1 height='47'>");
		ts.append("<tr>");
		ts.append("<td width='20%' bgcolor='#FFFFFF'><b><font color='#000000' face='Arial' size='2'>" + FirstCol
				+ "</font></b></td>");
		ts.append("<td width='20%' bgcolor='#FFFFFF'><b><font color='#000000' face='Arial' size='2'>" + SecondCol
				+ "</font></b></td>");
		ts.append("<td width='20%' bgcolor='#FFFFFF'><b><font color='#000000' face='Arial' size='2'>" + ThirdCol
				+ "</font></b></td>");
		ts.append("<td width='20%' bgcolor='#FFFFFF'><b><font color='#000000' face='Arial' size='2'>" + ForthCol
				+ "</font></b></td>");
		ts.append("</tr>");
		ts.append("</table></p>");
		
		intpasscnt = intpasscnt + 1;
		
	}
	
	
	public static void AddCustDetailSteps(String FirstCol, String SecondCol, String ThirdCol, String ForthCol) {

		ts.append("<p><table border='1' width='100%' CELLPADDING=1 CELLSPACING=1 height='47'>");
		ts.append("<tr>");
		ts.append("<td width='20%' bgcolor='#FFFFFF'><b><font color='#000000' face='Arial' size='2'>" + FirstCol
				+ "</font></b></td>");
		ts.append("<td width='20%' bgcolor='#FFFFFF'><b><font color='#000000' face='Arial' size='2'>" + SecondCol
				+ "</font></b></td>");
		ts.append("<td width='20%' bgcolor='#FFFFFF'><b><font color='#000000' face='Arial' size='2'>" + ThirdCol
				+ "</font></b></td>");
		ts.append("<td width='20%' bgcolor='#FFFFFF'><b><font color='#000000' face='Arial' size='2'>" + ForthCol
				+ "</font></b></td>");
		ts.append("</tr>");
		ts.append("</table></p>");
		
		System.out.println(SecondCol);
		System.out.println(FirstCol);
		System.out.println(ThirdCol);
		
		switch (SecondCol.toUpperCase().trim()){
		
		case "PROCESSED":
			totalProcessed=totalProcessed+1;
			break;
			
		case "PENDING":
			totalPending=totalPending+1;
			break;
			
		case "REJECTED":
			totalRejected=totalRejected+1;
			break;
		
		
		}
		
		
	}
	
	
	

	
	

	/**
	 * Method to write to file
	 * 
	 * @param outfilepath:
	 *            File path
	 * @param val
	 *            : data to be written
	 */
	public static void writeToFile(String outfilepath, String val) {
		try {
			FileWriter fl = new FileWriter(new File(outfilepath));
			fl.write(val);
			fl.close();
			Log.info("File created at " + outfilepath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	/**
//	 * This method will report failures under Failure Section
//	 */
//	public static void failures() {
//		HtmlReport.AddSection("FAILURES");
//
//		for (int f = 0; f < HtmlReport.failureList.size(); f++) {
//			String arr[] = HtmlReport.failureList.get(f).split("#");
//
//			HtmlReport.AddSteps(arr[0], arr[1]);
//
//		}
//		HtmlReport.closingsection();
//
//	}
	
	
	@SuppressWarnings("static-access")
	public static void endHtmlReport() {

//	 HtmlReport.failures();		
	 HtmlReport.footer();
	 intpasscnt=0;
		intfailcnt=0;
		totalcnt=0;
		ts = null;

//	 try{
//	 FileUtils.copyFile(new File(HtmlReport.htmlFilePath.toString()), new File(System.getProperty("user.dir").toString()+"\\target\\BRMS_AutomationReport.html"));
//	}
//	 catch (Exception e){
//		 System.out.println("Report FIle Not copied form : " +HtmlReport.htmlFilePath.toString() +" to :" +System.getProperty("user.dir").toString()+"\\target\\BRMS_AutomationReport.html");
//	 }
	 }
	
	
	@SuppressWarnings("static-access")
	public static void endCustDetailHtmlReport() {
	
	 HtmlReport.CustDetailfooter();
	 intpasscnt=0;
		intfailcnt=0;
		totalcnt=0;
		ts = null;

	 }
	

}
