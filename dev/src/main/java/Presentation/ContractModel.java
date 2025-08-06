package Presentation;

public record ContractModel(String startdate, String enddate, double salary, int daysoff) {


    public void printdetails(){
        System.out.println("My contract details are: ");
        System.out.println("The start day of my work is: " + startdate);
        System.out.println("The end date of my work is: " + enddate);
        System.out.println("My salary is: " + salary);
        System.out.println("The amount of days off i have: " + daysoff);
    }
}
