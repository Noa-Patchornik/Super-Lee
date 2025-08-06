package Domain;

public class TransportToSend {

    public int id;
    public String datetoArrive;
    public int type;
    public Branch b;
    public String trucktype;
    public WorkerToSend driver;

    public TransportToSend(int id, String date, int type, Branch b, String truck,WorkerToSend d){
        this.id=id;
        this.b=b;
        this.datetoArrive=date;
        this.driver=d;
        this.type=type;
        this.trucktype=truck;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatetoArrive() {
        return datetoArrive;
    }

    public void setDatetoArrive(String datetoArrive) {
        this.datetoArrive = datetoArrive;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Branch getB() {
        return b;
    }

    public void setB(Branch b) {
        this.b = b;
    }

    public String getTrucktype() {
        return trucktype;
    }

    public void setTrucktype(String trucktype) {
        this.trucktype = trucktype;
    }

    public WorkerToSend getDriver() {
        return driver;
    }

    public void setDriver(WorkerToSend driver) {
        this.driver = driver;
    }
}
