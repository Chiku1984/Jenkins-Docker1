package Hackathon_Test;

import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import dataUtil.RandomDataGen;

public class DataProviderRepo {
	@Step("Data Generation Step - Static Iterations")
	@DataProvider(name = "Sample-data-provider")
	public Object[][] dataProviderMethod() {
		return new Object[][] { { "FirstVal" }, { "SecondVal" } };
	}

	@Step("Data Generation Step - Number of Iterations{0}")
	@DataProvider(name = "Sample-data-provider2")
	public Object[][] dataProviderMethod_withDynamicRecords(ITestContext InputDataiteration) {
		boolean testStepPassed = false;
		int iteration = Integer.parseInt(InputDataiteration.getCurrentXmlTest().getParameter("DataProvider_Iteration"));
		Object[][] data = new Object[iteration][1];
		for (int i = 0; i < iteration; i++) {
			data[iteration][0] = RandomDataGen.AlphaNumericString_Sequence(5);
		}
		if (data.length == iteration) {
			testStepPassed = true;
		}
		Assert.assertEquals(testStepPassed, true);
		return data;
	}

	@DataProvider(name = "Data Base Connection details")
	public Object[][] dataProviderMethod_DB() {

		Object[][] data = new Object[1][5];
		data[0][0] = "sqlserver";
		data[0][1] = "#Domain name#";
		data[0][2] = "#Database name #";
		data[0][3] = "#Username#";
		data[0][4] = "#Password to be Set#";
		return data;
	}

}
