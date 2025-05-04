package api;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import utils.EmployeeApi;

import java.util.Map;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Owner("Alexander Kuznetsov")
@Epic("API-тесты для раздела 'Сотрудники'")
@Feature("Позитивные API бизнес-тесты")
public class EmployeeTestsBusinessPositive extends BaseTest {

	// Храним ID сотрудника, созданного в тесте
	private static int createdEmployeeId;

	@Test
	@Tag("api")
	@Order(1)
	@DisplayName("Создать сотрудника")
	@Severity(SeverityLevel.BLOCKER)
	@Tag("Позитивный")
	void createEmployeePositiveTest() {
		// Создаём нового сотрудника
		step("Создаем нового сотрудника, заполняем данные", () ->
		createdEmployeeId = EmployeeApi.createEmployee(
				token,
				testCompanyId,
				"Ivan",
				"Petrov",
				"Ivanovich",
				"ivanpetrov@gmail.com",
				"79998887766",
				"http://photostock.url/pic01.png",
				"2000-05-03T00:00:00Z",
				true
		));

		System.out.println("Created employeeId=" + createdEmployeeId);
		step("Проверяем, что сотрудник создан ", () ->
				assertThat(
				"Сотрудник не создался (ID <= 0)",
				createdEmployeeId,
				greaterThan(0)
		));
	}

	@Test
	@Tag("api")
	@Order(2)
	@DisplayName("Получить список сотрудников по companyId")
	@Severity(SeverityLevel.BLOCKER)
	@Tag("Позитивный")
	void getEmployeesByCompanyPositiveTest() {
		// Запрашиваем список сотрудников для созданной компании
		step("Запрашиваем список сотрудников по companyId", () -> {
			Response response = EmployeeApi.getEmployeesByCompany(token, testCompanyId);
			response.then().log().all().statusCode(200);

			step("Проверяем, что список сотрудников не пустой", () -> {
				var employees = response.jsonPath().getList("$");
				assertTrue(employees.size() > 0, "Список сотрудников пуст!");
			});
		});
	}

	@Test
	@Tag("api")
	@Order(3)
	@DisplayName("Получить сотрудника по ID")
	@Severity(SeverityLevel.BLOCKER)
	@Tag("Позитивный")
	void getEmployeeByIdPositiveTest() {
		// Запрашиваем данные по конкретному сотруднику
		step("Запрашиваем данные сотрудника по ID", () -> {
			Response response = EmployeeApi.getEmployeeById(token, createdEmployeeId);
			response.then().log().all().statusCode(200);

			step("Проверяем, что тело ответа не пустое", () -> {
				String body = response.asString();
				assertTrue(body.length() > 2, "Тело ответа пустое!");

				// Проверяем корректность некоторых полей
				String firstName = response.jsonPath().getString("firstName");
				String lastName = response.jsonPath().getString("lastName");
				assertThat(firstName, equalTo("Ivan"));
				assertThat(lastName, equalTo("Petrov"));
			});
		});
	}

	@Test
	@Tag("api")
	@Order(4)
	@DisplayName("Изменить информацию о сотруднике (PATCH)")
	@Severity(SeverityLevel.BLOCKER)
	@Tag("Позитивный")
	void updateEmployeePositiveTest() {
		// Формируем карту полей, которые нужно обновить
		Map<String, Object> updatedFields = Map.of(
				"firstName", "Ivan",
				"middleName", "Ivanovich",
				"lastName", "Bodrov", // Изменяем фамилию
				"email", "ivanpetrov@gmail.com",
				"phone", "79998887766",
				// Используем "avatar_url" вместо "url", чтобы протестировать логику замены ключа
				"avatar_url", "http://photostock.url/pic01.png",
				// Дату рождения передаём в упрощённом формате
				"birthdate", "2000-05-03",
				"companyId", testCompanyId,
				// Меняем активность на false
				"isActive", false
		);

		// Шаг 1: Отправляем PATCH-запрос
		step("Отправляем PATCH-запрос для обновления информации о сотруднике", () -> {
			Response patchResponse = EmployeeApi.updateEmployee(token, createdEmployeeId, updatedFields);
			patchResponse.then().log().all().statusCode(anyOf(is(200), is(201)));
		});

		// Шаг 2: Делаем GET-запрос, чтобы проверить, что данные обновились
		step("Делаем GET-запрос, чтобы проверить, что данные обновились", () -> {
			Response getResponse = EmployeeApi.getEmployeeById(token, createdEmployeeId);
			getResponse.then().log().all().statusCode(200);

			// Шаг 3: Проверяем, что в ответе содержатся обновлённые поля
			step("Проверяем, что в ответе содержатся обновлённые поля", () -> {
				String lastName = getResponse.jsonPath().getString("lastName");
				boolean isActive = getResponse.jsonPath().getBoolean("isActive");

				assertThat(lastName, equalTo("Bodrov"));
				assertThat(isActive, is(false));
			});
		});
	}

}

