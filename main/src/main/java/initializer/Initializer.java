package initializer;

import dao.*;
import dao.intefaces.*;
import database.objects.*;

import java.util.Random;

public class Initializer {


    public static void initializeTheData(){
        IBuildingDao buildingsDao = new BuildingDaoImpl();
        IFlatsDao flatsDao = new FlatsDaoImpl();
        IHeaterDao heatersDao = new HeaterDaoImpl();
        IManagerDao managerDao = new ManagerDaoImpl();
        IControllerDao controllerDao = new ControllerDaoImpl();
        ITaskDao taskDao = new TaskDaoImpl();
        Random rand = new Random();

        buildingsDao.addBuilding(new Building("Rydygiera 2a", 0));
        buildingsDao.addBuilding(new Building("Pomorska 3c", 0));
        buildingsDao.addBuilding(new Building("Zielonogorska 18", 0));
        buildingsDao.addBuilding(new Building("Wroclawska 79", 0));
        buildingsDao.addBuilding(new Building("Brodek 51", 0));

        managerDao.addManager(new Manager("Dawid Jasper"));
        managerDao.addManager(new Manager("Zbigniew Stonoga"));
        managerDao.addManager(new Manager("Wlodzimierz Bialy"));

        controllerDao.addNewController(new Controller("Zbigniew Małysz"));
        controllerDao.addNewController(new Controller("Kamil Stoch"));

        for (int i = 0; i < 100; i++){
            Flat flat = new Flat(rand.nextInt(100)+1, rand.nextInt(5)+1, rand.nextInt(3)+1);
            flatsDao.addFlat(flat);

        }

        for(int i = 0; i < 300; i++){
            int flatId = rand.nextInt(40)+1;
            double reading = rand.nextDouble(10)+1;
            Building building = buildingsDao.getBuildingById(flatsDao.getFlat(flatId).getBuilding_id());
            double currentMainRead = building.getMainReader();
            heatersDao.addHeater(new Heater(reading, flatId));
            currentMainRead += reading;
            building.setMainReader(currentMainRead);
            buildingsDao.updateBuilding(building, flatsDao.getFlat(flatId).getBuilding_id());
        }
    }
}
