package runner;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",
        plugin = {"pretty", "html:target/cucumber-reports","json:target/cucumber.json"},
        glue = {"com.consol.citrus.cucumber.step.designer"},
        snippets = SnippetType.CAMELCASE,
        tags = {"@Test"}
)
public class Runner {
}
