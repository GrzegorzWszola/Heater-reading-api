package objectUtils;

import database.objects.CostReaders;
import database.objects.Heater;

import java.util.ArrayList;
import java.util.List;

import static org.tenant.TenantClient.costReadingDao;

public class Tenant {
    private List<Heater> heaterList = new ArrayList<>();
    private int flatId;
    private double toPay;
    private List<CostReaders> isPaid;
    private List<CostReaders> isUnpaid;

    public Tenant(int flatId, List<Heater> heaterList, List<CostReaders> isPaid, List<CostReaders> isUnpaid) {
        this.flatId = flatId;
        this.heaterList = heaterList;
        this.isPaid = isPaid;
        this.isUnpaid = isUnpaid;
    }

    public List<Heater> getHeaterList() {return heaterList;}
    public int getFlatId() {return flatId;}
    public void setFlatId(int flatId) {this.flatId = flatId;}
    public double getToPay() {return toPay;}
    public List<CostReaders> getIsPaid() {return isPaid;}
    public List<CostReaders> getIsUnpaid() {return isUnpaid;}

    public void refreshData() {
        this.isUnpaid = costReadingDao.getAllUnpaid(flatId);
        this.isPaid = costReadingDao.getAllPaid(flatId);
    }
}
