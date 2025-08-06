package Service;

import Domain.*;
import com.google.gson.Gson;
import java.time.LocalDate;
import java.util.List;


public class WorkerService {

    //controller of worker
    private WorkerFaced WF = new WorkerFaced();


    //--add functions --//

    /**
     * the function gets all the data from the user to add constraint to a worker in a specific week,
     * and try to add it by sending it to the domain layer
     * if the adding was successful return success msg, else return error msg
     */
    public String saveConstarints(int id, String[] date, int[][] ans) {
        Response res;
        Gson gson = new Gson();
        try {
            WF.saveConstarints(id, date, ans);
            res = new Response<Integer>();
            res.setValuetosend(0);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            res =new Response();
            res.setErrormsg(e.getMessage());
            res.setValuetosend(null);
            return gson.toJson(res);
        }
    }


    /**
     * the function get all the data from the user about a new worker and try to add it by sending the domain layer
     * all the data. if the adding was successful return the success msg, else return an error msg
     */
    public String addWorker(int id, String fn, String ln, String ba, String role, String branch, List<Object> Cond) {
        Response res;
        Gson gson = new Gson();
        try {
            WF.WorkerRegistration( id, fn, ln, ba, role, branch, Cond);
            res = new Response();
            res.setValuetosend(0);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            res=new Response();
            res.setErrormsg(e.getMessage());
            res.setValuetosend(null);
            return gson.toJson(res);
        }
    }


    /** add a current worker to the previous worker table
     * the function get from the user the id of the worker to fire, and the date of the end of the contract,
     * the function send all the data to the domain layer. if the fire was successful send the success msg, else send an error msg
     */
    public String fireWorker(int id, String date) {
        Response res;
        Gson gson = new Gson();
        try{
            WF.WorkerFire(id,date);
            res = new Response();
            res.setValuetosend(0);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            res=new Response();
            res.setValuetosend(null);
            res.setErrormsg(e.getMessage());
            return gson.toJson(res);
        }
    }

    //--end of adding functions -- //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--update functions -- //


    /**
     * the function try to update the first name of the worker, if it was successful return the success msg
     * else return an error msg
     */
    public String updateFN(int id, String inputstring) {
        Response res;
        Gson gson = new Gson();
        try {
            WF.updateWorkerDetails(id, inputstring,1);
            res = new Response();
            res.setValuetosend(0);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            res=new Response();
            res.setValuetosend(null);
            res.setErrormsg(e.getMessage());
            return gson.toJson(res);
        }
    }

    /**
     * the function try to update the last name of the worker, if it was successful return the success msg
     * else return an error msg
     */
    public String updateLN(int id, String inputstring) {
        Response res;
        Gson gson = new Gson();
        try {
            WF.updateWorkerDetails(id, inputstring,2);
            res = new Response();
            res.setValuetosend(0);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            res=new Response();
            res.setValuetosend(null);
            res.setErrormsg(e.getMessage());
            return gson.toJson(res);
        }
    }

    /**
     * the function try to update the bank account of the worker, if it was successful return the success msg
     * else return an error msg
     */
    public String updateBA(int id, String inputstring) {
        Response res;
        Gson gson = new Gson();
        try {
            WF.updateWorkerDetails(id, inputstring,3);
            res = new Response();
            res.setValuetosend(0);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            res=new Response();
            res.setValuetosend(null);
            res.setErrormsg(e.getMessage());
            return gson.toJson(res);
        }
    }

    /**
     * the function update one of the options from the contract of the requested worker, send the data to the domain layer,
     * if the update succeeded return a success msg, else send an error msg
     * @param inputstring - the new data of the contract
     * @param input - the option to update: 1 - end date, 2 - salary, 3- days off
     * @return
     */
    public String updateContrcact(int id, Object inputstring, int input) {
        Response res;
        Gson gson = new Gson();
        try{
            WF.updateContractDetails(id,inputstring,input);
            res = new Response();
            res.setValuetosend(0);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            res=new Response();
            res.setValuetosend(null);
            res.setErrormsg(e.getMessage());
            return gson.toJson(res);
        }
    }

    /**
     * add a role to a worker
     */
    public String addNewRole(int id, String r) {
        Response res = new Response();
        Gson gson = new Gson();
        try{
            //add a role to the worker
            WF.addPerforworker(id, r);
            res.setValuetosend(0);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            res.setValuetosend(null);
            res.setErrormsg(e.getMessage());
            return gson.toJson(res);
        }
    }

    //--end of updating functions --//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--delete functions -- //

    /**
     *delete a role from a worker
     */
    public String delRole(int id, String delr) {
        Response res;
        Gson gson = new Gson();
        try{
            WF.delPerforworker(id,delr);
            //check if the worker has 0 roles
            boolean b = WF.hasRoles(id);
            //has more than one role, delete was ok
            if(!b){
            res = new Response();
            res.setErrormsg(null);
            res.setValuetosend(0);
            return gson.toJson(res);
            }
            //has 0 roles after delete one, needs to add a new role
            else{
                res=new Response();
                res.setValuetosend(1);
                res.setErrormsg(null);
                return gson.toJson(res);
            }
        }
        catch (Exception e){
            res=new Response();
            res.setValuetosend(null);
            res.setErrormsg(e.getMessage());
            return gson.toJson(res);
        }
    }

    /**
     * the function get an id of a previous worker to delete from the database, if succeeded return success msg, else error msg
     */
    public String deleteWorker(int id) {
        Response res=new Response();
        Gson gson = new Gson();
        try {
            WF.deleteWorkerfromDB(id);
            res.setValuetosend(0);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
        res.setValuetosend(null);
        res.setErrormsg(e.getMessage());
        return gson.toJson(res);
        }
    }

    //--end of deleting functions --//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--print functions -- //


    /**
     * the function get the id of the worker and try to print it by sending it to the domain layer,
     * if the print was successful the worker details will be printed, else an error msg
     */
    public String printWorkerDetails(int id) {
//        Response res;
        Gson gson = new Gson();
        try{
            //the print functions need to bring an
            Response<WorkerToSend> res = new Response<>();
            res.setValuetosend(WF.PrintWorkerdetailes(id));
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            Response res2 = new Response<>();
            res2.setErrormsg(e.getMessage());
            res2.setValuetosend(null);
            return gson.toJson(res2);
        }
    }

    /**
     * the function try to print the constraint of an employee in a specific week by sending the data to the domain layer
     * if the print was successful the constraint of the requested week will be printer, else an error msg
     *
     * @return
     */
    public String Printconstriant(int id, String startdate) {
        Response<AvailabilityToSend> res = new Response<>();
        Gson gson = new Gson();
        try{
            AvailabilityToSend out = WF.Printconstriant(id,startdate);
            res.setErrormsg(null);
            res.setValuetosend(out);
            return gson.toJson(res);
        }
        catch (Exception e){
            res.setValuetosend(null);
            res.setErrormsg(e.getMessage());
            return gson.toJson(res);
        }
    }




    //--end of printing functions --//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--calculate functions -- //

    /**
     * the function calculate the difference between to dates and returns it from the domain layer to the
     * presentation layer through this layer

     */
    public String CalculateDiffDay(String startdate, LocalDate today) {
        Response res;
        Gson gson = new Gson();
        int out;
        try {
            out = WF.CalculateDiffDay(startdate, today);
            res = new Response<Integer>();
            res.setErrormsg(null);
            res.setValuetosend(out);
            return gson.toJson(res);
        }
        catch (Exception e){
            res =new Response<>();
            res.setValuetosend(null);
            res.setErrormsg(e.getMessage());
            return gson.toJson(res);
        }
    }


    /**
     * the function calculate the salary for a worker and return it
     */
    public String CalculateSalary(int tmp) {
        Response res=new Response<Double>();
        Gson gson = new Gson();
        try {
            double out= WF.CalculateSalary(tmp);
            res.setErrormsg(null);
            res.setValuetosend(out);
            return gson.toJson(res);
        }
        catch (Exception e){
            res.setValuetosend(null);
            res.setErrormsg(e.getMessage());
            return gson.toJson(res);
        }
    }

    /**
     * the function transfer the salary of all the employees to a report that is ready to export
     */
    public void transferSalary() {
        Response res;
        Gson gson = new Gson();
        WF.transferSalary();
    }

    //--end of calculation functions --//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //--other functions--//

    /**
     * the function get an id from the user and send it to the domain layer to check if the id belongs to a worker form
     * the current workers or not
     * @param tmp
     * @return
     */
    public String isCurr(int tmp) {
        Response res;
        Gson gson = new Gson();
        try{
            boolean b = WF.isCurr(tmp);
            if(b){
                res = new Response<>();
                res.setErrormsg(null);
                res.setValuetosend(0);
                return gson.toJson(res);
            }
            else {
                res = new Response<>();
                res.setErrormsg("Can't find the worker");
                res.setValuetosend(null);
                return gson.toJson(res);
            }
        }
        catch (Exception e){
            res = new Response();
            res.setValuetosend(null);
            res.setErrormsg(e.getMessage());
            return gson.toJson(res);
        }
    }


    public String bringworkersbyenum(Role askfornewRole) {
        Response<List<WorkerToSend>> res= new Response<>();
        Gson gson = new Gson();
        try {
            List<WorkerToSend> w = WF.bringworkersbyenum(askfornewRole);
            if(!w.isEmpty()){
               res.setValuetosend(w);
               res.setErrormsg(null);
            }
            return gson.toJson(res);
        }
        catch (Exception e){
            res.setValuetosend(null);
            res.setErrormsg(e.getMessage());
            return gson.toJson(res);
        }
    }

    public String bringworkersbybranch(Branch a) {
        Response< List<WorkerToSend>> res = new Response<>();
        Gson gson = new Gson();
        try {
            List<WorkerToSend> map =WF.bringworkersbybranch(a);
            if(map!=null && !map.isEmpty()){
                res.setValuetosend(map);
                res.setErrormsg(null);
            }
            return gson.toJson(res);
        }
        catch (Exception e){
            res.setValuetosend(null);
            res.setErrormsg(e.getMessage());
            return gson.toJson(res);
        }
    }

    public String printPervWorker(int id) {
        Gson gson = new Gson();
        try{
            //the print functions need to bring an
            Response<WorkerToSend> res = new Response<>();
            res.setValuetosend(WF.PrintPrevWorkerdetailes(id));
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            Response res2 = new Response<>();
            res2.setErrormsg(e.getMessage());
            res2.setValuetosend(null);
            return gson.toJson(res2);
        }

    }
}
