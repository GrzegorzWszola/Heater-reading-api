package database.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;


public class DatabaseConnector {
    private Connection connection;
    private final Logger logger = Logger.getLogger(DatabaseConnector.class.getName());
    private final String dbUrl = "jdbc:sqlite:HeaterReadingAPI.db";

    public Connection connect(){
        try{
            if(connection != null){
                return connection;
            } else {
                connection = DriverManager.getConnection(dbUrl);
                try (Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate("PRAGMA foreign_keys = ON;");
                }
            }
        } catch (SQLException e) {
            logger.severe("Could not connect to database");
        }

        return connection;
    }

    public void disconnect(){
        try{
            connection.close();
            connection = null;
        } catch(SQLException e){
            logger.severe("Could not disconnect from database");
        }
    }
}
