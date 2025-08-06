package Data;

import java.util.ArrayList;
import java.util.List;

public class WorkerManager extends DAO {

    public WorkerManager(){
        super();
    }

    public List<String> readWorkerfromDBnoRoles(int id) throws Exception {
        ensureConnection();
        List<String>  ans = new ArrayList();
        ans = db.readWorkerfromDBWorker(conn, id);
        closeConnection();
        return ans;
    }
    public List<String> readWorkerfromDB(int id) throws Exception {
        ensureConnection();
        List<String> ans =  db.readWorkerfromDBWorker(conn, id);
        closeConnection();

        return ans;
    }

    public boolean updateContractDetails(int id, String startdate, String enddate, double salary, int dayoff) throws Exception {
        ensureConnection();
        boolean ans = db.updateContractDetails(conn, id, startdate, enddate, salary);
        closeConnection();
        return ans;
    }

    public boolean insertWorker(int id, String fn, String ln, String ba, String b, String startDate, String endDate, double hourlySalary, String r) throws Exception {
        ensureConnection();

        boolean ans= db.insertWorker(conn, id, fn, ln, ba, b, startDate, endDate, hourlySalary, r);
        closeConnection();
        return ans;
    }
    public boolean insertRoles2Worker(int id, String r) throws Exception {
        ensureConnection();

        boolean ans= db.insertRoles2Worker(conn, id, r);
        closeConnection();
        return ans;
    }

    public List<Integer> readWorkerByRole(String role) throws Exception {
        ensureConnection();
        List<Integer> ans = db.readWorkerByRole(conn, role);
        closeConnection();
        return ans;
    }
    public boolean deletePrevWorker(int id) throws Exception {
        ensureConnection();
        boolean ans  = db.deletePrevWorker(conn, id);
        closeConnection();
        return ans;
    }
    public boolean fireWorker(int id) throws Exception {
        ensureConnection();
        List<String> parm = readWorkerfromDBnoRoles(id);
        closeConnection();
        ensureConnection();
        boolean ans = db.fireWorker(conn, id,parm);
        closeConnection();
        return ans;
    }
    public boolean deleteRoleFromWorker(int workerID, String role) throws Exception {
        ensureConnection();
        boolean ans = db.deleteRoleFromWorker(conn, workerID, role);
        closeConnection();
        return ans;
    }
    public boolean insertToPrev(int workerID, String firstName, String lastName, String bankAccount, String startDate, String endDate, Double hourlySalary, String branch) throws Exception {
        ensureConnection();
        boolean ans=  db.insertToPrev(conn, workerID, firstName, lastName, bankAccount, startDate, endDate, hourlySalary, branch);
        closeConnection();
        return ans;
    }

    public List<String> readPrevWorkerFromDB(int id) throws Exception {
        ensureConnection();
        List<String> ans = db.readPrevWorkerFromDB(conn, id);
        closeConnection();
        return ans;
    }

    public List<Integer> readWorkerByBranch(String branch) throws Exception {
        ensureConnection();
        List<Integer> ans = db.readWorkerByBranch(conn, branch);
        closeConnection();
        return ans;
    }

    public boolean updateWorkerDetailsManager(int id, String firstName, String lastName, String bankAccount, String startDate, String endDate, double hourlySalary, String branch) throws Exception {
        ensureConnection();
        boolean ans = db.updateWorkerDetailsManager(conn, id, firstName, lastName, bankAccount, startDate, endDate, hourlySalary, branch);
        closeConnection();
        return ans;
    }

    public boolean insertWorkerRoles(int id, String trim) throws Exception {
        ensureConnection();
        boolean ans = db.insertRoles2Worker(conn,id,trim);
        closeConnection();;
        return ans;
    }

    public List<String> readWorkerfromDBRoles(int id) throws Exception {
        ensureConnection();
        List<String> ans = db.readWorkerfromDBRoles(conn,id);
        closeConnection();;
        return ans;
    }


    public boolean updateSalary(int workerID, double salary) throws Exception {
        ensureConnection();
        boolean ans = db.updateSalary(conn,workerID,salary);
        closeConnection();;
        return ans;
    }

    public double readSalary(int tmpID) throws Exception {
        ensureConnection();
        String sal = db.readSalary(conn,tmpID);
        double ans = Double.valueOf(sal);
        closeConnection();;
        return ans;
    }
}
