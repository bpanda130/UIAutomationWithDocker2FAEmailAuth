package com.qa.factory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import com.qa.util.Constants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v95.log.Log;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.qa.util.Browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {

	public WebDriver driver;
	public static String highlight = "true";
	private static Browser browserName;
	private OptionsManager optionsManager;
	public static String remoteExecution;

//	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

	//public static ThreadLocal<RemoteWebDriver> remoteDriver = new ThreadLocal<RemoteWebDriver>();
	public CapabilityFactory capabilityFactory = new CapabilityFactory();
	public static ThreadLocal remoteDriver;

	/**
	 * This method is used to initialize the threadlocal driver on the basis of
	 * given browser
	 * 
	 * @param broserName
	 * @return this will return tlDriver
	 */
	public WebDriver init_driver(Properties prop, Browser broserName) throws MalformedURLException {
		highlight = prop.getProperty("highlight");
		remoteExecution = prop.getProperty("remote");
		optionsManager = new OptionsManager(prop);

		switch (broserName) {
		case FIREFOX:
			System.out.println("Required Broowser : Firefox");
			if(remoteExecution.equalsIgnoreCase("True")) {
				remoteDriver = new ThreadLocal<RemoteWebDriver>();
				remoteDriver.set(new RemoteWebDriver(new URL(Constants.WEB_CONFIG.REMOTE_CHROME_URL), capabilityFactory.getCapabilities("firefox")));
				break;
			}
			else {
				remoteDriver = new ThreadLocal<WebDriver>();
				WebDriverManager.firefoxdriver().setup();
				remoteDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
			}

		case CHROME:
			System.out.println("Inside Chrome");
			if(remoteExecution.equalsIgnoreCase("True")) {
				remoteDriver = new ThreadLocal<RemoteWebDriver>();
				remoteDriver.set(new RemoteWebDriver(new URL(Constants.WEB_CONFIG.REMOTE_CHROME_URL), capabilityFactory.getCapabilities("chrome")));
				break;
			}else {
				remoteDriver = new ThreadLocal<WebDriver>();
				WebDriverManager.chromedriver().setup();
				remoteDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));

			}

		}
		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		return getDriver();
	}

	/**
	 * this is used to get the driver with threadLocal
	 * 
	 * @return
	 */
	public static synchronized WebDriver getDriver() {
		return (WebDriver) remoteDriver.get();
	}

}
