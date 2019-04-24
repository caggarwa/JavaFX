
package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelUtils 
{
	
	  public static Map<String, String> ReadExcelRowByIndexValue(String FileName, String SheetName,String IndexValue) {
		    Log.info("Reading Excel Data from file:" + FileName + " Sheet:" + SheetName + " With Index value:" + IndexValue);
		    Map<Integer, List<String>> data = new HashMap<Integer, List<String>>();
	        Map<String, String> dictionary = new HashMap<String, String>();
		    
	        
	        try{
	        //Path of excel file: FileName
	        	
	        FileInputStream inputStream = new FileInputStream(new File(FileName));
	        Workbook workbook = new XSSFWorkbook(inputStream);
	        workbook.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);
	        Sheet firstSheet = workbook.getSheet(SheetName);
	        Iterator<Row> iterator = firstSheet.iterator();
	        int rowCnt = 0;
	        while (iterator.hasNext()) { 
	            Row nextRow = iterator.next();
	            Iterator<Cell> cellIterator = nextRow.cellIterator();
	            List<String> obj = new ArrayList<String>();
	            String cellobj="";
	            for (int cn=0; cn<nextRow.getLastCellNum(); cn++) {
	                Cell cell = nextRow.getCell(cn);
	                try{
	                cellobj = cell.toString();
	                }
	                catch(Exception ex){
	                	cellobj=null;	
	                }
	                if (cellobj==null) {  //
	                    obj.add(" ");
	                } else if (cellobj.equals(" ")) {
	                    obj.add(" ");
	                }else if (cellobj.isEmpty()) {
	                    obj.add(" ");
	                }else if (cellobj.equalsIgnoreCase("")) {
	                    obj.add(" ");
	                } else {
	                    obj.add(cell.toString());
	                    
	                }
	             }
	            
	            
	            
//	            while (cellIterator.hasNext()) {
//	                Cell cell = cellIterator.next();
//	                String cellobj = cell.toString();
//	                if (cellobj.equals(" ")) {
//	                    obj.add("NULL");
//	                } else if (cellobj.equals(null)) {
//	                    obj.add("NULL");
//	                }else if (cellobj.isEmpty()) {
//	                    obj.add("NULL");
//	                }else if (cellobj.equalsIgnoreCase("")) {
//	                    obj.add("NULL");
//	                } else {
//	                    obj.add(cell.toString());
//	                    
//	                }
//	            }

	            data.put(rowCnt, obj);
	            rowCnt++;

	        }
	        workbook.close();
	        String RowData = null;
	    	
	    	String Columns = data.get(0).toString();
	    	for (int i=1; i<data.size(); i++){
				if (data.get(i).toString().contains(IndexValue))
						{
					RowData = data.get(i).toString();
					break;
						}
		    }
	    	
	    	String [] ColumnsNames = Columns.replace("[", "").replace("]", "").split(",");
	    	String [] ColumnsValues = RowData.replace("[", "").replace("]", "").split(",");
	    	
	    	for (int j=0; j<ColumnsNames.length; j++){
	    		dictionary.put(ColumnsNames[j].trim(), ColumnsValues[j].trim());
	    	}
	    	
		  }
	  catch(Exception e){
		  
		  Log.error("Exception occured while reading Excel Data. Exception:" + e.getMessage());
		  
	  }
		    return dictionary;
}
	  
	  
	  
	  
	  
	  
	  @SuppressWarnings("deprecation")
	public static ArrayList<String> extractExcelContentByColumnIndex(String FileName,String SheetName,int columnIndex ){
	        ArrayList<String> columndata = null;
	        try {
	            File sfile = new File(FileName);
	            FileInputStream inputStream = new FileInputStream(sfile);
	            Workbook workbook = new XSSFWorkbook(inputStream);
		        Sheet sheet = workbook.getSheet(SheetName);
	           
	            Iterator<Row> rowIterator = sheet.iterator(); // Traversing over each row of XLSX file
	            columndata = new ArrayList<>();

	            while (rowIterator.hasNext()) {
	                Row row = rowIterator.next();  // For each row, iterate through each columns
	                Iterator<Cell> cellIterator = row.cellIterator();
	                while (cellIterator.hasNext()) {
	                    Cell cell = cellIterator.next(); 

	                    if(row.getRowNum() > 0){ //To filter column headings
	                        if(cell.getColumnIndex() == columnIndex){// To match column index
	                            switch (cell.getCellType()) {
	                            case Cell.CELL_TYPE_NUMERIC:
	                                columndata.add(cell.getNumericCellValue()+"");
	                                break;
	                            case Cell.CELL_TYPE_STRING:
	                                columndata.add(cell.getStringCellValue());
	                                break;
	                            }
	                        }
	                    }
	                }
	            }
	            inputStream.close();
//	            System.out.println(columndata);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return columndata;
	    }
	  
	  
	  public static ArrayList<String> extractExcelContentByColumnName(String FileName,String SheetName, String ColumnName){
	        ArrayList<String> columndata = null;
	        String cellValueMaybeNull,cellValue = null;
	        int cellIndex = 0;
	        int RowIndex=0;
	        boolean flag=false;
	        boolean blnIndex=true;
	        try {
	            File sfile = new File(FileName);
	            FileInputStream inputStream = new FileInputStream(sfile);
	            Workbook workbook = new XSSFWorkbook(inputStream);
		        Sheet sheet = workbook.getSheet(SheetName);
	           
	            Iterator<Row> rowIterator = sheet.iterator();
	            columndata = new ArrayList<>();
	            
	            //================================================================
	            // Loop To get the Column and Row number of the required CoumnName
	            //================================================================
	            outerloop:
	            while (rowIterator.hasNext() ) {
	            	Row row = rowIterator.next();
	            	Iterator<Cell> cellIterator = row.cellIterator();
	            	
	            	while (cellIterator.hasNext() ) {
	            		Cell cell = cellIterator.next();
	            		if (cell != null)  {
	            			
	            			switch (cell.getCellType()) {
	                        	
	                        	case Cell.CELL_TYPE_STRING:
	                        		cellValue=cell.getStringCellValue();
	                            	break;
	            			}
	            			
	            			 if (cellValue.equalsIgnoreCase(ColumnName)){
	            				 cellIndex=cell.getColumnIndex();
	            				 RowIndex=cell.getRowIndex();
	            				 
	            				 break outerloop;
	            			 }
	            		}
	            	}	
	            }
	            
	           
	            //================================================================
	            // Loop To get all Column values  of the required CoumnName
	            //================================================================
	            rowIterator = sheet.iterator();
	            while (rowIterator.hasNext()) {		
	            	Row row = rowIterator.next();
	            	Iterator<Cell> cellIterator = row.cellIterator();	
	            	while (cellIterator.hasNext()) {

			            for (int rowIndex = RowIndex+1; rowIndex <=sheet.getLastRowNum() ; rowIndex++) {
			            	row = sheet.getRow(rowIndex);
			            	  if (row != null) {
			            		 Cell cell=row.getCell(cellIndex);
			            	    if (cell != null) {
			            	      // Found column and there is value in the cell.
			            	      cellValueMaybeNull = cell.getStringCellValue().toString().trim();
			            	      // Do something with the cellValueMaybeNull here ..
//			            	      if (cellValueMaybeNull.equalsIgnoreCase(ColumnName)){
			            	    	 flag=true;
//			            	      }
			            	     if(flag){
//			            	    	 System.out.println(cellValueMaybeNull);
			            	    	 columndata.add(cellValueMaybeNull);
			            	     }
			            	    }
			            	  }
			            
			            }
			            break;
	            
	            	} 
	            	break;
	            }
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return columndata;
	    }
	  
	  
	  
	  public static Map<Integer, List<String>> ReadExcelData(String FileName, String SheetName) {
		    Log.info("Reading Excel Data from file:" + FileName + " Sheet:" + SheetName );
		    Map<Integer, List<String>> data = new HashMap<Integer, List<String>>();
//	        Map<String, String> dictionary = new HashMap<String, String>();
		    
	        
	        try{
	        //Path of excel file: FileName
	        	
	        FileInputStream inputStream = new FileInputStream(new File(FileName));
	        Workbook workbook = new XSSFWorkbook(inputStream);
	        Sheet firstSheet = workbook.getSheet(SheetName);
	        Iterator<Row> iterator = firstSheet.iterator();
	        int rowCnt = 0;
	        while (iterator.hasNext()) { 
	            Row nextRow = iterator.next();
	            Iterator<Cell> cellIterator = nextRow.cellIterator();
	            List<String> obj = new ArrayList<String>();
	            while (cellIterator.hasNext()) {
	                Cell cell = cellIterator.next();
	                String cellobj = cell.toString();
	                if (cellobj.equals(" ")) {
	                    obj.add(" ");
	                } else if (cellobj.equals(null)) {
	                    obj.add(" ");
	                } else {
	                    obj.add(cell.toString());
	                }

	            }

	            data.put(rowCnt, obj);
	            rowCnt++;

	        }
	        workbook.close();     
	        
		  }
	      catch(Exception e){
		  
	    	  Log.error("Exception occured while reading Excel Data. Exception:" + e.getMessage());
		  
	      }
		    return data;
	  }
	  
	  
	  public static Map<String, String> ExcelDataRowByIndexValue(Map<Integer, List<String>> ExcelData,int HeaderRowNumber,String IndexValue){
		  Map<String, String> dictionary = new HashMap<String, String>();
		  try{
			  String RowData = null;
			  String Columns = ExcelData.get(HeaderRowNumber-1).toString();
		      	for (int i=1; i<ExcelData.size(); i++){
		      		
		      		
		    			if (ExcelData.get(i).toString().substring(0, ExcelData.get(i).toString().indexOf(",")).contains(IndexValue))//ExcelData.get(i).toString().contains(IndexValue)
		    				{
		    				RowData = ExcelData.get(i).toString();
		    				break;
		    				}
		    	    }
		      	
		      	String [] ColumnsNames = Columns.replace("[", "").replace("]", "").split(",");
		      	String [] ColumnsValues = RowData.replace("[", "").replace("]", "").split(",");
		      	
		      	for (int j=0; j<ColumnsNames.length; j++){
		      		dictionary.put(ColumnsNames[j].trim(), ColumnsValues[j].trim());
		      	}
		  }
		  catch(Exception e){
			  
	    	  Log.error("Exception occured while reading Excel Data. Exception:" + e.getMessage());
		  
	      }
		    ExcelData=null;
		    return dictionary;
	  }

	  
	  public static boolean updateExcelCell(String FileName,String SheetName,String rowIdentifier,String columnIdentifier,String Value)
	  {
				boolean excelFlag=true;
		boolean chkflag=false;
		try
		{
			Workbook wb =null;
			int rw=0;
			int cl=0;
					
					File fl = new File(FileName);
					FileInputStream fi = new FileInputStream(fl);
					String filenameextension = FileName.substring(FileName.lastIndexOf("."));
					if(filenameextension.equals(".xlsx"))
					{
					wb= new XSSFWorkbook(fi);
					}
					else if(filenameextension.equals(".xls"))
					{
					wb= new HSSFWorkbook(fi);
					}

					Sheet sh = wb.getSheet(SheetName);
					int rowcnt = sh.getPhysicalNumberOfRows();
					int colcnt = sh.getRow(0).getLastCellNum();

					for(int i=0;i<rowcnt;i++)
					{
						for(int j=0;j<colcnt;j++)
						{
							Row row = sh.getRow(i);
						if(row.getCell(j).getStringCellValue().equals(rowIdentifier))
						{
							rw=i;
							chkflag=true;
							break;
						}
						}
						if(chkflag==true)
						{
							break;
						}
						
					}

					for(int j=0;j<colcnt;j++)
					{
						Row row = sh.getRow(0);
						if(row.getCell(j).getStringCellValue().equals(columnIdentifier))
						{
							cl=j;
						}
						
					}
					
					Cell cell2Update = sh.getRow(rw).getCell(cl);
					cell2Update.setCellValue(Value);
					FileOutputStream outputStream = new FileOutputStream(fl);
			        wb.write(outputStream);
			        wb.close();

		}
		catch (Exception e) {
			excelFlag=false;
		}
		return excelFlag;
		  
	  }
			
}
