package database.objects;

import java.time.LocalDate;
import java.util.List;

public class Task {
    private int id;
    private int plannedDate;
    private int taskPerformer;
    private List<Flat> flats;

    public Task(int plannedDate, int taskPerformer) {
        this.plannedDate = plannedDate;
        this.taskPerformer = taskPerformer;
    }

    public int getPlannedDate() {return plannedDate;}
    public void setPlannedDate(int plannedDate) {
        if(LocalDate.now().isAfter(LocalDate.parse(Integer.toString(plannedDate)))){
            System.out.println("Can not set planned date before today");
        } else {
            this.plannedDate = plannedDate;
        }
    }

    public int getTaskPerformer() {return taskPerformer;}
    public void setTaskPerformer(int taskPerformer) {this.taskPerformer = taskPerformer;}
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public List<Flat> getFlats() {return flats;}
    public void setFlats(List<Flat> flats) {this.flats = flats;}
}
