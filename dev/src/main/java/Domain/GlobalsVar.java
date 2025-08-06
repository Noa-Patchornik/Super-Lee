package Domain;

import java.time.LocalDate;
import java.util.*;
import java.time.LocalDate.*;

public class GlobalsVar {

    private static DBoperations db = DBoperations.GetDBInstance();
    /**
     * A Global Variables class
     * Static final attributes that can be used in the system
     */
    //the number of shifts per day
    public static int NUMOFSHIFTSPERDAY=2;

    public static void setNUMOFSHIFTSPERDAY(int num){
        NUMOFSHIFTSPERDAY=num;
    }
    public static int getNUMOFSHIFTSPERDAY(){
        return NUMOFSHIFTSPERDAY;
    }

    //the number of working day of the week
    public static int NUMOFWORKDAYSATWEEK=7;

    public static void setNUMOFWORKDAYSATWEEK(int num){
        NUMOFWORKDAYSATWEEK=num;
    }
    public static int getNUMOFWORKDAYSATWEEK(){
        return NUMOFWORKDAYSATWEEK;
    }

    //the minimum salary of an employee
    public static  double MINIMUMSALARY=50;

    public static void setMINIMUMSALARY(double salary){
        MINIMUMSALARY=salary;
    }
    public static double getMINIMUMSALARY(){
        return MINIMUMSALARY;
    }

    //the ID of the shifts, availability,assigningpres
    private static int generalIDCounter=55555555;

    public static void setgeneralIDCounter(int id){
        generalIDCounter=id;
    }
    public static int getGeneralIDCounter(){
        generalIDCounter = db.getCounter();
        generalIDCounter++;
        db.saveCounter(generalIDCounter);
        return generalIDCounter;
    }

    //the length of the morning shifts
    public static int LENGTHOFMORNINGSHIFTINHOUES=7;

    public static void setLENGTHOFMORNINGSHIFTINHOUES(int input){
        LENGTHOFMORNINGSHIFTINHOUES=input;
    }
    public static int getLENGTHOFMORNINGSHIFTINHOUES(){
        return LENGTHOFMORNINGSHIFTINHOUES;
    }

    //length of evening shifts
    public static int LENGTHOFEveningSHIFTINHOUES=7;

    public static void setLENGTHOFEveningSHIFTINHOUES(int input) {
        LENGTHOFEveningSHIFTINHOUES = input;
    }
    public static int getLENGTHOFEveningSHIFTINHOUES() {
        return LENGTHOFEveningSHIFTINHOUES;
    }

    //the password of the HR manager to the system
    private static int SystemPassword=1234;

    public static void setSystemPassword(int newpassword){
        SystemPassword=newpassword;
    }
    public static int getSystemPassword() {
        return SystemPassword;
    }

    //the defaul amount of off days
    public static int  defaultoffdays=10;

    public static void setDayoff(int input) {
        defaultoffdays=input;
    }
    public static int getOffDays() {
        return defaultoffdays;
    }


    //the shift options amount, string
    private static Map<Integer, String> shiftoptions=new HashMap<>();

    public static Map<Integer, String> getShiftoptions() {
        shiftoptions.put(0,"no shifts");
        shiftoptions.put(1,"just morning");
        shiftoptions.put(2,"just evening");
        shiftoptions.put(3,"morning and evening");
        return shiftoptions;
    }

    //the date of the currnent day
    public static LocalDate Today = LocalDate.now();

    //the list of all the possibleRoles
    private static List<Role> allposibleRoles = new ArrayList<>();

    public static List<Role> getAllposibleRoles() {
        if(allposibleRoles.isEmpty()) {
            allposibleRoles.add(Role.Cashier);
            allposibleRoles.add(Role.Manager);
            allposibleRoles.add(Role.Store_Keeper);
            allposibleRoles.add(Role.Delivery_Person);
            allposibleRoles.add(Role.DriverB);
            allposibleRoles.add(Role.DriverC);
            allposibleRoles.add(Role.DriverD);
        }
        return allposibleRoles;
    }

    public static Role makeitenum(String trim) {
        Role r =null;
        for (int i = 0; i < GlobalsVar.getAllposibleRoles().size(); i++) {
            r = GlobalsVar.getAllposibleRoles().get(i);
            if (trim.compareTo("Cashier")==0)
                return Role.Cashier;
            if (trim.compareTo("Manager")==0)
                return Role.Manager;
            if (trim.compareTo("Store_Keeper")==0)
                return Role.Store_Keeper;
            if(trim.compareTo("Delivery_person")==0)
                return Role.Delivery_Person;
            if(trim.compareTo("DriverB")==0)
                return Role.DriverB;
            if(trim.compareTo("DriverC")==0)
                return Role.DriverC;
            if(trim.compareTo("DriverD")==0)
                return Role.DriverD;
        }
        return r;
    }


    public static Branch getmyEnum(int tmpinput) {
        switch (tmpinput){
            case 1:
                return Branch.A;
            case 2:
                return Branch.B;
            case 3:
                return Branch.C;
            case 4:
                return Branch.D;
            case 5:
                return Branch.E;
            case 6:
                return Branch.F;
            case 7:
                return Branch.G;
            case 8:
                return Branch.H;
            case 9:
                return Branch.I;
            case 10:
                return Branch.J;
        }
        return Branch.A;
    }

    public static Branch getmyEnumB(String s) {
        switch (s){
            case "A":
                return Branch.A;
            case "B":
                return Branch.B;
            case "C":
                return Branch.C;
            case "D":
                return Branch.D;
            case "E":
                return Branch.E;
            case "F":
                return Branch.F;
            case "G":
                return Branch.G;
            case "H":
                return Branch.H;
            case "I":
                return Branch.I;
            case "J":
                return Branch.J;
        }
        return Branch.A;

    }

    /**
     * print all the workers and their details
     * @param tmpList- List of workers to print their details
     */
    public static void PrintList(List<Worker> tmpList) {
        for(int i=0;i<tmpList.size();i++){
            tmpList.get(i).PrintMyDetails();
        }
    }

    public static String getBranchByString(int input) {
        switch (input){
            case 1:
                return "A";
            case 2:
                return "B";
            case 3:
                return "C";
            case 4:
                return "D";
            case 5:
                return "E";
            case 6:
                return "F";
            case 7:
                return "G";
            case 8:
                return "H";
            case 9:
                return "I";
            case 10:
                return "J";
            default:
                return null;
        }
    }
}
