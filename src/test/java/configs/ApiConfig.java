//Класс с конфигурационными настройками для тестов (подгружаем их из apitestsdata.properties).

package configs;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiConfig {

    private static final Properties props = new Properties();

    static {
        try (InputStream is = ApiConfig.class.getResourceAsStream("/apitestsdata.properties")) {  // Считываем файл apitestsdata.properties из ресурсов
            if (is == null) {
                throw new RuntimeException("Файл apitestsdata.properties не найден в classpath");
            }
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения apitestsdata.properties", e);
        }
    }

    public static final String BASE_URL  = props.getProperty("base.url"); // Достаём нужные поля из properties
    public static final String USERNAME  = props.getProperty("username");
    public static final String PASSWORD  = props.getProperty("password");

}
