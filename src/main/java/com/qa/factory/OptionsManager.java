package com.qa.factory;

import java.util.Properties;
import java.util.logging.Level;

import com.qa.util.Constants;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class OptionsManager implements Constants {
	
	private Properties prop;
	private ChromeOptions co;
	private FirefoxOptions fo;
	
	public OptionsManager(Properties prop) {
		this.prop = prop;
	}

	public static ChromeOptions getChromeOptions() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments(WEB_CONFIG.WINDOW_START_STATE);
		options.addArguments(CHROME_CONFIG.SANDBOX_FLAG);
		//options.addArguments(WEB_CONFIG.EXECUTION_MODE);
		options.addArguments(CHROME_CONFIG.CERTIFICATE_ERROR);
		options.addArguments(CHROME_CONFIG.POP_UP_DISABLE);
		options.addArguments(CHROME_CONFIG.WINDOW_SIZE);
		options.addArguments(CHROME_CONFIG.NOTIFICATION_DISABLE);
		return options;

	}

	public static FirefoxOptions getFirefoxOptions() {
		FirefoxOptions options = new FirefoxOptions();
		FirefoxProfile profile = new FirefoxProfile();
		profile.setAcceptUntrustedCertificates(FIREFOX_CONFIG.ACCEPT_FLAG);
		options.setHeadless(FIREFOX_CONFIG.ACCEPT_FLAG);
		options.addArguments(WEB_CONFIG.WINDOW_START_STATE);
		profile.setAssumeUntrustedCertificateIssuer(FIREFOX_CONFIG.DECLINE_FLAG);
		options.addArguments(FIREFOX_CONFIG.WINDOW_WIDTH);
		options.addArguments(FIREFOX_CONFIG.WINDOW_HEIGHT);
		profile.setPreference(FIREFOX_CONFIG.NETWORK_PROXY, 0);
		options.setCapability(FirefoxDriver.PROFILE, profile);
		return options;
	}

}
