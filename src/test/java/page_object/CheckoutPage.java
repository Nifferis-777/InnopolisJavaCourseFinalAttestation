package page_object;
import configs.ConfigLoader;

import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckoutPage {

    private static final ConfigLoader configLoader = new ConfigLoader();
    public void fillCheckoutForm(String firstName, String lastName, String postalCode) {
        step("Ввести имя", () -> $("#first-name").setValue(firstName));
        step("Ввести фамилию", () -> $("#last-name").setValue(lastName));
        step("Ввести почтовый индекс", () -> $("#postal-code").setValue(postalCode));
        step("Нажать кнопку 'Continue'", () -> $("#continue").click());
    }

    public String getTotalAmount() {
        return step("Получить сумму добавленных в корзину товаров", () -> $(".summary_total_label").getText());
    }

    public void finishOrder() {
        step("Нажать кнопку 'Finish'", () -> $("#finish").click());
    }

    public void enterUserCredentials() {
        fillCheckoutForm(configLoader.getFirstName(), configLoader.getLastName(), configLoader.getPostalCode());
    }
}