package configs;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;

public class SelenideConfig {

    private final ConfigLoader configLoader;

    public SelenideConfig() {
        this.configLoader = new ConfigLoader();
    }

    public void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.browser = "chrome";
        Configuration.baseUrl = configLoader.getBaseUrl();
        Configuration.pageLoadStrategy = "normal";
        Configuration.timeout = 20000;
        Configuration.headless = true;
    }
}