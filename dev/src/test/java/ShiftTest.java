import Data.SQLDB;
import Domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ShiftTest {

    private ShiftFaced SF = new ShiftFaced();
    private DBoperations db = DBoperations.GetDBInstance();

    @Test
    public void testreadShiftFromTheDB(){
        //1
        //read shift from the DB, shift that is no exist, shift that is exists, and new shift that is created
        //shift that is not in the DB
        boolean out = false;
        String date = "01/08/2024";
        int type =0;
        Branch b = Branch.A;
        Shift tmp=null;
        try {
            tmp = db.bringShiftbyDatenType(date, type, b);
            assertNull(tmp);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        //shift that is in the DB
        try {
            Shift s = db.bringShiftbyDatenType("13/06/2024", 0, Branch.A);
            if(s!=null){
                out= true;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        Assertions.assertTrue(out);
        out = false;
        //create new shift and then try to read it from the DB
        Map<Role,Integer> counter = new HashMap<>();
        counter.put(Role.Cashier,1);
        counter.put(Role.Manager,1);
        counter.put(Role.Store_Keeper,1);
        counter.put(Role.Delivery_Person,1);
        counter.put(Role.DriverB,0);
        counter.put(Role.DriverD,0);
        counter.put(Role.DriverC,0);
        try {
            SF.createShift(date, type, Branch.B, counter);
            tmp = db.bringShiftbyDatenType("01/08/2024",0,Branch.B);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        if(tmp != null){
            out=true;
        }
        assertTrue(out);
        assertEquals(tmp.getShiftType(),type);
        assertEquals(tmp.getShiftdate(),date);
    }


    @Test
    public void WorkerwhocanworkTest(){
        //2
        //the test checks the function that return all the workers who belong to a branch and can work in a specific shift
        String date =  "16/08/2024";
        int type = 0;
        Map<Role, Integer> dict = new HashMap<>();
        dict.put(Role.Cashier, 2);
        dict.put(Role.Manager, 1);
        dict.put(Role.Store_Keeper, 0);
        dict.put(Role.Delivery_Person, 1);
        dict.put(Role.DriverB,0);
        dict.put(Role.DriverD,0);
        dict.put(Role.DriverC,0);
        int plansize=0;
        for(Role r : dict.keySet()){
            plansize += dict.get(r);
        }
        String[] tmprange = new String[2];
        tmprange[0] = "14/08/2024";
        tmprange[1] = "20/08/2024";
        Map<Role, List<Worker>> workers=new HashMap<>();
        //create the shift, checks who can work
        try {
            SF.createShift(date,type,Branch.A,dict);
            workers = SF.Workerswhocanworkatshift(date, type, dict, Branch.A, tmprange);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        //checks that there are workers that can work in the shift in this branch
        assertTrue(workers!=null);
        assertTrue(!workers.get(Role.Delivery_Person).isEmpty());
        int workrssize=0;
        for(Role r : workers.keySet()){
            workrssize += workers.get(r).size();
        }
        //check that the amount of the planning is smaller from the amount of the workers who can work
        assertTrue(plansize<=workrssize);
    }


    @Test
    public void testAssignWorkersNoExistShift(){
        //3
        //assign workers to shift that not exist
        boolean out = false;
        String date =  "25/08/2024";
        int type = 0;
        Map<Role, Integer> dict = new HashMap<>();
        dict.put(Role.Cashier, 1);
        dict.put(Role.Manager, 1);
        dict.put(Role.Store_Keeper, 0);
        dict.put(Role.Delivery_Person, 1);
        dict.put(Role.DriverB,0);
        dict.put(Role.DriverD,0);
        dict.put(Role.DriverC,0);
        String[] tmprange = new String[2];
        tmprange[0] = "21/08/2024";
        tmprange[1] = "27/08/2024";
        try{
            SF.Assignworkers(date,type,"B", tmprange,dict,null);
            out=true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out=false;
        }
        Assertions.assertFalse(out);
    }

    @Test
    public void testAssignWorkersExistShift(){
        //4
        //assign workers to shift exist in the DB
        boolean out = false;
        String date =  "28/06/2024";
        int type = 0;
        Map<Role, Integer> dict = new HashMap<>();
        dict.put(Role.Cashier, 1);
        dict.put(Role.Manager, 1);
        dict.put(Role.Store_Keeper, 0);
        dict.put(Role.Delivery_Person, 1);
        dict.put(Role.DriverB,0);
        dict.put(Role.DriverD,0);
        dict.put(Role.DriverC,0);
        String[] tmprange = new String[2];
        tmprange[0] = "23/06/2024";
        tmprange[1] = "29/06/2024";
        try{
            SF.Assignworkers(date,type,"A", tmprange,dict,null);
            out=true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out=false;
        }
        Assertions.assertTrue(out);
    }

    @Test
    public void testAssignWorkersNewShift(){
        //5
        //assign workers to a new shift
        boolean out = false;
        String date =  "25/08/2024";
        int type = 0;
        Map<Role, Integer> dict = new HashMap<>();
        dict.put(Role.Cashier, 1);
        dict.put(Role.Manager, 1);
        dict.put(Role.Store_Keeper, 0);
        dict.put(Role.Delivery_Person, 1);
        dict.put(Role.DriverB,0);
        dict.put(Role.DriverD,0);
        dict.put(Role.DriverC,0);
        String[] tmprange = new String[2];
        tmprange[0] = "21/08/2024";
        tmprange[1] = "27/08/2024";
        try{
            SF.createShift(date,type,Branch.H,dict);
            SF.Assignworkers(date,type,"H", tmprange,dict,null);
            out=true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out=false;
        }
        Assertions.assertTrue(out);
        Shift s=null;
        try{
            s = db.bringShiftbyDatenType(date,type,Branch.H);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        Assertions.assertNotNull(s);
        Map<Role,List<Worker>> par = s.getParticipatesWorkersinShift();
        Assertions.assertFalse(par.isEmpty());
        assertEquals(par.get(Role.Cashier).size(),1);
        assertEquals(par.get(Role.Manager).size(),1);
        assertEquals(par.get(Role.Delivery_Person).size(),1);
        Assertions.assertNull(par.get(Role.DriverD));
    }

    @Test
    public void testNoManagerAssigning(){
        //6
        //assign workers to a new shift with no Manager
        boolean out = false;
        String date =  "22/08/2024";
        int type = 0;
        Map<Role, Integer> dict = new HashMap<>();
        dict.put(Role.Cashier, 1);
        dict.put(Role.Manager, 0);
        dict.put(Role.Store_Keeper, 0);
        dict.put(Role.Delivery_Person, 1);
        dict.put(Role.DriverB,0);
        dict.put(Role.DriverD,0);
        dict.put(Role.DriverC,0);
        String[] tmprange = new String[2];
        tmprange[0] = "21/08/2024";
        tmprange[1] = "27/08/2024";
        try{
            SF.createShift(date,type,Branch.H,dict);
            SF.Assignworkers(date,type,"H", tmprange,dict,null);
            out=true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out=false;
        }
        Assertions.assertFalse(out);

    }


    @Test
    public void testaddTransport(){
        //7
        //add transport to a shift
        boolean out = false;
        String date =  "27/08/2024";
        int type = 0;
        Map<Role, Integer> dict = new HashMap<>();
        dict.put(Role.Cashier, 1);
        dict.put(Role.Manager, 1);
        dict.put(Role.Store_Keeper, 1);
        dict.put(Role.Delivery_Person, 1);
        dict.put(Role.DriverB,1);
        dict.put(Role.DriverD,0);
        dict.put(Role.DriverC,0);
        String[] tmprange = new String[2];
        tmprange[0] = "21/08/2024";
        tmprange[1] = "27/08/2024";
        try{
            SF.createShift(date,type,Branch.H,dict);
            SF.Assignworkers(date,type,"H", tmprange,dict,Role.DriverB);
            out=true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out=false;
        }
        Assertions.assertTrue(out);
        try{
            SF.addTranporttoShift(date,type,"B",Branch.H,Role.DriverB);
            out=true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out=false;
        }
        Assertions.assertTrue(out);
    }

    @Test
    public void testAllFunctions(){
        //8
        //test all the functions of shift together
        boolean out = false;
        String date =  "12/08/2024";
        int type = 0;
        Map<Role, Integer> dict = new HashMap<>();
        dict.put(Role.Cashier, 0);
        dict.put(Role.Manager, 1);
        dict.put(Role.Store_Keeper, 1);
        dict.put(Role.Delivery_Person, 0);
        dict.put(Role.DriverB,0);
        dict.put(Role.DriverD,1);
        dict.put(Role.DriverC,0);
        String[] tmprange = new String[2];
        tmprange[0] = "11/08/2024";
        tmprange[1] = "17/08/2024";
        //try to creat a shift with no counter for the roles
        try{
            SF.createShift(date,type,Branch.D,null);
            out=true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out=false;
        }
        Assertions.assertFalse(out);
        //create a shift with counter
        try {
            SF.createShift(date,type,Branch.I,dict);
            out = true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out = false;
        }
        Assertions.assertTrue(out);
        //assign to the shift
        try {
            SF.Assignworkers(date,type,"I",tmprange,dict,null);
            out = true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out = false;
        }
        Assertions.assertTrue(out);
        //try to add transport to the shift with no correct driver
        try {
            SF.addTranporttoShift(date,type,"B",Branch.I,Role.DriverB);
            out= true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out = false;
        }
        Assertions.assertFalse(out);
        //creat new shift and add to it transport with no Store_Keeper
        String date2 =  "13/08/2024";
        int type2 = 0;
        Map<Role, Integer> dict2 = new HashMap<>();
        dict2.put(Role.Cashier, 1);
        dict2.put(Role.Manager, 1);
        dict2.put(Role.Store_Keeper, 0);
        dict2.put(Role.Delivery_Person, 0);
        dict2.put(Role.DriverB,1);
        dict2.put(Role.DriverD,0);
        dict2.put(Role.DriverC,0);
        String[] tmprange2 = new String[2];
        tmprange2[0] = "11/08/2024";
        tmprange2[1] = "17/08/2024";
        //create shift with driver and no Store_Keeper
        try{
            SF.createShift(date2,type2,Branch.G,dict2);
            out = true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out = false;
        }
        Assertions.assertTrue(out);
        //assign to the shift
        try{
            SF.Assignworkers(date2,type2,"G",tmprange2,dict2,null);
            out= true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out = false;
        }
        Assertions.assertTrue(out);
        //try to add transport
        try{
            SF.addTranporttoShift(date2,type2,"B",Branch.G,Role.DriverB);
            out = true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out = false;
        }
        Assertions.assertFalse(out);
        //delete shift from the DB and it's participants to a shift that is not assigned
        try {
            SF.removeshiftfrom(0,"13/06/2024",Branch.A);
            out = true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out= false;
        }
        Assertions.assertTrue(out);
        try {
            db.deleteParticipating("13/06/2024",0,"A");
            out = true;
        }
        catch (Exception e ){
            System.out.println(e.getMessage());
            out = false;
        }
        Assertions.assertFalse(out);
        //delete a shift and it's participants that is assigned
        try{
            SF.createShift(date,type,Branch.H,dict);
            SF.Assignworkers(date,type,"H", tmprange,dict,null);
            SF.removeshiftfrom( type,date, Branch.H);
            db.deleteParticipating(date, type,"H");
            out = true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            out = false;
        }
        Assertions.assertTrue(out);
    }
}
