package dao;

import dao.intefaces.IManagerDao;
import database.objects.Manager;
import database.utilities.DatabaseConnector;
import logger.FileLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManagerDaoImpl implements IManagerDao {
    private final DatabaseConnector connection = new DatabaseConnector();
    private final FileLogger fileLogger = new FileLogger("application.log");

    @Override
    public void addManager(Manager manager) {
        String sql = "INSERT INTO managers (name) VALUES (?)";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            pstmt.setString(1, manager.getManagerName());
            pstmt.executeUpdate();
            fileLogger.logInfo("Manager added successfully");
        }catch (SQLException e){
            fileLogger.logSevere("Could not add a manager " + e.getMessage());
        }
        connection.disconnect();
    }

    @Override
    public void deleteManager(int id) {
        String sql = "DELETE FROM managers WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            fileLogger.logInfo("Manager deleted successfully");
        } catch (SQLException e){
            fileLogger.logSevere("Could not delete a manager " + e.getMessage());
        }
        connection.disconnect();
    }

    @Override
    public void updateManager(Manager manager, int id) {
        String sql = "UPDATE managers SET name = ? WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            pstmt.setString(1, manager.getManagerName());
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            fileLogger.logInfo("Manager updated successfully");
        } catch (SQLException e){
            fileLogger.logSevere("Could not update a manager " + e.getMessage());
        }
        connection.disconnect();
    }

    @Override
    public Manager getManager(int id) {
        String sql = "SELECT managers.id, managers.name FROM managers WHERE id = ?";
        Manager manager = null;
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                manager = new Manager(rs.getString("name"));
                manager.setManagerId(rs.getInt("id"));
            }
        } catch (SQLException e){
            fileLogger.logSevere("Could not fetch a manager " + e.getMessage());
        }
        connection.disconnect();
        return manager;
    }

    @Override
    public List<Manager> getAllManagers() {
        String sql = "SELECT managers.name FROM managers ";
        List<Manager> managers = new ArrayList<>();

        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                managers.add(new Manager(rs.getString("name")));
            }
        } catch (SQLException e){
            fileLogger.logSevere("Could not fetch a manager list " + e.getMessage());
        }
        connection.disconnect();
        return managers;
    }
}
