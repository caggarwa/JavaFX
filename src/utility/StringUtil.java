
package utility;

import org.apache.log4j.Logger;
import com.sun.jmx.snmp.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;



/**
 * Handles string manipulation or any other type of return or conversion.
 * 
 * @author Chetanya Aggarwal
 */
public class StringUtil {
    private static final Logger log = Logger.getLogger("com.m1.ems.util.StringUtil");
    
    /**
     * Converts a string comma or semi-colon separated into an array.
     * <p>If string contains both comma or semi-colon, then semi-colon takes precedence.
     * @param s the string which is comma or semi-colon separated
     * @return  an array of the results
     */
    public static String[] stringToArray(String s) {
        String[] result = new String[] {s};
        if (s.indexOf(';') > 0) {
            result = s.split(";");
        } else if (s.indexOf(',') > 0) {
            result = s.split(",");
        }
        for (int i=0; i<result.length; i++) {
            result[i] = result[i].trim();
        }
        return result;
    }
    
    /**
    * This function will escape special characters within a string to ensure that the string will not
    * be parsed as a regular expression. This is helpful with accepting using input that needs to be used
    * in functions that take a regular expression as an argument (such as String.replaceAll(), or String.split()).
    * @see <a href="http://www.dreamincode.net/code/snippet3139.htm">Source</a>
    * @param string - argument which we wish to escape.
    * @return - Resulting string with the following characters escaped: [](){}+*^?$.\
    */
    public static String escapeRegex(String string) {
        final Pattern  GRAB_SP_CHARS = Pattern.compile("([\\\\*+\\[\\](){}\\$.?\\^|])");
        log.debug("Escaping string='"+ string +"'");
        Matcher match = GRAB_SP_CHARS.matcher(string);
        String s = match.replaceAll("\\\\$1");
        log.debug("Escaped string='"+ s +"'");
        return s;
    }
    
    /**
     * Generates a unique ID of lower case alpha and numeric characters.
     * <p>Return string will be any characters in "{@code abcdefghijklmonpqrstuvwxyz0123456789}"
     * <br>Guarentee's to starts with alpha character.
     * @param length    the length of string to return
     * @return          a randomly generated string of alpha and numeric characters 
     */
    public static String generateID(int length) {
        String chars = "abcdefghijklmonpqrstuvwxyz";
        String allChars = chars + "0123456789"; 
        Random ran = new Random();
        char[] buffer = new char[length];
        buffer[0] = chars.charAt(ran.nextInt(chars.length()));
        for (int i=1; i < buffer.length; i++) {
            buffer[i] = allChars.charAt(ran.nextInt(allChars.length()));
        }
        return new String(buffer);
    }
    
    /**
     * Repeats a single character a given number of times.
     * 
     * @param character     a char to repeat
     * @param numberOfTimes the number of times to repeat char
     * @return              a String of the chars repeated
     */
    public static String repeat(char character, int numberOfTimes) {
        String r = "";
        for (int j=0; j<numberOfTimes; j++) {
            r += character;
        }
        return r;
    }
    
    /**
     * Determine if a string or expression was found in a string.
     * 
     * @param expression    pattern what to look for (can be a regular expression)
     * @param string        the string to look in
     * @return              true if expression was matched, otherwise false
     */
    public static boolean match(String expression, String string) {
        //log.debug("Match expression '"+ expression +"' in '"+ string +"'");
        if (expression.length() == 0)
            return false;
        try {
            Pattern p = Pattern.compile(expression);
            if ( p.matcher(string).find() ) {
                return true;
            } else {
                return false;
            }
        } catch (PatternSyntaxException ex) {
            log.fatal("String match syntax error in string='"+ expression +"'", ex);
        }
        return false;
    }
    
    
    /**
     * Determine if a string matches another string
     * 
     * @param string1    First string to compare
     * @param string2    Second string to compare
     * @return           true if both strings are exact
     */
    public static boolean matchExact(String string1, String string2) {
        //log.debug("Match exact "+ string1 +"="+ string2);
        Pattern p = Pattern.compile("^"+ string1 +"$");
        if ( p.matcher(string2).find() ) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Converts a string to a boolean.
     * <p>Accepted string to set as ({@code true})
     * <pre>
     * true
     * 1
     * enable
     * enabled
     * positive
     * </pre>
     * @param string    the string to convert
     * @return          the string as a boolean
     */
    public static boolean toBoolean(String string) {
        boolean b = false;
        if (string.length() > 0) {
            if (inArray(string, new String[] {"true", "1", "enable", "enabled", "positive"}))
                b = true;
        }
        return b;
    }
    
    /**
     * Converts a string to a Long.
     * 
     * @param string    the string to convert
     * @return          the string as a long, {@code 0} on error
     */
    public static long toLong(String string) {
        long l = 0;
        if (string.length() > 0) {
            try {
                l = Long.parseLong(string.trim());
             } catch (NumberFormatException nfe) {
                log.error("Failed to convert string to long :: "+ nfe.getMessage());
             }
        }
        return l;
    }
    
    /**
     * Converts a string to an Integer.
     * 
     * @param string    the string to convert
     * @return          the string as a integer, {@code 0} on error
     */
    public static int toInt(String string) {
        int i = 0;
        if (string.length() > 0) {
            try {
                i = Integer.parseInt(string.trim());
             } catch (NumberFormatException nfe) {
                log.error("Failed to convert string to integer :: "+ nfe.getMessage());
             }
        }
        return i;
    }
    
    /**
     * Convert a Object array into a String array.
     * @param obj   the array of Objects
     * @return      the array as a String
     */
    public static String[] toArray(Object[] obj) {
        String[] resultsReturn = new String[obj.length];
        for (int i=0; i<obj.length; i++) {
            resultsReturn[i] = (String) obj[i];
        }
        return resultsReturn;
    }

    /**
     * Converts a string to a Date.
     * 
     * <p>For date format: <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html">
     * http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html</a>
     * <p>Prints an error message to logs if string could not be parsed.
     * 
     * @param dateString    the string to convert to a date
     * @param dateFormat    the format of the string (e.g: {@code EEE, d MMM yyyy HH:mm:ss Z})
     * @return              the string as a Date object, {@code null} on error
     */
    public static Date toDate(String dateString, String dateFormat) {
    	
    	DateFormat inputFormat = new SimpleDateFormat(dateFormat); //yyyy-MM-dd HH:mm:ss.S
//		Date date = inputFormat.parse(dateString);
       
        Date date = null;
        try {
            date = inputFormat.parse(dateString);
        } catch (Exception ex) {
            log.error("Invalid date given :: "+ dateString +" :: "+ dateFormat);
        }
        return date;
    }
    
    /**
     * Converts a date to a string format.
     * <p>For date format: <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html">
     * http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html</a>
     * @param date          the date to convert to string
     * @param dateFormat    the format to return
     * @return              the date formated as a string
     */
    public static String dateToString(Date date, String dateFormat) {
        SimpleDateFormat simpleDate = new SimpleDateFormat(dateFormat);
        return simpleDate.format(date);
    }
    
    
	
    /**
     * Fills a string to a given size with a specific character.
     * 
     * <p>If the string passed is larger then the size specified, 
     * then just the complete string is returned.
     * 
     * @param string    the string to fill
     * @param size      the size string should be filled to
     * @param character the character to fill string with
     * @return          the original string, plus character filled to the size given 
     */
    public static String fill(String string, int size, char character) {
        String filler = repeat(character, size);
        return string + filler.substring(Math.min(string.length(), filler.length()));
    }
    
    /**
     * Determine if a string or pattern exists in an array.
     * 
     * @param query         the string to query for
     * @param stringArray   the array to search in
     * @return              true if string was found in array, otherwise false
     */
    public static boolean inArray(String query, String[] stringArray) {
        for (String s : stringArray) {
            if (match(query, s)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Converts a string to HTML save characters (escaping HTML code).
     * Except for '&' which allows for special character injection.
     * @param string    the string to convert into HTML save code
     * @return          the string with HTML elements escaped
     */
    public static String escapeHTML(String string) {
        StringBuffer sb = new StringBuffer(string.length());
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;
    
        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            if (c == ' ') {
                // only replace every other space to preserve word breaking
                if (lastWasBlankChar) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                } else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                }
            } else {
                lastWasBlankChar = false;
                // HTML Special Chars
                switch (c) {
                    case '<'  : sb.append("&lt;"); break;
                    case '>'  : sb.append("&gt;"); break;
                    //case '&'  : sb.append("&amp;"); break;
                    case '"'  : sb.append("&quot;"); break;
                    case '\n' : sb.append("&lt;br/&gt;"); break;
                    default   : sb.append(c); break;
                }
            }
        }
        return sb.toString();
    }
    
    
    /**
     * Generates a unique string of alphabets, numerics and special characters
     * @param lowerBound 	the Decimal ASCII code.
     * @param upperBound	the Decimal ASCII code
     * @param length		the length of the string to return
     * @return				a randomly generated string
     * <p> lowerBound should always be lesser than the upperBound
     * <p> For ASCII values refer to the link <a href="http://www.asciitable.com">http://www.asciitable.com</a>
     */
    public static String generateRandomString(int lowerBound,int upperBound,int length) {    	
    	int randomNumber;
		char[] buffer = null;
		try {
			buffer = new char[length];
			for (int i = 0; i < length; i++) {
				randomNumber = (int) (Math.random() * (upperBound - lowerBound))
						+ lowerBound;
				buffer[i] = (char) randomNumber;
			}
		} catch (Exception e) {
			log.error("Failed to generate a random string " + e.getMessage() );
		}
		return new String(buffer);    	
    }
    
    public static String generateRandomString(int length) {    	
    	int randomNumber;
		char[] buffer = null;
		try {
			buffer = new char[length];
			for (int i = 0; i < length; i++) {
				randomNumber = (int) (Math.random() * (90 - 65))
						+ 65;
				buffer[i] = (char) randomNumber;
			}
		} catch (Exception e) {
			log.error("Failed to generate a random string " + e.getMessage() );
		}
		return new String(buffer);    	
    }
    
    
	
	/**
     * Generates a Random number on the basis of timestamp
     * @param Length 	Length of characters 
     * @return a randomly generated number of string type
    **/
   
	public static String randomNumber(int Length){
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		String TimeStamp=String.valueOf(timestamp.getDateTime());
		return TimeStamp.substring(TimeStamp.length()-Length);		
    }
	
	public static final  Pattern VALID_PATTERN = Pattern.compile("[0-9]+|[A-Z]+");

	 public static List<String> parse(String toParse) {
	    List<String> chunks = new ArrayList<String>();
	    Matcher matcher = VALID_PATTERN.matcher(toParse);
	    while (matcher.find()) {
	        chunks.add( matcher.group() );
	    }
	    return chunks;
	}
	 
	 public static String getTimeStamp(){
//		 new java.util.Date();
		 String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		 timeStamp=timeStamp.toString().replaceAll("[-+.^:,]","");
		 return timeStamp;

	 }
	 
//	public static String intToString(int iNumber){
//		return String.valueOf(iNumber);
//		
//	}
	
	 public static String StringtoMultiLineHtmlTable(String sText, int charLimit ){
			String newString="";
			String sString=sText;
			char[] chars = sString.toCharArray();
			boolean endOfString = false;
			int start = 0;
			int end = start;
			while(start < chars.length-1) {
				int charCount = 0;
				int lastSpace = 0;
				while(charCount < charLimit) {
					if(chars[charCount+start] == ' ') {
						lastSpace = charCount;
					}
					charCount++;
					if(charCount+start == sString.length()) {
						endOfString = true;
						break;
					}
				} 
				end = endOfString ? sString.length(): (lastSpace > 0) ? lastSpace+start : charCount+start;
					String Stemp=sString.substring(start, end);
					newString=newString + Stemp + "<br>" ;
//					System.out.println(sString.substring(start, end));
					start = end+1;
			}
			String multiLineString=newString.substring(0, newString.length()-4);
			return multiLineString;
			
		}
	 
	 
	 public static String extractNumberFromString(String sText){
		 String sNumber="";
		 try{
		 StringBuilder myNumbers = new StringBuilder();
		    for (int i = 0; i < sText.length(); i++) {
		        if (Character.isDigit(sText.charAt(i))) 
		            myNumbers.append(sText.charAt(i));
		            
		    }
		    System.out.println("Your numbers: " + myNumbers.toString());
		    sNumber=myNumbers.toString();
		 }
		 catch(Exception ex){
			 Log.error("Error in fetching numberd from a text");
		 }
		 return sNumber;
	 }
	 
	 public static double RoundOffBillAmountInBRMS(double dvalue){
			double value=0.0;
			//Convert Double into String type
			String number = String.valueOf(dvalue);
			
			//Get the decimal places in a number
			String decimalnumber = number.substring(number.indexOf(".")).substring(1);
			
			//Round Off The Value 
			String[] chars=null;
			
			chars=decimalnumber.split("(?!^)");
			int CharsLen=0;
			if (chars.length>4)
				CharsLen=4;
			else
				CharsLen=chars.length;
			
			int a[] = new int[CharsLen];
			if (chars.length>2){
				
				for (int ipos=1; ipos<CharsLen ;ipos++ ){
					a[ipos-1] =Integer.parseInt(chars[ipos]);
				}
				int max = a[0];
				for(int i = 0; i < a.length; i++)
				{
					if(max < a[i])
						max = a[i];
				}
		
				String newnumber = number.substring(0,number.indexOf(".")+2) + Integer.toString(max);
				value = Double.parseDouble(newnumber);
			}
			else
				value=dvalue;
			
			return value;
		}
}
	

