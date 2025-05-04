package ui;

import page_object.LoginPage;
import configs.ConfigLoader;
import page_object.ProductsPage;
import com.codeborne.selenide.junit5.BrowserPerTestStrategyExtension;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserPerTestStrategyExtension.class)

@Owner("Alexander Kuznetsov")
@Epic("Онлайн-магазин Swag Labs")
@Feature("Авторизация на сайте под разными пользователями")
public class AuthorizationTests extends LoginPage {

    private final LoginPage loginPage = new LoginPage();
    private final ProductsPage productsPage = new ProductsPage();
    private final ConfigLoader configLoader = new ConfigLoader();


    @Test
    @Tag("ui")
    @DisplayName("Успешная авторизация под standard_user")
    @Description("Открываем сайт, вводим логин и пароль от standard_user, нажимаем кнопку входа, попадаем на страницу с товарами")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    public void successAuthStandardUser() {
        String username = configLoader.getStandardUserLogin();
        String password = configLoader.getPassword();
        loginPage.enterLoginAndPassword(username, password);
        loginPage.clickLoginButton();
        productsPage.checkCartDisplay();
    }

    @Test
    @Tag("ui")
    @DisplayName("Авторизация заблокированного пользователя (locked_out_user)")
    @Description("Открываем сайт, вводим логин и пароль от locked_out_user, нажимаем кнопку входа, получаем предупреждение-ошибку входа, так как пользователь заблокирован")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    public void authLockedOutUser() {
        String username = configLoader.getLockedOutUserLogin();
        String password = configLoader.getPassword();
        loginPage.enterLoginAndPassword(username, password);
        loginPage.clickLoginButton();
        loginPage.checkAuthErrorDisplay();
    }
}
