package com.westernunion.feature;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.iosdriver.util.CommonUtils;
import com.westernunion.resources.LoginActivity;
import com.westernunion.resources.RegistrationActivity;

public class AllTest {
	RemoteWebDriver driver;
	
  
 
  @Parameters ({"platform", "nodeUrl"})
  @Test(enabled=true)
  public void test(String platform, String nodeUrl) throws Exception {
	  driver = CommonUtils.selectedPlatformServer(platform, nodeUrl);
	  CommonUtils.waitForText(10);
	 
	  // Do Login Test Cases
	  LoginActivity.loginActivityPage(driver);
	  
	  // Do registration Test Cases
	  RegistrationActivity.registrationActivityPage(driver);
	  
  }
  
  
  @AfterTest
  public void tearDown() {
	  driver.quit();
  }

}
