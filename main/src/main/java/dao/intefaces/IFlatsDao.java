package dao.intefaces;

import database.objects.Flat;

import java.util.List;

public interface IFlatsDao {
    void addFlat(Flat flat);
    void deleteFlat(int id);
    void updateFlat(Flat flat, int id);
    Flat getFlat(int id);
    List<Flat> getAllFlatsByBuildingId(int buildingId);
    List<Flat> getAllFlatsByManagerId(int managerId);
    List<Flat> getAllFlatsByTaskId(int taskId);
    void updateFlatsForTask(int flat_id, int task_id);
    List<Flat> getAllFlats();
    void updateFlatCost(double flat_cost, int flat_id);
}
