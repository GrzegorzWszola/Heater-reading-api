package gui_manager;

import CurrentManager.CurrentManager;
import dao.intefaces.*;

import javax.swing.*;
import java.awt.*;

public class GenerateGui extends JFrame {
    public GenerateGui(IManagerDao managerDao, IFlatsDao flatsDao, ITaskDao taskDao, IBuildingDao buildingDao, ICostReadingDao costReadingDao)  {
        super("Manager Client");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocation(200, 200);
        setLayout(new FlowLayout());
        setVisible(true);

        ManagerInput managerInput = new ManagerInput(this, managerDao, id -> {
            CurrentManager currentManager = new CurrentManager(managerDao.getManager(id).getManagerId(), flatsDao.getAllFlatsByManagerId(id), managerDao.getManager(id).getManagerName());

            MainWindow mainWindow = new MainWindow(this, currentManager, taskDao, flatsDao, buildingDao, costReadingDao);
            getContentPane().removeAll();
            add(mainWindow);
            revalidate();
            repaint();
        });
        add(managerInput);

    }
}