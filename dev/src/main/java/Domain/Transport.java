package Domain;

import java.util.ArrayList;
import java.util.List;

public class Transport {
    private int ID;
    private int type;
    private String dateToArrive;
    //the options of this is B,C,D
    private String Trucktype;
    private Worker driver;
    private Branch branch;

    public Transport(int typeof, String dateof, String trucklicense, Branch b){
        this.branch =b;
        this.ID=GlobalsVar.getGeneralIDCounter();
        this.type=typeof;
        this.dateToArrive=dateof;
        this.Trucktype=trucklicense;
    }

    public int getID(){
        return this.ID;
    }
    public String getDateToArrive(){
        return this.dateToArrive;
    }
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Branch getBranch() {
        return branch;
    }

    /**
     * add a driver to the transport by his license
     * @param driverof - the driver to add to this transport
     * @return true if the driver is assigned, false else
     */
    public boolean setDriver(Worker driverof){
        if(driverof== null){
            System.out.println("Can't assign driver to the transport");
            return false;
        }
        List<Role> roles= driverof.getRole();
        for(int i=0; i<roles.size(); i++){
            Role r=roles.get(i);
            String tmprole= ChangetoString(r);
            if(tmprole!=null && tmprole.compareTo(this.Trucktype)==0){
                this.driver=driverof;
                return true;
            }

        }
        System.out.println("This driver doesn't have the correct license please choose other driver");
        return false;
    }

    /**
     * change the role of the driver to the license
     * @param r - the current role
     * @return - the correct license
     */
    private String ChangetoString(Role r) {
        if(r.compareTo(Role.DriverB)==0){
            return "B";
        }
        else if(r.compareTo(Role.DriverC)==0 ){
            return "C";
        }
        if(r.compareTo( Role.DriverD)==0){
            return "D";
        }
        return null;
    }

    /**
     * get the date and type of the shift and the branch that the transport is supposed to arrive
     * @return
     */
    public List<Object> getShiftToArriveAndTheBranch(){
        List<Object> lst = new ArrayList<>();
        lst.add(this.dateToArrive);
        lst.add(this.type);
        lst.add(this.branch);
        return lst;
    }

    public Worker getDriver(){
        return this.driver;
    }

    public String getTrucktype(){
        return this.Trucktype;
    }

    public void Print() {
        System.out.println("The transport details are:");
        System.out.println("The date of the shift to arrive at is : " + this.dateToArrive);
        System.out.print("The type of shift to arrive at is :");
        if(this.type==0){
            System.out.println("morning");
        }
        else {
            System.out.println("evening");
        }
        System.out.println("The drive of this transport is: " + driver.getWorkerID());
    }
}
