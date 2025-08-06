package Presentation;
import Domain.Branch;
import Domain.Role;

import java.util.List;
import java.util.Map;

public record WorkerModel(int id, String firstname, String lastname, String bankAccount, ContractModel c,
                          List<Role> roles , Map<String, AvailabilityModel> avail, Branch branch) {

        public WorkerModel(int id, String firstname, String lastname,String bankAccount, ContractModel c,
                           List<Role> roles,Branch branch) {
            this(id,firstname,lastname,bankAccount,c,roles,null,branch);
        }
        @Override
        public String toString() {
            return " Worker: " + id + " " + firstname + " " + lastname;
        }

        public void printMyDetails() {
            System.out.println();
            System.out.println();
            System.out.println("My details: " );
            System.out.println("My id: " + id );
            System.out.println("my full name: " + firstname +" " + lastname);
            System.out.println("my bank account: " + bankAccount);
            System.out.println("I work at branch: " + branch);
            System.out.println( "My roles are: ");
            for(int i=0; i<roles.size(); i++){
                System.out.println((roles.get(i)));
            }
            c.printdetails();
        }
    }

