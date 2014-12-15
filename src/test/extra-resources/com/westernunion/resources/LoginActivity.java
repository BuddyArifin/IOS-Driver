package com.westernunion.resources;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.iosdriver.util.CommonUtils;


public class LoginActivity {
	
	public LoginActivity (WebDriver driver) throws Exception{
		
	}
	
	public static void loginActivityPage(WebDriver driver) throws Exception{
		
		// Assert Login button - when landing on login page.
		driver.findElement(By.id("btn-main-login-mobile")).click();
		CommonUtils.dynamicWait(driver, By.className("btn-homePage"), By.id("btn-do-login"));
		WebElement signInButton = driver.findElement(By.id("btn-do-login"));
		Assert.assertEquals(signInButton.isDisplayed(), true);
		
		// Login with Blank Credential
		signInButton.click();
		Assert.assertEquals(driver.findElement(By.id("error-msg-username")).isDisplayed(), true);
		Assert.assertEquals(driver.findElement(By.id("error-msg-password")).isDisplayed(), true);
		
		// Login with Blank password
		WebElement password = driver.findElement(By.id("wu-password-textbox"));
		WebElement username = driver.findElement(By.id("wu-username-textbox"));
		username.sendKeys("bugs@mail.com");
		signInButton.click();
		Assert.assertEquals(driver.findElement(By.id("error-msg-password")).isDisplayed(), true);
		
		// Login with Blank username
		username.clear();
		password.sendKeys("Welcome123");
		signInButton.click();
		Assert.assertEquals(driver.findElement(By.id("error-msg-username")).isDisplayed(), true);
		
		// Login with invalid Credential
		password.clear();
		username.sendKeys("invalid@mail.com");
		password.sendKeys("Welcome1");
		signInButton.click();
		CommonUtils.dynamicWait(driver, By.className("spinner-container"), By.className("wum-popup-button"));
		Assert.assertEquals(driver.findElement(By.className("wum-popup-button")).isDisplayed(), true);
		driver.findElement(By.xpath("//div[@class='wum-popup-button']/div[1]")).click();
		

		// Login with format email
		
		
		
		// Login with format password
		
		
		
	}

}
