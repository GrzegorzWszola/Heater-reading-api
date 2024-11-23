package database.objects;

public class Building {
    private final String address;
    private  double mainReader;

    public Building(String address, double mainReader) {
        this.address = address;
        this.mainReader = mainReader;
    }

    public String getAddress() {return address;}
    public double getMainReader() {return mainReader;}
    public void setMainReader(double mainReader) {this.mainReader = mainReader;}
}
