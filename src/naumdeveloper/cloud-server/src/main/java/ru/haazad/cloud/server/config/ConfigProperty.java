import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperty {
    private static final Properties properties = new Properties();

    public static String getProperties(String propertyName) {
        try (InputStream in = ConfigProperty.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(in);
            return properties.getProperty(propertyName);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unknown property " + propertyName);
        }
    }

    public static String getServerPort() {
        return getProperties("server.port");
    }

    public static String getStorage() {
        return getProperties("server.storage.directory");
    }

}
