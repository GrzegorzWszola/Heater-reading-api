package dao.intefaces;

import database.objects.Building;

import java.util.List;

public interface IBuildingDao {
    void addBuilding(Building building);
    void deleteBuilding(int id);
    void updateBuilding(Building building, int id);
    Building getBuildingById(int id);
    List<Building> getAllBuildings();
}
