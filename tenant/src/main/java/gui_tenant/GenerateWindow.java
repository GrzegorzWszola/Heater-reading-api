package gui_tenant;

import dao.intefaces.ICostReadingDao;
import dao.intefaces.IFlatsDao;
import dao.intefaces.IHeaterDao;
import objectUtils.Tenant;

import javax.swing.*;
import java.awt.*;

public class GenerateWindow extends JFrame {
    public GenerateWindow(IHeaterDao heaterDao, ICostReadingDao costReadingDao, IFlatsDao flatsDao) {
        super("Tenant Client");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocation(200, 200);
        setLayout(new FlowLayout());
        setVisible(true);

        UserLogin userLogin = new UserLogin(this, heaterDao, id -> {
            Tenant tenant = new Tenant(id, heaterDao.getAllHeaterByFlatId(id), costReadingDao.getAllPaid(id), costReadingDao.getAllUnpaid(id));

            MainWindow mainWindow = new MainWindow(this, tenant, costReadingDao);
            getContentPane().removeAll();
            add(mainWindow);
            revalidate();
            repaint();
        });

        add(userLogin);
    }
}
