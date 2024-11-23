package dao;

import dao.intefaces.IHeaterDao;
import database.objects.Heater;
import database.utilities.DatabaseConnector;
import logger.FileLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HeaterDaoImpl implements IHeaterDao {
    private final DatabaseConnector connection = new DatabaseConnector();
    private final FileLogger fileLogger = new FileLogger("application.log");

    @Override
    public void addHeater(Heater heater) {
        String sql = "INSERT INTO heaters (reading, flat_id) VALUES (?, ?)";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setDouble(1, heater.getReading());
            pstmt.setInt(2, heater.getFlat_id());
            pstmt.executeUpdate();
            fileLogger.logInfo("Heater added successfully");
        } catch (SQLException e) {
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                fileLogger.logSevere("Could not add a heater, the apartment does not exist");
            } else {
                fileLogger.logSevere("Could not add a heater");
            }
        }
        connection.disconnect();
    }

    @Override
    public void deleteHeater(int id) {
        String sql = "DELETE FROM heaters WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            fileLogger.logInfo("Heater deleted successfully");
        } catch (SQLException e) {
            fileLogger.logSevere("could not delete heater" + " " + e.getMessage());
        }
        connection.disconnect();
    }

    @Override
    public void updateHeater(Heater heater, int id) {
        String sql = "UPDATE heaters SET reading = ?, flat_id = ? WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setDouble(1, heater.getReading());
            pstmt.setInt(2, heater.getFlat_id());
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            fileLogger.logInfo("Heater updated successfully");
        } catch (SQLException e){
            fileLogger.logSevere("Could not update flat " + " " + e.getMessage());
        }
        connection.disconnect();
    }

    @Override
    public Heater getHeater(int id) {
        String sql = "SELECT heaters.reading, heaters.flat_id FROM heaters WHERE id = ?";
        Heater heater = null;

        try (PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    heater = new Heater(rs.getDouble("reading"), rs.getInt("flat_id"));
                } else {
                    fileLogger.logInfo("No heater found with id " + id);
                }
            }
        } catch (SQLException e) {
            fileLogger.logSevere("Could not get flat " + id + ": " + e.getMessage());
        }
        connection.disconnect();
        return heater;
    }

    @Override
    public List<Heater> getAllHeaterByFlatId(int flat_id) {
        String sql = "SELECT heaters.id, heaters.reading, heaters.flat_id FROM heaters WHERE flat_id = ?";
        List<Heater> heaters = new ArrayList<>();

        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            pstmt.setInt(1, flat_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Heater heater = new Heater(rs.getDouble("reading"), rs.getInt("flat_id"));
                    heater.setId(rs.getInt("id"));
                    heaters.add(heater);
                }
            }
        } catch (SQLException e){
            fileLogger.logSevere("Could not get heater from " + flat_id + ": " + e.getMessage());
        }
        connection.disconnect();
        return heaters;
    }

    public List<Heater> getAllHeaters(){
        String sql = "SELECT * FROM heaters";
        List<Heater> heaters = new ArrayList<>();

        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    heaters.add(new Heater(rs.getDouble("reading"), rs.getInt("flat_id")));
                }
            }
        } catch (SQLException e){
            fileLogger.logSevere("Could not get heater from: " + e.getMessage());
        }
        connection.disconnect();
        return heaters;
    }

}
