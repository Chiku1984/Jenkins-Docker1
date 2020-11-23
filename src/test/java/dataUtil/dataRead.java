package dataUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.model.InternalSheet;
import org.apache.poi.hssf.record.DVRecord;
import org.apache.poi.hssf.record.aggregates.DataValidityTable;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class dataRead 
{

	XSSFWorkbook excelWorkbook = null;
	XSSFSheet excelSheet = null;
	XSSFRow row = null;
	XSSFCell cell = null;
	WebDriver driver = null;
	String fileName = null;
	@DataProvider(name = "dataProviderMethod_withDynamicRecords")
	public Object[][] dataProviderMethod_withDynamicRecords(int length) {
		boolean testStepPassed = false;
		Object[][] data = new Object[3][1];
		data[0][0] = RandomDataGen.String_Sequence(length-1);
		data[1][0] = RandomDataGen.String_Sequence(length);
		data[2][0] = RandomDataGen.String_Sequence(length+1);
		
		if (data.length == 3) {
			testStepPassed = true;
		}
		Assert.assertEquals(testStepPassed, true);
		return data;
	}
	
	@Test(dataProvider = "getData") //dataProvider value should be equal to @DataProvider method name
	public void doLogin(String text) throws InterruptedException 
	{ //no. of parameter = no. of columns

		System.setProperty("webdriver.chrome.driver","C://Automation//chromedriver_win32//chromedriver.exe");
		driver=new ChromeDriver();

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get("http://www.google.com");
		driver.findElement(By.xpath("//input[@title='Search']")).sendKeys(text);
		driver.findElement(By.name("btnK")).click();
		Thread.sleep(7000);
		driver.quit();
	}


	@DataProvider // supplying data for a test method.
	public Object[][] getData(String fileName, String sheetName) throws IOException 
	{
		FileInputStream fis = new FileInputStream(fileName); // Your .xlsx file name along with path
		excelWorkbook = new XSSFWorkbook(fis);
		// Read sheet inside the workbook by its name
		excelSheet = excelWorkbook.getSheet(sheetName); //Your sheet name
		// Find number of rows in excel file
		System.out.println("First Row Number/index:"+ excelSheet.getFirstRowNum() + " *** Last Row Number/index:"
				+ excelSheet.getLastRowNum());
		int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum()+1;
		int colCount = excelSheet.getRow(0).getLastCellNum();
		System.out.println("Row Count is: " + rowCount
				+ " *** Column count is: " + colCount);
		Object data[][] = new Object[rowCount-1][colCount];
		for (int rNum = 2; rNum <= rowCount; rNum++) 
		{
			for (int cNum = 0; cNum < colCount; cNum++) 
			{
				System.out.print(getCellData("Sheet1", cNum, rNum) + " "); // Your sheet name
				data[rNum - 2][cNum] = getCellData("Sheet1", cNum, rNum); //Your sheet name
			}
			System.out.println();
		}
		return data;
	}
	// Function will always used as below. It returns the data from a cell - No need to make any changes
	public String getCellData(String sheetName, int colNum, int rowNum) throws IOException 
	{
		FileInputStream fis = new FileInputStream(fileName); // Your .xlsx file name along with path
		excelWorkbook = new XSSFWorkbook(fis);
		try
		{
			if (rowNum <= 0)
				return "";
			int index = excelWorkbook.getSheetIndex(sheetName);
			if (index == -1)
				return "";
			excelSheet = excelWorkbook.getSheetAt(index);
			row = excelSheet.getRow(rowNum - 1);
			if (row == null)
				return "";
			cell = row.getCell(colNum);
			if (cell == null)
				return "";
			if (cell.getCellType() == CellType.STRING)
				return cell.getStringCellValue();
			else if (cell.getCellType() == CellType.NUMERIC
					|| cell.getCellType() == CellType.FORMULA)
			{
				cell.setCellType(CellType.STRING);
				String cellText = String.valueOf(cell.getStringCellValue());
				return cellText;
			} else if (cell.getCellType() == CellType.BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e)
		{
			e.printStackTrace();
			return "row " + rowNum + " or column " + colNum
					+ " does not exist in xls";
		}
	}
	
	public String getRequestName(String fileName, String sheetName, String Request) throws IOException {
		String requestName = null;
		this.fileName = fileName;
		FileInputStream fis = new FileInputStream(fileName); // Your .xlsx file name along with path
		XSSFWorkbook excelWorkbook = new XSSFWorkbook(fis);
		
		// Read sheet inside the workbook by its name
		XSSFSheet excelSheet = excelWorkbook.getSheet(sheetName);
		
		// Find number of rows & columns in excel file
		int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum()+1;
		int colCount = excelSheet.getRow(0).getLastCellNum();
		
		for(int rowNum = 1; rowNum <= rowCount; rowNum++) {
			for(int colNum = 0; colNum < colCount; colNum++) {
				if(getCellData(sheetName, colNum, rowNum).equalsIgnoreCase(Request)) {
					requestName = getCellData(sheetName, colNum+1, rowNum);
					break;
				}
			}
		}
		
		
		return requestName;
	}
	
	public String getRequestValue(String fileName, String sheetName, String requestName) throws IOException {
		String requestValue = null;
		this.fileName = fileName;
		FileInputStream fis = new FileInputStream(fileName); // Your .xlsx file name along with path
		XSSFWorkbook excelWorkbook = new XSSFWorkbook(fis);
		// Read sheet inside the workbook by its name
		XSSFSheet excelSheet = excelWorkbook.getSheet(sheetName);
		
		// Find number of rows & columns in excel file
		int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum()+1;
		int colCount = excelSheet.getRow(0).getLastCellNum();
		
		for(int rowNum = 1; rowNum <= rowCount; rowNum++) {
			for(int colNum = 0; colNum < colCount; colNum++) {
				if(getCellData(sheetName, colNum, rowNum).equalsIgnoreCase(requestName)) {
					requestValue = getCellData(sheetName, colNum+1, rowNum);
					break;
				}
			}
		}
		
		
		return requestValue;
	}
	
	public LinkedList<String> getRequest(String fileName, String sheetName) throws IOException {
		LinkedList<String> requests = new LinkedList<String>();
		this.fileName = fileName;
		FileInputStream fis = new FileInputStream(fileName); // Your .xlsx file name along with path
		XSSFWorkbook excelWorkbook = new XSSFWorkbook(fis);
		// Read sheet inside the workbook by its name
		XSSFSheet excelSheet = excelWorkbook.getSheet(sheetName);
		
		// Find number of rows & columns in excel file
		int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum()+1;
		int colCount = excelSheet.getRow(0).getLastCellNum();
		
		for(int rowNum = 1; rowNum <= rowCount; rowNum++) {
			requests.add(getCellData(sheetName, 0, rowNum));
		}
		return requests;
	}
		
		public List<String> getCellDropdown(String filePath, String sheetName) {
			XSSFWorkbook wb = null;
			String[] explicitListValues = null;
			try {
			    wb = (XSSFWorkbook) WorkbookFactory.create(new FileInputStream(filePath));
			} catch (EncryptedDocumentException | IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}

			XSSFSheet sheet = wb.getSheet(sheetName);

			List<XSSFDataValidation> dataValidations = sheet.getDataValidations();
			Iterator<XSSFDataValidation> iterator = dataValidations.iterator();
			while(iterator.hasNext()){
			    XSSFDataValidation dataValidation = iterator.next();
			    explicitListValues = dataValidation.getValidationConstraint().getExplicitListValues();

			}

			return Arrays.asList(explicitListValues);
		}
}
