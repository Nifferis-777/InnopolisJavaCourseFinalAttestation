package ui;

import configs.ConfigLoader;
import page_object.*;
import com.codeborne.selenide.junit5.BrowserPerTestStrategyExtension;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(BrowserPerTestStrategyExtension.class)

@Owner("Alexander Kuznetsov")
@Epic("Онлайн-магазин Swag Labs")
@Feature("Сценарий покупки товаров в магазине под разными профилями пользователей")
public class EndToEndTests extends LoginPage {

    private final LoginPage loginPage = new LoginPage();
    private final ProductsPage productsPage = new ProductsPage();
    private final CartPage cartPage = new CartPage();
    private final CheckoutPage checkoutPage = new CheckoutPage();
    private static final ConfigLoader configLoader = new ConfigLoader();
    private static final String EXPECTED_TOTAL_AMOUNT = "Total: $58.29";

    @ParameterizedTest
    @Tag("ui")
    @CsvSource({
            "standard_user, secret_sauce",
            "performance_glitch_user, secret_sauce"
    })
    @DisplayName("Покупка товаров в магазине под разными профилями пользователей")
    @Description("Открываем сайт, вводим логин и пароль от необходимого пользователя, добавляем 3 товара в корзину, " +
            "открываем корзину и проверяем что в корзину добавлено 3 товара," +
            "переходим на страницу с контактными данными - заполняем их, " +
            "проверяем,что сумма покупки $58.29, нажимаем кнопку завершения покупки")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")

    public void usersBuyProducts (String username, String password) {
        loginPage.login(username, password);
        productsPage.addProductsToCart();
        productsPage.goToCart();
        cartPage.CheckAddingThreeItemsToCart();
        cartPage.proceedToCheckout();
        checkoutPage.enterUserCredentials();
        String totalAmount = checkoutPage.getTotalAmount();
        step("Убедиться, что сумма за три, добавленных в корзину, товара совпадает с ожидаемой",
                () -> assertEquals(EXPECTED_TOTAL_AMOUNT, totalAmount));
        checkoutPage.finishOrder();
    }
}