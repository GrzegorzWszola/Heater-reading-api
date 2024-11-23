package gui_controller;

import dao.intefaces.*;
import objects.CurrentController;

import javax.swing.*;
import java.awt.*;

public class GenerateGui extends JFrame {
    public GenerateGui(IControllerDao controllerDao, ITaskDao taskDao, IFlatsDao flatsDao, IBuildingDao buildingDao, IHeaterDao heaterDao) {
        super("Controller Client");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocation(200, 200);
        setLayout(new FlowLayout());
        setVisible(true);

        ControlerInput controlerInput = new ControlerInput(this, controllerDao, id -> {
            CurrentController currentController = new CurrentController(id, controllerDao.getController(id).getName(), taskDao.getTasksByController(id));

            MainWindow mainWindow = new MainWindow(this, currentController, taskDao, flatsDao, buildingDao, heaterDao);
            getContentPane().removeAll();
            add(mainWindow);
            revalidate();
            repaint();
        });
        add(controlerInput);
    }
}
