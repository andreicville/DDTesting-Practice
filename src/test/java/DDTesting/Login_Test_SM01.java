package DDTesting;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.opencsv.exceptions.CsvException;

import Utilities.ParseCSVData;
import Utilities.readConfig;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Login_Test_SM01{
	 static WebDriver driver;
	 
	@BeforeMethod  // Open base URL specified in Config with Browser specified in Config
	public static void setUp() throws Exception {
		
		 switch (readConfig.getProp("browser")) {
         case "firefox":
             WebDriverManager.firefoxdriver().setup();
             driver = new FirefoxDriver();
             break;
         case "chrome":
             WebDriverManager.chromedriver().setup();
             driver = new ChromeDriver();
             break;
         case "ie":
             WebDriverManager.iedriver().setup();
             driver = new InternetExplorerDriver();
             break;
         }
		 
		 driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		 driver.get(readConfig.getProp("website"));
	}

	@DataProvider(name = "myTests")
	public Object[][] testData() throws IOException, CsvException {
		String csvTestData = readConfig.getProp("csvTestData");

		ParseCSVData myData = new ParseCSVData();

		return myData.getCSVFileData(csvTestData);
	}

	@Test(dataProvider = "myTests")
	public void Test_Scenario_SM1(String userName, String passWord) throws IOException {
		String actualBoxtitle;
		// Enter user name
		driver.findElement(By.name("uid")).clear();
		driver.findElement(By.name("uid")).sendKeys(userName);
		// Enter Password
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(passWord);
		// Click Login
		driver.findElement(By.name("btnLogin")).click();
		try {
			// Alert message pop up
			Alert alt = driver.switchTo().alert();
			actualBoxtitle = alt.getText();
			alt.accept();
			// Getting expected error?
			Assert.assertEquals(actualBoxtitle, readConfig.getProp("expected_error"));

		} catch (NoAlertPresentException e) {
			// No Alert message ...
			String loginText = driver.findElement(By.tagName("tbody")).getText();
			String[] textParts = loginText.split(":");
			String dynamicText = textParts[1].trim();

			Assert.assertTrue(dynamicText.substring(0, 5).contains("mngr"));
			String remain = dynamicText.substring(dynamicText.length() - 4);

			Assert.assertTrue(remain.matches("[0-9]+"));

			Calendar rightNow = Calendar.getInstance();
			int day = rightNow.get(Calendar.DAY_OF_MONTH);
			int hour = rightNow.get(Calendar.HOUR_OF_DAY);
			int minute = rightNow.get(Calendar.MINUTE);

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.home") + File.separator + "TestScenarioSM1-"
					+ day + "-" + hour + "-" + minute + ".png"));
		}
	}

	@AfterMethod
	public void wrappingUp() {
		driver.close();
	}

}
