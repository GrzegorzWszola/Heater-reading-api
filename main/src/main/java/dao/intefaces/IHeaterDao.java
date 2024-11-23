package dao.intefaces;

import database.objects.Heater;

import java.util.List;

public interface IHeaterDao {
    void addHeater(Heater heater);
    void deleteHeater(int id);
    void updateHeater(Heater heater, int id);
    Heater getHeater(int id);
    List<Heater> getAllHeaterByFlatId(int flat_id);
    List<Heater> getAllHeaters();
}
