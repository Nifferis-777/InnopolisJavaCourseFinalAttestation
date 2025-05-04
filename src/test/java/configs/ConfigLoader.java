package configs;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

        private final Properties properties;

        public ConfigLoader() {
            properties = new Properties();
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("uitestsdata.properties")) {
                if (input == null) {
                    System.out.println("Не удалось найти uitestsdata.properties");
                    return;
                }
                properties.load(input);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public String getBaseUrl() {
            return properties.getProperty("BaseUrl");
        }
        public String getStandardUserLogin() {
            return properties.getProperty("StandardUserLogin");
        }
        public String getLockedOutUserLogin() { return properties.getProperty("LockedOutUserLogin");}
        public String getPassword() {
            return properties.getProperty("Password");
        }
        public String getFirstName() {
        return properties.getProperty("Firstname");
    }
        public String getLastName() {
        return properties.getProperty("Lastname");
    }
        public String getPostalCode() {
        return properties.getProperty("Postalcode");
    }
        public String getProductOne() {
        return properties.getProperty("ProductOne");
    }
        public String getProductTwo() {
        return properties.getProperty("ProductTwo");
    }
        public String getProductThree() {
        return properties.getProperty("ProductThree");
    }
    }

