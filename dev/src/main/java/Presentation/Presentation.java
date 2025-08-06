package Presentation;
import Domain.*;
import java.util.Scanner;

public class Presentation {

    /**
     * The controller class of the Presentation.Presentation layer:
     * the menu of the system that is going to be used by the HR Manager and all the employees
     * The responsibility is to present the user the option of the system according to their permissions
     * By the user choices the system convey it to the other functions.
     * attribute: asd - a class part of the presentation layer that is responsible to get details about the shifts
     * attribute: awd - a class part of the presentation layer that is responsible to get details about the employees
     */
    private static boolean flag=false;
    private static AskforWorkerDetails awd = new AskforWorkerDetails();
    private static AskforShiftDetails asd = new AskforShiftDetails();

    /**
     * The first choice of the user is the option to choose which permission he has, so the correct menu would appear
     * after the user chooses one of the options another menu would appear based on the permissions he has
     */
    public static void Login() throws Exception {
        System.out.println("Welcome to SUPER-LEE HR Managing system");
        //read the configuration file with all the global variables
//        ReadData2System.readConfigurtionFile(null);
//        ReadData2System.readWorkersData(null);
//          ReadData2System.readWorkersRolesData(null);
//          ReadData2System.readPrevWorkersData();
//          ReadData2System.readShifts(null);
//          ReadData2System.readAvailability(null);
          System.out.println("loading all data to the system\n\n");
        int input =-1;
        while(input!=0){
            System.out.println("if you are the HR - Manager please press 1");
            System.out.println("else please press 2");
            System.out.println("if you want to exit, please press 0");
            Scanner sc = new Scanner(System.in);
            String tmp = sc.nextLine();
            try {
                input = Integer.valueOf(tmp);
            }
            catch (Exception e){
                System.out.println("You need to enter a number");
            }
            switch(input){
                case 0:
                    System.out.println("See you next time, Bye Bye");
                    return;
                case 1:
                    HRManager();
                    break;
                case 2:
                    RegularWorker();
                    break;
                default:
                    System.out.println("invalid choose, please press 0-2 inclusive");
                    
            }

        }
    }

    /** The menu for all the workers. They have the option to see their details,
     *אה salary, and insert constraint for shifts to the following week
     */
    private static void RegularWorker() throws Exception {
        boolean problem=true;
//      if(!flag) {
//          flag = true;
//          ReadData2System.readWorkersData(null);
//          ReadData2System.readWorkersRolesData(null);
//          ReadData2System.readPrevWorkersData();
//          ReadData2System.readShifts(null);
//          ReadData2System.readAvailability(null);
//          System.out.println("loading all data to the system\n\n");
//        }
        while(problem){
            System.out.println("for exit, please press 0");
            System.out.println("1- for fill your constraints of the following week");
            System.out.println("2- for check out your salary");
            System.out.println("3- to view all your details in the system include the contract");
            System.out.println("4- to see all your constraints for the following week");
            Scanner sc = new Scanner(System.in);
            int input=sc.nextInt();
            switch (input){
                case 0:
                    System.out.println("Bye Bye, see you next time");
                    return;
                case 1:
                    awd.askforconstraints();
                    System.out.println("thank you, filling completed");
                    break;
                case 2:
                    awd.CalculateSalary(awd.askID());
                    System.out.println("returning to menu\n");
                    break;
                case 3:
                    awd.ShowWorker();
                    System.out.println("returning to menu\n");
                    break;
                case 4:
                    awd.printconstraint();
                    System.out.println("returning to menu\n");
                    break;
                default:
                    System.out.println("invalid choice, please press 0-3 inclusive");
            }
        }
    }

    /**
     * If the user is HR - Manager he needs to enter a password to validate the permissions.
     */
    public static void HRManager() throws Exception {
        int input=-1;
        Scanner sc;
        int ans = GlobalsVar.getSystemPassword();
        System.out.println("this system is dedicated for the HR MANAGER only, please enter the password:");
        while(input !=ans) {
            sc = new Scanner(System.in);
            input = sc.nextInt();
            //get the password from the manager to check if it has the permissions to enter the system and change it
            if(input!= GlobalsVar.getSystemPassword()) {
                System.out.println("wrong password.");
                System.out.println("please press 1 to try again");
                System.out.println("please press 0 to exit");
                sc = new Scanner(System.in);
                input = sc.nextInt();
            }
            switch (input){
                case 0:
                    System.out.println("Bye Bye. see you next time\n");
                    return;
                case 1:
                    System.out.println("please enter the password:");
                    sc = new Scanner(System.in);
                    input = sc.nextInt();
                    break;
                default:
                    if(input!=1 && input!= GlobalsVar.getSystemPassword()){
                        System.out.println("invalid number, please choose between 0-1 inclusive");
                    }
            }
        }

        System.out.println("\n\n");
        System.out.println("Login was successful");
        //maybe with the database don't need to ask about booting the system cause there is data already
        /**
         * after the validation the HR - Manager has the option to choose to boot the system with data or to work with an empty database
         */
//        Boolean boot=true;
//        while(boot) {
//            System.out.println("Would you like to boot the system with records to DB? press 1");
//            System.out.println("in any other case press 2");
//            sc=new Scanner(System.in);
//            input=sc.nextInt();
//            switch (input) {
//                case 1:
//                    if(!flag) {
//                        flag = true;
//                        ReadData2System.readWorkersData(null);
//                        ReadData2System.readWorkersRolesData(null);
//                        ReadData2System.readPrevWorkersData();
//                        ReadData2System.readShifts(null);
//                        ReadData2System.readAvailability(null);
//                        System.out.println("loading all data to the system\n\n");
//                    }
//                    boot = false;
//                    break;
//                case 2:
//                    boot = false;
//                    break;
//                default:
//                    System.out.println("you pressed invalid choice please press one of the options");
//            }
//        }
        /**
         * The main menu for the HR - Manager to use the system. Each option would present another menu base on the topic
         * the HR - Manager wants to deal with.
         */
        input = -1;
        while (input != 0) {
            System.out.println("welcome to the System. Please choose your wanted actions:");
            System.out.println("0 - exit");
            System.out.println("1 - Managing human resources (registration,resignation or updating)");
            System.out.println("2 - Salaries");
            System.out.println("3 - Shifts (creation, assigning employees, view details on the assigning)");
            System.out.println("4 - Plan the following week (choose the shifts per day)");
            System.out.println("5 - read DB info");
            //add the option to change some of the Global Variables
            sc = new Scanner(System.in);
            input = sc.nextInt();
            switch (input) {
                case 0:
                    System.out.println("Bye Bye! see you next time");
                    break;
                case 1:
                    Presentation.MangingHROption1();
                    break;
                case 2:
                    Presentation.Salaries2();
                    break;
                case 3:
                    Presentation.Assigning3();
                    break;
                case 4:
                    Presentation.PlanaWeek4();
                    break;
                case 5:
                    Presentation.readDBinfo5();
                    break;

                default:
                    System.out.println("please choose a valid number 0-6 inclusive");
            }
        }
    }


    /**
     * The menu that control all the human resources: update, add, delete
     */
    public static void MangingHROption1() {
        int input = -1;
        while (input != 0) {
            System.out.println("what would you like to do?");
            System.out.println("0 - return to menu");
            System.out.println("1 - Add a new employee");
            System.out.println("2 - fire an existing employee");
            System.out.println("3 - update personal details upon given employee");
            System.out.println("4 - update contract details upon given employee");
            System.out.println("5 - update employee permission for shift");
            System.out.println("6 - delete an employee fired record from DB");
            Scanner sc = new Scanner(System.in);
            input = sc.nextInt();
            switch (input) {
                case 0:
                    System.out.println("returning to menu\n");
                    break;
                case 1:
                    awd.WorkerRegistration();
                    System.out.println("returning to menu\n");
                    break;
                case 2:
                    awd.WorkerFire();
                    System.out.println("returning to menu\n");
                    break;
                case 3:
                    awd.updateWorkerDetails();
                    System.out.println("returning to menu\n");
                    break;
                case 4:
                    awd.updateContractDetails();
                    System.out.println("returning to menu\n");
                    break;
                case 5:
                    awd.updatePermissionsWorker();
                    System.out.println("returning to menu\n");
                    break;
                case 6:
                    awd.deleteWorkerfromDB();
                    System.out.println("returning to menu\n");
                    break;
                default:
                    System.out.println("please choose a valid number 0-6 inclusive");
            }
        }
    }

    /**
     * The menu that control the salaries: calculate, present reports, export the report of the salaries
     */
    public static void Salaries2() {
        int input = -1;
        int tmp;
        double d;
        while (input != 0) {
            System.out.println("what would you like to do?");
            System.out.println("0 - return to menu");
            System.out.println("1 - Calculate a salary to an given employee");
            System.out.println("2 - make a report of all employee's salaries");
            Scanner sc = new Scanner(System.in);
            input = sc.nextInt();
            switch (input) {
                case 0:
                    System.out.println("returning to menu\n");
                    break;
                case 1:
                    awd.CalculateSalary(awd.askID());
                    System.out.println("returning to menu\n");
                    break;
                case 2:
                    System.out.println("making an external report named: allWorkersSalaries");
                    awd.transferSalary();
                    System.out.println("returning to menu\n");
                    break;
                default:
                    System.out.println("please choose a valid number 0-3 inclusive");
            }

        }
    }

    /**
     * The menu that control the shift assigning, updates, creating, deleting, presenting
     */
    public static void Assigning3() {
        int input = -1;
        while (input != 0) {
            System.out.println("what would you like to do?");
            System.out.println("0 - return to menu");
            System.out.println("1 - create a new shift");
            System.out.println("2 - Assigning workers to shift");
            System.out.println("3 - show existing shift details");
            System.out.println("4- add new transport to a shift");
            Scanner sc = new Scanner(System.in);
            input = sc.nextInt();
            switch (input) {
                case 0:
                    System.out.println("returning to menu\n");
                    break;
                case 1:
                    asd.createShift();
                    System.out.println("returning to menu\n");
                    break;
                case 2:
                    asd.assignWorkers();
                    System.out.println("returning to menu\n");
                    break;
                case 3:
                    asd.readshift();
                    System.out.println("returning to menu\n");
                    break;
                case 4:
                    asd.addTransportToShift();
                    System.out.println("returning to menu\n");
                    break;
            }
        }
    }

    /**
     * The menu that control the week plan, making a shift plan and presenting it
     */
    public static void PlanaWeek4() {
        int input = -1;
        while (input != 0) {
            System.out.println("what would you like to do?");
            System.out.println("0 - return to menu");
            System.out.println("1 - plan a week at a specific branch");
            System.out.println("2 - show a week scheduling");
            Scanner sc = new Scanner(System.in);
            input = sc.nextInt();
            switch (input) {
                case 0:
                    System.out.println("returning to menu\n");
                    break;
                case 1:
                    asd.planshiftsforthefollowingweek();
                    System.out.println("returning to menu\n");
                    break;
                case 2:
                    asd.ShowSchedualforweek();
                    System.out.println("returning to menu\n");
                    break;
                default:
                    System.out.println("please choose a valid number 0-? inclusive");
                }

            }
        }

    /**
     * The menu that present all the data that require from the Database
     */
    public static void readDBinfo5()  {
        int input = -1;
        while (input != 0) {
            System.out.println("what would you like to do?");
            System.out.println("0 - return to menu");
            System.out.println("1 - read a given employee details");
            System.out.println("2 - read a given shift details");
            System.out.println("3 - read all employees with a given role");
            System.out.println("4 - read a previous given employee details");
            System.out.println("5 - read all employees from the same branch");
            Scanner sc = new Scanner(System.in);
            input = sc.nextInt();
            switch (input) {
                case 0:
                    System.out.println("returning to menu\n");
                    break;
                case 1:
                    awd.ShowWorker();
                    System.out.println("returning to menu\n");
                    break;
                case 2:
                    asd.readshift();
                    System.out.println("returning to menu\n");
                    break;
                case 3:
                    awd.PrintWorkerByRole();
                    System.out.println("returning to menu\n");
                    break;
                case 4:
                    awd.ShowPrevWorker();
                    System.out.println("returning to menu.\n");
                    break;
                case 5:
                    awd.PrintWorkerByBranch();
                    System.out.println("returning to menu.\n");
                    break;
                default:
                    System.out.println("please choose a valid number 0-5 inclusive");

            }
        }

    }
}