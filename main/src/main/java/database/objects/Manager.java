package database.objects;

public class Manager {
    private int id;
    private final String name;

    public Manager(String name) {
        this.name = name;
    }

    public String getManagerName() {return name;}
    public int getManagerId() {return id;}
    public void setManagerId(int id) {this.id = id;}
}
