package page_object;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartPage {
    public int getItemCount() {
        return $$(".cart_item").size();
    }

    public void proceedToCheckout() {
        step("Нажать на кнопку Checkout (Переход к вводу контактных данных)", () ->$("#checkout").click());
    }

    public void CheckAddingThreeItemsToCart() {
        step("Убедиться, что в корзину добавлено три товара", () -> assertEquals(3, getItemCount()));
    }


}