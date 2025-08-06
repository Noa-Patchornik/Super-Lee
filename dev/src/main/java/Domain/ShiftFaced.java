package Domain;

import Presentation.AssigningPres;

import java.util.*;

public class ShiftFaced {
    /**
     * The class controller of the Domain.Shift class in the domain layer
     * The resbonsebility of the class is to connect between the presentation layer to the database layer, get the data from
     * the database, update the objects, and send it to the function who ask for it
     * attribute: dboper - the instance of the database pointer so all the changes would be saved to the database
     * attribute: WF - an instance of Workerfaced to connect between the controllers
     */

    private DBoperations dbOper = DBoperations.GetDBInstance();

    //--load data from txt file--//
    /**
     * the function load the shifts data to the database
     * @param readPrevShiftData - the previous shift to insert to the database
     */
    public boolean loadDataPrevshifts(List<Shift> readPrevShiftData) throws Exception{
        for(int i=0;i<readPrevShiftData.size();i++) {
            dbOper.savetoHistory(readPrevShiftData.get(i));
        }
        return true;
    }
    //--end of the loading data--//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--add functions--//

    /**
     * creat new shift and saves it to the database
     * @param date - shift date
     * @param type - type of shift
     * @param b - branch of shift
     * @param map - the map of the needed roles and the amount from each other
     */
    public void createShift(String date, int type, Branch b,Map<Role,Integer> map) throws Exception {
        if (map.isEmpty() || this.isExistShift(date,type,b)){
            throw new Exception();
        }
        if(date.compareTo("00/00/0000")==0){
            throw new Exception();
        }
        Shift s = new Shift(type,date,b);
        s.setCounterNeededRoles(map);
        boolean tmp = dbOper.savetoHistory(s);
        if(!tmp){
            throw new Exception();
        }
    }

    /**
     * the function that saves the week plan to the database
     * @param daterange - the start date and end date of the week
     * @param planning - the amount of shifts each day
     */
    public void planshiftsforthefollowingweek(String[] daterange, int[] planning,Branch b) throws Exception {
        if(this.isvalid(daterange[0]) && this.isvalid(daterange[1])) {
            if (dbOper.calculateDifferenceInDays(daterange[1], daterange[0]) == 6) {
                AssigningPres assigningPres = new AssigningPres(daterange[0], daterange[1], planning, b);
                dbOper.saveassign(assigningPres);
                return;
            }
            else{
                throw new Exception("not valid range of week");
            }
        }
        throw new Exception("The date range is not valid, needs to be date range of a week, please try again");
    }


    //--end of addition functions--//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--update functions--//


    /**
     * update the weekly assigning to present it and return true if the update succeeded
     */
    public boolean updateassignpres(String startDateRange, int[] planbymanager,Branch b) throws Exception {
        AssigningPres assigningPres = dbOper.bringAssigningPres(startDateRange,b);
        List<Shift> shifts = dbOper.getShiftHistory();
        if(assigningPres== null || !assigningPres.getAllmyshift().isEmpty()){
            throw new Exception("the week requested is not in the database please try again");
        }
        else{
            for(int i=0; i<shifts.size(); i++){
                int dif= dbOper.calculateDifferenceInDays(shifts.get(i).getShiftdate(),assigningPres.getStartDateRange());
                if((dif < 7) && shifts.get(i).getBranch().compareTo(b)==0){
                    int dayplan = planbymanager[dif];
                    switch (dayplan){
                        case 0:
                            break;
                        case 1:
                            if(shifts.get(i).getShiftType()==0)
                                assigningPres.setAllmyshift(shifts.get(i));
                            break;
                        case 2:
                            if(shifts.get(i).getShiftType()==1)
                                assigningPres.setAllmyshift(shifts.get(i));
                            break;
                        case 3:
                            assigningPres.setAllmyshift(shifts.get(i));
                            break;
                    }
                }
            }
            dbOper.deleteassingfromDB(assigningPres);
            dbOper.saveassign(assigningPres);
            return true;
        }
    }

    /**
     * create transport, add to it a driver, add the transport to the relevant shift and make sure there is at least 1 storekeeper
     * @param date - the date that the transport should arrive
     * @param type - the type of the transport to arrive (morning/evening)
     * @param truckkind - the license of the truck for the driver
     */
    public boolean addTranporttoShift(String date, int type, String truckkind, Branch b, Role r) throws Exception {
        //when the program gets here there is a driver and a storekeeper in the counter and in the participants of the shift

        //bring the shift from the database
        Shift shift = dbOper.bringShiftbyDatenType(date,type, b);
        if(shift==null){
            throw new Exception("The shift is not created yet, please create it and then try again");
        }
        if(shift.getParticipatesWorkersinShift().get(Role.Store_Keeper) == null ||
                shift.getParticipatesWorkersinShift().get(Role.Store_Keeper).isEmpty()){
            throw new Exception("Can't add transport to a shift with no Store_Keeper");
        }
        if(shift.getParticipatesWorkersinShift().get(r) == null || shift.getParticipatesWorkersinShift().get(r).isEmpty()){
            throw new Exception("Can't add transport to a shift with no driver");
        }
        //need to: 1- create a transport and add the driver to it
        //2- add the transport to the shift
        //3- save everything to the database

        //create the transport
        Transport tran = new Transport(type,date,truckkind,b);
        //add the driver
        Map<Role,List<Worker>> par = shift.getParticipatesWorkersinShift();
        tran.setDriver(par.get(r).get(0));

        //add the transport to the shift
        shift.addtranport(tran);
        //save the shift to the DB
        dbOper.deleteShiftfromDB(shift.getShiftType(),shift.getShiftdate(),String.valueOf(shift.getBranch()));
        dbOper.deleteParticipating(shift.getShiftdate(),shift.getShiftType(), String.valueOf(shift.getBranch()));
        dbOper.savetoHistory(shift);
        //save the transport to the DB
        //dbOper.deleteTransport(shift.getShiftID());
        dbOper.insertTransport(tran.getID(),date,String.valueOf(b),type,tran.getDriver().getWorkerID(),truckkind);
        return true;
    }

    //--end of uptadting functions--//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--delete functions--//

    /**
     * remove the shift from the database
     * @param
     */
    public void removeshiftfrom(int type,String date, Branch b) throws Exception{
        dbOper.deleteShiftfromDB(type,date,String.valueOf(b));
    }

    //--end of deleting functions--//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //--other functions--//

    private boolean isvalid(String s) {
        if(s.compareTo("00/00/0000")==0){
            return false;
        }
        String[] parts = s.split("/");
        int day = Integer.parseInt(parts[0].trim());
        int month = Integer.parseInt(parts[1].trim());
        int year = Integer.parseInt(parts[2].trim());
        if(day<1 || day>31){
            return false;
        }
        if(month<1 || month>12){
            return false;
        }
        if(year<2023){
            return false;
        }
        return true;
    }

    /**
     * the function gets specific shift and the amount of each role and return the list of employees who can work in the shift
     * @param dateofshift - the date of the shift
     * @param type - the type of the shift ( 0- morning, 1- evening)
     * @param map - the map that represent the amount of each role
     * @return - a map of the employees who can work in the shift based on their constraints and the shift in a list
     */
    public Map<Role,List<Worker>> Workerswhocanworkatshift(String dateofshift, int type, Map<Role,Integer> map,
                                                           Branch b,String[] weekrange) throws Exception{
        List<Worker> workerwhocanwork = new ArrayList<>();
        //bring the shift from the DB
        Shift shifttoassign = dbOper.bringShiftbyDatenType(dateofshift, type,b);
        if(shifttoassign==null){
            throw new Exception("Shift not in the DB please create it and try again");
        }
        //if the shift is in the database
        if (shifttoassign != null) {
            //if the counter of the shift is empty set the new counter
            if (shifttoassign.getCounterNeededRoles() == null || shifttoassign.getCounterNeededRoles().isEmpty()||
            shifttoassign.getCounterNeededRoles().get(Role.Cashier)==-1) {
                shifttoassign.setCounterNeededRoles(map);
                //save the shift to the DB after the update
                dbOper.deleteShiftfromDB(type,dateofshift,String.valueOf(b));
                if(shifttoassign.getParticipatesWorkersinShift()!=null && !shifttoassign.getParticipatesWorkersinShift().isEmpty())
                    dbOper.deleteParticipating(shifttoassign.getShiftdate(),shifttoassign.getShiftType(), String.valueOf(shifttoassign.getBranch()));
                dbOper.savetoHistory(shifttoassign);
            }
            //bring from the DB all the workers who can work at this shift and belong to this branch
            workerwhocanwork = dbOper.bringworkersbyshift(shifttoassign, weekrange,b);
        }

        //makes a map of available workers by role - Role: list of workers
        Map<Role,List<Worker>> roleListMap=new HashMap<>();
        for(int i=0;i<workerwhocanwork.size();i++){
            for(int k=0;k<workerwhocanwork.get(i).getRole().size();k++){
                List<Worker> tmp = roleListMap.get(workerwhocanwork.get(i).getRole().get(k));
                if(tmp == null){
                    tmp = new ArrayList<>();
                    roleListMap.put(workerwhocanwork.get(i).getRole().get(k),tmp);
                }
                tmp.add(workerwhocanwork.get(i));
            }
        }
        //return the map of workers
        return roleListMap;
    }


    /**
     * the function that assign employees to shift
     * @param weekrange - the dates of the week that the shift is in
     * @param mapcount - the amount of workers needed in each role
     * @return - the employee who was assigned to work in the shift
     */

    public boolean Assignworkers(String date, int type, String b, String[] weekrange , Map<Role,Integer> mapcount,Role driver) throws Exception {

        Branch branch = GlobalsVar.getmyEnumB(b);

        //all the workers who belong to this branch and can work at this shift sorted by their role
        Map<Role,List<Worker>> workerswhocanwork = Workerswhocanworkatshift(date,type,mapcount,branch,weekrange);
        int count;
        List<Worker> tmplist = new ArrayList<>();
        Shift shift = dbOper.bringShiftbyDatenType(date,type,branch);
        if(shift==null){
            throw new Exception("The shift is not in the DB, please create it and then try to assign workers");
        }
        //if driver != null it means that need at least one storekeeper and one driver with the same role
        if(driver!= null){
            //check if there is a storekeeper in the counter
            if(shift.getCounterNeededRoles().get(Role.Store_Keeper)==null || shift.getCounterNeededRoles().get(Role.Store_Keeper)==0 ){
                shift.setStoreKeeper();
            }
            //check if there is a driver in the counter
            if(shift.getCounterNeededRoles().get(driver)==null || shift.getCounterNeededRoles().get(driver)==0 ){
                shift.getCounterNeededRoles().put(driver,1);
            }
        }
        Map<Role,List<Worker>> partice= new HashMap<>();
        //go through the counter of each role and assign workers to the shift based on the amount
        for(Role role : shift.getCounterNeededRoles().keySet()){
            count = shift.getCounterNeededRoles().get(role);
            //while the counter is bigger than 0 add workers at the same role to the list, decrease count by 1
            while(count-1>=0){
                tmplist.add(workerswhocanwork.get(role).get(count-1));
                count--;
            }
            //add the list to the map of the participants of the shift
            if(!tmplist.isEmpty()) {
                partice.put(role, tmplist);
                tmplist = new ArrayList<>();
            }
        }
        //add the map to the shift
        shift.setParticipatesWorkersinShift(partice);
        //set the manager of the shift
        shift.setShiftManager(shift.getParticipatesWorkersinShift().get(Role.Manager).get(0));
        //update the salary of the workers
        setcounterforsalary(shift,partice);
        //update the shift in the database
        dbOper.deleteShiftfromDB(type,date,b);
        //dbOper.deleteParticipating(shift.getShiftID());
        dbOper.savetoHistory(shift);
        return true;
    }


    /**
     * the function that return the weekly plan by the required start date
     * @param askforDate - the start date of the week
     * @return - the weekly plan
     */
    public AssigningPres bringAssigningPres(String askforDate, Branch b) throws Exception{
        return dbOper.bringAssigningPres(askforDate,b);
    }



    /**
     * the function that add to the employees from the shift the amount of hours they worked in the shift
     * @param shifttoassign - the shift
     * @param assign - the employees who worked in the shift who needs to add the hours to
     */
    public void setcounterforsalary(Shift shifttoassign, Map<Role, List<Worker>> assign) throws Exception {
        Map<Role,Integer> salary;
        int counter=0;
        for (Role key : assign.keySet()) {
            List<Worker> workers = assign.get(key);
            for(int i=0;i< workers.size();i++){
                workers.get(i).setCounter(key,shifttoassign.getShiftType());
                salary=workers.get(i).getCounterHourly();
                for(Role r: salary.keySet()){
                    counter+=salary.get(r);
                }
                dbOper.updateSalary(workers.get(i).getWorkerID(),counter*workers.get(i).getWorkerContract().getHourlySalary());
            }
        }
    }

    /**
     * the function checks in the database if the shift is already exist
     * @param date - the date
     * @param type - the type
     * @return - true if the shift is in the database, false else
     */
    public boolean isExistShift(String date, int type,Branch b) throws Exception {
        Shift tmp= dbOper.bringShiftbyDatenType(date,type, b);
        if (tmp!=null) {
            return true;
        }
        return false;
    }


    /**
     * add to the date of the shift the number i to print it
     * @param startDateRange
     * @param i
     * @return
     */
    public String getshiftplusone(String startDateRange, int i) {
        String[] parts = startDateRange.split("/");
        int date = 0;
        date = Integer.parseInt(parts[0].trim());
        date += i;
        String ans = String.valueOf(date);
        ans = ans +"/"+parts[1]+ "/" + parts[2];
        return ans;
    }

    public ShiftToSend printShift(String date, int type, Branch branch) throws Exception {
        Shift shift = dbOper.bringShiftbyDatenType(date, type, branch);
        if(shift!=null){
            ShiftToSend s = makeShiftToSend(shift);
            return s;
        }
        return null;
    }

    private ShiftToSend makeShiftToSend(Shift shift) {
        List<WorkerToSend> pars = new ArrayList<>();
        Map<Role,List<Worker>> map;
        Map<Role,List<WorkerToSend>> map2= new HashMap<>();
        List<Worker> tmplist = new ArrayList<>();
        WorkerToSend w=null;
        //if the shift wasn't assigned put null in the participants map
        if(shift.getParticipatesWorkersinShift()==null || shift.getParticipatesWorkersinShift().isEmpty()){
            map=null;
        }
        //if the shift was assigned put all the participants as workersToSend in a map
        else{
            map = shift.getParticipatesWorkersinShift();
            //go through all the participants, change them to workerToSend and put in the map again
            for(Role r : map.keySet()){
                tmplist = map.get(r);
                for(int i=0; i<tmplist.size(); i++){
                    w = dbOper.createWorkerToSend(tmplist.get(i));
                    pars.add(w);
                }
                map2.put(r,pars);
                pars=new ArrayList<>();
            }
        }
        if(shift.getShiftManager()!=null) {
             w= dbOper.createWorkerToSend(shift.getShiftManager());
        }
        TransportToSend t=null;
        List<TransportToSend> tran = new ArrayList<>();
        //if there are Transport in the shift make them as TransportToSend and add to the shift and send
        if(shift.getTransports()!=null && !shift.getTransports().isEmpty()){
            WorkerToSend d = dbOper.createWorkerToSend(shift.getTransports().get(0).getDriver());
            t = new TransportToSend(shift.getTransports().get(0).getID(),shift.getShiftdate(),shift.getShiftType(),
                    shift.getBranch(),shift.getTransports().get(0).getTrucktype(),d);
            tran.add(t);
        }
        return new ShiftToSend(shift.getShiftID(),shift.getShiftdate(),shift.getShiftType(),shift.getBranch(),
                w,shift.getCounterNeededRoles(),map2,tran);
    }

    /**
     * the function assign one worker with the requested role to the shift
     * @param date
     * @param type
     * @param b
     * @param role
     * @return
     */
    public boolean AssignOne(String date, int type, String b, Role role,String[] weekrange) throws Exception {
        //bring the shift from the DB
        Shift shift = dbOper.bringShiftbyDatenType(date,type,GlobalsVar.getmyEnumB(b));
        //add 1 to the counter of the shift
        Map<Role,Integer> counter = shift.getCounterNeededRoles();
        counter.put(role,1);
        shift.setCounterNeededRoles(counter);
        dbOper.deleteShiftfromDB(shift.getShiftType(),shift.getShiftdate(),b);
        dbOper.deleteParticipating(shift.getShiftdate(),shift.getShiftType(), String.valueOf(shift.getBranch()));
        dbOper.savetoHistory(shift);
        //choose a worker in this branch who has this role and can work
        Map<Role,List<Worker>> workerswhocanwork = Workerswhocanworkatshift(date,type,shift.getCounterNeededRoles(),
                GlobalsVar.getmyEnumB(b),weekrange);
        Map<Role,List<Worker>> par = shift.getParticipatesWorkersinShift();
        List<Worker> list = new ArrayList<>();
        list.add(workerswhocanwork.get(role).get(0));
        //add to the participants the worker with the role
        par.put(role,list);
        //save it to the shift and to the DB
        shift.setParticipatesWorkersinShift(par);
        dbOper.deleteShiftfromDB(shift.getShiftType(),shift.getShiftdate(),b);
        dbOper.deleteParticipating(shift.getShiftdate(),shift.getShiftType(), String.valueOf(shift.getBranch()));
        dbOper.savetoHistory(shift);
        return true;

    }
}
