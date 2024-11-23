package dao.intefaces;

import database.objects.CostReaders;

import java.util.List;

public interface ICostReadingDao {
    void addReading(CostReaders reading);
    void updateReading(double newPayAmount, int isPaid, int id);
    void deleteReading(int id);
    CostReaders getReading(int id);
    List<CostReaders> getAllReadings(int id);
    List<CostReaders> getAllUnpaid(int id);
    List<CostReaders> getAllPaid(int id);
    public void markAsPaid(int flat_id);
}
