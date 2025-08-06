package Domain;



public class Contract {

    /**
     * a class that represent the contract between an employee and Super-Lee
     * attribute: int contractID - the id of the contract
     * attribute: Domain.Worker workerinContract - the id of the worker whose contract belong to him
     * attribute: String startDateRange - the start date of the contract
     * attribute: String endDateRange - the end date of the contract
     * attribute: double HourlySalary - the hourly salary
     * attribute: int offDays - the amount of the off days the employee has
     */
    private int contractID;
    private String startDateRange;
    private String endDateRange;
    private double HourlySalary;
    private int offDays;

    public Contract(String startDateRange, String enddaterange, double HourlySalary){
        this.contractID= GlobalsVar.getGeneralIDCounter();
        this.startDateRange=startDateRange;
        this.endDateRange=enddaterange;
        this.HourlySalary=HourlySalary;
        this.offDays= GlobalsVar.getOffDays();
    }

    public String getEndDateRange(){
        return endDateRange;
    }

    public void setEnddate(String inputstring) {
        this.endDateRange=inputstring;
    }

    public void setHourlySalary(double input) {
        this.HourlySalary=input;
    }

    public double getHourlySalary() {
        return HourlySalary;
    }

    public int getoffdays() {
        return this.offDays;
    }

    public void setOffdays(int input) {
        this.offDays=input;
    }

    /**
     * the function prints all the contract details for the user
     */
    public void PrintMyDetails() {
        System.out.println("My Contract's Details are:");
        System.out.println("start date: " + this.startDateRange);
        System.out.println("end date : " + this.endDateRange);
        System.out.println("my hourly salary is : " + this.HourlySalary);
        System.out.println("the amount of my offdays is: " + this.offDays);
    }

    public String getStartDateRange() {
        return this.startDateRange;
    }
}
