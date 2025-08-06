package Presentation;
import Domain.*;
import Service.*;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class AskforWorkerDetails{
    /**
     * The class that is responsible to get the input from the user about the employees and transfer
     * the data to the domain layer or return a response to the presentation class
     * attribute: WF - a controller class of the domain layer
     */

    private WorkerService WS = new WorkerService();
    private static AskforShiftDetails asd = new AskforShiftDetails();

    /**
     * The function ask the user details of a new employee to insert the database, and then send the details to the service
     * layer, if the registration succeeded the msg would be good one, else there would be an error message
     */
    public void WorkerRegistration() {
        System.out.println("You are going to enter a new employee, please enter all the data: ");
        Scanner sc;
        String tmpr;
        int input;
        String tmpstring;
        List<Object> tmpList = new ArrayList<>();
        System.out.println("What is the worker's ID?");
        sc = new Scanner(System.in);
        input = sc.nextInt();
        int id= input;
        if(input<=0){
            System.out.println("Can't be employee with ID 0 or negative, please try again");
            return;
        }
        tmpList.add(input);
        System.out.println("What is the worker's first name?");
        sc = new Scanner(System.in);
        tmpstring = sc.nextLine();
        tmpList.add(tmpstring);
        System.out.println("What is the worker's last name?");
        sc = new Scanner(System.in);
        tmpstring = sc.nextLine();
        tmpList.add(tmpstring);
        System.out.println("What is the worker's Bank account? all numbers format: XXXXXXXXX");
        sc = new Scanner(System.in);
        tmpstring = sc.nextLine();
        tmpList.add(tmpstring);
        tmpr = askfornewRole();
        String tmpb = this.askforbranch();
        if(tmpb==null){
            System.out.println("the branch you choose isn't exist please try again");
            return;
        }
        List<Object> c = this.askforContract(id);
        Gson gson = new Gson();
        String out =  WS.addWorker((int) tmpList.get(0), (String) tmpList.get(1),
                (String) tmpList.get(2),(String) tmpList.get(3), tmpr ,tmpb,c);
        Response msg = gson.fromJson(out, Response.class);
        if(msg.errormsg!= null && !msg.errormsg.isEmpty()){
            System.out.println(msg.errormsg);
        }
        else if(msg.valuetosend != null){
            System.out.println("The worker was added successfully to the system");
        }
    }

    /**
     * the function asks the details of a contract of a new worker and return the list with this details
     */
    public List<Object> askforContract(int id) {
        List<Object> tmplist=new ArrayList<>();
        System.out.println("You need to enter the contract details for the employee.");
        System.out.println("Please enter the date of first day of the employee");
        Scanner sc = new Scanner(System.in);
        String startdate = sc.nextLine();
        System.out.println("Please enter the Hourly Salary of the employee, format: int");
        sc = new Scanner(System.in);
        int salary = sc.nextInt();
        String[] parts = startdate.split("/");
        int tmpdate = Integer.parseInt(parts[2].trim());
        tmpdate +=1;
        String enddate = parts[0] + "/" + parts[1] + "/" + String.valueOf(tmpdate);
        tmplist.add(startdate);
        tmplist.add(enddate);
        tmplist.add(salary);
        return tmplist;
    }

    /**
     * the function asks the user the branch of the employee of shift ahd return the string that represent it
     */
    public String askforbranch() {
        int input=0;
        while (input<1 || input>10){
            System.out.println("Please choose the branch/site (A-J)");
            System.out.println("A=1, B=2, C=3, D=4, E=5, F=6 ,G=7, H=8, I=9, J=10");
            Scanner sc = new Scanner(System.in);
            input = sc.nextInt();
            if (input<1 || input > 10){
                System.out.println("Must enter the brunch for the employee, please choose one of the options");
            }
        }
        return GlobalsVar.getBranchByString(input);
    }

    /**
     * The function asks the id of a worker and return it
     */
    public int askID() {
        System.out.println("What is the worker's ID?");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        return input;
    }

    /**
     * The function asks the user if he wants to add or delete role of an employee.
     * If he wants to add return 1, if he wants to delete return 2, else return 0 as error
     */
    public int addOrChangeRole() {
        System.out.println("Do you want to add/delete the worker role? add - press 1, delete - press 2");
        Scanner sc = new Scanner(System.in);
        int addorchange = sc.nextInt();
        if (addorchange == 1) {
            return 1;
        } else if (addorchange == 2) {
            return 2;
        }
        return 0;
    }

    /**
     * The function that the HR - Manager choose which role to delete from the list of roles an employee has.
     * @return - Domain.Role the one that the manager choose to delete
     */
    public String deleteRole() {
        int tmpstring = 0;
        System.out.println("which role do you want to delete the permissions for the worker?");
        while (true) {
            System.out.println("What is the worker's new role?");
            System.out.println("0 - Cashier, 1- Manager , 2- StoreKeeper, 3 - Delivery Person, 4 - driverB, 5 - driverC, 6 - driverD");
            Scanner sc = new Scanner(System.in);
            tmpstring = sc.nextInt();
            switch (tmpstring) {
                case 0:
                    return "Cashier";
                case 1:
                    return "Manager";
                case 2:
                    return "StoreKeeper";
                case 3:
                    return "Delivery_Person";
                case 4:
                    return "DriverB";
                case 5:
                    return "DriverC";
                case 6:
                    return "DriverD";
                default:
                    System.out.println("You entered incorrect number.");
                    System.out.println("Please choose one of the roles:");
            }
        }
    }

    /**
     * The function ask the user for the new role of the employee and return the string that represent it
     */
    public String askfornewRole() {
        int tmpstring;
        boolean role= true;
        while (role) {
            System.out.println("What is the worker's new role?");
            System.out.println("0 - Cashier, 1- Manager , 2- StoreKeeper, 3 - Delivery Person, 4 - driverB, 5 - driverC, 6 - driverD");
            Scanner sc = new Scanner(System.in);
            tmpstring = sc.nextInt();
            switch (tmpstring) {
                case 0:
                    return "Cashier";
                case 1:
                    return "Manager";
                case 2:
                    return "StoreKeeper";
                case 3:
                    return "Delivery_Person";
                case 4:
                    return "DriverB";
                case 5:
                    return "DriverC";
                case 6:
                    return "DriverD";
                default:
                    System.out.println("You entered incorrect number.");
                    System.out.println("Please choose one of the roles:");
            }
        }
        return null;
    }

    /**
     * The function that gets the constraint from the employees
     * At first the employee needs to set the constraint for the morning shifts, then for the evening shifts
     * @param value - the type of the shift: morning or evening
     * @return - an array of 0/1 that represent the constraint of an employee for a specific week
     */
    public int[] askforconstraintswithshift(String value) {
        int[] arr = new int[GlobalsVar.getNUMOFWORKDAYSATWEEK()];
        int i = 0;
        int tmp;
        while (i < GlobalsVar.getNUMOFWORKDAYSATWEEK()) {
            tmp=i+1;
            System.out.println("Can you work on " + tmp + "day in " + value + " shift? (y/n)");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            switch (input) {
                case "y":
                    arr[i] = 1;
                    i++;
                    break;
                case "n":
                    arr[i] = 0;
                    i++;
                    break;
                default:
                    System.out.println("you enters a wrong value, please choose y/n only");
            }
        }
        return arr;
    }

    /**
     * The function ask for the id of the employee to insert its constraint to the system
     * If the id is in the database, ask for the constraints first for morning shifts, then for evening shifts and update
     * the worker availability for the required week and save it to the database
     */
    public void askforconstraints() {
        Gson gson = new Gson();
        int id = this.askID();
        String msg = WS.isCurr(id);
        Response r = gson.fromJson(msg, Response.class);
        if(r.errormsg != null && !r.errormsg.isEmpty()){
            System.out.println(r.errormsg);
            return;
        }
        String[] date = asd.askfordaterange();
        String startdate = date[0];
        LocalDate today = GlobalsVar.Today;
        msg = WS.CalculateDiffDay(startdate,today);
        r = gson.fromJson(msg, Response.class);
        double diff = 4 ;
        if(r.getErrormsg()!=null && !r.getErrormsg().isEmpty()){
            System.out.println(r.getErrormsg());
        }
        else if(r.getValuetosend()!= null)
             diff=Double.valueOf(String.valueOf(r.getValuetosend()));
        if(diff < 4 && today.getDayOfWeek().getValue() > 3){
            System.out.println("You can't enter now constraint for this week, please try for a week later");
            return;
        }
        if(diff > 5 && today.getDayOfWeek().getValue() <3){
            System.out.println("You can't enter now constraint for this week, please try for a week later");
            return;
        }
        int[] morningconstraints = askforconstraintswithshift("morning");
        int[] eveningconstraints = askforconstraintswithshift("evening");
        int[][] ans = new int[GlobalsVar.getNUMOFWORKDAYSATWEEK()][GlobalsVar.getNUMOFSHIFTSPERDAY()];
        ans[0] = morningconstraints;
        ans[1] = eveningconstraints;
        String out= WS.saveConstarints(id, date, ans);
        r = gson.fromJson(out, Response.class);
        if(r.errormsg != null && !Objects.equals(r.errormsg, "")){
            System.out.println(r.errormsg);
        }
        else if(r.valuetosend != null){
            System.out.println("Successfully add the constraint for the week.");
        }
    }

    /**
     *the function print all the constraint of the given employee in a specific week
     */
    public void printconstraint(){
        int id = askID();
        String date = asd.askforDate();
        Gson gson = new Gson();
        Response<AvailabilityToSend> r;
        String out = WS.Printconstriant(id,date);
        r = gson.fromJson(out, new TypeToken<Response<AvailabilityToSend>>(){}.getType());
        if(r.errormsg!=null && !r.errormsg.isEmpty()){
            System.out.println(r.errormsg);
        }
        else if(r.valuetosend!=null){
            AvailabilityModel av = new AvailabilityModel(r.valuetosend.getId(),r.valuetosend.getStartweek(),r.valuetosend.getEndweek()
            ,r.valuetosend.getAvailability());
            av.printConstraints();
        }
    }

    /**
     * The function get the new details of the employee and send it to the service layer to update the database
     * @return - the employee with the new details
     */
    public void updateWorkerDetails() {
        int id = this.askID();
        System.out.println("welcome. you are going to update " + id + " worker details");
        boolean problem=true;
        Scanner sc;
        int input;
        String inputstring;
        Response msgval;
        Gson gson = new Gson();
        while(problem){
            System.out.println("what would you like to change?, please notice ypu can change only one detail at a time");
            System.out.println("1 - first name, 2- last name, 3- bank account, 0 - exit");
            sc= new Scanner(System.in);
            input = sc.nextInt();
            switch (input){
                case 0:
                    problem=false;
                    break;
                case 1:
                    System.out.println("enter the new first name for the worker:");
                    sc= new Scanner(System.in);
                    inputstring= sc.nextLine();
                    String out= WS.updateFN(id,inputstring);
                    msgval = gson.fromJson( out, Response.class);
                    if(msgval.errormsg!=null && !msgval.errormsg.isEmpty())
                        System.out.println(msgval.errormsg);
                    else if (msgval.valuetosend != null){
                        System.out.println("Successfully change the first name of worker " + id);
                    }
                    break;
                case 2:
                    System.out.println("enter the new last name for the worker:");
                    sc= new Scanner(System.in);
                    inputstring= sc.nextLine();
                    String out1= WS.updateLN(id,inputstring);
                    msgval = gson.fromJson(out1 , Response.class);
                    if(msgval.errormsg!=null && !msgval.errormsg.isEmpty())
                        System.out.println(msgval);
                    else if (msgval.valuetosend!=null){
                        System.out.println("Successfully change the last name of worker " + id);
                    }
                    break;
                case 3:
                    System.out.println("enter the new bank account for the worker: in format XXXXXXXXX, X represent a number between 0-9");
                    sc= new Scanner(System.in);
                    inputstring= sc.nextLine();
                    String out2= WS.updateBA(id,inputstring);
                    msgval = gson.fromJson( out2, Response.class);
                    if(msgval.errormsg!=null && !msgval.errormsg.isEmpty())
                        System.out.println(msgval);
                    else if (msgval.valuetosend!=null){
                        System.out.println("Successfully change the bank account of worker " + id);
                    }
                    break;
                default:
                    System.out.println("please choose only valid numbers between 0-3 (inclusive)");
            }
        }
    }

    /**
     * The function get the new details of the contract of the employee and update it
     */
    public void updateContractDetails() {
        int id = this.askID();
        System.out.println("welcome to changing " + id +" contract details.");
        System.out.println("what detail would you like to change?");
        System.out.println("0 - exit, 1- end date of contract, 2 - global salary, 3 - offdays");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        String inputstring;
        boolean problem=true;
        Response msg;
        Gson gson=new Gson();
        int salary;
        int day;
        String out;
        while(problem){
            switch(input) {
                case 0:
                    problem = false;
                    break;
                case 1:
                    System.out.println("please enter the end date for the planning (format dd/mm/yyyy)");
                    sc = new Scanner(System.in);
                    inputstring = sc.nextLine();
                    out = WS.updateContrcact(id,inputstring, input);
                    msg =gson.fromJson( out, Response.class);
                    if(msg.errormsg!=null && !msg.errormsg.isEmpty())
                        System.out.println(msg);
                    else if(msg.valuetosend!=null){
                        System.out.println("change the end date was successfully");
                    }
                    problem=false;
                    break;
                case 2:
                    System.out.println("please enter the new hourly salary in NIS");
                    sc = new Scanner(System.in);
                    salary = sc.nextInt();
                    out = WS.updateContrcact(id,salary, input);
                    msg =gson.fromJson(out , Response.class);
                    if(msg.errormsg!=null && !msg.errormsg.isEmpty())
                        System.out.println(msg);
                    else if(msg.valuetosend!=null){
                        System.out.println("change the salary was successfully");
                    }
                    problem=false;
                    break;

                case 3:
                System.out.println("please enter the new amount of offdays (you can only set a greater amount)");
                sc = new Scanner(System.in);
                day = sc.nextInt();
                out = WS.updateContrcact(id,day, input);
                msg =gson.fromJson( out , Response.class);
                if(msg.errormsg!=null && !msg.errormsg.isEmpty())
                    System.out.println(msg);
                else if(msg.valuetosend!=null){
                    System.out.println("change the amount of days off was successfully");
                }
                problem=false;
                break;
            }
        }
    }

    /**
     * the function update the day of resignation of an employee and them change it from the list of current employees
     * to the list of previous employees in the database
     */
    public void WorkerFire() {
        Response msg;
        Gson gson = new Gson();
        int id = this.askID();
        System.out.println("Please enter the date of resignation");
        Scanner sc = new Scanner(System.in);
        String date = sc.nextLine();
        String out = WS.fireWorker(id,date);
        msg = gson.fromJson(out, Response.class);
        if(msg.errormsg!= null && !msg.errormsg.isEmpty()){
            System.out.println(msg.errormsg);
        }
        else if(msg.valuetosend != null){
            System.out.println("Fire worker " + id + " was successful");
        }
    }

    /**
     * the function that gets the worker whose permissions needs to be changed and send it to the correct function
     */
    public void updatePermissionsWorker() {
            int id = this.askID();
            //check if the HR wants to add or delete role
            int choice = this.addOrChangeRole();
            String msg;
            Gson gson=new Gson();
            Response res;
            switch (choice) {
                case 0:
                    System.out.println("invalid choice please try again");
                    return;
                //add role to the worker
                case 1:
                    String r = askfornewRole();
                    msg = WS.addNewRole(id, r);
                    res = gson.fromJson(msg, Response.class);
                    if(res.errormsg != null && !res.errormsg.isEmpty()){
                        System.out.println(res.errormsg);
                    }
                    else if(res.valuetosend!=null){
                        System.out.println("add role was successful");
                    }
                    break;
                //delete role from the worker
                //check if the worker has other roles, if not add role to the worker
                case 2:
                    String delr = this.deleteRole();
                    msg = WS.delRole(id, delr);
                    res= gson.fromJson(msg, Response.class);
                    if(res.errormsg != null && !res.errormsg.isEmpty()){
                        System.out.println(res.errormsg);
                    }
                    //needs to add role to the worker
                    else if(res.valuetosend.equals(1)){
                        System.out.println("successfully delete the role of the worker");
                        System.out.println("a worker have to own at lease one role");
                        delr = askfornewRole();
                        msg = WS.addNewRole(id,delr);
                        res = gson.fromJson( msg, Response.class);
                        if(res.errormsg!=null && !res.errormsg.isEmpty()){
                            System.out.println(res.errormsg);
                        }
                        else if(res.valuetosend!=null){
                            System.out.println("adding the new role was succeeded");
                        }
                    }
                    else{
                        System.out.println("delete the role was successful");
                    }
                    break;
            }
        }



    /**
     * the function delete a previous employee from the database without the option to change it or undo it
     */
    public void deleteWorkerfromDB() {
        int id = this.askID();
        Gson gson=new Gson();
        String output =  WS.deleteWorker(id);
        Response msg = gson.fromJson(output, Response.class);
        if(msg.errormsg != null && !msg.errormsg.isEmpty())
            System.out.println(msg.errormsg);
        else if(msg.valuetosend != null){
            System.out.println("Delete worker " + id + " form the database was successful");
        }
    }


    /**
     * the function gets id of an employee and calculate the salary based on the hourly salary and the amount of hours
     * @param tmp - id of an employee
     * @return the salary of the employee
     */
    public void CalculateSalary(int tmp) {

        String out = WS.CalculateSalary(tmp);
        Gson gson = new Gson();
        Response<Double> r= gson.fromJson(out, Response.class);
        if(r.errormsg!=null && !r.errormsg.isEmpty()){
            System.out.println(r.errormsg);
            return;
        }
        System.out.println("the salary is: " + Double.valueOf(r.valuetosend));
    }

    /**
     * the function transfer all the salaries of the employees to a report to export
     */
    public void transferSalary() {
        WS.transferSalary();
    }

    /**
     * the function get the id from the user, checks if the employee is part of the system and send the data to the service layer
     * to try and print it
     */
    public void ShowWorker() {
        int id = this.askID();
        Gson gson=new Gson();
        String out = WS.printWorkerDetails(id);
        Response<WorkerToSend> r = gson.fromJson(out, new TypeToken<Response<WorkerToSend>>(){}.getType());
        if(r.errormsg!=null && !r.errormsg.isEmpty()){
            System.out.println(r.errormsg);
        }
        else if(r.valuetosend != null){
            //creat a contract from the contract in the domain
            WorkerModel w = createWorkerModel(r.valuetosend);
            if(w!=null)
                w.printMyDetails();
        }
    }

    /**
     * the function gets from the user the role and print all the workers with this role in the system
     */
    public void PrintWorkerByRole() {
        Response<List<WorkerToSend>> res = new Response<>();
        Gson gson =new Gson();
        String tmp=askfornewRole();
        List<WorkerModel> workers = new ArrayList<>();
        String out = WS.bringworkersbyenum(GlobalsVar.makeitenum(tmp));
        res = gson.fromJson(out, new TypeToken<Response<List<WorkerToSend>>>(){}.getType());
        if(res.errormsg!=null && !res.errormsg.isEmpty()){
            System.out.println(res.errormsg);
            return;
        }
        else if(res.valuetosend!=null){
            for(int i=0; i<res.valuetosend.size(); i++){
                WorkerModel w = createWorkerModel(res.valuetosend.get(i));
                if(w!=null)
                    workers.add(w);
            }
        }
        for(int i=0; i<workers.size(); i++){
            workers.get(i).printMyDetails();
        }
    }

    /**
     * the function get a WorkerToSend object and change it to WorkerModel to print it
     * @param workerToSend
     * @return
     */
    public WorkerModel createWorkerModel(WorkerToSend workerToSend) {
        if(workerToSend==null){
            return null;
        }
        ContractModel c = new ContractModel(workerToSend.getC().getStartdate(),workerToSend.getC().getEndDate(),
                workerToSend.getC().getSalary(), workerToSend.getC().getDaysoff());
        //creat the worker from the worker that sent from the domain as jason
        WorkerModel w = new WorkerModel(workerToSend.getId(),workerToSend.getFirstname(),workerToSend.getLastname(),
                workerToSend.getBankAccount(),c ,workerToSend.getRoles(),workerToSend.getBranch());
        return w;
    }

    public void ShowPrevWorker() {
        int id = askID();
        Gson gson=new Gson();
        String out = WS.printPervWorker(id);
        Response<WorkerToSend> r = gson.fromJson(out, new TypeToken<Response<WorkerToSend>>(){}.getType());
        if(r.errormsg!=null && !r.errormsg.isEmpty()){
            System.out.println(r.errormsg);
        }
        else if(r.valuetosend != null){
            //creat a contract from the contract in the domain
            WorkerModel w = createWorkerModel(r.valuetosend);

            if(w!=null)
                w.printMyDetails();
        }
    }

    public void PrintWorkerByBranch() {
        Branch b = GlobalsVar.getmyEnumB(askforbranch());
        Response<List<WorkerToSend>> res = new Response<>();
        Gson gson =new Gson();
        List<WorkerModel> workers = new ArrayList<>();
        String out = WS.bringworkersbybranch(b);
        res = gson.fromJson(out, new TypeToken<Response<List<WorkerToSend>>>(){}.getType());
        if(res.errormsg!=null && !res.errormsg.isEmpty()){
            System.out.println(res.errormsg);
            return;
        }
        else if(res.valuetosend!=null){
            for(int i=0; i<res.valuetosend.size(); i++){
                WorkerModel w = createWorkerModel(res.valuetosend.get(i));
                if(w!=null)
                    workers.add(w);
            }
        }
        for(int i=0; i<workers.size(); i++){
            workers.get(i).printMyDetails();
        }
    }
}




