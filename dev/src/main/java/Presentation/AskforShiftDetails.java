package Presentation;

import Domain.*;
import Service.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.*;

public class AskforShiftDetails {
    /**
     * The class that is responsible to get the input from the user about the shifts and transfer
     * the data to the domain layer or return a response to the presentation class
     * attribute: SF - a controller class of the domain layer
     * attribute awd - the class that get the data from the user about the workers
     */

    private ShiftService SS=new ShiftService();
    private ShiftFaced SF = new ShiftFaced();
    private AskforWorkerDetails awd = new AskforWorkerDetails();

    /**
     * the function gets from the HR - Manager the amount of employees he wants in all the possible roles in a specific shift
     * @return - a Map of Domain.Role and the amount the manager chose
     */
    public Map<Role, Integer> chooserolesforshift() {
        List<Role> allposibleroles = GlobalsVar.getAllposibleRoles();
        Map<Role, Integer> dict = new Hashtable<>();
        boolean inmanager=true;
        System.out.println("welcome. you are going to choose how many roles you want for a wanted shift");
        for (int i = 0; i < allposibleroles.size(); i++) {
            if(allposibleroles.get(i)== Role.Manager){
                while(inmanager) {
                    System.out.println("how many " + allposibleroles.get(i) + " do you want?");
                    System.out.println("You must choose at least one Manager for the shift");
                    Scanner sc = new Scanner(System.in);
                    int input = sc.nextInt();
                    if (input != 0) {
                        dict.put(allposibleroles.get(i), input);
                        inmanager=false;
                        break;
                    } else {
                        System.out.println("you must choose at least one Manager, please enter the number again");
                    }
                }
            }
            else {
                System.out.println("how many " + allposibleroles.get(i) + " do you want?");
                Scanner sc = new Scanner(System.in);
                int input = sc.nextInt();
                dict.put(allposibleroles.get(i), input);
            }
        }
        return dict;
    }

    /**
     * The function ask from the user the date range of the week he wants to plan and return it
     * @return - an array of 2 dates, the start of the week and the end of the week
     */
    public String[] askfordaterange() {
        String[] tmpdaterange = new String[2];
//        System.out.println("welcome to planning the following week");
        System.out.println("please enter the start date for the week (format dd/mm/yyyy)");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        tmpdaterange[0] = input;
        System.out.println("please enter the end date for the week (format dd/mm/yyyy)");
        sc = new Scanner(System.in);
        input = sc.nextLine();
        tmpdaterange[1] = input;
        return tmpdaterange;
    }

    /**
     * the function ask the HR - Manager for a plan for the following week, so he needs to enter the number of shifts each day
     * @return - an array of integers that represent the options of the shifts of the week
     */
    public int[] askforplan() {
        int[] planning = new int[GlobalsVar.getNUMOFWORKDAYSATWEEK()];
        System.out.println("welcome for week planning. for each day in a week choose:");
        System.out.println("0- no shifts at all, 1 - just morning shift, 2- just evening shift, 3- both of them");
        Scanner sc;
        int input;
        for (int i = 1; i <= GlobalsVar.getNUMOFWORKDAYSATWEEK(); i++) {
            System.out.println("for day number " + i + " , choose the wanted option");
            sc = new Scanner(System.in);
            input = sc.nextInt();
            planning[i - 1] = input;
        }

        return planning;
    }

    /**
     * the function asks from the user date and return it as string
     */
    public String askforDate() {
        System.out.println("Please enter shift date in the format (dd/mm/yyyy)");
        String date;
        Scanner sc = new Scanner(System.in);
        date = sc.nextLine();
        return date;
    }

    /**
     * the function asks for the type of the shift, morning or evening and return the choice as a number
     * @return - 0 for morning and 1 for evening
     */
    public int askfortypeofShift() {
        System.out.println("Please enter the type of the shift: ");
        System.out.println("Morning- 0, Evening- 1");
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }


    /**
     * the function create new shift and saves it to the database.
     * the function gets from the user all the details for a new shift:
     * date, type, the amount of each role. if the shift is already in the database the system will print a massage
     */
    public void createShift() {
        String date = this.askforDate();
        int type= this.askfortypeofShift();
        String b = this.awd.askforbranch();
        String out = SS.isExistShift(date,type,GlobalsVar.getmyEnumB(b));
        Gson gson = new Gson();
        boolean exist = false;
        Response<Boolean> res = gson.fromJson(out, Response.class);
        if(res.errormsg!=null && !res.errormsg.isEmpty()){
            System.out.println(res.errormsg);
        }
        else if(res.valuetosend!=null){
            if(res.getValuetosend()==true){
                exist = true;
            }
            else {
                exist = false;
            }
        }
        String msg;
        if(!exist){
            Map<Role,Integer> map = this.chooserolesforshift();
            msg = SS.createShift(date,type,GlobalsVar.getmyEnumB(b),map);
            Response<String> r = gson.fromJson(msg, Response.class);
            if(r.errormsg!=null && !r.errormsg.isEmpty()){
                System.out.println(r.errormsg);
            }
            else if(r.valuetosend!=null){
                System.out.println(r.valuetosend);
            }
        }
        else{
            System.out.println("the shift is already in the system, please try again");
        }
    }


    /**
     * the function for planning the week and save it to the database
     */
    public void planshiftsforthefollowingweek()  {
        String[] daterange = this.askfordaterange();
        String b = awd.askforbranch();
        int[] plan = this.askforplan();
        String out = SS.planshiftsforthefollowingweek(daterange, plan, GlobalsVar.getmyEnumB(b));
        Gson gson = new Gson();
        Response r = gson.fromJson(out,Response.class);
        if(r.errormsg!= null && !r.errormsg.isEmpty()){
            System.out.println(r.errormsg);
        }
        else if(r.valuetosend!=null){
            System.out.println("week plan was successfully added to the database");
        }
    }


    public String getshiftplusone(String startDateRange, int i) {
        return SF.getshiftplusone(startDateRange, i);
    }

    /**
     * the function get all the data from the user about new transport to add and add it to a shift
     * the requested data: date of shift, type of shift, branch of shift, the week range of the shift
     * the type of truck, driver with license of this truck
     * the function checks if the shift is in the system, if it is checks if there is a driver with the correct license that
     * is already assigned to the shift, if there is no driver assigned asks from the HR to choose one
     * after this when the driver is choosen or already assigned, needs to check if there is a Storekeeper in the shift already
     * if there is a storeKeeper adds the transport, if not check, if there is an available one and assign it.
     */
    public void addTransportToShift() {
        System.out.println("you need to choose the date,type and branch for the transport to arrive");
        String date= this.askforDate();
        int type = this.askfortypeofShift();
        String b= awd.askforbranch();
        System.out.println("You need to enter the week dates of the shift, the start of the week and the end of the week");
        String[] weekrange= this.askfordaterange();
        //enter the type of the truck
        String truckkind = this.askTrucktype();
        Role r = this.getmyRole(truckkind);
        if(r==null){
            System.out.println("The truck type must be B,C or D. please try again");
            return;
        }
        //checks if the shift exists, if not print error and go back to the menu
        String s = SS.isExistShift(date,type,GlobalsVar.getmyEnumB(b));
        Gson gson = new Gson();
        Response<Boolean> res = gson.fromJson(s, Response.class);
        if(res.errormsg!=null && !res.errormsg.isEmpty()){
            System.out.println(res.errormsg);
        }
        else if(res.valuetosend!=null){
            if(!res.getValuetosend()){
                System.out.println("The requested shift for transport is not exist, please try other");
                return;
            }
        }
        ShiftModel shift=null;
        //checks if the shift was assigned or not
        String out = SS.bringShift(date, type,GlobalsVar.getmyEnumB(b));
        Response<ShiftToSend> res2= gson.fromJson(out,new TypeToken<Response<ShiftToSend>>(){}.getType());
        if(res2.errormsg!=null && !res2.errormsg.isEmpty()){
            System.out.println(res.errormsg);
            return;
        }
        else if(res.valuetosend!=null){
            shift = createShiftModel(res2.valuetosend);
        }
        //if the shift has no workers print error and go back to the menu
        if(shift!=null) {
            //the shift is not assigned yet, need to assign with one driver and one stroeKeeper
            if (shift.participent().isEmpty() || shift.participent() == null) {
                System.out.println("you must have at least one Store_Keeper, and a driver that has the same license");
                //assign the shift with one driver and one storekeeper
                s = SS.Assignworkers(date,type,b,weekrange,shift.counter(),r);
                res = gson.fromJson(s, Response.class);
                if(res.errormsg!=null && !res.errormsg.isEmpty()){
                    System.out.println(res.errormsg);
                    return;
                }
            }
            //the shift has an assigning already, check if there is a driver and storekeeper
            else{
                //checks the StoreKeeper
                if(shift.participent().get(Role.Store_Keeper)==null || shift.participent().get(Role.Store_Keeper).isEmpty()) {
                    System.out.println("you must choose one StoreKeeper to the shift");
                    //assign the storekeeper to the shift
                    s = SS.AssignOneWorker(date, type, b, Role.Store_Keeper, weekrange);
                    res = gson.fromJson(s, Response.class);
                    if (res.errormsg != null && !res.errormsg.isEmpty()) {
                        System.out.println(res.errormsg);
                        return;
                    }
                }
                //checks if there is a driver that has the right license
                if(shift.participent().get(r)==null || shift.participent().get(r).isEmpty()){
                    System.out.println("you must have at least one driver in this shift");
                    //assign the driver to the shift + add it to the transport
                    s = SS.AssignOneWorker(date, type, b, r, weekrange);
                    res = gson.fromJson(s, Response.class);
                    if (res.errormsg != null && !res.errormsg.isEmpty()) {
                        System.out.println(res.errormsg);
                        return;
                    }
                }
            }
            //add the transport to the shift and to DB
            s = SS.addTranporttoShift(date, type, truckkind, GlobalsVar.getmyEnumB(b), r);
            res = gson.fromJson(s, Response.class);
            if(res.errormsg!=null && !res.errormsg.isEmpty()){
                System.out.println(res.errormsg);
                return;
            }
        }

    }


    /**
     * the function change the String into role
     * @param trucktype
     * @return
     */
    public Role getmyRole(String trucktype){
        Role r =null;
        if(trucktype.compareTo("B")==0){
            r=Role.DriverB;
        }
        else if(trucktype.compareTo("C")==0){
            r=Role.DriverC;

        }
        else if(trucktype.compareTo("D")==0){
            r=Role.DriverD;
        }
        else{
            System.out.println("The truck type must be B or C or D");
        }
        return r;
    }


    /**
     * get the truck type from the user, the types are B,C,D as the license of the drivers
     * @return
     */
    public String askTrucktype() {
        System.out.println("Please enter the type of truck you want for this transport:");
        System.out.println("The options are: B, C, D");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    /**
     * the function read a shift from the database
     * @return - the required shift
     */
    public void readshift() {
        String date = this.askforDate();
        int type =this.askfortypeofShift();
        String b=awd.askforbranch();
        //change it to return GSON
        Gson gson = new Gson();
        Response<ShiftToSend> res = new Response<>();
        String out = SS.bringShift(date, type,GlobalsVar.getmyEnumB(b));
        res = gson.fromJson(out,new TypeToken<Response<ShiftToSend>>(){}.getType());
        if(res.errormsg!=null && !res.errormsg.isEmpty()){
            System.out.println(res.errormsg);
        }
        else if(res.valuetosend!=null){
            ShiftModel shift= createShiftModel(res.valuetosend);
            shift.printShiftDetails();
        }
    }

    public ShiftModel createShiftModel(ShiftToSend shift) {
        WorkerModel man = awd.createWorkerModel(shift.manager);
        List<WorkerModel> par = new ArrayList<>();
        Map<Role,List<WorkerModel>> partice = new HashMap<>();
        for(Role r: shift.participent.keySet()) {
            for (int i = 0; i < shift.participent.get(r).size(); i++) {
                WorkerModel w = awd.createWorkerModel(shift.participent.get(r).get(i));
                par.add(w);
            }
            partice.put(r,par);
            par=new ArrayList<>();
        }
        WorkerModel driver=null;
        List<TransportModel> t = new ArrayList<>();
        if(shift.transports!= null && !shift.transports.isEmpty()) {
             driver= awd.createWorkerModel(shift.transports.get(0).driver);
            TransportModel tran = new TransportModel(shift.transports.get(0).id, shift.transports.get(0).datetoArrive,
                    shift.type, shift.b, shift.transports.get(0).trucktype, driver);
            t.add(tran);
        }
        ShiftModel shiftModel = new ShiftModel(shift.id,shift.date,shift.type,shift.b,man,shift.counter,partice,t);
        return shiftModel;
    }


    /**
     * Present the plan for a specific week
     * The function is asking the user the start date of the week he wants to see and print the plan
     * each day and the shifts in this day: no shifts, only morning, only evening, morning and evening
     */
    public void ShowSchedualforweek() {
        Gson gson = new Gson();
        String date = askforDate();
        Branch b =GlobalsVar.getmyEnumB(awd.askforbranch());
        String out = SS.bringAssigningPres(date, b);
        Response<AssigningPres> r = gson.fromJson(out, new TypeToken<Response<AssigningPres>>(){}.getType());
        if (r.errormsg != null && !r.errormsg.isEmpty()) {
            System.out.println(r.errormsg);
            return;
        } else if(r.valuetosend!=null){
            AssigningPres assigningPresweek = r.valuetosend;
            out = SS.updateassignpresforweek(assigningPresweek.getStartDateRange(), assigningPresweek.getPlannedbymanager(),b);
            Response<Boolean> r2= gson.fromJson(out, Response.class);
            if(r2.errormsg!=null && !r2.errormsg.isEmpty()){
                System.out.println(r.errormsg);
                return;
            }
            boolean updated = r2.valuetosend;
            //bring the assign pres again from the db
            out = SS.bringAssigningPres(date,b);
            r = gson.fromJson(out,new TypeToken<Response<AssigningPres>>(){}.getType());
            if (r.errormsg != null && !r.errormsg.isEmpty()) {
                System.out.println(r.errormsg);
                return;
            }
            assigningPresweek =r.valuetosend;
            int[] weekplan = assigningPresweek.getPlannedbymanager();
            if (weekplan.length != 0) {
                System.out.println("The shifts for the week you asked are:\n");
                for (int i = 0; i < weekplan.length; i++) {
                    System.out.println("For day " + (getshiftplusone(assigningPresweek.getStartDateRange(), i)) + " this is the schedule : " + GlobalsVar.getShiftoptions().get((weekplan[i])) + " \n");
                }
                if (updated) {
                    System.out.println(assigningPresweek.getStartDateRange());
                    assigningPresweek.printplan();
                }
            } else {
                System.out.println("The week you asked is not planned yet, please try again");
            }
        }
        else{
            System.out.println(r.errormsg);
        }
    }


    /**
     * the function gets from the HR manager all the data it is needs to assign workers to a requested shift:
     * the date,type,branch and the week of the shift, the amount of workers in each role
     * the function checks if the shift is in the database, if so its assign the workers automatically
     */
    public void assignWorkers() {
        String date = this.askforDate();
        int type = this.askfortypeofShift();
        String b = awd.askforbranch();
        String[] weekrange = askfordaterange();
        Gson gson = new Gson();
        //check if the shift exist in the system before the assigning
        String msg = SS.isExistShift(date,type,GlobalsVar.getmyEnumB(b));
        Response<Boolean> res = gson.fromJson(msg, Response.class);
        boolean out =false;
        if(res.errormsg!=null && !res.errormsg.isEmpty()){
            System.out.println(res.errormsg);
            return;
        }
        else if(res.valuetosend!=null){
            if(res.valuetosend== true){
                out = true;
            }
            else {
                out = false;
            }
        }
        //the shift is not in the system, print error and go back
        if(!out){
            System.out.println("The shift is not in the system, for assign you need to create it");
            return;
        }
        //the shift is in the system, bring it from the DB and checks if the counter is not null or empty
        msg = SS.bringShift(date,type,GlobalsVar.getmyEnumB(b));
        ShiftModel shift = null;
        Response<ShiftToSend> r = gson.fromJson(msg,new TypeToken<Response<ShiftToSend>>(){}.getType());
        if(r.errormsg!=null && !r.errormsg.isEmpty()){
            System.out.println(r.errormsg);
            return;
        }
        //create the shift from the DB as shiftModel to check its counter
        else if(r.valuetosend!=null){
            shift = createShiftModel(r.valuetosend);
        }
        Map<Role, Integer> counter = null;
        //check the counter of shiftModel
        if(shift!=null){
            //the counter is empty
            if(shift.counter()==null || shift.counter().isEmpty()){
                counter = this.chooserolesforshift();
            }
        }
        //need to update shift with the counter if not null + assign workers to the shift
        msg = SS.Assignworkers(date,type,b,weekrange,counter,null);
        Response response = gson.fromJson( msg, Response.class);
        if(response.errormsg!= null && !response.errormsg.isEmpty()){
            System.out.println(response.errormsg);
        }
        else if(response.valuetosend!=null){
            System.out.println("assigning to shift was successful");
        }
    }
}

