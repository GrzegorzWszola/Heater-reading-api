package dao;

import dao.intefaces.ITaskDao;
import database.objects.Task;
import database.utilities.DatabaseConnector;
import logger.FileLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDaoImpl implements ITaskDao {
    private final DatabaseConnector connection = new DatabaseConnector();
    private final FileLogger fileLogger = new FileLogger("application.log");

    @Override
    public void addTask(Task task) {
        String sql = "INSERT INTO tasks (planned_date, task_performer) VALUES (?, ?)";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            pstmt.setInt(1, task.getPlannedDate());
            pstmt.setInt(2, task.getTaskPerformer());
            pstmt.executeUpdate();
            fileLogger.logInfo("Task added successfully");
        } catch (SQLException e) {
            fileLogger.logSevere("Could not add a task " + e.getMessage());
        }
        connection.disconnect();
    }

    @Override
    public void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            fileLogger.logInfo("Task deleted successfully");
        }catch (SQLException e) {
            fileLogger.logSevere("Could not delete a task " + e.getMessage());
        }
        connection.disconnect();
    }

    @Override
    public void updateTask(Task task) {
        String sql = "UPDATE tasks SET planned_date = ?, task_performer = ? WHERE id = ?";
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            pstmt.setInt(1, task.getPlannedDate());
            pstmt.setInt(2, task.getTaskPerformer());
            pstmt.executeUpdate();
            fileLogger.logInfo("Task updated successfully");
        }catch (SQLException e) {
            fileLogger.logSevere("Could not update a task " + e.getMessage());
        }
        connection.disconnect();
    }

    @Override
    public Task getTask(int id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        Task task = null;
        try(PreparedStatement pstmt = connection.connect().prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                task = new Task( rs.getInt("planned_date"), rs.getInt("task_performer"));
                task.setId(rs.getInt("id"));
                fileLogger.logInfo("Task retrieved successfully");
            }
        }catch (SQLException e) {
            fileLogger.logSevere("Could not get a task " + e.getMessage());
        }
        connection.disconnect();
        return task;
    }

    @Override
    public List<Task> getAllTasks() {
        String sql = "SELECT * FROM tasks";
        List<Task> tasks = new ArrayList<>(); // Initialize the list
        try (PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
                fileLogger.logInfo("There is no tasks");
            } else {
                while (rs.next()) {
                    Task task = new Task( rs.getInt("planned_date"), rs.getInt("task_performer"));
                    task.setId(rs.getInt("id"));
                    tasks.add(task);
                }
                fileLogger.logInfo("Tasks retrieved successfully.");
            }
        } catch (SQLException e) {
            fileLogger.logSevere("Could not get tasks: " + e.getMessage());
        } finally {
            connection.disconnect();
        }
        return tasks;
    }

    @Override
    public List<Task> getTasksByController(int id) {
        String sql = "SELECT * FROM tasks WHERE task_performer = ?";
        List<Task> tasks = new ArrayList<>();
        try (PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    Task task = new Task( rs.getInt("planned_date"), rs.getInt("task_performer"));
                    task.setId(rs.getInt("id"));
                    tasks.add(task);
                }
            }
        }catch (SQLException e){
            fileLogger.logSevere("Could not get tasks: " + e.getMessage());
        }
        connection.disconnect();
        return tasks;
    }

    @Override
    public int getLastItemId() {
        String sql = "SELECT * FROM tasks ORDER BY id DESC LIMIT 1";
        int id = 0;
        try (PreparedStatement pstmt = connection.connect().prepareStatement(sql)) {
            try(ResultSet rs = pstmt.executeQuery()){
                id = rs.getInt("id");
            }
        }catch (SQLException e) {
            fileLogger.logSevere("Could not get tasks: " + e.getMessage());
        }
        connection.disconnect();
        return id;
    }
}
