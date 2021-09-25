import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;

public class FlywayConfig {
    private static final Logger logger = LogManager.getLogger(FlywayConfig.class);

    public static void flywayMigrate(){
        logger.info("Checking new flyway migration");
        DbConfig config = new DbConfig();
        Flyway flyway = Flyway.configure()
                .dataSource(config.getDbUrl(), config.getDbUser(), config.getDbPassword())
                .load();
        flyway.migrate();
    }
}
