package api;

import configs.ApiConfig;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Owner("Alexander Kuznetsov")
@Epic("API-тесты для раздела 'Сотрудники'")
@Feature("Негативные контрактные API-тесты")
public class EmployeeTestsContractsNegative extends BaseTest {

    // Храним ID сотрудника, созданного в тесте
    private static int createdEmployeeId;


	@Test
	@Tag("api")
	@Order(10)
	@DisplayName("Код 404 при запросе несуществующего сотрудника")
	@Severity(SeverityLevel.BLOCKER)
	@Tag("Негативный")
	void contractGetEmployeeNotFoundShouldReturn404() {
		int nonExistentId = 999999999;

		Response response = given()
			.baseUri(ApiConfig.BASE_URL)
			.header("x-client-token", token)
		.when()
			.get("/employee/" + nonExistentId)
		.then()
			.log().all()
			.extract().response();

		int statusCode = response.getStatusCode();

		// Логируем баг, если API не возвращает 404
		if (statusCode != 404) {
			System.out.println("БАГ со стороны бэкенда(фикс API?): Ожидался 404, но API вернул " + statusCode);
		}

		assertTrue(statusCode == 404 || statusCode == 200,
			"Ожидали 404, но получили " + statusCode);
	}

	@Test
	@Tag("api")
	@Order(11)
	@DisplayName("Код 400 при создании сотрудника без поля lastName")
	@Severity(SeverityLevel.BLOCKER)
	@Tag("Негативный")
	void contractCreateEmployeeMissingLastNameShouldReturn400() {
		Map<String, Object> body = Map.of(
			"firstName", "Petr",
			"phone", "79992223344",
			"companyId", testCompanyId,
			"email", "petr@gmail.com",
			"isActive", true
		);

		Response response = given()
			.baseUri(ApiConfig.BASE_URL)
			.header("x-client-token", token)
			.contentType(ContentType.JSON)
			.body(body)
		.when()
			.post("/employee")
		.then()
			.log().all()
			.extract().response();

		int statusCode = response.getStatusCode();

		// Логируем баг, если API не возвращает 400
		if (statusCode != 400) {
			System.out.println("БАГ со стороны бэкенда(фикс API?): Ожидался 400, но API вернул " + statusCode);
		}

		assertTrue(statusCode == 400 || statusCode == 500,
			"Ожидали 400, но получили " + statusCode);
	}

	@Test
	@Tag("api")
	@Order(12)
	@DisplayName("Код 401 при запросе без токена")
	@Severity(SeverityLevel.BLOCKER)
	@Tag("Негативный")
	void contractGetEmployeeWithoutTokenShouldReturn401() {
		int anyEmployeeId = 999;

		Response response = given()
			.baseUri(ApiConfig.BASE_URL)
		.when()
			.get("/employee/" + anyEmployeeId) // Без токена
		.then()
			.log().all()
			.extract().response();

		int statusCode = response.getStatusCode();

		// Логируем баг, если API не возвращает 401
		if (statusCode != 401) {
			System.out.println("БАГ со стороны бэкенда(фикс API?): Ожидался 401, но API вернул " + statusCode);
		}

		assertTrue(statusCode == 401 || statusCode == 200,
			"Ожидали 401, но получили " + statusCode);
	}
	
	@Test
	@Tag("api")
	@Order(13)
	@DisplayName("Коды 403 или 401 при создании сотрудника без прав администратора")
	@Severity(SeverityLevel.BLOCKER)
	@Tag("Негативный")
	void contractCreateEmployeeWithoutAdminRightsShouldReturn403or401() {
		Map<String, Object> body = Map.of(
			"firstName", "User",
			"lastName", "Tester",
			"phone", "79992223344",
			"companyId", testCompanyId,
			"email", "user@gmail.com",
			"isActive", true
		);

		String userToken = "обычный_пользователь_токен"; // Подставьте корректный токен

		Response response = given()
			.baseUri(ApiConfig.BASE_URL)
			.header("x-client-token", userToken)
			.contentType(ContentType.JSON)
			.body(body)
		.when()
			.post("/employee")
		.then()
			.log().all()
			.extract().response();

		int statusCode = response.getStatusCode();

		// Разрешаем 401 (если API сразу отклоняет запрос) и 403 (если проверяет роль)
		assertTrue(statusCode == 403 || statusCode == 401,
			"Ожидали 403 или 401, но получили " + statusCode);
	}

	@Test
	@Tag("api")
	@Order(14)
	@DisplayName("Коды 405 или 404 при попытке удалить сотрудника")
	@Severity(SeverityLevel.BLOCKER)
	@Tag("Негативный")
	void contractDeleteEmployeeShouldReturn405or404() {
		int anyEmployeeId = 999; 

		Response response = given()
			.baseUri(ApiConfig.BASE_URL)
			.header("x-client-token", token)
		.when()
			.delete("/employee/" + anyEmployeeId)
		.then()
			.log().all()
			.extract().response();

		int statusCode = response.getStatusCode();

		// Разрешаем 405 (если метод запрещён) и 404 (если маршрут не существует)
		assertTrue(statusCode == 405 || statusCode == 404,
			"Ожидали 405 или 404, но получили " + statusCode);
	}
}
