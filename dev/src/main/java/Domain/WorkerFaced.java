package Domain;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;
import java.io.File;


public  class WorkerFaced {
    /**
     * The class controller of the Domain.Worker class in the domain layer
     * The resbonsebility of the class is to connect between the presentation layer to the database layer, get the data from
     * the database, update the objects, and send it to the function who ask for it
     * attribute: dboper - the instance of the database pointer so all the changes would be saved to the database
     */
    private DBoperations dbOper = DBoperations.GetDBInstance();

    // --load data from the txt files--//
    /**
     * the function saves the current employees to the database from the file
     * @param readWorkersData - the list of the employees to save to the database
     */
    public void loadData(List<Worker> readWorkersData) {
        for(int i=0;i<readWorkersData.size();i++) {
            try {
                dbOper.saveworkertoDB(readWorkersData.get(i));
                dbOper.saveWorkersRolestoDB(readWorkersData.get(i));
            }
            catch(Exception e){

            }
        }
    }

    /**
     * the function load to the database the availability of the employees
     * @param readAvailability - the map of availability by employee id, availability
     */
    public void loadDataav(Map<Integer, Availability> readAvailability) throws Exception {
        for ( int key : readAvailability.keySet()){
            dbOper.insertAv(key,readAvailability.get(key).getAvailabilityArr()[0],readAvailability.get(key).getAvailabilityArr()[1],
                    readAvailability.get(key).getStartWeekRange(),readAvailability.get(key).getEndWeekRange() );
        }
    }

    /**
     * the function saves the previous employees to the database from the file
     * @param readPrevWorkersData - the list of the employees to save to the database
     */
    public void loadDataPrevworkers(List<Worker> readPrevWorkersData) throws Exception{
        for(int i=0;i<readPrevWorkersData.size();i++) {
            dbOper.savePrevworkertoDB(readPrevWorkersData.get(i));
        }
    }

    //--end of loading data from txt file--//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--add to datebase functions--//
    /**
     * the function that gets new employee and save it to the database if the id is not in it
     * @param - new worker to add to the database
     */
    public void WorkerRegistration(int id, String fn, String ln, String ba, String role, String branch, List<Object> Cond) throws Exception {
        if(id == 0){
            throw new Exception();
        }
        if(isCurr(id)){
            System.out.println("The id is already in the database");
            throw new Exception();
        }
//
        Contract c = new Contract(String.valueOf(Cond.get(0)),String.valueOf(Cond.get(1)),Double.valueOf(String.valueOf( Cond.get(2))));
        Worker worker = new Worker(id,fn,ln,ba,GlobalsVar.makeitenum(role),GlobalsVar.getmyEnumB(branch),c);
        boolean b = dbOper.saveworkertoDB(worker);

        if(!b){
            throw new Exception();
        }
        b = dbOper.saveWorkersRolestoDB(worker);
        if(!b)
            throw new Exception();
    }

    /**
     * add role tho the employee
     *
     * @param id - the employee to add to
     * @param r  - the new role
     */
    public void addPerforworker(int id, String r) throws Exception {
        Worker w = dbOper.readarecfromDB(id);
        if( w==null || !isCurr(id)){
            throw new Exception();
        }
        boolean b =dbOper.updatePer(w,GlobalsVar.makeitenum(r));
        if(!b){
            throw new Exception();
        }
    }

    /**
     * the function saves the new constraint of the employee to the database, if there is an error throws Exception
     * @param id - the id of the employee
     * @param daterange - the range of the week of the constraints
     * @param avail - the array that represent the availability of the employee
     */
    public void saveConstarints(int id, String[] daterange, int[][] avail) throws Exception {
        Worker w = dbOper.readarecfromDB(id);
        if( w == null ){
            throw new Exception();
        }
        Availability av = new Availability(daterange[0],daterange[1],avail);
        Map<String, Availability> m= new HashMap<>();
        m.put(av.getStartWeekRange(),av);
        w.setMyConstraintsperWeek(m, av.getStartWeekRange());
        //save to database
        boolean b =dbOper.insertAv(id,avail[0],avail[1],daterange[0],daterange[1]);
        if(!b){
            throw new Exception();
        }
    }



    //--end of adding functions--//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--update database functions--//
    /**
     * the function that update the contract of the employee in the database
     * @param id - the id of the employee
     * @param input - the number that represent which detail to change
     *        1 - end date, 2- salary, 3- off days
     * @param  - the new contract to save to the database
     */
    public void updateContractDetails(int id, Object conChange, int input) throws Exception {
        Worker w= dbOper.readarecfromDB(id);
        if (w == null || !isCurr(id)) {
            throw new Exception();
        }
        Contract c = w.getWorkerContract();
        if(input==1){
            c.setEnddate((String) conChange);
        }
        if(input==2){
            c.setHourlySalary(Double.valueOf(String.valueOf(conChange)));
        }
        if(input==3){
            if(c.getoffdays()< Integer.valueOf(String.valueOf(conChange))){
                System.out.println("Can't change the amount of days off to less then the employee has");
                return;
            }
            c.setOffdays((int) conChange);
        }
        boolean b= dbOper.updateContract(c,id);
        if(!b){
            throw new Exception();
        }
    }


    /** update the worker from the current workers to the previous workers
     * the function gets an employee and "fire" him, change the worker from the list of current employees to the previous empolyee
     * @param id - an employee to fire
     * @param date - the end date of the contract of the worker
     */
    public boolean WorkerFire(int id, String date) throws Exception {
        Worker w = dbOper.readarecfromDB(id);
        if( w==null || !isCurr(id)){
            throw new Exception();
        }
        Contract c = new Contract(w.getWorkerContract().getStartDateRange(),date,w.getWorkerContract().getHourlySalary());
        boolean b = dbOper.updateContract(c,id);
        if(!b){
            throw new Exception();
        }
        b = dbOper.FireaWorkerDB(id);
        if(!b){
            throw new Exception();
        }
        return true;
    }

    /**
     * the function update one of the options of the personal data about the requested worker
     * @param id - the worker id
     * @param inputstring - the new data of the worker
     * @param i - the options to change:
     *        1 - first name, 2- last name, 3- bank account
     * @throws Exception - if an Exception happened with read the worker from the database or the update, throws exception
     * to the service layer to deal with
     */
    public void updateWorkerDetails(int id, String inputstring, int i) throws Exception {
        Worker w = dbOper.readarecfromDB(id);
        if(w== null)
            throw new NullPointerException();
        if(!this.isCurr(id))
            throw new Exception();
        if(i == 1){
            w.setFirstName(inputstring);
        }
        if(i == 2){
            w.setLastName(inputstring);
        }
        if(i == 3){
            w.setBankAccount(inputstring);
        }
        boolean b = dbOper.updateWorkerDetails(id,w.getFirstName(),w.getLastName(), w.getBankAccount(),
                w.getWorkerContract().getStartDateRange(),w.getWorkerContract().getEndDateRange(),
                w.getWorkerContract().getHourlySalary(),String.valueOf(w.getmyBranch()));
        if(!b){
            throw new Exception();
        }
    }

    //--end of updating functions --//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--delete functions -- //
    /**
     * the function delete a previous employee from the database without the option to undo it
     * @param id- a previous employee to delete from the database
     */
    public void deleteWorkerfromDB(int id) throws Exception {
        boolean del = dbOper.deletePrevWorkerfromDB(id);
        if (!del){
            throw new Exception();
        }
    }

    /**
     * the function that delete role from the employee
     * @param id - the employee to delete from
     * @param r - the role to delete
     */
    public void delPerforworker(int id, String r) throws Exception {
        Worker w = dbOper.readarecfromDB(id);
        if(w==null || !isCurr(id)){
            throw new Exception();
        }
        boolean b = dbOper.deletePer(w,GlobalsVar.makeitenum(r));
        if(!b){
            throw new Exception();
        }
    }


    //--end of deleting functions --//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--calculate functions --//

    /**
     * the function calculate the salary of the employee based on their hourly salary and the amount of hours they worked
     * @param tmpID - the id of the employee to calculate
     * @return - the salary
     * @throws Exception - if the employee is not in the database throws exception
     */
    public double CalculateSalary(int tmpID) throws Exception {
          return dbOper.readWorkerSalary(tmpID);
    }

    /**
     * the function gets 2 dates and calculate the difference between them and return it
     */
    public int CalculateDiffDay(String startdate, LocalDate now) {
        int day= now.getDayOfMonth();
        int month = now.getMonthValue();
        int year = now.getYear();
        String today = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
        return dbOper.calculateDifferenceInDays(startdate,today);
    }


    /**
     * the function get all the salaries of the employees and transfer it to a report to export it
     */
    public void transferSalary(){
        Map<Integer,Double> tmpdict = dbOper.collectallsalaries();
        try {
            File file = new File("dev/allWorkersSalaries.txt");
            PrintWriter writer = new PrintWriter("allWorkersSalaries");
            for ( int key : tmpdict.keySet()){
                writer.println("for worker ID : " + key + " the salary is: " + this.CalculateSalary(key));
            }
            writer.close();
            System.out.println("File created at: " + file.getAbsolutePath());
        }
        catch(Exception e){
        }
    }

    //--end of calculation functions--//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--bring from db functions--//

    /**
     * the function return a list of all the current employees in a specific role
     * @param askfornewRole - the role to check
     * @return - the list of all the employees with this role
     */
    public List<WorkerToSend> bringworkersbyenum(Role askfornewRole) throws Exception {
        return dbOper.bringworkersbyenum(askfornewRole);
    }

    /**
     * the function gets a specific branch and returns all the employees from this branch sorted by roles
     */
    public List<WorkerToSend> bringworkersbybranch(Branch a) throws Exception{
        List<WorkerToSend> list =new ArrayList<>();
        List<Worker> tmplist = dbOper.readdworkersbybranch(a);
        if(tmplist==null || tmplist.isEmpty()){
            return null;
        }
        for(int i=0;i<tmplist.size();i++){
            if(tmplist.get(i).getmyBranch().compareTo(a)==0){
                WorkerToSend w= dbOper.createWorkerToSend(tmplist.get(i));
                list.add(w);
            }
        }
        return list;
    }

    //--end of bringing functions--//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--print functions--//

    /**
     * the function prints the constraint of the employee for a requested week
     */
    public AvailabilityToSend Printconstriant(int id, String tmpdate) throws Exception{
        Worker w = dbOper.readarecfromDB(id);
        if(w==null || !isCurr(id)){
            throw new Exception();
        }
        List<String> tmp = dbOper.readav(id,tmpdate);
        if(tmp.isEmpty() || tmp==null){
            throw new Exception("You didn't enter constraints for this week, please try again");
        }

        int [] morning = new int[GlobalsVar.NUMOFWORKDAYSATWEEK];
        for(int i=0;i<GlobalsVar.NUMOFWORKDAYSATWEEK;i++){
            morning[i]=Integer.valueOf(tmp.get(i));
        }
        int [] evening = new int[GlobalsVar.NUMOFWORKDAYSATWEEK];
        for(int i=0;i<GlobalsVar.NUMOFWORKDAYSATWEEK;i++){
            evening[i] = Integer.valueOf(tmp.get(i+GlobalsVar.NUMOFWORKDAYSATWEEK));
        }
        String date= tmp.get(14);
        String enddate= tmp.get(15);
        int[][] constrain = new int[GlobalsVar.NUMOFWORKDAYSATWEEK][GlobalsVar.NUMOFSHIFTSPERDAY];
        constrain[0]=morning;
        constrain[1]=evening;
        Availability av=new Availability(date,enddate,constrain);
        AvailabilityToSend ans = new AvailabilityToSend(av.getAvID(), av.getStartWeekRange(),av.getEndWeekRange(),av.getAvailabilityArr());
        return ans;
    }


    /**
     * the function print the worker details if the employee is a current employee, else throws an Exception
     */
    public WorkerToSend PrintWorkerdetailes(int id) throws Exception {
        Worker w = dbOper.readarecfromDB(id);
        if( w == null){
            throw new Exception("Employee wasn't found please try again");
        }
        ContractToSend c = new ContractToSend(w.getWorkerContract().getStartDateRange(),w.getWorkerContract().getEndDateRange(),
                w.getWorkerContract().getHourlySalary(),w.getWorkerContract().getoffdays());
        return new WorkerToSend(w.getWorkerID(),w.getFirstName(), w.getLastName(),w.getBankAccount(),c,w.getRole(),w.getmyBranch());
    }
    //--end of print functions--//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--boolean functions--//

    /**
     * the function check if a specific employee is in the current employees list
     * @param workerID - the id of the employee to check
     * @return true if he is a current employee, false else
     */
    public boolean isCurr(int workerID) throws Exception {
        return dbOper.IsCurr(workerID);
    }

    /**
     * the function get id and check if the employee has roles in the list of roles
     * true- if the worker doesn't have roles, false if he has roles
     */
    public boolean hasRoles(int id) throws Exception {
        Worker w= dbOper.readarecfromDB(id);
        if(w==null || !isCurr(id)){
            throw new Exception();
        }
        if(w.getRole().isEmpty() || w.getRole()==null){
            return true;
        }
        return false;
    }

    //--end of boolean functions--//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * get the employee by id and return the object from the database
     * @param id - an id of employee
     * @return worker object if the id is in the database
     */
    public Worker GetworkerbyID(int id){
        Worker w =null ;
        try {
            if(dbOper.IsCurr(id)){
                w =dbOper.readarecfromDB(id);
            }
            else {
                return w;
            }
        }
        catch(Exception e){
            return w;
        }
        return w;
    }

    public WorkerToSend PrintPrevWorkerdetailes(int id) throws Exception {
        WorkerToSend w = dbOper.readPrevWorker(id);
        if( w == null){
            throw new Exception("Employee wasn't found please try again");
        }
        return w;
    }

}

