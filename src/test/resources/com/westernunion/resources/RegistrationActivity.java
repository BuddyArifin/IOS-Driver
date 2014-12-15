package com.westernunion.resources;

import java.net.URL;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.uiautomation.ios.IOSCapabilities;
import org.uiautomation.ios.client.uiamodels.impl.RemoteIOSDriver;
import org.uiautomation.ios.client.uiamodels.impl.RemoteUIAPickerWheel;
import org.uiautomation.ios.client.uiamodels.impl.augmenter.IOSDriverAugmenter;

import com.iosdriver.constant.AlertConstant;
import com.iosdriver.util.CommonUtils;

public class RegistrationActivity {
	public static Select select ;
	public static Select selectQuestion1 ;
	public static Select selectQuestion2 ;
	public static Select selectQuestion3 ;
	
	public static void registrationActivityPage(RemoteWebDriver driver) throws Exception {
		driver.findElement(By.id("register-now")).click();
		CommonUtils.dynamicWait(driver, By.id("register-now"), By.id("wu_register_email"));
		
		/** Assert inline message on Account Tab */
		WebElement Reg_Next = driver.findElement(By.id("wum_profile_btn_next"));
		Reg_Next.click();
		Assert.assertEquals(driver.findElement(By.id("wu_register_email_error")).getText(), AlertConstant.reg_inline_email);
		Assert.assertEquals(driver.findElement(By.id("wu_register_password_error")).getText(), AlertConstant.reg_inline_password);
		Assert.assertEquals(driver.findElement(By.id("wu_register_password_repeat_error")).getText(), AlertConstant.reg_inline_confirm);
		Assert.assertEquals(driver.findElement(By.id("wu_register_first_name_error")).getText(), AlertConstant.reg_inline_firstname);
		Assert.assertEquals(driver.findElement(By.id("wu_register_last_name_error")).getText(), AlertConstant.reg_inline_firstname);
		
		/** input Account Tab */
		String[] dataForm = {"automation1.0@mail.com", "Welcome123", "Welcome123", "Bugs", "Mail"};
		
		driver.findElement(By.id("wu_register_email")).sendKeys(dataForm[0]);
		driver.findElement(By.id("wu_register_password")).sendKeys(dataForm[1]);
		driver.findElement(By.id("wu_register_password_repeat")).sendKeys(dataForm[2]);
		driver.findElement(By.id("wu_register_first_name")).sendKeys(dataForm[3]);
		driver.findElement(By.id("wu_register_last_name")).sendKeys(dataForm[4]);
		Reg_Next.click();
		
		/** Assert inline error Address Tab **/
		CommonUtils.dynamicWait(driver, By.id("wu_register_last_name"), By.id("wu_register_addr_line1"));
		WebElement addressNextButton = driver.findElement(By.id("wum_profile_btn_next"));
		CommonUtils.waitForText(10);
		addressNextButton.click();
		Assert.assertEquals(driver.findElement(By.id("wu_register_addr_line1_error")).getText(), AlertConstant.reg_inline_address);
		Assert.assertEquals(driver.findElement(By.id("wu_register_city_error")).getText(), AlertConstant.reg_inline_city);
		Assert.assertEquals(driver.findElement(By.id("wu_register_state_error")).getText(), AlertConstant.reg_inline_state);
		Assert.assertEquals(driver.findElement(By.id("wu_register_postal_code_error")).getText(), AlertConstant.reg_inline_zipcode);
		Assert.assertEquals(driver.findElement(By.id("wu_register_mobile_error")).getText(), AlertConstant.reg_inline_mobilePhone);
		
		/** Input address Tab */
		String[] dataFormAddressTab = {"Warrent 123", "philadelphia", "19029", "2025555886"};
		
		driver.findElement(By.id("wu_register_addr_line1")).sendKeys(dataFormAddressTab[0]);
		driver.findElement(By.id("wu_register_city")).sendKeys(dataFormAddressTab[1]);
		new Select(driver.findElement(By.id("wu_register_state"))).selectByValue("PA");
		CommonUtils.waitForText(5);
		driver.findElement(By.id("wu_register_postal_code")).sendKeys(dataFormAddressTab[2]);
		System.out.println("Log this>>>>");
		driver.findElement(By.id("wu_register_mobile")).sendKeys(dataFormAddressTab[3]);
		addressNextButton.click();
		
		
		/** Assert inline error message Security tab */
		CommonUtils.dynamicWait(driver, By.id("wu_register_mobile"), By.id("wu_register_answer1"));
		driver.findElement(By.id("wum_profile_btn_next")).click();
		Assert.assertEquals(driver.findElement(By.id("wum_register_birth_date_error")).getText(), AlertConstant.reg_inline_birthday);
		Assert.assertEquals(driver.findElement(By.id("wu_register_question1_error")).getText(), AlertConstant.reg_inline_question);
		Assert.assertEquals(driver.findElement(By.id("wu_register_answer1_error")).getText(), AlertConstant.reg_inline_answer);
		Assert.assertEquals(driver.findElement(By.id("wu_register_question2_error")).getText(), AlertConstant.reg_inline_question);
		Assert.assertEquals(driver.findElement(By.id("wu_register_answer2_error")).getText(), AlertConstant.reg_inline_answer);
		Assert.assertEquals(driver.findElement(By.id("wu_register_question3_error")).getText(), AlertConstant.reg_inline_question);
		Assert.assertEquals(driver.findElement(By.id("wu_register_answer3_error")).getText(), AlertConstant.reg_inline_answer);
		Assert.assertEquals(driver.findElement(By.id("wu_register_captcha_text_error")).getText(), AlertConstant.reg_inline_captcha);
		Assert.assertEquals(driver.findElement(By.id("wu_register_terms_error")).getText(), AlertConstant.reg_inline_terms);
		
		/** Input Security Tab */
		driver.findElement(By.id("wum-register-birthdate-type-date")).click();

		
		if (CommonUtils.getNativeView().equalsIgnoreCase("Native")) {
			/** IOS Date Picker */
			CommonUtils.waitForText(20);
			driver.switchTo().window(CommonUtils.getNativeView());
			RemoteIOSDriver iosDriver = IOSDriverAugmenter.getIOSDriver(new RemoteWebDriver(new URL("http://localhost:18001/wd/hub"), 
					IOSCapabilities.iphone("WesternUnion")));
			WebElement picker = iosDriver.findElement(By.xpath("//UIAPicker[1]//UIAPickerWheel[2]"));
			WebElement picker1 = iosDriver.findElement(By.xpath("//UIAPicker[1]//UIAPickerWheel[1]"));
			WebElement picker2 = iosDriver.findElement(By.xpath("//UIAPicker[1]//UIAPickerWheel[3]"));
			((RemoteUIAPickerWheel)picker1).select("1");
			((RemoteUIAPickerWheel)picker).select("January");
			((RemoteUIAPickerWheel)picker2).select("1980");
			iosDriver.findElement(By.xpath("//UIAButton[@name='Done']")).click();
			driver.switchTo().window(CommonUtils.getWebView());
		}
		else if (CommonUtils.getNativeView().equalsIgnoreCase("NATIVE_APP")) {	
			/** Android Date Picker */
			CommonUtils.waitForText(20);
			driver.switchTo().window(CommonUtils.getNativeView());
			driver.findElement(By.xpath("//NumberPicker[@id='day']/EditText")).clear();
			driver.findElement(By.xpath("//NumberPicker[@id='day']/EditText")).sendKeys("01");
			driver.findElement(By.xpath("//NumberPicker[@id='month']/EditText")).clear();
			driver.findElement(By.xpath("//NumberPicker[@id='month']/EditText")).sendKeys("Jan");
			driver.findElement(By.xpath("//NumberPicker[@id='year']/EditText")).clear();
			driver.findElement(By.xpath("//NumberPicker[@id='year']/EditText")).sendKeys("1980");
			CommonUtils.waitForText(10);
			driver.findElement(By.xpath("//Button[@id='button1']")).click();
			driver.switchTo().window(CommonUtils.getWebView());
			
		}
		
		selectQuestion1 = new Select(driver.findElement(By.xpath("//select[@id='wu_register_question1']")));
		CommonUtils.waitForText(5);
		selectQuestion1.selectByValue("favTVShow");
		driver.findElement(By.id("wu_register_answer1")).sendKeys("Comedy Tv");
		CommonUtils.waitForText(5);
		selectQuestion2 = new Select(driver.findElement(By.xpath("//select[@id='wu_register_question2']")));
		selectQuestion2.selectByValue("favHobby");
		driver.findElement(By.id("wu_register_answer2")).sendKeys("Comedy Hobby");
		CommonUtils.waitForText(5);
		selectQuestion3 = new Select(driver.findElement(By.xpath("//select[@id='wu_register_question3']")));
		selectQuestion3.selectByValue("favMovie");
		driver.findElement(By.id("wu_register_answer3")).sendKeys("Comedy Movie");
		CommonUtils.waitForText(5);
		driver.findElement(By.id("wu_register_terms")).click();
		
	}

}
