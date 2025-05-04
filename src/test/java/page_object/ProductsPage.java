package page_object;
import configs.ConfigLoader;
import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.qameta.allure.Allure.step;

public class ProductsPage {

    private static final ConfigLoader configLoader = new ConfigLoader();
    public void addToCart(String productName) {
        $$(".inventory_item")
                .findBy(text(productName))
                .$("button.btn_inventory")
                .click();
    }

    public void goToCart() {

        step("Перейти в корзину", () -> $("#shopping_cart_container").click());
    }

    public void addProductsToCart() {
        step("Добавить в корзину первый товар", () ->  addToCart(configLoader.getProductOne()));
        step("Добавить в корзину второй товар", () -> addToCart(configLoader.getProductTwo()));
        step("Добавить в корзину третий товар", () -> addToCart(configLoader.getProductThree()));
    }

    public void checkCartDisplay() {
        step("Проверить, что на экране отображается корзина", () ->
                $(".shopping_cart_link").shouldHave(Condition.visible)
        );
    }
}