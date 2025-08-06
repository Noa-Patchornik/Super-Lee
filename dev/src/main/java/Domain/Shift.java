package Domain;

import java.util.*;

public class Shift {

    /**
     * The class that represent the Shifts objects, creat shifts, set details
     * attrinute: int shiftID - the id of the shift
     * attrinute: Map<Domain.Role, Integer> counterNeededRoles - a map that represent the amount of employees needed in each role
     * attrinute: int shiftType - the type of the shift, 0-morning 1-evening
     * attrinute: String shiftdate - the date of the shift
     * attrinute: Domain.Worker shiftManager - the manager of the shift
     * attrinute: Map<Domain.Role,List<Domain.Worker>> participatesWorkersinShift - all the employees who worked in the shift sort by their role
     * attribute: Branch branch - the branch of the shift
     * * The functions of this class are getters, setters and print the details of the shifts
     */

    private int shiftID;
    private Map<Role, Integer> counterNeededRoles= new HashMap<>();
    private  int shiftType;// 0-morning 1-evening
    private  String shiftdate;
    private Worker shiftManager;
    private Map<Role,List<Worker>> participatesWorkersinShift = new HashMap<>();
    private List<Transport> transports = new ArrayList<>();
    private Branch branch;

    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }

    public void addtranport(Transport trans){
        this.transports.add(trans);
    }

    public List<Transport> getTransports(){
        return transports;
    }

    public void setStoreKeeper(){
        this.counterNeededRoles.put(Role.Store_Keeper,1);
    }
    public Shift(){}

    public Branch getBranch(){
        return branch;
    }

    public Shift(int shiftType,String date, Branch b){
        this.shiftID= GlobalsVar.getGeneralIDCounter();
        this.shiftdate=date;
        this.shiftType=shiftType;
        this.branch=b;
    }

    public Map<Role, Integer> getCounterNeededRoles() {
        return counterNeededRoles;
    }

    public void setParticipatesWorkersinShift(Map<Role, List<Worker>> participatesWorkersinShift) {
        this.participatesWorkersinShift = participatesWorkersinShift;
    }

    public void setShiftManager(Worker shiftManager) {
        this.shiftManager = shiftManager;
    }

    public Map<Role, List<Worker>> getParticipatesWorkersinShift() {
        return participatesWorkersinShift;
    }

    public Worker getShiftManager(){
        return this.shiftManager;
    }

    public int getShiftType() {
        return shiftType;
    }

    public String getShiftdate() {
        return shiftdate;
    }

    public int getShiftID(){
        return shiftID;
    }

    public void setCounterNeededRoles(Map<Role, Integer> counterNeededRoles) {
        this.counterNeededRoles = counterNeededRoles;
    }

    /**
     * the function that print the shift details
     */
    public void PrintMyDetails() {
        System.out.println("Shift Details are:");
        System.out.println("Shift ID : " + this.getShiftID());
        System.out.println("Shift date : " + this.getShiftdate());
        System.out.println("The Shift Branch is : " + this.getBranch());
        System.out.print("Shift type is : ");
        if(this.getShiftType()==0)
            System.out.println("Morning shift");
        else{
            System.out.println("Evening shift");
        }
        if(shiftManager!=null) {
            System.out.println("The manager was : " + this.getShiftManager().getWorkerID());
        }
        if(!counterNeededRoles.isEmpty()) {
            System.out.println("The requirements of the Shift in numbers: ");
            for (Role key : this.getCounterNeededRoles().keySet()) {
                System.out.println("for role " + key + " : needed " + this.getCounterNeededRoles().get(key) + " workers");
            }
            System.out.println("\n");
        }
        if(participatesWorkersinShift!=null && !participatesWorkersinShift.isEmpty()) {
            for (Role r : participatesWorkersinShift.keySet()) {
                System.out.println("as " + r + " role, the following workers participated: ");
                for (int i = 0; i < participatesWorkersinShift.get(r).size(); i++) {
                    (participatesWorkersinShift.get(r).get(i)).PrintMyDetails();
                }
                System.out.println("\n");
            }
        }
        if(this.transports!=null&& !this.transports.isEmpty()){
            for(int i=0; i<transports.size(); i++){
                transports.get(i).Print();
            }
        }
    }

    /**
     * the function that print the counter of the amount employees needed in each role
     */
    public void PrintCounterNeeded(){
        for (Role r : counterNeededRoles.keySet()) {
                System.out.println("as " + r + " role, needs: " + this.counterNeededRoles.get(r) + " workers");
            }
        }

    public Map<String, Integer> getCounterNeededRolesString() {
        Map<String,Integer> dict = new HashMap<>();
        for(Role key : this.counterNeededRoles.keySet()){
            dict.put(String.valueOf(key),this.counterNeededRoles.get(key));
        }
        return dict;
    }
}

