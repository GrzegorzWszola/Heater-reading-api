package dao;


import dao.intefaces.IFlatsDao;
import database.objects.Flat;
import database.utilities.DatabaseConnector;
import logger.FileLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FlatsDaoImpl implements IFlatsDao {
    private final DatabaseConnector connection = new DatabaseConnector();
    private final FileLogger fileLogger = new FileLogger("application.log");

    @Override
    public void addFlat(Flat flat){
        String sql = "INSERT INTO flats (number, building_id, manager_id, task_id) VALUES (?, ?, ?, NULL)";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, flat.getNumber());
            pstmt.setInt(2, flat.getBuilding_id());
            pstmt.setInt(3, flat.getManager_id());
            pstmt.executeUpdate();
            fileLogger.logInfo("Flat added successfully");
        } catch (SQLException e) {
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                fileLogger.logSevere("Could not add a flat, the building to assign an apartment or manager doesn't exist");
            } else {
                fileLogger.logSevere("Could not add a flat " + e.getMessage());
            }
        }
        connection.disconnect();
    }

    @Override
    public void deleteFlat(int id) {
        String sql = "DELETE FROM flats WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            fileLogger.logInfo("Flat deleted successfully");
        } catch (SQLException e) {
            fileLogger.logSevere("could not delete flat");
        }
        connection.disconnect();
    }

    @Override
    public void updateFlat(Flat flat, int id) {
        String sql = "UPDATE flats SET number = ?, building_id = ?, manager_id = ? WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, flat.getNumber());
            pstmt.setInt(2, flat.getBuilding_id());
            pstmt.setInt(3, flat.getManager_id());
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            fileLogger.logInfo("Flat updated successfully");
        } catch (SQLException e){
            fileLogger.logSevere("Could not update flat " + flat.getNumber() + e.getMessage());
        }
        connection.disconnect();
    }

    @Override
    public Flat getFlat(int id) {
        String sql = "SELECT flats.id, flats.number, flats.building_id, flats.manager_id, flats.flat_cost FROM flats WHERE id = ?";
        Flat flat = null;

        try (PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    flat = new Flat(rs.getInt("number"), rs.getInt("building_id"), rs.getInt("manager_id"));
                    flat.setId(rs.getInt("id"));
                    flat.setFlat_cost(rs.getDouble("flat_cost"));
                } else {
                    fileLogger.logInfo("No flat found with id " + id);
                }
            }
        } catch (SQLException e) {
            fileLogger.logSevere("Could not get flat " + id + ": " + e.getMessage());
        }
        connection.disconnect();
        return flat;
    }

    @Override
    public List<Flat> getAllFlatsByBuildingId(int building_id){
        String sql = "SELECT flats.number, flats.building_id FROM flats WHERE building_id = ?";
        List<Flat> flats = new ArrayList<>();
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, building_id);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                Flat flat = new Flat(rs.getInt("number"), rs.getInt("building_id"), rs.getInt("manager_id"));
                flats.add(flat);
            }
        } catch (SQLException e) {
            fileLogger.logSevere("Could not fetch data from Flats list");
        }
        connection.disconnect();
        return flats;
    }

    @Override
    public List<Flat> getAllFlatsByManagerId(int manager_id){
        String sql = "SELECT flats.id, flats.number, flats.building_id, flats.manager_id, flats.flat_cost FROM flats WHERE manager_id = ?";
        List<Flat> flats = new ArrayList<>();
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, manager_id);
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()) {
                    Flat flat = new Flat(rs.getInt("number"), rs.getInt("building_id"), rs.getInt("manager_id"));
                    flat.setId(rs.getInt("id"));
                    flat.setFlat_cost(rs.getDouble("flat_cost"));
                    flats.add(flat);
                }
            }
        } catch (SQLException e) {
            fileLogger.logSevere("Could not fetch data from Flats list");
        }
        connection.disconnect();
        return flats;
    }

    @Override
    public List<Flat> getAllFlatsByTaskId(int taskId){
        String sql = "SELECT * FROM flats WHERE task_id = ?";
        List<Flat> flats = new ArrayList<>();
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    Flat flat = new Flat(rs.getInt("number"), rs.getInt("building_id"), rs.getInt("manager_id"));
                    flat.setTask_id(rs.getInt("task_id"));
                    flat.setId(rs.getInt("id"));
                    flats.add(flat);
                }
            }
        }catch (SQLException e){
            fileLogger.logSevere("Could not fetch data from Flats list" + e.getMessage());
        }
        connection.disconnect();
        return flats;
    }

    @Override
    public List<Flat> getAllFlats(){
        String sql = "SELECT * FROM flats";
        List<Flat> flats = new ArrayList<>();
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    Flat flat = new Flat(rs.getInt("number"), rs.getInt("building_id"), rs.getInt("manager_id"));
                    flat.setTask_id(rs.getInt("task_id"));
                    flats.add(flat);
                }
            }
        }catch (SQLException e){
            fileLogger.logSevere("Could not fetch data from Flats list" + e.getMessage());
        }
        connection.disconnect();
        return flats;
    }

    @Override
    public void updateFlatsForTask(int flat_id, int task_id){
        String sql = "UPDATE flats SET task_id = ? WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, task_id);
            pstmt.setInt(2, flat_id);
            pstmt.executeUpdate();
        }catch (SQLException e){
            fileLogger.logSevere("Could not update flat " + flat_id + " " + task_id + ": " + e.getMessage());
            e.printStackTrace();
        }
        connection.disconnect();
    }

    @Override
    public void updateFlatCost(double flat_cost, int flat_id){
        String sql = "UPDATE flats SET flat_cost = ? WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setDouble(1, flat_cost);
            pstmt.setInt(2, flat_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            fileLogger.logSevere("Could not update flat " + flat_id + " " + flat_cost + ": " + e.getMessage());
        }
        connection.disconnect();
    }

}
