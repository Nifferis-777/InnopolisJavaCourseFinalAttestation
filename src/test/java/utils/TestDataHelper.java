// Вспомогательный класс, который может пригодиться для получения тестовых данных
//(например, ID уже существующей компании).

package utils;

import configs.ApiConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
public class TestDataHelper {

    /**
     * Возвращает ID первой попавшейся компании из списка /company.
     * Если компаний нет, тест может упасть, но обычно там есть несколько дефолтных.
     * @return ID первой найденной компании
     */

    public static int getAnyExistingCompanyId() {
        Response response = 
            given()
                .baseUri(ApiConfig.BASE_URL)
            .when()
                .get("/company")
            .then()
                .statusCode(200)
                .extract().response();
        return response.jsonPath().getInt("[0].id"); // Извлекаем ID из первого элемента массива JSON
    }
}
