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

    public static String getServerHost() {
        return getProperties("server.host");
    }

    public static int getServerPort() {
        return Integer.parseInt(getProperties("server.port"));
    }

    public static String getLocalStorage() {
        return getProperties("src.directory");
    }

}
