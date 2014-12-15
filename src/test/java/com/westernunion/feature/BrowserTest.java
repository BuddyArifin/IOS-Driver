package com.westernunion.feature;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.iosdriver.util.CommonUtils;
import com.westernunion.webapp.LoginActivity;
import com.westernunion.webapp.RegistrationActivity;


public class BrowserTest {
	WebDriver driver;
	
	@Parameters({"platform", "nodeUrl"})
	@Test(enabled=false)
	public void test(String platform, String nodeUrl) throws Exception {
		driver = CommonUtils.selectedPlatformServer(platform, nodeUrl);
		CommonUtils.waitForText(10);
		
		driver.get("https://wudispatcher-dev51.wucloud.net/us/en/m/home.html");
		
		LoginActivity.loginActivityPage(driver);
		RegistrationActivity.registrationActivityPage(driver);
	}
}
