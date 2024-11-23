package database.objects;

public class Flat {
    private int id;
    private final int number;
    private final int building_id;
    private final int manager_id;
    private int task_id;
    private double flat_cost;

    public Flat(int number, int building_id, int manager_id) {
        this.number = number;
        this.building_id = building_id;
        this.manager_id = manager_id;
    }

    public int getNumber() {return number;}
    public int getBuilding_id() {return building_id;}
    public int getManager_id() {return manager_id;}
    public int getTask_id() {return task_id;}
    public void setTask_id(int task_id){this.task_id = task_id;}
    public void setId(int id){this.id = id;}
    public int getId(){return id;}
    public void setFlat_cost(double flat_cost){this.flat_cost = flat_cost;}
    public double getFlat_cost(){return flat_cost;}
}
