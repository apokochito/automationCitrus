package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-reports","json:target/cucumber.json"},
        glue = {"com.consol.citrus.cucumber.step.designer"},
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        tags = {"@General"}
)
public class Runner {
}
