package dao.intefaces;

import database.objects.Task;

import java.util.List;

public interface ITaskDao {
    void addTask(Task task);
    void deleteTask(int id);
    void updateTask(Task task);
    Task getTask(int id);
    List<Task> getAllTasks();
    List<Task> getTasksByController(int id);
    public int getLastItemId();
}
