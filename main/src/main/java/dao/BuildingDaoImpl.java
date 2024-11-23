package dao;

import dao.intefaces.IBuildingDao;
import database.objects.Building;
import database.utilities.DatabaseConnector;
import logger.FileLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BuildingDaoImpl implements IBuildingDao {
    private final DatabaseConnector connection = new DatabaseConnector();
    private final FileLogger fileLogger = new FileLogger("application.log");

    @Override
    public void addBuilding(Building building) {
        String sql = "INSERT INTO buildings (address, mainReader) VALUES (?,?)";
        try (PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setString(1, building.getAddress());
            pstmt.setDouble(2, building.getMainReader());
            pstmt.executeUpdate();
            fileLogger.logInfo("Building added successfully");
        } catch (SQLException e) {
            if(e.getMessage().contains("UNIQUE")){
                fileLogger.logSevere("There is already a building with this address");
            } else {
                fileLogger.logSevere("Could not add a building" + " " + e.getMessage());
            }
        }
        connection.disconnect();
    }

    @Override
    public void deleteBuilding(int id) {
        String sql = "DELETE FROM buildings WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            fileLogger.logInfo("Building deleted successfully");
        } catch (SQLException e) {
            fileLogger.logSevere("could not delete building" + e.getMessage());
        }
        connection.disconnect();
    }

    @Override
    public void updateBuilding(Building building, int id) {
        String sql = "UPDATE buildings SET address = ?, mainReader = ? WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            pstmt.setString(1, building.getAddress());
            pstmt.setDouble(2, building.getMainReader());
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            fileLogger.logInfo("Building updated successfully");
        }catch (SQLException e) {
            fileLogger.logSevere("could not update building" + e.getMessage());
        }
        connection.disconnect();
    }

    @Override
    public Building getBuildingById(int id) {
        String sql = "SELECT buildings.address, buildings.mainReader FROM buildings WHERE id = ?";
        Building building = null;
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            pstmt.setInt(1, id);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    building = new Building(rs.getString("address"), rs.getDouble("mainReader"));
                }
            }
        }catch (SQLException e) {
            fileLogger.logSevere("could not get building " + e.getMessage());
        }
        connection.disconnect();
        return building;
    }

    @Override
    public List<Building> getAllBuildings() {
        String sql = "SELECT buildings.address, buildings.mainReader FROM buildings";
        List<Building> buildings = new ArrayList<>();
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                Building building = new Building(rs.getString("address"), rs.getDouble("mainReader"));
                buildings.add(building);
            }
        } catch (SQLException e) {
            fileLogger.logSevere("Could not fetch data from Buildings list" + " " + e.getMessage());
        }
        connection.disconnect();
        return buildings;
    }
}
