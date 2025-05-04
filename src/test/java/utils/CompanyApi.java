// Класс с методами для работы с эндпоинтами, связанными с компаниями


package utils;

import configs.ApiConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CompanyApi {

    /**
     * Создаёт новую компанию и возвращает её ID.
     * @param token    авторизационный токен
     * @param name     название компании
     * @param description описание компании
     * @return ID созданной компании
     */

    public static int createCompany(String token, String name, String description) {
        Response response =
            given()
                .baseUri(ApiConfig.BASE_URL)
                .header("x-client-token", token) // Обязательный заголовок
                .contentType(ContentType.JSON)
                .body(Map.of(
                    "name", name,
                    "description", description
                ))
            .when()
                .post("/company")
            .then()
                .log().all()
                .statusCode(201)
                .body("id", notNullValue()) // Проверяем, что ID не null
                .extract().response();

        // Возвращаем ID компании из тела ответа
        return response.jsonPath().getInt("id");
    }

    /**
     * Удаляет компанию по её ID.
     * @param token     авторизационный токен
     * @param companyId ID удаляемой компании
     */

    public static void deleteCompany(String token, int companyId) {
        given()
            .baseUri(ApiConfig.BASE_URL)
            .header("x-client-token", token)
        .when()
            .get("/company/delete/" + companyId)
        .then()
            .log().all()
            .statusCode(200);
    }
}
