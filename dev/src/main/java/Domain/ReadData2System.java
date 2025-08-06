package Domain;

import Data.SQLDB;
import Data.ShiftManager;
import Data.WorkerManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ReadData2System {

    /**
     * A class part of the domain layer. The responsebility of this class is to read the Database and boot the system
     * attribute: WF - The controller of the Domain.Worker class in the domain layer
     * attribute: SF - The controller of the Domain.Shift class in the domain layer
     */
    private static WorkerFaced WF = new WorkerFaced();
    private static ShiftFaced SF = new ShiftFaced();
    private static WorkerManager WM = new WorkerManager();
    private static ShiftManager SM = new ShiftManager();



    /**
     *
     */
    public static void readConfigurtionFile(String filename) {
        String fileName1;
        try {

            if (filename == null) {

                String currentDirectory = System.getProperty("user.dir");
                File currentDirFile = new File(currentDirectory);
                String parentDirectory = currentDirFile.getParent();
                fileName1 = Paths.get(parentDirectory, "dev", "configoration.txt").toString();
            }
            else {
                fileName1 = filename;
            }
//            String currentDirectory = System.getProperty("user.dir");
//            File currentDirFile = new File(currentDirectory);
//            String parentDirectory = currentDirFile.getParent();
//            String fileName = Paths.get(parentDirectory,"dev", "configoration.txt").toString();
            List<Integer> confidata = new ArrayList<>();
            //the file is: number_of_shifts_per_day, number_of_working_days_per_week, minimum_salary, generalID
            //Length of morning shifts, length of evening shifts, system Password, days off
            try (BufferedReader br = new BufferedReader(new FileReader(fileName1))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] tmpnum = line.split(" ");
                    try {
                        int numtoload = Integer.parseInt(tmpnum[0].trim());
                        confidata.add(numtoload);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format in line: " + line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            GlobalsVar.setNUMOFSHIFTSPERDAY(confidata.get(0));
            GlobalsVar.setNUMOFWORKDAYSATWEEK(confidata.get(1));
            GlobalsVar.setMINIMUMSALARY((double) confidata.get(2));
            GlobalsVar.setgeneralIDCounter(confidata.get(3));
            GlobalsVar.setLENGTHOFMORNINGSHIFTINHOUES(confidata.get(4));
            GlobalsVar.setLENGTHOFEveningSHIFTINHOUES(confidata.get(5));
            GlobalsVar.setSystemPassword(confidata.get(6));
            GlobalsVar.setDayoff(confidata.get(7));
            DBoperations.GetDBInstance().loadConfig(confidata);
        }
        catch(Exception e){

        }

    }
    /**
     * The function that read the data of the current employees and insert them to the database
     * the function gets the File with the data and by the format split it and creat workers and saves them to the database
     * each worker has its own personals details and contract details.
     */
    public static void readWorkersData(String filename) throws Exception {
        String fileName1;
        List<Worker> workers = new ArrayList<>();

        if (filename == null) {

            String currentDirectory = System.getProperty("user.dir");
            File currentDirFile = new File(currentDirectory);
            String parentDirectory = currentDirFile.getParent();
            fileName1 = Paths.get(parentDirectory, "dev", "workersData.txt").toString();
        }
        else {
            fileName1 = filename;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(fileName1))) {
            String line;
            while ((line = br.readLine()) != null) {
//          each line in format as following: int,String,String,String,String,String,String,double
//          ID,FirstName,LastName,BankAccount,Domain.Role,startDate,endDate,salary
                String[] parts = line.split(",");
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String firstName = parts[1].trim();
                    String lastName = parts[2].trim();
                    String bankaccount = parts[3].trim();
                    Role role = GlobalsVar.makeitenum(parts[4].trim());
                    String startdate = parts[5].trim();
                    String enddate = parts[6].trim();
                    double salary = Double.parseDouble(parts[7].trim());
                    Branch b = Branch.valueOf(parts[8].trim());
                    Contract contract = new Contract(startdate, enddate, salary);
                    Worker worker = new Worker(id, firstName, lastName, bankaccount, role, b, contract);
                    WM.insertWorker(id,firstName,lastName,bankaccount,parts[8].trim(),startdate,enddate,salary,parts[4].trim());
                    workers.add(worker);
                    WM.closeConnection();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in line: " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        WF.loadData(workers);
    }


    public static void readWorkersRolesData(String filename) throws Exception {
        String fileName1;
        List<Worker> workers = new ArrayList<>();

        if (filename == null) {

            String currentDirectory = System.getProperty("user.dir");
            File currentDirFile = new File(currentDirectory);
            String parentDirectory = currentDirFile.getParent();
            fileName1 = Paths.get(parentDirectory, "dev", "workersData.txt").toString();
        }
        else {
            fileName1 = filename;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(fileName1))) {
            String line;
            while ((line = br.readLine()) != null) {
//          each line in format as following: int,String,String,String,String,String,String,double
//          ID,FirstName,LastName,BankAccount,Domain.Role,startDate,endDate,salary
                String[] parts = line.split(",");
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String firstName = parts[1].trim();
                    String lastName = parts[2].trim();
                    String bankaccount = parts[3].trim();
                    Role role = GlobalsVar.makeitenum(parts[4].trim());
                    String startdate = parts[5].trim();
                    String enddate = parts[6].trim();
                    double salary = Double.parseDouble(parts[7].trim());
                    Branch b = Branch.valueOf(parts[8].trim());
                    Contract contract = new Contract(startdate, enddate, salary);
                    Worker worker = new Worker(id, firstName, lastName, bankaccount, role, b, contract);
                    WM.insertWorkerRoles(id,parts[4].trim());
                    workers.add(worker);
                    WM.closeConnection();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in line: " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        WF.loadData(workers);
    }

    /**
     * The function read the data about the previous employees and saves them to the database to.
     */
    public static void readPrevWorkersData() throws Exception{
        String currentDirectory = System.getProperty("user.dir");
        File currentDirFile = new File(currentDirectory);
        String parentDirectory = currentDirFile.getParent();
        String fileName = Paths.get(parentDirectory,"dev", "prevWorkerData.txt").toString();        List<Worker> workers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
//          each line in format as following: int,String,String,String,String,String,String,double
//          ID,FirstName,LastName,BankAccount,Domain.Role,startDate,endDate,salary
                String[] parts = line.split(",");
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String firstName = parts[1].trim();
                    String lastName = parts[2].trim();
                    String bankaccount = parts[3].trim();
                    Role role = GlobalsVar.makeitenum(parts[4].trim());
                    String startdate = parts[5].trim();
                    String enddate = parts[6].trim();
                    double salary = Double.parseDouble(parts[7].trim());
                    Branch b = Branch.valueOf(parts[8].trim());
                    Contract contract = new Contract(startdate, enddate, salary);
                    Worker worker = new Worker(id, firstName, lastName, bankaccount, role,b,contract);
                    workers.add(worker);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        WF.loadDataPrevworkers(workers);
    }

    /**
     * The function reads the details of the shifts and saves them to ths database
     */
    public static void readShifts(String filename) throws Exception {
        String fileName1;
        if (filename == null) {

            String currentDirectory = System.getProperty("user.dir");
            File currentDirFile = new File(currentDirectory);
            String parentDirectory = currentDirFile.getParent();
            fileName1 = Paths.get(parentDirectory, "dev", "datesData.txt").toString();
        }
        else {
            fileName1 = filename;
        }
            List<Shift> shifts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName1))) {
            String line;
            while ((line = br.readLine()) != null) {
//          each line in format as following: int,String,String,String,String,String,String,double
//          ID,FirstName,LastName,BankAccount,Domain.Role,startDate,endDate,salary
                String[] parts = line.split(",");
                try {
                    int type = Integer.parseInt(parts[1].trim());
                    String date = parts[0].trim();
                    Branch b = GlobalsVar.getmyEnum((Integer.valueOf(parts[2])));
                    SM.insertShift(GlobalsVar.getGeneralIDCounter(), date, parts[2], type, new HashMap());
                    shifts.add(new Shift(type,date,b));
                    WM.closeConnection();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SF.loadDataPrevshifts(shifts);


    }

    /**
     * The function gets the constraint of the employees that are already in the system
     * For each one of them creat an object from the Domain.Availability class and save it to the Domain.Worker itself and to the database
     */
    public static void readAvailability(String filename) throws Exception{
        String fileName1;
        if (filename == null) {

            String currentDirectory = System.getProperty("user.dir");
            File currentDirFile = new File(currentDirectory);
            String parentDirectory = currentDirFile.getParent();
            fileName1 = Paths.get(parentDirectory, "dev", "availability.txt").toString();
        }
        else {
            fileName1 = filename;
        }
//
        Map<Integer,Availability> ans= new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName1))) {
            String line;
            while ((line = br.readLine()) != null) {
//          each line in format as following: int,int,int,int,int,int,int,int,int,int,int,int,int,int,int,String,String
//          14 times 0/1 that represent constraint, 1- can work, 0- can't, ID,StartWeekDay,EndWeekDay
                String[] parts = line.split(",");
                try {
                    int k=0;
                    int[] morningav =new  int[GlobalsVar.getNUMOFWORKDAYSATWEEK()];
                    int[] eveningav =new  int[GlobalsVar.getNUMOFWORKDAYSATWEEK()];
                    for(int i=0;i<GlobalsVar.getNUMOFWORKDAYSATWEEK();i++) {
                        morningav[i] =  Integer.parseInt(parts[i].trim());
                        k++;
                    }
                    for(int i=0;i<GlobalsVar.getNUMOFWORKDAYSATWEEK();i++) {
                        eveningav[i] =  Integer.parseInt(parts[k].trim());
                        k++;
                    }
                    int idworkers = Integer.parseInt(parts[k].trim());
                    String datestart = parts[k+1].trim();
                    String dateend = parts[k+2].trim();
                    int[][] arg = new int[GlobalsVar.getNUMOFSHIFTSPERDAY()][GlobalsVar.getNUMOFWORKDAYSATWEEK()];
                    arg[0]=morningav;
                    arg[1]=eveningav;
                    SM.insertAvailability(idworkers,morningav,eveningav,datestart,dateend);
                    ans.put(idworkers,new Availability(datestart,dateend,arg));

                    WM.closeConnection();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        WF.loadDataav(ans);
    }
}



