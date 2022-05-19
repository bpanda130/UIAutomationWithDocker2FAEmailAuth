package com.qa.pages;

import java.util.HashMap;
import java.util.Properties;

import com.qa.util.*;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {
	
	private WebDriver driver;
	private final ConfigReader configReader;
	Properties prop;
	ElementUtil elementUtil;
	AuthenticationUtility authUtility;
	CSVFileUtility csvFileUtility;
	JavaScriptUtil javaScriptUtil;

	private final By homePageTitle = By.xpath("//span[text()=' Welcome to Dealcierge ']");
	private final By buyer_registnbtn = By.xpath("//p[contains(text(),'Are you a buyer')]/following-sibling::a");
	private final By buyer_registnbtn1 = By.xpath("//div[@class='btn-card-container']//app-base-button//span[text()='I´m looking to BUY']");
	private final By seller_registration = By.xpath("//p[contains(text(),'Are you a seller')]/following-sibling::a");
	private final By buyer_StartBtn = By.xpath("//button/span[text()='START']");
	private final By corporateEmail_input = By.xpath("//span[label[text()='Corporate email']]/input");
	private final By password_input = By.xpath("//span[label[text()='Password']]//input");
	private final By confirmpswd_input = By.xpath("//span[label[text()='Repeat Password']]//input");
	private final By agreedCheckBox = By.xpath("//label[text()='Agree with']/preceding-sibling::div//div[@class='p-checkbox-box']");
	private final By nextBtn = By.xpath("//button[span[text()='NEXT']]");
	private final By introduction_code = By.xpath("//span[label[text()='Verification Code']]//input");
	private final By createActBtn = By.xpath("//button[span[text()='CREATE ACCOUNT']]");
	private final By userSecretCode = By.xpath("//div[form[div[@class='grid']]]/span");
	private final By userRegistrationMsg = By.xpath("//div[p[text()=' REGISTER YOURSELF ']]/span");
	private final By authPageMessage = By.xpath("//div[div[p[text()=' REGISTER YOURSELF ']]/span]/following-sibling::div//p");
	private final By login_link = By.xpath("//a[text()='Login']");
	private final By termsOfUse_Dialog = By.xpath("//div[@role='dialog']");
	private final By termsOfUse_lastelement = By.xpath("//div[@id='content-term-of-use']/*[last()]");
	private final By privacyPolicy_lastelement = By.xpath("//div[@id='content-privacy-policy']/*[last()]");
	private final By IAgree_Btn = By.xpath("//button[span[text()='I Agree']]");

	//Constructor
		public HomePage(WebDriver driver) {
			//this.driver = driver;
			elementUtil = new ElementUtil(driver);
			configReader = new ConfigReader();
			authUtility = new AuthenticationUtility();
			csvFileUtility = new CSVFileUtility();
			javaScriptUtil = new JavaScriptUtil(driver);
		}
		
		//page Action
		public void lauchURL(String role) {
			prop = configReader.init_prop();
			if(role.equalsIgnoreCase("User"))
				elementUtil.launchURL(prop.getProperty("url"));
			else
				elementUtil.launchURL(prop.getProperty("adminUrl"));
		}

		public boolean verifyHomePageDetails() {
			if(elementUtil.getDisplayedStatus(homePageTitle) &&
					elementUtil.getDisplayedStatus(buyer_registnbtn) &&
					elementUtil.getDisplayedStatus(seller_registration)) {
				return true;
			}
			else
				return false;
		}

		public void clickOnLogin() {
			elementUtil.doClick(login_link);
		}
		
		public void clickOnBuyerRegistration(String user_role){
			if(user_role.equalsIgnoreCase("Buyer")) {
				WebElement buyer_regbtn = elementUtil.waitForElementToBeClickable(buyer_registnbtn, 20);
				javaScriptUtil.clickElementByJS(buyer_regbtn);
				//elementUtil.doClick(buyer_registnbtn);
//				elementUtil.clickWhenReady(buyer_registnbtn, 30);
			}
			else {
				WebElement seller_regBtn = elementUtil.waitForElementToBeClickable(seller_registration, 20);
				javaScriptUtil.clickElementByJS(seller_regBtn);
				//elementUtil.doClick(seller_registration);
			}
		}

		public String verifyRegisterPageDetails(){
			return elementUtil.doGetText(userRegistrationMsg);
		}

		public void clickOnStartButton(){
			elementUtil.doClick(buyer_StartBtn);
		}

		public void enterRegistrationDetails(String username, String password) {
			elementUtil.doSendKeys(corporateEmail_input, username);
			elementUtil.doSendKeys(password_input, password);
			elementUtil.doSendKeys(confirmpswd_input, password);
		}

		public void selectPrivacyPolicy() {
			//elementUtil.doClick(agreedCheckBox);
			WebElement policyCheckbox = elementUtil.waitForElementToBeClickable(agreedCheckBox,20);
			javaScriptUtil.clickElementByJS(policyCheckbox);

			// Wait for modal
			elementUtil.waitForElementVisible(termsOfUse_Dialog, 20);
			// Select last child of content div
			WebElement element_termsOfUse_lastelement = elementUtil.waitForElementVisible(termsOfUse_lastelement, 20);
			// Scroll to last child node
			javaScriptUtil.scrollIntoView(element_termsOfUse_lastelement);
			elementUtil.doClick(IAgree_Btn);

			// Wait for modal
			elementUtil.waitForElementVisible(termsOfUse_Dialog, 20);
			// Select last child of content div
			WebElement element_privacyPolicy_lastelement = elementUtil.waitForElementVisible(privacyPolicy_lastelement, 20);
			// Scroll to last child node
			javaScriptUtil.scrollIntoView(element_privacyPolicy_lastelement);
			elementUtil.doClick(IAgree_Btn);
		}

		public void clickOnNextBtn() {
			elementUtil.doClick(nextBtn);
		}

		public String getRegistrationSuccessMessage() {
			elementUtil.waitForElementVisible(introduction_code, 30);
			return elementUtil.doGetText(authPageMessage);
		}

		public void enterEmailAuthCode(String user, String role){
			try{
				Thread.sleep(3000);
				HashMap<String, String> hm = authUtility.getGmailData("Your verification code", user, role);
				System.out.println(hm.get("body"));
				String mbody = hm.get("body");
				String vCode = StringUtils.substringAfter(mbody, "Your confirmation code is").trim();
				elementUtil.doSendKeys(introduction_code,vCode);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}

		public void verifyQRCodeOnScreenAndEnter(String user) {
			try {
				String secretCode = elementUtil.doGetAttribute(userSecretCode, "textContent");
				//System.out.println(secretCode);
				csvFileUtility.addUserTokenDetailsToFile(user, secretCode);
				elementUtil.doSendKeys(introduction_code,authUtility.getTwoFactorCode(secretCode));
				GlobalData.scenarioData.put("Registration","true");
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		public void clickOnAcreatAccount() {
			elementUtil.doClick(createActBtn);
		}


}
