package dao;

import dao.intefaces.IControllerDao;
import database.objects.Controller;
import database.objects.Manager;
import database.utilities.DatabaseConnector;
import logger.FileLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ControllerDaoImpl implements IControllerDao {
    private final DatabaseConnector connection = new DatabaseConnector();
    private final FileLogger fileLogger = new FileLogger("application.log");

    @Override
    public void addNewController(Controller controller) {
        String sql = "INSERT INTO controllers (name) VALUES (?)";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setString(1, controller.getName());
            pstmt.executeUpdate();
            fileLogger.logInfo("Controller added successfully");
        } catch (SQLException e) {
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                fileLogger.logSevere("Could not Controller doesn't exist");
            } else {
                fileLogger.logSevere("Could not add a Controller " + e.getMessage());
            }
        }
        connection.disconnect();
    }

    @Override
    public void updateController(Controller controller, int id) {
        String sql = "UPDATE controllers SET name = ? WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            pstmt.setString(1, controller.getName());
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            fileLogger.logInfo("Controller updated successfully");
        } catch (SQLException e){
            fileLogger.logSevere("Could not update a controller " + e.getMessage());
        }
        connection.disconnect();
    }

    @Override
    public void deleteController(int id) {
        String sql = "DELETE FROM controllers WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            fileLogger.logInfo("Controller deleted successfully");
        } catch (SQLException e){
            fileLogger.logSevere("Could not delete a Controller " + e.getMessage());
        }
        connection.disconnect();
    }


    @Override
    public Controller getController(int id) {
        String sql = "SELECT * FROM controllers WHERE id = ?";
        Controller controller = null;
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                controller = new Controller(rs.getString("name"));
            }
        } catch (SQLException e){
            fileLogger.logSevere("Could not fetch a controller " + e.getMessage());
        }
        connection.disconnect();
        return controller;
    }

    @Override
    public List<Controller> getControllers() {
        String sql = "SELECT * FROM controllers";
        List<Controller> controllers = null;
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()){
                    controllers.add(new Controller(rs.getString("name")));
                }
            }
        }catch (SQLException e){
            fileLogger.logSevere("Could not fetch a controllers " + e.getMessage());
        }
        connection.disconnect();
        return controllers;
    }
}
