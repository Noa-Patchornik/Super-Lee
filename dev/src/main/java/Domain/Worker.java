package Domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Worker {
    /**
     * The class that creat the employees objects, set details and return the details
     * attribute: workerID - the ID of the employee
     * attribute: firstname - the first name of the employee
     * attribute: last name - the last name of the employee
     * attribute: bankaccount - a string that represent the bank account
     * attribute: workercontract - a Domain.Contract object that represent the details of the contract between the employee and Super-Lee
     * attribute: counterhourly - a map <Domain.Role,int> to represent the amount of hours the employee worked in each role to calculate salary
     * attribute: mepermissionroles - a list of all the roles and permissions the employee has
     * attribute: salary - the calculated salary of the employee
     * attribute: myconstraintperweek - a Domain.Availability object that represent the constraint of a specific week for the employee
     * attribute: mybranch - the branch of the employee
     * The functions of this class are getters, setters and print the details of the employee
     */
    private int workerID; // unique ID for worker
    private String firstNameWorker;
    private String lastNameWorker;
    private String bankAccount;
    private Contract workerContract;
    private Map<Role, Integer> counterHourly = new HashMap<>();
    private List<Role> myPermissionRoles;
    private double Salary;
    private Map<String,Availability> myConstraintsperWeek = new HashMap<>();
    private Branch MyBranch;


    public void setMyConstraintsperWeek(Map<String,Availability> myConstraintsperWeek, String date) {
        if (myConstraintsperWeek!=null){
            this.myConstraintsperWeek.put(date,myConstraintsperWeek.get(date));
        }
    }


    public Worker(){}

    public void setWorkerID(int workerID) {
        this.workerID = workerID;
    }

    public Worker(int workerid, String firstNameWorker, String lastNameWorker, String Bankaccount, Role role,Branch b , Contract contract){
        this.workerID=workerid;
        this.firstNameWorker=firstNameWorker;
        this.lastNameWorker=lastNameWorker;
        this.bankAccount=Bankaccount;
        this.myPermissionRoles = new ArrayList<>();
        this.myPermissionRoles.add(role);
        this.MyBranch = b;
        this.workerContract = contract;
    }

    public Map<String,Availability> getMyConstraintsperWeek() {
        return myConstraintsperWeek;
    }
    public int getWorkerID(){
        return this.workerID;
    }

    public List<Role> getRole(){
        return myPermissionRoles;
    }

    public void  setRole(List<Role> tmplistrole){
        this.myPermissionRoles = tmplistrole;
    }

    public Map<Role, Integer> getCounterHourly() {
        return counterHourly;
    }

    public double getHourlySalary(){
        return this.getWorkerContract().getHourlySalary();
    }

    public Contract getWorkerContract(){
        return workerContract;
    }

    public void setFirstName(String inputstring) {
        this.firstNameWorker=inputstring;
    }

    public void setLastName(String inputstring) {
        this.lastNameWorker=inputstring;
    }

    public void setBankAccount(String inputstring) {
        this.bankAccount=inputstring;
    }

    public void setContract(Contract tmpcon) {
        this.workerContract=tmpcon;
    }


    public void PrintMyDetails(){
        System.out.println("\n");
        System.out.println("My Details are:");
        System.out.println("My ID: " + this.getWorkerID());
        System.out.println("My Full name: " + this.firstNameWorker +" " + this.lastNameWorker +"  ");
        System.out.println("My Bank account: " + this.bankAccount);
        System.out.println("My permission roles are: ");
        for(int i=0;i<this.getRole().size();i++){
            System.out.println(this.getRole().get(i));
        }
        System.out.println("I work in branch: " + this.MyBranch+"\n\n");

        this.workerContract.PrintMyDetails();
    }

    public Object getSalary() {
        return Salary;
    }

    public void setCounter(Role key, int shiftType) {
        int tmp=0;
        try {
                tmp = this.counterHourly.get(key);
                if (shiftType == 1) {
                    tmp += GlobalsVar.getLENGTHOFEveningSHIFTINHOUES();
                } else {
                    tmp += GlobalsVar.getLENGTHOFMORNINGSHIFTINHOUES();
                }
                this.setCounterHouers(key, tmp);
        }
        catch (Exception e){
            tmp=0;
            if (shiftType == 1) {
                tmp += GlobalsVar.getLENGTHOFEveningSHIFTINHOUES();
            }
            else{
                tmp += GlobalsVar.getLENGTHOFMORNINGSHIFTINHOUES();
            }
            this.setCounterHouers(key,tmp);
        }
    }

    private void setCounterHouers(Role key, int tmp) {
        this.counterHourly.remove(key);
        this.counterHourly.put(key,tmp);
    }

    public String getFirstName() {
        return this.firstNameWorker;
    }

    public String getLastName() {
        return this.lastNameWorker;
    }

    public String getBankAccount() {
        return this.bankAccount;
    }

    public Branch getmyBranch() {
        return MyBranch;
    }
}
