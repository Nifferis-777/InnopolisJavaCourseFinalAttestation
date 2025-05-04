package page_object;
import configs.ConfigLoader;
import com.codeborne.selenide.Condition;
import configs.SelenideConfig;
import org.junit.jupiter.api.BeforeEach;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;


public class LoginPage {

    private static final ConfigLoader configLoader = new ConfigLoader();
    private static final SelenideConfig selenideConfig = new SelenideConfig();

    @BeforeEach
    public void setUp() {
        selenideConfig.setUp();
        step("Открыть страницу авторизации", () -> open(configLoader.getBaseUrl()));
    }

    public void login(String username, String password) {
        enterLoginAndPassword(username, password);
        step("Нажать на кнопку входа", this::clickLoginButton);
    }

    public void enterLoginAndPassword(String username, String password) {
        step("Ввести корректное значение в поле логина", () -> $("#user-name").setValue(username));
        step("Ввести корректное значение в поле пароля", () -> $("#password").setValue(password));
    }

    public void clickLoginButton() {
        step("Нажать на кнопку входа", () -> $("#login-button").click());
    }


    public void checkAuthErrorDisplay() {
        step("Проверить, что на экране отображается предупреждение-ошибка об авторизации", () ->
                $x("//h3").shouldHave(Condition.visible)
        );
    }
}

