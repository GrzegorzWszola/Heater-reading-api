package database.utilities;

import logger.FileLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DatabaseSetup {
    private static final String DATABASE_URL = "jdbc:sqlite:HeaterReadingAPI.db";
    private static final FileLogger fileLogger = new FileLogger("application.log");
    private static Connection connection = null;

    public static void setupDatabase() {
        try{
            connection = DriverManager.getConnection(DATABASE_URL);
            fileLogger.logInfo("Connected successfully");
        }
        catch(SQLException e){
            fileLogger.logSevere("Couldn't connect to database");
        }
    }

    public static void createTables(){
        String sqlBuildingTables = """
                CREATE TABLE IF NOT EXISTS buildings
                (id INTEGER PRIMARY KEY AUTOINCREMENT,
                address TEXT NOT NULL UNIQUE,
                mainReader REAL NOT NULL);
                """;

        String sqlFlatsTables = """
                CREATE TABLE IF NOT EXISTS flats
                (id INTEGER PRIMARY KEY AUTOINCREMENT,
                number INTEGER NOT NULL,
                building_id INTEGER,
                manager_id INTEGER,
                task_id INTEGER DEFAULT NULL,
                flat_cost REAL DEFAULT 100.0,
                FOREIGN KEY (building_id) REFERENCES buildings(id) ON DELETE CASCADE,
                FOREIGN KEY (manager_id) REFERENCES managers(id),
                FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE SET DEFAULT);
                """;

        String sqlHeaterTables= """
                CREATE TABLE IF NOT EXISTS heaters
                (id INTEGER PRIMARY KEY AUTOINCREMENT,
                reading REAL NOT NULL,
                flat_id INTEGER,
                FOREIGN KEY (flat_id) REFERENCES flats(id) ON DELETE CASCADE);
                """;

        String sqlManagerTables= """
                CREATE TABLE IF NOT EXISTS managers
                (id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL)
                """;

        String sqlTaskTables= """
                CREATE TABLE IF NOT EXISTS tasks
                (id INTEGER PRIMARY KEY AUTOINCREMENT,
                planned_date INTEGER NOT NULL,
                task_performer INTEGER NOT NULL,
                FOREIGN KEY (task_performer) REFERENCES controllers(id));
                """;

        String sqlCostReaders= """
                CREATE TABLE IF NOT EXISTS cost_readers
                (id INTEGER PRIMARY KEY AUTOINCREMENT,
                flat_id INTEGER NOT NULL,
                to_pay REAL NOT NULL,
                isPaid INTEGER DEFAULT 0,
                FOREIGN KEY (flat_id) REFERENCES flats(id) ON DELETE CASCADE)
                """;

        String sqlTaskPerformers= """
                CREATE TABLE IF NOT EXISTS controllers
                (id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL)
                """;

        try(Statement stmt = connection.createStatement()){
            stmt.executeUpdate("PRAGMA foreign_keys = ON;");

            stmt.executeUpdate(sqlBuildingTables);
            stmt.executeUpdate(sqlFlatsTables);
            stmt.executeUpdate(sqlHeaterTables);
            stmt.executeUpdate(sqlManagerTables);
            stmt.executeUpdate(sqlTaskTables);
            stmt.executeUpdate(sqlCostReaders);
            stmt.executeUpdate(sqlTaskPerformers);
            fileLogger.logInfo("Tables successfully created or they already exist");
        } catch(SQLException e){
            fileLogger.logSevere("Couldn't create tables " + e.getMessage());
        }
    }

    public static String getDatabaseUrl() {return DATABASE_URL;}
}
