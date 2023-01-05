package TestRunner;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features=".//Features/",
		glue="stepdefinitions",
		dryRun=false,
		monochrome=true,
		tags= {"@sanity"},
		plugin= {"pretty","html:Reports"}
		)

public class TestRun {

}
