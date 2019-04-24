package utility;
import org.apache.log4j.Logger;




 
public class Log {
// Initialize Log4j logs
     private static Logger Log = Logger.getLogger(Log.class.getName()); 
//Initialize logger for Extent Report
    
     
// This is to print log for the beginning of the test case, as we usually run so many test cases as a test suite
public static void startTestCase(String sTestCaseName){
    Log.info("****************************************************************************************");
    Log.info("****************************************************************************************");
    Log.info("$$$$$$$$$$$$$$$$$$$$$                 "+sTestCaseName+ "       $$$$$$$$$$$$$$$$$$$$$$$$$");
    Log.info("****************************************************************************************");
    Log.info("****************************************************************************************");
    }
 
    //This is to print log for the ending of the test case
public static void endTestCase(String sTestCaseName){
    Log.info("XXXXXXXXXXXXXXXXXXXXXXX             "+"-E---N---D-"+"             XXXXXXXXXXXXXXXXXXXXXX");
    Log.info("X");
    Log.info("X");
    Log.info("X");
    Log.info("X");
    }
 
    // Need to create these methods, so that they can be called  
public static void info(String message) {
        Log.info(message);
//        AdvanceReporting2X.getTest().log(LogStatus.INFO, message);
//        AdvanceReporting.getTest().log(Status.INFO, message);
        }
public static void warn(String message) {
    Log.warn(message);
//    AdvanceReporting2X.getTest().log(LogStatus.WARNING, message);
//    AdvanceReporting.getTest().log(Status.WARNING, message);
  
    }
public static void error(String message) {
    Log.error(message);
//    AdvanceReporting.getTest().log(Status.INFO, message);
//    AdvanceReporting2X.getTest().log(LogStatus.INFO, message);
    }
public static void fatal(String message) {
    Log.fatal(message);
//    AdvanceReporting.getTest().log(Status.FATAL, message);
//    AdvanceReporting2X.getTest().log(LogStatus.FATAL, message);
    }
public static void debug(String message) {
    Log.debug(message);
//    AdvanceReporting.getTest().log(Status.UNKNOWN, message);
//    AdvanceReporting2X.getTest().log(LogStatus.SKIP, message);
    }
}
