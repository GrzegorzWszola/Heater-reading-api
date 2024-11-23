package CurrentManager;

import database.objects.Flat;

import java.util.List;

import static org.manager.ManagerClient.flatsDao;

public class CurrentManager {
    private int id;
    List<Flat> flats;
    private String name;

    public CurrentManager(int id, List<Flat> flats, String name) {
        this.id = id;
        this.flats = flats;
        this.name = name;
    }

    public int getId() {return id;}
    public List<Flat> getFlats() {return flats;}
    public String getName() {return name;}

    public void refreshData(){
        this.flats = flatsDao.getAllFlatsByManagerId(id);
    }
}
