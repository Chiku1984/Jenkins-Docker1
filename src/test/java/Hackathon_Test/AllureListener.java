package Hackathon_Test;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureListener extends BrowserFactory implements ITestListener {
    private static String getTestMethodName(ITestResult iTestResult)
    {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }
    @Attachment
    public byte[] saveFailureScreenShot(WebDriver driver)
    {
        return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "{0}",type = "text/plain")
    public static String saveTextLog(String message)
    {
        return message;
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("On TestStart Method "+getTestMethodName(result) + " Start");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("On TestSuccess Method "+getTestMethodName(result) + " Success");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("On TestFailure Method "+getTestMethodName(result) + " Failure");
        WebDriver dr = GetWebDriver();
        if(dr instanceof WebDriver)
        {
            System.out.println("Screenshot Captured for Test Case "+getTestMethodName(result)+ " Failure");
            saveFailureScreenShot(dr);
        }
        saveTextLog(getTestMethodName(result)+ "Test Failed & Screenshot Taken!");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("On TestSkipped Method "+getTestMethodName(result) + " Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("On TestFailedButWithinSuccessPercentage Method "+getTestMethodName(result) + " Failed with success %");
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        System.out.println("On TestFailedWithTimeout Method "+getTestMethodName(result) + " Failed due to Timeout");
        WebDriver dr = GetWebDriver();
        if(dr instanceof WebDriver)
        {
            System.out.println("Screenshot Captured for Test Case "+getTestMethodName(result)+ " Timeout");
            saveFailureScreenShot(dr);
        }
        saveTextLog(getTestMethodName(result)+ "Test Failed & Screenshot Taken!");
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("On Start Method "+context.getName());
        context.setAttribute("Webdriver",GetWebDriver());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("On Finish Method "+context.getName());
    }
}
