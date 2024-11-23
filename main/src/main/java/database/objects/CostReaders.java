package database.objects;

import dao.CostReadingDaoImpl;
import dao.FlatsDaoImpl;
import dao.HeaterDaoImpl;
import dao.intefaces.ICostReadingDao;
import dao.intefaces.IFlatsDao;
import dao.intefaces.IHeaterDao;

import java.util.List;

public class CostReaders {
    public static IFlatsDao flatsDao = new FlatsDaoImpl();
    int id;
    private int flat_id;
    private double toPay;
    private int isPaid;
    IHeaterDao heaterDao = new HeaterDaoImpl();
    private double costs;

    public CostReaders(int flat_id) {
        this.flat_id = flat_id;
        this.toPay = calculateToPay(flat_id);
    }

    public int getFlat_id() {return flat_id;}
    public double getToPay() {return toPay;}
    public int getIsPaid() {return isPaid;}
    public void setIsPaid(int isPaid) {
        this.isPaid = isPaid;
    }
    public double getCosts() {return costs;}
    public void setCosts(double costs) {this.costs = costs;}

    public double calculateToPay(int id){
        List<Heater> heaters = heaterDao.getAllHeaterByFlatId(id);
        double cost = 0;
        for(Heater heater : heaters){
            cost += heater.getReading() * 1.3;
        }
        cost += flatsDao.getFlat(flat_id).getFlat_cost();
        return cost;
    }

}
