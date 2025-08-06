package Data;

import Domain.Branch;
import java.util.List;
import java.util.Map;

public class ShiftManager extends DAO{
    public List<String> readShiftbyDTB(String date, int type, String branch) throws Exception {
        ensureConnection();
        List<String> ans =  db.readShiftbyDTB(conn, date, type, branch);
        closeConnection();
        return ans;
    }

    public boolean insertShift(int id, String date, String b, int type, Map<String, Integer> counter) throws Exception {
        ensureConnection();
        boolean ans = db.insertShift(conn, id, date, b, type, counter);
        closeConnection();
        return ans;
    }

    public boolean insertAvailability(int id, int[] morning, int[] evening, String start, String end) throws Exception {
        ensureConnection();
        boolean ans =  db.insertAvailability(conn, id, morning, evening, start, end);
        closeConnection();
        return ans;
    }

    public boolean insertParticipatesWorkersInShift(String date,int type, int id, String r,String branch) throws Exception {
        ensureConnection();
        boolean ans = db.insertParticipatesWorkersInShift(conn, date,type, id, r,branch);
        closeConnection();
        return ans;
    }

    public boolean deleteShift(String date, int type, String branch) throws Exception {
        ensureConnection();
        boolean ans=  db.deleteShift(conn, date, type, branch);
        closeConnection();
        return ans;
    }
    public boolean updateShiftCounterRoles(int type, String date, String branch, Integer cashier, Integer manager, Integer storeKeeper, Integer deliveryPerson, Integer driverB, Integer driverC, Integer driverD) throws Exception {
        ensureConnection();
        boolean ans = db.updateShiftCounterRoles(conn, type, date, branch, cashier, manager, storeKeeper, deliveryPerson, driverB, driverC, driverD);
        closeConnection();
        return ans;
    }

    public List<String> readAvailability(int id, String date) throws Exception {
        ensureConnection();
        List<String> ans = db.readAvailability(conn, id, date);
        closeConnection();
        return ans;
    }

    public List<List<String>> readassignWorkersperShift(String date, int idW, String b) throws Exception {
        ensureConnection();
        List<List<String>> ans = db.readassignWorkersperShift(conn,date,idW,b);
        closeConnection();
        return ans;
    }

    public boolean insertTransport(int idS,String date, String branch, int type, int workerID, String truck) throws Exception {
        ensureConnection();
        boolean ans = db.insertTransport(conn,idS,date, branch,type,workerID,truck);
        closeConnection();
        return ans;
    }

    public List<String> readTransport(int type,String branch, String date) throws Exception {
        ensureConnection();
        List<String> ans = db.readTransport(conn,type,branch,date);
        closeConnection();
        return ans;
    }


    public boolean deleteParticipating(String date, int type, String b) throws Exception {
        ensureConnection();
        boolean ans = db.deleteParticipating(conn,date,type,b);
        closeConnection();
        return ans;
    }

    public boolean deleteTransport(int type, String date, String branch) throws  Exception{
        ensureConnection();
        boolean ans = db.deleteTransport(conn,type ,date,branch);
        closeConnection();
        return ans;
    }

    public boolean deleteAvail(int workerID, String date) throws Exception {
        ensureConnection();
        boolean ans = db.deleteAvail(conn,workerID ,date);
        closeConnection();
        return ans;
    }

    public int IDcounter() throws Exception{
        ensureConnection();
        int ans = db.IDcounter(conn);
        closeConnection();
        return ans;
    }

    public boolean saveCounter(int counter) throws Exception{
        ensureConnection();
        boolean ans = db.saveCounter(conn,counter);
        closeConnection();
        return ans;
    }


}
