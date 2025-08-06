package Presentation;

import Domain.Branch;
import Domain.Role;
import java.util.List;
import java.util.Map;

public record ShiftModel(int id, String date, int type, Branch b, WorkerModel manager, Map<Role,Integer> counter,
                         Map<Role,List<WorkerModel>> participent, List<TransportModel> transports) {

    public ShiftModel(int id, String date, int type, Branch b){
        this(id,date,type,b,null,null,null,null);
    }

    public void printShiftDetails(){
        System.out.println("My details are: ");
        System.out.println("Shift ID : " + id);
        System.out.println("Shift date : " + date);
        System.out.print("Shift type is : ");
        if(type==0)
            System.out.println("Morning shift");
        else{
            System.out.println("Evening shift");
        }
        System.out.println("The Shift Branch is : " + b);
        //print the manager details
        if(manager!=null) {
            System.out.println("The manager is : ");
            manager.printMyDetails();
        }
        //print the counter ot requirements
        if(!counter.isEmpty() && counter!=null && counter.get(Role.Cashier)!=-1) {
            System.out.println("The requirements of the Shift in numbers: ");
            for (Role key : counter.keySet()) {
                System.out.println("for role " + key + " : needed " + counter.get(key) + " workers");
            }
            System.out.println("\n");
        }
        //print the workers in the shift
        if(participent!=null && !participent.isEmpty()) {
            System.out.println("All the workers at this shift are: ");
            for(Role r: participent.keySet()) {
                for (int i = 0; i < participent.get(r).size(); i++) {
                    participent.get(r).get(i).printMyDetails();
                }
            }
            System.out.println("\n");
        }
        //print the transportations of the shift
        if(this.transports!=null && !this.transports.isEmpty()){
            for(int i=0; i<transports.size(); i++){
                transports.get(i).printTransport();
            }
        }
    }

    /**
     * in case the need is only to the counter of requirements to be printed
     */
    public void PrintCounterNeeded(){
        for (Role r : counter.keySet()) {
                System.out.println("as " + r + " role, needs: " + this.counter.get(r) + " workers");
            }
        }
}
