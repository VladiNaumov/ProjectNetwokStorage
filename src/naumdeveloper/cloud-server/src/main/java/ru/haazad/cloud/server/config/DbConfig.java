public class DbConfig {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public DbConfig() {
        dbUrl = ConfigProperty.getProperties("db.url");
        dbUser = ConfigProperty.getProperties("db.user");
        dbPassword = ConfigProperty.getProperties("db.password");
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
