package org.controller;

import dao.*;
import dao.intefaces.*;
import gui_controller.GenerateGui;

import java.awt.*;

public class ControllerClient {
    public static IControllerDao controllerDao = new ControllerDaoImpl();
    public static IBuildingDao buildingDao = new BuildingDaoImpl();
    public static IHeaterDao heaterDao = new HeaterDaoImpl();
    public static ITaskDao taskDao = new TaskDaoImpl();
    public static IFlatsDao flatsDao = new FlatsDaoImpl();
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> new GenerateGui(controllerDao, taskDao, flatsDao, buildingDao, heaterDao));
    }
}
