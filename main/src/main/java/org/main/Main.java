package org.main;


import dao.CostReadingDaoImpl;
import dao.FlatsDaoImpl;
import dao.TaskDaoImpl;
import dao.intefaces.ICostReadingDao;
import dao.intefaces.IFlatsDao;
import dao.intefaces.ITaskDao;
import database.objects.Task;
import database.utilities.DatabaseSetup;
import initializer.Initializer;

public class Main {
    static {
        DatabaseSetup.setupDatabase();
        DatabaseSetup.createTables();
        Initializer.initializeTheData();
    }

    public static void main(String[] args) {
    }
}