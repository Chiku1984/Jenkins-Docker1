package Hackathon_Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dataUtil.RandomDataGen;
import dataUtil.dataRead;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import io.restassured.RestAssured;
import io.restassured.response.Response;

public class convertJson2 {
	dataRead dataRepo = new dataRead();
	private static XSSFSheet ExcelWSheet;
	 LinkedList<String> Sheet3ListCombo1 = null, Sheet3ListCombo2 = null;
	 private static XSSFWorkbook ExcelWBook;
	 
	 private static XSSFCell Cell;
	 
	 private static XSSFRow Row;
	 static int Length = -1;
	static boolean Combo = false, Defined = true;
	static int ComboCount = 3;
	static String ComboType = null;
	static String excelFilePath = "./API temp.xlsx";
	static String jsonBodyString = null;
	static String RequestURL = null;
	static String BaseURL = null;
	static String ExpectedStatus = null;
	static String ErrorStatus = null;
	static String method = null;
	static String ComboLengthKey = null;
	static String ComboKey = null;
	
	@Test
	public void testR() throws IOException, InvalidFormatException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		System.out.println(dataRepo.getCellDropdown("./APITemplate.xlsx", "Sheet1").toString());
		System.out.println(dataRepo.getRequest("./APITemplate.xlsx", "Sheet1"));
		System.out.println(dataRepo.getRequestName("./APITemplate.xlsx", "Sheet1", "Path"));
		System.out.println(dataRepo.getRequestValue("./APITemplate.xlsx", "Sheet1", "OK"));
	}
	
	@SuppressWarnings("unchecked")
	//@Test (dataProvider = "getExcelData", description = "Get Data From API Template")
	public void testRunner(String p1, String p2, String p3, String p4, String p5) throws IOException {
		ArrayList<String> Sheet1List = new ArrayList<String>();
		System.out.println();
		HashMap<String, String> map1 = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();
		System.out.println("pointer 1");
		FileInputStream fis = new FileInputStream(new File(excelFilePath));
		FileOutputStream fos = new FileOutputStream(new File(excelFilePath), true);
		 Workbook workbook = new XSSFWorkbook(fis);
        Row nextRow = null;
         Iterator<Cell> cellIterator = null;
	        Sheet Sheet1 = workbook.getSheet("Sheet1");
	        int rowCount = Sheet1.getLastRowNum();
         Iterator<String> iterator1 = Sheet1List.iterator();
         
         RequestURL = p1;
         BaseURL = p2;
         ExpectedStatus = p3;
         ErrorStatus = p4;
         method = p5;
         System.out.println("pointer 2");
         map1 = new HashMap<String,String>();
	        List<String> Sheet2List = new LinkedList<String>();
	        Sheet Sheet2 = workbook.getSheet("Sheet2");
	        Iterator<Row> iterator2 = Sheet2.iterator();
	         iterator2.next();
	         System.out.println("Sheet 2 Loop");
	         
	         //Sheet 2 Loop
	        while (iterator2.hasNext()) {
	            nextRow = iterator2.next();
	            cellIterator = nextRow.cellIterator();
	             
	            for(int i = 0; cellIterator.hasNext(); i++) {
	                Cell cell = cellIterator.next();
	                switch (cell.getCellType()) {
	                case STRING:
	                    System.out.println(cell.getStringCellValue());
	                    break;
	                case NUMERIC:
	                	cell.setCellType(CellType.STRING);
	                    System.out.println(cell.getStringCellValue());
	                    break;
	                }
	                if(i == 0 || i == 1) {
		                if(cell.getStringCellValue().equals(RequestURL) || cell.getStringCellValue().equals(BaseURL))
		                	continue;
		                else
		                	break;
	                }
	                Sheet2List.add(cell.getStringCellValue());
	            }
	                Iterator<String> lstIterator = Sheet2List.iterator();
	                System.out.println("Putting in map");
	                while(lstIterator.hasNext()) {
	                	String key = lstIterator.next();
	                	String value = lstIterator.next();
	                	map1.put(key,value);                	
	                }
	                
	                
	                
	            }
	        System.out.println(map1.toString());
	        
	        map2 = new HashMap<String,String>();
	        LinkedList<String> Sheet3List = new LinkedList<String>();
	        
	        while(ComboCount != 0) {
	        Sheet Sheet3 = workbook.getSheet("Sheet3");
	        Iterator<Row> iterator3 = Sheet3.iterator();
	        iterator3.next();
	        
	        //Sheet 3 Loop
	        while (iterator3.hasNext()) {
	            nextRow = iterator3.next();
	            cellIterator = nextRow.cellIterator();
	             
	            for(int i = 0; cellIterator.hasNext(); i++) {
	                Cell cell = cellIterator.next();
	                
	                switch (cell.getCellType()) {
	                case STRING:
	                    System.out.println(cell.getStringCellValue());
	                    
	                    break;
	                case NUMERIC:
	                	cell.setCellType(CellType.STRING);
	                    System.out.println(cell.getStringCellValue());
	                    break;
	                }
	                if(i == 4 && cell.getStringCellValue().equals("JSON")) {
	                	cell = cellIterator.next();
	                	if(cell.getStringCellValue().contains(".json")) {
	                		JSONParser parser = new JSONParser();
	                		Object obj = null;
	                		try {
	                			obj = parser.parse(new FileReader(cell.getStringCellValue()));
	                		} catch (Exception e1) {
	                			e1.printStackTrace();
	                		} 
	                		JSONObject jsonObject = (JSONObject) obj;
	                		
	                		jsonBodyString = jsonObject.toJSONString();
	                	}
	                	else {
	                		jsonBodyString = cell.getStringCellValue();
	                		
	                	}
	                	continue;
	                }
	                if(i == 0 || i == 1) {
		                if(cell.getStringCellValue().equals(RequestURL) || cell.getStringCellValue().equals(BaseURL))
		                	continue;
		                else
		                	break;
	                }
	                
	                if(i == 2) {
	                	if(!cell.getStringCellValue().equalsIgnoreCase("defined")) {
	                		Combo = true;
	                		Defined = false;
	                		if(cell.getStringCellValue().contains("String")) {
	                			ComboType = "String";
	                		}
	                		if(cell.getStringCellValue().contains("Number")) {
	                			ComboType = "Number";
	                		}
	                		if(cell.getStringCellValue().contains("AlphaNum")) {
	                			ComboType = "AlphaNum";
	                		}
	                	}
	                }
	                
	                if(i == 3) {
	                	if(!cell.getStringCellValue().equalsIgnoreCase("")) {
	                		Length = Integer.parseInt(cell.getStringCellValue()) - 1;
	                		
	                	}
	                }
	                
	                if(i == 4)
	                	Sheet3List.add(cell.getStringCellValue());
	                
	                if(i == 5) {
	                	if(Combo) {
	                		
	                		RandomDataGen randomGen = new RandomDataGen();
	                		if(ComboType.equals("String")) 
	                			Sheet3List.add(RandomDataGen.String_Sequence(Length++));
	                		
	                		
	                		if(ComboType.equals("Number"))
	                			Sheet3List.add(RandomDataGen.NumberSequence(Length++));
	                		
	                		if(ComboType.equals("AlphaNum"))
	                			Sheet3List.add(RandomDataGen.AlphaNumericString_Sequence(Length++));
	                			                		
	                	}
	                	
	                	if(Defined) {
	                		Sheet3List.add(cell.getStringCellValue());
	                	}
	                
	                
	                }
	            }
	                Iterator<String> lstIterator = Sheet3List.iterator();
	                System.out.println("Putting in map");
	                while(lstIterator.hasNext()) {
	                	String key = lstIterator.next();
	                	String value = lstIterator.next();
	                	map2.put(key,value);                	
	                }
	        }
	                System.out.println("SHeet3::::"+Sheet3List.toString());
	                
	             
	            
	        
	        
	       
        System.out.println("*******************"+map2.toString());
        
        RestAssured.baseURI = RequestURL;
		Response res = null;
		if(method.equalsIgnoreCase("post")) {
			if(jsonBodyString == null)
	        {
				
				try
				{
				  res = RestAssured.given().log().all()
			            .queryParams(map1)
						.header("Content-Type","application/json")
			            .when().relaxedHTTPSValidation()
			            .body(map2)
			               
			               
			            .post(BaseURL);
		
						
						
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
	        }
			else {
				try
				{
				  res = RestAssured.given().log().all()
			            .queryParams(map1)
						.header("Content-Type","application/json")
			            .when().relaxedHTTPSValidation()
			            .body(jsonBodyString)
			               
			               
			            .post(BaseURL);
		
						
						
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
	
			}
		}
		if(method.equalsIgnoreCase("get")) {
			if(jsonBodyString == null)
	        {
				
				try
				{
				  res = RestAssured.given().log().all()
			            .queryParams(map1)
						.header("Content-Type","application/json")
			            .when().relaxedHTTPSValidation()
			            .body(map2)
			               
			               
			            .get(BaseURL);
				  		
						
						
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
	        }
			else {
				try
				{
				  res = RestAssured.given().log().all()
			            .queryParams(map1)
						.header("Content-Type","application/json")
			            .when().relaxedHTTPSValidation()
			            .body(jsonBodyString)
			               
			               
			            .get(BaseURL);
		
						
						
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
	
			}
		}
	     

				System.out.println(res.getBody().asString());
				System.out.println(res.getStatusCode());
				Assert.assertTrue(res.getStatusCode()==Integer.parseInt(ExpectedStatus), "API Status not matching as expected");
				System.out.println(res.getStatusLine());
	       
				
				map2.clear();
				
				Sheet3List.clear();
				jsonBodyString = "";
				Combo = false;
				Defined = true;
				ComboCount--;
				iterator3 = Sheet3.iterator();
		        iterator3.next();
	        }
	        map1.clear();
	        Sheet2List.clear();
		Sheet1List.clear();
    }

	@DataProvider(name="getExcelData")
	public Object[][] getExcelData() throws IOException {
		dataRepo = new dataRead();
		return dataRepo.getData(excelFilePath,"Sheet1");
	}
	
	@DataProvider(name="generateString")
	public Object[][] generateString() throws IOException {
		dataRepo = new dataRead();
		return dataRepo.dataProviderMethod_withDynamicRecords(3);
	}

	
	
	        
}
