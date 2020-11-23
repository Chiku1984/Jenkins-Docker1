package Hackathon_Test;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.concurrent.TimeUnit;

public class BrowserFactory {
    public static Object[][] DriverExcel = null;
    public static ThreadLocal<WebDriver> localDriver = new ThreadLocal<WebDriver>();
    public static WebDriverWait wait;
    public static WebDriver driver;
    public static final int minWait = 5, medWait = 15, longWait = 30, maxWait = 60;
    
    public void SetWebDriver(WebDriver obj)
    {
    	localDriver.set(obj);
    }
    public static WebDriver GetWebDriver()
    {
        return localDriver.get();
    }

    //@BeforeMethod
    public void Init_HubExecution() throws MalformedURLException {
        DesiredCapabilities capabilitiesObj = DesiredCapabilities.firefox();
        capabilitiesObj.setVersion("");
        capabilitiesObj.setPlatform(Platform.ANY);
        WebDriver driverTest = new RemoteWebDriver(new URL("http://10.92.130.43:4444/wd/hub"),capabilitiesObj);
        SetWebDriver(driverTest);
        GetWebDriver().manage().window().maximize();
    }

    @BeforeTest(alwaysRun = true)
    @Parameters({"selenium.browser"})
    public void Init_LocalExecution(String browser)
    {
        switch(browser)
        {
            case "firefox":
            	WebDriverManager.firefoxdriver().setup();
            	localDriver.set(new FirefoxDriver());
            	driver = localDriver.get();
                wait = new WebDriverWait(driver,maxWait);
                break;
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("start-maximized");
                options.addArguments("enable-automation");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-infobars");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-browser-side-navigation");
                options.addArguments("--disable-gpu");
                //For Headless execution
                options.addArguments("--headless", "--window-size=1920,1200","--ignore-certificate-errors");
                localDriver.set(new ChromeDriver(options));
                driver = localDriver.get();
                wait = new WebDriverWait(driver,maxWait);
                break;
            default:
                System.out.println("Not A valid Browser - Only Firefox & Chrome Supported");
        }
        GetWebDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        GetWebDriver().manage().window().maximize();
    }
    @AfterTest(alwaysRun = true)
    public void Destroy_LocalExecution()
    {
        GetWebDriver().quit();
        SetWebDriver(null);
    }
}
