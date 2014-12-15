package com.iosdriver.util;

import io.selendroid.SelendroidCapabilities;
import io.selendroid.SelendroidConfiguration;
import io.selendroid.SelendroidDriver;
import io.selendroid.SelendroidLauncher;
import io.selendroid.device.DeviceType;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.uiautomation.ios.IOSCapabilities;
import org.uiautomation.ios.server.IOSServer;
import org.uiautomation.ios.server.IOSServerConfiguration;

public class CommonUtils {
	public static IOSServerConfiguration config;
	public static IOSServer server;
	public static WebDriver driver;
	public static RemoteWebDriver nativeDriver;
	private static final String ERROR_SCREENSHOOT_PNG = "ErrorScreenshoot.png";
	public static Logger log = Logger.getRootLogger();
	public static DesiredCapabilities capabilities;
	public static String nativeView;
	public static String webView;
	
	public static String getWebView() {
		return webView;
	}

	public static String getNativeView() {
		return nativeView;
	}

	public static void setNativeView(String nativeView) {
		CommonUtils.nativeView = nativeView;
	}

	public static void setWebView(String webView) {
		CommonUtils.webView = webView;
	}
	
	public static WebDriver getDriver() {
		return driver;
	}

	public static void startServer() throws Exception{
		String[] args = {
//				"-beta" , 
				"-aut", "/Users/maretskakirana/WesternUnion.app",
				"-port", "18001" };
		config = IOSServerConfiguration.create(args);
		server = new IOSServer(config);
		server.start();
	}
	
	public static void startServerSelendroid() throws Exception {
		SelendroidConfiguration config = new SelendroidConfiguration();
		// Need to copy apk to project
		config.addSupportedApp("src/main/resources/WU_R3_MOBILE_Release4000.apk");
		SelendroidLauncher selendroidserver = new SelendroidLauncher(config);
		selendroidserver.lauchSelendroid();
	}
	
	/** Decide Server to Start */
	public static RemoteWebDriver launchWuApp (String Platform, String nodeUrl) throws Exception {
		if (Platform.equalsIgnoreCase("ios")){
			IOSCapabilities westernUnion = IOSCapabilities.iphone("WesternUnion");
//			westernUnion.setCapability(IOSCapabilities.DEVICE, DeviceType.PHONE);
//			westernUnion.setCapability(IOSCapabilities.SIMULATOR, false);
//			westernUnion.setCapability(IOSCapabilities.LOCALE, "en_GB");
//			westernUnion.setCapability(CapabilityType.PLATFORM, "Mac");
//			westernUnion.setCapability(IOSCapabilities.UUID, "8f3f09c4bac73e6f14f1620cb6b5d83b67587e3b");
			driver = new RemoteWebDriver(new URL(nodeUrl), westernUnion);	
		}
		else if (Platform.equalsIgnoreCase("android")) {
			// Need to run selendroid server to capture activity name.
			SelendroidCapabilities caps = new SelendroidCapabilities("com.westernunion.wrapper:Build 4000");
			driver = new SelendroidDriver(caps);
		} 
		/** inialize ios webapp */
		else if (Platform.equalsIgnoreCase("ios-webapp")){
			IOSCapabilities westernUnion = IOSCapabilities.iphone("Safari");
			driver = new RemoteWebDriver(new URL(nodeUrl), westernUnion);
		} 
		/** inialize android webapp */
		else if (Platform.equalsIgnoreCase("android-webapp")) {
			driver = new RemoteWebDriver(DesiredCapabilities.iphone());
		}
		return driver;
	}
	
	public static void waitForText (int a) {
		for (int second = 0;; second++) {
			if (second >= 60);
			try {if (second >= a -1) break;} catch(Exception e){}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/** Decide Platform Test */
	public static RemoteWebDriver selectedPlatformServer(String Platform, String nodeUrl) throws Exception {
		if (Platform.equalsIgnoreCase("ios")){
			setNativeView("Native");
			setWebView("Web");
			startServer();
			driver = launchWuApp("ios", nodeUrl);
			// handled native proggressBar
			dynamicWaitWebview(driver, By.className("UIAProgressIndicator"), By.id("btn-main-login-mobile"));
			driver.switchTo().window("webview");
		}
		else if (Platform.equalsIgnoreCase("android")){
			setNativeView("NATIVE_APP");
			setWebView("WEBVIEW");
			startServerSelendroid();
			driver = launchWuApp("android", nodeUrl);
			// handled native proggressBar
			dynamicWaitWebview(driver, By.id("progressBar1"), By.id("btn-main-login-mobile"));
			driver.switchTo().window("WEBVIEW");
		}
		else if (Platform.equalsIgnoreCase("firefox")){
			capabilities = DesiredCapabilities.firefox();
//			capabilities.setBrowserName("firefox");
//			capabilities.setPlatform(org.openqa.selenium.Platform.ANY);
			driver = new RemoteWebDriver(new URL(nodeUrl), capabilities);
//			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		else if (Platform.equalsIgnoreCase("Safari")){
			capabilities = DesiredCapabilities.safari();
			capabilities.setBrowserName("safari");
			capabilities.setPlatform(org.openqa.selenium.Platform.MAC);
			driver = new RemoteWebDriver(new URL(nodeUrl), capabilities);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		return driver;
	}
	
	/** Waiting element between Native-view and Web-view */
	public static void dynamicWaitWebview(RemoteWebDriver driver, By lastElement, By expectedElement) throws Exception {
		System.out.println("Waiting Expected Element");
		while (ExpectedConditions.presenceOfElementLocated(lastElement) != null ){
			waitForText(3);
				try { driver.switchTo().window(getWebView());
					if (driver.findElement(expectedElement).isDisplayed()){
					System.out.print(".");
					break;}
				} catch (Exception e){}
				try {Thread.sleep(1000);
					System.out.print(".");
					driver.switchTo().window(getNativeView());
				}catch(InterruptedException e){
					File screenshotfile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(screenshotfile, new File("/Users/maretskakirana/Documents/Screenshot/"+ ERROR_SCREENSHOOT_PNG));
				e.printStackTrace();	
			}	
		}
	}
	
	public static void dynamicWait(RemoteWebDriver driver, By lastElement, By expectedElement) throws Exception{
		while (ExpectedConditions.presenceOfElementLocated(lastElement) != null ){
			waitForText(3);
				try { if (driver.findElement(expectedElement).isDisplayed()){
					System.out.print(".");
					break;}
				} catch (Exception e){}
				try {Thread.sleep(1000);
					System.out.println(".");
				}catch(InterruptedException e){
					File screenshotfile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(screenshotfile, new File("/Users/maretskakirana/Documents/Screenshot/"+ ERROR_SCREENSHOOT_PNG));
				e.printStackTrace();	
			}	
		}	
	}
	
	public static void datepicker(){
		
	}
}
