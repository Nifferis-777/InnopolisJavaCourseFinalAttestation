package api;

import configs.ApiConfig;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import utils.EmployeeApi;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Owner("Alexander Kuznetsov")
@Epic("API-тесты для раздела 'Сотрудники'")
@Feature("Позитивные контрактные API-тесты")
public class EmployeeTestsContractsPositive extends BaseTest {

    // Храним ID сотрудника, созданного в тесте
    private static int createdEmployeeId;

	
	@Test
    @Tag("api")
    @Order(8)
    @DisplayName("Создание сотрудника должно вернуть код 201")
    void contractCreateEmployeeShouldReturn201() {
        // Согласно "строгому" контракту, при успешном создании сервис возвращает 201
        Map<String, Object> body = Map.of(
                "firstName", "Contract",
                "lastName",  "Test",
                "middleName","API",
                "email",     "test.contract@example.com",
                "phone",     "79998881122",
                "url",       "http://some.url/test.png",
                "birthdate", "1990-01-01T00:00:00Z",
                "companyId", testCompanyId,
                "isActive",  true
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
                        // Здесь ЖЁСТКО ожидаем 201
                        .statusCode(201)
                        .body("id", notNullValue())
                        .extract().response();

        int newEmployeeId = response.jsonPath().getInt("id");
        System.out.println("Contract check: created employeeId = " + newEmployeeId);
    }

    @Test
    @Tag("api")
    @Order(9)
    @DisplayName("Код 200 при получении существующего сотрудника")
    void contractGetEmployeeShouldReturn200() {
        // Создадим сотрудника заранее, чтобы было, кого получать
        int tempId = EmployeeApi.createEmployee(
                token,
                testCompanyId,
                "Petr",
                "Petrov",
                "API2",
                "petrov@gmail.com",
                "79991112233",
                "http://photo.url/pic02.png",
                "1995-07-10T00:00:00Z",
                true
        );

        // Теперь проверим, что при запросе этого ID сервис строго возвращает 200
        given()
            .baseUri(ApiConfig.BASE_URL)
            .header("x-client-token", token)
        .when()
            .get("/employee/" + tempId)
        .then()
            .log().all()
            .statusCode(200) // Строго 200
            .body("id", equalTo(tempId));
    }

}
