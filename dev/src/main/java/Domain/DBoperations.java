package Domain;

import Data.ShiftManager;
import Data.WorkerManager;
import Presentation.AssigningPres;
import java.util.*;

public class DBoperations {
    /**
     * the class that represent the database for now and will be the data access layer later. a singleton class such that there is
     * only one database for all the system
     */
    private static DBoperations the_singletone = null;

    private ShiftManager SM = new ShiftManager();

    private WorkerManager WM = new WorkerManager();

    private   WorkerRepo WR = new WorkerRepo();

    private ShiftRepo SR = new ShiftRepo();

    private DBoperations()  {
    }

    public static DBoperations GetDBInstance()  {
        if (null == the_singletone) {
            the_singletone = new DBoperations();
        }
        return the_singletone;
    }

    //*a list of all the current workers */
    private List<Worker> allCurrentWorkers = new ArrayList<>();

    /**
     * delete worker from the DB and from the repository
     * @param ID
     * @return
     * @throws Exception
     */
    public boolean deletePrevWorkerfromDB(int ID) throws Exception {
        WR.deletePrev(ID);
        return WM.deletePrevWorker(ID);
    }

    /**
     * delete shift from the DB and from the repository
     * @param type
     * @param date
     * @param branch
     * @return
     * @throws Exception
     */
    public boolean deleteShiftfromDB(int type, String date, String branch) throws Exception {
        boolean b =SR.deleteShiftfromDB(type,date,branch);
        boolean b2= SM.deleteShift(date,type,branch);
        if(!b && !b2){
            throw new Exception("Can't delete the shift from the database");
        }
        else{
            return true;
        }
    }


    /**
     * the system delete worker from the database based on the ID of the worker
     * if the id is not in the database the system throws an exception
     */
    public boolean FireaWorkerDB(int tempID) throws Exception {
        WR.FireaWorkerDB(tempID);
        return WM.fireWorker(tempID);
    }

    /**
     * the function get worker and save its roles to the database
     */
    public boolean saveWorkersRolestoDB(Worker tempWorker) throws Exception {
        boolean b=true;
        for(int i=0;i<tempWorker.getRole().size();i++){
            b = WM.insertRoles2Worker(tempWorker.getWorkerID(), String.valueOf(tempWorker.getRole().get(i)));
        }
        return b;
    }

    /**
     * the function save worker to the DB
     * @param tempWorker
     * @return
     * @throws Exception
     */
    public boolean saveworkertoDB(Worker tempWorker) throws Exception {
        if(tempWorker.getWorkerID()==0){
            throw new Exception("Worker can't be with ID 0");
        }
        WR.saveworkertoDB(tempWorker);
        boolean b = WM.insertWorker(tempWorker.getWorkerID(),tempWorker.getFirstName(),tempWorker.getLastName(),tempWorker.getBankAccount(),String.valueOf(tempWorker.getmyBranch()),tempWorker.getWorkerContract().getStartDateRange(),tempWorker.getWorkerContract().getEndDateRange(),tempWorker.getWorkerContract().getHourlySalary(), String.valueOf(tempWorker.getRole().get(0)));
        if(!b){
            return false;
        }
        return true;
    }

    /**
     * the function read availability for a worker in a specific week
     * @param id
     * @param date
     * @return
     * @throws Exception
     */
    public List<String> readav(int id, String date) throws Exception {

            List<String> ans = SM.readAvailability(id,date);
            if(ans.size()==0)
                return ans;
            int [] morning = new int[7];
            for(int i=0;i<7;i++){
                morning[i]=Integer.valueOf(ans.get(i));
            }
            int [] evening = new int[7];
            for(int i=0;i<7;i++){
                evening[i] = Integer.valueOf(ans.get(i+7));
            }
            WR.insertAv(id,morning,evening, ans.get(14),ans.get(15));
            return ans;
    }


    /**
     * the function get shift and add it to the database of history shifts
     * if the shift has assign, add the participants to the DB as well
    */
    public boolean savetoHistory(Shift tempshift) throws Exception {

        SR.savetoHistory(tempshift);
        if(tempshift.getParticipatesWorkersinShift()!=null && !tempshift.getParticipatesWorkersinShift().isEmpty()){
            for(Role r: tempshift.getParticipatesWorkersinShift().keySet()) {
                List<Worker> tmp = tempshift.getParticipatesWorkersinShift().get(r);
                for(int i=0; i<tmp.size(); i++) {
                    boolean b = SM.insertParticipatesWorkersInShift(tempshift.getShiftdate(),tempshift.getShiftType(), tmp.get(i).getWorkerID(),
                            String.valueOf(r),String.valueOf(tempshift.getBranch()));
                    if(!b){
                        throw new Exception("Can't add workers to shift");
                    }
                }
            }
        }
        return SM.insertShift(tempshift.getShiftID(),tempshift.getShiftdate(),String.valueOf(tempshift.getBranch()),tempshift.getShiftType(),tempshift.getCounterNeededRolesString());


    }

    /** the function get id of worker read it from the database
     * the function returns the worker
     * if the id is not in the database the system throws an exception
     */
    public Worker readarecfromDB(int tempID) throws Exception {
        Worker w = WR.readarecfromDB(tempID);
        if(w!=null)
            return w;
        List<String> Workerlist = WM.readWorkerfromDB(tempID);
        if(Workerlist==null)
            throw new Exception("The worker is not in the database please try again");
        Contract returnc = new Contract(Workerlist.get(4),Workerlist.get(5),Double.parseDouble(Workerlist.get(6)));
        Branch b = GlobalsVar.getmyEnumB(Workerlist.get(7));
        List<String> roles= WM.readWorkerfromDBRoles(Integer.parseInt(Workerlist.get(0)));
        List<Role> myroles = new ArrayList<>();
        for(int i=0;i<roles.size();i++){
            myroles.add(GlobalsVar.makeitenum(roles.get(i)));
        }
        Worker returnw = new Worker(Integer.parseInt(Workerlist.get(0)),Workerlist.get(1),Workerlist.get(2),Workerlist.get(3),null,b,returnc);
        returnw.setRole(myroles);
        WR.insertWorker(returnw);
        return returnw;
    }

    /**
     * the function read a prevWorker from the DB
     * @param id
     * @return
     * @throws Exception
     */
    public WorkerToSend readPrevWorker(int id) throws Exception {
        Worker ans = WR.readPrevWorker(id);
        if(ans!=null)
            return createWorkerToSend(ans);
        else{
            List<String> tmp = WM.readPrevWorkerFromDB(id);
            if(tmp!= null && !tmp.isEmpty()) {
                Contract c = new Contract(tmp.get(4), tmp.get(5), Double.valueOf(tmp.get(6)));
                Worker w = new Worker(Integer.valueOf(tmp.get(0)), tmp.get(1), tmp.get(2), tmp.get(3), null, GlobalsVar.getmyEnumB(tmp.get(7)), c);
                return createWorkerToSend(w);
            }
        }
        return null;
    }


    /**
     *  the function gets Role and returns a list of all the workers in the same role as it gets
     */
    public List<WorkerToSend> bringworkersbyenum(Role temprole) throws Exception {
        List<Worker> tmp  = WR.bringworkersbyenum(temprole);
        List<WorkerToSend> ans = new ArrayList<>();
        for(int i=0; i<tmp.size(); i++){
            WorkerToSend w = createWorkerToSend(tmp.get(i));
            if(w!=null)
                ans.add(w);
        }
        if(!ans.isEmpty())
            return ans;
        List<Worker> returnlist = new ArrayList<>();
        List<Integer> tmplist = WM.readWorkerByRole(String.valueOf(temprole)); // all the workers id with the asme temprole
        for(int i=0;i< tmplist.size();i++){
            returnlist.add(readarecfromDB(tmplist.get(i)));
        }
        for(int i=0; i<returnlist.size(); i++){
            WorkerToSend w = createWorkerToSend(returnlist.get(i));
            if(w!=null)
                ans.add(w);
        }
        return ans;

    }

    /**
     * the function creat an Object WorkertoSend to send it to the Service layer
     * @param worker
     * @return
     */
    public WorkerToSend createWorkerToSend(Worker worker) {
        ContractToSend c = new ContractToSend(worker.getWorkerContract().getStartDateRange(),worker.getWorkerContract().getEndDateRange(),
                worker.getWorkerContract().getHourlySalary(),worker.getWorkerContract().getoffdays());
        WorkerToSend w = new WorkerToSend(worker.getWorkerID(), worker.getFirstName(), worker.getLastName(),
                worker.getBankAccount(),c,worker.getRole(),worker.getmyBranch());
        return w;
    }

    /**
     *  the function gets worker and role and add the role to the worker
     * if the worker is not in the database the function throws exception
     */
    public boolean updatePer(Worker tempworker, Role tempRole) throws Exception {

        for(int i=0;i<tempworker.getRole().size();i++){
            if(tempworker.getRole().get(i)==tempRole){
                throw new Exception("you can not add an existing role to a worker");
            }
        }
        WR.updatePer(tempworker,tempRole);
        return WM.insertRoles2Worker(tempworker.getWorkerID(),String.valueOf(tempRole));

    }

    /**
     * the function get worker and role and delete the role from the worker
     * if the worker is not in the database the function throws exception
     */
    public boolean deletePer(Worker tempworker, Role tempRole) throws Exception {
        boolean ans = WR.deletePer(tempworker,tempRole);
        return WM.deleteRoleFromWorker(tempworker.getWorkerID(),String.valueOf(tempRole));
    }

    /**
     * the function adds transport to a shift
     * @param idS
     * @param date
     * @param branch
     * @param type
     * @param idW
     * @param truck
     * @return
     * @throws Exception
     */
    public boolean insertTransport(int idS,String date, String branch, int type, int idW,String truck) throws Exception {
        boolean ans2 = SM.insertTransport(idS,date, branch, type, idW,truck);
        return  ans2;
    }


    /**
     * the function gets 2 dates as strings and calculate if the shift is in the range
     * @param shift - the date of the shift
     * @param start - the start date of the week
     * @return - int that represent the day of the week from the start day
     */
    public static int calculateDifferenceInDays(String shift, String start) {
        String[] parts2 = start.split("/");
        String[] parts = shift.split("/");
        if(Integer.parseInt(parts[1].trim())!=Integer.parseInt(parts[1].trim())){
            return 100;
        }
        if(Integer.parseInt(parts[2].trim())!=Integer.parseInt(parts[2].trim())) {
            return 100;
        }
            int date = 0, date2 = 0;
        try {
            date = Integer.parseInt(parts[0].trim());
        } catch (NumberFormatException e) {
        }

        try {
            date2 = Integer.parseInt(parts2[0].trim());
        } catch (NumberFormatException e) {
        }
        int ans = date - date2;
        if (ans >= 0)
            return ans;
        else return date+30-date2;


    }


    /**
     * the function update the database with the new contract of the employee
     * @param tmpcon - the new contract
     * @param tmpID - the worker id
     */
    public boolean updateContract(Contract tmpcon, int tmpID) throws Exception {
        WR.updateContract(tmpcon,tmpID);
        return WM.updateContractDetails(tmpID,tmpcon.getStartDateRange(),tmpcon.getEndDateRange(),tmpcon.getHourlySalary(),tmpcon.getoffdays());
    }

    /**
     * the function return a map of roles and list of workers with their salaries
     * @return - the map of the worker based on their role
     */
    public Map<Integer, Double> collectallsalaries() {
        Map<Integer, Double> tmpdict = new HashMap<>();
        for (int i = 0; i < this.allCurrentWorkers.size(); i++) {
            tmpdict.put(this.allCurrentWorkers.get(i).getWorkerID(), (double) this.allCurrentWorkers.get(i).getSalary());
        }
        return tmpdict;
    }


    /**
     * the function return the shift from the database
     * @param date - the date of the shift
     * @param type - the type of the shift (0-morning, 1- evening)
     * @return - the record of the shift from the database
     * @throws Exception - if the shift is not in the database throw exception
     */
    public Shift bringShiftbyDatenType(String date, int type,Branch b) throws Exception {
        Map<Role, List<Worker>> map =new HashMap<>();
        List<Worker> workers = new ArrayList<>();
        Shift ans = SR.bringShiftbyDatenType(date,type,b);
        if(ans==null) {
            List<String> list = SM.readShiftbyDTB(date, type, String.valueOf(b));
            if(list!=null && !list.isEmpty()) {
                ans = new Shift(Integer.valueOf(list.get(2)), list.get(1), GlobalsVar.getmyEnumB(list.get(3)));

                //bring the participants of the shift from the DB
                List<List<String>> tmp = SM.readassignWorkersperShift(ans.getShiftdate(),ans.getShiftType(), list.get(3));
                if (tmp != null && !tmp.isEmpty() && !tmp.get(0).isEmpty()) {
                    //go through all the possible roles, and all the participants
                    for (int i = 0; i < GlobalsVar.getAllposibleRoles().size(); i++) {
                        for (int j = 0; j < tmp.size(); j++) {
                            //if the current role is the role of the current worker add it to list
                            if (GlobalsVar.getAllposibleRoles().get(i).compareTo(GlobalsVar.makeitenum(tmp.get(j).get(1))) == 0) {
                                //read the data of the worker from the DB and add it to the list
                                Worker w = readarecfromDB(Integer.valueOf(tmp.get(j).get(0)));
                                workers.add(w);
                            }
                        }
                        //add the list of workers at the same role to the map
                        map.put(GlobalsVar.getAllposibleRoles().get(i), workers);
                        workers = new ArrayList<>();
                    }
                    //set the participants to the shift
                    ans.setParticipatesWorkersinShift(map);
                    //set the manager
                    ans.setShiftManager(ans.getParticipatesWorkersinShift().get(Role.Manager).get(0));
                }
                //add the counter map to the shift
                Map<Role,Integer> tmpmap = new HashMap<>();
                tmpmap.put(Role.Cashier,Integer.valueOf(list.get(4)));
                tmpmap.put(Role.Delivery_Person,Integer.valueOf(list.get(5)));
                tmpmap.put(Role.Manager,Integer.valueOf(list.get(6)));
                tmpmap.put(Role.Store_Keeper,Integer.valueOf(list.get(7)));
                tmpmap.put(Role.DriverB,Integer.valueOf(list.get(8)));
                tmpmap.put(Role.DriverC,Integer.valueOf(list.get(9)));
                tmpmap.put(Role.DriverD,Integer.valueOf(list.get(10)));
                ans.setCounterNeededRoles(tmpmap);

                //bring the transport to the shift: shiftID, date,branch,type,idW (driver), trucktype
                List<String> tran = SM.readTransport(ans.getShiftType(),String.valueOf(ans.getBranch()),ans.getShiftdate());
                if(tran!= null && !tran.isEmpty()) {
                    Transport t = new Transport(Integer.valueOf(tran.get(3)), tran.get(1), tran.get(5), GlobalsVar.getmyEnumB(tran.get(2)));
                    Worker w = readarecfromDB(Integer.valueOf(tran.get(4)));
                    t.setDriver(w);
                    ans.addtranport(t);
                }
            }
        }
        return ans;
    }

    /**
     * the function return the employees who can work in the given shift
     * @param s - shift to work on
     * @return - the list of all the employees who can work in the shift
     */
    public List<Worker> bringworkersbyshift(Shift s, String[] weekrange,Branch b) throws Exception {
        List<Worker> tmplist = new ArrayList<>();
        Map<String,Availability> av;
        int[][] arr;
        List<Worker> workersbranch= this.readdworkersbybranch(b);
        for (int i = 0; i < workersbranch.size(); i++) {
            av = workersbranch.get(i).getMyConstraintsperWeek();
            boolean flag=false;
            for(String date : av.keySet()) {
                int startweek = this.calculateDifferenceInDays(s.getShiftdate(), date);
                if(startweek<7) {
                    arr = av.get(date).getAvailabilityArr();
                    if (arr[s.getShiftType()][startweek]== 1) {
                        tmplist.add(workersbranch.get(i));
                    }
                        flag = true;
                        break;

                }
            }
            if(!flag) {
                //in case the employee didn't set constraint creat for him a default one for the week
                Availability avail = new Availability(weekrange[0], weekrange[1]);
                SM.deleteAvail(workersbranch.get(i).getWorkerID(),weekrange[0]);
                int[] mornings = avail.getAvailabilityArr()[0];
                int[] evenings = avail.getAvailabilityArr()[1];
                SM.insertAvailability(workersbranch.get(i).getWorkerID(), mornings, evenings, weekrange[0], weekrange[1]);
                tmplist.add(workersbranch.get(i));
            }
        }
        return tmplist;
    }

    /**
     * the function saves the assigning of the week to the database
     * @param assigningPres - a week planned
     */
    public void saveassign(AssigningPres assigningPres) {
        SR.saveassign(assigningPres);
    }

    /**
     * the function gets a date of a week and bring from the database the correct record of the assigning
     * @param startdate - the start date of the week
     * @return - the assigning
     * @throws Exception - if the week wasn't planned
     */
    public AssigningPres bringAssigningPres(String startdate, Branch b){
        return SR.bringAssigningPres(startdate,b);
    }

    /**
     * the function checks an employee is a current employee
     * @param workerID - an id to check
     * @return - true if it's a current employee, false else
     */
    public boolean IsCurr(int workerID) throws Exception {
        boolean ans = WR.IsCurr(workerID);
        if (!ans) {
            return WM.isCurr(workerID);
        }
        return true;
    }

    /**
     * the function saves previous worker to the database
     * @param tempWorker
     */
    public boolean savePrevworkertoDB(Worker tempWorker) throws Exception {

        boolean b = WM.insertToPrev(tempWorker.getWorkerID(),tempWorker.getFirstName(),
                tempWorker.getLastName(),tempWorker.getBankAccount(),tempWorker.getWorkerContract().getStartDateRange(),
                tempWorker.getWorkerContract().getEndDateRange(),Double.valueOf(tempWorker.getWorkerContract().getHourlySalary()),
                String.valueOf(tempWorker.getmyBranch()));
        if(b){
            boolean b2 = WR.savePrevworkertoDB(tempWorker);
            return b2;
        }
        return false;
    }


    /**
     * the function return all the workers that belong to the tmpb Branch
     * @param tmpb
     * @return
     * @throws Exception
     */
    public List<Worker> readdworkersbybranch(Branch tmpb) throws Exception {
        List<Worker> ans1 = WR.readdworkersbybranch(tmpb);
        if(ans1!=null && !ans1.isEmpty())
            return ans1;
        List<Worker> tmplist = null;

        tmplist = new ArrayList<>();
        List<Integer> ids = this.WM.readWorkerByBranch(String.valueOf(tmpb));
        for (int i = 0; i < ids.size(); i++) {
            tmplist.add(readarecfromDB(ids.get(i)));
        }
        return tmplist;
    }

    /**
     * return all the currWorkers from the repository
     */
    public List<Worker> getAllCurrentWorkers() {
        return WR.getAllCurrWorkers();
    }

    /**
     * return all the shift in the DB
     */
    public List<Shift> getShiftHistory() {
        return SR.getAllShifts();
    }

    /**
     * delete the assigning Pres from the repository
     * @param assigningPres
     * @return
     * @throws Exception
     */
    public boolean deleteassingfromDB(AssigningPres assigningPres) throws Exception {
        boolean ans2 = SR.deleteassingfromDB(assigningPres);
        if(ans2)
            return true;
        return false;
    }

    /**
     * insert constraints for a worker to the DB
     * @param id
     * @param morning
     * @param evening
     * @param startdate
     * @param endDate
     * @return
     * @throws Exception
     */
    public boolean insertAv(int id, int[] morning, int[] evening, String startdate, String endDate) throws Exception {
            boolean ans2 = WR.insertAv(id, morning, evening, startdate, endDate);
            boolean ans = SM.insertAvailability(id,morning,evening,startdate,endDate);
            if (ans2 && ans)
                return true;
        return false;
    }

    /**
     * update the details of an employee in the DB
     * @param id
     * @param firstName
     * @param lastName
     * @param bankAccount
     * @param startDate
     * @param endDate
     * @param hourlySalary
     * @param branch
     * @return
     * @throws Exception
     */
    public boolean updateWorkerDetails(int id, String firstName, String lastName, String bankAccount, String startDate, String endDate, double hourlySalary, String branch) throws Exception {
        boolean ans1 =  WM.updateWorkerDetailsManager(id, firstName, lastName, bankAccount, startDate, endDate, hourlySalary, branch);
        return ans1;
    }

    /**
     * update the salary of a worker
     * @param workerID
     * @param salary
     * @throws Exception
     */
    public void updateSalary(int workerID, double salary) throws Exception {
        WM.updateSalary(workerID,salary);
    }

    /**
     * read the salary of a worker
     * @param tmpID
     * @return
     * @throws Exception
     */
    public double readWorkerSalary(int tmpID) throws Exception {
        return WM.readSalary(tmpID);
    }

    /**
     * delete all the participants of the shift from the DB
     * @param date
     * @param type
     * @param b
     * @return
     * @throws Exception
     */
    public boolean deleteParticipating(String date, int type, String b) throws Exception {
        return SM.deleteParticipating(date,type,b);
    }

    /**
     * delete transport from the DB
     * @param type
     * @param date
     * @param branch
     * @return
     * @throws Exception
     */
    public boolean deleteTransport(int type, String date, String branch) throws Exception {
        return SM.deleteTransport(type,date, branch);
    }

    /**
     * return the ID counter from the DB
     * @return
     */
    public int getCounter() {
        try{
        return SM.IDcounter();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return 0;
        }
    }

    /**
     * save the new ID counter to the DB
     * @param counter
     */
    public void saveCounter(int counter) {
        try {
            SM.saveCounter(counter);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * add the configuration file to the DB to boot the system
     * @param confidata
     */
    public void loadConfig(List<Integer> confidata) {
        try {
            SM.readConfig(confidata);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}