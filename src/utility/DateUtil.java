package utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;


public class DateUtil {

	/**
     * Get the current date given the format.
     * <p>Format examples see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html">
     * http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html</a>
     * @param format    the format of the current date
     * @return          the current date formatted
     */
    public static String getDate(String format) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }
    
    public static String getDate(String format,Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }
    
    /**
	 * add days to date in java
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
				
		return cal.getTime();
	}
	
	public static Date addMonths(Date date, int months) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
				
		return cal.getTime();
	}
	
	public static Date addYears(Date date, int years) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.YEAR, years);
				
		return cal.getTime();
	}
	
	public static Date customDate(Date date, String Component, int Count)
	{
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    if (Component.equalsIgnoreCase("day"))
	    	cal.add(Calendar.DATE, Count); //minus number would decrement the days
	    
	    else if (Component.equalsIgnoreCase("month"))
	    	cal.add(Calendar.MONTH, Count); //minus number would decrement the Months
	    
	    else
	    	cal.add(Calendar.YEAR, Count); //minus number would decrement the Year
	    return cal.getTime();
	}
	
	/**
     * Generates a Random date on the basis of age
     * @param age 	
     * @return a randomly generated date of string type
    **/
   
	public static String randomDate(int age){

		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());	// Gets the current date and time
		
		int year =localCalendar.get(Calendar.YEAR)- age;
		String DateOfBirth = ("" + (1 + (int) (Math.random() * 12) + "/" + (1 + (int) (Math.random() * 31) + "/" + Integer.toString(year)))).toString();
		return DateOfBirth;
		
    }
	
	public static int getCurrentYear(){
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());	// Gets the current date and time
		
		int year =localCalendar.get(Calendar.YEAR);
		return year;
	}
	
	public static int getCurrentMonth(){
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());	// Gets the current date and time
		
		int month =localCalendar.get(Calendar.MONTH);
		return month +1;
	}
	
	public static int getCurrentDate(){
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());	// Gets the current date and time
		
		int date =localCalendar.get(Calendar.DATE);
		return date;
	}
	
	public static int getYear(Date dDate ){
		Date date; // your date
		int iYear=0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dDate);
		iYear = cal.get(Calendar.YEAR);
//		int month = cal.get(Calendar.MONTH);
//		int day = cal.get(Calendar.DAY_OF_MONTH);
		return iYear;
	}
	
	public static int getMonth(Date dDate ){
		Date date; // your date
		int iMonth=0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dDate);
		iMonth = cal.get(Calendar.MONTH);
//		int month = cal.get(Calendar.MONTH);
//		int day = cal.get(Calendar.DAY_OF_MONTH);
		return iMonth;
	}
	
	public static String getDayOfWeek(Date dDate){
		String Day="";
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
		Day=simpleDateformat.format(dDate);
		return Day;
	}

	
	public static String myCustomDate (String dateOfBirth,String Criteia)
	{
		String DateOfBirth;
		if (dateOfBirth.equals("Dynamic")) {
			
			Date date = new Date();
	
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			
			List <String> criteriaList= StringUtil.parse(Criteia);
			String CriteiaCase=criteriaList.get(1).toString().toUpperCase().trim();
			String Amount=criteriaList.get(0).toString().trim();
			int Count=StringUtil.toInt (Amount);
			
			if (CriteiaCase.contains("P"))
			{
				Count *= -1;
				CriteiaCase=CriteiaCase.replace("P", "");
			}
			
			else{
				Count *= 1;
				CriteiaCase=CriteiaCase.replace("F", "");
			}	
			
			switch (CriteiaCase) {
				case "D":
					cal.add(Calendar.DATE, Count);
					break;
					
				case "M":
					cal.add(Calendar.MONTH, Count);
					break;
					
				case "Y":
					cal.add(Calendar.YEAR, Count);
					break;
				default :
					break;
			}
			Date MyDate=cal.getTime();
			
			DateOfBirth=StringUtil.dateToString(MyDate, "MM/dd/YYYY");
						
		}	
		else
			DateOfBirth=dateOfBirth;
		
		return DateOfBirth;
	}
	
	
}
