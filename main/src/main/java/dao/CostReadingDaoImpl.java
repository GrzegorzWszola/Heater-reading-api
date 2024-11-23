package dao;

import dao.intefaces.ICostReadingDao;
import database.objects.CostReaders;
import database.utilities.DatabaseConnector;
import logger.FileLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CostReadingDaoImpl implements ICostReadingDao {
    private final DatabaseConnector connection = new DatabaseConnector();
    private final FileLogger fileLogger = new FileLogger("application.log");

    @Override
    public void addReading(CostReaders reading) {
        String sql = "INSERT INTO cost_readers (flat_id, to_pay) VALUES (?, ?)";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, reading.getFlat_id());
            pstmt.setDouble(2, reading.getToPay());
            pstmt.executeUpdate();
            fileLogger.logInfo("Cost reading added successfully");
        }catch (SQLException e) {
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                fileLogger.logSevere("Could not add a reading, the building to assign a reading doesn't exist");
            } else {
                fileLogger.logSevere("Could not add a reading " + e.getMessage());
            }
        }
        connection.disconnect();
    }

    @Override
    public void updateReading(double newPayAmount, int isPaid, int id) {
        String sql = "UPDATE cost_readers SET to_pay = ?, isPaid = ? WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setDouble(1, newPayAmount);
            pstmt.setInt(2, isPaid);
            pstmt.setDouble(3, id);
            pstmt.executeUpdate();
            fileLogger.logInfo("Cost reading updated successfully");
        }catch (SQLException e) {
            fileLogger.logSevere("Could not update a reading " + e.getMessage());
        }
        connection.disconnect();
    }

    @Override
    public void deleteReading(int id) {
        String sql = "DELETE FROM cost_readers WHERE flat_id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            fileLogger.logInfo("Cost reading deleted successfully");
        } catch (SQLException e) {
            fileLogger.logSevere("Could not delete a reading " + e.getMessage());
        }
        connection.disconnect();
    }

    @Override
    public CostReaders getReading(int id) {
        CostReaders reading = null;

        String sql = "SELECT * FROM cost_readers WHERE flat_id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    reading = new CostReaders(id);
                }
            }
        } catch (SQLException e) {
            fileLogger.logSevere("Could not get a reading " + e.getMessage());
        }
        connection.disconnect();
        return reading;
    }

    @Override
    public List<CostReaders> getAllReadings(int id) {
        String sql = "SELECT * FROM cost_readers WHERE flat_id = ?";
        List<CostReaders> readings = null;

        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    readings.add(new CostReaders(id));
                }
            }
        }catch (SQLException e) {
            fileLogger.logSevere("Could not get a reading " + e.getMessage());
        }catch (NullPointerException e) {
            fileLogger.logSevere("The list is empty " + e.getMessage());
        }
        connection.disconnect();
        return readings;
    }

    @Override
    public List<CostReaders> getAllUnpaid(int id){
        String sql = "SELECT isPaid FROM cost_readers WHERE flat_id = ? AND isPaid = 0";
        List<CostReaders> unpaid = new ArrayList<>();

        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    unpaid.add(new CostReaders(id));
                }
            }
        }catch (SQLException e) {
            fileLogger.logSevere("Could not get readings " + e.getMessage());
        }
        connection.disconnect();
        return unpaid;
    }

    @Override
    public List<CostReaders> getAllPaid(int id){
        String sql = "SELECT isPaid FROM cost_readers WHERE flat_id = ? AND isPaid = 1";
        List<CostReaders> paid = new ArrayList<>();

        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    paid.add(new CostReaders(id));
                }
            }
        }catch (SQLException e) {
            fileLogger.logSevere("Could not get readings " + e.getMessage());
        }
        connection.disconnect();
        return paid;
    }

    @Override
    public void markAsPaid(int flatId) {
        String sql = "UPDATE cost_readers SET isPaid = 1 WHERE flat_id = ? AND isPaid = 0";  // Update all unpaid readings for a specific flat

        try (PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, flatId);  // Set the flat_id to identify which records to update

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                fileLogger.logInfo(rowsAffected + " cost reading(s) for flat ID " + flatId + " marked as paid.");
            } else {
                fileLogger.logWarning("No unpaid cost readings found for flat ID " + flatId);
            }
        } catch (SQLException e) {
            fileLogger.logSevere("Could not mark readings as paid: " + e.getMessage());
        } finally {
            connection.disconnect();
        }
    }
}
