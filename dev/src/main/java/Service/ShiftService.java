package Service;

import Domain.*;
import Presentation.AssigningPres;
import com.google.gson.Gson;

import java.util.Map;

public class ShiftService {

    private ShiftFaced SF = new ShiftFaced();
    //--add functions--//

    /**
     * the function gets the data to create shift from the user and try to create it by sending the data to the domain layer
     * if the creation was successful send good message, else send error to the presentation layer
     */
    public String createShift(String date, int type, Branch b, Map<Role, Integer> map) {
        Response<String> res = new Response<>();
        Gson gson = new Gson();
        try{
            SF.createShift(date, type, b, map);
            res.setErrormsg(null);
            res.setValuetosend("Shift created successfully");
            return gson.toJson(res);
        }
        catch (Exception e){
           res.setValuetosend(null);
           res.setErrormsg(e.getMessage());
           return gson.toJson(res);
        }
    }

    /**
     * the function get all the data to add transport to a shift and try to do this by sending the data to the domain layer
     * if the adding was successful send good message, else send error to the presentation layer
     */
    public String addTranporttoShift(String date, int type, String truckkind, Branch b, Role r) {
        Response<Boolean> res = new Response<>();
        Gson gson = new Gson();
        try{
            boolean tmp = SF.addTranporttoShift(date, type, truckkind, b, r);
            res.setValuetosend(tmp);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            res.setErrormsg(e.getMessage());
            res.setValuetosend(null);
            return gson.toJson(res);
        }
    }


    //--end of addition functions--//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //--other functions--//

    /**
     * the function checks if a shift is in the database and return true or false based on it
     */
    public String isExistShift(String date, int type, Branch b) {
        Response<Boolean> res = new Response<>();
        Gson gson = new Gson();
        try {
            boolean tmp = SF.isExistShift(date, type, b);
            res.setValuetosend(tmp);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            res.setErrormsg(e.getMessage());
            res.setValuetosend(null);
            return gson.toJson(res);
        }
    }


    public String bringShift(String date, int type, Branch branch) {
        Response<ShiftToSend> res = new Response<>();
        Gson gson = new Gson();
        try {
            ShiftToSend s =SF.printShift(date,type,branch);
            if(s!=null){
                res.setValuetosend(s);
                res.setErrormsg(null);
                return gson.toJson(res);
            }
        }
        catch (Exception e){
            res.setErrormsg(e.getMessage());
            res.setValuetosend(null);
            return gson.toJson(res);
        }
        res.setErrormsg("shift not exist in the database, please try again");
        res.setValuetosend(null);
        return gson.toJson(res);
    }

    public String planshiftsforthefollowingweek(String[] daterange, int[] plan, Branch branch) {
        Response res = new Response<>();
        Gson gson = new Gson();
        try {
            SF.planshiftsforthefollowingweek(daterange, plan, branch);
            res.setValuetosend(0);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            res.setErrormsg(e.getMessage());
            res.setValuetosend(null);
            return gson.toJson(res);
        }
    }

    public String bringAssigningPres(String date,Branch b) {
        Response<AssigningPres> res = new Response<>();
        Gson gson = new Gson();
        try {
            AssigningPres as = SF.bringAssigningPres(date, b);
            if(as!=null) {
                res.setValuetosend(as);
                res.setErrormsg(null);
                return gson.toJson(res);
            }
            else {
                res.setErrormsg("week plan is not in the database, please try again");
                res.setValuetosend(null);
                return gson.toJson(res);
            }
        }
        catch (Exception e){
            res.setErrormsg(e.getMessage());
            res.setValuetosend(null);
            return gson.toJson(res);
        }
    }

    public String updateassignpresforweek(String startDateRange, int[] plannedbymanager,Branch branch){
        Response<Boolean> res = new Response<>();
        Gson gson = new Gson();
        try {
            boolean b = SF.updateassignpres(startDateRange, plannedbymanager,branch);
            res.setValuetosend(b);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            res.setErrormsg(e.getMessage());
            res.setValuetosend(null);
            return gson.toJson(res);
        }
    }

    public String Assignworkers(String date, int type, String b, String[] weekrange, Map<Role, Integer> counter,Role driver) {
        Response res = new Response<>();
        Gson gson = new Gson();
        try {
            boolean bool = SF.Assignworkers(date, type, b , weekrange,counter,driver);
            res.setValuetosend(bool);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            res.setErrormsg(e.getMessage());
            res.setValuetosend(null);
            return gson.toJson(res);
        }
    }

    /**
     * the function gets the data of a shift and a specific role and assign one worker with the requested role to the shift
     * @param date
     * @param type
     * @param b
     * @param role
     */
    public String AssignOneWorker(String date, int type, String b, Role role,String[] weekrange) {
        Response res = new Response<>();
        Gson gson = new Gson();
        try {
            boolean bool = SF.AssignOne(date, type, b , role,weekrange);
            res.setValuetosend(bool);
            res.setErrormsg(null);
            return gson.toJson(res);
        }
        catch (Exception e){
            res.setErrormsg(e.getMessage());
            res.setValuetosend(null);
            return gson.toJson(res);
        }

    }
}
