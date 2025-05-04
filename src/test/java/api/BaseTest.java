//Базовый класс для тестов.
//Содержит логику авторизации и создания/удаления тестовой компании.

package api;

import configs.ApiConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import utils.CompanyApi;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected static String token; // Храним общий токен авторизации

    protected static int testCompanyId; // ID компании, созданной перед запуском тестов

    @BeforeAll
    public static void setUp() {
        token = getAuthToken(ApiConfig.USERNAME, ApiConfig.PASSWORD); // 1. Авторизуемся и получаем токен

        testCompanyId = CompanyApi.createCompany( // 2. Создаём тестовую компанию
            token,
            "MyTestCompany_" + System.currentTimeMillis(),
            "Just a test company"
        );
        System.out.println("==> Company created, id=" + testCompanyId);
    }

    @AfterAll
    public static void tearDown() {
        if (testCompanyId > 0) { // Удаляем созданную компанию по её ID
            CompanyApi.deleteCompany(token, testCompanyId);
            System.out.println("==> Company " + testCompanyId + " deleted");
        }
    }

    // Выполняет авторизацию и возвращает токен.
    // @param username логин
    // @param password пароль
    // @return строка с токеном

    private static String getAuthToken(String username, String password) {
        Response response =
            given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ContentType.JSON)
                .body(Map.of("username", username, "password", password))
            .when()
                .post("/auth/login")
            .then()
                .log().all()
                .statusCode(201) // Ожидаем успешную авторизацию
                .extract().response();
        return response.jsonPath().getString("userToken");  // Извлекаем токен из ответа
    }
}
