package objects;

import database.objects.Flat;
import database.objects.Task;

import java.util.List;

import static org.controller.ControllerClient.taskDao;

public class CurrentController {
    private int id;
    private String name;
    private List<Task> tasks;

    public CurrentController(int id, String name, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public List<Task> getTasks() {return tasks;}

    public void refreshData(){
        this.tasks = taskDao.getTasksByController(id);
    }
}
