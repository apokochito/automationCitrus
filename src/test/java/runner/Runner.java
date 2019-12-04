package runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-reports","json:target/cucumber.json"},
        glue = {"com.consol.citrus.cucumber.step.designer"},
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        tags = {"@Test"}
)
public class Runner {
}
