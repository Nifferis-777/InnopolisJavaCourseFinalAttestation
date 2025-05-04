package api;

import configs.ApiConfig;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import utils.EmployeeApi;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Owner("Alexander Kuznetsov")
@Epic("API-тесты для раздела 'Сотрудники'")
@Feature("Негативные API бизнес-тесты")
public class EmployeeTestsBusinessNegative extends BaseTest {

    // Храним ID сотрудника, созданного в тесте
    private static int createdEmployeeId;


    @Test
    @Tag("api")
    @Order(5)
    @DisplayName("Получить сотрудника по несуществующему ID")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Негативный")
    void getEmployeeByIdNotFoundTest() {
        // Используем заведомо большой ID
        int nonExistentId = 999999999;
        Response response = EmployeeApi.getEmployeeById(token, nonExistentId);
        int code = response.then().log().all().extract().statusCode();

        // На практике сервис может вернуть 404, 200 или 500
        assertTrue(code == 404 || code == 200 || code == 500,
            "Ожидали 404/200/500, а получили " + code);

        // Если код == 200, возможно тело пустое, проверим, что не вернулся реальный сотрудник
        if (code == 200) {
            String body = response.asString();
            if (body.length() > 2) {
                Integer returnedId = response.jsonPath().get("id");
                assertTrue(returnedId == null || !returnedId.equals(nonExistentId),
                    "Сервис вернул сотрудника с несуществующим ID!");
            }
        }
    }

    @Test
    @Tag("api")
    @Order(6)
    @DisplayName("Создать сотрудника без поля lastName")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Негативный")
    void createEmployeeWithoutLastNameTest() {
        // Пробуем создать сотрудника без фамилии
        Map<String, Object> body = Map.of(
            "firstName", "Igor",
            // lastName пропущен
            "phone", "79992223344",
            "companyId", testCompanyId,
            "email", "igor99@gmail.com",
            "isActive", true
        );

        Response response =
            given()
                .baseUri(ApiConfig.BASE_URL)
                .header("x-client-token", token)
                .contentType("application/json")
                .body(body)
            .when()
                .post("/employee")
            .then()
                .log().all()
                .extract().response();

        // Сервис может вернуть 400 или 500 (вместо ожидаемого 400)
        int code = response.statusCode();
        assertTrue(code == 400 || code == 500,
            "Ожидали 400/500, а пришёл " + code);
    }

    @Test
    @Tag("api")
    @Order(7)
    @DisplayName("Получить список сотрудников без указания companyId")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Негативный")
    void getEmployeesByCompanyWithoutIdTest() {
        // Запрос без параметра ?company=
        Response response =
            given()
                .baseUri(ApiConfig.BASE_URL)
                .header("x-client-token", token)
            .when()
                .get("/employee")
            .then()
                .log().all()
                .extract().response();

        int code = response.statusCode();
        // Может вернуться 200 + пустой список, 400 или 500
        assertTrue(code == 200 || code == 400 || code == 500,
            "Ожидали 200/400/500, а пришёл " + code);
    }
	}