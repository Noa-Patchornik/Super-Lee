package Presentation;

import Domain.Branch;

public record TransportModel(int id, String datetoArrive, int type, Branch b, String trucktype,
                             WorkerModel driver) {

    public void printTransport(){
        System.out.println("The transoprt details are: ");
        System.out.println("ID of transport: " + id);
        System.out.println("The shift to arrive: " + datetoArrive + type);
        System.out.println("The branch to arrive: " + b);
        System.out.println("The truck type and driver: " + trucktype + driver.toString());
    }
}
