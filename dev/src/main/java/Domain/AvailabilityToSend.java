package Domain;

public class AvailabilityToSend {
    public int id;
    public String startweek;
    public String endweek;
    public int[][] availability;

    public AvailabilityToSend(int id, String sw, String ew, int[][] av){
        this.availability = av;
        this.endweek = ew;
        this.id = id;
        this.startweek = sw;
    }

    public String getStartweek() {
        return startweek;
    }

    public void setStartweek(String startweek) {
        this.startweek = startweek;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEndweek() {
        return endweek;
    }

    public void setEndweek(String endweek) {
        this.endweek = endweek;
    }

    public int[][] getAvailability() {
        return availability;
    }

    public void setAvailability(int[][] availability) {
        this.availability = availability;
    }
}
