package database.objects;

public class Heater {
    private int id;
    private double reading;
    private final int flat_id;

    public Heater(double reading, int flat_id) {
        this.reading = reading;
        this.flat_id = flat_id;
    }

    public double getReading() {return reading;}
    public void setReading(double newReading) {
        this.reading = newReading;
    }
    public int getFlat_id() {return flat_id;}
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
}
