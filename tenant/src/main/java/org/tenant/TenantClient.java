package org.tenant;

import dao.CostReadingDaoImpl;
import dao.FlatsDaoImpl;
import dao.HeaterDaoImpl;
import dao.intefaces.ICostReadingDao;
import dao.intefaces.IFlatsDao;
import dao.intefaces.IHeaterDao;
import gui_tenant.GenerateWindow;

import java.awt.*;


public class TenantClient {
    private static final IHeaterDao heaterDao = new HeaterDaoImpl();
    public static final ICostReadingDao costReadingDao = new CostReadingDaoImpl();
    public static final IFlatsDao flatsDao = new FlatsDaoImpl();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new GenerateWindow(heaterDao, costReadingDao, flatsDao));
    }
}