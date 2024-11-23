package dao.intefaces;

import database.objects.Manager;

import java.util.List;

public interface IManagerDao {
    void addManager(Manager manager);
    void deleteManager(int id);
    void updateManager(Manager manager, int id);
    Manager getManager(int id);
    List<Manager> getAllManagers();
}
