public class ServerApp {

    public static void main(String[] args) {
        FlywayConfig.flywayMigrate();

        Factory.getServerService().startServer();
    }

}
