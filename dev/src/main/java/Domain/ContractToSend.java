package Domain;

public class ContractToSend {

    public String startdate;
    public String endDate;
    public double salary;
    public int daysoff;

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getDaysoff() {
        return daysoff;
    }

    public void setDaysoff(int daysoff) {
        this.daysoff = daysoff;
    }

    public ContractToSend(String sd, String ed, double sal, int dof){
        this.startdate=sd;
        this.endDate = ed;
        this.salary =sal;
        this.daysoff = dof;
    }
}
