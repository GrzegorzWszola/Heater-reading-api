package dao.intefaces;

import database.objects.Controller;

import java.util.List;

public interface IControllerDao {
    void addNewController(Controller controller);
    void updateController(Controller controller, int id);
    void deleteController(int id);
    Controller getController(int id);
    List<Controller> getControllers();
}
