package Presentation;

import Domain.Branch;
import Domain.GlobalsVar;
import Domain.Shift;

import java.util.ArrayList;
import java.util.List;

public class AssigningPres {
    /**
     * A class part of the presentation layer, the responsibility is to present to the user the shifts in a specific week
     */
    private String startDateRange;
    private String endDateRange;
    private int AssignID;
    private int[] plannedbymanager;
    private List<Shift> allmyshift= new ArrayList<>();
    private Branch branch;

    public AssigningPres() {}

    public String getStartDateRange() {
        return startDateRange;
    }

    public int[] getPlannedbymanager() {
        return plannedbymanager;
    }

    public void setAllmyshift(Shift shift) {
        this.allmyshift.add(shift);
    }

    public void printplan() {
        System.out.println("The weekly schedule is: ");
        for(int i=0; i<allmyshift.size(); i++){
            System.out.println("The shift details are: ");
            if(allmyshift.get(i)!= null)
                allmyshift.get(i).PrintMyDetails();
        }
    }

    public AssigningPres(String startDateRange, String endDateRange, int[] arr, Branch b){
        this.AssignID= GlobalsVar.getGeneralIDCounter();
        this.startDateRange=startDateRange;
        this.endDateRange=endDateRange;
        this.plannedbymanager=arr;
        this.branch = b;
    }

    public List<Shift> getAllmyshift() {
        return allmyshift;
    }

    public String getEndRange() {
        return endDateRange;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public int getID() {
        return AssignID;
    }

    public String getEndDateRange() {
        return endDateRange;
    }

    public void setEndDateRange(String endDateRange) {
        this.endDateRange = endDateRange;
    }

    public void setStartDateRange(String startDateRange) {
        this.startDateRange = startDateRange;
    }

    public int getAssignID() {
        return AssignID;
    }

    public void setAssignID(int assignID) {
        AssignID = assignID;
    }
}
