package testRunners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		//features = {"src/test/resources/Appfeatures/OnboardingFlow.feature"},
		//features = {"src/test/resources/AppFeatures/Registration.feature"},
		features = {"src/test/resources/AppFeatures"},
		glue= {"stepdefinitions","appHooks"},
		plugin = {"pretty","com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"json:target/cucumber-json/CucumberTestReport.json",
				"rerun:target/failedScenarios.txt"
				})

public class MyTestRunners {
	
}
