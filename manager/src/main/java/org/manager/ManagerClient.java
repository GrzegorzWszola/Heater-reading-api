package org.manager;

import dao.*;
import dao.intefaces.*;
import gui_manager.GenerateGui;

import java.awt.*;

public class ManagerClient {
    public static IManagerDao managerDao = new ManagerDaoImpl();
    public static IFlatsDao flatsDao = new FlatsDaoImpl();
    public static ITaskDao taskDao = new TaskDaoImpl();
    public static IBuildingDao buildingDao = new BuildingDaoImpl();
    public static ICostReadingDao costReadingDao = new CostReadingDaoImpl();
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new GenerateGui(managerDao, flatsDao, taskDao, buildingDao, costReadingDao));
    }
}