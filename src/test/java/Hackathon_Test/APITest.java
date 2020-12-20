package Hackathon_Test;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.fasterxml.jackson.databind.ObjectMapper;

import dataUtil.Excel;
import dataUtil.RandomDataGen;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

@Listeners({ AllureListener.class })
public class APITest {

	Excel excel = new Excel();
	static SoftAssert softAssert = null;
	public static ThreadLocal<String> threadLocaldomain = new ThreadLocal<String>();
	public static ThreadLocal<Integer> threadLocalStatus = new ThreadLocal<Integer>();
	public static ThreadLocal<String> threadLocalpath = new ThreadLocal<String>();
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat readDateFormat = null;

	public void setStatus(int status) {

		threadLocalStatus.set(status);
	}

	public static int status() {
		return threadLocalStatus.get();
	}

	public void setDomain(String domain) {

		threadLocaldomain.set(domain);
	}

	public static String domain() {
		return threadLocaldomain.get();
	}

	public void setPath(String path) {

		threadLocalpath.set(path);
	}

	public static String path() {
		return threadLocalpath.get();
	}

	@Test(description = "Good Request")
	@Description("Good Request")
	@Epic("Ep0001")
	@Feature("Feature1")
	@Story("UserStory1")
	@Severity(SeverityLevel.NORMAL)
	public static void testGoodRequest()
			throws InterruptedException, EncryptedDocumentException, InvalidFormatException, IOException {
		Cell cell = null;
		Cell cell2 = null;
		HashMap<String, String> headerPASS = new HashMap<String, String>();
		HashMap<String, String> bodyPASS = new HashMap<String, String>();
		HashMap<String, String> paramsPASS = new HashMap<String, String>();
		HashMap<String, String> paramsQueryPASS = new HashMap<String, String>();
		String jsonBodyStringPASS = null;
		String requestMethod = null;
		String domain = null;
		String path = null;
		String parameterType = null;
		String excelPath = "./InputFolder/APITemplate.xlsx";
		Sheet sheet = null;
		String sheetName = null;
		InputStream file = new FileInputStream(excelPath);
		Workbook wb = WorkbookFactory.create(file);
		int statusPASS = 0;
		String headerName = null, headerValue = null;
		String parameterName = null, parameterValue = null;
		int temprow = 0;
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			sheet = wb.getSheetAt(i);
			System.out.println(sheet.getLastRowNum());
			for (int row = 1; row <= sheet.getLastRowNum(); row++) {
				if (sheet.getRow(row).getCell(0).toString().equalsIgnoreCase("domain")) {

					cell = sheet.getRow(row).getCell(1);
					cell.setCellType(CellType.STRING);
					domain = cell.getStringCellValue();
					threadLocaldomain.set(domain);

					System.out.println(threadLocaldomain.get());
				}
				if (sheet.getRow(row).getCell(0).toString().equalsIgnoreCase("Request Method")) {

					cell = sheet.getRow(row).getCell(1);
					cell.setCellType(CellType.STRING);
					requestMethod = cell.getStringCellValue();
					System.out.println(requestMethod);
				}
				if (sheet.getRow(row).getCell(0).toString().equalsIgnoreCase("Path")) {

					cell = sheet.getRow(row).getCell(1);
					cell.setCellType(CellType.STRING);
					path = cell.getStringCellValue();
					threadLocalpath.set(path);
					System.out.println(path);
				}

				if (sheet.getRow(row).getCell(0).toString().equalsIgnoreCase("Status") && statusPASS == 0) {
					cell = sheet.getRow(row).getCell(1);
					cell2 = sheet.getRow(row).getCell(2);
					cell.setCellType(CellType.STRING);

					statusPASS = (int) cell2.getNumericCellValue();
					threadLocalStatus.set(statusPASS);
					System.out.println(statusPASS);

					if (threadLocalStatus.get() >= 200 && threadLocalStatus.get() <= 299) {
						temprow = row;

						for (int j = row; j <= sheet.getLastRowNum() - 1; j++) {

							System.out.println(sheet.getRow(row).getCell(0).toString());
							if (sheet.getRow(row).getCell(0).toString().equalsIgnoreCase("header")) {
								cell = sheet.getRow(row).getCell(1);
								cell2 = sheet.getRow(row).getCell(2);
								cell.setCellType(CellType.STRING);
								cell2.setCellType(CellType.STRING);

								headerName = cell.getStringCellValue();

								headerValue = cell2.getStringCellValue();
								headerPASS.put(headerName, headerValue);
							}
							row++;
						}

						for (int paraRow = row - 1; paraRow <= sheet.getLastRowNum(); paraRow++) {
							System.out.println("Current Row: " + sheet.getRow(paraRow).getCell(0).toString() + "-> "
									+ sheet.getRow(paraRow).getCell(1).toString());
							if (sheet.getRow(paraRow).getCell(0).toString().contains("NormalParameter")) {
								parameterType = sheet.getRow(paraRow).getCell(0).toString();
								cell = sheet.getRow(paraRow).getCell(1);
								cell2 = sheet.getRow(paraRow).getCell(2);
								cell.setCellType(CellType.STRING);

								parameterName = cell.getStringCellValue();
								System.out.println(parameterName);

								parameterValue = getParameterValue(cell2);
								System.out.println(parameterValue);
								if (parameterValue.contains("RandomString")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.AlphaString_Sequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								if (parameterValue.contains("RandomNumber")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.NumberSequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								if (parameterValue.contains("RandomAlphaNum")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.AlphaNumericString_Sequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								bodyPASS.put(parameterName, parameterValue);
							}

							if (sheet.getRow(paraRow).getCell(0).toString().contains("FormParameter")) {
								parameterType = sheet.getRow(paraRow).getCell(0).toString();
								cell = sheet.getRow(paraRow).getCell(1);
								cell2 = sheet.getRow(paraRow).getCell(2);
								cell.setCellType(CellType.STRING);

								parameterName = cell.getStringCellValue();
								System.out.println(parameterName);
								parameterValue = getParameterValue(cell2);
								System.out.println(parameterValue);
								if (parameterValue.contains("RandomString")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.AlphaString_Sequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								if (parameterValue.contains("RandomNumber")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.NumberSequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								if (parameterValue.contains("RandomAlphaNum")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.AlphaNumericString_Sequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								paramsPASS.put(parameterName, parameterValue);
							}

							if (sheet.getRow(paraRow).getCell(0).toString().contains("QueryParameter")) {
								parameterType = sheet.getRow(paraRow).getCell(0).toString();
								cell = sheet.getRow(paraRow).getCell(1);
								cell2 = sheet.getRow(paraRow).getCell(2);
								cell.setCellType(CellType.STRING);
								parameterName = cell.getStringCellValue();
								System.out.println(parameterName);

								parameterValue = getParameterValue(cell2);
								System.out.println(parameterValue);
								if (parameterValue.contains("RandomString")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.AlphaString_Sequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}

								if (parameterValue.contains("RandomNumber")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.NumberSequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								if (parameterValue.contains("RandomAlphaNum")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.AlphaNumericString_Sequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								paramsQueryPASS.put(parameterName, parameterValue);
							}

						}
						if (sheet.getRow(row).getCell(0).toString().contains("FileUpload")) {
							parameterType = "JSON";
							if (sheet.getRow(row).getCell(2).toString().contains(".json")) {

								JSONParser parser = new JSONParser();
								Object obj = null;
								try {
									obj = parser.parse(new FileReader(sheet.getRow(row).getCell(2).toString()));
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								JSONObject jsonObject = (JSONObject) obj;
								ObjectMapper mapper = new ObjectMapper();
								jsonBodyStringPASS = jsonObject.toJSONString();
								bodyPASS = (HashMap<String, String>) mapper.readValue(jsonBodyStringPASS, Map.class);
							} else {
								ObjectMapper mapper = new ObjectMapper();
								jsonBodyStringPASS = sheet.getRow(row).getCell(2).toString();
								bodyPASS = (HashMap<String, String>) mapper.readValue(jsonBodyStringPASS, Map.class);

							}
						}
					}
				}

			}
			apiTestPASS(sheet.getSheetName(), threadLocaldomain.get(), requestMethod, parameterType, headerPASS,
					threadLocalpath.get(), paramsPASS, bodyPASS, statusPASS, paramsQueryPASS);
			bodyPASS.clear();
			headerPASS.clear();
			paramsPASS.clear();
			paramsQueryPASS.clear();
			statusPASS = 0;
		}

		wb.close();

	}

	@Test(description = "Bad Request")
	@Description("Bad Request")
	@Epic("Ep0001")
	@Feature("Feature1")
	@Story("UserStory1")
	@Severity(SeverityLevel.NORMAL)
	public static void testBadRequest()
			throws InterruptedException, EncryptedDocumentException, InvalidFormatException, IOException {
		Cell cell = null;
		Cell cell2 = null;
		String jsonBodyStringFAIL = null;
		HashMap<String, String> headerFAIL = new HashMap<String, String>();
		HashMap<String, String> bodyFAIL = new HashMap<String, String>();
		HashMap<String, String> paramsFAIL = new HashMap<String, String>();
		HashMap<String, String> paramsQueryFAIL = new HashMap<String, String>();
		String path = null;
		String requestMethod = null;
		String domain = null;
		String parameterType = null;
		String excelPath = "./InputFolder/APITemplate.xlsx";
		Sheet sheet = null;
		String sheetName = null;
		InputStream file = new FileInputStream(excelPath);
		Workbook wb = WorkbookFactory.create(file);
		int statusFAIL = 0;
		int temprow = 0;

		String headerName = null, headerValue = null;
		String parameterName = null, parameterValue = null;

		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			sheet = wb.getSheetAt(i);

			for (int row = 1; row <= sheet.getLastRowNum(); row++) {
				if (sheet.getRow(row).getCell(0).toString().equalsIgnoreCase("domain")) {

					cell = sheet.getRow(row).getCell(1);
					cell.setCellType(CellType.STRING);
					domain = cell.getStringCellValue();
					threadLocaldomain.set(domain);
					System.out.println(threadLocaldomain.get());
				}
				if (sheet.getRow(row).getCell(0).toString().equalsIgnoreCase("Request Method")) {

					cell = sheet.getRow(row).getCell(1);
					cell.setCellType(CellType.STRING);
					requestMethod = cell.getStringCellValue();
					System.out.println(requestMethod);
				}
				if (sheet.getRow(row).getCell(0).toString().equalsIgnoreCase("Path")) {

					cell = sheet.getRow(row).getCell(1);
					cell.setCellType(CellType.STRING);
					path = cell.getStringCellValue();
					threadLocalpath.set(path);
					System.out.println(path);
				}

				if (sheet.getRow(row).getCell(0).toString().equalsIgnoreCase("Status")) {
					cell = sheet.getRow(row).getCell(1);
					cell2 = sheet.getRow(row).getCell(2);

					cell.setCellType(CellType.STRING);

					statusFAIL = (int) cell2.getNumericCellValue();
					threadLocalStatus.set(statusFAIL);
					System.out.println(statusFAIL);

					if ((threadLocalStatus.get() >= 400 && threadLocalStatus.get() <= 499)|(threadLocalStatus.get() >= 500 && threadLocalStatus.get() <= 599)) {
						temprow = row;

						for (int j = row; j <= sheet.getLastRowNum() - 1; j++) {
							if (sheet.getRow(row).getCell(0).toString().equalsIgnoreCase("header")) {
								cell = sheet.getRow(row).getCell(1);
								cell2 = sheet.getRow(row).getCell(2);

								cell.setCellType(CellType.STRING);
								cell2.setCellType(CellType.STRING);

								headerName = cell.getStringCellValue();
								headerValue = cell2.getStringCellValue();
								headerFAIL.put(headerName, headerValue);
							}
							row++;
						}
						for (int paraRow = row - 1; paraRow <= sheet.getLastRowNum(); paraRow++) {

							if (sheet.getRow(paraRow).getCell(0).toString().contains("NormalParameter")) {
								parameterType = sheet.getRow(paraRow).getCell(0).toString();
								cell = sheet.getRow(paraRow).getCell(1);
								cell2 = sheet.getRow(paraRow).getCell(3);

								cell.setCellType(CellType.STRING);

								parameterName = cell.getStringCellValue();
								parameterValue = getParameterValue(cell2);

								parameterValue = cell2.getStringCellValue();
								if (parameterValue.contains("RandomString")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.AlphaString_Sequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								if (parameterValue.contains("RandomNumber")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.NumberSequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								if (parameterValue.contains("RandomAlphaNum")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.AlphaNumericString_Sequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}

								bodyFAIL.put(parameterName, parameterValue);
							}

							if (sheet.getRow(paraRow).getCell(0).toString().contains("FormParameter")) {
								parameterType = sheet.getRow(paraRow).getCell(0).toString();
								cell = sheet.getRow(paraRow).getCell(1);
								cell2 = sheet.getRow(paraRow).getCell(3);
								cell.setCellType(CellType.STRING);

								parameterName = cell.getStringCellValue();
								System.out.println(parameterName);
								parameterValue = getParameterValue(cell2);
								System.out.println(parameterValue);
								if (parameterValue.contains("RandomString")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.AlphaString_Sequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								if (parameterValue.contains("RandomNumber")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.NumberSequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								if (parameterValue.contains("RandomAlphaNum")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.AlphaNumericString_Sequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								paramsFAIL.put(parameterName, parameterValue);
							}

							if (sheet.getRow(paraRow).getCell(0).toString().contains("QueryParameter")) {
								parameterType = sheet.getRow(paraRow).getCell(0).toString();
								cell = sheet.getRow(paraRow).getCell(1);
								cell2 = sheet.getRow(paraRow).getCell(3);
								cell.setCellType(CellType.STRING);

								parameterName = cell.getStringCellValue();
								System.out.println(parameterName);

								parameterValue = getParameterValue(cell2);
								System.out.println(parameterValue);
								if (parameterValue.contains("RandomString")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.AlphaString_Sequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								if (parameterValue.contains("RandomNumber")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.NumberSequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								if (parameterValue.contains("RandomAlphaNum")) {
									parameterValue = parameterValue.replace("(", "");
									parameterValue = parameterValue.replace(")", "");
									parameterValue = parameterValue.split(",")[0];
									parameterValue = RandomDataGen.AlphaNumericString_Sequence_BetBounds(
											Integer.parseInt(parameterValue.split("-")[0]),
											Integer.parseInt(parameterValue.split("-")[1]));

								}
								paramsQueryFAIL.put(parameterName, parameterValue);
							}
						}
						if (sheet.getRow(row).getCell(0).toString().contains("FileUpload")) {
							parameterType = "JSON";
							if (sheet.getRow(row).getCell(3).toString().contains(".json")) {
								JSONParser parser = new JSONParser();
								Object obj = null;
								try {
									obj = parser.parse(new FileReader(sheet.getRow(row).getCell(3).toString()));
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								JSONObject jsonObject = (JSONObject) obj;
								ObjectMapper mapper = new ObjectMapper();
								jsonBodyStringFAIL = jsonObject.toJSONString();
								bodyFAIL = (HashMap<String, String>) mapper.readValue(jsonBodyStringFAIL, Map.class);
							} else {
								ObjectMapper mapper = new ObjectMapper();
								jsonBodyStringFAIL = sheet.getRow(row).getCell(3).toString();
								bodyFAIL = (HashMap<String, String>) mapper.readValue(jsonBodyStringFAIL, Map.class);

							}
						}
					}
				}

			}
			apiTestFAIL(sheet.getSheetName(), threadLocaldomain.get(), requestMethod, parameterType, headerFAIL,
					threadLocalpath.get(), paramsFAIL, bodyFAIL, statusFAIL, paramsQueryFAIL);
			bodyFAIL.clear();
			headerFAIL.clear();
			paramsFAIL.clear();
			paramsQueryFAIL.clear();
			statusFAIL = 0;
		}
		wb.close();

	}

	@Step("Verify API Good Request for {0}")
	public static void apiTestPASS(String sheetName, String domain, String requestMethod, String parameterType,
			HashMap<String, String> header, String path, HashMap<String, String> params, HashMap<String, String> body,
			int expectedStatus, HashMap<String, String> paramsQuery)
			throws EncryptedDocumentException, InvalidFormatException, InterruptedException, IOException {
		String responseBody = null;
		 softAssert = new SoftAssert();
		System.out.println(domain);
		System.out.println(header);
		System.out.println(path);
		RestAssured.baseURI = domain;
		Response res = null;

		if (requestMethod.equalsIgnoreCase("post") && parameterType.contains("JSON")) {

			try {
				res = RestAssured.given().log().all().headers(header).params(params).body(body).when()
						.relaxedHTTPSValidation().post(path);
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Body: " + body + "\nActual: " + res.getStatusCode()
						+ "->Expected: " + expectedStatus);
				// Assert.assertEquals(res.getStatusCode(), status);
				expectedStatus = threadLocalStatus.get();
				
				System.out.println(responseBody);
				
					String responseStatus = res.getStatusLine().toString();
					responseBody = res.getBody().asString();
					printlog(responseStatus, responseBody);
					

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (requestMethod.equalsIgnoreCase("get") && parameterType.contains("JSON")) {

			try {
				res = RestAssured.given().log().all().headers(header).params(params).body(body).when()
						.relaxedHTTPSValidation().get(path);
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Body: " + body + "\nActual: " + res.getStatusCode()
						+ "->Expected: " + expectedStatus);
				// Assert.assertEquals(res.getStatusCode(), status);
				expectedStatus = threadLocalStatus.get();
				String responseStatus = res.getStatusLine().toString();
				responseBody = res.getBody().asString();
				printlog(responseStatus, responseBody);
				Assert.assertEquals(res.getStatusCode(), expectedStatus, domain + ":" + path + "\n" + body);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (requestMethod.equalsIgnoreCase("post") && parameterType.contains("Normal")) {

			try {
				res = RestAssured.given().log().all().headers(header).params(params).body(body).when()
						.relaxedHTTPSValidation().post(path);
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Body: " + body + "\nActual: " + res.getStatusCode()
						+ "->Expected: " + expectedStatus);
				// Assert.assertEquals(res.getStatusCode(), status);
				expectedStatus = threadLocalStatus.get();

				String responseStatus = res.getStatusLine().toString();
				responseBody = res.getBody().asString();
				printlog(responseStatus, responseBody);
				Assert.assertEquals(res.getStatusCode(), expectedStatus, domain + ":" + path + "\n" + body);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (requestMethod.equalsIgnoreCase("get") && parameterType.contains("Normal")) {

			try {
				res = RestAssured.given().log().all().headers(header).params(params).body(body).when()
						.relaxedHTTPSValidation().get(path);
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Body: " + body + "\nActual: " + res.getStatusCode()
						+ "->Expected: " + expectedStatus);
				// Assert.assertEquals(res.getStatusCode(), status);
				expectedStatus = threadLocalStatus.get();
				String responseStatus = res.getStatusLine().toString();
				responseBody = res.getBody().asString();
				printlog(responseStatus, responseBody);

				System.out.println(responseBody);
				Assert.assertEquals(res.getStatusCode(), expectedStatus, domain + ":" + path + "\n" + body);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (requestMethod.equalsIgnoreCase("post") && parameterType.contains("Query")) {

			try {
				res = RestAssured.given().log().all().headers(header).queryParams(paramsQuery).params(params).body(body)
						.when().relaxedHTTPSValidation().post(path);
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Body: " + body + "\nActual: " + res.getStatusCode()
						+ "->Expected: " + expectedStatus);
				// Assert.assertEquals(res.getStatusCode(), status);
				expectedStatus = threadLocalStatus.get();
				String responseStatus = res.getStatusLine().toString();
				responseBody = res.getBody().asString();
				printlog(responseStatus, responseBody);
				System.out.println(responseBody);
				Assert.assertEquals(res.getStatusCode(), expectedStatus, domain + ":" + path + "\n" + body);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (requestMethod.equalsIgnoreCase("get") && parameterType.contains("Query")) {

			try {
				res = RestAssured.given().log().all().headers(header).queryParams(paramsQuery).params(params).body(body)
						.when().relaxedHTTPSValidation().get(path);
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Body: " + body + "\nActual: " + res.getStatusCode()
						+ "->Expected: " + expectedStatus);
				// Assert.assertEquals(res.getStatusCode(), status);
				expectedStatus = threadLocalStatus.get();
				String responseStatus = res.getStatusLine().toString();
				responseBody = res.getBody().asString();
				printlog(responseStatus, responseBody);
				System.out.println(responseBody);
				Assert.assertEquals(res.getStatusCode(), expectedStatus, domain + ":" + path + "\n" + body);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (requestMethod.equalsIgnoreCase("post") && parameterType.contains("Form")) {

			try {
				res = RestAssured.given().log().all().headers(header).queryParams(paramsQuery).params(params).when()
						.relaxedHTTPSValidation().post(path);
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Body: " + body + "\nActual: " + res.getStatusCode()
						+ "->Expected: " + expectedStatus);
				// Assert.assertEquals(res.getStatusCode(), status);
				expectedStatus = threadLocalStatus.get();
				String responseStatus = res.getStatusLine().toString();
				responseBody = res.getBody().asString();
				printlog(responseStatus, responseBody);
				System.out.println(responseBody);
				Assert.assertEquals(res.getStatusCode(), expectedStatus, domain + ":" + path + "\n" + body);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (requestMethod.equalsIgnoreCase("get") && parameterType.contains("Form")) {

			try {
				res = RestAssured.given().log().all().headers(header).queryParams(paramsQuery).params(params).when()
						.relaxedHTTPSValidation().get(path);
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Body: " + body + "\nActual: " + res.getStatusCode()
						+ "->Expected: " + expectedStatus);
				// Assert.assertEquals(res.getStatusCode(), status);
				expectedStatus = threadLocalStatus.get();
				String responseStatus = res.getStatusLine().toString();
				responseBody = res.getBody().asString();
				printlog(responseStatus, responseBody);
				System.out.println(responseBody);
				Assert.assertEquals(res.getStatusCode(), expectedStatus, domain + ":" + path + "\n" + body);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	@Step("Verify API Bad Request for {0}")
	public static void apiTestFAIL(String sheetName, String domain, String requestMethod, String parameterType,
			HashMap<String, String> header, String path, HashMap<String, String> params, HashMap<String, String> body,
			int expectedStatus, HashMap<String, String> paramsQuery)
			throws EncryptedDocumentException, InvalidFormatException, InterruptedException, IOException {
		String responseBody = null;
		 softAssert = new SoftAssert();
		System.out.println(domain);
		System.out.println(header);
		System.out.println(path);
		RestAssured.baseURI = domain;
		Response res = null;
		if (requestMethod.equalsIgnoreCase("post") && parameterType.equalsIgnoreCase("json")) {

			try {
				res = RestAssured.given().log().all().headers(header).params(params).body(body).when()
						.relaxedHTTPSValidation().post(path);
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Body: " + body + "\nActual: " + res.getStatusCode()
						+ "->Expected: " + expectedStatus);
				// Assert.assertEquals(res.getStatusCode(), status);
				expectedStatus = threadLocalStatus.get();
				String responseStatus = res.getStatusLine().toString();
				responseBody = res.getBody().asString();
				printlog(responseStatus, responseBody);
				System.out.println(responseBody);
				Assert.assertEquals(res.getStatusCode(), expectedStatus, domain + ":" + path + "\n" + body);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (requestMethod.equalsIgnoreCase("post") && parameterType.contains("Normal")) {

			try {
				res = RestAssured.given().log().all().headers(header).params(params).body(body).when()
						.relaxedHTTPSValidation().post(path);
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Body: " + body + "\nActual: " + res.getStatusCode()
						+ "->Expected: " + expectedStatus);
				// Assert.assertEquals(res.getStatusCode(), status);
				expectedStatus = threadLocalStatus.get();
				String responseStatus = res.getStatusLine().toString();
				responseBody = res.getBody().asString();
				printlog(responseStatus, responseBody);
				System.out.println(responseBody);
				Assert.assertEquals(res.getStatusCode(), expectedStatus, domain + ":" + path + "\n" + body);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (requestMethod.equalsIgnoreCase("get") && parameterType.contains("Normal")) {

			try {
				res = RestAssured.given().log().all().headers(header).params(params).body(body).when()
						.relaxedHTTPSValidation().get(path);
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Body: " + body + "\nActual: " + res.getStatusCode()
						+ "->Expected: " + expectedStatus);
				// Assert.assertEquals(res.getStatusCode(), status);
				expectedStatus = threadLocalStatus.get();
				String responseStatus = res.getStatusLine().toString();
				responseBody = res.getBody().asString();
				printlog(responseStatus, responseBody);
				System.out.println(responseBody);
				Assert.assertEquals(res.getStatusCode(), expectedStatus, domain + ":" + path + "\n" + body);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (requestMethod.equalsIgnoreCase("post") && parameterType.contains("Query")) {

			try {
				res = RestAssured.given().log().all().headers(header).queryParams(paramsQuery).params(params).body(body)
						.when().relaxedHTTPSValidation().post(path);
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Body: " + body + "\nActual: " + res.getStatusCode()
						+ "->Expected: " + expectedStatus);
				// Assert.assertEquals(res.getStatusCode(), status);
				expectedStatus = threadLocalStatus.get();
				String responseStatus = res.getStatusLine().toString();
				responseBody = res.getBody().asString();
				printlog(responseStatus, responseBody);
				System.out.println(responseBody);
				Assert.assertEquals(res.getStatusCode(), expectedStatus, domain + ":" + path + "\n" + body);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (requestMethod.equalsIgnoreCase("get") && parameterType.contains("Query")) {

			try {
				res = RestAssured.given().log().all().headers(header).queryParams(paramsQuery).params(params).body(body)
						.when().relaxedHTTPSValidation().get(path);
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Body: " + body + "\nActual: " + res.getStatusCode()
						+ "->Expected: " + expectedStatus);
				// Assert.assertEquals(res.getStatusCode(), status);
				expectedStatus = threadLocalStatus.get();
				String responseStatus = res.getStatusLine().toString();
				responseBody = res.getBody().asString();
				printlog(responseStatus, responseBody);
				System.out.println(responseBody);
				Assert.assertEquals(res.getStatusCode(), expectedStatus, domain + ":" + path + "\n" + body);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	@Step("Response Status is {0}\n Response Body {1}")
	public static void printlog(String responseStatus, String responseBody) {
		try
		{
			
	Assert.assertEquals(responseStatus.contains(String.valueOf(threadLocalStatus.get())), true);
	}
	catch(AssertionError e)
	{
		responseStatus =e.getMessage();
				Assert.fail(responseStatus);
		
	}

	}

	@BeforeSuite
	public static void deleteAllureReport() throws IOException {
		String projectPath = System.getProperty("user.dir");
		try {
			FileUtils.deleteDirectory(new File(projectPath + "/allure-results"));
		} catch (Exception e) {
			System.out.println("Allure Result Folder Does not Exists");
		}
	}

	@AfterTest
	public static void raiseAssertion() {
		// Assert.assertAll();
	}

	@AfterSuite
	public static void generateAllureReport() throws IOException {
		//Linux - Generate Allure Report
		String command = "allure -c /allure-results";
		Process proc = Runtime.getRuntime().exec(command);
		try {
			proc.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static SimpleDateFormat isDate(String date) {
		SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyy/dd/MM");
		SimpleDateFormat format4 = new SimpleDateFormat("yyyy/MM/dd");

		SimpleDateFormat format5 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat format6 = new SimpleDateFormat("MM-dd-yyyy");
		SimpleDateFormat format7 = new SimpleDateFormat("yyyy-dd-MM");
		SimpleDateFormat format8 = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<SimpleDateFormat> formats = new ArrayList<SimpleDateFormat>();
		formats.add(format1);
		formats.add(format2);
		formats.add(format3);
		formats.add(format4);
		formats.add(format5);
		formats.add(format6);
		formats.add(format7);
		formats.add(format8);

		for (SimpleDateFormat format : formats) {
			try {
				format.setLenient(false);
				format.parse(date);
				System.out.println("Format Applied: " + format.toString());
				return format;
			} catch (Exception e) {
			}
		}
		return null;
	}

	public static String getDate(String date, SimpleDateFormat currentFormat) {
		try {
			System.out.println("Get Formatted Date: " + dateFormat.format(currentFormat.parse(date)));
			return dateFormat.format(currentFormat.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static String getParameterValue(Cell cell) {
		String parameterValue = null;
		try {
			// Check if it is date else revert the type
			readDateFormat = isDate(cell.getStringCellValue());
			if (readDateFormat != null)
				parameterValue = getDate(cell.getStringCellValue(), readDateFormat);
			else {
				cell.setCellType(CellType.STRING);
				parameterValue = cell.getStringCellValue();
			}
				
		}
		catch(Exception e) {
				if(!DateUtil.isCellDateFormatted(cell)) {
					parameterValue = String.valueOf(cell.getNumericCellValue());
				}
				else if(DateUtil.isCellDateFormatted(cell)) {
					parameterValue = dateFormat.format(cell.getDateCellValue());
				}
		}
		
		
		return parameterValue;
	}

}
