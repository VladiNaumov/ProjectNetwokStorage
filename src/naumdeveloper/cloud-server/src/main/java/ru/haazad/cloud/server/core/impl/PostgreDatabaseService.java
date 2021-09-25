import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.haazad.cloud.server.config.DbConfig;
import ru.haazad.cloud.server.core.DatabaseService;

import java.sql.*;


public class PostgreDatabaseService implements DatabaseService {
    private static final Logger logger = LogManager.getLogger(PostgreDatabaseService.class);

    private Connection connection;
    private PreparedStatement preStatement;

    private PostgreDatabaseService() {
    }

    public static PostgreDatabaseService initializeDbConnection() {
        return new PostgreDatabaseService();
    }

    @Override
    public void connect() {
        DbConfig config = new DbConfig();
        try {
            connection = DriverManager.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPassword());
        } catch (SQLException e) {
            logger.throwing(Level.ERROR, e);
        }
    }

    @Override
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.throwing(Level.ERROR, e);
            }
        }
    }

    private void prepareIsRegister() {
        try {
            preStatement = connection.prepareStatement("select isRegister(?, ?)");
        } catch (SQLException e) {
            logger.throwing(Level.ERROR, e);
        }
    }

    private void prepareRegister() {
        try {
            preStatement = connection.prepareStatement("select registerNewUser(?, ?, ?);");
        } catch (SQLException e) {
            logger.throwing(Level.ERROR, e);
        }
    }

    @Override
    public boolean tryLogin(Object[] args) {
        try {
            prepareIsRegister();
            for (int i = 0; i < args.length; i++) {
                preStatement.setString(i + 1, (String) args[i]);
            }
            ResultSet resultSet = preStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getBoolean(1);
            }
            resultSet.close();
            preStatement.close();
        } catch (SQLException e) {
            logger.throwing(Level.ERROR, e);
        }
        return false;
    }

    @Override
    public boolean tryRegister(Object[] args) {
        try {
            prepareRegister();
            for (int i = 0; i < args.length; i++) {
                preStatement.setString(i + 1, (String) args[i]);
            }
            ResultSet result = preStatement.executeQuery();
            while (result.next()) {
                return result.getBoolean(1);
            }
            result.close();
            preStatement.close();
        } catch (SQLException e) {
            logger.throwing(Level.ERROR, e);
        }
        return false;
    }

    @Override
    public boolean checkLogin(Object[] args) {
        String login = (String) args[0];
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format("select count(1) from users u where u.login = '%s'", login));
            while (resultSet.next()) {
                return resultSet.getString(1).equals("1");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.throwing(Level.ERROR, e);
        }
        return false;
    }
}
