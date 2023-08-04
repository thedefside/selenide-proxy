import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getSelenideProxy;
import static com.codeborne.selenide.proxy.RequestMatcher.HttpMethod.GET;
import static com.codeborne.selenide.proxy.RequestMatchers.urlContains;

public class ProxyTest {

    @BeforeClass
    public void setup() {
        Configuration.proxyEnabled = true;

        open();

        getSelenideProxy().responseMocker().mockText("activities-mock",
                urlContains(GET, "/Activities"), 403, () -> "[]");

    }

    @Test
    public void mockTest() {

        open("https://fakerestapi.azurewebsites.net/index.html");

        var activities = $("#operations-Activities-get_api_v1_Activities").click(ClickOptions.usingDefaultMethod());

        activities.$("button.try-out__btn").click();
        activities.$("button.execute").click();

        activities.$("tbody  td.response-col_status", 0).shouldHave(Condition.text("403"));
    }
}
