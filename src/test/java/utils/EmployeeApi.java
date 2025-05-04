package utils;

import configs.ApiConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

/**
 * Методы для работы с эндпоинтами, связанными с сотрудниками.
 */
public class EmployeeApi {

    /**
     * Создаёт сотрудника в компании.
     * Возвращает ID созданного сотрудника (из тела ответа {"id": ...}).
     *
     * @param token      авторизационный токен
     * @param companyId  ID компании
     * @param firstName  имя
     * @param lastName   фамилия
     * @param middleName отчество/доп. поле
     * @param email      email сотрудника
     * @param phone      телефон
     * @param url        ссылка (например, на аватар)
     * @param birthdate  дата рождения
     * @param isActive   активен ли сотрудник
     * @return ID созданного сотрудника
     */
    public static int createEmployee(
            String token,
            int companyId,
            String firstName,
            String lastName,
            String middleName,
            String email,
            String phone,
            String url,
            String birthdate,
            boolean isActive
    ) {
        // Формируем тело запроса без поля "id", чтобы избежать ошибок сервиса
        Map<String, Object> body = Map.of(
            "firstName", firstName,
            "middleName", middleName,
            "lastName", lastName,
            "email", email,
            "phone", phone,
            "url", url,
            "birthdate", birthdate,
            "companyId", companyId,
            "isActive", isActive
        );

        // Отправляем POST-запрос
        Response response =
            given()
                .baseUri(ApiConfig.BASE_URL)
                .header("x-client-token", token)
                .contentType(ContentType.JSON)
                .body(body)
            .when()
                .post("/employee")
            .then()
                .log().all()
                // Сервис может возвращать 200 или 201 при успехе
                .statusCode(anyOf(is(200), is(201)))
                .extract().response();

        // Возвращаем ID из ответа {"id": ... }
        return response.jsonPath().getInt("id");
    }

    /**
     * Возвращает список сотрудников для указанной компании.
     * @param token     авторизационный токен
     * @param companyId ID компании
     * @return Response (для дальнейших проверок)
     */
    public static Response getEmployeesByCompany(String token, int companyId) {
        return given()
            .baseUri(ApiConfig.BASE_URL)
            .header("x-client-token", token)
            .when()
            .get("/employee?company=" + companyId);
    }

    /**
     * Получить данные сотрудника по его ID.
     * @param token      авторизационный токен
     * @param employeeId ID сотрудника
     * @return Response (для дальнейших проверок)
     */
    public static Response getEmployeeById(String token, int employeeId) {
        return given()
            .baseUri(ApiConfig.BASE_URL)
            .header("x-client-token", token)
            .when()
            .get("/employee/" + employeeId);
    }

    /**
     * Обновляет информацию о сотруднике методом PATCH.
     * Если в карте полей присутствует "avatar_url", переименовываем его в "url" 
     * (так как сервис может ожидать ключ "url").
     *
     * @param token        авторизационный токен
     * @param employeeId   ID сотрудника
     * @param updateFields карта полей для обновления
     * @return Response (тело ответа сервера)
     */
    public static Response updateEmployee(String token, int employeeId, Map<String, Object> updateFields) {
        // Если вместо "url" пришёл ключ "avatar_url", переименуем его в "url"
        if (updateFields.containsKey("avatar_url")) {
            Object avatarUrl = updateFields.get("avatar_url");
            Map<String, Object> updatedMap = new HashMap<>(updateFields);
            updatedMap.remove("avatar_url");
            updatedMap.put("url", avatarUrl);
            updateFields = updatedMap;
        }

        return given()
            .baseUri(ApiConfig.BASE_URL)
            .header("x-client-token", token)
            .contentType(ContentType.JSON)
            .body(updateFields)
            .when()
            .patch("/employee/" + employeeId);
    }
}
