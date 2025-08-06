
import Data.SQLDB;
import Domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class WorkerTest {

    private WorkerFaced WF = new WorkerFaced();
    private DBoperations db = DBoperations.GetDBInstance();


    @Test
    public void testreadfromDB(){
        //1
        //read worker from the DB
        Worker w = WF.GetworkerbyID(170);
        assertNull(w);

        Contract c = new Contract("10/05/2024", "10/05/2025", 20.00);
        Worker w1 = new Worker(170,"test","test","1234",Role.Cashier,Branch.B,c);
        try {
            db.saveworkertoDB(w1);
        }
        catch (Exception e){
            System.out.println( e.getMessage());
        }
        assertTrue(WF.GetworkerbyID(170).getWorkerID()==170);
    }

    @Test
    public void testAddNewWorkerWithUniqueID() {
        //2
        //add worker with a unique ID
        Contract c = new Contract("10/05/2024", "10/05/2025", 20.00);
        Worker worker1 = new Worker(207, "test1", "test1", "123456789", Role.Cashier,Branch.H,c);
        int size0 = db.getAllCurrentWorkers().size();
        List<Object> cond = new ArrayList<>();
        cond.add(c.getStartDateRange());
        cond.add(c.getEndDateRange());
        cond.add(c.getHourlySalary());
        try {
            boolean b = db.saveworkertoDB(worker1);
            assertTrue(b);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        int size1 = db.getAllCurrentWorkers().size();
        assertEquals(size0+1,size1);
    }

    @Test
    public void testAddNewWorkerWithDuplicatedID() {
        //3
        // adding with duplicated ID
        Contract c = new Contract("10/05/2024", "10/05/2025", 20.00);
        Worker worker1 = new Worker(300, "test1", "test1", "123456789", Role.Cashier,Branch.G,c);
        Worker worker2 = new Worker(300, "test2", "test2", "12345", Role.Cashier,Branch.I,c);
        int size0 = db.getAllCurrentWorkers().size();
        boolean result=false;
        try {
            result = db.saveworkertoDB(worker1);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        assertTrue(result);
        int size1 =db.getAllCurrentWorkers().size();
        assertEquals(size0 + 1, size1);
        try {
            result = db.saveworkertoDB(worker2);
        }
        catch (Exception e){
            result = false;
            System.out.println(e.getMessage());
        }
        int size2 = db.getAllCurrentWorkers().size();
        Assertions.assertFalse(result);
        assertEquals(size1, size2);
    }

    @Test
    public void testAddNewWorkerWithnoID() {
        //4
        boolean result=false;
        Contract c = new Contract("10/05/2024", "10/05/2025", 20.00);
        Worker worker1 = new Worker(0, "test1", "test1", "123456789", Role.Cashier,Branch.B,c);
        int size=db.getAllCurrentWorkers().size();
        try {
            result = db.saveworkertoDB(worker1);
        }
        catch (Exception e){

        }
        assertFalse(result);
        int size0 =db.getAllCurrentWorkers().size();
        assertEquals(size0,size);
    }


    @Test
    public void testUpdatePersonalDetailsExistWorker(){
        //5
        //update firstName, Lastname, BankAccount
        Contract c = new Contract("10/05/2024", "10/05/2025", 20.00);
        Worker w1 = new Worker(800,"test","test","12345678", Role.Delivery_Person,Branch.H,c);
        try {
            db.saveworkertoDB(w1);
            WF.updateWorkerDetails(800,"TEST",1);
            WF.updateWorkerDetails(800,"TEST2",2);
            WF.updateWorkerDetails(800,"555",3);
        }
        catch (Exception e){
        }
        Worker w2 = WF.GetworkerbyID(800);
        Assertions.assertEquals(w2.getWorkerID(),800);
        Assertions.assertEquals(w2.getFirstName(),"TEST");
        Assertions.assertEquals(w2.getLastName(),"TEST2");
        Assertions.assertEquals(w2.getBankAccount(),"555");
    }

    @Test
    public void testUpdatePersonalDetailsNOTExistWorker(){
        //6
        //update firstName, Lastname, BankAccount to not exist worker
        try {
            WF.updateWorkerDetails(8005,"TEST",1);
            WF.updateWorkerDetails(8005,"TEST2",2);
            WF.updateWorkerDetails(8005,"555",3);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        Worker w2 = WF.GetworkerbyID(8005);
        Assertions.assertNull(w2);
    }

    @Test
    public void testUpdateContractDetailsforExistWorker(){
        //7
        //update contract for exist worker
        Contract c = new Contract("10/05/2024", "10/05/2025", 20.00);
        Worker w1 = new Worker(456,"test","test","12345678", Role.Delivery_Person,Branch.D,c);
        try {
            db.saveworkertoDB(w1);
            WF.updateContractDetails(456,"28/06/2024",1);
            WF.updateContractDetails(456,50,2);
            WF.updateContractDetails(456,40,3);
        }
        catch (Exception e){

        }
        Worker w3= WF.GetworkerbyID(456);
        Assertions.assertEquals(w3.getWorkerContract().getHourlySalary(),50.0);
        Assertions.assertEquals(w3.getWorkerContract().getEndDateRange(),"28/06/2024");
    }

    @Test
    public void testUpdateContractDetailsfornotExistWorker(){
        //8
        //update contract for not exist worker
        Contract c = new Contract("10/05/2024", "10/05/2025", 20.00);
        Worker w1 = new Worker(5002,"test","test","12345678", Role.Delivery_Person,Branch.D,c);
        boolean b;
        try {
            WF.updateContractDetails(5002,"28/06/2024",1);
            WF.updateContractDetails(5002,50,2);
            WF.updateContractDetails(5002,40,3);
            b = true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            b=false;
        }
        Assertions.assertFalse(b);
    }


    @Test
    public void testUpdatePermissionAdd(){
        //9
        //add role to exist worker
        Contract c = new Contract("10/05/2024", "10/05/2025", 20.00);
        Worker w1 = new Worker(358,"TEST","TEST","12121", Role.Cashier,Branch.C,c);
        boolean result = false;
        try {
            db.saveworkertoDB(w1);
            db.saveWorkersRolestoDB(w1);
            Worker w2 = WF.GetworkerbyID(w1.getWorkerID());
            result =w2.getRole().contains(Role.Cashier);
            assertTrue(result);
        }
        catch (Exception e){
        }
        int size = WF.GetworkerbyID(w1.getWorkerID()).getRole().size();
        try {
            WF.addPerforworker(w1.getWorkerID(), "Manager");
            result = true;
        }
        catch (Exception e){
            result = false;
        }
        int size1 = WF.GetworkerbyID(w1.getWorkerID()).getRole().size();
        assertEquals(size+1,size1);
        assertTrue(result);
        result = WF.GetworkerbyID(w1.getWorkerID()).getRole().contains(Role.Manager);
        assertTrue(result);
    }

    @Test
    public void testUpdatePermissionDel(){
        //10
        //delete role for exist worker
        Contract c = new Contract("10/05/2024", "10/05/2025", 20.00);
        Worker w1 = new Worker(804,"TEST","TEST","12121", Role.Cashier,Branch.H,c);
        boolean b2;
        try{
            b2=db.saveworkertoDB(w1);
            db.saveWorkersRolestoDB(w1);
            assertTrue(b2);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        Worker w2 = WF.GetworkerbyID(w1.getWorkerID());
        b2 =w2.getRole().contains(Role.Cashier);
        assertTrue(b2);
        int size = WF.GetworkerbyID(w1.getWorkerID()).getRole().size();
        try {
            WF.delPerforworker(w1.getWorkerID(), "Manager");
            b2=true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            b2=false;
        }
        assertFalse(b2);
        int size1= WF.GetworkerbyID(w1.getWorkerID()).getRole().size();
        assertEquals(size,size1);
        try {
            WF.addPerforworker(w1.getWorkerID(),"Store_Keeper");
            WF.delPerforworker(w1.getWorkerID(), "Cashier");
            b2 = true;
        }
        catch (Exception e){
            b2 = false;
            System.out.println(e.getMessage());
        }
        assertTrue(b2);
        int size2= WF.GetworkerbyID(w1.getWorkerID()).getRole().size();
        assertEquals(size2,1);
        try {
            WF.addPerforworker(w1.getWorkerID(), "Manager");
            b2= true;
        }
        catch (Exception e){
            b2=false;
            System.out.println(e.getMessage());
        }
        int size3 = WF.GetworkerbyID(w1.getWorkerID()).getRole().size();
        assertTrue(b2);
        assertEquals(size3-1,size2);

    }


    @Test
    public void testisCurr(){
        //11
        //test if the id of a worker is in the curr workers, for exist and not exist and for a prev worker
        boolean result1=true;
        try {
             result1= WF.isCurr(12221);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        assertFalse(result1);
        try {
            result1 = WF.isCurr(75);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        assertTrue(result1);
        try {
            result1 = WF.isCurr(131);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        Assertions.assertFalse(result1);
    }

        @Test
    public void testFireWorkernotinDB() {
        //12
        //deletion a worker with ID not in the system
        boolean res=false;
        boolean res2 = true;
        try {
           res = WF.WorkerFire(15,"30/06/2024");
           res2 = WF.WorkerFire(124,"30/06/2024");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            res2 = false;
        }
        Assertions.assertTrue(res);
        Assertions.assertFalse(res2);
    }


//    @Test
//    public  void testPrevWorker(){
//        Worker w = WF.GetworkerbyID(10);
//        Assertions.assertNull(w);
//        assertEquals(db.getPrevWorkers().size(),0);
//        Contract c = new Contract("10/05/2024", "10/05/2025", 20.00);
//        Worker worker1 = new Worker(109, "test1", "test1", "123456789", Role.Cashier,Branch.G,c);
//        try {
//            db.saveworkertoDB(worker1);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        try {
//            WF.WorkerFire(109,"28/06/2024");
//        }
//        catch (Exception e){
//
//        }
//        System.out.println(db.getPrevWorkers().size());
//        List<Worker> prev = db.getPrevWorkers();
//        assertEquals(db.getPrevWorkers().size(),1);
//    }



//
//    @Test
//    public void testBringWorkersbyEnum(){
//        Contract c = new Contract("10/05/2024", "10/05/2025", 20.00);
//        Worker w1 = new Worker(1,"TEST","TEST","12121", Role.Cashier,Branch.H,c);
//        Worker w2 = new Worker(231,"TEST","TEST","12121", Role.StoreKeeper,Branch.G,c);
//        Worker w3 = new Worker(950,"TEST","TEST","12121", Role.Cashier,Branch.C,c);
//        WF.WorkerRegistration(w1);
//        WF.WorkerRegistration(w2);
//        WF.WorkerRegistration(w3);
//        List<Worker> list = WF.bringworkersbyenum(Role.Cashier);
//        int size = list.size();
//        assertEquals(size,2);
//        list = WF.bringworkersbyenum(Role.StoreKeeper);
//        assertEquals(list.size(),1);
//        list = WF.bringworkersbyenum(Role.Manager);
//        assertEquals(list.size(),0);
//    }




}
