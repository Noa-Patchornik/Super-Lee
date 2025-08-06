package Presentation;

import Domain.GlobalsVar;

public record AvailabilityModel(int id, String startweek, String endweek, int[][] availability) {

    public void printConstraints(){
        System.out.println("The constraints of the week are: ");
        System.out.println("The dates of the week: form " + startweek + " to " + endweek);
        System.out.println("For morning shifts: ");
        int num;
        for (int j = 0; j < GlobalsVar.getNUMOFWORKDAYSATWEEK(); j++) {
            if (availability[0][j] == 1) {
                num=j+1;
                System.out.println("Can work at day number " + num + " in morning shift");
            }
        }
        System.out.println("For evening shifts: ");
        for (int j = 0; j <  GlobalsVar.getNUMOFWORKDAYSATWEEK(); j++) {
            if (availability[1][j] == 1) {
                num=j+1;
                System.out.println("Can work at day number " + num + " in evening shift");
            }
        }
    }
}
